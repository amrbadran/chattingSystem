package com.example.hw2p2_netowrks1;

import com.example.hw2p2_netowrks1.records.Message;
import com.example.hw2p2_netowrks1.records.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.ResourceBundle;

public class Client implements Initializable {

    public boolean flag_logged_in = false;
    public int timer_c = 0;

    public boolean flag_timer = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        statusCombo.getItems().addAll("Active", "Away", "Busy");

        statusCombo.setValue("Active");

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            try {
                if (flag_logged_in)
                    reloadActiveUsers();
            } catch (IOException e) {
                // hide the error (error will pass if you start the server)
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        Timeline timeline2 = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            if(timer_c == 1 && flag_timer){
                try {
                    updateStatus("Active");
                    flag_timer = false;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }


            }
            if(timer_c == 30){
                try {
                    updateStatus("Away");
                    flag_timer = true;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            if(!loggedInAs.getText().isEmpty()){
                timer_c++;
                timer.setText("" + timer_c);
            }

        }));

        timeline2.setCycleCount(Timeline.INDEFINITE);
        timeline2.play();

    }

    @FXML
    void onEvent(MouseEvent event) throws IOException {
        timer_c = 0;

    }
    @FXML
    void onSomeEvent(KeyEvent event) throws IOException {
        timer_c = 0;
    }

    @FXML
    private ListView<Label> activeUsers;

    @FXML
    private ListView<Label> chat;

    @FXML
    private TextField destIp;

    @FXML
    private TextField destPort;

    @FXML
    private TextArea message;

    @FXML
    private PasswordField password;

    @FXML
    private TextField serverIp;

    @FXML
    private TextField serverPort;

    @FXML
    private TextField userIp;

    @FXML
    private TextField userPort;

    @FXML
    private TextField username;
    @FXML
    private Label lastLogin;
    @FXML
    private Label loggedInAs;
    @FXML
    private Label userColor;
    @FXML
    private ComboBox<String> statusCombo;
    @FXML
    private Label timer;

    @FXML
    void onClearConversation(ActionEvent event) {
        chat.getItems().clear();
    }

    @FXML
    void onDeleteMessage(ActionEvent event) {
        Label selectedLabel = chat.getSelectionModel().getSelectedItem();
        if (selectedLabel != null) {
            chat.getItems().remove(selectedLabel);
        }
    }

    @FXML
    void onLogin(ActionEvent event) throws IOException {

        if (!loggedInAs.getText().isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "You Are Already Logged in").show();
            ;
            return;
        }
        TCPClient tcpClient = new TCPClient("AUTH");
        tcpClient.setIPAddress(serverIp.getText());
        tcpClient.setPort(Integer.parseInt(serverPort.getText()));

        if (username.getText().isEmpty() || password.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "Empty Fields").show();
            ;
            return;
        }
        String response = tcpClient.sendRequest(username.getText() + "," + password.getText());


        if (!response.equals("404")) {

            new Alert(Alert.AlertType.INFORMATION, "Welcome").show();

            User tmp = User.parseUser(response);

            userPort.setText(String.valueOf(tmp.getPort()));
            userIp.setText(tmp.getIPAdress());
            lastLogin.setText("Last Login :" + tmp.getLastLogin());
            loggedInAs.setText(tmp.getUsername());
            userColor.setText(tmp.getColor());
            flag_logged_in = true;
            updateStatus("Active");
            reloadActiveUsers();


            try {
                UDPServer server = new UDPServer(Integer.parseInt(userPort.getText().trim()), chat, activeUsers);
                server.start();
            } catch (Exception e) {
                // this is to handle if udp has already started
            }

        } else {
            new Alert(Alert.AlertType.ERROR, "Username or password is error").show();
        }


    }

    @FXML
    void onActiveUserClicked() {
        Label selectedLabel = activeUsers.getSelectionModel().getSelectedItem();
        if (selectedLabel != null) {
            destIp.setText(selectedLabel.getText().split(",")[1]);
            destPort.setText(selectedLabel.getText().split(",")[2]);
        }
    }

    @FXML
    void onSend(ActionEvent event) throws IOException {
        if (destPort.getText().equals(userPort.getText()) && destIp.getText().equals(userIp.getText()))
            return;
        sendToChat(Integer.parseInt(destPort.getText().trim()), destIp.getText().trim(), "");
    }

    public boolean flag_sending = true;

    public void sendToChat(int port, String IP, String toAll) throws IOException {
        UDPClient client = new UDPClient();
        client.setPort(port);
        client.setIPAddress(IP);

        Message m = new Message(
                message.getText(),
                userIp.getText(),
                userPort.getText(),
                new Date().toString()
        );
        if (!loggedInAs.getText().isEmpty()) {
            m.setSender_username(loggedInAs.getText());
            m.setColor(userColor.getText());
        }

        client.sendMessage(m.toString());

        m.setSender("Me");
        Label lb = Helper.formatMessage(m.toString());
        lb.setText(lb.getText() + toAll);
        if (flag_sending)
            chat.getItems().add(lb);
    }

    @FXML
    void onSendToAll(ActionEvent event) throws IOException {
        flag_sending = true;
        System.out.println(loggedInAs.getText());
        for (Label l : activeUsers.getItems()) {
            if (l.getText().split(",")[0].trim().equalsIgnoreCase(loggedInAs.getText().trim())) continue;
            int port = Integer.parseInt(l.getText().split(",")[2]);
            String IP = l.getText().split(",")[1];
            sendToChat(port, IP, " (To All)");
            flag_sending = false;
        }
        flag_sending = true;
    }

    @FXML
    private void onStatusChanged(ActionEvent event) throws IOException {
        String selectedStatus = statusCombo.getValue();

        updateStatus(selectedStatus);
    }

    @FXML
    void onLogout(ActionEvent event) throws IOException {
        updateStatus("Away");
        flag_logged_in = false;
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    public void updateStatus(String status) throws IOException {
        TCPClient tcpClient = new TCPClient("UPDATE_STATUS");
        tcpClient.setIPAddress(serverIp.getText());
        tcpClient.setPort(Integer.parseInt(serverPort.getText()));
        tcpClient.sendRequest(loggedInAs.getText() + "," + status);
    }

    public void reloadActiveUsers() throws IOException {

        activeUsers.getItems().clear();
        TCPClient tcpClient = new TCPClient("ACTIVE_USERS");
        tcpClient.setIPAddress(serverIp.getText());
        tcpClient.setPort(Integer.parseInt(serverPort.getText()));

        try{
            String[] res = tcpClient.sendRequest("abc").split(";");
            for (String s : res) {
                User usr_t = User.parseUser(s);
                Label l = new Label(usr_t.getUsername() + "," + usr_t.getIPAdress() + "," + usr_t.getPort() + ",(" + usr_t.getStatus() + ")");
                l.setStyle("-fx-text-fill: " + usr_t.getColor());

                activeUsers.getItems().add(l);
            }
        }
        catch (Exception e){
            return;
        }

    }


}
