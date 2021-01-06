package User;

public class Administrator extends User {
    /*public Administrator(String firstName, String lastName, String email) {
        super(firstName, lastName, email, 0);
    }*/

    public Administrator(String firstName, String lastName, String email){
        super(new UserBuilder(firstName, lastName).email(email).role(0));
    }
}
