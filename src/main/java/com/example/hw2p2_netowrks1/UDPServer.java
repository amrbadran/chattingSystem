package com.example.hw2p2_netowrks1;

import com.example.hw2p2_netowrks1.records.User;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPServer extends Thread {
    public DatagramSocket serverSocket;
    byte[] receiveData;
    int port;

    private ListView<Label> chat;
    private ListView<Label> activeUsers;

    UDPServer(int port, ListView<Label> chat, ListView<Label> activeUsers) throws SocketException {
        setPort(port);
        receiveData = new byte[65535];
        this.chat = chat;
        this.activeUsers = activeUsers;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new DatagramSocket(getPort());
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        while (true) {
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            try {
                serverSocket.receive(receivePacket);
                String message = new String(receivePacket.getData()).trim();
                System.out.println(message);
                Platform.runLater(() -> chat.getItems().add(Helper.formatMessage(message)));
                receiveData = new byte[65535];
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
    }
}
