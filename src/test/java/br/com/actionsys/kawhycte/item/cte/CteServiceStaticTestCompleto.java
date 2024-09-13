package br.com.actionsys.kawhycte.item.cte;

import br.com.actionsys.kawhycommons.infra.license.LicenseService;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import br.com.actionsys.kawhycte.Orchestrator;
import br.com.actionsys.kawhycte.Schedule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
@MockBean(Schedule.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CteServiceStaticTestCompleto {

    public static final String CHAVE_CTE = "CTe29200606203406000662570110000086721008919921";
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Value("xml/staticTest/" + CHAVE_CTE + "_Completo.xml")
    private Resource cTeResource;
    @Autowired
    private Orchestrator orchestrator;

    @SpyBean
    private LicenseService licenseService;

    @BeforeAll
    public void before() throws Exception {
        Mockito.doReturn(true).when(licenseService).validateCnpj(Mockito.any());
        Mockito.doReturn(true).when(licenseService).validateCnpjs(Mockito.any());

        orchestrator.processItem(new IntegrationContext(cTeResource.getFile()));
    }

    @Test
    public void validarCamposCteComTabelaKwcte_controle() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c335 where ccq72c100 like '%" + CHAVE_CTE + "%'");//KWCTE_CONTROLE
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("3.00", row.get("ccq72c342").toString().trim()); //CSTATUS03
        Assertions.assertEquals(CHAVE_CTE, row.get("ccq72c100").toString().trim()); //CTE_ID
        Assertions.assertEquals("329200016794509", row.get("ccq72c334").toString().trim()); //protocolo
        Assertions.assertEquals("100", row.get("ccq72c294").toString().trim()); //cStatusSefaz
        Assertions.assertEquals("Autorizado o uso do CT-e", row.get("ccq72c333").toString().trim()); //xMotivo
        Assertions.assertEquals("2020-06-13", row.get("ccq72c200").toString().trim()); //DEMI
        Assertions.assertEquals("12:31:49", row.get("ccq72c307").toString().trim()); //HEMI
    }

    @Test
    public void validarCamposCteComTabelaKwcte() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c300 where ctq72c100 like '%" + CHAVE_CTE + "%'");//kwcte
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals(CHAVE_CTE, row.get("ctq72c100").toString().trim()); //id
        Assertions.assertEquals("29", row.get("ctq72c101").toString().trim()); //cUF
        Assertions.assertEquals(891400, ((BigDecimal) row.get("ctq72c102")).intValue()); //cCT
        Assertions.assertEquals(5353, ((BigDecimal) row.get("ctq72c103")).intValue()); //CFOP
        Assertions.assertEquals("Transp a est comercial", row.get("ctq72c104").toString().trim()); //natOp
        Assertions.assertEquals("57", row.get("ctq72c106").toString().trim()); //mod
        Assertions.assertEquals("11", row.get("ctq72c107").toString().trim()); //serie
        Assertions.assertEquals("8672", row.get("ctq72c108").toString().trim()); //nCT
        Assertions.assertEquals("2020-06-13", row.get("ctq72c109").toString().trim()); //dhEmi
        Assertions.assertEquals("12:31:49", row.get("ctq72c307").toString().trim()); //dhEmi
        Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c110")).intValue()); //tpImp
        Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c111")).intValue()); //tpEmis
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c112")).intValue()); //cDV
        Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c113")).intValue()); //tpAmb
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c114")).intValue()); //tpCTe
        Assertions.assertEquals("Este é um exemplo de dado NCHAR(160)", row.get("ctq72c130").toString().trim()); //xDetRetira
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c115")).intValue()); //procEmi
        Assertions.assertEquals("1.0", row.get("ctq72c116").toString().trim()); //verProc
        Assertions.assertEquals(2930709, ((BigDecimal) row.get("ctq72c118")).intValue()); //cMunEnv
        Assertions.assertEquals("SIMOES FILHO", row.get("ctq72c119").toString().trim()); //xMunEnv
        Assertions.assertEquals("BA", row.get("ctq72c120").toString().trim()); //UFEnv
        Assertions.assertEquals("01", row.get("ctq72c121").toString().trim()); //modal
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c122")).intValue()); //tpServ
        Assertions.assertEquals(2925204, ((BigDecimal) row.get("ctq72c123")).intValue()); //cMunIni
        Assertions.assertEquals("POJUCA", row.get("ctq72c124").toString().trim()); //xMunIni
        Assertions.assertEquals("BA", row.get("ctq72c125").toString().trim()); //UFIni
        Assertions.assertEquals(2917508, ((BigDecimal) row.get("ctq72c126")).intValue()); //cMunFim
        Assertions.assertEquals("JACOBINA", row.get("ctq72c127").toString().trim()); //xMunFim
        Assertions.assertEquals("BA", row.get("ctq72c128").toString().trim()); //UFFim
        Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c129")).intValue()); //retira
        Assertions.assertEquals(4, ((BigDecimal) row.get("ctq72c131")).intValue()); //toma
        Assertions.assertEquals(59.0, ((BigDecimal) row.get("ctq72c132")).intValue()); //vPrest_vTPrest
        Assertions.assertEquals(59.0, ((BigDecimal) row.get("ctq72c133")).intValue()); //vPrest_vRec
        Assertions.assertEquals("40", row.get("ctq72c134").toString().trim()); //ICMS_CST
        Assertions.assertEquals(500, ((BigDecimal) row.get("ctq72c136")).intValue()); //ICMS_vBC
        Assertions.assertEquals(18, ((BigDecimal) row.get("ctq72c137")).intValue()); //ICMS_pICMS
        Assertions.assertEquals(90, ((BigDecimal) row.get("ctq72c138")).intValue()); //ICMS_vICMS
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c139")).intValue()); //ICMS_vCred
        Assertions.assertEquals(354, ((BigDecimal) row.get("ctq72c308")).intValue()); //ICMS_vBCSTRet
        Assertions.assertEquals(70.0, ((BigDecimal) row.get("ctq72c309")).intValue()); //ICMS_vICMSSTRet
        Assertions.assertEquals(12, ((BigDecimal) row.get("ctq72c310")).intValue()); //ICMS_pICMSSTRet
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c311")).intValue()); //ICMSOutraUF_pRedBCOutraUF
        Assertions.assertEquals(0, ((BigDecimal) row.get("ctq72c312")).intValue()); //ICMSOutraUF_vBCOutraUF
        Assertions.assertEquals(20, ((BigDecimal) row.get("ctq72c313")).intValue()); //ICMSOutraUF_pICMSOutraUF
        Assertions.assertEquals(200, ((BigDecimal) row.get("ctq72c314")).intValue()); //ICMSOutraUF_vICMSOutraUF
       // Assertions.assertEquals(1, ((BigDecimal) row.get("ctq72c315")).intValue()); //ICMSSN_indSN
        Assertions.assertEquals("Texto Teste", row.get("ctq72c140").toString().trim()); //ICMSSN_infAdFisco
        Assertions.assertEquals("VOLUME", row.get("ctq72c143").toString().trim()); //infCarga_xOutCat
        Assertions.assertEquals(1923.0, ((BigDecimal) row.get("ctq72c141")).intValue()); //infCarga_vCarga
        Assertions.assertEquals("ENTREGA NORMAL", row.get("ctq72c142").toString().trim()); //infcarga_proPred
    }
    @Test
    public void validarCamposCteComTabelaKwcte_veicnovos() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c330 where cvq72c100 like '%" + CHAVE_CTE + "%'");//Kwcte_veicnovos
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals(CHAVE_CTE, row.get("cvq72c100").toString().trim()); //id
        Assertions.assertEquals("12345678912345678", row.get("cvq72c282").toString().trim()); //chassi
        Assertions.assertEquals("12345678912345678", row.get("cvq72c282").toString().trim()); //chassi
        Assertions.assertEquals("1234", row.get("cvq72c283").toString().trim()); //cCor
        Assertions.assertEquals("VERMELHO", row.get("cvq72c284").toString().trim()); //xCor
        Assertions.assertEquals("123456", row.get("cvq72c285").toString().trim()); //cMod
        Assertions.assertEquals(0, ((BigDecimal) row.get("cvq72c286")).intValue()); //vUnit
        Assertions.assertEquals(0, ((BigDecimal) row.get("cvq72c262")).intValue()); //vFrete
    }

    @Test
    public void validarCamposCteComTabelaKwcte_iddocantpap() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c310 where cdq72c100 like '%" + CHAVE_CTE + "%'");//Kwcte_iddocantpap
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("12/08/2019", row.get("cdq72c200").toString().trim()); //dEmi
        Assertions.assertEquals("9636", row.get("cdq72c199").toString().trim()); //nDoc
        Assertions.assertEquals("00", row.get("cdq72c197").toString().trim()); //tpDoc
        Assertions.assertEquals("11", row.get("cdq72c107").toString().trim()); //serie
        Assertions.assertEquals("10", row.get("cdq72c218").toString().trim()); //subser
    }

    @Test
    public void validarCamposCteComTabelaKwcte_infctesub() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c331 where csq72c100 like '%" + CHAVE_CTE + "%'");//Kwcte_infctesub
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("CTe29200606203406000662570110000086721008914000", row.get("csq72c318").toString().trim()); //chCte
        Assertions.assertEquals("CTe29200606203406000662570110000086721008914000", row.get("csq72c117").toString().trim()); //refCte
        Assertions.assertEquals("NFe29200606203406000662570110000086721008914000", row.get("csq72c287").toString().trim()); //refNFe
        Assertions.assertEquals("42463174000130", row.get("csq72c154").toString().trim()); //CNPJ
        Assertions.assertEquals("57", row.get("csq72c106").toString().trim()); //mod
        Assertions.assertEquals("11", row.get("csq72c204").toString().trim()); //serie
        Assertions.assertEquals("11", row.get("csq72c288").toString().trim()); //subserie
        Assertions.assertEquals("1796", row.get("csq72c162").toString().trim()); //nro
        Assertions.assertEquals("0", row.get("csq72c289").toString().trim()); //valor
        Assertions.assertEquals("12/08/2019", row.get("csq72c200").toString().trim()); //dEmi
    }

    @Test
    public void validarCamposCteComTabelaKwcte_Compl() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c301 where ccq72c100 like '%" + CHAVE_CTE + "%'");//KWCTE_COMPL
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("SAO BERNARDO DO CAMPO", row.get("ccq72c186").toString().trim()); //origCalc
        Assertions.assertEquals("EXTREMA", row.get("ccq72c187").toString().trim()); //destCalc
        Assertions.assertEquals("Normal", row.get("ccq72c172").toString().trim()); //xCaracAd
        Assertions.assertEquals("BIOLAB", row.get("ccq72c173").toString().trim()); //xCaracSer
        Assertions.assertEquals("DCAMPOS", row.get("ccq72c174").toString().trim()); //xEmi

        Assertions.assertEquals("YHE", row.get("ccq72c175").toString().trim()); //xOrig
        Assertions.assertEquals("EDT", row.get("ccq72c176").toString().trim()); //xDest
        Assertions.assertEquals("YHEEDT", row.get("ccq72c177").toString().trim()); //xRota

        Assertions.assertEquals("18:00:00", row.get("ccq72c184").toString().trim()); //hIni
        Assertions.assertEquals("18:00:00", row.get("ccq72c185").toString().trim()); //hFim
        Assertions.assertEquals("18:00:00", row.get("ccq72c183").toString().trim()); //hProg
        Assertions.assertEquals("2019-05-21", row.get("ccq72c180").toString().trim()); //dIni
        Assertions.assertEquals("2023-08-07", row.get("ccq72c181").toString().trim()); //dFim
        Assertions.assertEquals("2", row.get("ccq72c178").toString().trim()); //tpPer
        Assertions.assertEquals("2020-06-18", row.get("ccq72c179").toString().trim()); //dProg
        Assertions.assertEquals("0", row.get("ccq72c182").toString().trim()); //tpHor

        Assertions.assertEquals("""
                DIM VOLUMES(01): 0,38x0,28x0,41x1 CST: 40 N PEDIDO: 0000099134. - Apolice seguro: 5400017925 -
                                    Seguradora: 61383493000180 SOMPO SEGUROS ISENTO CONF. INCISO CXIII DO ART. 265 DO DECRETO 13780/12
                                    NAO BASTA TRANSPORTAR, NOSSO OBJETIVO E ENCANTAR RNTRC: 01179174.""", row.get("ccq72c188").toString().trim()); //xObs



    }

    @Test
    public void validarCamposCteComTabelaKwcte_pass() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c303 where cpq72c100 like '%" + CHAVE_CTE + "%'");//KWCTE_COMPL
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("GRU", row.get("cpq72c189").toString().trim()); //xPass

    }


    @Test
    public void validarCamposCteComTabelaKwcte_enderecos_dest() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c302 where ceq72c100 like '%" + CHAVE_CTE + "%' order by CEQ72C152");//KWCTE_ENDERECOS
        Map<String, Object> dest = result.get(0);
        Map<String, Object> emit = result.get(1);
        Map<String, Object> exped = result.get(2);
        Map<String, Object> receb = result.get(3);
        Map<String, Object> rem = result.get(4);
        Map<String, Object> emiOcc = result.get(5); // rodo_emiOcc
        Map<String, Object> toma4 = result.get(6);

        Assertions.assertEquals("06203406000662", emit.get("ceq72c154").toString().trim()); //CNPJ
//        Assertions.assertEquals("47363130809", emit.get("ceq72c155").toString().trim()); //CPF NULL
        Assertions.assertEquals("135888791", emit.get("ceq72c156").toString().trim()); //IE
        Assertions.assertEquals("TRANSLAG TRANSP E LOG LTDA", emit.get("ceq72c157").toString().trim()); //xNome
        Assertions.assertEquals("TRANSPORTADORA1", emit.get("ceq72c158").toString().trim()); //xFant
        Assertions.assertEquals("VIA ACESSO 02 BR 324", emit.get("ceq72c161").toString().trim()); //xLgr
        Assertions.assertEquals("LT-3A GLEBA A", emit.get("ceq72c163").toString().trim()); //xCpl
        Assertions.assertEquals("1796", emit.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("CIA SUL", emit.get("ceq72c164").toString().trim()); //xBairro
        Assertions.assertEquals("2930709", emit.get("ceq72c165").toString().trim()); //cMun
        Assertions.assertEquals("SIMOES FILHO", emit.get("ceq72c166").toString().trim()); //xMun
        Assertions.assertEquals("43700000", emit.get("ceq72c167").toString().trim()); //CEP
        Assertions.assertEquals("BA", emit.get("ceq72c120").toString().trim()); //UF
        Assertions.assertEquals("62992947373", emit.get("ceq72c159").toString().trim()); //fone


        Assertions.assertEquals("82749987001188", rem.get("ceq72c154").toString().trim()); //CNPJ
        Assertions.assertEquals("47363130809", rem.get("ceq72c155").toString().trim()); //CPF
        Assertions.assertEquals("163550823", rem.get("ceq72c156").toString().trim()); //IE
        Assertions.assertEquals("NETZSCH DO BRASIL IND COM LTDA - BA", rem.get("ceq72c157").toString().trim()); //xNome
        Assertions.assertEquals("62992947373", rem.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("teste.teste@teste.com.br", rem.get("ceq72c316").toString().trim()); //email
        Assertions.assertEquals("TRANSPORTADORA1", rem.get("ceq72c158").toString().trim()); //xFant
        Assertions.assertEquals("AVENIDA ITAPARICA", rem.get("ceq72c161").toString().trim()); //xLgr
        Assertions.assertEquals("LT-3A GLEBA A", rem.get("ceq72c163").toString().trim()); //xCpl
        Assertions.assertEquals("204", rem.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("POJUCA II", rem.get("ceq72c164").toString().trim()); //xBairro
        Assertions.assertEquals("2925204", rem.get("ceq72c165").toString().trim()); //cMun
        Assertions.assertEquals("POJUCA", rem.get("ceq72c166").toString().trim()); //xMun
        Assertions.assertEquals("48120000", rem.get("ceq72c167").toString().trim()); //CEP
        Assertions.assertEquals("BA", rem.get("ceq72c120").toString().trim()); //UF
        Assertions.assertEquals("1058", rem.get("ceq72c168").toString().trim()); //cPais
        Assertions.assertEquals("BRASIL", rem.get("ceq72c169").toString().trim()); //xPais

        Assertions.assertEquals("82749987001188", exped.get("ceq72c154").toString().trim()); //CNPJ
        Assertions.assertEquals("163550823", exped.get("ceq72c156").toString().trim()); //IE
        Assertions.assertEquals("NETZSCH DO BRASIL IND COM LTDA - BA", exped.get("ceq72c157").toString().trim()); //xNome
        Assertions.assertEquals("AVENIDA ITAPARICA", exped.get("ceq72c161").toString().trim()); //xLgr
        Assertions.assertEquals("204", exped.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("POJUCA II", exped.get("ceq72c164").toString().trim()); //xBairro
        Assertions.assertEquals("2925204", exped.get("ceq72c165").toString().trim()); //cMun
        Assertions.assertEquals("POJUCA", exped.get("ceq72c166").toString().trim()); //xMun
        Assertions.assertEquals("48120000", exped.get("ceq72c167").toString().trim()); //CEP
        Assertions.assertEquals("BA", exped.get("ceq72c120").toString().trim()); //UF
        Assertions.assertEquals("1058", exped.get("ceq72c168").toString().trim()); //cPais
        Assertions.assertEquals("BRASIL", exped.get("ceq72c169").toString().trim()); //xPais

        Assertions.assertEquals("42463174000130", receb.get("ceq72c154").toString().trim()); //CNPJ
        Assertions.assertEquals("026501484", receb.get("ceq72c156").toString().trim()); //IE
        Assertions.assertEquals("JACOBINA MINERACAO E COMERCIO S.A.", receb.get("ceq72c157").toString().trim()); //xNome
        Assertions.assertEquals("7436218127", receb.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("FAZENDA DO ITAPICURU", receb.get("ceq72c161").toString().trim()); //xLgr
        Assertions.assertEquals("SN", receb.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("CENTRO", receb.get("ceq72c164").toString().trim()); //xBairro
        Assertions.assertEquals("2917508", receb.get("ceq72c165").toString().trim()); //cMun
        Assertions.assertEquals("JACOBINA", receb.get("ceq72c166").toString().trim()); //xMun
        Assertions.assertEquals("44700000", receb.get("ceq72c167").toString().trim()); //CEP
        Assertions.assertEquals("BA", receb.get("ceq72c120").toString().trim()); //UF
        Assertions.assertEquals("1058", receb.get("ceq72c168").toString().trim()); //cPais
        Assertions.assertEquals("BRASIL", receb.get("ceq72c169").toString().trim()); //xPais

        Assertions.assertEquals("42463174000130", dest.get("ceq72c154").toString().trim()); //CNPJ
        Assertions.assertEquals("026501484", dest.get("ceq72c156").toString().trim()); //IE
        Assertions.assertEquals("JACOBINA MINERACAO E COMERCIO LTDA", dest.get("ceq72c157").toString().trim()); //xNome
        // Assertions.assertEquals("0", dest.get("ceq72c171").toString().trim()); //tpProp NULL DESCONTINUADO
        Assertions.assertEquals("7436218127", dest.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("teste.teste@teste.com.br", dest.get("ceq72c316").toString().trim()); //email
        Assertions.assertEquals("ABCDEFG12345678", dest.get("ceq72c160").toString().trim()); //ISUF
        Assertions.assertEquals("FAZENDA ITAPICURU", dest.get("ceq72c161").toString().trim()); //xLgr
        Assertions.assertEquals("S/N", dest.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("PARTE", dest.get("ceq72c163").toString().trim()); //xCpl
        Assertions.assertEquals("CENTRO", dest.get("ceq72c164").toString().trim()); //xBairro
        Assertions.assertEquals("2917508", dest.get("ceq72c165").toString().trim()); //cMun
        Assertions.assertEquals("JACOBINA", dest.get("ceq72c166").toString().trim()); //xMun
        Assertions.assertEquals("44700000", dest.get("ceq72c167").toString().trim()); //CEP
        Assertions.assertEquals("BA", dest.get("ceq72c120").toString().trim()); //UF
        Assertions.assertEquals("1058", dest.get("ceq72c168").toString().trim()); //cPais
        Assertions.assertEquals("BRASIL", dest.get("ceq72c169").toString().trim()); //xPais

        Assertions.assertEquals("42463174000130", emiOcc.get("ceq72c154").toString().trim()); //CNPJ
        Assertions.assertEquals("026501484", emiOcc.get("ceq72c156").toString().trim()); //IE
        Assertions.assertEquals("BA", emiOcc.get("ceq72c120").toString().trim()); //UF
        Assertions.assertEquals("04133360877", emiOcc.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("1234567890", emiOcc.get("ceq72c170").toString().trim()); //cInt

        Assertions.assertEquals("06203406000662", toma4.get("ceq72c154").toString().trim()); //CNPJ
        Assertions.assertEquals("978203557244", toma4.get("ceq72c156").toString().trim()); //IE
        Assertions.assertEquals("NETZSCH DO BRASIL IND COM LTDA - BA", toma4.get("ceq72c157").toString().trim()); //xNome
        Assertions.assertEquals("62992947373", toma4.get("ceq72c159").toString().trim()); //fone
        Assertions.assertEquals("teste.teste@teste.com.br", toma4.get("ceq72c316").toString().trim()); //email
        Assertions.assertEquals("VIA ACESSO 02 BR 324", toma4.get("ceq72c161").toString().trim()); //xLgr
        Assertions.assertEquals("1796", toma4.get("ceq72c162").toString().trim()); //nro
        Assertions.assertEquals("LT-3A GLEBA A", toma4.get("ceq72c163").toString().trim()); //xCpl
        Assertions.assertEquals("CIA SUL", toma4.get("ceq72c164").toString().trim()); //xBairro
        Assertions.assertEquals("2930709", toma4.get("ceq72c165").toString().trim()); //cMun
        Assertions.assertEquals("SIMOES FILHO", toma4.get("ceq72c166").toString().trim()); //xMun
        Assertions.assertEquals("43700000", toma4.get("ceq72c167").toString().trim()); //CEP
        Assertions.assertEquals("BA", toma4.get("ceq72c120").toString().trim()); //UF
        Assertions.assertEquals("1058", toma4.get("ceq72c168").toString().trim()); //cPais
        Assertions.assertEquals("BRASIL", toma4.get("ceq72c169").toString().trim()); //xPais


    }

    @Test
    public void validarCamposCteComTabelaKwcte_observacao() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c304 where coq72c100 like '%" + CHAVE_CTE + "%' order by coq72c190 desc, coq72c153 desc ");//KWCTE_OBSERVACAO
        Map<String, Object> fisrtRow = result.get(0);
        Map<String, Object> secondRow = result.get(1);
        Map<String, Object> thirdRow = result.get(2);

        Map<String, Object> fourthRow = result.get(3);
        Map<String, Object> fifthRow = result.get(4);
        Map<String, Object> sixthRow = result.get(5);

        Assertions.assertEquals(CHAVE_CTE, fisrtRow.get("coq72c100").toString().trim());//id
        Assertions.assertEquals(3, ((BigDecimal) fisrtRow.get("coq72c153")).intValue()); //nSequencia
        Assertions.assertEquals("ObsFisco", fisrtRow.get("coq72c190").toString().trim()); //cTipoObservacao
        Assertions.assertEquals("RESPSEG", fisrtRow.get("coq72c191").toString().trim()); //xCampo
        Assertions.assertEquals("06203406000662", fisrtRow.get("coq72c192").toString().trim()); //xTexto

        Assertions.assertEquals(CHAVE_CTE, secondRow.get("coq72c100").toString().trim());//id
        Assertions.assertEquals(2, ((BigDecimal) secondRow.get("coq72c153")).intValue()); //nSequencia
        Assertions.assertEquals("ObsFisco", secondRow.get("coq72c190").toString().trim()); //cTipoObservacao
        Assertions.assertEquals("2", secondRow.get("coq72c191").toString().trim()); //xCampo
        Assertions.assertEquals("RNTRC: 01179174.", secondRow.get("coq72c192").toString().trim()); //xTexto


        Assertions.assertEquals(CHAVE_CTE, thirdRow.get("coq72c100").toString().trim());//id
        Assertions.assertEquals(1, ((BigDecimal) thirdRow.get("coq72c153")).intValue()); //nSequencia
        Assertions.assertEquals("ObsFisco", thirdRow.get("coq72c190").toString().trim()); //cTipoObservacao
        Assertions.assertEquals("1", thirdRow.get("coq72c191").toString().trim()); //xCampo
        Assertions.assertEquals("NAO BASTA TRANSPORTAR, NOSSO OBJETIVO E ENCANTAR", thirdRow.get("coq72c192").toString().trim()); //xTexto

        Assertions.assertEquals(CHAVE_CTE, fourthRow.get("coq72c100").toString().trim());//id
        Assertions.assertEquals(3, ((BigDecimal) fourthRow.get("coq72c153")).intValue()); //nSequencia
        Assertions.assertEquals("ObsCont", fourthRow.get("coq72c190").toString().trim()); //cTipoObservacao
        Assertions.assertEquals("3", fourthRow.get("coq72c191").toString().trim()); //xCampo
        Assertions.assertEquals("ISENTO CONF. INCISO CXIII DO ART. 265 DO DECRETO 13780/12", fourthRow.get("coq72c192").toString().trim()); //xTexto


        Assertions.assertEquals(CHAVE_CTE, fifthRow.get("coq72c100").toString().trim());//id
        Assertions.assertEquals(2, ((BigDecimal) fifthRow.get("coq72c153")).intValue()); //nSequencia
        Assertions.assertEquals("ObsCont", fifthRow.get("coq72c190").toString().trim()); //cTipoObservacao
        Assertions.assertEquals("2", fifthRow.get("coq72c191").toString().trim()); //xCampo
        Assertions.assertEquals("CST: 40 N PEDIDO: 0000099134. - Apolice seguro: 5400017925 - Seguradora: 61383493000180\n" +
                "                        SOMPO SEGUROS", fifthRow.get("coq72c192").toString().trim()); //xTexto

        Assertions.assertEquals(CHAVE_CTE, sixthRow.get("coq72c100").toString().trim());//id
        Assertions.assertEquals(1, ((BigDecimal) sixthRow.get("coq72c153")).intValue()); //nSequencia
        Assertions.assertEquals("ObsCont", sixthRow.get("coq72c190").toString().trim()); //cTipoObservacao
        Assertions.assertEquals("1", sixthRow.get("coq72c191").toString().trim()); //xCampo
        Assertions.assertEquals("DIM VOLUMES(01): 0,38x0,28x0,41x1", sixthRow.get("coq72c192").toString().trim()); //xTexto


    }

    @Test
    public void validarCamposCteComTabelaKwcte_infdocumentos_infNf() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c305 where cdq72c194 = '" + CHAVE_CTE + "' and cdq72c152 = 'infNF'");//Kwcte_infdocumentos
        Map<String, Object> infNF = result.get(0);

        Assertions.assertEquals("12/08/2019", infNF.get("cdq72c200").toString().trim()); //dEmi
        Assertions.assertEquals("9636", infNF.get("cdq72c199").toString().trim()); //nDoc
        Assertions.assertEquals("123456789", infNF.get("cdq72c196").toString().trim()); //PIN
        Assertions.assertEquals("N", infNF.get("cdq72c202").toString().trim()); //nRoma
        Assertions.assertEquals("N", infNF.get("cdq72c203").toString().trim()); //nPed
        Assertions.assertEquals("57", infNF.get("cdq72c106").toString().trim()); //mod
        Assertions.assertEquals("11", infNF.get("cdq72c204").toString().trim()); //serie
        Assertions.assertEquals("9636", infNF.get("cdq72c199").toString().trim()); //nDoc
        Assertions.assertEquals("12/08/2019", infNF.get("cdq72c200").toString().trim()); //dEmi
        Assertions.assertEquals("500", infNF.get("cdq72c205").toString().trim()); //vBC
        Assertions.assertEquals("0", infNF.get("cdq72c138").toString().trim()); //vICMS
        Assertions.assertEquals("0", infNF.get("cdq72c206").toString().trim()); //vBCST
        Assertions.assertEquals("0", infNF.get("cdq72c207").toString().trim()); //vST
        Assertions.assertEquals("0", infNF.get("cdq72c209").toString().trim()); //vNF
        Assertions.assertEquals("0", infNF.get("cdq72c103").toString().trim()); //nCFOP
        Assertions.assertEquals("0", infNF.get("cdq72c210").toString().trim()); //nPeso
        Assertions.assertEquals("123456789", infNF.get("cdq72c196").toString().trim()); //PIN
    }

    @Test
    public void validarCamposCteComTabelaKwcte_infdocumentos_infNfe() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c305 where cdq72c194 = '" + CHAVE_CTE + "' and cdq72c152 = 'infNFe'");//Kwcte_infdocumentos
        Map<String, Object> infNFe = result.get(0);

        Assertions.assertEquals("29200682749987001188550040000004231833018426", infNFe.get("cdq72c100").toString().trim()); //chave
        Assertions.assertEquals("123456789", infNFe.get("cdq72c196").toString().trim()); //PIN
    }

    @Test
    public void validarCamposCteComTabelaKwcte_infdocumentos_infOutros() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c305 where cdq72c194 = '" + CHAVE_CTE + "' and cdq72c152 = 'infOutros'");//Kwcte_infdocumentos
        Map<String, Object> infOutros = result.get(0);

        Assertions.assertEquals("00", infOutros.get("cdq72c197").toString().trim()); //tpDoc
        Assertions.assertEquals("Teste", infOutros.get("cdq72c198").toString().trim()); //descOutros
        Assertions.assertEquals("9636", infOutros.get("cdq72c199").toString().trim()); //nDoc
        Assertions.assertEquals("12/08/2019", infOutros.get("cdq72c200").toString().trim()); //dEmi
        Assertions.assertEquals(0, ((BigDecimal) infOutros.get("cdq72c201")).intValue()); //vDocFisc
    }

    @Test
    public void validarCamposCteComTabelaKwcte_duto() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c328 where cdq72c100 like '%" + CHAVE_CTE + "%'");//kwcte_duto
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals(0, ((BigDecimal) row.get("cdq72c246")).intValue()); //vTar
        Assertions.assertEquals("2023-08-07", row.get("cdq72c180").toString().trim()); //dIni
        Assertions.assertEquals("2023-08-07", row.get("cdq72c181").toString().trim()); //dFim
    }

    @Test
    public void validarCamposCteComTabelaKwcte_ferrov() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c323 where cfq72c100 like '%" + CHAVE_CTE + "%'");//kwcte_ferrov
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals(0, ((BigDecimal) row.get("cfq72c259")).intValue()); //tpTraf
        Assertions.assertEquals("1", row.get("cfq72c326").toString().trim()); //respFat
        Assertions.assertEquals("2", row.get("cfq72c327").toString().trim()); //ferrEmi
        Assertions.assertEquals("0", row.get("cfq72c262").toString().trim()); //vFrete
    }

    @Test
    public void validarCamposCteComTabelaKwcte_aquav_balsa() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c322 where chq72c100 like '%" + CHAVE_CTE + "%'");//kwcte_aquav_balsa
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("123456789", row.get("chq72c325").toString().trim()); //xBalsa

    }


    @Test
    public void validarCamposCteComTabelaKwcte_aquav() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c320 where chq72c100 like '%" + CHAVE_CTE + "%'");//kwcte_aquav
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals(0, ((BigDecimal) row.get("chq72c247")).intValue()); //vPrest
        Assertions.assertEquals(0, ((BigDecimal) row.get("chq72c248")).intValue()); //vAFRMM
        Assertions.assertEquals("Teste", row.get("chq72c251").toString().trim()); //xNavio
        Assertions.assertEquals("1234567890", row.get("chq72c252").toString().trim()); //nViag
        Assertions.assertEquals("N", row.get("chq72c253").toString().trim()); //direc
        Assertions.assertEquals("1", row.get("chq72c257").toString().trim()); //tpNav
    }

    @Test
    public void validarCamposCteComTabelaKwcte_aquav_lacre() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c321 where chq72c100 like '%" + CHAVE_CTE + "%'");//kwcte_aquav_lacre
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("123456789", row.get("chq72c217").toString().trim()); //nLacre

    }

    @Test
    public void validarCamposCteComTabelaKwcte_peri() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c329 where cpq72c100 like '%" + CHAVE_CTE + "%'");//kwcte_peri
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("1234", row.get("cpq72c275").toString().trim()); //nONU

    }


    @Test
    public void validarCamposCteComTabelaKwcte_aereo() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c319 where caq72c100 like '%" + CHAVE_CTE + "%'");//kwcte_aereo
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("123456789", row.get("caq72c239").toString().trim()); //nMinu
        Assertions.assertEquals("12345678910", row.get("caq72c240").toString().trim()); //nOCA
        Assertions.assertEquals("M", row.get("caq72c244").toString().trim()); //cl
        Assertions.assertEquals("123", row.get("caq72c245").toString().trim()); //cTar
        Assertions.assertEquals(0, ((BigDecimal) row.get("caq72c246")).intValue()); //vTar
        Assertions.assertEquals("1234", row.get("caq72c322").toString().trim()); //xDime
        Assertions.assertEquals("01", row.get("caq72c323").toString().trim()); //cInfManu

    }



    @Test
    public void validarCamposCteComTabelaKwcte_rodo_occ() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c314 where crq72c100 like '%" + CHAVE_CTE + "%'");// kwcte_rodo_occ
        Map<String, Object> row = result.get(0);

        Assertions.assertEquals("11", row.get("crq72c107").toString().trim());// serie
        Assertions.assertEquals("12/08/2019", row.get("crq72c200").toString().trim());// dEmi
        Assertions.assertEquals("123456", row.get("crq72c225").toString().trim());// nOcc


    }



    @Test
    public void validarCamposCteComTabelaKwcte_vprest_comp() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c306 where cpq72c100 like '%" + CHAVE_CTE + "%' order by cpq72c153");//KWCTE_VPREST_COMP
        Map<String, Object> fisrtRow = result.get(0);
        Map<String, Object> secondRow = result.get(1);
        Map<String, Object> thirdRow = result.get(2);
        Map<String, Object> fourthRow = result.get(3);


        Assertions.assertEquals("FRETE PESO", fisrtRow.get("cpq72c157").toString().trim()); //xnome
        Assertions.assertEquals(42.00, ((BigDecimal) fisrtRow.get("cpq72c211")).intValue()); //vComp

        Assertions.assertEquals("GRIS", secondRow.get("cpq72c157").toString().trim()); //xnome
        Assertions.assertEquals(4, ((BigDecimal) secondRow.get("cpq72c211")).intValue()); //vComp 4,81 na tabela

        Assertions.assertEquals("PEDAGIO", thirdRow.get("cpq72c157").toString().trim()); //xnome
        Assertions.assertEquals(6.00, ((BigDecimal) thirdRow.get("cpq72c211")).intValue()); //vComp

        Assertions.assertEquals("POS", fourthRow.get("cpq72c157").toString().trim()); //xnome
        Assertions.assertEquals(6.00, ((BigDecimal) fourthRow.get("cpq72c211")).intValue()); //vComp 6,73
    }

    @Test
    public void validarCamposCteComTabelaKwcte_infcarga_infq() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c307 where ciq72c100 like '%" + CHAVE_CTE + "%' order by ciq72c153");//KWCTE_INFCARGA_INFQ
        Map<String, Object> firstRow = result.get(0);
        Map<String, Object> secondRow = result.get(1);
        Map<String, Object> thirdRow = result.get(2);
        Map<String, Object> fourthRow = result.get(3);
        Map<String, Object> fifthRow = result.get(4);


        Assertions.assertEquals("03", firstRow.get("ciq72c212").toString().trim()); //cUnid
        Assertions.assertEquals("UNIDADE", firstRow.get("ciq72c213").toString().trim()); //tpMed
        Assertions.assertEquals(1, ((BigDecimal) firstRow.get("ciq72c214")).intValue()); //qCarga


        Assertions.assertEquals("03", secondRow.get("ciq72c212").toString().trim()); //cUnid
        Assertions.assertEquals("PARES", secondRow.get("ciq72c213").toString().trim()); //tpMed
        Assertions.assertEquals(0, ((BigDecimal) secondRow.get("ciq72c214")).intValue()); //qCarga

        Assertions.assertEquals("00", thirdRow.get("ciq72c212").toString().trim()); //cUnid
        Assertions.assertEquals("M3", thirdRow.get("ciq72c213").toString().trim()); //tpMed
        Assertions.assertEquals(0, ((BigDecimal) thirdRow.get("ciq72c214")).intValue()); //qCarga

        Assertions.assertEquals("01", fourthRow.get("ciq72c212").toString().trim()); //cUnid
        Assertions.assertEquals("PESO REAL", fourthRow.get("ciq72c213").toString().trim()); //tpMed
        Assertions.assertEquals(2.0000, ((BigDecimal) fourthRow.get("ciq72c214")).intValue()); //qCarga

        Assertions.assertEquals("01", fifthRow.get("ciq72c212").toString().trim()); //cUnid
        Assertions.assertEquals("PESO BASE DE CALCULO", fifthRow.get("ciq72c213").toString().trim()); //tpMed
        Assertions.assertEquals(13, ((BigDecimal) fifthRow.get("ciq72c214")).intValue()); //qCarga


    }

    @Test
    public void validarCamposCteComTabelaKwcte_infresptec() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c338 where rtq72c100 like '%" + CHAVE_CTE + "%'");//KWCTE_INFRESPTEC
        Map<String, Object> row = result.get(0);


        Assertions.assertEquals("01446320000132", row.get("rtq72c154").toString().trim()); //cnpj
        Assertions.assertEquals("Suporte SSW", row.get("rtq720806").toString().trim()); //xcontato
        Assertions.assertEquals("suporte@ssw.inf.br", row.get("rtq720824").toString().trim()); //email
       // Assertions.assertEquals("04133360877", row.get("rtq720824").toString().trim()); //fone DESCONTINUADO
        Assertions.assertEquals("0", row.get("rtq72c385").toString().trim()); //idCSRT
        Assertions.assertEquals("0", row.get("rtq72c386").toString().trim()); //hashCSRT
    }

    @Test
    public void validarCamposCteComTabelaKwcte_infcteanu() {
        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c334 where ciq72c100 like '%" + CHAVE_CTE + "%'");//KWCTE_INFCTEANU
        Map<String, Object> row = result.get(0);


        Assertions.assertEquals("0", row.get("ciq72c318").toString().trim()); //chCte
        Assertions.assertEquals("12/08/2019", row.get("ciq72c200").toString().trim()); //dEmi
    }

    @Test
    void validarCamposCteComTabelaKwcte_infctecomp() {

        List<Map<String, Object>> result = jdbcTemplate.queryForList("select * from fq72c332 where ccq72c100 like '%" + CHAVE_CTE + "%'");//KWCTE_INFCTECOMP

        // deve gravar 2 linhas
        Assertions.assertEquals(2, result.size());

        // validar primeira linha
        Map<String, Object> firstRow = result.get(0);
        Assertions.assertEquals("1", firstRow.get("ccq72c153").toString()); //nsequencia
        Assertions.assertEquals("35240108988083000180570010002473691002473699", firstRow.get("ccq72c318").toString().trim()); //chave

        // validar segunda linha
        Map<String, Object> secondRow = result.get(1);
        Assertions.assertEquals("2", secondRow.get("ccq72c153").toString()); //nsequencia
        Assertions.assertEquals("35240108988083000180570010002473691002473799", secondRow.get("ccq72c318").toString().trim()); //chave
    }
}
