package br.com.actionsys.kawhycte.infra.control;

import br.com.actionsys.kawhycommons.infra.license.LicenseService;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import br.com.actionsys.kawhycte.Orchestrator;
import br.com.actionsys.kawhycte.Schedule;
import lombok.extern.slf4j.Slf4j;
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

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Slf4j
@MockBean(Schedule.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControlService_findCnpjTomadorTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Orchestrator orchestrator;
    @SpyBean
    private LicenseService licenseService;

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

    @BeforeEach
    public void before() {
        Mockito.doReturn(true).when(licenseService).validateCnpj(Mockito.any());
        Mockito.doReturn(true).when(licenseService).validateCnpjs(Mockito.any());
    }


    @Test
    @Sql({"/data.sql"})
    public void validarTomadorRemetenteTest() throws Exception {
        IntegrationContext item = new IntegrationContext(resourceTomadorRemetente.getFile());
        orchestrator.processItem(item);

        String result = jdbcTemplate.queryForObject(
            //          cnpj_dest      kwcte_controle  cte_id
                "select ccq72c329 from fq72c335 where (ccq72c100 like 'CTe35231105798428000145570010000496001644803310%')", String.class);

        assertEquals("08062665000646", result.trim());
    }

    @Test
    @Sql({"/data.sql"})
    public void validarTomadorExpedidorTest() throws Exception {
        IntegrationContext item = new IntegrationContext(resourceTomadorExpedidor.getFile());
        orchestrator.processItem(item);

        String result = jdbcTemplate.queryForObject(
                //      cnpj_dest      kwcte_controle  cte_id
                "select ccq72c329 from fq72c335 where (ccq72c100 like 'CTe35231105798428000145570010000496001644803311%')", String.class);

        assertEquals("08062665000647", result.trim());
    }

    @Test
    @Sql({"/data.sql"})
    public void validarTomadorRecebedorTest() throws Exception {
        IntegrationContext item = new IntegrationContext(resourceTomadorRecebedor.getFile());
        orchestrator.processItem(item);

        String result = jdbcTemplate.queryForObject(
                //      cnpj_dest      kwcte_controle  cte_id
                "select ccq72c329 from fq72c335 where (ccq72c100 like 'CTe35231105798428000145570010000496001644803312%')", String.class);

        assertEquals("02746406000143", result.trim());
    }

    @Test
    @Sql({"/data.sql"})
    public void validarTomadorDestinatarioTest() throws Exception {
        IntegrationContext item = new IntegrationContext(resourceTomadorDestinatario.getFile());
        orchestrator.processItem(item);

        String result = jdbcTemplate.queryForObject(
                //      cnpj_dest      kwcte_controle  cte_id
                "select ccq72c329 from fq72c335 where (ccq72c100 like 'CTe35231105798428000145570010000496001644803313%')", String.class);

        assertEquals("02746406000144", result.trim());
    }

    @Test
    @Sql({"/data.sql"})
    public void validarTomadorOutrosTest() throws Exception {
        IntegrationContext item = new IntegrationContext(resourceTomadorOutros.getFile());
        orchestrator.processItem(item);

        String result = jdbcTemplate.queryForObject(
                //      cnpj_dest      kwcte_controle  cte_id
                "select ccq72c329 from fq72c335 where (ccq72c100 like 'CTe35231105798428000145570010000496001644803314%')", String.class);

        assertEquals("99999999000191", result.trim());
    }
}

