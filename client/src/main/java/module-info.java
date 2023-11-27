module com.grotor.javafxcw {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.grotor.javafxcw to javafx.fxml;
    exports com.grotor.javafxcw;
    exports com.grotor.javafxcw.controller;
    opens com.grotor.javafxcw.controller to javafx.fxml;
}