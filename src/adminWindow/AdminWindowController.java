package adminWindow;

import authentication.UsersRepository;
import book.BooksRepository;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import user.User;

import java.io.IOException;
import java.util.Optional;

public class AdminWindowController {


    ObservableList<User> students;

    @FXML
    public TextField searchTxt;
    @FXML
    public TableView tableView;
    @FXML
    public Button studentsButton;
    @FXML
    public Button librariansButton;
    @FXML
    public Button booksButton;
    @FXML
    public Button signOutButton;
    @FXML
    public GridPane gridPane;

    public void initialize() {
        students = UsersRepository.getInstance().getAllStudents();
        tableView.setItems(students);
    }

    @FXML
    public void handleStudentsButton(ActionEvent actionEvent) {
        students = UsersRepository.getInstance().getAllStudents();
        tableView.setItems(students);
    }

    @FXML
    public void handleLibrariansButton(ActionEvent actionEvent) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/adminWindow/adminLibrarianWindow/adminLibrarianFxml.fxml"));
            Node source = (Node) actionEvent.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
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

        dialog.setTitle("Add New Student");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newUserDialogFxml.fxml"));

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
            User student = controller.getUser(2);
            UsersRepository.getInstance().addUser(student);
            students.add(student);
        }
    }

    @FXML
    public void handleDeleteButton(ActionEvent actionEvent) {
        try {
            int addressIndex = tableView.getSelectionModel().getSelectedIndex();
            User user = students.get(addressIndex);
            UsersRepository.getInstance().deleteUser(user);
            students.remove(addressIndex);
        } catch (Exception e) {
            System.out.println("Select an item");
        }
    }

    @FXML
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
