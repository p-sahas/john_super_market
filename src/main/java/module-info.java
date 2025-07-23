module com.example.johnssupermarket {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.johnssupermarket to javafx.fxml;
    exports com.example.johnssupermarket;
}