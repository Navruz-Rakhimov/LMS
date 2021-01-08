package book;

import authentication.UsersRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class BooksRepository {
    String dbName = "database";
    String connectionURL = "jdbc:derby:./db/" + dbName;
    Connection conn = null;

    private final String GET_ALL_BOOKS_QUERY = "SELECT * FROM books";
    private final String GET_BOOK_QUANTITY_QUERY = "SELECT quantity FROM books WHERE isbn=?";
    private final String ADD_BOOK_QUERY = "INSERT INTO books(isbn, title, editionNumber, copyright, quantity) VALUES (?,?,?,?,?)";
    private final String CHECK_IF_EXISTS_QUERY = "SELECT COUNT(*) FROM books WHERE isbn=?";
    private final String UPDATE_QUANTITY_QUERY = "UPDATE books SET quantity=? WHERE isbn=?";
    private final String DELETE_FROM_BOOKS_QUERY = "DELETE FROM books WHERE isbn=?";
    private final String DELETE_FROM_STUDENT_BOOK_QUERY = "DELETE FROM studentBook WHERE isbn=?";
    private final String DELETE_FROM_OVERDUE_BOOK_QUERY = "DELETE FROM overdueBooks WHERE isbn=?";
    private final String DELETE_FROM_AUTHOR_ISBN_QUERY = "DELETE FROM authorISBN WHERE isbn=?";
    private final String UPDATE_BOOK_QUERY = "UPDATE books SET isbn=?, title=?, editionNumber=?, copyright=?, quantity=? WHERE isbn=?";

    private PreparedStatement getAllBooksStmt;
    private PreparedStatement getBookQuantityStmt;
    private PreparedStatement addBookStmt;
    private PreparedStatement checkStmt;
    private PreparedStatement updateStmt;

    private PreparedStatement deleteFromBooks;
    private PreparedStatement deleteFromStudentBook;
    private PreparedStatement deleteFromOverdueBooks;
    private PreparedStatement deleteFromAuthorIsbn;


    private static BooksRepository instance;

    private BooksRepository() {
        try {
            conn = DriverManager.getConnection(connectionURL);

            getAllBooksStmt = conn.prepareStatement(GET_ALL_BOOKS_QUERY);
            getBookQuantityStmt = conn.prepareStatement(GET_BOOK_QUANTITY_QUERY);
            addBookStmt = conn.prepareStatement(ADD_BOOK_QUERY);
            checkStmt = conn.prepareStatement(CHECK_IF_EXISTS_QUERY);
            updateStmt = conn.prepareStatement(UPDATE_QUANTITY_QUERY);

            deleteFromBooks = conn.prepareStatement(DELETE_FROM_BOOKS_QUERY);
            deleteFromStudentBook = conn.prepareStatement(DELETE_FROM_STUDENT_BOOK_QUERY);
            deleteFromOverdueBooks = conn.prepareStatement(DELETE_FROM_OVERDUE_BOOK_QUERY);
            deleteFromAuthorIsbn = conn.prepareStatement(DELETE_FROM_AUTHOR_ISBN_QUERY);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static BooksRepository getInstance() {
        if (instance == null) {
            instance = new BooksRepository();
        }
        return instance;
    }

    public ObservableList<Book> getAllBooks() {
        ResultSet rs;
        ObservableList<Book> list = FXCollections.observableArrayList();

        try {
            rs = getAllBooksStmt.executeQuery();

            // isbn, title, editionNumber, copyright, quantity
            Book book = null;
            while(rs.next()) {
                book = new Book(rs.getString("isbn"), rs.getString("title"), rs.getString("editionNumber"),
                        rs.getString("copyright"), rs.getInt("quantity"));

                list.add(book);
            }
            return list;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }


    public void deleteBook(Book book) {
        try {
            deleteFromStudentBook.setString(1, book.getIsbn());
            deleteFromOverdueBooks.setString(1, book.getIsbn());
            deleteFromAuthorIsbn.setString(1, book.getIsbn());
            deleteFromBooks.setString(1, book.getIsbn());

            deleteFromAuthorIsbn.executeUpdate();
            deleteFromOverdueBooks.executeUpdate();
            deleteFromAuthorIsbn.executeUpdate();
            deleteFromBooks.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void updateBook(String isbn, Book book) {

    }

    public void addBook(Book book) {
        try {
            checkStmt.setString(1, book.getIsbn());
            ResultSet rs = checkStmt.executeQuery();
            rs.next();
            if (rs.getInt(1) > 0) {
                getBookQuantityStmt.setString(1, book.getIsbn());
                rs = getBookQuantityStmt.executeQuery();
                rs.next();
                int bookQuantity = rs.getInt(1);
                bookQuantity++;
                updateStmt.setInt(1, bookQuantity);
                updateStmt.setString(2, book.getIsbn());
                updateStmt.executeUpdate();
            } else {
                addBookStmt.setString(1, book.getIsbn());
                addBookStmt.setString(2, book.getTitle());
                addBookStmt.setString(3, book.getEdition());
                addBookStmt.setString(4, book.getCopyright());
                addBookStmt.setString(5, String.valueOf(book.getQuantity()));

                addBookStmt.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
