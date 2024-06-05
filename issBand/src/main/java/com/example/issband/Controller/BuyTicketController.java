package com.example.issband.Controller;

import com.example.issband.Domain.Audience;
import com.example.issband.Domain.Ticket;
import com.example.issband.Repository.SQLAudienceRepository;
import com.example.issband.Repository.SQLTicketRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.SQLException;

public class BuyTicketController {
    @FXML
    private TextField audienceNameField;
    @FXML
    private Button buyTicketButton;

    private Stage dialogStage;
    private int eventId;
    private final SQLAudienceRepository audienceRepository = new SQLAudienceRepository("iss.db");
    private final SQLTicketRepository ticketRepository = new SQLTicketRepository("iss.db");

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
    public void handleBuyTicket(ActionEvent actionEvent) {
        String audienceName = audienceNameField.getText();

        if (audienceName.isEmpty()) {
            showAlert("Error", "Please enter an audience name.");
            return;
        }

        try {
            // Retrieve the audience by name or create a new one
            Audience audience = audienceRepository.getAudienceByName(audienceName);
            if (audience == null) {
                int highestId = audienceRepository.getHighestAudienceId();
                int newId = highestId + 1;
                audience = new Audience(newId, audienceName);
                audienceRepository.add(audience);
            }

            // Get the highest ticket ID and increment it
            int highestTicketId = ticketRepository.getHighestTicketId();
            int newTicketId = highestTicketId + 1;

            // Create and save the new ticket with the new ID, event ID, and audience ID
            ticketRepository.addTicket(newTicketId, eventId, audience.getId());

            showAlert("Success", "Ticket purchased successfully.");
            dialogStage.close();  // Ensure this method is called only if dialogStage is not null
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "An error occurred while purchasing the ticket.");
        }
    }
}
