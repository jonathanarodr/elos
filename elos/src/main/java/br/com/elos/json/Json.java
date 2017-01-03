/**
 * Classe Json responsável por efetuar o tratamento de conversões de objetos
 * para json e vice versa.
 * 
 * Exemplo: String   json   = toJson(Class<?>);
 *          String   json   = toJson(List<?>);
 *          Class<?> object = (Class<?>) fromJson(json, Class<?>.class);
 *          List<?>  object = (List<?>) fromJson(json, Class<?>.class);
 */

package br.com.elos.json;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class Json {
    
    private boolean status;
    private boolean isArray;
    private boolean isArrayAux;
    private String json;
    private String serializedname;
    private Object value;
    private static final String[] dateFormat = {"yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy HH:mm:ss", "yyyy-MM-dd", "dd/MM/yyyy", "HH:mm:ss", "HH:mm"};
    
    private void clear() {
        this.status = false;
        this.isArray = false;
        this.isArrayAux = false;
        this.serializedname = null;
        this.value = null;
    }
    
    private boolean isPrimitiveType(String type) {
        return (type.toLowerCase().equals("string"))
            || (type.toLowerCase().equals("char"))
            || (type.toLowerCase().equals("boolean"))
            || (type.toLowerCase().equals("byte"))
            || (type.toLowerCase().equals("short"))
            || (type.toLowerCase().equals("int"))
            || (type.toLowerCase().equals("long"))
            || (type.toLowerCase().equals("float"))
            || (type.toLowerCase().equals("double"))
            || (type.toLowerCase().equals("date"));
    }
    
    private boolean isNull(Object value) {
        return (value == null)
            || (value.toString().equals("-1"));
    }
    
    private Object[] toArray(Object object) {
        Object[] objArray = null;
        
        if (object.getClass().isArray()) {
            objArray = (Object[]) object;
        }
        
        if (object instanceof ArrayList) {
            ArrayList arraylist = (ArrayList) object;
            objArray = arraylist.toArray();
        }
        
        return objArray;
    }
    
    private Integer toInt(String intStr) {
        try {
            return Integer.parseInt((String) intStr);
        } catch(Exception ex) {
            return null;
        }
    }
    
    private Float toFloat(String floatStr) {
        try {
            return Float.parseFloat((String) floatStr);
        } catch(Exception ex) {
            return null;
        }   
    }
    
    private Date toDate(String dateStr) {
        SimpleDateFormat objSimpleDateFormat = new SimpleDateFormat();
        Date date = null;
        
        for (String format : Json.dateFormat) {
            try {
                objSimpleDateFormat.applyPattern(format);
                date = objSimpleDateFormat.parse(dateStr);
                
                if (date != null) {
                    break;
                }
            } catch(ParseException ex) {}
        }
        
        return date;
    }
    
    private String write(String jsonOut, Object object) {        
        try {
            if (this.isArray) {
                jsonOut += ",";
            }
            
            jsonOut += "{";
        
            Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {
                this.status = field.isAccessible();

                field.setAccessible(true);

                this.value = field.get(object);
                
                if ((!this.isNull(this.value)) && (!field.isAnnotationPresent(IgnoreField.class))) {
                    //utiliza nome serializado ou nome do próprio atributo da classe
                    if (field.isAnnotationPresent(SerializedName.class)) {
                        this.serializedname = field.getAnnotation(SerializedName.class).value();
                    } else {
                        this.serializedname = field.getName();
                    }
                    
                    if (this.isPrimitiveType(field.getType().getSimpleName())) {
                        if ((field.getType().getSimpleName().equals("String")) || (field.getType().getSimpleName().equals("Date"))) {
                            this.value = "\"" + this.value + "\"";
                        }
                        //adiciona field simples
                        jsonOut += serializedname + ":" + this.value + ",";
                    } else {
                        Object objectAtr = field.get(object);
                        Object objectArr = this.toArray(objectAtr);
                        
                        jsonOut += this.serializedname + ":";
                        
                        if (objectArr == null) {
                            this.isArrayAux = this.isArray;
                            this.isArray = false;
                            
                            //se é um objeto, efetua sua leitura
                            jsonOut = this.write(jsonOut, objectAtr) + ",";
                            
                            this.isArray = this.isArrayAux;
                        } else {
                            jsonOut += "[";
                            
                            //se é um array de objetos, efetua leitura unitária
                            for (int i=0; i<Array.getLength(objectArr); i++) {
                                jsonOut      = this.write(jsonOut, Array.get(objectArr, i));
                                this.isArray = true;
                            }
                            
                            jsonOut     += "],";
                            this.isArray = false;
                        }
                    }
                }

                field.setAccessible(this.status);
            }
            
            //remove última vírgula e fecha json
            jsonOut = jsonOut.substring(0, jsonOut.length()-1) + "}";
        } catch (IllegalAccessException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return jsonOut;
    }
    
    private Object read(String jsonIn, Object object) {        
        try {
            JSONObject jsonobject = new JSONObject(jsonIn);
            
            Field[] fields = object.getClass().getDeclaredFields();

            for (Field field : fields) {
                //utiliza nome serializado ou nome do próprio atributo da classe
                if (field.isAnnotationPresent(SerializedName.class)) {
                    this.serializedname = field.getAnnotation(SerializedName.class).value();
                } else {
                    this.serializedname = field.getName();
                }
                
                if ((!jsonobject.isNull(this.serializedname)) && (!field.isAnnotationPresent(IgnoreField.class))) {
                    this.status = field.isAccessible();
                    
                    field.setAccessible(true);

                    if (this.isPrimitiveType(field.getType().getSimpleName())) {
                        switch (field.getType().getSimpleName().toLowerCase()) {
                            case "int":
                                field.set(object, this.toInt(jsonobject.get(this.serializedname).toString()));
                                break;
                            case "float":
                                field.set(object, this.toFloat(jsonobject.get(this.serializedname).toString()));
                                break;
                            case "date":
                                field.set(object, this.toDate((String) jsonobject.get(this.serializedname)));
                                break;
                            default:
                                field.set(object, jsonobject.get(this.serializedname));
                                break;
                        }
                    } else {
                        if (field.getGenericType() instanceof ParameterizedType) {
                            ParameterizedType objParamType  = (ParameterizedType) field.getGenericType();
                            Class<?>          objClassList  = (Class<?>) objParamType.getActualTypeArguments()[0];
                            List<Object>      objectAtrList = new ArrayList<>();
                            
                            JSONArray jsonarray = jsonobject.getJSONArray(this.serializedname);

                            for (int i=0; i<jsonarray.length(); i++) {
                                JSONObject jsonobjectarr = jsonarray.getJSONObject(i);
                                Object     objectAtr     = objClassList.newInstance();
                                
                                objectAtrList.add(this.read(jsonobjectarr.toString(), objectAtr));
                            }
                            
                            field.set(object, objectAtrList);
                        } else {
                            Class<?> objClass  = Class.forName(field.getType().getTypeName());
                            Object   objectAtr = objClass.newInstance();
                            
                            field.set(object, this.read(jsonobject.getJSONObject(this.serializedname).toString(), objectAtr));
                        }
                    }

                    field.setAccessible(this.status);
                }
            }
        } catch (IllegalArgumentException | JSONException | IllegalAccessException | ClassNotFoundException | InstantiationException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
            ex.printStackTrace();
        }
        
        return object;
    }
    
    public String toJson(Object object) {
        this.clear();
        
        if (object == null) {
            return null;
        }
        
        Object objectArr = this.toArray(object);
        
        if (objectArr == null) {
            this.json = "";
            this.json = this.write(this.json, object);
        } else {
            this.json = "[";
            
            for (int i=0; i<Array.getLength(objectArr); i++) {
                this.json    = this.write(this.json, Array.get(objectArr, i));
                this.isArray = true;
            }

            this.json   += "]";
            this.isArray = false;
        }
        
        return this.json;
    }
    
    public Object fromJson(String jsonIn, Class<?> objClass) {
        this.clear();

        if ((jsonIn == null) || (jsonIn.trim().equals(""))) {
            return null;
        }
            
        try {
            Object objJSONTokener = new JSONTokener(jsonIn).nextValue();
            
            if (objJSONTokener instanceof JSONObject) {
                Object objectAtr = objClass.newInstance();
                return this.read(jsonIn, objectAtr);
            } else {
                JSONArray    objJSONArray  = new JSONArray(jsonIn);
                List<Object> objectAtrList = new ArrayList<>();
                
                for (int i=0; i<objJSONArray.length(); i++) {
                    JSONObject objJSON   = objJSONArray.getJSONObject(i);
                    Object     objectAtr = objClass.newInstance();
            
                    objectAtrList.add(this.read(objJSON.toString(), objectAtr));
                }
                
                return objectAtrList;
            }
        } catch (JSONException | InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
    public String getResponse(boolean status, String message, String jsonOut) {
        if ((jsonOut == null) || (jsonOut.trim().equals(""))) {
            return "{response:{status:" + status + ",message:\"" + message + "\"}}";
        } else {
            return "{response:{status:" + status + ",message:\"" + message + "\"}," + jsonOut.substring(1) + "}";
        }
    }
    
    public String getResponse(boolean status, String message, Object object) {
        if (object == null) {
            return "{response:{status:" + status + ",message:\"" + message + "\"}}";
        } else {
            return "{response:{status:" + status + ",message:\"" + message + "\"}," + object.getClass().getSimpleName().toLowerCase() + ":" + this.toJson(object) + "}";
        }
    }
    
    public Object getValue(String jsonObj, String key) {
        this.value = null;
        
        try {
            JSONObject jsonobject = new JSONObject(jsonObj);
            
            if (!jsonobject.isNull(key)) {
                this.value = jsonobject.get(key);
            }
        } catch (JSONException ex) {
            Logger.getLogger(Json.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return this.value;
    }
    
}