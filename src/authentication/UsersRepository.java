package authentication;

import User.User;

import java.sql.*;

public class UsersRepository {
//    String dbName = "database";
    String connectionURL = "jdbc:derby:./db/database";
    Connection conn = null;
    Statement s;

    private final String GET_ALL_QUERY = "SELECT * FROM Users";
    private final String GET_QUERY = "SELECT * FROM Users WHERE email=?";
    private final String ADD_QUERY = "INSERT INTO Users(Username,Password,Email,Role) VALUES (?,?,?,?)";
    private final String DELETE_QUERY = "DELETE FROM Users WHERE email=?";
    private final String GET_LAST_ID = "SELECT MAX(UserId) FROM Users";
    private final String GET_ROLE_QUERY = "SELECT Role FROM Users where username=?";

    private PreparedStatement getStmt;
    private PreparedStatement getAllStmt;
    private PreparedStatement addStmt;
    private PreparedStatement deleteStmt;
    private PreparedStatement getLastIdStmt;
    private PreparedStatement getRoleStmt;

    private static UsersRepository instance;

    UsersRepository() throws SQLException {
        this.conn = DriverManager.getConnection(connectionURL);
        this.getStmt = conn.prepareStatement(GET_QUERY);
        this.getAllStmt = conn.prepareStatement(GET_ALL_QUERY);
        this.getRoleStmt = conn.prepareStatement(GET_ROLE_QUERY);
        this.getLastIdStmt = conn.prepareStatement(GET_LAST_ID);
        this.deleteStmt = conn.prepareStatement(DELETE_QUERY);
        this.addStmt = conn.prepareStatement(ADD_QUERY);
    }

    public static UsersRepository getInstance() throws SQLException {
        if (instance == null) {
            instance = new UsersRepository();
        }
        return instance;
    }

    public boolean addUser(User user) {
        try {
            addStmt.setString(1, user.getUsername());
            addStmt.setString(2, user.getPassword());
            addStmt.setString(3, user.getEmail());
            addStmt.setInt(4, user.getRole());

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
            getStmt.setString(1, user.getEmail());

            if (getStmt.execute()) {
                ResultSet rs = getStmt.getResultSet();
                if (rs.next()) {
                    System.out.println("Email is correct");
                    if (rs.getString("Password").equals(user.getPassword())) {
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
