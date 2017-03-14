/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.elos.route;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author Tiago
 */
public enum MessageType {
    
    @SerializedName("msg_success")
    SUCCESS,
    @SerializedName("msg_info")
    INFO,
    @SerializedName("msg_warning")
    WARNING,
    @SerializedName("msg_error")
    ERROR;
    
}
