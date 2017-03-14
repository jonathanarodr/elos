package br.com.elos.orm;

import br.com.elos.helpers.Util;
import java.lang.reflect.Field;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.Transient;

public class MySQL {

    private boolean status;
    private boolean access;
    private Object entity;
    private Object entityMap;
    private String column;
    private Object value;
    private List<Object> values;
    private StringBuilder query;
    private StringBuilder queryval;
    private int rows;
    private PreparedStatement preparedst;
    private CallableStatement callablest;
    private ResultSet resultset;
    protected String sql;
    protected Connection connection;
    private final Util util = new Util();
    
    //comandos TCL
    public void close() {
        try {
            if ((this.preparedst != null) && (!this.preparedst.isClosed())) {
                this.preparedst.close();
            }
            if ((this.callablest != null) && (!this.callablest.isClosed())) {
                this.callablest.close();
            }
            if ((this.resultset != null) && (!this.resultset.isClosed())) {
                this.resultset.close();
            }
            if ((this.connection != null) && (!this.connection.isClosed())) {
                this.connection.close();
            }
        } catch (SQLException ex) {
            Logger.getLogger(MySQL.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //util
    private void clear() {
        this.status = false;
        this.access = false;
        this.entity = null;
        this.column = null;
        this.value = null;
        this.values = new ArrayList<>();
        this.query = new StringBuilder();
        this.queryval = new StringBuilder();
        this.sql = "";
        this.rows = 0;
    }

    private boolean isEntityMap(Field entity) {
        return (entity.isAnnotationPresent(OneToOne.class))
            || (entity.isAnnotationPresent(OneToMany.class))
            || (entity.isAnnotationPresent(ManyToOne.class))
            || (entity.isAnnotationPresent(ManyToMany.class));
    }

    private Field getFieldId(Object entity) {
        Field fieldId = null;

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(Id.class)) {
                fieldId = field;
                break;
            }
        }

        return fieldId;
    }

    private String getColumnName(Field field) {
        return (field.isAnnotationPresent(Column.class)) ? field.getAnnotation(Column.class).name() : field.getName();
    }
    
    
    private void extractField(Object entity, Field field) throws IllegalArgumentException, IllegalAccessException {
        this.extractField(entity, field, null);
    }
    
    private void extractField(Object entity, Field field, String columnName) throws IllegalArgumentException, IllegalAccessException {
        this.entityMap = null;
        this.column = columnName;
        this.value = null;
       
        //utiliza nome serializado ou nome do próprio atributo da classe caso o nome auxiliar não tenha sido informado
        if (this.column == null) {    
            this.column = this.getColumnName(field);
        }

        //captura valor do field
        this.access = field.isAccessible();
        field.setAccessible(true);
        this.value = field.get(entity);
        field.setAccessible(this.access);

        //se for uma entidades, captura instância da classe
        if (this.isEntityMap(field)) {
            this.entityMap = this.value;
        }

        //se for um tipo enum, captura valor do enumtype configurado
        if ((field.isAnnotationPresent(Enumerated.class)) && (this.value != null)) {
            switch (field.getAnnotation(Enumerated.class).value()) {
                case STRING:
                    this.value = this.value.toString();
                    break;
                case ORDINAL: {
                    int i = 0;
                    for (Object enums : field.getType().getEnumConstants()) {
                        if (enums.equals(this.value)) {
                            this.value = i;
                            break;
                        }

                        i++;
                    }
                    break;
                }
            }
        }

        //se for field temporal, define tipificação
        if ((field.isAnnotationPresent(Temporal.class)) && (this.value != null)) {
            Calendar calendar = Calendar.getInstance();
            calendar = (Calendar) this.value;

            switch (field.getAnnotation(Temporal.class).value()) {
                case DATE:
                    this.value = new java.sql.Date(calendar.getTimeInMillis());
                    break;
                case TIME:
                    this.value = new java.sql.Time(calendar.getTimeInMillis());
                    break;
                case TIMESTAMP:
                    this.value = new java.sql.Timestamp(calendar.getTimeInMillis());
                    break;
            }
        }
    }

    private void populateField(Object entity, Field field) throws IllegalArgumentException, IllegalAccessException, SQLException {
        this.populateField(entity, field, null);
    }
    
    private void populateField(Object entity, Field field, String columnName) throws IllegalArgumentException, IllegalAccessException, SQLException {
        if (field.isAnnotationPresent(Transient.class)) {
            return;
        }
        
        this.column = columnName;
        this.value = null;
        
        //utiliza nome serializado ou nome do próprio atributo da classe caso o nome auxiliar não tenha sido informado
        if (this.column == null) {    
            this.column = this.getColumnName(field);
        }
        
        this.value = this.resultset.getObject(this.column);
        this.access = field.isAccessible();
        field.setAccessible(true);

        if (this.value != null) {
            if (field.isAnnotationPresent(Enumerated.class)) {
                switch (field.getAnnotation(Enumerated.class).value()) {
                    case STRING:
                        for (Object enums : field.getType().getEnumConstants()) {
                            if (this.value.equals(enums.toString())) {
                                field.set(entity, enums);
                                break;
                            }
                        }
                        break;
                    case ORDINAL:
                        Object[] enums = field.getType().getEnumConstants();
                        field.set(entity, enums[Integer.parseInt(this.value.toString())]);
                        break;
                }
            } else {
                switch (this.value.getClass().getTypeName()) {
                    case "java.lang.String":
                        if (this.value != null) {
                            field.set(entity, this.resultset.getString(this.column));
                        }
                        break;
                    case "java.lang.Char":
                        if (this.value != null) {
                            field.set(entity, this.resultset.getString(this.column));
                        }
                        break;
                    case "java.lang.Integer":
                        if (this.value != null) {
                            field.set(entity, this.resultset.getInt(this.column));
                        }
                        break;
                    case "java.lang.Boolean":
                        if (this.value != null) {
                            field.set(entity, this.resultset.getBoolean(this.column));
                        }
                        break;
                    case "java.lang.Float":
                        if (this.value != null) {
                            field.set(entity, this.resultset.getFloat(this.column));
                        }
                        break;
                    case "java.lang.Double":
                        if (this.value != null) {
                            field.set(entity, this.resultset.getDouble(this.column));
                        }
                        break;
                    case "java.sql.Date":
                        if (this.value != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(this.resultset.getDate(this.column).getTime());
                            field.set(entity, calendar);
                        }
                        break;
                    case "java.sql.Time":
                        if (this.value != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(this.resultset.getTime(this.column).getTime());
                            field.set(entity, calendar);
                        }
                        break;
                    case "java.sql.Timestamp":
                        if (this.value != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTimeInMillis(this.resultset.getTimestamp(this.column).getTime());
                            field.set(entity, calendar);
                        }
                        break;
                }
            }
        }

        field.setAccessible(this.access);
    }

    //comandos DML
    public boolean insert(Object object) {
        this.clear();
        this.entity = object;

        if (this.entity == null) {
            return false;
        }

        if (!this.entity.getClass().isAnnotationPresent(Entity.class)) {
            return false;
        }

        try {
            String table = this.entity.getClass().getSimpleName().toLowerCase();
            Field[] fields = this.entity.getClass().getDeclaredFields();
            Field fieldId = this.getFieldId(this.entity);

            for (Field field : fields) {
                if ((!field.isAnnotationPresent(Id.class)) && (!field.isAnnotationPresent(Transient.class))) {
                    this.extractField(this.entity, field);

                    if (this.isEntityMap(field)) {
                        if ((this.entityMap != null) && (this.entityMap.getClass().isAnnotationPresent(Entity.class))) {
                            this.extractField(this.entityMap, this.getFieldId(this.entityMap));
                            this.query.append(this.column + ",");
                            this.queryval.append("?,");
                            this.values.add(this.value);
                        }
                    } else {
                        this.query.append(this.column + ",");
                        this.queryval.append("?,");
                        this.values.add(this.value);
                    }
                }
            }

            //monta query final
            this.sql = "insert into " + table
                     + " (" + this.query.substring(0, this.query.length() - 1)
                     + ") values (" + this.queryval.substring(0, this.queryval.length() - 1) + ");";

            //configura statement
            this.preparedst = this.connection.prepareStatement(this.sql, PreparedStatement.RETURN_GENERATED_KEYS);
            this.preparedst = new StatementFactory().getStatement(this.preparedst, this.values);

            //executa comando e captura resultset
            this.preparedst.executeUpdate();
            this.resultset = this.preparedst.getGeneratedKeys();

            //captura chave de inserção            
            if (this.resultset.next()) {
                this.access = fieldId.isAccessible();
                fieldId.setAccessible(true);
                fieldId.set(this.entity, this.resultset.getInt(1));
                fieldId.setAccessible(this.access);
                this.rows = 1;
                this.status = true;
            } else {
                this.status = false;
            }

            System.out.println("Script: " + this.sql + "\nValues: " + Arrays.toString(this.values.toArray()) + "\nID " + this.resultset.getInt(1) + " gerado para tabela " + table);
        } catch (IllegalAccessException | SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }

        return this.status;
    }

    public boolean update(Object object) {
        this.clear();
        this.entity = object;

        if (this.entity == null) {
            return false;
        }

        if (!this.entity.getClass().isAnnotationPresent(Entity.class)) {
            return false;
        }

        try {
            String table = this.entity.getClass().getSimpleName().toLowerCase();
            Field[] fields = this.entity.getClass().getDeclaredFields();
            Field fieldId = this.getFieldId(this.entity);

            for (Field field : fields) {
                if ((!field.isAnnotationPresent(Id.class)) && (!field.isAnnotationPresent(Transient.class))) {
                    this.extractField(this.entity, field);

                    if (this.isEntityMap(field)) {
                        if ((this.entityMap != null) && (this.entityMap.getClass().isAnnotationPresent(Entity.class))) {
                            this.extractField(this.entityMap, this.getFieldId(this.entityMap));
                            this.query.append(this.column + " = ?,");
                            this.values.add(this.value);
                        }
                    } else {
                        this.query.append(this.column + " = ?,");
                        this.values.add(this.value);
                    }
                }
            }

            //monta query final
            this.extractField(this.entity, fieldId);
            this.values.add(this.value);
            this.sql = "update " + table
                     + " set " + this.query.substring(0, this.query.length() - 1)
                     + " where " + this.column + " = ?;";
 
            //configura statement
            this.preparedst = this.connection.prepareStatement(this.sql);
            this.preparedst = new StatementFactory().getStatement(this.preparedst, this.values);

            //executa comando e captura resultset
            this.preparedst.executeUpdate();
            this.rows = this.preparedst.getUpdateCount();
            this.status = true;

            System.out.println("Script: " + this.sql + "\nValues: " + Arrays.toString(this.values.toArray()) + "\nAffected: " + this.rows + " row(s)");
        } catch (IllegalAccessException | SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }

        return this.status;
    }

    public boolean update(QueryBuilder querybuilder) {
        this.clear();
        
        try {
            querybuilder.build();
            this.sql = querybuilder.getSql();
            this.values = querybuilder.getValues();
            
            //configura statement
            this.preparedst = this.connection.prepareStatement(this.sql);
            this.preparedst = new StatementFactory().getStatement(this.preparedst, this.values);

            //executa comando
            this.preparedst.executeUpdate();
            this.rows = this.preparedst.getUpdateCount();
            this.status = true;

            System.out.println("Script: " + this.sql + "\nValues: " + Arrays.toString(this.values.toArray()) + "\nAffected: " + this.rows + " row(s)");
        } catch (SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }

        return this.status;
    }
    
    public boolean delete(Object object) {
        this.clear();
        this.entity = object;

        if (this.entity == null) {
            return false;
        }

        if (!this.entity.getClass().isAnnotationPresent(Entity.class)) {
            return false;
        }

        try {
            String table = this.entity.getClass().getSimpleName().toLowerCase();
            Field fieldId = this.getFieldId(this.entity);

            //monta query final
            this.extractField(this.entity, fieldId);
            this.values.add(this.value);
            this.sql = "delete from " + table
                     + " where " + this.column + " = ?;";

            //configura statement
            this.preparedst = this.connection.prepareStatement(this.sql);
            this.preparedst = new StatementFactory().getStatement(this.preparedst, this.values);

            //executa comando
            this.preparedst.executeUpdate();
            this.rows = this.preparedst.getUpdateCount();
            this.status = true;

            System.out.println("Script: " + this.sql + "\nValues: " + Arrays.toString(this.values.toArray()) + "\nAffected: " + this.rows + " row(s)");
        } catch (IllegalAccessException | SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }

        return this.status;
    }

    //comandos DML (query)
    public Object find(Class<?> entityClass, int id) {
        this.clear();

        try {
            this.entity = entityClass.newInstance();

            if (!this.entity.getClass().isAnnotationPresent(Entity.class)) {
                return false;
            }

            String table = this.entity.getClass().getSimpleName().toLowerCase();
            Field[] fields = this.entity.getClass().getDeclaredFields();
            Field fieldId = this.getFieldId(this.entity);

            for (Field field : fields) {
                if (!field.isAnnotationPresent(Transient.class)) {
                    this.extractField(this.entity, field);

                    if (this.isEntityMap(field)) {
                        this.entityMap = field.getType().newInstance();

                        if ((this.entityMap.getClass().isAnnotationPresent(Entity.class))) {
                            this.extractField(this.entityMap, this.getFieldId(this.entityMap), (field.isAnnotationPresent(Column.class)) ? this.getColumnName(field) : null);
                            this.query.append(this.column + ",");
                        }
                    } else {
                        this.query.append(this.column + ",");
                    }
                }
            }

            //monta query final
            this.extractField(this.entity, fieldId);
            this.values.add(id);
            this.sql = "select " + this.query.substring(0, this.query.length() - 1)
                     + " from " + table
                     + " where " + this.column + " = ?;";

            //configura statement
            this.preparedst = this.connection.prepareStatement(this.sql);
            this.preparedst = new StatementFactory().getStatement(this.preparedst, this.values);

            //executa comando e captura resultset
            this.resultset = this.preparedst.executeQuery();

            //seta entidade
            if (this.resultset.next()) {
                for (Field field : fields) {
                    if (this.isEntityMap(field)) {
                        this.entityMap = field.getType().newInstance();
                        this.populateField(this.entityMap, this.getFieldId(this.entityMap), (field.isAnnotationPresent(Column.class)) ? this.getColumnName(field) : null);

                        this.access = field.isAccessible();
                        field.setAccessible(true);
                        field.set(this.entity, this.entityMap);
                        field.setAccessible(this.access);
                    } else {
                        this.populateField(this.entity, field);
                    }
                }
                this.rows = 1;
                this.status = true;
            } else {
                this.status = false;
            }

            System.out.println("Script: " + this.sql + "\nValues: " + Arrays.toString(this.values.toArray()) + "\nAffected: " + this.rows + " row(s)");
        } catch (IllegalAccessException | InstantiationException | SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }

        return this.entity;
    }
    
    public ResultSet select(QueryBuilder querybuilder) {
        this.clear();

        try {
            querybuilder.build();
            this.sql = querybuilder.getSql();
            this.values = querybuilder.getValues();

            //configura statement
            this.preparedst = this.connection.prepareStatement(this.sql);
            this.preparedst = new StatementFactory().getStatement(this.preparedst, this.values);

            //executa comando e captura resultset
            this.resultset = this.preparedst.executeQuery();
            this.resultset.last();
            this.rows = this.resultset.getRow();
            this.resultset.beforeFirst();
            this.status = true;
            
            System.out.println("Script: " + this.sql + "\nValues: " + Arrays.toString(this.values.toArray()) + "\nAffected: " + this.rows + " row(s)");
        } catch (SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }
        
        return this.resultset;
    }
    
    public CallableStatement call(String objectname) {
        this.status = false;
        this.sql = "{" + objectname + "}";
        
        try {
            //executa comando
            this.callablest = this.connection.prepareCall(this.sql);
            this.callablest.executeUpdate();
            this.status = true;

            System.out.println("Script: " + this.sql);
        } catch (SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }

        return this.callablest;
    }
    
    public CallableStatement call(QueryBuilder querybuilder) {
        this.clear();
        
        try {
            querybuilder.build();
            this.sql = querybuilder.getSql();
            
            //configura statement e executa comando
            this.callablest = this.connection.prepareCall(this.sql);
            this.callablest = new StatementFactory().getStatement(this.callablest, querybuilder.getValues(), querybuilder.getValuesOut());
            this.callablest.executeUpdate();
            this.status = true;

            System.out.println("Script: " + this.sql);
        } catch (SQLException ex) {
            this.status = false;
            this.close();
            ex.printStackTrace();
        }

        return this.callablest;
    }
    
}