package br.com.elos.orm;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import br.com.elos.App;

public class ConnectionFactory {
    
    //configurações de acesso ao banco de dados
    private static final String user = App.getInstance().user_db;
    private static final String password = App.getInstance().password_db;
    private static final String database = App.getInstance().database;
    private static final String hostname = App.getInstance().hostname;
    private static final String driver = App.getInstance().driver;
    private static final int port = App.getInstance().port;
    
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
