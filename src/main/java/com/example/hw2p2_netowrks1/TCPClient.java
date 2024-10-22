package com.example.hw2p2_netowrks1;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
    Socket clientSocket;
    int port;
    String IPAddress;
    String Action;
    DataOutputStream outToServer;
    BufferedReader inFromServer;

    public TCPClient(int port, String IPAddress, String action) throws IOException {
        this.port = port;
        this.IPAddress = IPAddress;
        Action = action;
        clientSocket = new Socket(IPAddress, port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());
    }

    public TCPClient(String action) {
        Action = action;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String sendRequest(String req) throws IOException {
        clientSocket = new Socket(IPAddress, port);
        outToServer = new DataOutputStream(clientSocket.getOutputStream());

        String message = Action + ":" + req + "\n";
        outToServer.write(message.getBytes());
        inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        String res = inFromServer.readLine();
        clientSocket.close();

        return res;
    }
}
