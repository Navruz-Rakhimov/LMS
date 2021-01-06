module LMS {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens authentication;
    opens adminWindow;
}