package com.example.meetingplanner_demo.ViewModels;

import com.example.meetingplanner_demo.BusinessLayer.appLogic;
import com.example.meetingplanner_demo.BusinessLayer.configurationLogic;
import com.example.meetingplanner_demo.BusinessLayer.crudLogic;
import com.example.meetingplanner_demo.DataAccessLayer.DB;
import com.example.meetingplanner_demo.Models.Meetings;
import com.example.meetingplanner_demo.Models.Notes;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.sql.*;
import java.net.URL;
import java.util.ResourceBundle;

public class Controller1 implements Initializable {
    @FXML
    private Label labelForm;
    @FXML
    private Label labelTitle;
    @FXML
    private Label labelStart;
    @FXML
    private Label labelEnd;
    @FXML
    private Label labelAgenda;
    @FXML
    private Label labelNoteOverview;
    @FXML
    private Label labelPdf;
    @FXML
    private TextField inputID;
    @FXML
    private TextField inputTitle;
    @FXML
    private DatePicker inputStart;
    @FXML
    private TextField inputStartTime;
    @FXML
    private DatePicker inputEnd;
    @FXML
    private TextField inputEndTime;
    @FXML
    private TextArea inputAgenda;
    @FXML
    private TextArea inputNote;
    @FXML
    private TextArea inputNoteOverview;
    @FXML
    private TextField inputNoteID;
    @FXML
    private TableView<Meetings> tableMeetings;
    @FXML
    private TableColumn<Meetings, Integer> colID;
    @FXML
    private TableColumn<Meetings, String> colTitle;
    @FXML
    private TableColumn<Meetings, String> colStart;
    @FXML
    private TableColumn<Meetings, String> colEnd;
    @FXML
    private TableView<Notes> tableNotes;
    @FXML
    private TableColumn<Notes, Integer> colParentID;
    @FXML
    private TableColumn<Notes, Integer> colNoteID;
    @FXML
    private TableColumn<Notes, String> colNoteText;
    //buttons
    @FXML
    private Button buttonCreate;
    @FXML
    private Button buttonUpdate;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonAddNote;
    @FXML
    private Button buttonUpdateNote;
    @FXML
    private Button buttonDeleteNote;
    @FXML
    private Button buttonGeneratePdf;


    private final Logger controllerLogger = LogManager.getLogger(Controller1.class.getName());
    private final appLogic logicApp = new appLogic();
    private final crudLogic logicCrud = new crudLogic();
    DB database = new DB(configurationLogic.getConfiguration("connectionString"));
    //private Meetings selectedMeeting;

    //populates the meeting table view on launch
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showMeetings();
    }

    //calls appLogic function to fetch list of all Meetings from the database and prints them in the Meeting table view
    public void showMeetings(){
        ObservableList<Meetings> list = logicApp.getMeetingList(database);
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));

        tableMeetings.setItems(list);
        controllerLogger.trace("Meeting table populated");
    }
    //calls appLogic function to fetch list of all Notes from the database and prints them in the Note table view
    public void showNotes(int parentID){
        ObservableList<Notes> list = logicApp.getNotesList(parentID, database);
        colNoteID.setCellValueFactory(new PropertyValueFactory<>("noteID"));
        colParentID.setCellValueFactory(new PropertyValueFactory<>("parentMeetingID"));
        colNoteText.setCellValueFactory(new PropertyValueFactory<>("noteText"));

        tableNotes.setItems(list);
    }

    public void generatePdfOnClick() throws IOException {
        int answerCode;
        answerCode = logicApp.generatePdf(database);
        switch (answerCode){
            case 0:
                labelPdf.setText("PDF successfully created");
                labelPdf.setTextFill(Color.color(0, 0.9, 0.2));
                break;
            case -1:
                labelPdf.setText("Select a meeting from the table below first!");
                labelPdf.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelPdf.setText("Something went wrong");
                labelPdf.setTextFill(Color.color(1, 0.1, 0.2 ));
                controllerLogger.error("Default case log at generatePdf(); case: " + answerCode);
                break;
        }
    }
    public void displayMeetingData(){
        logicApp.setSelectedMeeting(tableMeetings.getSelectionModel().getSelectedItem());

        inputID.setText("" + logicApp.getSelectedMeeting().getID());
        inputTitle.setText(logicApp.getSelectedMeeting().getTitle());
        inputStart.setValue(logicApp.parseDate(logicApp.getSelectedMeeting().getStartDate()));
        inputStartTime.setText(logicApp.getSelectedMeeting().getStartTime());
        inputEnd.setValue(logicApp.parseDate(logicApp.getSelectedMeeting().getEndDate()));
        inputEndTime.setText(logicApp.getSelectedMeeting().getEndTime());
        inputAgenda.setText(logicApp.getSelectedMeeting().getAgenda());

        labelTitle.setText("TITLE : " + logicApp.getSelectedMeeting().getTitle());
        labelStart.setText("FROM : " + logicApp.getSelectedMeeting().getStartDate() + " " + logicApp.getSelectedMeeting().getStartTime());
        labelEnd.setText("TO : " + logicApp.getSelectedMeeting().getEndDate() + " " + logicApp.getSelectedMeeting().getEndTime());
        labelAgenda.setText("AGENDA : \n" + logicApp.getSelectedMeeting().getAgenda());

        showNotes(logicApp.getSelectedMeeting().getID());
    }
    public void displayNoteData(){
        Notes selectedNote = tableNotes.getSelectionModel().getSelectedItem();
        inputNoteID.setText("" + selectedNote.getNoteID());
        inputNoteOverview.setText(selectedNote.getNoteText());
    }

    public void createMeetingOnClick(){
        String modifier = "createMeeting";
        int answerCode = logicCrud.crudMeetingValidator(modifier, inputID, inputTitle, inputStart,
                inputStartTime, inputEnd, inputEndTime, inputAgenda, inputNote);
        switch (answerCode){
            case 0:
                logicCrud.createMeeting(inputTitle, inputStart, inputStartTime, inputEnd, inputEndTime, inputAgenda, inputNote, database);
                labelForm.setText("Meeting successfully created");
                labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                controllerLogger.trace("Meeting added to database");
                showMeetings();
                break;
            case -1:
                labelForm.setText("Error! One or more fields are not filled out");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -2:
                labelForm.setText("Error! Incorrect time format entered");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                controllerLogger.error("Default case log at createMeeting(); case: " + answerCode);
                break;
        }
        controllerLogger.trace("Passed through createMeetingOnClick() with code: " + answerCode);
    }
    public void updateMeetingOnClick() throws SQLException {
        String modifier = "updateMeeting";
        int answerCode = logicCrud.crudMeetingValidator(modifier, inputID, inputTitle, inputStart,
                inputStartTime, inputEnd, inputEndTime, inputAgenda, inputNote);
        switch (answerCode){
            case 0:
                if(logicCrud.updateMeeting(inputID, inputTitle, inputStart,
                        inputStartTime, inputEnd, inputEndTime, inputAgenda, database))
                {
                    showMeetings();
                    labelForm.setText("Meeting successfully updated");
                    labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                } else {
                    labelForm.setText("No meeting with given ID found");
                    labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                }
                break;
            case -1:
                labelForm.setText("Error! ID field filled incorrectly");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -2:
                labelForm.setText("Error! One or more fields are not filled out");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -3:
                //user tried to enter a note through the meeting form -> meeting updated but note not added
                logicCrud.updateMeeting(inputID, inputTitle, inputStart, inputStartTime,
                                        inputEnd, inputEndTime, inputAgenda, database);
                showMeetings();
                labelForm.setText("Warning! Note cannot be added in this formulary");
                labelForm.setTextFill(Color.color(1, 0.6 ,0  ));
                break;
            case -4:
                labelForm.setText("Error! Incorrect time format entered");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                controllerLogger.error("Default case log at updateMeeting(); case: " + answerCode);
                break;
        }
        controllerLogger.trace("Passed through updateMeetingOnClick() with code: " + answerCode);
    }
    public void deleteMeetingOnClick(){
        String modifier = "deleteMeeting";
        int answerCode = logicCrud.crudMeetingValidator(modifier, inputID, inputTitle, inputStart,
                inputStartTime, inputEnd, inputEndTime, inputAgenda, inputNote);
        switch (answerCode){
            case 0:
                logicCrud.deleteMeeting(inputID, database);
                break;
            case -1:
                labelForm.setText("Error! ID field filled incorrectly");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                controllerLogger.error("Default case log at createMeeting(); case: " + answerCode);
                break;
        }
        controllerLogger.trace("Passed through deleteMeetingOnClick() with code: " + answerCode);
    }

    public void addNote(){

    }
    public void updateNote(){

    }
    public void deleteNote(){

    }

}

/*
////////////////////////Note functionality///////////////////////////

    public void displayNoteData(){
        Notes selectedNote = tableNotes.getSelectionModel().getSelectedItem();

        inputNoteID.setText("" + selectedNote.getNoteID());
        inputNoteOverview.setText(selectedNote.getNoteText());
    }

    public void addNote(){
        String modifier = "createNote";
        switch (formValidator(modifier)){
            case 0:
                String query = "INSERT INTO meetingnotes (meetingID, noteText) VALUES ('" + selectedMeeting.getID() + "','" + inputNoteOverview.getText() + "')";
                execute(query);
                labelNoteOverview.setText("Note added to meeting ID: " + selectedMeeting.getID());
                labelNoteOverview.setTextFill(Color.color(0, 0.9, 0.2));
                showNotes(selectedMeeting.getID());
                break;
            case -1:
                labelNoteOverview.setText("No meeting selected, select a meeting first");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                 break;
            case -2:
                labelNoteOverview.setText("Input field is empty, cannot add note");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -3:
                labelNoteOverview.setText("Max length of note is 200 characters");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelNoteOverview.setText("Something went wrong");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                logger.error("Default case log at addNote(); case: " + formValidator(modifier));
                break;
        }

    }
    public void updateNote() throws SQLException {
        String modifier = "updateNote";
        switch (formValidator(modifier)){
            case 0:
                String query = "UPDATE meetingnotes SET noteText = '" + inputNoteOverview.getText() +
                        "' WHERE noteID = " + inputNoteID.getText();
                if(!checkRows(query)){
                    labelNoteOverview.setText("No note with given ID found");
                    labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                } else {
                    labelNoteOverview.setText("Note ID: " + inputNoteID.getText() + " updated");
                    labelNoteOverview.setTextFill(Color.color(0, 0.9, 0.2));
                    showNotes(selectedMeeting.getID());
                }
                break;
            case -1:
                labelNoteOverview.setText("No meeting selected, select a meeting first");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -2:
                labelNoteOverview.setText("Input field is empty, cannot update note");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -3:
                labelNoteOverview.setText("Max length of note is 200 characters");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -4:
                labelNoteOverview.setText("ID input is empty, enter correct noteID first");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelNoteOverview.setText("Something went wrong");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                logger.error("Default case log at updateNote(); case: " + formValidator(modifier));
                break;
        }
    }
    public void deleteNote(){
        String modifier = "deleteNote";
        switch (formValidator(modifier)){
            case 0:
                String query = "DELETE FROM meetingnotes WHERE noteID = " + inputNoteID.getText();
                execute(query);
                labelNoteOverview.setText("Note with ID: " + inputNoteID.getText() + " deleted: " + selectedMeeting.getID());
                labelNoteOverview.setTextFill(Color.color(0, 0.9, 0.2));
                showNotes(selectedMeeting.getID());
                break;
            case -1:
                labelNoteOverview.setText("No meeting selected, select a meeting first");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -2:
                labelNoteOverview.setText("ID input filled incorrectly");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelNoteOverview.setText("Something went wrong");
                labelNoteOverview.setTextFill(Color.color(1, 0.1, 0.2 ));
                logger.error("Default case log at deleteNote(); case: " + formValidator(modifier));
                break;
        }
    }


///////////////////////////////Additional functionality//////////////////////////////////////


    
    private int formValidator(String modifier){
        int answerCode = 0;
        switch (modifier) {
            case "createMeeting":
                if(inputTitle.getText().isEmpty() || inputStart.getValue() == null || inputEnd.getValue() == null
                    || inputEndTime.getText().isEmpty() || inputStartTime.getText().isEmpty() || inputTitle.getText().isEmpty()) {
                    answerCode = -1;
                } else if(!parseTime(inputStartTime.getText()) || !parseTime(inputEndTime.getText())){
                    answerCode = -2;
                }
                break;
            case "deleteMeeting":
                if(!checkInt(inputID.getText())){
                    answerCode = -1;
                }
                break;
            case "updateMeeting":
                if(!checkInt(inputID.getText())){
                    answerCode = -1;
                } else if (inputTitle.getText().isEmpty()) {
                    answerCode = -2;
                } else if (inputStart.getValue() == null) {
                    answerCode = -2;
                } else if (inputEnd.getValue() == null) {
                    answerCode = -2;
                } else if (inputAgenda.getText().isEmpty()) {
                    answerCode = -2;
                }
                break;
            case "createNote":
                if(selectedMeeting == null){
                    answerCode = -1;
                } else if(inputNoteOverview.getText().isEmpty()){
                    answerCode = -2;
                } else if(inputNoteOverview.getText().length() > 200){
                    answerCode = -3;
                }
                break;
            case "updateNote":
                if(selectedMeeting == null){
                    answerCode = -1;
                } else if(inputNoteOverview.getText().isEmpty()){
                    answerCode = -2;
                } else if(inputNoteOverview.getText().length() > 200){
                    answerCode = -3;
                } else if(!checkInt(inputNoteID.getText())){
                    answerCode = -4;
                }
                break;
            case "deleteNote":
                if(selectedMeeting == null){
                    answerCode = -1;
                } else if(!checkInt(inputNoteID.getText())){
                    answerCode = -2;
                }
                break;
            case "generatePdf":
                if(selectedMeeting == null){
                    answerCode = -1;
                }
                break;
            default:
                logger.error("Default case log at form Validator(); case: " + modifier);
                break;
        }
        logger.trace("Modifier passed through form validator with answerCode: " + answerCode + " on case: " + modifier);
        return answerCode;
    }
*/