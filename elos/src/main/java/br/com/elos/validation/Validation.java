package br.com.elos.validation;

import br.com.elos.helpers.Parse;
import br.com.elos.helpers.Util;
import br.com.elos.orm.DB;
import br.com.elos.orm.QueryBuilder;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private final int prValidator = 3;
    
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
     * @param value parâmetro a ser validado
     * @param format parâmetro string no formato a ser validado.
     * @return
     */
    public boolean isDate(Object value, String format) {
        if (value == null) {
            return false;
        }
        
        Parse parse = new Parse();
        
        if (value instanceof GregorianCalendar) {
        	return (parse.toString((Calendar)value, format) != null);
        } else {
        	return (parse.toCalendar(value.toString(), format) != null);
        }
    }
    
    /**
     * método responsável pela validação do valor hora válido.
     * @param value parâmetro a ser validado
     * @param format parâmetro string no formato a ser validado.
     * @return
     */
    public boolean isTime(Object value, String format) {
        if (value == null) {
            return false;
        }
        
        return (new Parse().toDate(value.toString(), format) != null);
    }

    /**
     * método responsável pela validação do valor data/hora válido.
     * @param value parâmetro a ser validado
     * @param format parâmetro string no formato a ser validado.
     * @return retorna 'true' se valor é uma data/hora válida ou 'false' para valor inválido.
     */    
    public boolean isDateTime(Object value, String format) {
        if (value == null) {
            return false;
        }
        
        return (new Parse().toDate(value.toString(), format) != null);
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
    
    //public boolean isUnique(Object value, String table, String column, String columnExcept, Object except) {
    public boolean isUnique(Object value, String[] param) {
        if ((param.length != 2) && (param.length != 4)) {
            throw new IllegalArgumentException("Validation rule unique requires 2 or 4 parameters");
        }
        
        DB db = new DB();
        QueryBuilder querybuilder = new QueryBuilder();
        
        String sql = "select 1"
                   + "  from " + param[0]
                   + " where " + param[1] + " = :p1 ";
        
        if (param.length == 2) {
            querybuilder.createQuery(sql);
            querybuilder.setParameter("p1", value);
        } else {
            sql += "and " + param[2] + " <> " + param[3];
            
            querybuilder.createQuery(sql);
            querybuilder.setParameter("p1", value);
        }
        
        sql += ";";
        
        try {
            return !db.select(querybuilder).next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            db.close();
        }
    }
    
    public boolean isExists(Object value, String[] param) {
        if ((param.length != 2) && (param.length != 4)) {
            throw new IllegalArgumentException("Validation rule exists requires 2 or 4 parameters");
        }
        
        DB db = new DB();
        QueryBuilder querybuilder = new QueryBuilder();
        
        String sql = "select 1"
                   + "  from " + param[0]
                   + " where " + param[1] + " = :p1 ";
        
        if (param.length == 2) {
            querybuilder.createQuery(sql);
            querybuilder.setParameter("p1", value);
        } else {
            sql += "and " + param[2] + " = " + param[3];
            
            querybuilder.createQuery(sql);
            querybuilder.setParameter("p1", value);
        }
        
        sql += ";";
        
        try {
            return db.select(querybuilder).next();
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        } finally {
            db.close();
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
    public void add(String field, Object value, String rules, Validator validator) {
        Object newRule[] = {value, rules, null, validator};
        this.rules.put(field, newRule);
    }
    
    public void add(String field, Object value, String rules, String message) {
        Object newRule[] = {value, rules, message, null};
        this.rules.put(field, newRule);
    }
    
    public void add(String field, Object value, String rules) {
        Object newRule[] = {value, rules, null, null};
        this.rules.put(field, newRule);
    }
    
    /**
     * método responsável pela validação dos field's adicionados pelo método <strong>add()</strong>.
     * @return retorna 'true' caso todos os field's são válidos ou 'false' caso tenha ocorrido o preenchimento inválido dos field's.
     */
    public boolean validate() {
        System.out.println("Start validation...");
        
        Object value = null;
        String message = null;
        
        //percorre todos os fields registrados no list
        for (Map.Entry<String,Object[]> field : this.rules.entrySet()) {
            //captura parâmetros
            value = field.getValue()[this.prValue];
            message = (String) field.getValue()[this.prMessage];
            
            //captura todas as regras existentes para validação
            String rules[] = field.getValue()[this.prRule].toString().split(";");
            
            //valida regras
            for(String rule : rules) {                
                //se houver parâmetro adicional, captura informações
                String param = "";
                if (rule.indexOf("[") > 0) {
                    param = rule.substring(rule.indexOf("[")+1, rule.indexOf("]"));
                    rule = rule.replace("[" + param + "]", "");
                }
                
                //verifica utilizaçao de validator
                Validator validator = (Validator) field.getValue()[this.prValidator];
                
                if ((validator != null) && (validator.messages() != null)) {
                	message = validator.messages().get(field.getKey() + "." + rule);
                }
                
                //configura enum
                Rules ruleEnum = Rules.valueOf(rule.toUpperCase());
                ruleEnum.setParam(param);
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
                    	if ((param == null) || (param.equals(""))) {
                    		param = "yyyy-MM-dd";
                    		ruleEnum.setParam(param);
                        }
                        if ((!this.isDate(value, param)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case TIME : {
                    	if ((param == null) || (param.equals(""))) {
                    		param = "HH:mm:ss";
                    		ruleEnum.setParam(param);
                        }
                        if ((!this.isTime(value, param)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case DATETIME : {
                    	if ((param == null) || (param.equals(""))) {
                    		param = "yyyy-MM-dd HH:mm:ss";
                    		ruleEnum.setParam(param);
                        }
                        if ((!this.isDateTime(value, param)) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case MINLENGTH : {
                        if ((!this.isMinLength(value, Integer.parseInt(ruleEnum.param()))) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case MAXLENGTH : {
                        if ((!this.isMaxLength(value, Integer.parseInt(ruleEnum.param()))) && (!this.isError(field.getKey()))) {
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
                    
                    case UNIQUE : {
                        if ((!this.isUnique(value, ruleEnum.param().split(","))) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                    
                    case EXISTS : {
                        if ((!this.isExists(value, ruleEnum.param().split(","))) && (!this.isError(field.getKey()))) {
                            this.errors.put(field.getKey(), ruleEnum.message());
                        }
                        break;
                    }
                }
            }
        }
        
        if (this.errors.isEmpty()) {
            System.out.println("Validation ok");
        } else {
            System.out.println("Failed to validate rules" + "\nErrors: " + this.getErrors());
        }
        
        return this.errors.isEmpty();
    }
    
    public void setError(String key, String message) {
    	this.errors.put(key, message);	
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