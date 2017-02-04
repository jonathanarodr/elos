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
    private String message;

    private String messageDef() {
        switch (this) {
            case REQUIRED : return "Este campo é requerido";
            case INTEGER : return "Número inteiro inválido";
            case FLOAT : return "Número decimal inválido";
            case DATE : return "Data inválida, informe algo como 00/00/0000";
            case TIME : return "Hora inválida, informe algo como 00:00:00";
            case DATETIME : return "Data inválida, informe algo como 00/00/0000 00:00:00";
            case MINLENGTH : return "Informe no mínimo " + this.length + " caracteres";
            case MAXLENGTH : return "Informe no máximo " + this.length + " caracteres";
            case EMAIL : return "E-mail inválido, informe algo como exemplo@dominio.com.br";
            default : return null;
        }
    }
    
    public String message() {
        if (this.message == null) {
            return this.messageDef();
        }
        
        return this.message;
    }
    
    public void setMessage(String message) {
        if (message == null) {
            message = this.messageDef();
        }
        
        this.message = message;
    }
    
    public int length() {
        return this.length;
    }

    public void setLength(int length) {
        this.length = length;
    }
    
}
