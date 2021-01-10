package studentWindow.myBooksWindow;

import authentication.UsersRepository;
import book.BooksRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import user.Student;

import java.io.IOException;

public class MyBooksWindowController {

    Student student;
    @FXML
    public TableView tableView;

    public void initialize() {
        String email = UsersRepository.getInstance().getCurrentUserEmail();
        student = UsersRepository.getInstance().getStudent(email);
        tableView.setItems(BooksRepository.getInstance().getStudentBooks(student));
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

    public void handleMyBooks(ActionEvent actionEvent) {

    }

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
}
