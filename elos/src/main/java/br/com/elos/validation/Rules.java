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
    EMAIL,
    UNIQUE,
    EXISTS;
    
    private String message;
    private String param;

    private String messageDef() {
        switch (this) {
            case REQUIRED : return "Este campo é requerido";
            case INTEGER : return "Número inteiro inválido";
            case FLOAT : return "Número decimal inválido";
            case DATE : return "Digite uma data válida";
            case TIME : return "Digite uma hora válida";
            case DATETIME : return "Data/hora inválida para o formato " + this.param;
            case MINLENGTH : return "Informe no mínimo " + this.param + " caracteres";
            case MAXLENGTH : return "Informe no máximo " + this.param + " caracteres";
            case EMAIL : return "E-mail inválido, informe algo como exemplo@dominio.com.br";
            case UNIQUE : return "O valor informado já existe";
            case EXISTS : return "O valor informado existe";
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
    
    public String param() {
        return this.param;
    }

    public void setParam(String param) {
        this.param = param;
    }
    
}
