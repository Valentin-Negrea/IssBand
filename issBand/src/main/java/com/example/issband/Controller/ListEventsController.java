package com.example.issband.Controller;

import com.example.issband.Domain.Event;
import com.example.issband.Domain.Band;
import com.example.issband.Repository.SQLEventRepository;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class ListEventsController {

    @FXML
    private TableView<Event> eventTableView;

    @FXML
    private TableColumn<Event, Integer> idColumn;

    @FXML
    private TableColumn<Event, String> nameColumn;

    @FXML
    private TableColumn<Event, String> dateColumn;

    @FXML
    private TableColumn<Event, String> venueColumn;

    @FXML
    private Button selectButtonList;

    private final SQLEventRepository eventRepository = new SQLEventRepository("iss.db");

    @FXML
    public void initialize() {
        populateTableView();
        idColumn.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getId()));
        nameColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getName()));
        dateColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDate()));
        venueColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getVenue()));
    }

    private void populateTableView() {
        // Retrieve events from the repository
        List<Event> events = eventRepository.getAllEvents();

        // Clear existing items in the TableView
        eventTableView.getItems().clear();

        // Set the list of events as the items for the TableView
        eventTableView.getItems().addAll(events);
    }

    @FXML
    private void handleSelectButtonPressed(ActionEvent event) {
        Event selectedEvent = eventTableView.getSelectionModel().getSelectedItem();
        if (selectedEvent != null) {
            try {
                // Retrieve the event along with associated bands
                selectedEvent.setBands(eventRepository.getBandsForEvent(selectedEvent.getId()));
                selectedEvent.setTickets(eventRepository.getTicketsForEvent(selectedEvent.getId()));
                selectedEvent.setAudience(eventRepository.getAudience(selectedEvent.getId()));

                // Load the FXML for the event details window
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/issband/EventDetails.fxml"));
                Parent root = loader.load();

                // Get the controller for the event details window
                EventDetailsController controller = loader.getController();
                controller.setEventDetails(selectedEvent);

                // Set up the stage for the event details window
                Stage stage = new Stage();
                stage.setTitle("Event Details");
                stage.setScene(new Scene(root));
                controller.setDialogStage(stage); // Set the dialog stage here

                // Show the event details window
                stage.showAndWait();
            } catch (SQLException | IOException e) {
                e.printStackTrace();
            }
        }
    }

}
