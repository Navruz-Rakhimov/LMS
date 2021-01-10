package librarianWindow.booksModifyWindow;

import authentication.UsersRepository;
import book.Book;
import book.BooksRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import librarianWindow.StudentsBorrowed;
import user.User;

import java.sql.ResultSet;
import java.sql.SQLException;

public class LendBookWindowController {
    private ObservableList<Book> books;
    private ObservableList<User> students;
    Book book;

    @FXML
    private TableView<User> studentsTableView;
    @FXML
    private Label bookTitleLabel;

    @FXML
    public void initialize(){}

    @FXML
    public void setUpWindow(Book selectedBook, ObservableList<Book> books){
        this.books = books;
        this.book = selectedBook;
        bookTitleLabel.setText(selectedBook.getTitle());

        // setting up student list table
        students = UsersRepository.getInstance().getAllStudents();
        studentsTableView.setItems(students);
    }

    @FXML
    public void handleLendButton(ActionEvent act) throws SQLException {
        int addressIndex = studentsTableView.getSelectionModel().getSelectedIndex();
        User user = students.get(addressIndex);

        // quantity check
        int tempQuantity = Integer.parseInt(BooksRepository.getInstance().getBookQuantity(book.getIsbn()));
        if (tempQuantity <= 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Book Landing");
            alert.setContentText("There is no book left.");
            alert.showAndWait();
            closeStage(act);
        } else if (BooksRepository.getInstance().checkIfStudentBorrowed(book.getIsbn(), user.getUserId())){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Book Landing");
            alert.setContentText("This Student has already taken this book.");
            alert.showAndWait();
        }else if (UsersRepository.getInstance().isBlocked(user)){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Book Landing");
            alert.setContentText("This Student has been blocked.");
            alert.showAndWait();
        }
        else {
            BooksRepository.getInstance().addBookToStudentBook(user.getUserId(), book.getIsbn());
            BooksRepository.getInstance().decrementQuantity(book.getIsbn());
            this.books.get(this.books.indexOf(book)).setQuantity(this.books.get(this.books.indexOf(book)).getQuantity() - 1);
            closeStage(act);
        }
    }

    @FXML
    private void closeStage(ActionEvent event) {
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
