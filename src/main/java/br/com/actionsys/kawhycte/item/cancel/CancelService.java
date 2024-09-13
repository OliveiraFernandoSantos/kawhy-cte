package br.com.actionsys.kawhycte.item.cancel;

import br.com.actionsys.kawhycommons.Constants;
import br.com.actionsys.kawhycommons.infra.util.DateUtil;
import br.com.actionsys.kawhycommons.infra.util.HostUtil;
import br.com.actionsys.kawhycommons.integration.IntegrationContext;
import br.com.actionsys.kawhycommons.types.KawhyType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static br.com.actionsys.kawhycommons.infra.util.APathUtil.execute;

@Slf4j
@Component
public class CancelService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //Salvar na tabela de cancelamento
    public void insert(IntegrationContext item) {
        Document d = item.getDocument();
        Map<String, Object> v = new HashMap<>();

        v.put("ccq72c100", item.getId());//CTE_ID
        v.put("ccq721022", execute(d, "procEventoCTe/@versao"));//VERSAO
        v.put("ccq720717", execute(d, "procEventoCTe/eventoCTe/infEvento/tpAmb"));//TPAMB
        v.put("ccq721023", Constants.CTE_EVCTE_XJUST);//XSERV
        v.put("ccq72c318", execute(d, "procEventoCTe/eventoCTe/infEvento/chCTe"));//CHCTE
        v.put("ccq721024", execute(d, "procEventoCTe/eventoCTe/infEvento/detEvento/evCancCTe/nProt"));//NPROT
        v.put("ccq720723", execute(d, "procEventoCTe/eventoCTe/infEvento/detEvento/evCancCTe/xJust"));//XJUST
        v.put("ccq721025", execute(d, "procEventoCTe/retEventoCTe/infEvento/verAplic"));//VERAPLIC
        v.put("ccq720961", execute(d, "procEventoCTe/retEventoCTe/infEvento/cStat"));//CSTAT
        v.put("ccq720833", execute(d, "procEventoCTe/retEventoCTe/infEvento/xMotivo"));//XMOTIVO
        v.put("ccq720702", execute(d, "procEventoCTe/retEventoCTe/infEvento/cOrgao"));//CUF
        v.put("ccq721026", execute(d, "procEventoCTe/eventoCTe/infEvento/dhEvento"));//DHRECBTO
        v.put("ccq721027", execute(d, "procEventoCTe/retEventoCTe/infEvento/nProt"));//NPROTRET
        v.put("ccuser", KawhyType.KAWHY_CTE.getServiceName());//USERID
        v.put("ccpid", KawhyType.KAWHY_CTE.getServiceName());//PID
        v.put("ccjobn", HostUtil.getHost());//jobn

        Date now = new Date();
        v.put("ccupmj", DateUtil.formatDateToJulian(now));//UPMJ
        v.put("ccupmt", DateUtil.formatTimeToDb(now).replaceAll(":", ""));//UPMT

        v.put("ccq72c350", Constants.ZERO);//STATUS01
        v.put("ccq72c351", Constants.ZERO);//STATUS02
        v.put("ccq72c352", Constants.ZERO);//STATUS03
        v.put("ccq72c353", Constants.ZERO);//STATUS04
        v.put("ccq72c354", Constants.ZERO);//STATUS05

        String dhRecbto = execute(d, "procEventoCTe/retEventoCTe/infEvento/dhRegEvento");
        v.put("ccq720962", DateUtil.formatDateSefazToDb(dhRecbto));//DTCONSEFAZ
        v.put("ccq720963", DateUtil.formatTimeSefazToDb(dhRecbto));//HRCONSEFAZ

        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        simpleJdbcInsert.setTableName("fq72c336");//KWCTE_CANCELADAS
        simpleJdbcInsert.execute(v);
    }
}

