package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.Model.Meetings;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class crudLogicMeetings {
    public Logger crudMeetingsLogger = LogManager.getLogger(crudLogicMeetings.class.getName());
    private final appLogic logicApp = new appLogic();

    public int createMeeting(String modifier, Meetings newMeeting, TextArea note){
        int answerCode = 0;
        int validatorCode = crudMeetingValidator(modifier,null, newMeeting, note);
        switch (validatorCode){
            case 0:
                //insert data into meetinglist table (notes added to different table)
                String query = "INSERT INTO meetinglist (title, startDate, startTime, endDate, endTime, agenda) VALUES ('" + newMeeting.getTitle() + "','" + newMeeting.getStartDate() +
                        "','" + newMeeting.getStartTime() + "','" + newMeeting.getEndDate() + "','" + newMeeting.getEndTime() + "','" + newMeeting.getAgenda() + "')";
                logicApp.getDBMeetings().addMeeting(query, newMeeting.getTitle(), note.getText());
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
    public int updateMeeting(String modifier, TextField ID, Meetings newMeeting, TextArea noteText) throws SQLException {
        int validatorCode = crudMeetingValidator(modifier, ID, newMeeting, noteText);
        int answerCode = 0;
        switch (validatorCode){
            case 0:
                String query = "UPDATE meetinglist SET title = '" + newMeeting.getTitle() + "', startDate = '" + newMeeting.getStartDate() +
                        "', startTime = '" + newMeeting.getStartTime() + "', endDate = '" + newMeeting.getEndDate() +
                        "', endTime = '" + newMeeting.getEndTime() + "', agenda = '" + newMeeting.getAgenda() + "' WHERE meetingID = " + ID.getText();
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
        int validatorCode = crudMeetingValidator(modifier, inputID, null, null);
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

    public int crudMeetingValidator(String modifier, TextField inputID, Meetings newMeeting, TextArea note)
    {
        int answerCode = 0;
        switch (modifier){
            case "createMeeting":
                if(newMeeting.getTitle().isEmpty() || newMeeting.getStartDate() == null || newMeeting.getStartTime().isEmpty()
                        || newMeeting.getEndDate() == null || newMeeting.getEndTime().isEmpty() || newMeeting.getAgenda().isEmpty()) {
                    answerCode = -1;
                } else if(!logicApp.parseTime(newMeeting.getStartTime()) || !logicApp.parseTime(newMeeting.getEndTime())){
                    answerCode = -2;
                }
                break;
            case "updateMeeting":
                if(!logicApp.checkInt(inputID.getText())){
                    answerCode = -1;
                } else if (newMeeting.getTitle().isEmpty() || newMeeting.getStartDate() == null || newMeeting.getStartTime().isEmpty()
                        || newMeeting.getEndDate() == null || newMeeting.getEndTime().isEmpty() || newMeeting.getAgenda().isEmpty()) {
                    answerCode = -2;
                } else if (!note.getText().isEmpty()) {
                    answerCode = -3;
                } else if(!logicApp.parseTime(newMeeting.getStartTime()) || !logicApp.parseTime(newMeeting.getEndTime())){
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
