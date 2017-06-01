package br.com.elos.route;

import com.google.gson.annotations.SerializedName;

public enum MessageType {

	@SerializedName("msg_success")
    SUCCESS("msg_success"),
    @SerializedName("msg_info")
    INFO("msg_info"),
    @SerializedName("msg_warning")
    WARNING("msg_warning"),
    @SerializedName("msg_error")
    ERROR("msg_error");
    
    private String value;
    
    MessageType(String value) {
        this.value = value;
    }
    
    public String value() {
        return this.value;
    }
	
}
