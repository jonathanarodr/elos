package br.com.elos.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    
    //configurações de acesso ao banco de dados
	private static final String user = "user_name";
	private static final String password = "user_password";
	private static final String database = "db_name";
	private static final String hostname = "domain_name";
	private static final String driver = "com.mysql.jdbc.Driver";
	private static final int port = 3306;
    
    public static Connection getConnection() {
        String url = "jdbc:mysql://" + ConnectionFactory.hostname + ":" + ConnectionFactory.port + "/" + ConnectionFactory.database;
        Connection connection = null;
        try {
            Class.forName(ConnectionFactory.driver);
            connection = DriverManager.getConnection(url, ConnectionFactory.user, ConnectionFactory.password);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
    }
    
}
