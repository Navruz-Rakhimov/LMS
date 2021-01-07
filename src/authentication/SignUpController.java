package authentication;

import user.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class SignUpController {
    @FXML
    public TextField emailTxt;
    @FXML
    public PasswordField passwordTxt;
    @FXML
    public RadioButton studentRadioButton;
    @FXML
    public RadioButton librarianRadioButton;
    @FXML
    public Button signUpButton;
    @FXML
    public TextField usernameTxt;
    @FXML
    public Button logInButton;
    public Label warningLabel;


    @FXML
    public void handleSignUp(ActionEvent actionEvent) {
        String signUpUsername = usernameTxt.getText().trim();
        String signUpPassword = passwordTxt.getText().trim();
        String signUpEmail = emailTxt.getText().trim();

        if (!signUpUsername.equals("") && !signUpPassword.equals("") && !signUpEmail.equals("")) {
            User user = new User(signUpUsername, signUpPassword, signUpEmail);
            if (UsersRepository.getInstance().addUser(user)) {
                warningLabel.setText("Success! Now log in.");
            } else {
                warningLabel.setText("This username is taken");
            }
        }
    }

    @FXML
    public void handleLogIn(ActionEvent actionEvent) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("logInFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*logInButton.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("logInFxml.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }
}
