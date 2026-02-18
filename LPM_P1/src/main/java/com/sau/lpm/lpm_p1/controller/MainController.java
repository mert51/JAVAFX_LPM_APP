package com.sau.lpm.lpm_p1.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

import java.io.IOException;

public class MainController {

	@FXML
	private TabPane tabPane;

	@FXML
	private Tab studentsTab;

	@FXML
	private Tab placesTab;

	@FXML
	private Tab reservationsTab;

	private boolean studentsLoaded = false;
	private boolean placesLoaded = false;
	private boolean reservationsLoaded = false;

	@FXML
	public void initialize() {
		// When a tab is selected, load its FXML content lazily
		tabPane.getSelectionModel().selectedItemProperty().addListener((obs, oldTab, newTab) -> {
			if (newTab == studentsTab && !studentsLoaded) {
				studentsLoaded = loadTabContent(studentsTab, "/com/sau/lpm/lpm_p1/Student.fxml");
			} else if (newTab == placesTab && !placesLoaded) {
				placesLoaded = loadTabContent(placesTab, "/com/sau/lpm/lpm_p1/Place.fxml");
			} else if (newTab == reservationsTab && !reservationsLoaded) {
				reservationsLoaded = loadTabContent(reservationsTab, "/com/sau/lpm/lpm_p1/Reservation.fxml");
			}
		});

		// Optionally load first tab immediately
		if (!studentsLoaded) {
			studentsLoaded = loadTabContent(studentsTab, "/com/sau/lpm/lpm_p1/Student.fxml");
		}
	}

	private boolean loadTabContent(Tab tab, String resourcePath) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(resourcePath));
			Node content = loader.load();
			tab.setContent(content);
			return true;
		} catch (IOException | RuntimeException e) {
			System.err.println("Failed to load FXML for tab " + tab.getText() + ": " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
}
