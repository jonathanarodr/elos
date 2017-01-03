package br.com.elos.validation;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation1 {
    private final List<String[]> arrValues   = new ArrayList<>();
    private final List<String[]> arrErrors   = new ArrayList<>();
    private final int            prField     = 0;
    private final int            prLabel     = 1;
    private final int            prRule      = 2;
    private final int            prMessage   = 3;
    private final String         tpInteger   = "integer";
    private final String         tpFloat     = "float";
    private final String         tpDate      = "date";
    private final String         tpTime      = "time";
    private final String         tpDateTime  = "datetime";
    private final String         tpMinLength = "minlength";
    private final String         tpMaxLength = "maxlength";
    private final String         tpRequired  = "required";
    private final String         tpEmail     = "email";
    
    /**
     * método privado responsável pela conversão de string para data.
     * @param date data em string.
     * @param format formato da data desejado para conversão.
     * @return retorna data convertida no formato desejado.
     * @throws Exception gera uma exceção caso ocorra algum erro de conversão.
     */
    private Date strToDate(String dateStr, String format) throws Exception {
        java.util.Date date;
        SimpleDateFormat formato = new SimpleDateFormat(format);
        formato.setLenient(false);

        try {
            date = formato.parse(dateStr);
        } catch (Exception ex) {
            date = null;
        }

        return date;
    }
    
    /**
     * método privado responsável pela remoção de todos espaços da string.
     * @param value parâmetro string para análise.
     * @return retorna string sem espaços.
     */
    private String trim(String value) {
        if (value == null) {
            value = "";
        }

        value = value.replaceAll(" ", "");

        return value;
    }
    
    /**
     * método responsável pela validação do valor inteiro válido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor é um inteiro válido ou 'false' para valor inválido.
     */
    public boolean isInteger(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
    
    /**
     * método responsável pela validação do valor decimal válido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor é um decimal válido ou 'false' para valor inválido.
     */
    public boolean isFloat(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
    
    /**
     * método responsável pela validação do valor data válido.
     * @param value parâmetro string no formato 'dd/MM/yyyy'.
     * @return retorna 'true' se valor é uma data válida ou 'false' para valor inválido.
     */
    public boolean isDate(String value) {
        try {
            this.strToDate(value, "dd/MM/yyyy");
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    /**
     * método responsável pela validação do valor hora válido.
     * @param value parâmetro string no formato 'HH:mm:ss'.
     * @return retorna 'true' se valor é uma hora válida ou 'false' para valor inválido.
     */    
    public boolean isTime(String value) {
        try {
            this.strToDate(value, "HH:mm:ss");
            return true;
        } catch(Exception ex) {
            return false;
        }
    }

    /**
     * método responsável pela validação do valor data/hora válido.
     * @param value parâmetro string no formato 'dd/MM/yyyy HH:mm:ss'.
     * @return retorna 'true' se valor é uma data/hora válida ou 'false' para valor inválido.
     */    
    public boolean isDateTime(String value) {
        try {
            this.strToDate(value, "dd/MM/yyyy HH:mm:ss");
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
   
    /**
     * método responsável pela validação de quantidade mínima de carácteres.
     * @param value parâmetro string para análise.
     * @param min quantidade mínima exigida.
     * @return retorna 'true' se valor atingiu o mínimo desejado ou 'false' para um valor inferior.
     */
    public boolean isMinLength(String value, Integer min) {
        return value.length() >= min;
    }
    
    /**
     * método responsável pela validação de quantidade máxima de carácteres.
     * @param value parâmetro string para análise.
     * @param max quantidade máxima exigida.
     * @return retorna 'true' se valor é menor ou igual ao máximo exigido ou 'false' para um valor superior.
     */
    public boolean isMaxLength(String value, Integer max) {
        return value.length() <= max;
    }
    
    /**
     * método responsável pela verificação de preenchimento requerido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor foi preenchido ou 'false' para valor nulo.
     */
    public boolean isRequired(String value) {        
        return this.trim(value).length() > 0;
    }
    
    /**
     * método responsável pela verificação de um e-mail válido.
     * @param value parâmetro e-mail para análise.
     * @return retorna 'true' se e-mail é válido ou 'false' para e-mail inválido.
     */
    public boolean isEmail(String value) {
        Pattern p = Pattern.compile("^[\\w-]+(\\.[\\w-]+)*@([\\w-]+\\.)+[a-zA-Z]{2,7}$"); 
        Matcher m = p.matcher(value); 
        
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
        this.arrValues.clear();
        this.arrErrors.clear();
    }
    
    /**
     * método responsável por adicionar valores para validação.
     * @param field nome do campo responsável pelo valor informado.
     * @param label valor presente no campo responsável.
     * @param rule regras para validação <strong>integer</strong>, <strong>float</strong>, <strong>date</strong>, <strong>time</strong>,<strong>datetime</strong>, <strong>minlength[0]</strong>, <strong>maxlength[0]</strong>, <strong>required</strong>, <strong>email</strong>
     * @param message mensagem padrão caso validação seja inválida.
     * <br><br><strong>Exemplo: </strong> add('nome', 'Nome do usuário', 'required;minlength[3];maxlength[150]', 'Informe o nome do usuário');
     */
    public void add(String field, String label, String rule, String message) {
        String valueAdd[] = {field, label, rule, message};
        this.arrValues.add(valueAdd);
    }
    
    /**
     * método responsável pela validação dos field's adicionados pelo método <strong>add()</strong>.
     * @return retorna 'true' caso todos os field's são válidos ou 'false' caso tenha ocorrido o preenchimento inválido dos field's.
     */
    public boolean validate() {                        
        //percorre todos os fields registrados no list
        for (String[] arrValue : this.arrValues) {
            
            //captura todas as regras existentes
            String arrRule[] = arrValue[this.prRule].split(";");
            
            //valida regras
            for (int i=0; i < arrRule.length; i++) {
                String rule   = arrRule[i];
                int    length = 0;
                
                //se houver parâmetro adicional, captura informações
                if (rule.indexOf("[") > 0) {
                    length = Integer.parseInt(rule.substring(rule.indexOf("[")+1, rule.indexOf("]")));
                    rule   = arrRule[i].replace("[" + length + "]", "");
                }
                        
                switch(rule) {
                    case tpRequired : {
                        if ((!this.isRequired(arrValue[this.prLabel])) && (!this.findError(arrValue[this.prField]))) {
                            String arrError[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrError);
                        }
                        break;
                    }

                    case tpInteger : {
                        if ((!this.isInteger(arrValue[this.prLabel])) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }

                    case tpFloat : {
                        if ((!this.isFloat(arrValue[this.prLabel])) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }

                    case tpDate : {
                        if ((!this.isDate(arrValue[this.prLabel])) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }

                    case tpTime : {
                        if ((!this.isTime(arrValue[this.prLabel])) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }

                    case tpDateTime : {
                        if ((!this.isDateTime(arrValue[this.prLabel])) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }

                    case tpMinLength : {
                        if ((!this.isMinLength(arrValue[this.prLabel], length)) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }

                    case tpMaxLength : {
                        if ((!this.isMaxLength(arrValue[this.prLabel], length)) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }

                    case tpEmail : {
                        if ((!this.isEmail(arrValue[this.prLabel])) && (!this.findError(arrValue[this.prField]))) {
                            String arrErro[] = {arrValue[this.prField], arrValue[this.prMessage]};
                            this.arrErrors.add(arrErro);
                        }
                        break;
                    }
                    default : break;
                }
            }
            
            System.out.println("Validade: field[" + arrValue[this.prField] + "] - label[" + arrValue[this.prLabel] + "] - rule[" + arrValue[this.prRule] + "] - message[" + arrValue[this.prMessage] + "]");
        }
        
        return this.arrErrors.isEmpty();
    }
    
    /**
     * método responsável por retornar uma lista de erros de validação.
     * @return retorna erros encontrados após a chamada do método <strong>validate()</strong>.
     */
    public List<String[]> getErrors() {
        return arrErrors;
    }
    
    /**
     * método responsável pela localização de field's que originaram um erro após sua validação.
     * @param field nome do field a ser localizado na lista de erros.
     * @return retorna 'true' caso o field do erro seja localizado ou 'false' caso o field não seja localizado.
     */
    public boolean findError(String field) {
        for (String[] arrError : this.arrErrors) {
            if (arrError[this.prField].contains(field)) {
                return true;
            }
        }
        
        return false;
    }
    
}