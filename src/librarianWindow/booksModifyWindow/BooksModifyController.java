package librarianWindow.booksModifyWindow;

import book.Book;
import book.BooksRepository;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

import java.sql.SQLException;

public class BooksModifyController {
    private Book book;
    private ObservableList<Book> books;

    @FXML
    private TextField isbnTextField;
    @FXML
    private TextField titleTextField;
    @FXML
    private TextField editionTextField;
    @FXML
    private TextField copyrightTextField;
    @FXML
    private TextField quantityTextField;

    @FXML
    public void initialize(){}

    @FXML
    public void setBookInfo(Book bookInfo, ObservableList<Book> books){
        book = bookInfo;
        this.books = books;

        isbnTextField.setText(bookInfo.getIsbn());
        titleTextField.setText(bookInfo.getTitle());
        editionTextField.setText(bookInfo.getEdition());
        copyrightTextField.setText(bookInfo.getCopyright());
        quantityTextField.setText(String.format("%d", bookInfo.getQuantity()));
    }

    @FXML
    public void modifyBookInfo(){
        Book editedBook = this.book;
        editedBook.setCopyright(copyrightTextField.getText());
        editedBook.setEdition(editionTextField.getText());
        editedBook.setQuantity(Integer.parseInt(quantityTextField.getText()));
        editedBook.setTitle(titleTextField.getText());
        try {
            BooksRepository.getInstance().updateBook(editedBook);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
