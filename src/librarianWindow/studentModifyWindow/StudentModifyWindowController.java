package librarianWindow.studentModifyWindow;

import authentication.UsersRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import user.User;

public class StudentModifyWindowController {
    private User user;
    @FXML
    private TextField emailTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField passwordTextField;

    @FXML
    public void initialize(){}

    @FXML
    public void setUserParams(User user){
        this.user = user;
        emailTextField.setText(user.getEmail());
        firstNameTextField.setText(user.getFirstName());
        lastNameTextField.setText(user.getLastName());
        passwordTextField.setText(user.getPassword());
    }

    @FXML
    public void userModify(ActionEvent act){
        User editedUser = this.user;
        editedUser.setEmail(emailTextField.getText());
        editedUser.setFirstName(firstNameTextField.getText());
        editedUser.setLastName(lastNameTextField.getText());
        editedUser.setPassword(passwordTextField.getText());

        UsersRepository.getInstance().updateUser(this.user, editedUser);

        closeStage(act);
    }

    private void closeStage(ActionEvent act) {
        Node source = (Node)  act.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

