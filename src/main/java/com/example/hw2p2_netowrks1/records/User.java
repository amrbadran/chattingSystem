package com.example.hw2p2_netowrks1.records;

import java.util.Date;

public class User {
    String IPAdress;
    int port;
    String username;
    String password;

    String LastLogin;
    String Status;

    String Color;

    public User(String username, String password, String IPAdress, int port, String lastLogin, String status) {
        this.IPAdress = IPAdress;
        this.port = port;
        this.username = username;
        this.password = password;
        LastLogin = lastLogin;
        Status = status;
    }

    public User(String username, String password, String IPAdress, int port, String lastLogin, String status,String Color) {
        this.IPAdress = IPAdress;
        this.port = port;
        this.username = username;
        this.password = password;
        LastLogin = lastLogin;
        Status = status;
        this.Color = Color;
    }

    public void setIPAdress(String IPAdress) {
        this.IPAdress = IPAdress;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLastLogin(String lastLogin) {
        LastLogin = lastLogin;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getIPAdress() {
        return IPAdress;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getLastLogin() {
        return LastLogin;
    }

    public String getStatus() {
        return Status;
    }

    public String getColor() {
        return Color;
    }

    public void setColor(String color) {
        Color = color;
    }

    @Override
    public String toString() {
        return username + "," + password + "," + IPAdress + "," + port + "," + LastLogin + "," + Status + "," + Color;
    }

    public static User parseUser(String user) {
        String[] data = user.split(",");
        return new User(
                data[0], data[1], data[2], Integer.parseInt(data[3]), data[4], data[5],data[6]
        );
    }
}
