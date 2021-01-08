package librarianWindow;

import adminWindow.NewBookDialogController;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import librarianWindow.booksModifyWindow.BooksModifyController;
import staticTools.UserTracker;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class librarianWindowController {
    ObservableList<Book> books;

    @FXML
    private Label userEmailLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private TableView<Book> booksTableView;

    @FXML
    public void initialize(){
        userEmailLabel.setText(UserTracker.getLastTrackedUser());
        books = BooksRepository.getInstance().getAllBooks();
        booksTableView.setItems(books);
    }

    @FXML
    public void handleAddButton() {
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
            // .....
            if (containsIsbn(books, book.getIsbn())) {
                books = BooksRepository.getInstance().getAllBooks();
                booksTableView.setItems(books);
            } else {
                books.add(book);
            }
        }
    }

    @FXML
    public void handleModifyButton() throws IOException, SQLException {
        int addressIndex = booksTableView.getSelectionModel().getSelectedIndex();
        Book book = books.get(addressIndex);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/librarianWindow/booksModifyWindow/booksModifyWindow.fxml"));
        Parent parent = loader.load();
        BooksModifyController dialogController = loader.<BooksModifyController>getController();
        dialogController.setBookInfo(book, books);

        Scene scene = new Scene(parent, 400, 400);
        Stage stage = new Stage();
        stage.setTitle("Modify Book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        booksTableView.refresh();
    }

    @FXML
    public void handleDeleteButton() {
        try {
            int addressIndex = booksTableView.getSelectionModel().getSelectedIndex();
            Book book = books.get(addressIndex);
            BooksRepository.getInstance().deleteBook(book);
            books.remove(addressIndex);
        } catch (Exception e) {
            System.out.println("Select an item");
        }
    }

    @FXML
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

    // https://stackoverflow.com/questions/18852059/java-list-containsobject-with-field-value-equal-to-x
    private boolean containsIsbn(final List<Book> list, final String isbn){
        return list.stream().filter(o -> o.getIsbn().equals(isbn)).findFirst().isPresent();
    }
}
