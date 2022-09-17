package com.example.meetingplanner_demo;

import org.apache.logging.log4j.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Logger logger = LogManager.getLogger(Main.class.getName());
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("View/View1.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Meeting Planner 3000©");
        stage.setResizable(false);
        stage.setScene(scene);
        logger.info("Stage setup successful");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}