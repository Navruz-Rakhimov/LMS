package authentication;

import User.User;

import java.sql.*;

public class UsersRepository {
    String dbName = "users";
    String connectionURL = "jdbc:derby:./db/" + dbName;
    Connection conn = null;
    Statement s;

    private final String GET_ALL_QUERY = "SELECT * FROM Users";
    private final String GET_QUERY = "SELECT * FROM Users where username=?";
    private final String ADD_QUERY = "INSERT INTO Users(Username,Password,Email) VALUES (?,?,?)";
    private final String DELETE_QUERY = "DELETE FROM Users WHERE username=?";
    private final String GET_LAST_ID = "SELECT MAX(UserId) FROM Users";
    private final String GET_ROLE_QUERY = "SELECT Role FROM Users where username=?";

    private PreparedStatement getStmt;
    private PreparedStatement getAllStmt;
    private PreparedStatement addStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement getLastIdStmt;
    private PreparedStatement getRoleStmt;

    private static UsersRepository instance;

    private UsersRepository() {}

    public static UsersRepository getInstance() {
        if (instance == null) {
            instance = new UsersRepository();
        }
        return instance;
    }

    public boolean addUser(User user) {

        try {
            conn = DriverManager.getConnection(connectionURL);
            addStmt = conn.prepareStatement(ADD_QUERY);

            addStmt.setString(1, user.getUsername());
            addStmt.setString(2, user.getPassword());
            addStmt.setString(3, user.getEmail());

            if (addStmt.executeUpdate() > 0) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException e) {
            return false;
        }
    }

    public boolean verifyUser(User user) {
        try {
            conn = DriverManager.getConnection(connectionURL);

            getStmt = conn.prepareStatement(GET_QUERY);
            getStmt.setString(1, user.getUsername());

            if (getStmt.execute()) {
                ResultSet rs = getStmt.getResultSet();
                if (rs.next()) {
                    System.out.println("Username is correct");
                    if (rs.getString("Password").equals(user.getPassword())) {
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
            conn = DriverManager.getConnection(connectionURL);
            getRoleStmt = conn.prepareStatement(GET_ROLE_QUERY);

            getRoleStmt.setString(1, user.getUsername());

            rs = getRoleStmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("Role");
            }
            return -1;

        } catch (SQLException e) {
            return -1;
        } finally {
            try { rs.close(); } catch (Exception e) {}
            try { getRoleStmt.close(); } catch (Exception e) {}
            try { conn.close(); } catch (Exception e) {}
        }
    }

}
