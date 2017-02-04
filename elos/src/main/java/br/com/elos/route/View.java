package br.com.elos.route;

public class View {
    
    protected int status;
    protected String location;
    protected String entityname;
    protected Object entity;
    protected ResponseType responseType;
    
    public View ok(String location) {
        this.status = 200;
        this.location = location;
        this.entity = null;
        this.responseType = ResponseType.DISPATCHER;
        return this;
    }
    
    @Deprecated
    public View ok(String location, Object[] entity) {
        this.status = 200;
        this.location = location;
        this.entity = entity;
        this.responseType = ResponseType.DISPATCHER;
        return this;
    }
    
    public View created(String location) {
        this.status = 201;
        this.location = location;
        this.entity = null;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    @Deprecated
    public View created(String location, Object[] entity) {
        this.status = 201;
        this.location = location;
        this.entity = entity;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    public View redirect(String location) {
        this.status = 302;
        this.location = location;
        this.entity = null;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    @Deprecated
    public View redirect(String location, Object[] entity) {
        this.status = 302;
        this.location = location;
        this.entity = entity;
        this.responseType = ResponseType.REDIRECT;
        return this;
    }
    
    public View error(int status) {
        this.status = status;
        this.entity = null;
        this.responseType = ResponseType.ERROR;
        return this;
    }
    
    public View error(int status, String message) {
        this.status = status;
        this.entity = message;
        this.responseType = ResponseType.ERROR;
        return this;
    }
    
    public View entity(String name, Object value) {
        this.entityname = name;
        this.entity = value;
        return this;
    }
    
}
