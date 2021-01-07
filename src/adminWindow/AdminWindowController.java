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
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(gridPane.getScene().getWindow());
        dialog.setTitle("Modify Student Details");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newUserDialogFxml.fxml"));

        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        NewUserDialogController controller = fxmlLoader.getController();

        int addressIndex = tableView.getSelectionModel().getSelectedIndex();
        User oldStudent = null;
        try {
            oldStudent = students.get(addressIndex);
        } catch (Exception e) {}


        if (oldStudent != null) {
            controller.setUser(oldStudent);
            Optional<ButtonType> result = dialog.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                User newStudent = controller.getUser(2);
                if (newStudent != null) {
                    if (UsersRepository.getInstance().updateUser(oldStudent, newStudent)) {
                        oldStudent.setEmail(newStudent.getEmail());
                        oldStudent.setPassword(newStudent.getPassword());
                        oldStudent.setFirstName(newStudent.getFirstName());
                        oldStudent.setLastName(newStudent.getLastName());
                        tableView.refresh();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Modify Student Details");
                        alert.setHeaderText("The student details could not be modified!");
                        alert.setContentText("Student with entered email already exists!");
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
            if (UsersRepository.getInstance().addUser(student)) {
                student.setUserId(UsersRepository.getInstance().getUserId(student));
                students.add(student);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Add New Student");
                alert.setHeaderText("The student was not added!");
                alert.setContentText("Student with entered email already exists!");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void handleDeleteButton(ActionEvent actionEvent) {
        try {
            int addressIndex = tableView.getSelectionModel().getSelectedIndex();
            User student = students.get(addressIndex);

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Student");
            alert.setHeaderText("Are you sure you want to delete the student: " + student.getFirstName() + " " + student.getLastName());
            Optional<ButtonType> result  = alert.showAndWait();

            if (result.isPresent() && result.get() == ButtonType.OK) {
                UsersRepository.getInstance().deleteUser(student);
                students.remove(addressIndex);
            }
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
