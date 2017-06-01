package br.com.elos.orm;

import br.com.elos.helpers.Util;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryBuilder {
    
    private String sql;
    private Map<String,Object> params;
    private Map<String,Object> paramsout;
    private List<Object> values;
    private List<Object> valuesout;

    public QueryBuilder() {
        this.clear();
    }
    
    public String getSql() {
        return this.sql;
    }

    public List<Object> getValues() {
        return this.values;
    }
    
    public List<Object> getValuesOut() {
        return this.valuesout;
    }
    
    private void clear() {
        this.params = new HashMap<String,Object>();
        this.paramsout = new HashMap<String,Object>();
        this.values = new ArrayList<>();
        this.valuesout = new ArrayList<>();
        this.sql = "";
    }
    
    public void createQuery(String sql) {
        this.clear();
        this.sql = sql;
    }
    
    public void setParameter(String param, Object value) {
        this.params.put(param, value);
    }
    
    public void setParameterOut(String paramOut, int typeIndex) {
        this.paramsout.put(paramOut, typeIndex);
    }
    
    public void build() {
        //trata query
        Util util = new Util();
        this.sql = util.ltrim(util.rtrim(this.sql));
        this.sql = (this.sql.endsWith(";")) ? this.sql : this.sql.concat(";");
        int start = 0;
        int end = 0;
        String param = null;
        
        //captura parâmetros da query
        while (this.sql.contains(":")) {
            start = this.sql.indexOf(":");
            end = start+1;
            
            while ((!this.sql.substring(start,end).endsWith(","))
                && (!this.sql.substring(start,end).endsWith(" "))
                && (!this.sql.substring(start,end).endsWith(")"))
                && (!this.sql.substring(start,end).endsWith(";"))) {
                end++;
            }
            
            param = this.sql.substring(start,end-1);
            this.sql = this.sql.replace(param, "?");
            
            if (this.params.containsKey(param.substring(1))) {
                this.values.add(this.params.get(param.substring(1)));
            }
            
            if (this.paramsout.containsKey(param.substring(1))) {
                this.valuesout.add(this.paramsout.get(param.substring(1)));
            }
            
        }
    }
    
}