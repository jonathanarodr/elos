package br.com.elos.serialization;

import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.servlet.http.HttpServletRequest;

public class Json {
    
    private final GsonBuilder builder = new GsonBuilder();
    
    public Gson build() {
        this.builder.setDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        
        //registra adapters
        this.builder.registerTypeAdapter(Calendar.class, new CalendarAdapter());
        this.builder.registerTypeAdapter(GregorianCalendar.class, new CalendarAdapter());
        
        //registra classes não serializáveis
        this.builder.setExclusionStrategies(new ExcludeStrategy());
        
        return this.builder.create();
    }
    
    public String getJson(HttpServletRequest request) {
        try {
            return new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8")).readLine();
        } catch (IOException ex) {
            return null;
        }
    }
    
}
