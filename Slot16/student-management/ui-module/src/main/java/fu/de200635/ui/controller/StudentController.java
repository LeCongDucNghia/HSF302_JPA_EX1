package fu.de200635.ui.controller;

import fu.de200635.data.pojo.Student;
import fu.de200635.data.service.StudentService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

@Component
public class StudentController {

    @FXML private TableView<Student> tableView;
    @FXML private TextField searchField, idField, nameField, emailField, ageField;
    @FXML private Button btnUpdate, btnDelete;
    @FXML private Label messageLabel, statusLabel;

    private final StudentService studentService;   // INTERFACE, không phải StudentServiceImpl

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @FXML private void handleSearch()  { }
    @FXML private void handleRefresh() { }
    @FXML private void handleAdd()     { }
    @FXML private void handleUpdate()  { }
    @FXML private void handleDelete()  { }
    @FXML private void handleClear()   { }
}