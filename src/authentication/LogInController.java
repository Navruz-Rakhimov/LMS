package authentication;

import user.User;
import staticTools.UserTracker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

public class LogInController {

    @FXML
    public PasswordField passwordTxt;
    @FXML
    public Button signUpButton;
    @FXML
    public TextField emailTxt;
    @FXML
    public Button logInButton;
    @FXML
    public Label warningLabel;

    @FXML
    public void handleSignUp(ActionEvent actionEvent) {
        /*RadioButton button = (RadioButton) roleToggleGroup.getSelectedToggle();

        try {
            Parent root = FXMLLoader.load(getClass().getResource("signUpFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        /*signUpButton.getScene().getWindow().hide();
        try {
            Parent root = FXMLLoader.load(getClass().getResource("signUpFxml.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    @FXML
    public void handleLogIn(ActionEvent actionEvent) {
        String email = emailTxt.getText().trim();
        String password = passwordTxt.getText().trim();


        UsersRepository.getInstance().setCurrentUserEmail(email);

        UserTracker.trackLogIn(email);
      
        if (!email.equals("") && !password.equals("")) {
            User user = new User(email, password);

            if (UsersRepository.getInstance().verifyUser(user)) {
                warningLabel.setText("Success! Loading...");

                int role = UsersRepository.getInstance().getUserRole(user);
                Parent root = null;

                try {
                    switch (role) {
                        case 0:
                            root = FXMLLoader.load(getClass().getResource("/adminWindow/adminWindowFxml.fxml"));
                            break;
                        case 1:
                            root = FXMLLoader.load(getClass().getResource("/librarianWindow/librarianMainWindow.fxml"));
                            break;
                        case 2:
                            root = FXMLLoader.load(getClass().getResource("/studentWindow/booksWindow/booksWindow.fxml"));
                            break;
                    }
                } catch (IOException e) {
                    if (root == null)
                        System.out.println("Root is null");
                    e.printStackTrace();
                }

                if (root != null) {
                    System.out.println("Root is not null");
                    Node source = (Node) actionEvent.getSource();
                    Stage stage = (Stage) source.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                }

            } else {
                warningLabel.setText("Incorrect username or password!");
            }
        }
    }
}
