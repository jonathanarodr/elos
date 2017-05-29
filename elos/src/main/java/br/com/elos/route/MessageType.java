package br.com.elos.route;

import com.google.gson.annotations.SerializedName;

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
