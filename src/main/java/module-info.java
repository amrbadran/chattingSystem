module com.example.hw2p2_netowrks1 {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.example.hw2p2_netowrks1 to javafx.fxml;
    exports com.example.hw2p2_netowrks1;
}