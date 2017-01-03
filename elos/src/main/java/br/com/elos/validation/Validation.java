package br.com.elos.validation;

import br.com.elos.helpers.Parse;
import br.com.elos.helpers.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    private final List<String[]> arrValues   = new ArrayList<>();
    private final List<String[]> arrErrors   = new ArrayList<>();
    
    private final List<Object[]> rules     = new ArrayList<>();
    private final int            prField   = 0;
    private final int            prValue   = 1;
    private final int            prRule    = 2;
    private final int            prMessage = 3;
    
    /**
     * método responsável pela validação do valor inteiro válido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor é um inteiro válido ou 'false' para valor inválido.
     */
    public boolean isInteger(String value) {
        return (new Parse().toInt(value) != null);
    }
    
    /**
     * método responsável pela validação do valor decimal válido.
     * @param value parâmetro string para análise.
     * @return retorna 'true' se valor é um decimal válido ou 'false' para valor inválido.
     */
    public boolean isFloat(String value) {
        return (new Parse().toFloat(value) != null);
    }
    
    /**
     * método responsável pela validação do valor data válido.
     * @param value parâmetro string no formato 'dd/MM/yyyy'.
     * @return retorna 'true' se valor é uma data válida ou 'false' para valor inválido.
     */
    public boolean isDate(String value) {
        return (new Parse().toDate(value, "dd/MM/yyyy") != null);
    }

    /**
     * método responsável pela validação do valor hora válido.
     * @param value parâmetro string no formato 'HH:mm:ss'.
     * @return retorna 'true' se valor é uma hora válida ou 'false' para valor inválido.
     */    
    public boolean isTime(String value) {
        return (new Parse().toDate(value, "HH:mm:ss") != null);
    }

    /**
     * método responsável pela validação do valor data/hora válido.
     * @param value parâmetro string no formato 'dd/MM/yyyy HH:mm:ss'.
     * @return retorna 'true' se valor é uma data/hora válida ou 'false' para valor inválido.
     */    
    public boolean isDateTime(String value) {
        return (new Parse().toDate(value, "dd/MM/yyyy HH:mm:ss") != null);
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
        return new Util().trim(value).length() > 0;
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
        this.rules.clear();
        this.arrValues.clear();
        this.arrErrors.clear();
    }
    
    public void add(String field, Object value, List<Rules> rules, String message) {
        Object newRule[] = {field, value, rules, message};
        this.rules.add(newRule);
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
        for (Object[] field : this.rules) {
            //captura todas as regras existentes
            List<Rules> typeRules = (List<Rules>) field[this.prRule];
            
            //valida regras
            for(Rules type : typeRules) {
                System.out.println(type);
            }
            
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
        }*/
        
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