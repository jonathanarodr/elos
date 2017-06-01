package br.com.elos.route;

import com.google.gson.annotations.Expose;

public class View {
    
    protected int status;
    protected String location;
    @Expose
    protected String entityname;
    protected Object entity;
    protected String sessionname;
    protected Object session;
    protected ResponseType responseType;
    protected ResponseMessage responseMessage;
    
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
        this.status = 201;
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
    
    public View json(Object value) {
        this.status = 200;
        this.location = null;
        this.entity = value;
        this.responseType = null;
        this.responseMessage = null;
        return this;
    }
    
    public View json(Object value, ResponseMessage responseMessage) {
        this.status = 200;
        this.location = null;
        this.entity = value;
        this.responseType = null;
        this.responseMessage = responseMessage;
        return this;
    }
    
    public View with(String session, Object data) {
        this.sessionname = session;
        this.session = data;
        return this;   
    }
    
}
