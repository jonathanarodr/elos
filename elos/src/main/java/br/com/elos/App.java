package br.com.elos;

import br.com.elos.logging.LogLevel;

public abstract class App {

    private static String pack = "br.com.company.AppConfig";

    /**
     * SERVE: configurações do servidor, como base url, modo debug permite que
     *        o elos gere logs de depuração e imprima em console seu processamento
     */
    public String server_url = "http://localhost:8080/";
    public boolean server_debug = true;
    public LogLevel server_loglevel = LogLevel.DEBUG;

    /**
     * RESOURCE: indica base de recursos utilizados no gerenciamento de rota
     */
    public String resource = null;

    /**
     * ORM: configurações do banco de dados e orm
     */
    public String db_connection = "mysql";
    public String db_hostname = "localhost";
    public int db_port = 3306;
    public String db_database = "db_name";
    public String db_username = "root";
    public String db_password = "";

    /**
     * FILESYSTEM: indica local de armazenamento de arquivos e tamanhos permitidos
     */
    public String storage_location = "/";
    public long storage_maxsize = 0; //bytes
    public long storage_maxrequestsize = 0; //bytes
    public boolean storage_generateuuid = true;
    public String[] storage_extensions = {"bmp", "png", "jpg", "jpeg"};

    /**
     * AEON
     */
    public String timezone = "America/Sao_Paulo";

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
