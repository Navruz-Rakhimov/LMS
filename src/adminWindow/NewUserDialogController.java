package adminWindow;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import user.Librarian;
import user.Student;
import user.User;

public class NewUserDialogController {
    @FXML
    public TextField username;
    @FXML
    public TextField password;
    @FXML
    public TextField firstName;
    @FXML
    public TextField lastName;
    @FXML
    public TextField email;

    public User getUser(int role) {
        User user = null;
        switch (role) {
            case 1:
                user = new Librarian(username.getText(), password.getText(),
                        firstName.getText(), lastName.getText(), email.getText());
                break;
            case 2:
                user = new Student(username.getText(), password.getText(),
                        firstName.getText(), lastName.getText(), email.getText());
                break;
        }
        return user;
    }

    public void setUser(User user) {
        username.setText(user.getUsername());
        password.setText(user.getPassword());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        email.setText(user.getEmail());
    }
}
