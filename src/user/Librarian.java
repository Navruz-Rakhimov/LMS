package user;

public class Librarian extends User {

    public Librarian(String userId, String email, String password, String firstName, String lastName) {
        super(userId, email, password, firstName, lastName, 1);
    }
}
