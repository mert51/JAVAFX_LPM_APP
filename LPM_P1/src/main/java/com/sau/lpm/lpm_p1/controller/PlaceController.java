package com.sau.lpm.lpm_p1.controller;

import com.sau.lpm.lpm_p1.db.PlaceCrudOperations;
import com.sau.lpm.lpm_p1.dto.Place;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.util.Optional;

public class  PlaceController {
    @FXML
    private TextField building ;

    @FXML
    private TextField placeId;

    @FXML
    private Button clearPlace;

    @FXML
    private Button close;

    @FXML
    private Button deletePlace;

    @FXML
    private Button getPlace;

    @FXML
    private TextField floor;

    @FXML
    private TextField room;

    @FXML
    private TextField seat;

    @FXML
    private Button savePlace;

    @FXML
    private Button updatePlace;

    @FXML
    void clearPlace(ActionEvent event) {
        placeId.setText("");
        building.setText("");
        floor.setText("");
        room.setText("");
        seat.setText("");
    }

    @FXML
    public void close(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void deletePlace(ActionEvent event) {
        if (!checkId(placeId.getText(), event)) return;
        PlaceCrudOperations crudOperations = new PlaceCrudOperations();
        int id = Integer.parseInt(placeId.getText().trim());
        int result = crudOperations.deletePlaceById(id);
        Alert alert = new Alert(result > 0 ? Alert.AlertType.INFORMATION : Alert.AlertType.ERROR);
        alert.setTitle(result > 0 ? "Success" : "Error");
        if (result > 0) {
            alert.setHeaderText("Place with id " + id + " deleted");
        } else {
            alert.setHeaderText("Place with id " + id + " not found");
        }
        alert.showAndWait();
        clearPlace(event);
    }

    @FXML
    void getPlace(ActionEvent event) {
        if (!checkId(placeId.getText(), event)) return;
        PlaceCrudOperations crudOperations = new PlaceCrudOperations();
        int id = Integer.parseInt(placeId.getText().trim());
        Optional<Place> place = crudOperations.getPlaceById(id);
        if(place.isPresent()){
            placeId.setText(Integer.toString(place.get().getId()));
            building.setText(place.get().getBuilding());
            floor.setText(place.get().getFloor());
            room.setText(place.get().getRoom());
            seat.setText(Integer.toString(place.get().getSeat()));

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Place with id " + id + " not found");
            alert.showAndWait();
        }
    }

    @FXML
    void savePlace(ActionEvent event) {
        if (!checkId(placeId.getText(), event)) return;
        Place place = new Place();
        place.setBuilding(building.getText());
        place.setFloor(floor.getText());
        place.setRoom(room.getText());
        // Validate seat is an integer
        String seatText = seat.getText();
        int seatNumber = 0;
        try {
            seatNumber = Integer.parseInt(seatText.trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Seat must be a valid integer!");
            alert.showAndWait();
            return;
        }
        place.setSeat(seatNumber);
        place.setId(Integer.parseInt(placeId.getText().trim()));
        PlaceCrudOperations crudOperations = new PlaceCrudOperations();
        int res = crudOperations.insertPlaceById(place);
        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Place with id " + placeId.getText() + " saved");
            alert.showAndWait();
        } else if(res == -1){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("There another place with id: " + placeId.getText());
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on save place!");
            alert.showAndWait();
        }
    }

    @FXML
    void updatePlace(ActionEvent event) {
        if (!checkId(placeId.getText(), event)) return;
        Place place = new Place();
        place.setBuilding(building.getText());
        place.setFloor(floor.getText());
        place.setRoom(room.getText());
        // Validate seat is an integer
        String seatText = seat.getText();
        int seatNumber = 0;
        try {
            seatNumber = Integer.parseInt(seatText.trim());
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Seat must be a valid integer!");
            alert.showAndWait();
            return;
        }
        place.setSeat(seatNumber);
        place.setId(Integer.parseInt(placeId.getText().trim()));
        PlaceCrudOperations crudOperations = new PlaceCrudOperations();
        int res = crudOperations.updatePlaceById(place);
        if(res > 0){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Place with id " + placeId.getText() + " id updated");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error on update place!");
            alert.showAndWait();
        }
    }

    // Utility
    private boolean checkId(String id, ActionEvent event) {
        if (id == null || id.trim().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id is wrong!");
            alert.showAndWait();
            clearPlace(event);
            return false;
        }
        String trimmed = id.trim();
        int parsed;
        try {
            parsed = Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id is not a valid number!");
            alert.showAndWait();
            clearPlace(event);
            return false;
        }
        if (parsed <= 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Id must be greater than 0!");
            alert.showAndWait();
            clearPlace(event);
            return false;
        }
        return true;
    }
}
