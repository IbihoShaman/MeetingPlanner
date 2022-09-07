package com.example.meetingplanner_demo;

import java.sql.Connection;
import java.sql.DriverManager;

public class DB {
    // creates a connection to the database
    public Connection getConnection(){
        Connection conn;
        try{
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/meetings", "root", "root");
            System.out.println("Connected to database");
            return conn;
        }catch (Exception e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
