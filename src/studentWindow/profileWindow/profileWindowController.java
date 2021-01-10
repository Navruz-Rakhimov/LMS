package studentWindow.profileWindow;

import authentication.UsersRepository;
import book.BooksRepository;
import book.Fine;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import user.Student;
import user.User;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class profileWindowController {

    Student student;

    @FXML
    public Button booksButton;
    @FXML
    public Button signOutButton;
    @FXML
    public Label firstName;
    @FXML
    public Label lastName;
    @FXML
    public Label numberOfBooks;
    @FXML
    public Label overdueBooks;
    @FXML
    public Label fineAmount;

    public void initialize() {
        String email = UsersRepository.getInstance().getCurrentUserEmail();
        student = UsersRepository.getInstance().getStudent(email);

        firstName.setText(student.getFirstName());
        lastName.setText(student.getLastName());
        numberOfBooks.setText(String.valueOf(BooksRepository.getInstance().getStudentBooks(student).size()));
        overdueBooks.setText(String.valueOf(BooksRepository.getInstance().getOverdueBooks(student).size()));
        fineAmount.setText(String.format("%.2f", BooksRepository.getInstance().getTotalFine(student)));
    }

    public void handleProfile(ActionEvent actionEvent) {}

    public void handleMyBooks(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/studentWindow/myBooksWindow/myBooksWindow.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
