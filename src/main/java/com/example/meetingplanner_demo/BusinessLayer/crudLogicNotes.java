package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.Model.Meetings;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;

public class crudLogicNotes {
    public Logger crudNotesLogger = LogManager.getLogger(crudLogicNotes.class.getName());
    private final appLogic logicApp = new appLogic();
    private int answerCode = 0;

    public int addNote(String modifier, TextArea noteText, int selectedID, Meetings selectedMeeting){
        int validatorCode = crudNoteValidator(modifier, noteText, null, selectedMeeting, selectedID);
        switch(validatorCode){
            case 0:
                String query = "INSERT INTO meetingnotes (meetingID, noteText) VALUES ('" + selectedID + "','" + noteText.getText() + "')";
                logicApp.getDBNotes().addNote(query);
                answerCode = 0;
                break;
            case -1:
                answerCode = -1;
                break;
            case -2:
                answerCode = -2;
                break;
            case -3:
                answerCode = -3;
            default:
                crudNotesLogger.error("Default case hit at addNote(), case: " + validatorCode);
                break;
        }
        return answerCode;
    }
    public int updateNote(String modifier, TextArea noteText, TextField noteID, Meetings selectedMeeting, int selectedID) throws SQLException {
        int validatorCode = crudNoteValidator(modifier, noteText, noteID, selectedMeeting, selectedID);
        switch (validatorCode){
            case 0:
                String query = "UPDATE meetingnotes SET noteText = '" + noteText.getText() +
                        "' WHERE noteID = " + noteID.getText();
                if(!logicApp.getDBNotes().updateNote(query)){
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
                break;
        }
        return answerCode;
    }
    public int deleteNote(String modifier, TextField noteID, Meetings selectedMeeting, int selectedMeetingID){
        int validatorCode = crudNoteValidator(modifier, null, noteID, selectedMeeting, selectedMeetingID);
         switch (validatorCode){
             case 0:
                 String query = "DELETE FROM meetingnotes WHERE noteID = " + noteID.getText();
                 logicApp.getDBNotes().deleteNote(query);
                 answerCode = 0;
                 break;
             case -1:
                 answerCode = -1;
                 break;
             case -2:
                 answerCode = -2;
                 break;
             default:
                 crudNotesLogger.error("Default case hit at deleteNote(), case: " + validatorCode);
                 break;
         }
        return answerCode;
    }

    public int crudNoteValidator(String modifier, TextArea noteText, TextField noteID, Meetings selectedMeeting, int selectedMeetingID){
        int answerCode = 0;
        switch (modifier){
            case "addNote":
                if(selectedMeeting == null || selectedMeetingID == 0){
                    answerCode = -1;
                } else if(noteText.getText().isEmpty()){
                    answerCode = -2;
                } else if(noteText.getText().length() > 200){
                    answerCode = -3;
                }
                break;
            case "updateNote":
                if(selectedMeeting == null || selectedMeetingID == 0){
                    answerCode = -1;
                } else if(noteText.getText().isEmpty()){
                    answerCode = -2;
                } else if(noteText.getText().length() > 200){
                    answerCode = -3;
                } else if(!logicApp.checkInt(noteID.getText())){
                    answerCode = -4;
                }
                break;
            case "deleteNote":
                if(selectedMeeting == null){
                    answerCode = -1;
                } else if(!logicApp.checkInt(noteID.getText())){
                    answerCode = -2;
                }
                break;
            default:
                crudNotesLogger.error("Default case hit at crudNoteValidator(), case: " + modifier);
                break;
        }
        return answerCode;
    }
}
