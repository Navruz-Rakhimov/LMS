package adminWindow;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import user.Librarian;
import user.Student;
import user.User;

public class NewUserDialogController {

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
        if (!email.getText().equals("") && !password.getText().equals("") &&
                !firstName.getText().equals("") && !lastName.getText().equals("")) {
            switch (role) {
                case 1:
                    user = new Librarian("", email.getText(), password.getText(),
                            firstName.getText(), lastName.getText());
                    break;
                case 2:
                    user = new Student("", email.getText(), password.getText(),
                            firstName.getText(), lastName.getText());
                    break;
            }
        }
        return user;
    }

    public void setUser(User user) {
        email.setText(user.getEmail());
        password.setText(user.getPassword());
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
    }
}
