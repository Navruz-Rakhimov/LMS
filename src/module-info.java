module LMS {
    requires javafx.fxml;
    requires javafx.controls;
    requires java.sql;

    opens authentication;
    opens adminWindow;
    opens book;
    opens user;
    opens adminWindow.adminBookWindow;
    opens adminWindow.adminLibrarianWindow;
    opens adminWindow.viewDialogs;
}