package com.example.hw2p2_netowrks1;

import com.example.hw2p2_netowrks1.records.User;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.concurrent.ThreadLocalRandom;

public class Server implements Initializable {

    public boolean serverStarted = false;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        allUsersAway();

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(2), event -> {
            if (serverStarted)
                reloadActiveUsers();
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    private ListView<Label> activeUsers;

    @FXML
    private PasswordField password;

    @FXML
    private TextField portNumber;

    @FXML
    private Label status;

    @FXML
    private TextField username;

    @FXML
    void onAddUser(ActionEvent event) {

        String uName = username.getText();
        String pass = password.getText();

        User usr = new User(
                uName, pass
                , ""
                , generateRandomPort(), ""
                , "away", generateRandomHexColor());

        String res = new FileHandler().writeToFile(usr.toString(),uName);
        System.out.println(usr.toString());
        System.out.println(uName);
        System.out.println(res);
        if(res.equals("200")){
            new Alert(Alert.AlertType.INFORMATION,"Added User").show();
        }
        else if(res.equals("401")){
            new Alert(Alert.AlertType.ERROR,"username is taken").show();
        }

    }

    public static int generateRandomPort() {
        Random random = new Random();
        return random.nextInt(65535 - 1024 + 1) + 1024;
    }

    public static String generateRandomHexColor() {
        int randomColor = ThreadLocalRandom.current().nextInt(0x1000000);
        return String.format("#%06X", randomColor);
    }


    @FXML
    void onStartServer(ActionEvent event) throws IOException {
        TCPServer tcp = new TCPServer(Integer.parseInt(portNumber.getText().trim()));
        tcp.start();
        status.setText("Server Started running on port " + portNumber.getText());
        serverStarted = true;
        reloadActiveUsers();
    }


    public static String loginLogic(String username, String password, String ip, int port) throws IOException {
        List<String> users = new FileHandler().findAllLines();

        for (String usr : users) {
            String username_t = usr.split(",")[0];
            String password_t = usr.split(",")[1];

            if (username_t.equalsIgnoreCase(username) && password_t.equals(password)) {
                User usr_t = User.parseUser(usr);
                usr_t.setIPAdress(ip);
                new FileHandler().updateOrDeleteLineInFile(username, 'u', usr_t.toString());
                return usr_t.toString();
            }
        }
        return "404";
    }

    public void reloadActiveUsers() {

        List<String> users = getActiveUsers();

        activeUsers.getItems().clear();

        for (String user : users) {
            User usr_t = User.parseUser(user);

            Label l = new Label(usr_t.getUsername() + "," + usr_t.getIPAdress() + "," + usr_t.getPort());
            l.setStyle("-fx-text-fill: " + usr_t.getColor());

            activeUsers.getItems().add(l);
        }

    }

    public static List<String> getActiveUsers() {
        List<String> users = new FileHandler().findAllLines();

        List<String> res = new ArrayList<>();
        for (String usr : users) {
            User usr_t = User.parseUser(usr);
            if (usr_t.getStatus().equalsIgnoreCase("active")) {
                res.add(usr_t.toString());
            }
        }

        return res;
    }


    public static String getActiveUsersinOne() {


        List<String> users = new FileHandler().findAllLines();
        if(users.isEmpty()) return "404";
        String res = "";
        for (String usr : users) {
            res += usr + ";";
        }

        res = res.substring(0, res.length() - 1);

        return res;
    }

    public void allUsersAway(){
        List<String> users = new FileHandler().findAllLines();

        List<String> res = new ArrayList<>();
        for (String usr : users) {
            User usr_t = User.parseUser(usr);
            usr_t.setStatus("Away");
            new FileHandler().updateOrDeleteLineInFile(usr_t.getUsername(),'u',usr_t.toString());
        }
    }

}
