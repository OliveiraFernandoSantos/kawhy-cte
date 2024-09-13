package br.com.actionsys.kawhycte.infra.control;

import br.com.actionsys.kawhycommons.Constants;
import br.com.actionsys.kawhycommons.infra.util.APathUtil;
import br.com.actionsys.kawhycommons.infra.util.DateUtil;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import br.com.actionsys.kawhycommons.types.KawhyType;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static br.com.actionsys.kawhycommons.infra.util.APathUtil.execute;
import static br.com.actionsys.kawhycommons.infra.util.APathUtil.executeOrNull;

@Slf4j
@Service
public class ControlService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${dir.saida}")
    private String outputFolder;

    public void insert(IntegrationContext item, String cnpj) {
        Document d = item.getDocument();
        Map<String, Object> v = new HashMap<>();

        v.put("ccq72c100", item.getId());//cte_id
        v.put("ccq72c108", execute(d, "cteProc/CTe/infCte/ide/nCT"));//nct
        v.put("ccq72c204", execute(d, "cteProc/CTe/infCte/ide/serie"));//serie

        v.put("ccq72c154", executeOrNull(d, "cteProc/CTe/infCte/emit/CNPJ"));//cnpj_emit
        v.put("ccq72c120", execute(d, "cteProc/CTe/infCte/emit/enderEmit/UF"));//uf_emit
        v.put("ccq72c119", execute(d, "cteProc/CTe/infCte/emit/enderEmit/xMun"));//xmun_emit

        v.put("ccq72c329", cnpj);//cnpj_dest
        v.put("ccq72c331", executeOrNull(d, "cteProc/CTe/infCte/dest/CPF"));//cpf_dest
        v.put("ccq72c332", execute(d, "cteProc/CTe/infCte/dest/enderDest/xMun"));//xmun_dest
        v.put("ccq72c330", execute(d, "cteProc/CTe/infCte/dest/enderDest/UF"));//uf_dest

        v.put("ccq72c200", execute(d, "cteProc/CTe/infCte/ide/dhEmi").substring(0, 10));//demi
        v.put("ccq72c307", execute(d, "cteProc/CTe/infCte/ide/dhEmi").substring(11, 19));//hemi
        v.put("ccq72c132", new BigDecimal(execute(d, "cteProc/CTe/infCte/vPrest/vTPrest")));//vtprest
        v.put("ccq72c319", new BigDecimal(execute(d, "cteProc/CTe/infCte/infCTeNorm/infCarga/vCarga")));//vcarga
        v.put("ccq72c294", execute(d, "cteProc/protCTe/infProt/cStat"));//cstatussefaz
        v.put("ccq72c333", execute(d, "cteProc/protCTe/infProt/xMotivo"));//xmotivo
        v.put("ccq72c334", execute(d, "cteProc/protCTe/infProt/nProt"));//protocolo

        Date dateNow = new Date();
        v.put("ccq72c295", DateUtil.formatDateToDb(dateNow));//dtconsultasefaz
        v.put("ccq72c296", DateUtil.formatTimeToDb(dateNow));//hrconsultasefaz

        v.put("ccq72c306", outputFolder + item.getFileName());//arquivo
        v.put("ccq72c336", Constants.XML);//statuscte

        v.put("ccq72c299", DateUtil.formatDateToDb(dateNow));//dtentrada
        v.put("ccq72c300", DateUtil.formatTimeToDb(dateNow));//hrentrada

        v.put("ccq72c340", Constants.SPACE);//cstatus01
        v.put("ccq72c341", Constants.SPACE);//cstatus02
        v.put("ccq72c342", execute(d, "cteProc/CTe/infCte/@versao"));//cstatus03
        v.put("ccq72c343", Constants.SPACE);//cstatus04

        v.put("ccq72c344", KawhyType.KAWHY_CTE.getServiceName());//audit_usuario
        v.put("ccq72c345", KawhyType.KAWHY_CTE.getServiceName());//audit_programa
        v.put("ccq72c346", DateUtil.formatDateToDb(dateNow));//audit_data
        v.put("ccq72c347", DateUtil.formatTimeToDb(dateNow));//audit_hora

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.setTableName("fq72c335");//KWCTE_CONTROLE
        simpleJdbcInsert.execute(v);
    }

    public void updateToCancel(IntegrationContext item) {
        //                  KWCTE_CONTROLE
        String sql = "UPDATE fq72c335 set "
                // STATUSCTE       cstatussefaz   XMOTIVO       AUDIT_USUARIO  AUDIT_PROGRAMA
                + "ccq72c336 = ?, ccq72c294 = ?, ccq72c333 = ?, ccq72c344 = ?, ccq72c345 = ?, "
                //AUDIT_DATA      AUDIT_HORA     DTCONSULTASEFAZ  HRCONSULTASEFAZ
                + "ccq72c346 = ?, ccq72c347 = ?, ccq72c295 = ?, ccq72c296 = ? "
                //        CTE_ID
                + " where ccq72c100 Like ?";

        String dhRecbto = execute(item.getDocument(), "procEventoCTe/eventoCTe/infEvento/dhEvento");
        Date now = new Date();

        int r = jdbcTemplate.update(sql, Constants.DESCRIPTION_CANCEL_DOCUMENT
                , Constants.CSTATUS_SEFAZ_CACELAMENTO
                , Constants.CTE_XMOTIVO
                , KawhyType.KAWHY_CTE.getServiceName()
                , KawhyType.KAWHY_CTE.getServiceName()
                , DateUtil.formatDateToDb(now)
                , DateUtil.formatTimeToDb(now)
                , DateUtil.formatDateSefazToDb(dhRecbto)
                , DateUtil.formatTimeSefazToDb(dhRecbto)
                , item.getId() + "%"
        );

      if (r > 1) {
            log.warn("Cancelou " + r + " registro na tabela fq72c335 (controle) para chave " + item.getId());
        }
    }

    public boolean hasCteControle(String id) {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select 1 from fq72c335 where ccq72c100 Like ?", id + "%");

        return !result.isEmpty();
    }

    public String findCnpjTomador(IntegrationContext item) {
        String valorTomador = APathUtil.execute(item.getDocument(), "cteProc/CTe/infCte/ide/toma3/toma");

        // Achamos um caso em que na versão 2.0 do NFe em que a tag do tomador mudou
        if (StringUtils.isBlank(valorTomador)) {
            valorTomador = APathUtil.execute(item.getDocument(), "cteProc/CTe/infCte/ide/toma03/toma");
        }

        String cnpjCliente;
        if (TomadorType.TOMA_REM.getKey().equals(valorTomador)) {
            cnpjCliente = APathUtil.execute(item.getDocument(), TomadorType.TOMA_REM.getAPath());
        } else if (TomadorType.TOMA_EXPED.getKey().equals(valorTomador)) {
            cnpjCliente = APathUtil.execute(item.getDocument(), TomadorType.TOMA_EXPED.getAPath());
        } else if (TomadorType.TOMA_RECEB.getKey().equals(valorTomador)) {
            cnpjCliente = APathUtil.execute(item.getDocument(), TomadorType.TOMA_RECEB.getAPath());
        } else if (TomadorType.TOMA_DEST.getKey().equals(valorTomador)) {
            cnpjCliente = APathUtil.execute(item.getDocument(), TomadorType.TOMA_DEST.getAPath());
        } else {
            cnpjCliente = APathUtil.execute(item.getDocument(), TomadorType.TOMA_OUTROS.getAPath());
        }
        return cnpjCliente;
    }
}
