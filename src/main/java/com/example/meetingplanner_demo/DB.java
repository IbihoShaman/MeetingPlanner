package com.example.meetingplanner_demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    private String connectionString;

    public DB (String connectionString){
        this.connectionString = connectionString;
    }

    // creates a connection to the database
    public Connection getConnection(){
        String DBUser = configurationLogic.getConfiguration("DBUser");
        String DBPassword = configurationLogic.getConfiguration("DBPassword");
        Connection conn;
        try{
            conn = DriverManager.getConnection(connectionString, DBUser, DBPassword);
            Main.logger.trace("Connection to database successful");
            return conn;
        }catch (Exception e){
            Main.logger.fatal("Database connection failed");
            System.out.println(e.getMessage());
            return null;
        }
    }
}
