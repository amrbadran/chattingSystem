package com.example.hw2p2_netowrks1;

import com.example.hw2p2_netowrks1.records.Message;
import com.google.gson.Gson;
import javafx.scene.control.Label;


public class Helper {

    public static Label formatMessage(String data) {

        Message m = Message.parseMessage(data);

        String text = m.getTime() + " [" + m.getSender() + ":" + m.getPort() + "]" +
                " " + m.getData();

        Label l = new Label(text);
        if(m.getSender().equalsIgnoreCase("me")){
            l.setStyle("-fx-text-fill: blue");
        }
        else{
            l.setStyle("-fx-text-fill: red");
        }


        return l;
    }
}
