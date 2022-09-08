package com.example.meetingplanner_demo;

import java.io.FileReader;
import java.util.Properties;
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
        try (FileReader reader = new FileReader("config")){
            Properties properties = new Properties();
            properties.load(reader);
            //String string = properties.getProperty("variable");
            //System.out.println(string);
        } catch (Exception e){
            e.printStackTrace();
        }
        launch();
    }
}