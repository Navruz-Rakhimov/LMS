package adminWindow.viewDialogs;

import book.Author;
import book.Book;
import book.BooksRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class BookViewController {

    @FXML
    public TextField isbn;
    @FXML
    public TextField title;
    @FXML
    public TextField edition;
    @FXML
    public TextField copyright;
    @FXML
    public TextField quantity;
    @FXML
    public TableView<Author> table;

    public void setBook(Book book) {
        isbn.setText(book.getIsbn());
        title.setText(book.getTitle());
        edition.setText(book.getEdition());
        copyright.setText(book.getCopyright());
        quantity.setText(String.valueOf(book.getQuantity()));
        table.setItems(BooksRepository.getInstance().getAuthors(book));
    }
}
