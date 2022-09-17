package com.example.meetingplanner_demo.DataAccessLayer;

import com.example.meetingplanner_demo.Main;
import com.example.meetingplanner_demo.BusinessLayer.configurationLogic;
import com.example.meetingplanner_demo.Models.Meetings;
import com.example.meetingplanner_demo.Models.Notes;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class DB {
    private final String connectionString;
    public static Logger DBlogger = LogManager.getLogger(Main.class.getName());

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
    private void execute(String query) {
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

    public void fetchMeetingData(ObservableList<Meetings> meetingList){
        Connection conn = getConnection();
        String query = "SELECT * FROM meetinglist";
        Statement statement;
        ResultSet results;
        try{
            statement = conn.createStatement();
            results = statement.executeQuery(query);
            Meetings meeting;
            while(results.next()){
                meeting = new Meetings(results.getInt("meetingID"), results.getString("title"), results.getString("startDate"), results.getString("startTime"), results.getString("endDate"),
                        results.getString("endTime"), results.getString("agenda"));
                meetingList.add(meeting);
            }
        }catch(Exception e){
            DBlogger.error("Could not fetch meetings from database");
            e.printStackTrace();
        }
        DBlogger.trace("Meeting list successfully filled with meetings");
    }

    public void fetchNoteData(int parentID, ObservableList<Notes> notesList){
        Connection conn = getConnection();
        String query = "SELECT * FROM meetingnotes WHERE meetingID = " + parentID;
        Statement statement;
        ResultSet results;
        try{
            statement = conn.createStatement();
            results = statement.executeQuery(query);
            Notes note;
            while(results.next()){
                //System.out.println("Note Text -----> " + results.getString("noteText"));
                note = new Notes(results.getInt("noteID"), results.getInt("meetingID"), results.getString("noteText"));
                notesList.add(note);
            }
        }catch(Exception e){
            DBlogger.error("Could not fetch notes from database");
            e.printStackTrace();
        }
        DBlogger.trace("Note list successfully filled with notes");
    }

    public void addMeeting(String query, String title, String note){
        //adds meeting into meetingList DB table
        execute(query);
        //if user entered a note, insert it with corresponding meeting ID
        if(!note.isBlank()) {
            int currentID = 0;
            //get meeting ID to which the note will be appended (suboptimal solution might change later)
            Connection conn = getConnection();
            query = "SELECT meetingID FROM meetinglist WHERE title = '" + title + "'";
            Statement statement;
            try {
                statement = conn.createStatement();
                ResultSet result;
                result = statement.executeQuery(query);
                while (result.next()) {
                    currentID = result.getInt(1);
                }
            }catch (Exception e){
                DBlogger.error("Error fetching meetingID in createMeeting()");
                e.printStackTrace();
            }
            // inserts note into noteList table
            query = "INSERT INTO meetingnotes (noteText, meetingID) VALUES ('" + note + "','" + currentID + "')";
            execute(query);
        }
    }
    public boolean updateMeeting(String query) throws SQLException {
        int rows;
        Connection conn = getConnection();
        Statement statement;
        statement = conn.createStatement();
        rows = statement.executeUpdate(query);
        return rows == 1;
    }
    public void deleteMeeting(String queryNote, String queryMeeting){
        //deleting the entries from the note table first
        execute(queryNote);
        //deleting the parent meeting
        execute(queryMeeting);
    }
}

