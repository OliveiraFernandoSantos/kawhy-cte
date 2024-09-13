package br.com.actionsys.kawhycte;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@EnableScheduling
@SpringBootApplication(scanBasePackages = "br.com.actionsys")
@EntityScan("br.com.actionsys")
@Component
@Slf4j
public class KawhyCteApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(KawhyCteApplication.class, args);
	}

    @Autowired
    private Environment env;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("------------- "+ env.getProperty("pom.project.name") + " " + env.getProperty("pom.project.version") + " -------------");
        log.info("Versão do java no pom : " + env.getProperty("pom.java.version")+"\n");
        log.info("-------------Banco de dados -------------");
        log.info("db.driver : " + env.getProperty("db.driver"));
        log.info("url : " + env.getProperty("db.url"));
        log.info("biblioteca : " + env.getProperty("db.schema"));
        log.info("usuario : " + env.getProperty("db.user")+"\n");
        log.info("-------------KaWhyCte config-------------");
        log.info("schedule.delay-seconds : " + env.getProperty("schedule.delay-seconds")+"\n");
        log.info("-------------Configurações de pastas-------------");
        log.info("dir.base : " + env.getProperty("dir.base"));
        log.info("dir.entrada : " + env.getProperty("dir.entrada"));
        log.info("dir.saida : " + env.getProperty("dir.saida"));
        log.info("dir.consulta : " + env.getProperty("dir.consulta"));
        log.info("dir.erro : " + env.getProperty("dir.erro"));
        log.info("dir.duplicados : " + env.getProperty("dir.duplicados"));
        log.info("dir.log : " + env.getProperty("dir.log"));
        log.info("-------------------------------------------------------------");
    }
}
