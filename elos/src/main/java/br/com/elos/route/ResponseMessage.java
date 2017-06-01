package br.com.elos.route;

public class ResponseMessage {
    
    protected boolean status;
    protected String message;
    protected MessageType type;
    
    public ResponseMessage(boolean status, String message, MessageType type) {
        this.status = status;
        this.message = message;
        this.type = type;
   }
}
