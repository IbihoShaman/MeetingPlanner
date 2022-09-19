package com.example.meetingplanner_demo.DataAccessLayer;

import com.example.meetingplanner_demo.BusinessLayer.configurationLogic;
import com.example.meetingplanner_demo.Main;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {
    private final String connectionString;
    public static Logger DBlogger = LogManager.getLogger(Main.class.getName());

    public DB(String connectionString){
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
    void execute(String query) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch(Exception e){
            DBlogger.fatal("Query execution failed");
            e.printStackTrace();
        }
        DBlogger.trace("Query execution successful");
    }

    public boolean checkUpdate(String query) throws SQLException {
        int rows;
        Connection conn = getConnection();
        Statement statement;
        statement = conn.createStatement();
        rows = statement.executeUpdate(query);
        return rows == 1;
    }
}

