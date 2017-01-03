package br.com.elos.logging;

import br.com.elos.helpers.Parse;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;

public class Log {
    
    @Deprecated
    private StringBuffer log;
    
    /**
     * Gera mensagem de nível de depuração
     * @param message mensagem a ser armazenada no arquivo de log.
     */
    public static void debug(String message) {
        Log.writeLog(message, LogLevel.DEBUG);
    }
    
    /**
     * Gera mensagem de nível informativo
     * @param message mensagem a ser armazenada no arquivo de log.
     */
    public static void info(String message) {
        Log.writeLog(message, LogLevel.INFO);
    }
    
    /**
     * Gera mensagem de alerta em condições de aviso
     * @param message mensagem a ser armazenada no arquivo de log.
     */
    public static void warning(String message) {
        Log.writeLog(message, LogLevel.WARNING);
    }
    
    /**
     * Gera mensagem de alerta em condições de erro
     * @param message mensagem a ser armazenada no arquivo de log.
     */
    public static void error(String message) {
        Log.writeLog(message, LogLevel.ERROR);
    }                      

    /**
     * Gera mensagem de alerta em condições críticas
     * @param message mensagem a ser armazenada no arquivo de log.
     */
    public static void critical(String message) {
        Log.writeLog(message, LogLevel.CRITICAL);
    }
    
    /**
     * Gera mensagem emergencial sobre indisponibilidade do sistema
     * @param message mensagem a ser armazenada no arquivo de log.
     */
    public static void emergency(String message) {
        Log.writeLog(message, LogLevel.EMERGENCY);
    }
    
    /**
     * Gera arquivo de log e persiste mensagem passada pela origem.
     * @param message mensagem a ser armazenada no arquivo de log. 
     * @param loglevel severidade do log a ser gerado.
     */
    private static void writeLog(String message, LogLevel loglevel) {
        BufferedWriter bufferWriter = null;
        
        try {
            OutputStream outputStr = new FileOutputStream("elos.log", true);
            OutputStreamWriter outputStrWriter = new OutputStreamWriter(outputStr);
            bufferWriter = new BufferedWriter(outputStrWriter);
            bufferWriter.write(new Parse().toString(Calendar.getInstance(), "dd/MM/yyyy HH:mm:ss") + ";" + loglevel.toString() + ";" + message + ";");
            bufferWriter.newLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                bufferWriter.flush();
                bufferWriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
    
}