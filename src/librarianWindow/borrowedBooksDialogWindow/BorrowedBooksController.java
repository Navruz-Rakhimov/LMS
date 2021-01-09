package librarianWindow.borrowedBooksDialogWindow;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import librarianWindow.StudentsBorrowed;
import user.User;

public class BorrowedBooksController {
    private ObservableList<StudentsBorrowed> students = null;
    @FXML
    private TableView<StudentsBorrowed> studentsTableView;
    @FXML
    private TableColumn<StudentsBorrowed, String> emailColumn;
    @FXML
    private TableColumn<StudentsBorrowed, String> firstNameColumn;
    @FXML
    private TableColumn<StudentsBorrowed, String> lastNameColumn;
    @FXML
    private Label bookTitleLabel;

    @FXML
    public void initialize(){
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    }

    @FXML
    public void setStudentsInfo(ObservableList<StudentsBorrowed> students, String bookTitle){
        this.students = students;
        studentsTableView.setItems(this.students);
        studentsTableView.refresh();
        bookTitleLabel.setText(bookTitle);
    }

    @FXML
    public void closeWindow(ActionEvent event){
        Node source = (Node)  event.getSource();
        Stage stage  = (Stage) source.getScene().getWindow();
        stage.close();
    }
}
