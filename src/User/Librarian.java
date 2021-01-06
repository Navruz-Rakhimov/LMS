package User;

public class Librarian extends User {
    /*public Librarian(String firstName, String lastName, String email) {
        super(firstName, lastName, email, 1);
    }*/

    public Librarian(String firstName, String lastName, String email) {
        super(new UserBuilder(firstName, lastName).email(email).role(1));
    }
}
