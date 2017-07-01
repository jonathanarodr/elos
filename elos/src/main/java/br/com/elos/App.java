package br.com.elos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

import br.com.elos.logging.LogLevel;

public abstract class App {
	
	/**
	 * ELOS
	 */
	private static final String elosPropertiesName = "elos.properties";
	public static String elos_package = "br.com.company.AppConfig";
	
    /**
     * SERVER: configurações do servidor, como base url, modo debug permite que 
     *         o elos gere logs de depuração e imprima em console seu processamento
     */
    public static String server_url = "http://localhost:8080/";
    public static boolean server_debug = true;
    public static LogLevel server_loglevel = LogLevel.DEBUG;
    
    /**
     * RESOURCE: indica base de recursos utilizados no gerenciamento de rota
     */
    public static String resource = null;

    /**
     * ORM: configurações do banco de dados e orm
     */
    public static String db_connection = "mysql";
    public static String db_hostname = "localhost";
    public static int db_port = 3306;
    public static String db_database = "db_name";
    public static String db_username = "root";
    public static String db_password = "";
    
    /**
     * FILESYSTEM: indica local de armazenamento de arquivos e tamanhos permitidos
     */
    public static String storage_location = "/";
    public static long storage_maxsize = 0; //bytes
    public static long storage_maxrequestsize = 0; //bytes
    public static boolean storage_generateuuid = true; 
    public static String[] storage_extensions = {"bmp", "png", "jpg", "jpeg"};
    
    /**
     * AEON
     */
    public static String timezone = "America/Sao_Paulo";
    
    /**
     * Criar arquivo de configuracao do elos
     */
    public void writeProperties() {
    	Properties properties = new Properties();
		OutputStream fileStream = null; 
				
    	try {
    		fileStream = new FileOutputStream(elosPropertiesName);

    		//package
    		properties.setProperty("package", elos_package);
    		
    		//server
    		properties.setProperty("server_url", server_url);
    		properties.setProperty("server_debug", String.valueOf(server_debug));
    		properties.setProperty("server_loglevel", server_loglevel.name());
    		
    		//resource
    		properties.setProperty("resource", resource);
    		
    		//orm
    		properties.setProperty("db_connection", db_connection);
    		properties.setProperty("db_hostname", db_hostname);
    		properties.setProperty("db_port", String.valueOf(db_port));
    		properties.setProperty("db_database", db_database);
    		properties.setProperty("db_username", db_username);
    		properties.setProperty("db_password", db_password);
    		
    		//filesystem
    		properties.setProperty("storage_location", storage_location);
    		properties.setProperty("storage_maxsize", String.valueOf(storage_maxsize));
    		properties.setProperty("storage_maxrequestsize", String.valueOf(storage_maxrequestsize));
    		properties.setProperty("storage_generateuuid", String.valueOf(storage_generateuuid));
    		properties.setProperty("storage_extensions", String.join(",", storage_extensions));
    		
    		//aeon
    		properties.setProperty("timezone", timezone);
    		
    		//salva properties na raiz do projeto
    		properties.store(fileStream, null);
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (fileStream != null) {
    			try {
    				fileStream.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
    }
    
    /**
     * Carrega arquivo de configuracao do elos
     */
    public static void readProperties() {
    	Properties properties = new Properties();
    	InputStream fileStream = null;

    	try {
    		fileStream = new FileInputStream(elosPropertiesName);
    		properties.load(fileStream);
    		
    		//package
    		elos_package = properties.getProperty("package", elos_package);
    		
    		//server
    		server_url = properties.getProperty("server_url", server_url);
    		server_debug = Boolean.parseBoolean(properties.getProperty("server_debug", String.valueOf(server_debug)));
    		server_loglevel = LogLevel.valueOf(properties.getProperty("server_loglevel", server_loglevel.name()));
    		
    		//resource
    		resource = properties.getProperty("resource", resource);
    		
    		//orm
    		db_connection = properties.getProperty("db_connection", db_connection);
    		db_hostname = properties.getProperty("db_hostname", db_hostname);
    		db_port = Integer.parseInt(properties.getProperty("db_port", String.valueOf(db_port)));
    		db_database = properties.getProperty("db_database", db_database);
    		db_username = properties.getProperty("db_username", db_username);
    		db_password = properties.getProperty("db_password", db_password);
    		
    		//filesystem
    		storage_location = properties.getProperty("storage_location", storage_location);
    		storage_maxsize = Long.parseLong(properties.getProperty("storage_maxsize", String.valueOf(storage_maxsize)));
    		storage_maxrequestsize = Long.parseLong(properties.getProperty("storage_maxrequestsize", String.valueOf(storage_maxrequestsize)));
    		storage_generateuuid = Boolean.parseBoolean(properties.getProperty("storage_generateuuid", String.valueOf(storage_generateuuid)));
    		storage_extensions = properties.getProperty("storage_extensions", String.join(",", storage_extensions)).split(",");
    		
    		//aeon
    		timezone = properties.getProperty("timezone", timezone);
    	} catch (IOException ex) {
    		ex.printStackTrace();
    	} finally {
    		if (fileStream != null) {
    			try {
    				fileStream.close();
    			} catch (IOException ex) {
    				ex.printStackTrace();
    			}
    		}
    	}
    }
    
    public static App getInstance(String pack) {
    	try {
    		elos_package = pack;
        	Class<?> classe = Class.forName(pack);
            return (App) classe.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
            ex.printStackTrace();
            return null;
        }
    }
    
    public static App getInstance() {
    	readProperties();
    	return getInstance(elos_package);
    }
}