package com.example.hw2p2_netowrks1;

import com.example.hw2p2_netowrks1.records.User;
import javafx.scene.control.Alert;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class TCPServer extends Thread {
    int port;
    ServerSocket welcomeSocket;

    TCPServer(int port) throws IOException {
        setPort(port);
        welcomeSocket = new ServerSocket(port);
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket connectionSocket = welcomeSocket.accept();
                BufferedReader inFromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
                DataOutputStream outToClient = new DataOutputStream(connectionSocket.getOutputStream());

                String clientIP = connectionSocket.getInetAddress().getHostAddress();
                int clientPort = connectionSocket.getPort();

                String response = operation(inFromClient.readLine(), clientIP, clientPort);
                outToClient.writeBytes(response + "\n");
            } catch (Exception e) {
               e.printStackTrace();
            }

        }
    }

    public String operation(String message, String clientIP, int clientPort) throws IOException {
        String res = "";
        switch (message.split(":")[0]) {
            case "AUTH":
                String username = message.split(":")[1].split(",")[0];
                String password = message.split(":")[1].split(",")[1];
                res = Server.loginLogic(username, password, clientIP, clientPort);

                if(res.equals("404")) break;
                // update the last login
                User usr_t = User.parseUser(res);
                usr_t.setLastLogin(new Date().toString());
                new FileHandler().updateOrDeleteLineInFile(usr_t.getUsername(), 'u', usr_t.toString());

                break;

            case "ACTIVE_USERS":
                res = Server.getActiveUsersinOne();
                break;
            case "UPDATE_STATUS":
                String uName = message.split(":")[1].split(",")[0];
                String uStatus = message.split(":")[1].split(",")[1];
                User usr_tt = User.parseUser(new FileHandler().findRecord(uName));
                usr_tt.setStatus(uStatus);
                new FileHandler().updateOrDeleteLineInFile(uName,'u',usr_tt.toString());
        }
        return res;
    }

}
