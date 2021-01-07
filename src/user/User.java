package user;

public class User {
    protected String userId;
    protected String email;
    protected String password;
    protected String firstName;
    protected String lastName;
    protected int role; // 0, 1, 2 for admin, librarian, student respectively

    public User() {}

    public User(String userId, String email, String password, String firstName, String lastName, int role) {
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
    }

    public User(String email, String password, int role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }
}
