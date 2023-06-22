module com.example.controlejava {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.controlejava to javafx.fxml;
    exports com.example.controlejava;
}