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
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());
        dialog.setTitle("Modify Librarian Details");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/adminWindow/newUserDialogFxml.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewUserDialogController controller = fxmlLoader.getController();

        int addressIndex = tableView.getSelectionModel().getSelectedIndex();
        User oldLibrarian = null;
        try {
            oldLibrarian = librarians.get(addressIndex);
        } catch (Exception e) {}

        if (oldLibrarian != null) {
            controller.setUser(oldLibrarian);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                User newLibrarian = controller.getUser(1);
                if (newLibrarian != null) {
                    if (UsersRepository.getInstance().updateUser(oldLibrarian, newLibrarian)) {
                        oldLibrarian.setEmail(newLibrarian.getEmail());
                        oldLibrarian.setPassword(newLibrarian.getPassword());
                        oldLibrarian.setFirstName(newLibrarian.getFirstName());
                        oldLibrarian.setLastName(newLibrarian.getLastName());
                        tableView.refresh();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Modify Librarian Details");
                        alert.setHeaderText("The librarian details could not be modified!");
                        alert.setContentText("Librarian with entered email already exists!");
                        alert.showAndWait();
                    }
                }
            }
        }
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

            if (UsersRepository.getInstance().addUser(librarian)) {
                librarian.setUserId(UsersRepository.getInstance().getUserId(librarian));
                librarians.add(librarian);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Add New Librarian");
                alert.setHeaderText("The librarian was not added!");
                alert.setContentText("Librarian with entered email already exists!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleDeleteButton(ActionEvent actionEvent) {
        try {
            int addressIndex = tableView.getSelectionModel().getSelectedIndex();
            User librarian = librarians.get(addressIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Librarian");
            alert.setHeaderText("Are you sure you want to delete the librarian: " + librarian.getFirstName() + " " + librarian.getLastName());
            Optional<ButtonType> result  = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                UsersRepository.getInstance().deleteUser(librarian);
                librarians.remove(addressIndex);
            }
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
