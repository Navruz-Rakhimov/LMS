package user;

public class Administrator extends User {

    public Administrator(String username, String password, String firstName, String lastName, String email) {
        super(username, password, firstName, lastName, email, 0);
    }

}
