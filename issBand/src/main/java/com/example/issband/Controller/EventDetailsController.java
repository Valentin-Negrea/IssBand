package com.example.issband.Controller;

import com.example.issband.Domain.Band;
import com.example.issband.Domain.Event;
import com.example.issband.Domain.Ticket;
import com.example.issband.Repository.SQLEventRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EventDetailsController {
    @FXML
    private Label eventIdLabel;
    @FXML
    private Label eventNameLabel;
    @FXML
    private Label eventDateLabel;
    @FXML
    private Label eventVenueLabel;
    @FXML
    private Label bandsLabel;
    @FXML
    private Label ticketsLabel;
    @FXML
    private Label audienceLabel;
    @FXML
    private Button attendAsBandButton;
    @FXML
    private Button buyTicketButton;
    @FXML
    private Button refreshButton;

    private Stage dialogStage;
    private Collectors Collectors;
    private final SQLEventRepository eventRepository = new SQLEventRepository("iss.db");

    @FXML
    private void handleCloseButton() {
        dialogStage.close();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setEventDetails(Event event) {
        eventIdLabel.setText("Event ID: " + event.getId());
        eventNameLabel.setText("Event Name: " + event.getName());
        eventDateLabel.setText("Date: " + event.getDate());
        eventVenueLabel.setText("Venue: " + event.getVenue());

        // Display bands
        List<String> bandNames = new ArrayList<>();
        for (Band band : event.getBands()) {
            bandNames.add(band.getName());
        }
        bandsLabel.setText("Bands: " + String.join(", ", bandNames));

        // Display tickets and audience details
        List<Integer> ticketsID = new ArrayList<>();
        for (Ticket ticket : event.getTickets()) {
            ticketsID.add(ticket.getId());
        }

        // Convert the list of ticket IDs to a list of strings
        List<String> ticketsIDStrings = ticketsID.stream()
                .map(String::valueOf)
                .collect(Collectors.toList());

        List<String> ticketsNames = new ArrayList<>();

        // Iterate over the ticket IDs
        for (Integer ticketID : ticketsID) {
            // Retrieve the corresponding ticket using its ID from the eventRepository
            Ticket ticket = eventRepository.getTicketById(ticketID);
            // If the ticket is not null it has an associated audience, add the audience's name to the list
            if (ticket != null && ticket.getAudience() != null) {
                ticketsNames.add(ticket.getAudience().getName());
            }
        }

        // Display the tickets
        ticketsLabel.setText("TicketsIDs: " + String.join(", ", ticketsIDStrings));
        audienceLabel.setText("Audience: " + String.join(", ", ticketsNames));
    }

    @FXML
    private void handleOk() {
        dialogStage.close();
    }

    @FXML
    private void handleAttendAsBand() {
        System.out.println("Attend As Band button clicked");

        try {
            // Load the FXML file for attending as a band
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/issband/AttendAsBand.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Attend as Band");
            stage.setScene(new Scene(root));

            // Get the controller and set the event ID and dialog stage
            AttendAsBandController controller = fxmlLoader.getController();
            controller.setEventId(Integer.parseInt(eventIdLabel.getText().split(" ")[2])); // Extract event ID from label
            controller.setDialogStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBuyTicket() {
        System.out.println("Buy Ticket button clicked");

        try {
            // Load the FXML file for buying a ticket
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/issband/BuyTicket.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Buy Ticket");
            stage.setScene(new Scene(root));

            // Get the controller and set the event ID and dialog stage
            BuyTicketController controller = fxmlLoader.getController();
            controller.setEventId(Integer.parseInt(eventIdLabel.getText().split(" ")[2])); // Extract event ID from label
            controller.setDialogStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeleteBand() {
        try {
            // Load the FXML file for deleting a band
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/issband/DeleteBand.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Delete Band");
            stage.setScene(new Scene(root));

            // Get the controller and set the event ID and dialog stage
            DeleteBandController controller = fxmlLoader.getController();
            controller.setEventId(Integer.parseInt(eventIdLabel.getText().split(" ")[2])); // Extract event ID from label
            controller.setDialogStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleDeleteTicket() {
        try {
            // Load the FXML file for deleting a ticket
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/issband/DeleteTicket.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Delete Ticket");
            stage.setScene(new Scene(root));

            // Get the controller and set the event ID and dialog stage
            DeleteTicketController controller = fxmlLoader.getController();
            controller.setEventId(Integer.parseInt(eventIdLabel.getText().split(" ")[2])); // Extract event ID from label
            controller.setDialogStage(stage);

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        String eventIdText = eventIdLabel.getText();
        int eventId = Integer.parseInt(eventIdText.split(": ")[1].trim());

        Event refreshedEvent = null; // Assuming you have a method to get event by ID
        try {
            refreshedEvent = eventRepository.getEventById(eventId);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        setEventDetails(refreshedEvent);
    }
}
