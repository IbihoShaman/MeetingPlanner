package com.example.meetingplanner_demo.BusinessLayer;

import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class crudLogicMeetings {
    public Logger crudMeetingsLogger = LogManager.getLogger(crudLogicMeetings.class.getName());
    private final appLogic logicApp = new appLogic();

    public int createMeeting(String modifier, TextField title, DatePicker startDate, TextField startTime, DatePicker endDate,
                              TextField endTime, TextArea agenda, TextArea note){
        int answerCode = 0;
        int validatorCode = crudMeetingValidator(modifier,null, title, startDate, startTime, endDate, endTime, agenda, note);
        switch (validatorCode){
            case 0:
                String formattedStartDate = logicApp.parseDateToString(startDate.getValue());
                String formattedEndDate = logicApp.parseDateToString(endDate.getValue());
                //insert data into meetinglist table (notes added to different table)
                String query = "INSERT INTO meetinglist (title, startDate, startTime, endDate, endTime, agenda) VALUES ('" + title.getText() + "','" + formattedStartDate + "','" + startTime.getText()
                        + "','" + formattedEndDate + "','" + endTime.getText() + "','" + agenda.getText() + "')";
                logicApp.getDBMeetings().addMeeting(query, title.getText(), note.getText());
                break;
            case -1:
                answerCode = -1;
                break;
            case -2:
                answerCode = -2;
                break;
            default:
                crudMeetingsLogger.error("Default case log at createMeeting(); case: " + validatorCode);
                break;
        }
        crudMeetingsLogger.trace("Passed through createMeeting() with answerCode: " + answerCode);
        return answerCode;
    }
    public int updateMeeting(String modifier, TextField ID, TextField title, DatePicker start, TextField startTime,
                              DatePicker end, TextField endTime, TextArea agenda, TextArea noteText) throws SQLException {
        int validatorCode = crudMeetingValidator(modifier, ID, title, start, startTime, end, endTime, agenda, noteText);
        int answerCode = 0;
        switch (validatorCode){
            case 0:
                String formattedStartDate = logicApp.parseDateToString(start.getValue());
                String formattedEndDate = logicApp.parseDateToString(end.getValue());
                String query = "UPDATE meetinglist SET title = '" + title.getText() + "', startDate = '" + formattedStartDate + "', startTime = '" + startTime.getText() + "', endDate = '" + formattedEndDate + "', endTime = '" + endTime.getText() + "', agenda = '" + agenda.getText() +
                        "' WHERE meetingID = " + ID.getText();
                if(!logicApp.getDBMeetings().updateMeeting(query)){
                    answerCode = -5;
                }
                break;
            case -1:
                answerCode = -1;
                break;
            case -2:
                answerCode = -2;
                break;
            case -3:
                answerCode = -3;
                break;
            case -4:
                answerCode = -4;
                break;
            default:
                crudMeetingsLogger.error("Default case log at updateMeeting(); case: " + validatorCode);
                break;
        }
        return answerCode;
    }
    public int deleteMeeting(String modifier, TextField inputID){
        int validatorCode = crudMeetingValidator(modifier, inputID, null, null,
                null, null, null, null, null);
        int answerCode = 0;
        switch (validatorCode){
            case 0:
                String queryNote = "DELETE FROM meetingNotes WHERE meetingID = " + inputID.getText();
                String queryMeeting = "DELETE FROM meetinglist WHERE meetingID = " + inputID.getText();
                logicApp.getDBMeetings().deleteMeeting(queryNote, queryMeeting);
                break;
            case -1:
                answerCode = -1;
                break;
            default:
                crudMeetingsLogger.error("Default case log at deleteMeeting(); case: " + validatorCode);
                break;
            }
        return answerCode;
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
                crudMeetingsLogger.error("Default case hit at crudMeetingValidator(), case: " + modifier);
                break;
        }
        return answerCode;
    }
}
