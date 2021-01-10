package librarianWindow;

import user.User;

public class Librarian extends User {
    public Librarian(String username, String password, String firstName, String lastName, String email) {
        super(username, password, firstName, lastName, email, 1);
    }
}

