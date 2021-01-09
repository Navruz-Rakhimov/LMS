package user;

public class Student extends User {
    private boolean isBlocked;

    public Student(String userId, String email, String password, String firstName, String lastName) {
        super(userId, email, password, firstName, lastName , 2);
        isBlocked = false;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }
}
