package br.com.actionsys.kawhycte.infra.control;

import br.com.actionsys.kawhycommons.Constants;
import br.com.actionsys.kawhycommons.infra.license.LicenseService;
import br.com.actionsys.kawhycommons.infra.util.DateUtil;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import br.com.actionsys.kawhycommons.types.KawhyType;
import br.com.actionsys.kawhycte.Orchestrator;
import br.com.actionsys.kawhycte.Schedule;
import lombok.extern.slf4j.Slf4j;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
@MockBean(Schedule.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ControlService_insertTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private Orchestrator orchestrator;
    @SpyBean
    private LicenseService licenseService;

    @Value("/xml/staticTest/CTe35231105798428000145570010000496001644803314.xml")
    private Resource xmlResource;

    public void before() throws Exception {
        Mockito.doReturn(true).when(licenseService).validateCnpj(Mockito.any());
        Mockito.doReturn(true).when(licenseService).validateCnpjs(Mockito.any());

        File resource = xmlResource.getFile();
        orchestrator.processDocumentFile(new IntegrationContext(resource));
    }

    @Test
    @Sql({"/data.sql"})
    public void validarTabelaKwcte_controle() throws Exception {
        before();

        List<Map<String, Object>> result = jdbcTemplate.queryForList
                ("select * from fq72c335 where ccq72c100 like 'CTe29200606203406000662570110000086721008919921%'");//kwnfe
        Map<String, Object> row = result.get(0);

        assertEquals("CTe29200606203406000662570110000086721008919921", row.get("ccq72c100").toString().trim());//cte_id
        assertEquals("8672", row.get("ccq72c108").toString().trim());//nct
        assertEquals("11", row.get("ccq72c204").toString().trim());//serie
        assertEquals("06203406000662", row.get("ccq72c154").toString().trim());//cnpj_emit
        assertEquals("BA", row.get("ccq72c120").toString().trim());//uf_emit
        assertEquals("SIMOES FILHO", row.get("ccq72c119").toString().trim());//xmun_emit
        assertEquals("42463174000130", row.get("ccq72c329").toString().trim());//cnpj_dest
        assertEquals("47363130808", row.get("ccq72c331").toString().trim());//cpf_dest
        assertEquals("BA", row.get("ccq72c330").toString().trim());//uf_dest
        assertEquals("JACOBINA", row.get("ccq72c332").toString().trim());//xmun_dest
        assertEquals("2020-06-13", row.get("ccq72c200").toString().trim());//demi
        assertEquals("12:31:49", row.get("ccq72c307").toString().trim());//hemi
        assertEquals("59.54", row.get("ccq72c132").toString().trim());//vtprest
        assertEquals("1923.55", row.get("ccq72c319").toString().trim());//vcarga
        assertEquals("100", row.get("ccq72c294").toString().trim());//cstatussefaz
        assertEquals("Autorizado o uso do CT-e", row.get("ccq72c333").toString().trim());//xmotivo
        assertEquals("329200016794509", row.get("ccq72c334").toString().trim());//protocolo

        Date date = new Date();
        assertEquals(DateUtil.formatDateToDb(date), row.get("ccq72c295").toString().trim());//dtconsultasefaz
//        assertEquals(DateUtil.formatTimeToDb(date), row.get("ccq72c296").toString().trim());//hrconsultasefaz

        assertEquals("temp/ActionSys/KaWhys/Arquivos/Saida/CTe/CTe29200606203406000662570110000086721008919921.xml", row.get("ccq72c306").toString().trim());//arquivo
        assertEquals(Constants.XML, row.get("ccq72c336").toString().trim());//statuscte

        assertNull(row.get("ccq72c335"));//tipocte
        assertNull(row.get("ccq72c297"));//cstatuscomercial
        assertNull(row.get("ccq72c298"));//cstatusfisico
        assertNull(row.get("ccq72c337"));//comercial
        assertNull(row.get("ccq72c338"));//filial

        assertEquals(DateUtil.formatDateToDb(date), row.get("ccq72c299"));//dtentrada
//        assertEquals(DateUtil.formatTimeToDb(date), row.get("ccq72c300"));//hrentrada

        assertNull(row.get("ccq72c301"));//userentrada
        assertNull(row.get("ccq72c303"));//ctipovalidacao
        assertNull(row.get("ccq72c304"));//cstatuserp
        assertNull(row.get("ccq72c305"));//dtrecepcao
        assertNull(row.get("ccq72c339"));//hrcoleta

        assertFalse(row.get("ccq72c340").toString().isEmpty());//cstatus01
        assertFalse(row.get("ccq72c341").toString().isEmpty());//cstatus02
        assertEquals("3.00", row.get("ccq72c342").toString().trim());//cstatus03
        assertFalse(row.get("ccq72c343").toString().isEmpty());//cstatus04

        assertEquals(KawhyType.KAWHY_CTE.getServiceName(), row.get("ccq72c344").toString().trim());//audit_usuario
        assertEquals(KawhyType.KAWHY_CTE.getServiceName(), row.get("ccq72c345").toString().trim());//audit_programa

        assertEquals(DateUtil.formatDateToDb(date), row.get("ccq72c346").toString().trim());//audit_data
//        assertEquals(DateUtil.formatTimeToDb(date), row.get("ccq72c347").toString().trim());//audit_hora
    }
}
