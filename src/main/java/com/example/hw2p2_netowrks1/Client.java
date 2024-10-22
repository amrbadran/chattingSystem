package com.example.hw2p2_netowrks1;

import com.example.hw2p2_netowrks1.records.Message;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.*;

public class Client implements Initializable {

    public boolean server_started = false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


    }

    @FXML
    private ListView<Label> archivedMessages;

    @FXML
    private ListView<Label> chat;

    @FXML
    private TextField destIp;

    @FXML
    private TextField destPort;

    @FXML
    private TextArea message;

    @FXML
    private TextField userIp;

    @FXML
    private TextField userPort;


    @FXML
    void onClearConversation(ActionEvent event) {
        chat.getItems().clear();
    }

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    @FXML
    void onDeleteMessage(ActionEvent event) {
        Label selectedLabel = chat.getSelectionModel().getSelectedItem();
        if (selectedLabel != null) {
            archivedMessages.getItems().add(selectedLabel);
            chat.getItems().remove(selectedLabel);

            scheduler.schedule(() -> {
                Platform.runLater(() -> archivedMessages.getItems().remove(selectedLabel));
            }, 15, TimeUnit.SECONDS);
        }
    }

    @FXML
    void onListen(ActionEvent event) throws SocketException {

        UDPServer server = new UDPServer(Integer.parseInt(userPort.getText().trim()),chat);
        server.start();
        server_started = true;
    }

    @FXML
    void onSend(ActionEvent event) throws IOException {
        UDPClient client = new UDPClient();
        client.setPort(Integer.parseInt(destPort.getText().trim()));
        client.setIPAddress(destIp.getText().trim());

        Message m = new Message(
                message.getText(),
                userIp.getText(),
                userPort.getText(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
                );

        client.sendMessage(m.toString());

        m.setSender("Me");
        chat.getItems().add(Helper.formatMessage(m.toString()));

    }

    @FXML
    void onTest(ActionEvent event) {

    }

}
