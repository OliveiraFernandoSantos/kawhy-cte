package br.com.actionsys.kawhycte.infra.control;

import lombok.Getter;

@Getter
public enum TomadorType {
    TOMA_REM("0", "cteProc/CTe/infCte/rem/CNPJ"),
    TOMA_EXPED("1", "cteProc/CTe/infCte/exped/CNPJ"),
    TOMA_RECEB("2", "cteProc/CTe/infCte/receb/CNPJ"),
    TOMA_DEST("3", "cteProc/CTe/infCte/dest/CNPJ"),
    TOMA_OUTROS("4", "cteProc/CTe/infCte/ide/toma4/CNPJ");
    private final String key;
    private final String aPath;

    TomadorType(String key, String aPath) {
        this.key = key;
        this.aPath = aPath;
    }
}
