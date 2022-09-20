package com.example.meetingplanner_demo.DataAccessLayer;

import com.example.meetingplanner_demo.BusinessLayer.configurationLogic;
import com.example.meetingplanner_demo.Main;
import com.example.meetingplanner_demo.Model.Meetings;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbAccessMeetings {
    private final DB database = new DB(configurationLogic.getConfiguration("connectionString"));
    private final dbAccessNotes DBNotes = new dbAccessNotes();
    public static Logger DBMeetingsLogger = LogManager.getLogger(Main.class.getName());

    public void fetchMeetingData(ObservableList<Meetings> meetingList){
        Connection conn = database.getConnection();
        String query = "SELECT * FROM meetinglist";
        Statement statement;
        ResultSet results;
        try{
            statement = conn.createStatement();
            results = statement.executeQuery(query);
            Meetings meeting;
            while(results.next()){
                meeting = new Meetings.meetingsBuilder(results.getString("title"))
                        .ID(results.getInt("meetingID"))
                        .startDate(results.getString("startDate"))
                        .startTime(results.getString("startTime"))
                        .endDate(results.getString("endDate"))
                        .endTime(results.getString("endTime"))
                        .agenda(results.getString("agenda"))
                        .build();
                meetingList.add(meeting);
            }
        }catch(Exception e){
            DBMeetingsLogger.error("Could not fetch meetings from database");
            e.printStackTrace();
        }
        DBMeetingsLogger.trace("Meeting list successfully filled with meetings");
    }

    ////Meetings
    public void addMeeting(String query, String title, String note){
        //adds meeting into meetingList DB table
        database.execute(query);
        //if user entered a note, insert it with corresponding meeting ID
        if(!note.isBlank()) {
            int currentID = 0;
            //get meeting ID to which the note will be appended (suboptimal solution might change later)
            Connection conn = database.getConnection();
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
                DBMeetingsLogger.error("Error fetching meetingID in createMeeting()");
                e.printStackTrace();
            }
            // inserts note into noteList table
            query = "INSERT INTO meetingnotes (noteText, meetingID) VALUES ('" + note + "','" + currentID + "')";
            DBNotes.addNote(query);
        }
    }
    public boolean updateMeeting(String query) throws SQLException {
        return database.checkUpdate(query);
    }
    public void deleteMeeting(String queryNote, String queryMeeting){
        //deleting the entries from the note table first
        database.execute(queryNote);
        //deleting the parent meeting
        database.execute(queryMeeting);
    }

}
