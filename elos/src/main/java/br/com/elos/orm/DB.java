package br.com.elos.orm;

import java.sql.Connection;

public class DB extends MySQL {
    
    /**
     * utiliza ConnectionFactory para persistência padrão
     */
    public DB() {
        this.connection = new ConnectionFactory().getConnection();
    }
    
    /**
     * utiliza connection jdbc configurada manualmente
     * @param connection 
     */
    public DB(Connection connection) {
        this.connection = connection;
    }
    
}
