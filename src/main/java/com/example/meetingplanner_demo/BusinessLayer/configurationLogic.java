package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.Main;

import java.io.FileReader;
import java.util.Properties;

public class configurationLogic {

    public static String getConfiguration(String confName) {
        try (
            FileReader reader = new FileReader("src/config.properties")) {
            Properties properties = new Properties();
            properties.load(reader);
            //System.out.println(properties.getProperty("confName"));
            return properties.getProperty(confName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Main.logger.fatal("Error fetching configuration file data");
        return "";
    }
}
