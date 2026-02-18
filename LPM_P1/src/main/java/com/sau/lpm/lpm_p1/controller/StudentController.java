package com.sau.lpm.lpm_p1.controller;

import com.sau.lpm.lpm_p1.db.StudentCrudOperation;
import com.sau.lpm.lpm_p1.dto.Student;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Optional;

public class StudentController {

    @FXML
    private TextField name;

    @FXML
    private TextField studentId;

    @FXML
    private Button clearStudent;

    @FXML
    private Button updateStudent;

    @FXML
    private Button close;

    @FXML
    private Button deleteStudent;

    @FXML
    private Button getStudent;

    @FXML
    private TextField department;

    @FXML
    private Button saveStudent;

    @FXML
    void clearStudent(ActionEvent event) {
        studentId.setText("");
        name.setText("");
        department.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void deleteStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) {
            return;
        }
        StudentCrudOperation crudOperations = new StudentCrudOperation();
        int id = Integer.parseInt(studentId.getText());
        // First check if there are reservations referencing this student
        int refCount = crudOperations.countReservationsByStudentId(id);
        Alert alert;
        if (refCount > 0) {
            // Ask user whether to delete related reservations first
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
            confirm.setTitle("Related reservations found");
            confirm.setHeaderText("There are " + refCount + " reservation(s) referencing this student (ID=" + id + ").");
            confirm.setContentText("Do you want to delete these reservations and then delete the student?");
            Optional<ButtonType> choice = confirm.showAndWait();
            if (choice.isPresent() && choice.get() == ButtonType.OK) {
                int deletedRes = crudOperations.deleteReservationsByStudentId(id);
                System.out.println("Deleted reservations: " + deletedRes);
                // Now try deleting the student
                int result = crudOperations.deleteStudentById(id);
                if (result > 0) {
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Success");
                    alert.setHeaderText("Student with id " + studentId.getText() + " deleted (and related reservations removed)");
                    alert.showAndWait();
                    clearStudent(event);
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText("Student could not be deleted after removing reservations.");
                    alert.showAndWait();
                }
            } else {
                // User cancelled; do nothing
                return;
            }
        } else {
            int result = crudOperations.deleteStudentById(id);
            if (result > 0) {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Student with id " + studentId.getText() + " deleted");
                alert.showAndWait();
                clearStudent(event);
            } else if (result == -2) {
                // Shouldn't happen because we checked refCount, but handle defensively
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete failed");
                alert.setHeaderText("Cannot delete student with id " + id + ". There are reservations referencing this student.");
                alert.setContentText("Please delete related reservations first or update the foreign key constraint (ON DELETE CASCADE) if appropriate.");
                alert.showAndWait();
            } else if (result == -1) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("An error occurred while deleting the student.");
                alert.showAndWait();
            } else {
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Not found");
                alert.setHeaderText("No student with id " + id + " was found.");
                alert.showAndWait();
                clearStudent(event);
            }
        }
    }

    @FXML
    void getStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) {
            return;
        }
        StudentCrudOperation crudOperations = new StudentCrudOperation();
        int id = Integer.parseInt(studentId.getText());
        Optional<Student> student = crudOperations.getStudentById(id);
        if (student.isPresent()) {
            studentId.setText(Integer.toString(student.get().getId()));
            name.setText(student.get().getName());
            department.setText(student.get().getDepartment());
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Student with id " + id + " not found");
            alert.showAndWait();
        }
    }

    @FXML
    void saveStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) {
            return;
        }
        Student student = new Student();
        student.setName(name.getText());
        student.setDepartment(department.getText());
        student.setId(Integer.parseInt(studentId.getText()));
        StudentCrudOperation crudOperations = new StudentCrudOperation();
        int res = crudOperations.insertStudentById(student);
        if (res > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Student with id " + studentId.getText() + " saved");
            alert.showAndWait();
        } else if (res == -1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There another student with id: " + studentId.getText());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on save student!");
            alert.showAndWait();
        }
    }

    @FXML
    void updateStudent(ActionEvent event) {
        if (!checkId(studentId.getText(), event)) {
            return;
        }
        Student student = new Student();
        student.setName(name.getText());
        student.setDepartment(department.getText());
        student.setId(Integer.parseInt(studentId.getText()));
        StudentCrudOperation crudOperations = new StudentCrudOperation();
        int res = crudOperations.updateStudentById(student);
        if (res > 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Student with id " + studentId.getText() + " id updated");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on update student!");
            alert.showAndWait();
        }
    }

    // Utility
    public boolean checkId(String id, ActionEvent event) {
        try {
            if (id.isEmpty() || Integer.parseInt(id) <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Id is wrong!");
                alert.showAndWait();
                clearStudent(event);
                return false;
            }
            return true;
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id must be a valid number!");
            alert.showAndWait();
            clearStudent(event);
            return false;
        }
    }
}
