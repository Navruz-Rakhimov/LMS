package adminWindow;

import book.Author;
import book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class NewBookDialogController {

    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    public TableView table;

    ObservableList<Author> authors = FXCollections.observableArrayList();

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

    public Book getBook() {
        Book book = null;

        String isbn1 = isbn.getText().trim();
        String title1 = title.getText().trim();
        String edition1 = edition.getText().trim();
        String copyright1 = copyright.getText().trim();
        int quantity1 = 0;
        if (!quantity.getText().trim().isEmpty())
            try {
                quantity1 = Integer.parseInt(quantity.getText().trim());
            } catch (NumberFormatException e) {
                quantity1 = 0;
            }

        if (!isbn1.isEmpty() && !title1.isEmpty() && !edition1.isEmpty() && !copyright1.isEmpty() && quantity1 != 0) {
            book =  new Book(isbn1, title1, edition1, copyright1, quantity1);
        }

        return book;
        // return new Book("777766X", "Intro to CS", "7", "2077", 77);
    }

    public void setBook(Book book) {
        isbn.setText(book.getIsbn());
        title.setText(book.getTitle());
        edition.setText(book.getEdition());
        copyright.setText(book.getCopyright());
        quantity.setText(String.valueOf(book.getQuantity()));
    }

    public ObservableList<Author> getAuthors() {
        return authors;
    }

    public void handleAddButton(ActionEvent actionEvent) {
        String firstN = firstName.getText().trim();
        String lastN = lastName.getText().trim();

        if(!firstN.equals("") && !lastN.equals("")) {
            Author author = new Author(firstN, lastN);
            authors.add(author);
            table.setItems(authors);
        }
    }
}
