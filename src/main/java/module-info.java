module com.example.tertris {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.Tetris to javafx.fxml;
    exports com.example.Tetris;
}