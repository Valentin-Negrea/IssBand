package com.example.issband.Controller;

import com.example.issband.Domain.Band;
import com.example.issband.Domain.Manager;
import com.example.issband.Repository.SQLBandRepository;
import com.example.issband.Repository.SQLManagerRepository;
import com.example.issband.RepositoryException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class AttendAsBandController {
    @FXML private TextField bandNameField;
    @FXML private TextField managerNameField;
    @FXML private Button registerBandButton;

    private Stage dialogStage;
    private int eventId;
    private final SQLBandRepository bandRepository = new SQLBandRepository("iss.db");
    private final SQLManagerRepository managerRepository = new SQLManagerRepository("iss.db");

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    public void handleRegisterBand(ActionEvent actionEvent) {
        String bandName = bandNameField.getText();
        String managerName = managerNameField.getText();

        if (bandName.isEmpty() || managerName.isEmpty()) {
            showAlert("Error", "Please fill in both Band Name and Manager Name.");
            return;
        }

        try {
            // Retrieve the manager by name
            Manager manager = managerRepository.getManagerByName(managerName);
            if (manager != null) {
                // Get the highest band ID and increment it
                int highestId = bandRepository.getHighestBandId();
                int newId = highestId + 1;

                // Create and save the new band with the new ID
                Band band = new Band(newId, bandName, manager);
                bandRepository.add(band);

                // Add the new band to the event
                bandRepository.addBandToEvent(eventId, newId);

                showAlert("Success", "Band registered successfully.");
                dialogStage.close();  // Ensure this method is called only if dialogStage is not null
            } else {
                showAlert("Error", "Manager not found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while registering the band.");
        }
    }
}
