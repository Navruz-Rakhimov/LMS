package authentication;

import librarianWindow.StudentsBorrowed;
import user.Librarian;
import user.Student;
import user.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;

public class UsersRepository {

    String dbName = "database";
    String connectionURL = "jdbc:derby:./db/" + dbName;
    Connection conn = null;

    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public void setCurrentUserEmail(String currentUserEmail) {
        this.currentUserEmail = currentUserEmail;
    }

    private String currentUserEmail;

    private final String GET_ALL_USERS_QUERY = "SELECT * FROM users";
    private final String GET_ALL_STUDENTS_QUERY = "SELECT * FROM users WHERE role=2";
    private final String GET_ALL_LIBRARIANS_QUERY = "SELECT * FROM users WHERE role=1";

    // get user using email (second primary key in table 'users')
    private final String GET_USER_QUERY_EMAIL = "SELECT * FROM users where email=?";
    private final String GET_USER_ID = "SELECT userId FROM users WHERE email=?";
    private final String GET_BLOCKED = "SELECT * FROM blockedStudents WHERE userId=?";
    private final String GET_ALL_BLOCKED_STUDENTS = "SELECT * FROM blockedStudents";
    private final String BLOCK_STUDENT_ID = "INSERT INTO BLOCKEDSTUDENTS(USERID) VALUES (?)";
    private final String DELETE_FROM_BLOCKED = "DELETE FROM BLOCKEDSTUDENTS WHERE USERID=?";

    // get user using userId
    private final String GET_USER_QUERY_ID = "SELECT * FROM users WHERE userId=?";

    private final String ADD_USER_QUERY = "INSERT INTO users(email, password, firstName, lastName, role) VALUES (?,?,?,?,?)";

    // you can delete user only with userId (because it is a foreign key in other tables)
    private final String DELETE_USER_QUERY = "DELETE FROM users WHERE userId=?";
    /*
    private final String DELETE_FROM_BOOKS_QUERY = "DELETE FROM books WHERE isbn=?";
    private final String DELETE_FROM_STUDENT_BOOK_QUERY = "DELETE FROM studentBook WHERE isbn=?";
    private final String DELETE_FROM_OVERDUE_BOOK_QUERY = "DELETE FROM overdueBooks WHERE isbn=?";
    private final String DELETE_FROM_AUTHOR_ISBN_QUERY = "DELETE FROM authorISBN WHERE isbn=?";
    */

    // get role to display the right page for the right user
    private final String GET_ROLE_QUERY = "SELECT role FROM users where email=?";
    private final String GET_LAST_ID = "SELECT MAX(userId) FROM users";

    private PreparedStatement getAllUsersStmt;
    private PreparedStatement getAllStudentsStmt;
    private PreparedStatement getAllLibrariansStmt;
    private PreparedStatement getAllBlocked;
    private PreparedStatement blockStudent;
    private PreparedStatement removeFromBlocked;

    private PreparedStatement getUserWithEmailStmt;
    private PreparedStatement getUserWithIdStmt;
    private PreparedStatement getUserIdStmt;
    private PreparedStatement getBlocked;

    private PreparedStatement addUserStmt;
    private PreparedStatement getRoleStmt;
    private PreparedStatement deleteUserStmt;
    private PreparedStatement getLastIdStmt;

    private static UsersRepository instance;

    private UsersRepository() {
        try {
            conn = DriverManager.getConnection(connectionURL);

            getAllBlocked = conn.prepareStatement(GET_ALL_BLOCKED_STUDENTS);
            getAllUsersStmt = conn.prepareStatement(GET_ALL_USERS_QUERY);
            getAllStudentsStmt = conn.prepareStatement(GET_ALL_STUDENTS_QUERY);
            getAllLibrariansStmt = conn.prepareStatement(GET_ALL_LIBRARIANS_QUERY);
            getUserWithEmailStmt = conn.prepareStatement(GET_USER_QUERY_EMAIL);
            getUserWithIdStmt = conn.prepareStatement(GET_USER_QUERY_ID);
            blockStudent = conn.prepareStatement(BLOCK_STUDENT_ID);
            removeFromBlocked = conn.prepareStatement(DELETE_FROM_BLOCKED);

            addUserStmt = conn.prepareStatement(ADD_USER_QUERY);
            getRoleStmt = conn.prepareStatement(GET_ROLE_QUERY);
            deleteUserStmt = conn.prepareStatement(DELETE_USER_QUERY);
            getLastIdStmt = conn.prepareStatement(GET_LAST_ID);
            getUserIdStmt = conn.prepareCall(GET_USER_ID);
            getBlocked = conn.prepareStatement(GET_BLOCKED);

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

    public boolean isBlocked(User user) {
        try {
            getBlocked.setString(1, user.getUserId());
            ResultSet rs = getBlocked.executeQuery();
            if (rs.next()) {
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public ObservableList<User> getAllBlockedStudents() throws SQLException {
        ObservableList<User> blocked = FXCollections.observableArrayList();
        ResultSet result = getAllBlocked.executeQuery();
        while (result.next()){
            getUserWithIdStmt.setString(1, result.getString("userId"));
            ResultSet temp = getUserWithIdStmt.executeQuery();
            if (temp.next()) {
                User user = new User(temp.getString("email"), temp.getString("firstName"), temp.getString("lastName"));
                blocked.add(user);
            }
        }
        return blocked;
    }

    public Student getStudent(String email) {
        try {
            getUserWithEmailStmt.setString(1, email);
            ResultSet rs = getUserWithEmailStmt.executeQuery();
            if (rs.next()) {
                Student student = new Student(rs.getString("userId"), rs.getString("email"), rs.getString("password"),
                        rs.getString("firstName"), rs.getString("lastName"));
                student.setBlocked(isBlocked(student));
                return student;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public Librarian getLibrarian(String email) {
        try {
            getUserWithEmailStmt.setString(1, email);
            ResultSet rs = getUserWithEmailStmt.executeQuery();
            if (rs.next()) {
                return new Librarian(rs.getString("userId"), rs.getString("email"), rs.getString("password"),
                        rs.getString("firstName"), rs.getString("lastName"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public boolean addUser(User user) {
        try {
            addUserStmt.setString(1, user.getEmail());
            addUserStmt.setString(2, user.getPassword());
            addUserStmt.setString(3, user.getFirstName());
            addUserStmt.setString(4, user.getLastName());
            addUserStmt.setInt(5, user.getRole());

            // to make sure there is no one with such email

            if (isUnique(user.getEmail())) {
                if (addUserStmt.executeUpdate() > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }

        return false;
    }

    public StudentsBorrowed getUser(String id) throws SQLException {
        getUserWithIdStmt.setInt(1, Integer.parseInt(id));
        ResultSet result = getUserWithIdStmt.executeQuery();
        if (result.next()){
            StudentsBorrowed user = new StudentsBorrowed(
                    result.getString("email"),
                    result.getString("firstName"),
                    result.getString("lastName")
            );
            return user;
        }
        return null;
    }

    public boolean verifyUser(User user) {
        try {
            // we use email and password for authorization
            getUserWithEmailStmt.setString(1, user.getEmail());

            if (getUserWithEmailStmt.execute()) {
                ResultSet rs = getUserWithEmailStmt.getResultSet();
                if (rs.next()) {
                    System.out.println("Email is correct");
                    if (rs.getString("password").equals(user.getPassword())) {
                        System.out.println("Password is correct");
                        return true;
                    }
                    System.out.println("Password is incorrect!");
                }
                else {
                    System.out.println("Email is incorrect");
                    return false;
                }
            }
        } catch (SQLException e) {
            return false;
        }
        return false;
    }

    public String getUserId(User user) {
        try {
            getUserIdStmt.setString(1, user.getEmail());
            ResultSet rs = getUserIdStmt.executeQuery();
            if(rs.next()) {
                return rs.getString("userId");
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }

    public int getUserRole(User user) {
        ResultSet rs = null;
        try {
            getRoleStmt.setString(1, user.getEmail());
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
                        rs.getString("userId"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                        )
                );
            }
            return students;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public ObservableList<User> getAllLibrarians() {
        ObservableList<User> librarians = FXCollections.observableArrayList();
        try {
            ResultSet rs = getAllLibrariansStmt.executeQuery();
            while (rs.next()) {
                librarians.add(new Librarian(
                        rs.getString("userId"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getString("firstName"),
                        rs.getString("lastName")
                        )
                );
            }
            return librarians;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }
    }

    public boolean updateUser(User oldUser, User newUser) {
        try {
            PreparedStatement updateStmt = conn.prepareStatement("UPDATE users  SET email=?, password=?, " +
                    "firstName=?, lastName=? WHERE userId=?");
            updateStmt.setString(1, newUser.getEmail());
            updateStmt.setString(2, newUser.getPassword());
            updateStmt.setString(3, newUser.getFirstName());
            updateStmt.setString(4, newUser.getLastName());
            updateStmt.setString(5, oldUser.getUserId());

            if (isUnique(newUser.getEmail()) || newUser.getEmail().equals(oldUser.getEmail())) {
                updateStmt.execute();
                return true;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void deleteUser(User user) {
        try {
            deleteUserStmt.setString(1, user.getUserId());
            PreparedStatement statement1 = conn.prepareStatement("DELETE FROM overdueBooks WHERE userId=" + user.getUserId());
            PreparedStatement statement2 = conn.prepareStatement("DELETE FROM studentBook WHERE userId=" + user.getUserId());
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

    private boolean isUnique(String email) {
        try {
            getUserWithEmailStmt.setString(1, email);
            ResultSet rs = getUserWithEmailStmt.executeQuery();
            return !rs.next();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }

    public void blockStudentID(User user) throws SQLException {
        getBlocked.setString(1, user.getUserId());
        ResultSet check = getBlocked.executeQuery();
        if (!check.next()){
            blockStudent.setString(1, user.getUserId());
            blockStudent.executeUpdate();
        }
    }

    public void removeFromBlockedSt(User user) throws SQLException {
        getUserWithEmailStmt.setString(1, user.getEmail());
        ResultSet result = getUserWithEmailStmt.executeQuery();
        if (result.next()) {
            String ID = result.getString("userId");
            removeFromBlocked.setString(1, ID);
            removeFromBlocked.executeUpdate();
        }
    }

}
