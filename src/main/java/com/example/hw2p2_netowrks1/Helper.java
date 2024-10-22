package com.example.hw2p2_netowrks1;

import com.example.hw2p2_netowrks1.records.Message;
import com.google.gson.Gson;
import javafx.scene.control.Label;


public class Helper {

    public static Label formatMessage(String data) {

        Message m = Message.parseMessage(data);

        String text = m.getTime() + " [" + m.getSender_username() + ":" + m.getPort() + "]" +
                " " + m.getData();

        Label l = new Label(text);
        if (m.getSender().equalsIgnoreCase("me")) {
            l.setStyle("-fx-text-fill: blue");
        } else {
            String color = m.getColor().isEmpty()
                    ? "red"
                    : m.getColor();
            System.out.println(color);
            l.setStyle("-fx-text-fill: " + color);
        }

        return l;
    }

}
