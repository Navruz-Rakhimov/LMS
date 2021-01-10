package librarianWindow;

import adminWindow.NewBookDialogController;
import adminWindow.NewUserDialogController;
import authentication.UsersRepository;
import book.Book;
import book.BooksRepository;
import book.BorrowedBook;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import librarianWindow.booksModifyWindow.BooksModifyController;
import librarianWindow.booksModifyWindow.LendBookWindowController;
import librarianWindow.borrowedBooksDialogWindow.BorrowedBooksController;
import librarianWindow.studentModifyWindow.StudentModifyWindowController;
import staticTools.UserTracker;
import user.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class librarianWindowController {
    ObservableList<User> students;
    ObservableList<Book> books;
    ObservableList<BorrowedBook> borrowedBooks;
    ObservableList<User> blockedStudents;

    @FXML
    private TableView<User> blockedStudentsTableView;
    @FXML
    private Label userEmailLabel;
    @FXML
    private GridPane gridPane;
    @FXML
    private TableView<Book> booksTableView;
    @FXML
    private TableView<User> studentsTableView;
    @FXML
    private TableView<BorrowedBook> borrowedBookTableView;

    @FXML
    public void initialize() throws SQLException {
        userEmailLabel.setText(UserTracker.getLastTrackedUser());
        books = BooksRepository.getInstance().getAllBooks();
        blockedStudents = UsersRepository.getInstance().getAllBlockedStudents();
        borrowedBooks = BooksRepository.getInstance().getBorrowedBooks();
        students = UsersRepository.getInstance().getAllStudents();
        booksTableView.setItems(books);
        studentsTableView.setItems(students);
        borrowedBookTableView.setItems(borrowedBooks);
        blockedStudentsTableView.setItems(blockedStudents);
    }
    public void reinitialize(){
        borrowedBooks = BooksRepository.getInstance().getBorrowedBooks();
        borrowedBookTableView.setItems(borrowedBooks);
    }

    // books tab
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
        dialogController.setBookInfo(book);

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
    public void handleLendButton() throws IOException {
        int addressIndex = booksTableView.getSelectionModel().getSelectedIndex();
        Book book = books.get(addressIndex);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/librarianWindow/booksModifyWindow/lendBookWindow.fxml"));
        Parent parent = loader.load();
        LendBookWindowController dialogController = loader.<LendBookWindowController>getController();
        dialogController.setUpWindow(book, this.books);

        Scene scene = new Scene(parent, 475, 600);
        Stage stage = new Stage();
        stage.setTitle("Lend Book");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
        booksTableView.refresh();
        reinitialize();
        borrowedBookTableView.refresh();
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



    // students tab
    @FXML
    public void handleAddStudentButton(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());

        dialog.setTitle("Add New Student");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/adminWindow/newUserDialogFxml.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewUserDialogController controller = fxmlLoader.getController();
            User student = controller.getUser(2);
            if (UsersRepository.getInstance().addUser(student)) {
                student.setUserId(UsersRepository.getInstance().getUserId(student));
                students.add(student);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Add New Student");
                alert.setHeaderText("The student was not added!");
                alert.setContentText("Student with entered email already exists!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleDeleteStudentButton(ActionEvent actionEvent) {
        try {
            int addressIndex = studentsTableView.getSelectionModel().getSelectedIndex();
            User student = students.get(addressIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Student");
            alert.setHeaderText("Are you sure you want to delete the student: " + student.getFirstName() + " " + student.getLastName());
            Optional<ButtonType> result  = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                UsersRepository.getInstance().deleteUser(student);
                students.remove(addressIndex);
            }
        } catch (Exception e) {
            System.out.println("Select an item");
        }
    }

    @FXML
    public void handleModifyStudentButton(ActionEvent act) throws IOException {
        int addressIndex = studentsTableView.getSelectionModel().getSelectedIndex();
        User user = students.get(addressIndex);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/librarianWindow/studentModifyWindow/studentModifyWindow.fxml"));
        Parent parent = loader.load();
        StudentModifyWindowController dialogController = loader.<StudentModifyWindowController>getController();
        dialogController.setUserParams(user);

        Scene scene = new Scene(parent, 400, 400);
        Stage stage = new Stage();
        stage.setTitle("Modify Student Info");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();

        studentsTableView.refresh();
    }


    // Borrowed books tab
    @FXML
    public void handleViewBorrowed() throws IOException {
        int addressIndex = borrowedBookTableView.getSelectionModel().getSelectedIndex();
        BorrowedBook book = borrowedBooks.get(addressIndex);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/librarianWindow/borrowedBooksDialogWindow/borrowedBooks.fxml"));
        Parent parent = loader.load();
        BorrowedBooksController dialogController = loader.<BorrowedBooksController>getController();
        dialogController.setStudentsInfo(BooksRepository.getInstance().getStudentsWhoBorrowed(book.getIsbn()), book.getTitle());

        Scene scene = new Scene(parent, 400, 400);
        Stage stage = new Stage();
        stage.setTitle("Borrow Info");
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(scene);
        stage.showAndWait();
    }
}
