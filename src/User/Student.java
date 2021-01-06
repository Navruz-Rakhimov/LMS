package User;

public class Student extends User {

    /*public Student(String firstName, String lastName, String email) {
        super(firstName, lastName, email, 2);
    }*/

    public Student(String firstName, String lastName, String email){
        super(new UserBuilder(firstName, lastName).email(email).role(2));
    }
}
