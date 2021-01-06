package User;

public class User {
    private String username;
    private String password;
    private String email;
    private int role; // 0, 1, 2 for admin, librarian, student respectively

    public User() {}

    /*public User(String username, String password, String email, int role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public User(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.email = "";
    }*/

    // Builder Pattern feature
    public User(UserBuilder userBuilder){
        this.username = userBuilder.username;
        this.password = userBuilder.password;
        this.email = userBuilder.email;
        this.role = userBuilder.role;
    }

    public static class UserBuilder{
        private String username;
        private String password;
        private String email;
        private int role;

        // mandatory entries
        public UserBuilder(String username, String password){
            this.username = username;
            this.password = password;
        }
        // optional entries
        public UserBuilder email(String email){
            this.email = email;
            return this;
        }
        public UserBuilder role(int role){
            this.role = role;
            return this;
        }

        public User build(){
            User user = new User(this);
            objectValidation(user);
            return user;
        }
        private void objectValidation(User user){
            if(user.email == null){
                user.email = "";
            }
        }
    }


    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
}
