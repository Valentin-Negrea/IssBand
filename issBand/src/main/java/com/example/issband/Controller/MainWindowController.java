package com.example.issband.Controller;

import com.example.issband.Domain.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

public class MainWindowController {
    @FXML private TableView<Event> eventsTable;
    @FXML private TableColumn<Event, Integer> eventIdColumn;
    @FXML private TableColumn<Event, String> eventNameColumn;
    @FXML private TableColumn<Event, String> eventDateColumn;
    @FXML private Button attendAsBandButton;
    @FXML private Button buyTicketButton;

    public void initialize() {
        // Initialize the table columns and load event data
    }

    @FXML
    private void handleAttendAsBand() {
        openWindow("AttendAsBand.fxml", "Attend as Band");
    }

    @FXML
    private void handleBuyTicket() {
        openWindow("BuyTicket.fxml", "Buy Ticket");
    }

    private void openWindow(String fxmlFile, String title) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(loader.load()));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
