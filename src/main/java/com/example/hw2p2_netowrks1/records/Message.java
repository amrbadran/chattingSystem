package com.example.hw2p2_netowrks1.records;

public class Message {
    String data;
    String port;

    String time;
    String sender;
    String sender_username;
    String color;

    public Message(String data, String sender, String port, String time) {
        this.data = data;
        this.port = port;
        this.time = time;
        this.sender = sender;
    }

    public Message(String data, String sender, String port, String time, String sender_username, String Color) {
        this.data = data;
        this.port = port;
        this.time = time;
        this.sender = sender;
        this.sender_username = sender_username;
        this.color = Color;
    }

    public String getSender_username() {
        return sender_username;
    }

    public void setSender_username(String sender_username) {
        this.sender_username = sender_username;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return sender + "," + data + "," + port + "," + time + "," + sender_username + "," + color;
    }

    public static Message parseMessage(String m) {
        String[] arr = m.split(",");

        return new Message(arr[1], arr[0], arr[2], arr[3], arr[4], arr[5]);

    }
}
