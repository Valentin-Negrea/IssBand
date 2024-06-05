package com.example.issband;

import com.example.issband.Repository.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class MainApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
//        SQLAudienceRepository audienceRepository = new SQLAudienceRepository("iss.db");
//        SQLBandRepository bandRepository = new SQLBandRepository("iss.db");
//        SQLManagerRepository managerRepository = new SQLManagerRepository("iss.db");
//        SQLEventRepository eventRepository = new SQLEventRepository("iss.db");
//        SQLTicketRepository ticketRepository = new SQLTicketRepository("iss.db");
//        SQLUserRepository userRepository = new SQLUserRepository("iss.db");


        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("/com/example/issband/UserWindow.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("User Login");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
