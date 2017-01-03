/**
 * Classe utilizada para conversão de valores
 */
package br.com.elos.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Parse {
    
    public Boolean toBoolean(String booleanStr) {
        try {
            return Boolean.parseBoolean(booleanStr);
        } catch(Exception ex) {
            return null;
        }
    }
    
    public Integer toInt(String intStr) {
        try {
            return Integer.parseInt((String) intStr);
        } catch(Exception ex) {
            return null;
        }
    }
    
    public Float toFloat(String floatStr) {
        try {
            return Float.parseFloat((String) floatStr);
        } catch(Exception ex) {
            return null;
        }   
    }
    
    public Double toDouble(String doubleStr) {
        try {
            return Double.parseDouble((String) doubleStr);
        } catch(Exception ex) {
            return null;
        }   
    }
    
    public Date toDate(String dateStr, String formatDate) {
        SimpleDateFormat format = new SimpleDateFormat();
        Date date = null;
        
        try {
            format.applyPattern(formatDate);
            date = format.parse(dateStr);
        } catch(ParseException ex) {
            return null;
        }
        
        return date;
    }
    
    public String toString(Date date, String formatDate) {
        String dateStr = null;
        SimpleDateFormat format = new SimpleDateFormat(formatDate);
        format.setLenient(false);

        try {
            dateStr = format.format(date);
        } catch (Exception ex) {
            return null;
        }

        return dateStr;
    }
    
    public Calendar toCalendar(String dateStr, String formatDate) {
        SimpleDateFormat format = new SimpleDateFormat();
        Calendar calendar = Calendar.getInstance();
        
        try {
            format.applyPattern(formatDate);
            calendar.setTime(format.parse(dateStr));
        } catch(ParseException ex) {
            return null;
        }
        
        return calendar;
    }
    
    public String toString(Calendar calendar, String formatDate) {
        String dateStr = null;
        SimpleDateFormat format = new SimpleDateFormat(formatDate);
        format.setLenient(false);

        try {
            dateStr = format.format(calendar.getTime());
        } catch (Exception ex) {
            return null;
        }

        return dateStr;
    }
   
}