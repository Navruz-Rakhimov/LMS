package authentication;

import user.Librarian;
import user.Student;
import user.User;
import book.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UsersRepository {

    String dbName = "main";
    String connectionURL = "jdbc:derby:./db/" + dbName;
    Connection conn = null;

    private final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private final String GET_ALL_STUDENTS_QUERY = "SELECT * FROM users WHERE role=2";
    private final String GET_ALL_LIBRARIANS_QUERY = "SELECT * FROM users WHERE role=1";
    private final String GET_ALL_ADMINS_QUERY = "SELECT * FROM users WHERE role=0";
    private final String GET_USER_QUERY = "SELECT * FROM users where username=?";
    private final String ADD_USER_QUERY = "INSERT INTO users(username, password, firstName, lastName, email, role) VALUES (?,?,?,?,?,?)";

    private final String DELETE_USER_QUERY = "DELETE FROM users WHERE username=?";
    private final String DELETE_FROM_BOOKS_QUERY = "DELETE FROM books WHERE isbn=?";
    private final String DELETE_FROM_STUDENT_BOOK_QUERY = "DELETE FROM studentBook WHERE isbn=?";
    private final String DELETE_FROM_OVERDUE_BOOK_QUERY = "DELETE FROM overdueBooks WHERE isbn=?";
    private final String DELETE_FROM_AUTHOR_ISBN_QUERY = "DELETE FROM authorISBN WHERE isbn=?";

    private final String GET_ROLE_QUERY = "SELECT role FROM Users where username=?";

    private PreparedStatement getAllUsersStmt;
    private PreparedStatement getAllStudentsStmt;
    private PreparedStatement getAllLibrariansStmt;
    private PreparedStatement getAllAdminsStmt;
    private PreparedStatement getUserStmt;
    private PreparedStatement addUserStmt;
    private PreparedStatement getRoleStmt;
    private PreparedStatement deleteUserStmt;

    private static UsersRepository instance;

    private UsersRepository() {
        try {
            conn = DriverManager.getConnection(connectionURL);

            getAllUsersStmt = conn.prepareStatement(GET_ALL_USERS_QUERY);
            getAllStudentsStmt = conn.prepareStatement(GET_ALL_STUDENTS_QUERY);
            getAllLibrariansStmt = conn.prepareStatement(GET_ALL_LIBRARIANS_QUERY);
            getAllAdminsStmt = conn.prepareStatement(GET_ALL_ADMINS_QUERY);
            getUserStmt = conn.prepareStatement(GET_USER_QUERY);
            addUserStmt = conn.prepareStatement(ADD_USER_QUERY);
            getRoleStmt = conn.prepareStatement(GET_ROLE_QUERY);
            deleteUserStmt = conn.prepareStatement(DELETE_USER_QUERY);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public static UsersRepository getInstance() {
        if (instance == null) {
            instance = new UsersRepository();
        }
        return instance;
    }

    public boolean addUser(User user) {
        try {
            addUserStmt.setString(1, user.getUsername());
            addUserStmt.setString(2, user.getPassword());
            addUserStmt.setString(3, user.getFirstName());
            addUserStmt.setString(4, user.getLastName());
            addUserStmt.setString(5, user.getEmail());
            addUserStmt.setInt(6, user.getRole());

            if (addUserStmt.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }

    public boolean verifyUser(User user) {
        try {
            getUserStmt.setString(1, user.getUsername());

            if (getUserStmt.execute()) {
                ResultSet rs = getUserStmt.getResultSet();
                if (rs.next()) {
                    System.out.println("Username is correct");
                    if (rs.getString("password").equals(user.getPassword())) {
                        System.out.println("Password is correct");
                        return true;
                    }
                    System.out.println("Password is incorrect!");
                }
                else {
                    System.out.println("Username is incorrect");
                    return false;
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public int getUserRole(User user) {
        ResultSet rs = null;
        try {
            getRoleStmt.setString(1, user.getUsername());
            rs = getRoleStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("role");
            }
            return -1;

        } catch (SQLException e) {
            return -1;
        }
    }

    public ObservableList<User> getAllStudents() {
        ObservableList<User> students = FXCollections.observableArrayList();

        try {
            ResultSet rs = getAllStudentsStmt.executeQuery();

            while (rs.next()) {
                students.add(new Student(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"))
                );
            }
            return students;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public void deleteUser(User user) {
        try {
            deleteUserStmt.setString(1, user.getUsername());
            PreparedStatement statement1 = conn.prepareStatement("DELETE FROM overdueBooks WHERE username='" + user.getUsername()+"'");
            PreparedStatement statement2 = conn.prepareStatement("DELETE FROM studentBook WHERE username='" + user.getUsername()+"'");
            if (user.getRole() == 1) {
                deleteUserStmt.execute();
            } else {
                statement1.executeUpdate();
                statement2.executeUpdate();
                deleteUserStmt.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public ObservableList<User> getAllLibrarians() {
        ObservableList<User> librarians = FXCollections.observableArrayList();

        try {
            ResultSet rs = getAllLibrariansStmt.executeQuery();

            while (rs.next()) {
                librarians.add(new Librarian(
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName"),
                        rs.getString("email"))
                );
            }
            return librarians;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }
}
