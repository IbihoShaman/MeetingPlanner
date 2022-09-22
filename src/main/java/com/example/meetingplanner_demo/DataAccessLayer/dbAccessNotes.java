package com.example.meetingplanner_demo.DataAccessLayer;

import com.example.meetingplanner_demo.BusinessLayer.configurationLogic;
import com.example.meetingplanner_demo.Model.Notes;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class dbAccessNotes {
    private final DB database = new DB(configurationLogic.getConfiguration("connectionString"));
    public static Logger DBNotesLogger = LogManager.getLogger(dbAccessNotes.class.getName());

    public void fetchNoteData(int parentID, ObservableList<Notes> notesList){
        Connection conn = database.getConnection();
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
            DBNotesLogger.error("Could not fetch notes from database");
            e.printStackTrace();
        }
        DBNotesLogger.trace("Note list successfully filled with notes");
    }

    ///////////Notes
    public void addNote(String query){
        database.execute(query);
    }
    public boolean updateNote(String query) throws SQLException { return database.checkUpdate(query); }
    public void deleteNote(String query){
        database.execute(query);
    }
}
