/**
 * Classe utilizada para métodos utilitários
 */
package br.com.elos.helpers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.Normalizer;
import java.math.BigInteger;

public class Util {

    public boolean isPrimitiveType(String type) {
        type = type.toLowerCase();
        
        return (type.toLowerCase().equals("string"))
            || (type.toLowerCase().equals("char"))
            || (type.toLowerCase().equals("boolean"))
            || (type.toLowerCase().equals("byte"))
            || (type.toLowerCase().equals("short"))
            || (type.toLowerCase().equals("int"))
            || (type.toLowerCase().equals("long"))
            || (type.toLowerCase().equals("float"))
            || (type.toLowerCase().equals("double"));
    }
    
    public boolean isNull(Object value) {
        return (value == null);
    }
    
    public String trim(String value) {
        if (value == null) {
            return null;
        }

        value = value.replaceAll(" ", "");

        return value;
    }
    
    public String ltrim(String value) {
        if (value == null) {
            return null;
        }
        
        int len = value.length();
        int index = 0;
        
        while ((index < len) && (value.substring(index, index+1).equals(" "))) {
            index++;
        }
        
        return value.substring(index, len);
    }
    
    public String rtrim(String value) {
        if (value == null) {
            return null;
        }
        
        int len = value.length();
        int index = 0;
        
        while ((index < len) && (value.substring(len-1, len).equals(" "))) {
            len--;
        }
        
        return value.substring(index, len);
    }

    public String truncate(String value, Integer length) {
        if (value.length() > length) {
            value = value.substring(0, (length - 3)) + "...";
        }

        return value;
    }

    public String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
    }
    
    public String slug(String value, String delimiter) {
        if ((value == null) || (delimiter == null)) {
            return null;
        }
        
        return value = normalize(value).replace(" ", delimiter).toLowerCase();
    }
    
    public String slug(String value) {
        return slug(value, "-");
    }
    
    public String encrypt(String value) {
        try {
            StringBuilder hash = new StringBuilder();
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte digest[] = sha256.digest(value.getBytes("UTF-8"));
        
            for (byte b : digest) {
                hash.append(String.format("%02X", 0xFF & b));
            }
        
            return hash.toString();      
        } catch(NoSuchAlgorithmException | UnsupportedEncodingException exEncrypt) {
            return null;
        }
    }
    
    public String random(int lenght) {
        return new BigInteger((lenght*5), new SecureRandom()).toString(32).substring(0, lenght);
    }

}