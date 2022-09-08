package com.example.meetingplanner_demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    // creates a connection to the database
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/meetings", "root", "root");
            Main.logger.trace("Connection to database successful");
            return conn;
        }catch (Exception e){
            Main.logger.fatal("Database connection failed");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
