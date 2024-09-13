package br.com.actionsys.kawhycte;

import br.com.actionsys.kawhycommons.erro.DbErrosService;
import br.com.actionsys.kawhycommons.infra.dbsavexml.DbSaveXmlService;
import br.com.actionsys.kawhycommons.infra.license.DecryptService;
import br.com.actionsys.kawhycommons.infra.license.LicenseService;
import br.com.actionsys.kawhycte.infra.control.ControlService;
import br.com.actionsys.kawhycte.item.cancel.CancelService;
import br.com.actionsys.kawhyimport.metadata.ImportService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.util.ArrayList;

@SpringBootTest(classes = {LicenseService.class, DecryptService.class})
@MockBean({DbErrosService.class, CancelService.class, DbSaveXmlService.class, ImportService.class, ControlService.class})
@Slf4j
@MockBean(Schedule.class)
public class Process_validateLicenseModuloTest {

    @MockBean
    private LicenseService licenseService;
    @SpyBean
    private Orchestrator orchestrator;

    @Test
    public void trueTest() {
        Mockito.doReturn(new ArrayList<>()).when(orchestrator).getFiles();
        Mockito.doReturn(true).when(licenseService).validateLicenseModule(Mockito.any());

        Assertions.assertDoesNotThrow(() -> orchestrator.process());

        Mockito.verify(orchestrator, Mockito.times(1)).getFiles();
    }

    @Test
    public void falseTest() {
        Mockito.doReturn(new ArrayList<>()).when(orchestrator).getFiles();
        Mockito.doReturn(false).when(licenseService).validateLicenseModule(Mockito.any());

        Assertions.assertDoesNotThrow(() -> orchestrator.process());

        Mockito.verify(orchestrator, Mockito.never()).getFiles();
    }

    @Test
    public void exceptionTest() {

        Mockito.doThrow(new RuntimeException("Licença não encontrada")).when(licenseService).validateLicenseModule(Mockito.any());

        Assertions.assertThrows(RuntimeException.class, () -> orchestrator.process());

        Mockito.verify(orchestrator, Mockito.never()).getFiles();
    }
}
