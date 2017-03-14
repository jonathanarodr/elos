package br.com.elos;

import br.com.elos.logging.LogLevel;

public abstract class App {

    private static String pack = "br.com.company.AppConfig";

    /**
     * SERVE: configurações do servidor, como base url, modo debug permite que
     *        o elos gere logs de depuração e imprima em console seu processamento
     */
    public static String serve_url = "http://localhost:8084/";
    public static boolean serve_debug = true;
    public static LogLevel serve_loglevel = LogLevel.DEBUG;

    /**
     * RESOURCE: indica base de recursos utilizados no gerenciamento de rota
     */
    public static String resource = "br.com.company.controllers";

    /**
     * ORM: configurações do banco de dados e orm
     */
    public static String db_connection = "mysql";
    public static String db_hostname = "localhost";
    public static int db_port = 3306;
    public static String db_database = "db_name";
    public static String db_username = "root";
    public static String db_password = "123";

    public static App getInstance(String pack) {
        App.pack = pack;
        return App.getInstance();
    }

    public static App getInstance() {
        try {
            Class<?> classe = Class.forName(App.pack);
            return (App) classe.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }

}
