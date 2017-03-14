/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.elos.route;

/**
 *
 * @author Tiago
 */
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
