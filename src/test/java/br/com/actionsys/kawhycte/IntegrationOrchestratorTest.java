package br.com.actionsys.kawhycte;

import br.com.actionsys.kawhycommons.infra.exception.DuplicateFileException;
import br.com.actionsys.kawhycommons.infra.exception.IgnoreFileException;
import br.com.actionsys.kawhycommons.infra.exception.OtherFileException;
import br.com.actionsys.kawhycommons.infra.license.LicenseService;
import br.com.actionsys.kawhycommons.infra.util.FilesUtil;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Slf4j
@MockBean(Schedule.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class IntegrationOrchestratorTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Orchestrator orchestrator;

    @SpyBean
    private LicenseService licenseService;

    @Value("/xml/ProcEvento_35180605048234000122570010001563431005252092_110111_1.xml")
    private Resource resourceCancelEvent;
    @Value("/xml/ProcEvento_35180605048234000122570010001563431005252092_99999.xml")
    private Resource resourceOtherEvent;
    @Value("/xml/CTe35180605048234000122570010001563431005252092.xml")
    private Resource resourceDocument;
    @Value("/xml/CTe35180605048234000122570010001563431005252092.txt")
    private Resource resourceTxt;
    @Value("/xml/CTe35231105798428000145570010000496001644803310.xml")
    private Resource resourceTomadorRemetente;
    @Value("/xml/CTe35231105798428000145570010000496001644803311.xml")
    private Resource resourceTomadorExpedidor;
    @Value("/xml/CTe35231105798428000145570010000496001644803312.xml")
    private Resource resourceTomadorRecebedor;
    @Value("/xml/CTe35231105798428000145570010000496001644803313.xml")
    private Resource resourceTomadorDestinatario;
    @Value("/xml/CTe35231105798428000145570010000496001644803314.xml")
    private Resource resourceTomadorOutros;
    @Value("xml/CTe35231105798428000145570010000496001644803315_caracter_especial.xml")
    private Resource resourceCaracterEspecial;
    @Value("xml/CTe35231105798428000145570010000496001644803316_charset.xml")
    private Resource resourceCharset;

    @BeforeEach
    public void before() {
        Mockito.doReturn(true).when(licenseService).validateCnpj(Mockito.any());
        Mockito.doReturn(true).when(licenseService).validateCnpjs(Mockito.any());
    }

    @Test
    public void isCancelTest() throws Exception {
        Assertions.assertTrue(orchestrator.isCancel(new IntegrationContext(resourceCancelEvent.getFile())));
        Assertions.assertFalse(orchestrator.isCancel(new IntegrationContext(resourceDocument.getFile())));
        Assertions.assertFalse(orchestrator.isCancel(new IntegrationContext(resourceOtherEvent.getFile())));
    }

    @Test
    public void isDocumentTest() throws Exception {
        Assertions.assertFalse(orchestrator.isDocument(new IntegrationContext(resourceCancelEvent.getFile())));
        Assertions.assertTrue(orchestrator.isDocument(new IntegrationContext(resourceDocument.getFile())));
        Assertions.assertFalse(orchestrator.isDocument(new IntegrationContext(resourceOtherEvent.getFile())));
    }

//    @Test
//    public void isEventTest() throws Exception {
//        Assertions.assertEquals(true, orchestrator.isEvent(new IntegrationItem(resourceCancelEvent.getFile())));
//        Assertions.assertEquals(false, orchestrator.isEvent(new IntegrationItem(resourceDocument.getFile())));
//        Assertions.assertEquals(true, orchestrator.isEvent(new IntegrationItem(resourceOtherEvent.getFile())));
//    }

    @Test
    public void processItemTest() throws Exception {

        //Validar arquivo .txt
        Assertions.assertThrows(OtherFileException.class, () -> orchestrator.processItem(new IntegrationContext(resourceTxt.getFile())),
                "Extensão do arquivo inválida." + resourceTxt.getFile().getAbsolutePath());

        IntegrationContext itemTxt = IntegrationContext.builder().file(resourceTxt.getFile().getParentFile()).build();
        Assertions.assertThrowsExactly(IgnoreFileException.class, () -> orchestrator.processItem(itemTxt), "Pasta não foi identificado");

        
    }

    @Test
    @Sql({"/data.sql"})
    public void processDocumentTest() throws Exception {
        IntegrationContext itemDoc = new IntegrationContext(resourceDocument.getFile());

        Assertions.assertDoesNotThrow(() -> orchestrator.processDocumentFile(itemDoc));

        //Caso o mesmo documento seja enviado novamente
        Assertions.assertThrows(DuplicateFileException.class, () -> orchestrator.processDocumentFile(itemDoc),
                "Chave duplicada no banco de dados: " + itemDoc.getId());

        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select ccq72c294 from fq72c335 where ccq72c100 like ?", itemDoc.getId().trim() + "%"); //cte_id
        Map<String, Object> row = result.get(0);

        assertNotNull(result, "Não foi encontrado registro na tabela kwcte_controle");

        //Nota aprovada, o status deve ser '100'
        Assertions.assertEquals("100", row.get("ccq72c294").toString().trim());
    }

    @Test
    @Sql({"/data.sql"})
    public void processDocumentWithSpecialCaracterTest() throws Exception {
        IntegrationContext itemDoc = new IntegrationContext(resourceCaracterEspecial.getFile());

        Assertions.assertDoesNotThrow(() -> orchestrator.processDocumentFile(itemDoc));


        Assertions.assertThrows(DuplicateFileException.class, () -> orchestrator.processDocumentFile(itemDoc),
                "Chave duplicada no banco de dados: " + itemDoc.getId());

        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select ccq72c294 from fq72c335 where ccq72c100 like ?", itemDoc.getId().trim() + "%"); //cte_id
        Map<String, Object> row = result.get(0);

        assertNotNull(result, "Não foi encontrado registro na tabela kwcte_controle");


        Assertions.assertEquals("100", row.get("ccq72c294").toString().trim());
    }

    @Test
    @Sql({"/data.sql"})
    public void processDocumentWithCharsetTest() throws Exception {
        IntegrationContext itemDoc = new IntegrationContext(resourceCharset.getFile());

        Assertions.assertDoesNotThrow(() -> orchestrator.processDocumentFile(itemDoc));

        //Caso o mesmo documento seja enviado novamente
        Assertions.assertThrows(DuplicateFileException.class, () -> orchestrator.processDocumentFile(itemDoc),
                "Chave duplicada no banco de dados: " + itemDoc.getId());

        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select ccq72c294 from fq72c335 where ccq72c100 like ?", itemDoc.getId().trim() + "%"); //cte_id
        Map<String, Object> row = result.get(0);

        assertNotNull(result, "Não foi encontrado registro na tabela kwcte_controle");

        //Nota aprovada, o status deve ser '100'
        Assertions.assertEquals("100", row.get("ccq72c294").toString().trim());
    }

    @Test
    @Sql({"/data.sql"})
    public void processCancelFileTest() throws Exception {
        IntegrationContext itemCancel = new IntegrationContext(resourceCancelEvent.getFile());
        //Ainda não chegou o documento fiscal.
        Assertions.assertThrows(IgnoreFileException.class, () -> orchestrator.processCancelFile(itemCancel));

        IntegrationContext itemDoc = new IntegrationContext(resourceDocument.getFile());
        //Inserir o documento fiscal no banco de dados
        Assertions.assertDoesNotThrow(() -> orchestrator.processDocumentFile(itemDoc));

        //Após receber o documento fiscal, processa o cancelamento
        Assertions.assertDoesNotThrow(() -> orchestrator.processCancelFile(itemCancel));

        //Valida se o item foi cancelado no banco de dados.
        Map<String, Object> resultControle = jdbcTemplate.queryForMap("select ccq72c294 from fq72c335 where ccq72c100 like ?", itemDoc.getId().trim() + "%");
        Assertions.assertEquals(resultControle.get("ccq72c294"), "101");

        
    }

    @Test
    @Sql({"/data.sql"})
    public void recuperarCnpjClienteRemetenteTest() throws Exception {
        

        // Nesse caso temos um cte que o cliente do gra naõ é o destinatário do cte,tag <toma>0</toma> 0-Remetente
        IntegrationContext integrationContext = new IntegrationContext(resourceTomadorRemetente.getFile());

        orchestrator.processItem(integrationContext);
        //Busca coluna cnpj para a chave da nota na tabela de controle e testar se está igual ao cnpj esperado (buscar manualmente e static no xml)
        Map<String, Object> resultControle = jdbcTemplate.queryForMap("select ccq72c329 from fq72c335 where ccq72c100 like ?", integrationContext.getId().trim() + "%");
        Assertions.assertEquals("08062665000646", resultControle.get("ccq72c329").toString().trim());

        
    }

    @Test
    @Sql({"/data.sql"})
    public void recuperarCnpjClienteExpedidorTest() throws Exception {
        

        // Nesse caso temos um cte que o cliente do gra naõ é o destinatário do cte,tag <toma>1</toma> 1-Expedidor;
        IntegrationContext integrationContext = new IntegrationContext(resourceTomadorExpedidor.getFile());

        orchestrator.processItem(integrationContext);
        //Busca coluna cnpj para a chave da nota na tabela de controle e testar se está igual ao cnpj esperado (buscar manualmente e static no xml)
        Map<String, Object> resultControle = jdbcTemplate.queryForMap("select ccq72c329 from fq72c335 where ccq72c100 like ?", integrationContext.getId().trim() + "%");
        Assertions.assertEquals("08062665000647", resultControle.get("ccq72c329").toString().trim());

        
    }

    @Test
    @Sql({"/data.sql"})
    public void recuperarCnpjClienteRecebedorTest() throws Exception {
        

        // Nesse caso temos um cte que o cliente do gra naõ é o destinatário do cte,tag <toma>2</toma> 2-Recebedor;
        IntegrationContext integrationContext = new IntegrationContext(resourceTomadorRecebedor.getFile());

        orchestrator.processItem(integrationContext);
        //Busca coluna cnpj para a chave da nota na tabela de controle e testar se está igual ao cnpj esperado (buscar manualmente e static no xml)
        Map<String, Object> resultControle = jdbcTemplate.queryForMap("select ccq72c329 from fq72c335 where ccq72c100 like ?", integrationContext.getId().trim() + "%");
        Assertions.assertEquals("02746406000143", resultControle.get("ccq72c329").toString().trim());

        
    }

    @Test
    @Sql({"/data.sql"})
    public void recuperarCnpjClienteDestinatarioTest() throws Exception {
        

        // Nesse caso temos um cte que o cliente do gra naõ é o destinatário do cte,tag <toma>3</toma> 3-Destinatário;
        IntegrationContext integrationContext = new IntegrationContext(resourceTomadorDestinatario.getFile());

        orchestrator.processItem(integrationContext);
        //Busca coluna cnpj para a chave da nota na tabela de controle e testar se está igual ao cnpj esperado (buscar manualmente e static no xml)
        Map<String, Object> resultControle = jdbcTemplate.queryForMap("select ccq72c329 from fq72c335 where ccq72c100 like ?", integrationContext.getId().trim() + "%");
        Assertions.assertEquals("02746406000144", resultControle.get("ccq72c329").toString().trim());

        
    }


    @Test
    @Sql({"/data.sql"})
    public void recuperarCnpjClienteOutrosTest() throws Exception {
        

        // Nesse caso temos um cte que o cliente do gra naõ é o destinatário do cte,tag <toma>4</toma> 4-Outros;
        IntegrationContext integrationContext = new IntegrationContext(resourceTomadorOutros.getFile());

        orchestrator.processItem(integrationContext);
        //Busca coluna cnpj para a chave da nota na tabela de controle e testar se está igual ao cnpj esperado (buscar manualmente e static no xml)
        Map<String, Object> resultControle = jdbcTemplate.queryForMap("select ccq72c329 from fq72c335 where ccq72c100 like ?", integrationContext.getId().trim() + "%");
        Assertions.assertEquals("99999999000191", resultControle.get("ccq72c329").toString().trim());

        
    }

    @Test
    public void processDocumentWiyhEncodeISO_8859_1 () throws Exception{
        IntegrationContext integrationContext = new IntegrationContext();
        integrationContext.setFile(resourceCaracterEspecial.getFile());

        Assertions.assertDoesNotThrow(() -> orchestrator.processItem(integrationContext));
    }
}
