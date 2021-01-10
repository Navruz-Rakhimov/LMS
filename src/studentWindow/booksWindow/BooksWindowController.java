package studentWindow.booksWindow;

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
import javafx.scene.control.*;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import user.Student;

import java.io.IOException;
import java.util.Optional;

public class BooksWindowController {

    Student student;
    ObservableList<Book> books;

    @FXML
    public TextField searchTxt;
    @FXML
    public ChoiceBox choiceBox;
    @FXML
    public GridPane gridPane;
    @FXML
    public TableView tableView;
    @FXML
    public Label contentLabel;

    public void initialize() {
        contentLabel.setText("Books");
        String email = UsersRepository.getInstance().getCurrentUserEmail();
        student = UsersRepository.getInstance().getStudent(email);
        choiceBox.getItems().add("Title");
        choiceBox.getItems().add("ISBN");
        choiceBox.getItems().add("Author");
        choiceBox.getItems().add("Copyright");

        choiceBox.setValue("Title");

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

        books = BooksRepository.getInstance().getAllBooks();
        tableView.setItems(books);

        searchTxt.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("newValue: " + newValue);
            if (!newValue.equals("")) {
                books = BooksRepository.getInstance().searchBooks(choiceBox.getValue().toString(), newValue);
                tableView.setItems(books);
            } else {
                books = BooksRepository.getInstance().getAllBooks();
                tableView.setItems(books);
            }
        });
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

    public void handleBorrow(ActionEvent actionEvent) {
        int addressIndex = tableView.getSelectionModel().getSelectedIndex();
        Book book = books.get(addressIndex);
        if(!BooksRepository.getInstance().borrowBook(student, book)) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Borrow Book");
            alert.setHeaderText("Access Denied!");
            alert.setContentText("You already have this book or no such book available");
            alert.showAndWait();
        }
        books = BooksRepository.getInstance().getAllBooks();
        tableView.setItems(books);
    }

}
