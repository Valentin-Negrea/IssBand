package com.example.issband.Controller;

import com.example.issband.Repository.SQLTicketRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class DeleteTicketController {

    private int eventId;

    @FXML
    private TextField audienceNameTextField;
    private Stage dialogStage;
    private SQLTicketRepository ticketRepository = new SQLTicketRepository("iss.db");
    @FXML
    public void handleDeleteTicket(ActionEvent actionEvent) {
        String audienceName = audienceNameTextField.getText();
        try {
            // Delete the ticket associated with the audience
            ticketRepository.deleteTicketByAudienceNameAndEventId(eventId, audienceName);

            // Delete the audience from the audience table
            ticketRepository.deleteAudienceByName(audienceName);

            // Show a success message
            showAlert(Alert.AlertType.INFORMATION, "Ticket Deleted", "The ticket and associated audience have been deleted successfully.");
            dialogStage.close();
        } catch (SQLException e) {
            // Show an error message
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to delete the ticket and associated audience: " + e.getMessage());
        }
    }



    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }


    public void setDialogStage(Stage stage) {
        this.dialogStage = stage;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
}
