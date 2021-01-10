package book;

import authentication.UsersRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import user.Student;
import librarianWindow.StudentsBorrowed;
import user.User;
import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class BooksRepository {
    String dbName = "main";
    String connectionURL = "jdbc:derby:./db/" + dbName;
    Connection conn = null;

    // main statements
    private final String GET_ALL_BOOKS_QUERY = "SELECT * FROM books";
    private final String GET_BOOK_QUANTITY_QUERY = "SELECT quantity FROM books WHERE isbn=?";
    private final String ADD_BOOK_QUERY = "INSERT INTO books(isbn, title, editionNumber, copyright, quantity) VALUES (?,?,?,?,?)";
    private final String CHECK_IF_EXISTS_QUERY = "SELECT COUNT(*) FROM books WHERE isbn=?";
    private final String UPDATE_QUANTITY_QUERY = "UPDATE books SET quantity=? WHERE isbn=?";
    private final String DELETE_FROM_BOOKS_QUERY = "DELETE FROM books WHERE isbn=?";
    private final String DELETE_FROM_STUDENT_BOOK_QUERY = "DELETE FROM studentBook WHERE isbn=?";
    private final String DELETE_FROM_OVERDUE_BOOK_QUERY = "DELETE FROM overdueBooks WHERE isbn=?";
    private final String DELETE_FROM_AUTHOR_ISBN_QUERY = "DELETE FROM authorISBN WHERE isbn=?";

    private final String DELETE_FROM_STUDENT_FINE = "DELETE FROM studentFine WHERE isbn=?";

    // statements for book borrowing
    private final String GET_ALL_BORROWED_BOOKS = "SELECT * FROM STUDENTBOOK";
    private final String GET_BOOK_QUERY = "SELECT * FROM books WHERE isbn=?";
    private final String GET_USER_ID_BORROWED_BOOKS = "SELECT * FROM STUDENTBOOK WHERE ISBN=?";

    // new statements
    private final String UPDATE_BOOK_QUERY = "UPDATE books SET title=?, editionNumber=?, copyright=?, quantity=? WHERE isbn=?";
    private final String ADD_AUTHOR_QUERY = "INSERT INTO authors(firstName, lastName) VALUES(?, ?)";
    private final String DELETE_AUTHOR_QUERY = "DELETE FROM authors WHERE authorId=?";
    private final String GET_AUTHORS_WITH_ID = "SELECT * FROM authorISBN WHERE authorId=?";
    private final String GET_BOOK_AUTHORS = "SELECT authors.authorId,  firstName, lastName FROM authors INNER JOIN authorISBN ON authors.authorId = authorISBN.authorID where isbn=?";
    private final String GET_AUTHOR = "SELECT * FROM authors WHERE firstName=? AND lastName=?";
    private final String GET_AUTHOR_ID = "SELECT authorId FROM authors WHERE firstName=? AND lastName=?";
    private final String GET_STUDENT_FINE = "SELECT * FROM studentFine WHERE userId=?";
    private final String ADD_AUTHOR_ISBN = "INSERT INTO authorISBN(authorId, isbn) VALUES (?,?)";
    private final String GET_STUDENT_BOOKS = "SELECT books.isbn, title, editionNumber, copyright FROM books INNER JOIN studentBook ON books.isbn = studentBook.isbn WHERE userId=?";
    private final String GET_OVERDUE_BOOKS = "SELECT books.isbn, title, editionNumber, copyright FROM books INNER JOIN overdueBooks ON books.isbn = overdueBooks.isbn WHERE userId=?";
    private final String GET_BOOK_DATE = "SELECT * FROM studentBook WHERE userId=? AND isbn=?";

    // statements for book lending
    private final String ADD_BOOK_TO_STUDENTBOOK_QUERY = "INSERT INTO STUDENTBOOK(userid, isbn) VALUES (?,?)";
    private final String GET_BOOK_FROM_STUDENTBOOK = "SELECT USERID FROM STUDENTBOOK WHERE ISBN=?";

    private PreparedStatement getAllBooksStmt;
    private PreparedStatement getBookQuantityStmt;
    private PreparedStatement getBookAuthors;
    private PreparedStatement getAuthorWithId;

    private PreparedStatement getStudentFine;

    private PreparedStatement getBookQuery;
    private PreparedStatement getUserIDBorrowed;
    private PreparedStatement addBookToStudentBook;
    private PreparedStatement getBookFromStudentBook;

    private PreparedStatement addBookStmt;
    private PreparedStatement addAuthor;
    private PreparedStatement checkStmt;

    private PreparedStatement updateQuantityStmt;
    private PreparedStatement updateBookStmt;
    private PreparedStatement getAuthor;
    private PreparedStatement getBook;

    private PreparedStatement deleteFromBooks;
    private PreparedStatement deleteFromStudentBook;
    private PreparedStatement deleteFromOverdueBooks;
    private PreparedStatement deleteFromAuthorIsbn;
    private PreparedStatement deleteFromStudentFine;
    private PreparedStatement deleteAuthor;

    private PreparedStatement getAllBorrowedBooks;

    private static BooksRepository instance;


    private BooksRepository() {
        try {
            conn = DriverManager.getConnection(connectionURL);

            getAllBooksStmt = conn.prepareStatement(GET_ALL_BOOKS_QUERY);
            getBookQuantityStmt = conn.prepareStatement(GET_BOOK_QUANTITY_QUERY);
            getBookAuthors = conn.prepareStatement(GET_BOOK_AUTHORS);
            getAuthorWithId = conn.prepareStatement(GET_AUTHORS_WITH_ID);
            getAuthor = conn.prepareStatement(GET_AUTHOR);

            getStudentFine = conn.prepareStatement(GET_STUDENT_FINE);

            getBookQuery = conn.prepareStatement(GET_BOOK_QUERY);
            getUserIDBorrowed = conn.prepareStatement(GET_USER_ID_BORROWED_BOOKS);
            addBookToStudentBook = conn.prepareStatement(ADD_BOOK_TO_STUDENTBOOK_QUERY);
            getBookFromStudentBook = conn.prepareStatement(GET_BOOK_FROM_STUDENTBOOK);


            addBookStmt = conn.prepareStatement(ADD_BOOK_QUERY);
            addAuthor = conn.prepareStatement(ADD_AUTHOR_QUERY);
            checkStmt = conn.prepareStatement(CHECK_IF_EXISTS_QUERY);

            updateQuantityStmt = conn.prepareStatement(UPDATE_QUANTITY_QUERY);

            updateBookStmt = conn.prepareStatement(UPDATE_BOOK_QUERY);

            deleteFromBooks = conn.prepareStatement(DELETE_FROM_BOOKS_QUERY);
            deleteFromStudentBook = conn.prepareStatement(DELETE_FROM_STUDENT_BOOK_QUERY);
            deleteFromOverdueBooks = conn.prepareStatement(DELETE_FROM_OVERDUE_BOOK_QUERY);
            deleteFromAuthorIsbn = conn.prepareStatement(DELETE_FROM_AUTHOR_ISBN_QUERY);
            deleteFromStudentFine = conn.prepareStatement(DELETE_FROM_STUDENT_FINE);
            deleteAuthor = conn.prepareStatement(DELETE_AUTHOR_QUERY);


            // new
            getBook = conn.prepareStatement("SELECT * FROM books WHERE isbn=?");
            getAllBorrowedBooks = conn.prepareStatement(GET_ALL_BORROWED_BOOKS);
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

    public boolean returnBook(Student student, Book book) {
        try {
            PreparedStatement deleteFromOverdue = conn.prepareStatement("DELETE FROM overdueBooks WHERE userId=? AND isbn=?");
            PreparedStatement deleteFromStudentBook = conn.prepareStatement("DELETE FROM studentBook WHERE userId=? AND isbn=?");
            PreparedStatement deleteFromStudentFine = conn.prepareStatement("DELETE FROM studentFine WHERE userId=? AND isbn=?");

            getBook.setString(1, book.getIsbn());
            ResultSet rs = getBook.executeQuery();
            int quantity = 0;
            if (rs.next()) {
                quantity = rs.getInt("quantity");
                quantity++;
                updateQuantityStmt.setInt(1, quantity);
                updateQuantityStmt.setString(2, book.getIsbn());
                updateQuantityStmt.execute();

                deleteFromOverdue.setString(1, student.getUserId());
                deleteFromOverdue.setString(2, book.getIsbn());
                deleteFromOverdue.execute();

                deleteFromStudentBook.setString(1, student.getUserId());
                deleteFromStudentBook.setString(2, book.getIsbn());
                deleteFromStudentBook.execute();

                deleteFromStudentFine.setString(1, student.getUserId());
                deleteFromStudentFine.setString(2, book.getIsbn());
                deleteFromStudentFine.execute();

                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public boolean borrowBook(Student student, Book book) {
        try {
            PreparedStatement checkIfExists = conn.prepareStatement("SELECT * FROM studentBook WHERE userId=? AND isbn=?");
            checkIfExists.setString(1, student.getUserId());
            checkIfExists.setString(2, book.getIsbn());
            ResultSet rs0 = checkIfExists.executeQuery();
            if (!rs0.next()) {
                PreparedStatement addStudentBook = conn.prepareStatement("INSERT INTO studentBook (userId, isbn, borrowedDate, dueDate) VALUES (?, ?, ?, ?)");
                getBook.setString(1, book.getIsbn());
                ResultSet rs = getBook.executeQuery();
                int quantity = 0;
                if (rs.next()) {
                    quantity = rs.getInt("quantity");
                }
                if (quantity > 0) {
                    quantity--;
                    updateQuantityStmt.setInt(1, quantity);
                    updateQuantityStmt.setString(2, book.getIsbn());

                    addStudentBook.setString(1, student.getUserId());
                    addStudentBook.setString(2, book.getIsbn());
                    addStudentBook.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
                    addStudentBook.setDate(4, java.sql.Date.valueOf(LocalDate.now().plusDays(14)));

                    updateQuantityStmt.execute();
                    addStudentBook.execute();
                    return true;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ObservableList<Book> getStudentBooks(User user) {
        final String ADD_OVERDUE_BOOK = "INSERT INTO overdueBooks (userId, isbn, borrowedDate, expiredDate) VALUES (?, ?, ?, ?)";
        final String ADD_FINE_STUDENT = "INSERT INTO studentFine (userId, isbn, fine) VALUES (?, ?, ?)";
        final String UPDATE_FINE = "UPDATE studentFine SET fine=? WHERE userId=? AND isbn=?";
        try {
            PreparedStatement getStudentBooksStmt = conn.prepareStatement(GET_STUDENT_BOOKS);
            PreparedStatement getBookDates = conn.prepareStatement(GET_BOOK_DATE);
            PreparedStatement addOverdueBook = conn.prepareStatement(ADD_OVERDUE_BOOK);
            PreparedStatement addFineStudent = conn.prepareStatement(ADD_FINE_STUDENT);
            PreparedStatement updateFine = conn.prepareStatement(UPDATE_FINE);

            getStudentBooksStmt.setString(1, user.getUserId());

            ResultSet rs = getStudentBooksStmt.executeQuery();
            ObservableList<Book> books = FXCollections.observableArrayList();
            Book book = null;
            while (rs.next()) {
                book = new Book(rs.getString("isbn"), rs.getString("title"),
                        rs.getString("editionNumber"), rs.getString("copyright"), 1);
                getBookDates.setString(1, user.getUserId());
                getBookDates.setString(2, book.getIsbn());
                ResultSet rs1 = getBookDates.executeQuery();
                if (rs1.next()) {
                    LocalDate borrowedDate = rs1.getDate("borrowedDate").toLocalDate();
                    LocalDate dueDate = rs1.getDate("dueDate").toLocalDate();
                    book.setBorrowedDate(borrowedDate);
                    book.setDueDate(dueDate);
                    if (dueDate.isAfter(LocalDate.now())) {
                        long days = ChronoUnit.DAYS.between(borrowedDate, dueDate);
                        double fine = (double) days * 0.25;
                        if (days > 1) {
                            updateFine.setDouble(1, fine);
                            updateFine.setString(2, user.getUserId());
                            updateFine.setString(3, book.getIsbn());
                            updateFine.execute();
                        } else {
                            addOverdueBook.setString(1, user.getUserId());
                            addOverdueBook.setString(2, book.getIsbn());
                            addOverdueBook.setDate(3, java.sql.Date.valueOf(borrowedDate));
                            addOverdueBook.setDate(4, java.sql.Date.valueOf(dueDate));
                            addOverdueBook.execute();

                            addFineStudent.setString(1, user.getUserId());
                            addFineStudent.setString(2, book.getIsbn());
                            addFineStudent.setDouble(3, fine);
                            addFineStudent.execute();
                        }

                    }
                }
                books.add(book);
            }
            return books;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ObservableList<Fine> getStudentFines(User student) {
        try {
            PreparedStatement getStudentFines = conn.prepareStatement("SELECT * FROM studentFine WHERE userId=?");
            getStudentFines.setString(1, student.getUserId());
            ObservableList<Fine> fines = FXCollections.observableArrayList();
            Fine fine = null;
            ResultSet rs = getStudentFines.executeQuery();
            while(rs.next()) {
                fine = new Fine(rs.getString("isbn"), rs.getDouble("fine"));
                fines.add(fine);
            }
            return fines;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    // returns all books
    public ObservableList<Book> getAllBooks() {
        ResultSet rs;
        ObservableList<Book> list = FXCollections.observableArrayList();

        try {
            rs = getAllBooksStmt.executeQuery();
            // isbn, title, editionNumber, copyright, quantity
            Book book = null;
            while (rs.next()) {
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

    // returns authors of a book
    public ObservableList<Author> getAuthors(Book book) {
        try {
            getBookAuthors.setString(1, book.getIsbn());
            ResultSet rs1 = getBookAuthors.executeQuery();
            ObservableList<Author> authors = FXCollections.observableArrayList();
            Author author = null;
            while (rs1.next()) {
                author = new Author(rs1.getString("authorId"), rs1.getString("firstName"), rs1.getString("lastName"));
                authors.add(author);
            }
            return authors;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    // deletes a book from the books table and all other relation tables where this book's isbn is a foreign key
    public void deleteBook(Book book) {
        try {
            deleteFromStudentBook.setString(1, book.getIsbn());
            deleteFromOverdueBooks.setString(1, book.getIsbn());
            deleteFromAuthorIsbn.setString(1, book.getIsbn());
            deleteFromBooks.setString(1, book.getIsbn());
            deleteFromStudentFine.setString(1, book.getIsbn());

            List<Author> authors = getAuthors(book);

            List<Author> authorsToDelete = new ArrayList<>();

            ResultSet rs = null;
            for (Author author: authors) {
                getAuthorWithId.setString(1, author.getAuthorId());
                // there must be only one author with such id if we want to delete it from authors table as well
                rs = getAuthorWithId.executeQuery();
                rs.next();
                if (!rs.next()) {
                    authorsToDelete.add(author);
                }
            }
            deleteFromAuthorIsbn.executeUpdate();
            // 1. we get authors to delete from authorISBN table
            // 2. we delete authors from authorISBN table
            // 3. we delete authors from authors table
            for (Author author : authorsToDelete) {
                deleteAuthor.setString(1, author.getAuthorId());
                deleteAuthor.execute();
            }
            deleteFromStudentFine.executeUpdate();
            deleteFromOverdueBooks.executeUpdate();
            deleteFromStudentBook.executeUpdate();
            deleteFromBooks.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
    // inserts information about book and its authors into authorISBN table
    public void addAuthorIsbn(Book book, List<Author> authors) {
        try {
            PreparedStatement addAuthorIsbn = conn.prepareStatement(ADD_AUTHOR_ISBN);
            for (Author author : authors) {
                addAuthorIsbn.setString(1, author.getAuthorId());
                addAuthorIsbn.setString(2, book.getIsbn());
                addAuthorIsbn.execute();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            System.out.println("Could NOT insert into 'authorISBN' table");
        }
    }

    // adds authors to 'authors' table
    public void addAuthors(List<Author> authors) {
        ResultSet rs1 = null;
        try {
            for (Author author : authors) {
                getAuthor.setString(1, author.getFirstName());
                getAuthor.setString(2, author.getLastName());
                rs1 = getAuthor.executeQuery();
                if (!rs1.next()) {
                    addAuthor.setString(1, author.getFirstName());
                    addAuthor.setString(2, author.getLastName());
                    addAuthor.execute();
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    // updates information of a book with provided isbn
    public void updateBook(Book book) throws SQLException {
    updateBookStmt.setString(1, book.getTitle());
    updateBookStmt.setString(2, book.getEdition());
    updateBookStmt.setString(3, book.getCopyright());
    updateBookStmt.setInt(4, book.getQuantity());
    updateBookStmt.setString(5, book.getIsbn());

    updateBookStmt.executeUpdate();
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
                bookQuantity += book.getQuantity();
                updateQuantityStmt.setInt(1, bookQuantity);
                updateQuantityStmt.setString(2, book.getIsbn());
                updateQuantityStmt.executeUpdate();
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

    public double getTotalFine(User user) {
        List<Fine> fines = getFine(user);
        double sum = 0;
        for (Fine fine: fines)
            sum += fine.getFineAmount();
        return sum;
    }
    public ObservableList<Fine> getFine(User user) {
        try {
            ObservableList<Fine> fines = FXCollections.observableArrayList();
            getStudentFine.setString(1, user.getUserId());
            ResultSet rs = getStudentFine.executeQuery();
            Fine fine = null;
            while(rs.next()) {
                fine = new Fine(rs.getString("isbn"), rs.getDouble("fine"));
                fines.add(fine);
            }
            return fines;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ObservableList<Book> getOverdueBooks(User student) {
        try {
            PreparedStatement getOverdueBooksStmt = conn.prepareStatement(GET_OVERDUE_BOOKS);
            PreparedStatement getBookDates = conn.prepareStatement(GET_BOOK_DATE);
            PreparedStatement getBookFine = conn.prepareStatement("SELECT * FROM studentFine WHERE userId=? AND isbn=?");
            getOverdueBooksStmt.setString(1, student.getUserId());

            ObservableList<Book> books = FXCollections.observableArrayList();
            Book book = null;

            ResultSet rs = getOverdueBooksStmt.executeQuery();
            while (rs.next()) {
                book = new Book(rs.getString("isbn"), rs.getString("title"),
                        rs.getString("editionNumber"), rs.getString("copyright"), 1);
                getBookDates.setString(1, student.getUserId());
                getBookDates.setString(2, book.getIsbn());
                ResultSet rs1 = getBookDates.executeQuery();
                if (rs1.next()) {
                    book.setBorrowedDate(rs1.getDate("borrowedDate").toLocalDate());
                    book.setDueDate(rs1.getDate("dueDate").toLocalDate());
                }
                getBookFine.setString(1, student.getUserId());
                getBookFine.setString(2, book.getIsbn());
                ResultSet rs2 = getBookFine.executeQuery();
                if (rs2.next()) {
                    book.setFineAmount(String.format("%.2f", rs2.getDouble("fine")));
                }
                books.add(book);
            }
            return books;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public String getAuthorId(Author author) {
        try {
            PreparedStatement statement = conn.prepareStatement(GET_AUTHOR_ID);
            statement.setString(1, author.getFirstName());
            statement.setString(2, author.getLastName());

            ResultSet rs = statement.executeQuery();
            if (rs.next())
                return rs.getString("authorId");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return "";
    }
  
    public ObservableList<Book> searchBooks(String select, String newValue) {
        System.out.println(select + " - " + newValue);
        ObservableList<Book> books = FXCollections.observableArrayList();

        String selectBooks = "SELECT * FROM books WHERE " + select.toLowerCase() + " LIKE '%" + newValue + "%'";

        if (newValue.length() != 0) {
            String getAuthors = "SELECT * FROM authors WHERE firstName LIKE '%" + newValue + "%' OR lastName LIKE '%" + newValue + "%'";
            String getAuthorBooks = "SELECT * FROM books INNER JOIN authorISBN ON books.isbn = authorISBN.isbn WHERE authorId=?";
            try {
                Statement getAuthorsStmt = conn.createStatement();
                PreparedStatement getAuthorBooksStmt = conn.prepareStatement(getAuthorBooks);

                if (select.equals("Author")) {
                    ResultSet rs = getAuthorsStmt.executeQuery(getAuthors);
                    while (rs.next()) {
                        // get each author's books;
                        String authorId = String.valueOf(rs.getInt("authorId"));
                        getAuthorBooksStmt.setString(1, authorId);
                        ResultSet rs1 = getAuthorBooksStmt.executeQuery();
                        while(rs1.next()) {
                            Book book = new Book(rs1.getString("isbn"), rs1.getString("title"),
                                    rs1.getString("editionNumber"), rs1.getString("copyright"), rs1.getInt("quantity"));
                            books.add(book);
                        }
                    }
                } else {
                    PreparedStatement selectBooksStmt = conn.prepareStatement(selectBooks);
                    ResultSet rs1 = selectBooksStmt.executeQuery();
                    while (rs1.next()) {
                        Book book = new Book(rs1.getString("isbn"), rs1.getString("title"),
                                rs1.getString("editionNumber"), rs1.getString("copyright"), rs1.getInt("quantity"));
                        books.add(book);
                    }
                }
                return books;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return null;
    }
    // return a list of borrowed books
    public ObservableList<BorrowedBook> getBorrowedBooks(){
        ResultSet resultSet = null;
        ObservableList<BorrowedBook> list = FXCollections.observableArrayList();

        try {
            resultSet = getAllBorrowedBooks.executeQuery();
            ArrayList<String> isbns = new ArrayList<String >();
            while (resultSet.next()){
                BorrowedBook book = null;
                getBookQuery.setString(1, resultSet.getString("isbn"));
                ResultSet temp = getBookQuery.executeQuery();
                if (temp.next()){
                    book = new BorrowedBook(temp.getString("isbn"), temp.getString("title"), 1);
                }
                if (!isbns.contains(book.getIsbn())){
                    isbns.add(temp.getString("isbn"));
                    book.resetAmount();
                    list.add(book);
                }else{
                    for (BorrowedBook bookT : list){
                        if (bookT.getIsbn().equals(book.getIsbn())){
                            bookT.incrementAmount();
                        }
                    }
//                    list.get(list.indexOf(book)).incrementAmount();
//                    amount += 1;
                }
            }
            return list;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public ObservableList<StudentsBorrowed> getStudentsWhoBorrowed(String isbn){
        ObservableList<StudentsBorrowed> students = FXCollections.observableArrayList();
        try {
            getUserIDBorrowed.setString(1, isbn);
            ResultSet results = getUserIDBorrowed.executeQuery();
            while (results.next()){
                String userID = results.getString("userID");
                StudentsBorrowed user = UsersRepository.getInstance().getUser(userID);
                students.add(user);
            }
            return students;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    // functions to get quantity and decrement
    public String getBookQuantity(String isbn) throws SQLException {
        getBookQuantityStmt.setString(1, isbn);
        ResultSet result = getBookQuantityStmt.executeQuery();
        if (result.next()){
            String quantity = result.getString("quantity");
            return quantity;
        }
        return null;
    }

    public void decrementQuantity(String isbn) throws SQLException {
        getBookQuantityStmt.setString(1, isbn);
        getBookQuantityStmt.setString(1, isbn);
        ResultSet result = getBookQuantityStmt.executeQuery();
        if (result.next()){
            String quantity = result.getString("quantity");
            int temp = Integer.parseInt(quantity) - 1;
            updateQuantityStmt.setString(1, String.format("%d", temp));
            updateQuantityStmt.setString(2, isbn);
            updateQuantityStmt.executeUpdate();
        }
    }

    public void addBookToStudentBook(String studentID, String isbn) throws SQLException {
        addBookToStudentBook.setString(1, studentID);
        addBookToStudentBook.setString(2, isbn);
        addBookToStudentBook.executeUpdate();
    }

    public boolean checkIfStudentBorrowed(String isbn, String studentID) throws SQLException {
        getBookFromStudentBook.setString(1, isbn);
        ResultSet result = getBookFromStudentBook.executeQuery();
        while (result.next()){
            if (result.getString("USERID").equals(studentID)){
                return true;
            }
            else {
                return false;
            }
        }
        return false;
    }
}
