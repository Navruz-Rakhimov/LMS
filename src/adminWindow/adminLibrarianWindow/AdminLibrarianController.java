package adminWindow.adminLibrarianWindow;

import adminWindow.NewUserDialogController;
import authentication.UsersRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;
import java.util.Optional;

public class AdminLibrarianController {

    ObservableList<User> librarians;

    @FXML
    public GridPane gridPane;
    @FXML
    public Button studentsButton;
    @FXML
    public Button librariansButton;
    @FXML
    public Button booksButton;
    @FXML
    public Button signOutButton;
    @FXML
    public TableView tableView;

    public void initialize() {
        librarians = UsersRepository.getInstance().getAllLibrarians();
        tableView.setItems(librarians);
    }

    @FXML
    public void handleViewButton(ActionEvent actionEvent) {
    }

    @FXML
    public void handleModifyButton(ActionEvent actionEvent) {
    }

    @FXML
    public void handleAddButton(ActionEvent actionEvent) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());

        dialog.setTitle("Add New Librarian");

        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/adminWindow/newUserDialogFxml.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());

        } catch (IOException e) {
            e.printStackTrace();
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = dialog.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewUserDialogController controller = fxmlLoader.getController();

            User librarian = controller.getUser(1);

            UsersRepository.getInstance().addUser(librarian);
            librarians.add(librarian);
        }
    }

    @FXML
    public void handleDeleteButton(ActionEvent actionEvent) {
        try {
            int addressIndex = tableView.getSelectionModel().getSelectedIndex();
            User librarian = librarians.get(addressIndex);
            UsersRepository.getInstance().deleteUser(librarian);
            librarians.remove(addressIndex);
        } catch (Exception e) {
            System.out.println("Select an item");
        }
    }

    public void handleStudentsButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/adminWindow/adminWindowFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleLibrariansButton(ActionEvent actionEvent) {
        librarians = UsersRepository.getInstance().getAllLibrarians();
        tableView.setItems(librarians);
    }

    public void handleBooksButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/adminWindow/adminBookWindow/adminBookFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleSignOutButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/authentication/logInFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
