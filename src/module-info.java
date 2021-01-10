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
  
    opens librarianWindow;
    opens librarianWindow.booksModifyWindow;
    opens librarianWindow.studentModifyWindow;
    opens librarianWindow.borrowedBooksDialogWindow;

    opens studentWindow.profileWindow;
    opens studentWindow.booksWindow;
    opens studentWindow.myBooksWindow;
    opens studentWindow.overdueBooksWindow;

}