package adminWindow.adminBookWindow;

import adminWindow.NewBookDialogController;
import book.Book;
import book.BooksRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class AdminBookController {


    ObservableList<Book> books;

    @FXML
    public GridPane gridPane;
    @FXML
    public Button studentsButton;
    @FXML
    public Button librariansButton;
    @FXML
    public Button booksButton;
    @FXML
    public Button signOutButton;
    @FXML
    public TableView tableView;

    public void initialize() {
        books = BooksRepository.getInstance().getAllBooks();
        tableView.setItems(books);
    }

    public void handleStudentsButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/adminWindow/adminWindowFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLibrariansButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/adminWindow/adminLibrarianWindow/adminLibrarianFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleBooksButton(ActionEvent actionEvent) {
        books = BooksRepository.getInstance().getAllBooks();
        tableView.setItems(books);
    }

    public void handleViewButton(ActionEvent actionEvent) {
    }

    public void handleModifyButton(ActionEvent actionEvent) {
    }

    public void handleAddButton(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());

        dialog.setTitle("Add New Book");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/adminWindow/newBookDialogFxml.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewBookDialogController controller = fxmlLoader.getController();

            Book book = controller.getBook();

            System.out.println("BOOK: " + book.getIsbn() + " " + book.getTitle() + " " + book.getEdition() + " " + book.getCopyright() + " " + book.getQuantity());

            BooksRepository.getInstance().addBook(book);
            books.add(book);
        }
    }

    public void handleDeleteButton(ActionEvent actionEvent) {
        try {
            int addressIndex = tableView.getSelectionModel().getSelectedIndex();
            Book book = books.get(addressIndex);
            BooksRepository.getInstance().deleteBook(book);
            books.remove(addressIndex);
        } catch (Exception e) {
            System.out.println("Select an item");
        }
    }

    public void handleSignOutButton(ActionEvent actionEvent) {
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