package user;

public class Student extends User {

    private boolean isBlocked;

    public Student(String username, String password, String firstName, String lastName, String email) {
        super(username, password, firstName, lastName, email, 2);
        isBlocked = false;
    }

}
