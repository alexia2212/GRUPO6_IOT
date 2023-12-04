package com.example.grupo_iot.delactividad;

import java.util.Date;
import java.sql.Time;
import java.sql.Timestamp;

public class ChatMessage {

    public ChatMessage() {
    }
    private String sender;
    private String message;
    private Date timestamp;


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}

