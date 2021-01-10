package studentWindow.myBooksWindow;

import adminWindow.viewDialogs.BookViewController;
import authentication.UsersRepository;
import book.Book;
import book.BooksRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import user.Student;
import user.User;

import java.io.IOException;

public class MyBooksWindowController {


    Student student;

    ObservableList<Book> books;

    @FXML
    public GridPane gridPane;
    @FXML
    public TableColumn borrowedDateColumn;
    @FXML
    public TableColumn dueDateColumn;
    @FXML
    public TableView tableView;

    public void initialize() {
        String email = UsersRepository.getInstance().getCurrentUserEmail();
        student = UsersRepository.getInstance().getStudent(email);
        books = BooksRepository.getInstance().getStudentBooks(student);
        tableView.setItems(books);
    }

    public void handleProfile(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/studentWindow/profileWindow/profileWindow.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleMyBooks(ActionEvent actionEvent) {}

    public void handleOverdueBooks(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/studentWindow/overdueBooksWindow/overdueBooksWindow.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBooks(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/studentWindow/booksWindow/booksWindow.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSignOut(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/authentication/logInFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleReturn(ActionEvent actionEvent) {
        int addressIndex = tableView.getSelectionModel().getSelectedIndex();
        Book book = books.get(addressIndex);
        BooksRepository.getInstance().returnBook(student, book);
        books.remove(book);
    }

    public void handleView(ActionEvent actionEvent) {
        int addressIndex = tableView.getSelectionModel().getSelectedIndex();
        Book book = books.get(addressIndex);

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());
        dialog.setTitle("View Book");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/adminWindow/viewDialogs/bookViewDialogFxml.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        BookViewController controller = fxmlLoader.getController();
        controller.setBook(book);
        dialog.showAndWait();
    }
}
