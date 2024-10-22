package com.example.hw2p2_netowrks1;

import java.io.IOException;
import java.net.*;

public class UDPClient {

    public InetAddress IPAddress;
    public int port;
    public DatagramSocket clientSocket;

    byte[] sendData;
    UDPClient() throws SocketException {
        sendData = new byte[65535];
        clientSocket = new DatagramSocket();
    }

    public void setIPAddress(String IPAddress) throws UnknownHostException {
        this.IPAddress = InetAddress.getByName(IPAddress);
    }

    public void setPort(int port) {
        this.port = port;
    }


    public void sendMessage(String message) throws IOException {
        sendData = message.trim().getBytes();
        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
        clientSocket.send(sendPacket);
        clientSocket.close();

    }
}
