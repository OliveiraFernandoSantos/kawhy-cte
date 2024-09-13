package br.com.actionsys.kawhycte;

import br.com.actionsys.kawhycommons.Constants;
import br.com.actionsys.kawhycommons.infra.exception.DuplicateFileException;
import br.com.actionsys.kawhycommons.infra.exception.IgnoreFileException;
import br.com.actionsys.kawhycommons.infra.exception.OtherFileException;
import br.com.actionsys.kawhycommons.infra.util.APathUtil;
import br.com.actionsys.kawhycommons.infra.util.DocumentUtil;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import br.com.actionsys.kawhycommons.integration.IntegrationOrchestrator;
import br.com.actionsys.kawhycommons.types.DocumentType;
import br.com.actionsys.kawhycommons.types.KawhyType;
import br.com.actionsys.kawhycte.infra.control.ControlService;
import br.com.actionsys.kawhycte.item.cancel.CancelService;
import br.com.actionsys.kawhyimport.metadata.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Orchestrator extends IntegrationOrchestrator {

    @Autowired
    private ControlService controlService;
    @Autowired
    private CancelService cancelService;
    @Autowired
    private ImportService importService;

    @Override
    public void processDocumentFile(IntegrationContext context) throws Exception {
        context.setId(context.aPath("cteProc/CTe/infCte/@Id"));

        String cnpj = controlService.findCnpjTomador(context);
        if (!licenseService.validateCnpj(cnpj)) {
            throw new OtherFileException("CNPJ sem licença : " + cnpj + " - " + context.getFile().getAbsolutePath());
        }

        if (controlService.hasCteControle(context.getId())) {
            throw new DuplicateFileException();
        }

        importService.process(context);

        //Salvar informções do CTe na Tabela fq72317/kwcte_controle
        controlService.insert(context, cnpj);
    }

    @Override
    public void processCancelFile(IntegrationContext context) throws Exception { //implementar processarCancel
        context.setId(DocumentType.CTE.getPrefix() + context.aPath("procEventoCTe/eventoCTe/infEvento/chCTe"));

        if (!controlService.hasCteControle(context.getId())) {
            throw new IgnoreFileException("Evento de cancelamento aguardando documento fiscal : " + context.getFile().getAbsolutePath());
        }

        //Atualizar informções do CTe CANCELADO na Tabela fq72317/kwcte_controle
        controlService.updateToCancel(context);

        //Salvar informções do CTe na Tabela fq72c336/kwcte_canceladas
        cancelService.insert(context);
    }

    @Override
    public boolean isCancel(IntegrationContext context) {
        String tpEvento = context.aPath("procEventoCTe/eventoCTe/infEvento/tpEvento");
        return Constants.CTE_DELIVERY_SEFAZ_EVENTO_TP_CANCELAMENTO.equalsIgnoreCase(StringUtils.trim(tpEvento));
    }

    @Override
    public boolean isDocument(IntegrationContext context) {
        return DocumentUtil.isCte(context.getDocument());
    }

    @Override
    public KawhyType getKawhyType() {
        return KawhyType.KAWHY_CTE;
    }
}
