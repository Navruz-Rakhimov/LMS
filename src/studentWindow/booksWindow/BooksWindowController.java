package studentWindow.booksWindow;

import authentication.UsersRepository;
import book.BooksRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import user.Student;

import java.io.IOException;
import java.util.Optional;

public class BooksWindowController {

    Student student;

    @FXML
    public TableView tableView;
    @FXML
    public Label contentLabel;

    public void initialize() {
        contentLabel.setText("Books");
        String email = UsersRepository.getInstance().getCurrentUserEmail();
        student = UsersRepository.getInstance().getStudent(email);

        if (student.isBlocked()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Access Denied");
            alert.setHeaderText("Your account is blocked");
            alert.setContentText("Contact the administrator or librarian!");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Parent root = null;
                try {
                    root = FXMLLoader.load(getClass().getResource("/authentication/logInFxml.fxml"));
                    Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.showAndWait();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        tableView.setItems(BooksRepository.getInstance().getAllBooks());
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

    public void handleBooks(ActionEvent actionEvent) {}

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

    public void handleViewButton(ActionEvent actionEvent) {
    }

    public void handleBorrow(ActionEvent actionEvent) {
    }

}
