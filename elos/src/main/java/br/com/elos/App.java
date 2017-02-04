package br.com.elos;

public abstract class App {
    
    /**
     * URL
     */
    public static String url;
    
    /**
     * DEBUG
     * configuração de contexto em modo debug
     */
    public static boolean debug;
    
    /**
     * ROUTE
     * configuração de pacotes para gerenciamento de rota
     */
    public static String resource;
    
    /**
     * ORM
     * dados de conexão do orm
     */
    public static String user_db;
    public static String password_db;
    public static String database;
    public static String hostname;
    public static String driver;
    public static int port;
    
    public static App getInstance() {
		try {
			Class<?> classe = Class.forName(null);
			return (App) classe.newInstance();
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
			ex.printStackTrace();
			return null;
		}
    }
    
    public static App getInstance(String resource) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> classe = Class.forName(resource);
        return (App) classe.newInstance();
    }
    
}
