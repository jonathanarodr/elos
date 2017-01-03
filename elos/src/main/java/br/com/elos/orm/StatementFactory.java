package br.com.elos.orm;

import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.CallableStatement;
import java.util.List;

public class StatementFactory {  

    public PreparedStatement getStatement(PreparedStatement prep, List<Object> values) throws SQLException {
        int i=0;
        
        for (Object value : values) {
            i++;
            prep.setObject(i, value);
        }
        
        return prep;
    }
    
    public CallableStatement getStatement(CallableStatement call, List<Object> values, List<Object> valuesout) throws SQLException {
        int i=0;
        
        for (Object value : values) {
            i++;
            call.setObject(i, value);
        }
        
        for (Object valueout : valuesout) {
            i++;
            call.registerOutParameter(i, (int) valueout);
        }
        
        return call;
    }
    
}