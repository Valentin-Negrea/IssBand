package com.example.issband.Controller;

import com.example.issband.Domain.Band;
import com.example.issband.Repository.SQLBandRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DeleteBandController {
    @FXML
    private TextField bandNameTextField;
    private int eventId;

    private Stage dialogStage;
    private final SQLBandRepository bandRepository = new SQLBandRepository("iss.db");

    @FXML
    public void handleDeleteBand(ActionEvent actionEvent) {
        String bandName = bandNameTextField.getText();
        try {
            Band band = bandRepository.getBandFromName(bandName);
            if (band != null) {
                bandRepository.deleteBandByName(bandName);
                bandRepository.deleteBandFromEvent(eventId, band.getId());
                System.out.println("Band '" + bandName + "' deleted successfully.");

                // Show alert for success
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("Success");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Band '" + bandName + "' deleted successfully.");
                successAlert.showAndWait();
                dialogStage.close();
            } else {
                System.out.println("Band '" + bandName + "' not found.");
            }
        } catch (SQLException e) {
            System.out.println("Error deleting band: " + e.getMessage());
        }
    }


    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
