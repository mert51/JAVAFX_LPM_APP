package com.sau.lpm.lpm_p1.controller;

import com.sau.lpm.lpm_p1.db.ReservationCrudOperations;
import com.sau.lpm.lpm_p1.dto.Reservation;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Optional;

public class ReservationController {
    @FXML
    private TextField reservationStudentId;

    @FXML
    private TextField reservationPlaceId;

    @FXML
    private TextField reservationDate;

    @FXML
    private TextField reservationDuration;

    @FXML
    private Button getReservation;

    @FXML
    private Button saveReservation;

    @FXML
    private Button updateReservation;

    @FXML
    private Button deleteReservation;

    @FXML
    private Button clearPlace;

    @FXML
    private Button close;

    @FXML
    void clearPlace(ActionEvent event) {
        reservationStudentId.setText("");
        reservationPlaceId.setText("");
        reservationDate.setText("");
        reservationDuration.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void deleteReservation(ActionEvent event) {
        checkIds(reservationStudentId.getText(), reservationPlaceId.getText(), event);
        ReservationCrudOperations crudOperations = new ReservationCrudOperations();
        int studentId = Integer.parseInt(reservationStudentId.getText());
        int placeId = Integer.parseInt(reservationPlaceId.getText());
        int result = crudOperations.deleteReservation(studentId, placeId);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("Reservation with studentId " + studentId + " and placeId " + placeId + " deleted");
        alert.showAndWait();
        clearPlace(event);
    }

    @FXML
    void getReservation(ActionEvent event) {
        checkIds(reservationStudentId.getText(), reservationPlaceId.getText(), event);
        ReservationCrudOperations crudOperations = new ReservationCrudOperations();
        int studentId = Integer.parseInt(reservationStudentId.getText());
        int placeId = Integer.parseInt(reservationPlaceId.getText());
        Optional<Reservation> reservation = crudOperations.getReservationByStudentAndPlace(studentId, placeId);
        if (reservation.isPresent()) {
            reservationStudentId.setText(Integer.toString(reservation.get().getStudentId()));
            reservationPlaceId.setText(Integer.toString(reservation.get().getPlaceId()));
            reservationDate.setText(reservation.get().getDate());
            reservationDuration.setText(Integer.toString(reservation.get().getDuration()));

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Reservation with studentId " + studentId + " and placeId " + placeId + " not found");
            alert.showAndWait();
        }
    }

    @FXML
    void saveReservation(ActionEvent event) {
        checkIds(reservationStudentId.getText(), reservationPlaceId.getText(), event);

        // Validate Duration
        try {
            Integer.parseInt(reservationDuration.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Duration must be a valid integer!");
            alert.showAndWait();
            return;
        }

        // Validate Date format (YYYY-MM-DD)
        String dateText = reservationDate.getText().trim();
        if (!dateText.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Date must be in YYYY-MM-DD format!");
            alert.showAndWait();
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setStudentId(Integer.parseInt(reservationStudentId.getText().trim()));
        reservation.setPlaceId(Integer.parseInt(reservationPlaceId.getText().trim()));
        reservation.setDate(dateText);
        reservation.setDuration(Integer.parseInt(reservationDuration.getText().trim()));
        ReservationCrudOperations crudOperations = new ReservationCrudOperations();

        try {
            int res = crudOperations.insertReservation(reservation);
            if (res > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reservation with studentId " + reservationStudentId.getText() + " and placeId " + reservationPlaceId.getText() + " saved");
                alert.showAndWait();
                clearPlace(event);
            } else if (res == -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("There is another reservation with studentId " + reservationStudentId.getText() + " and placeId " + reservationPlaceId.getText());
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error on save reservation!");
                alert.showAndWait();
            }
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    void updateReservation(ActionEvent event) {
        checkIds(reservationStudentId.getText(), reservationPlaceId.getText(), event);

        // Validate Duration
        try {
            Integer.parseInt(reservationDuration.getText().trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Duration must be a valid integer!");
            alert.showAndWait();
            return;
        }

        // Validate Date format (YYYY-MM-DD)
        String dateText = reservationDate.getText().trim();
        if (!dateText.matches("\\d{4}-\\d{2}-\\d{2}")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Date must be in YYYY-MM-DD format!");
            alert.showAndWait();
            return;
        }

        Reservation reservation = new Reservation();
        reservation.setStudentId(Integer.parseInt(reservationStudentId.getText().trim()));
        reservation.setPlaceId(Integer.parseInt(reservationPlaceId.getText().trim()));
        reservation.setDate(dateText);
        reservation.setDuration(Integer.parseInt(reservationDuration.getText().trim()));
        ReservationCrudOperations crudOperations = new ReservationCrudOperations();

        try {
            int res = crudOperations.updateReservation(reservation);
            if (res > 0) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Reservation with studentId " + reservationStudentId.getText() + " and placeId " + reservationPlaceId.getText() + " is updated");
                alert.showAndWait();
                clearPlace(event);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Error on update reservation!");
                alert.showAndWait();
            }
        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Utility
    public void checkIds(String studentId, String placeId, ActionEvent event) {
        if (studentId.isEmpty() || placeId.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("StudentId or PlaceId cannot be empty!");
            alert.showAndWait();
            clearPlace(event);
            return;
        }

        try {
            int sId = Integer.parseInt(studentId.trim());
            int pId = Integer.parseInt(placeId.trim());

            if (sId <= 0 || pId <= 0) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("StudentId and PlaceId must be positive numbers!");
                alert.showAndWait();
                clearPlace(event);
            }
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("StudentId and PlaceId must be valid integers!");
            alert.showAndWait();
            clearPlace(event);
        }
    }
}
