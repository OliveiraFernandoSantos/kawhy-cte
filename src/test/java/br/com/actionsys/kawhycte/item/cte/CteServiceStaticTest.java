package br.com.actionsys.kawhycte.item.cte;

import br.com.actionsys.kawhycommons.infra.license.LicenseService;
import br.com.actionsys.kawhycommons.infra.util.FilesUtil;
import br.com.actionsys.kawhycommons.infra.util.XmlUtil;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import br.com.actionsys.kawhycte.Orchestrator;
import br.com.actionsys.kawhycte.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
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
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
@MockBean(Schedule.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CteServiceStaticTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("classpath:xml/staticTest/CTe32230313576383150001860000000146031060219563.xml")
    private Resource cTeResource;
    @Autowired
    private Orchestrator orchestrator;

    @SpyBean
    private LicenseService licenseService;

    @BeforeAll
    @Sql("${data.sql}")
    public void before() throws Exception {
        Mockito.doReturn(true).when(licenseService).validateCnpj(Mockito.any());
        Mockito.doReturn(true).when(licenseService).validateCnpjs(Mockito.any());

        orchestrator.processItem(new IntegrationContext(cTeResource.getFile()));
    }

    @Test
    public void validarCamposCteComTabelaControle() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c335 where ccq72c100 like '%CTe32230313576383150001860000000146031060219563%'");//KWCTE_CONTROLE
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", row.get("ccq72c100").toString().trim()); //CTE_ID
        Assertions.assertEquals("14603", row.get("ccq72c108").toString().trim()); //nCT
        Assertions.assertEquals("0", row.get("ccq72c204").toString().trim()); //serie
        Assertions.assertEquals("57638315000186", row.get("ccq72c154").toString().trim()); //CNPJ_EMIT
        Assertions.assertEquals("SP", row.get("ccq72c120").toString().trim()); //UF_EMIT
        Assertions.assertEquals("Cachoeiro de Itapemirim", row.get("ccq72c119").toString().trim()); //XMUN_EMIT
        Assertions.assertEquals("43078849001081", row.get("ccq72c329").toString().trim()); //CNPJ_DEST
        Assertions.assertEquals("SP", row.get("ccq72c330").toString().trim()); //UF_DEST
        Assertions.assertEquals("Cerquilho", row.get("ccq72c332").toString().trim()); //XMUN_DEST
        Assertions.assertEquals("2023-03-22", row.get("ccq72c200").toString().trim()); //DEMI
        Assertions.assertEquals("08:05:00", row.get("ccq72c307").toString().trim()); //HEMI
        Assertions.assertEquals(11100, ((BigDecimal) row.get("ccq72c132")).intValue()); //vTPrest
        Assertions.assertEquals(10425, ((BigDecimal) row.get("ccq72c319")).intValue()); //vCarga
        Assertions.assertEquals("100", row.get("ccq72c294").toString().trim()); //cStatusSefaz
        Assertions.assertEquals("Autorizado o uso do CT-e", row.get("ccq72c333").toString().trim()); //xMotivo
        Assertions.assertEquals("332230025208044", row.get("ccq72c334").toString().trim()); //protocolo
        Assertions.assertEquals("3.00", row.get("ccq72c342").toString().trim()); //CSTATUS03
    }

    @Test
    public void validarCamposCteComTabelaKwcte() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c300 where ctq72c100 like '%CTe32230313576383150001860000000146031060219563%'");//kwcte
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", row.get("ctq72c100").toString().trim()); //id
        Assertions.assertEquals("32", row.get("ctq72c101").toString().trim()); //cuf
        Assertions.assertEquals(6021956, ((BigDecimal) row.get("ctq72c102")).intValue()); //cct
        Assertions.assertEquals(6352, ((BigDecimal) row.get("ctq72c103")).intValue()); //cfop
        Assertions.assertEquals("TRANSPORTE RODOVIARIO DE CARGAS", row.get("ctq72c104").toString().trim()); //natop
        Assertions.assertEquals("57", row.get("ctq72c106").toString().trim()); //mod
        Assertions.assertEquals("0", row.get("ctq72c107").toString().trim()); //serie
        Assertions.assertEquals("14603", row.get("ctq72c108").toString().trim()); //nct
        Assertions.assertEquals("2023-03-22", row.get("ctq72c109").toString().trim()); //dhemi
        Assertions.assertEquals("08:05:00", row.get("ctq72c307").toString().trim()); //dhemi
        Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c110")).intValue()); //tpimp
        Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c111")).intValue()); //tpemis
        Assertions.assertEquals(2, ((BigDecimal) row.get("ctq72c112")).intValue()); //cdv
        Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c113")).intValue()); //tpamb
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c114")).intValue()); //tpcte
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c115")).intValue()); //procemi
        Assertions.assertEquals("1", row.get("ctq72c116").toString().trim()); //verproc
        Assertions.assertEquals(3201209, ((BigDecimal) row.get("ctq72c118")).intValue()); //cmunenv
        Assertions.assertEquals("Cachoeiro de Itapemirim", row.get("ctq72c119").toString().trim()); //xmunenv
        Assertions.assertEquals("SP", row.get("ctq72c120").toString().trim()); //ufenv
        Assertions.assertEquals("01", row.get("ctq72c121").toString().trim()); //modal
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c122")).intValue()); //tpServ
        Assertions.assertEquals(3201209, ((BigDecimal) row.get("ctq72c123")).intValue()); //cmunini
        Assertions.assertEquals("Cachoeiro de Itapemirim", row.get("ctq72c124").toString().trim()); //xmunini
        Assertions.assertEquals("SP", row.get("ctq72c125").toString().trim()); //ufini
        Assertions.assertEquals(3511508, ((BigDecimal) row.get("ctq72c126")).intValue()); //cmunfim
        Assertions.assertEquals("Cerquilho", row.get("ctq72c127").toString().trim()); //xmunfim
        Assertions.assertEquals("SP", row.get("ctq72c128").toString().trim()); //uffim
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c129")).intValue()); //retira
        Assertions.assertEquals(3, ((BigDecimal) row.get("ctq72c131")).intValue()); //toma
        Assertions.assertEquals(11100, ((BigDecimal) row.get("ctq72c132")).intValue()); //vprest_vtprest
        Assertions.assertEquals(11100, ((BigDecimal) row.get("ctq72c133")).intValue()); //vprest_vrec
        Assertions.assertEquals("00", row.get("ctq72c134").toString().trim()); //icms_cst
        Assertions.assertEquals(11100, ((BigDecimal) row.get("ctq72c136")).intValue()); //icms_vbc
        Assertions.assertEquals(12, ((BigDecimal) row.get("ctq72c137")).intValue()); //icms_picms
        Assertions.assertEquals(1332, ((BigDecimal) row.get("ctq72c138")).intValue()); //icms_vicms
        Assertions.assertEquals(10425, ((BigDecimal) row.get("ctq72c141")).intValue()); //infcarga_vcarga
        Assertions.assertEquals("CARBONATO", row.get("ctq72c142").toString().trim()); //infcarga_propred
        Assertions.assertEquals("PALLET SACARIA -  CARRETA - LI", row.get("ctq72c143").toString().trim()); //infcarga_xoutcat
    }

    @Test
    public void validarCamposCteComTabelaKwcte_Compl() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c301 where CCQ72C100 like '%CTe32230313576383150001860000000146031060219563%'");//KWCTE_COMPL
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", row.get("CCQ72C100").toString().trim()); //cte_id
        Assertions.assertEquals("- SEGURADORA: SOMPO SEGUROS S/A, NUMERO DE APOLICE: 5400037666. RENAVAM: 01324803972, PLACA: SFR-2A42 - Vencimento: 08/05/2023 - Valor: R$ 11.100,00", row.get("CCQ72C188").toString().trim()); //xObs
    }

    @Test
    public void validarCamposCteComTabelaKwcte_enderecos_dest() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c302 where ceq72c100 like '%CTe32230313576383150001860000000146031060219563%' and ceq72c152 = 'dest'");//KWCTE_ENDERECOS
        Map<String, Object> dest = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", dest.get("ceq72c100").toString().trim()); //cte_id
        Assertions.assertEquals("dest", dest.get("ceq72c152").toString().trim()); //ctipo
        Assertions.assertEquals(1, ((BigDecimal) dest.get("ceq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("43078849001081", dest.get("ceq72c154").toString().trim()); //cnpj
        Assertions.assertEquals("265000201111", dest.get("ceq72c156").toString().trim()); //ie
        Assertions.assertEquals("CIPATEX IMPREGNADORA DE PAPEIS E TECIDOS LTDA", dest.get("ceq72c157").toString().trim()); //xnome
        Assertions.assertEquals("1532849000", dest.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("AV. 1 DE MAIO", dest.get("ceq72c161").toString().trim()); //xlgr
        Assertions.assertEquals("1341", dest.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("ESTIVA", dest.get("ceq72c164").toString().trim()); //xbairro
        Assertions.assertEquals(3511508, ((BigDecimal) dest.get("ceq72c165")).intValue()); //cmun
        Assertions.assertEquals("Cerquilho", dest.get("ceq72c166").toString().trim()); //xmun
        Assertions.assertEquals("18520000", dest.get("ceq72c167").toString().trim()); //cep
        Assertions.assertEquals("SP", dest.get("ceq72c120").toString().trim()); //uf
        Assertions.assertEquals(1058, ((BigDecimal) dest.get("ceq72c168")).intValue()); //cpais
        Assertions.assertEquals("BRASIL", dest.get("ceq72c169").toString().trim()); //xpais
        Assertions.assertEquals("Leandro.Thibes@cipatex.com.br", dest.get("ceq72c316").toString().trim()); //email
    }

    @Test
    public void validarCamposCteComTabelaKwcte_enderecos_emit() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c302 where ceq72c100 like '%CTe32230313576383150001860000000146031060219563%' and ceq72c152 = 'emit'");//KWCTE_ENDERECOS
        Map<String, Object> emit = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", emit.get("ceq72c100").toString().trim()); //cte_id
        Assertions.assertEquals("emit", emit.get("ceq72c152").toString().trim()); //ctipo
        Assertions.assertEquals(1, ((BigDecimal) emit.get("ceq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("57638315000186", emit.get("ceq72c154").toString().trim()); //cnpj
        Assertions.assertEquals("082777306", emit.get("ceq72c156").toString().trim()); //ie
        Assertions.assertEquals("FATRAN TRANSPORTE E LOGISTICA LTDA", emit.get("ceq72c157").toString().trim()); //xnome
        Assertions.assertEquals("FATRAN TRANSPORTES", emit.get("ceq72c158").toString().trim()); //xfant
        Assertions.assertEquals("2830368266", emit.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("RUA FEIERTAG JACQUES", emit.get("ceq72c161").toString().trim()); //xlgr
        Assertions.assertEquals("66", emit.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("CAICARA", emit.get("ceq72c164").toString().trim()); //xbairro
        Assertions.assertEquals(3201209, ((BigDecimal) emit.get("ceq72c165")).intValue()); //cmun
        Assertions.assertEquals("Cachoeiro de Itapemirim", emit.get("ceq72c166").toString().trim()); //xmun
        Assertions.assertEquals("29310375", emit.get("ceq72c167").toString().trim()); //cep
        Assertions.assertEquals("SP", emit.get("ceq72c120").toString().trim()); //uf
    }

    @Test
    public void validarCamposCteComTabelaKwcte_enderecos_rem() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c302 where ceq72c100 like '%CTe32230313576383150001860000000146031060219563%' and ceq72c152 = 'rem'");//KWCTE_ENDERECOS
        Map<String, Object> rem = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", rem.get("ceq72c100").toString().trim()); //cte_id
        Assertions.assertEquals("rem", rem.get("ceq72c152").toString().trim()); //ctipo
        Assertions.assertEquals(1, ((BigDecimal) rem.get("ceq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("92633315000194", rem.get("ceq72c154").toString().trim()); //cnpj
        Assertions.assertEquals("081958048", rem.get("ceq72c156").toString().trim()); //ie
        Assertions.assertEquals("SB MINERACAO LTDA", rem.get("ceq72c157").toString().trim()); //xnome
        Assertions.assertEquals("SB MINERACAO", rem.get("ceq72c158").toString().trim()); //xfant
        Assertions.assertEquals("2835231558", rem.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("ESTRADA GIRONDA X MOLEDO", rem.get("ceq72c161")); //xlgr
        Assertions.assertEquals("0", rem.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("GIRONDA", rem.get("ceq72c164").toString().trim()); //xbairro
        Assertions.assertEquals(3201209, ((BigDecimal) rem.get("ceq72c165")).intValue()); //cmun
        Assertions.assertEquals("Cachoeiro de Itapemirim", rem.get("ceq72c166").toString().trim()); //xmun
        Assertions.assertEquals("29326000", rem.get("ceq72c167").toString().trim()); //cep
        Assertions.assertEquals("SP", rem.get("ceq72c120").toString().trim()); //uf
        Assertions.assertEquals(1058, ((BigDecimal) rem.get("ceq72c168")).intValue()); //cpais
        Assertions.assertEquals("BRASIL", rem.get("ceq72c169").toString().trim()); //xpais
        Assertions.assertEquals("comercial@sbmineracao.com.br", rem.get("ceq72c316").toString().trim()); //email
    }

    @Test
    public void validarCamposCteComTabelaKwcte_observacao() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c304 where coq72c100 like '%CTe32230313576383150001860000000146031060219563%' order by coq72c153");//KWCTE_OBSERVACAO
        Map<String, Object> fisrtRow = result.get(0);
        Map<String, Object> secondRow = result.get(1);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", fisrtRow.get("coq72c100").toString().trim()); //cte_compl_cte_id
        Assertions.assertEquals("ObsCont", fisrtRow.get("coq72c190").toString().trim()); //ctipoobservacao
        Assertions.assertEquals(1, ((BigDecimal) fisrtRow.get("coq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("Lei da Transparencia", fisrtRow.get("coq72c191").toString().trim()); //xcampo
        Assertions.assertEquals("Fed - R$ 1.492,95/ Est - R$ 0,00/ Mun - R$ 222,00/ Total - R$ 1.714,95 / Fonte - IBPT  M2L5P8", fisrtRow.get("coq72c192").toString().trim()); //xtexto

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", secondRow.get("coq72c100").toString().trim()); //cte_compl_cte_id
        Assertions.assertEquals("ObsCont", secondRow.get("coq72c190").toString().trim()); //ctipoobservacao
        Assertions.assertEquals(2, ((BigDecimal) secondRow.get("coq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("RESPSEG", secondRow.get("coq72c191").toString().trim()); //xcampo
        Assertions.assertEquals("13.226.507/0001-00", secondRow.get("coq72c192").toString().trim()); //xtexto
    }

    @Test
    public void validarCamposCteComTabelaKwcte_vprest_comp() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c306 where cpq72c100 like '%CTe32230313576383150001860000000146031060219563%'");//KWCTE_VPREST_COMP
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", row.get("cpq72c100").toString().trim()); //cte_id
        Assertions.assertEquals(1, ((BigDecimal) row.get("cpq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("Frete Peso", row.get("cpq72c157").toString().trim()); //xnome
        Assertions.assertEquals(11100, ((BigDecimal) row.get("cpq72c211")).intValue()); //vcomp
    }

    @Test
    public void validarCamposCteComTabelaKwcte_infcarga_infq() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c307 where ciq72c100 like '%CTe32230313576383150001860000000146031060219563%' order by ciq72c153");//KWCTE_INFCARGA_INFQ
        Map<String, Object> firstRow = result.get(0);
        Map<String, Object> secondRow = result.get(1);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", firstRow.get("ciq72c100").toString().trim()); //cte_id
        Assertions.assertEquals(1, ((BigDecimal) firstRow.get("ciq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("03", firstRow.get("ciq72c212").toString().trim()); //cunid
        Assertions.assertEquals("UNID.", firstRow.get("ciq72c213").toString().trim()); //tpmed
        Assertions.assertEquals(25, ((BigDecimal) firstRow.get("ciq72c214")).intValue()); //qcarga

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", secondRow.get("ciq72c100").toString().trim()); //cte_id
        Assertions.assertEquals(2, ((BigDecimal) secondRow.get("ciq72c153")).intValue()); //nsequencia
        Assertions.assertEquals("01", secondRow.get("ciq72c212").toString().trim()); //cunid
        Assertions.assertEquals("PESO BRUTO", secondRow.get("ciq72c213").toString().trim()); //tpmed
        Assertions.assertEquals(37500, ((BigDecimal) secondRow.get("ciq72c214")).intValue()); //qcarga
    }

    @Test
    public void validarCamposCteComTabelaKwcte_infresptec() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72c338 where rtq72c100 like '%CTe32230313576383150001860000000146031060219563%'");//KWCTE_INFRESPTEC
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", row.get("rtq72c100").toString().trim()); //cte_id
        Assertions.assertEquals("10929600000192", row.get("rtq72c154").toString().trim()); //cnpj
        Assertions.assertEquals("Rodrigo Goronci Santana", row.get("rtq720806").toString().trim()); //xcontato
        Assertions.assertEquals("rodrigo@servicelogic.com.br", row.get("rtq720824").toString().trim()); //email
    }

    @Test
    public void validarCamposCteComTabelaKwnfe_xml() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList(
                "select * from fq72319 where nxq720652 like '%CTe32230313576383150001860000000146031060219563%'");//KWNFE_XML
        Map<String, Object> firstRow = result.get(0);
        Map<String, Object> secondRow = result.get(0);
        Map<String, Object> thirdRow = result.get(0);

        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", firstRow.get("nxq720652").toString().trim()); //nfe_id
        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", secondRow.get("nxq720652").toString().trim()); //nfe_id
        Assertions.assertEquals("CTe32230313576383150001860000000146031060219563", thirdRow.get("nxq720652").toString().trim()); //nfe_id
    }
}
