module com.example.thegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.thegame to javafx.fxml;
    exports com.example.thegame;
}