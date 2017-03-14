package br.com.elos.orm;

import br.com.elos.App;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionFactory {
    
    //configurações de acesso ao banco de dados
    private static final String driver = "com.mysql.jdbc.Driver";
    
    public static Connection getConnection() {
        App app = App.getInstance();
        
        String url = "jdbc:mysql://" + app.db_hostname + ":" + app.db_port + "/" + app.db_database;
        Connection connection = null;
        try {
            Class.forName(driver);
            connection = DriverManager.getConnection(url, app.db_username, app.db_password);
        } catch (SQLException | ClassNotFoundException ex) {
            Logger.getLogger(ConnectionFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return connection;
    }
    
}
