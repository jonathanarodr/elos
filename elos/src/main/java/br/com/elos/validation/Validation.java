package br.com.elos.validation;

import br.com.elos.helpers.Parse;
import br.com.elos.helpers.Util;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    
    private final Map<String,Object[]> rules = new HashMap<>();
    private final Map<String,String> errors = new HashMap<>();
    private final int prValue = 0;
    private final int prRule = 1;
    private final int prMessage = 2;
    
    /**
     * método responsável pela validação do valor inteiro válido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor é um inteiro válido ou 'false' para valor inválido.
     */
    public boolean isInteger(Object value) {
        if (value == null) {
            return false;
        }
        
        return (new Parse().toInt(value.toString()) != null);
    }
    
    /**
     * método responsável pela validação do valor decimal válido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor é um decimal válido ou 'false' para valor inválido.
     */
    public boolean isFloat(Object value) {
        if (value == null) {
            return false;
        }
        
        return (new Parse().toFloat(value.toString()) != null);
    }
    
    /**
     * método responsável pela validação do valor data válido.
     * @param value parâmetro string no formato 'dd/MM/yyyy'.
     * @return retorna 'true' se valor é uma data válida ou 'false' para valor inválido.
     */
    public boolean isDate(Object value) {
        if (value == null) {
            return false;
        }
        
        return (new Parse().toDate(value.toString(), "dd/MM/yyyy") != null);
    }

    /**
     * método responsável pela validação do valor hora válido.
     * @param value parâmetro string no formato 'HH:mm:ss'.
     * @return retorna 'true' se valor é uma hora válida ou 'false' para valor inválido.
     */    
    public boolean isTime(Object value) {
        if (value == null) {
            return false;
        }
        
        return (new Parse().toDate(value.toString(), "HH:mm:ss") != null);
    }

    /**
     * método responsável pela validação do valor data/hora válido.
     * @param value parâmetro string no formato 'dd/MM/yyyy HH:mm:ss'.
     * @return retorna 'true' se valor é uma data/hora válida ou 'false' para valor inválido.
     */    
    public boolean isDateTime(Object value) {
        if (value == null) {
            return false;
        }
        
        return (new Parse().toDate(value.toString(), "dd/MM/yyyy HH:mm:ss") != null);
    }
   
    /**
     * método responsável pela validação de quantidade mínima de carácteres.
     * @param value parâmetro string para análise.
     * @param min quantidade mínima exigida.
     * @return retorna 'true' se valor atingiu o mínimo desejado ou 'false' para um valor inferior.
     */
    public boolean isMinLength(Object value, Integer min) {
        if (value == null) {
            return false;
        }
        
        return value.toString().length() >= min;
    }
    
    /**
     * método responsável pela validação de quantidade máxima de carácteres.
     * @param value parâmetro string para análise.
     * @param max quantidade máxima exigida.
     * @return retorna 'true' se valor é menor ou igual ao máximo exigido ou 'false' para um valor superior.
     */
    public boolean isMaxLength(Object value, Integer max) {
        if (value == null) {
            return false;
        }
        
        return value.toString().length() <= max;
    }
    
    /**
     * método responsável pela verificação de preenchimento requerido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor foi preenchido ou 'false' para valor nulo.
     */
    public boolean isRequired(Object value) {
        if (value == null) {
            return false;
        }
        
        return new Util().trim(value.toString()).length() > 0;
    }
    
    /**
     * método responsável pela verificação de um e-mail válido.
     * @param value parâmetro e-mail para análise.
     * @return retorna 'true' se e-mail é válido ou 'false' para e-mail inválido.
     */
    public boolean isEmail(Object value) {
        if (value == null) {
            return false;
        }
        
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$"); 
        Matcher m = p.matcher(value.toString()); 
        
        if (m.find()){
            return true;
        } else {
            return false;
        }  
    }
    
    /**
     * método responsável por limpar array de valores e erros da classe, deve ser chamado antes da chamada do método <strong>add()</strong>.
     */
    public void clear() {
        this.rules.clear();
        this.errors.clear();
    }
    
    /**
     * método responsável por adicionar rules para validação.
     * @param field nome do campo responsável pelo valor informado.
     * @param value valor presente no campo responsável.
     * @param rules regras para validação.
     * @param message mensagem padrão caso validação seja inválida.
     */
    public void add(String field, Object value, String rules, String message) {
        Object newRule[] = {value, rules, message};
        this.rules.put(field, newRule);
    }
    
    public void add(String field, Object value, String rules) {
        Object newRule[] = {value, rules, null};
        this.rules.put(field, newRule);
    }
    
    /**
     * método responsável pela validação dos field's adicionados pelo método <strong>add()</strong>.
     * @return retorna 'true' caso todos os field's são válidos ou 'false' caso tenha ocorrido o preenchimento inválido dos field's.
     */
    public boolean validate() {
        Object value = null;
        String message = null;
        
        //percorre todos os fields registrados no list
        for (Map.Entry<String,Object[]> field : this.rules.entrySet()) {
            //captura parâmetros
            value = (Object) field.getValue()[this.prValue];
            message = (String) field.getValue()[this.prMessage];
            
            //captura todas as regras existentes para validação
            String rules[] = field.getValue()[this.prRule].toString().split(";");
            
            //valida regras
            for(String rule : rules) {                
                //se houver parâmetro adicional para min ou max, captura informações
                int length = 0;
                if (rule.indexOf("[") > 0) {
                    length = Integer.parseInt(rule.substring(rule.indexOf("[")+1, rule.indexOf("]")));
                    rule   = rule.replace("[" + length + "]", "");
                }
                
                //configura enum
                Rules ruleEnum = Rules.valueOf(rule.toUpperCase());
                ruleEnum.setLength(length);
                ruleEnum.setMessage(message);
                
                switch (ruleEnum) {
                    case REQUIRED : {
                        if ((!this.isRequired(value)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case INTEGER : {
                        if ((!this.isInteger(value)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case FLOAT : {
                        if ((!this.isFloat(value)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case DATE : {
                        if ((!this.isDate(value)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case TIME : {
                        if ((!this.isTime(value)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case DATETIME : {
                        if ((!this.isDateTime(value)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case MINLENGTH : {
                        if ((!this.isMinLength(value, ruleEnum.length())) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case MAXLENGTH : {
                        if ((!this.isMaxLength(value, ruleEnum.length())) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case EMAIL : {
                        if ((!this.isEmail(value)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                }
            }
        }
        
        return this.errors.isEmpty();
    }
    
    /**
     * método responsável por retornar uma lista de erros de validação.
     * @return retorna erros encontrados após a chamada do método <strong>validate()</strong>.
     */
    public Map<String,String> getErrors() {
        return this.errors;
    }
    
    /**
     * método responsável por retornar a mensagem de erro
     * @param field nome do campo a ser localizado na lista de erros.
     * @return 
     */
    public Object findError(String field) {
        return this.errors.get(field);
    }
    
    /**
     * método responsável pela localização de field's que originaram um erro após sua validação.
     * @param field nome do campo a ser localizado na lista de erros.
     * @return retorna 'true' caso o field do erro seja localizado ou 'false' caso o field não seja localizado.
     */
    public boolean isError(String field) {
        return this.errors.containsKey(field);
    }
    
}