package br.com.elos.validation;

public enum Rules {
    REQUIRED,
    INTEGER,
    FLOAT,
    DATE,
    TIME,
    DATETIME,
    MINLENGTH,
    MAXLENGTH,
    EMAIL;
    
    private int length;
    
    Rules() {
        this.length = 0;
    }

    public int getLength() {
        return length;
    }

    public Rules setLength(int length) {
        this.length = length;
        return this;
    }
    
    public String getMessage() {
        switch (this) {
            case REQUIRED : return "Este campo é requerido";
            case INTEGER : return "Número inteiro inválido";
            case FLOAT : return "Número decimal inválido";
            case DATE : return "Data inválida, informe algo como 00/00/0000";
            case TIME : return "Hora inválida, informe algo como 00:00:00";
            case DATETIME : return "Data inválida, informe algo como 00/00/0000 00:00:00";
            case MINLENGTH : return "Informe no mínimo " + this.length + " caracteres";
            case MAXLENGTH : return "Informe no máximo " + this.length + " caracteres";
            case EMAIL : return "E-mail inválido, informe algo como exemplo@elos.com.br";
            default : return null;
        }
    }
}
