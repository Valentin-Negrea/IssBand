package com.example.issband.Controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ButtonsController {

    @FXML
    public void handleAttendAsBand() {
        try {
            // Load the FXML file for attending as a band
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/issband/AttendAsBand.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Attend as Band");
            stage.setScene(new Scene(root));

            // Get the controller and set the dialog stage
            AttendAsBandController controller = fxmlLoader.getController();
            controller.setDialogStage(stage); // Make sure this line is executed

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleBuyTicket() {
        try {
            // Load the FXML file for buying a ticket
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/issband/BuyTicket.fxml"));
            Parent root = fxmlLoader.load();

            // Create a new stage and set the scene
            Stage stage = new Stage();
            stage.setTitle("Buy Ticket");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
