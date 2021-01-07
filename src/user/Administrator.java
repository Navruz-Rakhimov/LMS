package user;

public class Administrator extends User {

    public Administrator(String userId, String email, String password, String firstName, String lastName) {
        super(userId, email, password, firstName, lastName, 0);
    }

}
