package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.DataAccessLayer.DB;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class crudLogic {
    public Logger crudLogicLogger = LogManager.getLogger(crudLogic.class.getName());
    private final appLogic logicApp = new appLogic();

    public void createMeeting(TextField title, DatePicker startDate, TextField startTime, DatePicker endDate,
                              TextField endTime, TextArea agenda, TextArea note, DB database){
        String formattedStartDate = logicApp.parseDateToString(startDate.getValue());
        String formattedEndDate = logicApp.parseDateToString(endDate.getValue());
        //insert data into meetinglist table (notes added to different table)
        String query = "INSERT INTO meetinglist (title, startDate, startTime, endDate, endTime, agenda) VALUES ('" + title.getText() + "','" + formattedStartDate + "','" + startTime.getText()
                + "','" + formattedEndDate + "','" + endTime.getText() + "','" + agenda.getText() + "')";
        database.addMeeting(query, title.getText(), note.getText());

        crudLogicLogger.trace("Meeting added to database");
    }
    public boolean updateMeeting(TextField ID, TextField title, DatePicker start, TextField startTime,
                              DatePicker end, TextField endTime, TextArea agenda, DB database) throws SQLException {
        String formattedStartDate = logicApp.parseDateToString(start.getValue());
        String formattedEndDate = logicApp.parseDateToString(end.getValue());
        String query = "UPDATE meetinglist SET title = '" + title.getText() + "', startDate = '" + formattedStartDate + "', startTime = '" + startTime.getText() + "', endDate = '" + formattedEndDate + "', endTime = '" + endTime.getText() + "', agenda = '" + agenda.getText() +
                "' WHERE meetingID = " + ID.getText();
        return (database.updateMeeting(query));
    }
    public void deleteMeeting(TextField inputID, DB database){
        String queryNote = "DELETE FROM meetingNotes WHERE meetingID = " + inputID.getText();
        String queryMeeting = "DELETE FROM meetinglist WHERE meetingID = " + inputID.getText();
        database.deleteMeeting(queryNote, queryMeeting);
    }

    public int crudMeetingValidator(String modifier, TextField inputID, TextField title, DatePicker start, TextField startTime,
                                    DatePicker end, TextField endTime, TextArea agenda, TextArea note)
    {
        int answerCode = 0;
        switch (modifier){
            case "createMeeting":
                if(title.getText().isEmpty() || start.getValue() == null || end.getValue() == null
                        || endTime.getText().isEmpty() || startTime.getText().isEmpty() || agenda.getText().isEmpty()) {
                    answerCode = -1;
                } else if(!logicApp.parseTime(startTime.getText()) || !logicApp.parseTime(endTime.getText())){
                    answerCode = -2;
                }
                break;
            case "updateMeeting":
                if(!logicApp.checkInt(inputID.getText())){
                    answerCode = -1;
                } else if (title.getText().isEmpty() || start.getValue() == null || startTime.getText().isEmpty() || end.getValue() == null ||
                            endTime.getText().isEmpty() || agenda.getText().isEmpty()) {
                    answerCode = -2;
                } else if (!note.getText().isEmpty()) {
                    answerCode = -3;
                } else if(!logicApp.parseTime(startTime.getText()) || !logicApp.parseTime(endTime.getText())){
                    answerCode = -4;
                }
                break;
            case "deleteMeeting":
                if(!logicApp.checkInt(inputID.getText())){
                    answerCode = -1;
                }
                break;
            default:
                break;
        }
        return answerCode;
    }
}
