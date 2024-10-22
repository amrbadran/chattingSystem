package com.example.hw2p2_netowrks1;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();

        FXMLLoader fxmlLoader2 = new FXMLLoader(HelloApplication.class.getResource("server.fxml"));
        Scene scene2 = new Scene(fxmlLoader2.load());
        Stage stage2 = new Stage();
        stage2.setTitle("Hello!");
        stage2.setScene(scene2);
        stage2.show();

        FXMLLoader fxmlLoader3 = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene3 = new Scene(fxmlLoader3.load());
        Stage stage3 = new Stage();
        stage3.setTitle("Hello!");
        stage3.setScene(scene3);
        stage3.show();

        FXMLLoader fxmlLoader4 = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene4 = new Scene(fxmlLoader4.load());
        Stage stage4 = new Stage();
        stage4.setTitle("Hello!");
        stage4.setScene(scene4);
        stage4.show();
    }

    public static void main(String[] args) {
        launch();
    }
}