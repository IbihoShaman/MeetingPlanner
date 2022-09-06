package com.example.meetingplanner_demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;

import java.sql.*;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private TextField inputID;
    @FXML
    private TextField inputTitle;
    @FXML
    private DatePicker inputStart;
    @FXML
    private DatePicker inputEnd;
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

    Meetings selectedMeeting;


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

    //reads all meeting entries in the database into an observable list and returns it
    public ObservableList<Meetings> getMeetingList(){
        ObservableList<Meetings> meetingList = FXCollections.observableArrayList();
        Connection conn = getConnection();
        String query = "SELECT * FROM meetinglist";
        Statement statement;
        ResultSet results;
        try{
            statement = conn.createStatement();
            results = statement.executeQuery(query);
            Meetings meeting;
            while(results.next()){
                meeting = new Meetings(results.getInt("meetingID"), results.getString("title"), results.getString("start"), results.getString("end"), results.getString("agenda"));
                meetingList.add(meeting);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return meetingList;
    }

    //reads all note entries in the database with correct parent ID into an observable list and returns it
    public ObservableList<Notes> getNotesList(int parentID){
        ObservableList<Notes> notesList = FXCollections.observableArrayList();
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
            e.printStackTrace();
        }
        return notesList;
    }

    //populates the table view on launch
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //getConnection();
        showMeetings();
    }

///////////////////////////////////Meeting functionality/////////////////////////////////////

    //prints the data of all meetings from the observable list into the correct columns of the table view
    public void showMeetings(){
        ObservableList<Meetings> list = getMeetingList();
        colID.setCellValueFactory(new PropertyValueFactory<Meetings, Integer>("ID"));
        colTitle.setCellValueFactory(new PropertyValueFactory<Meetings, String>("title"));
        colStart.setCellValueFactory(new PropertyValueFactory<Meetings, String>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<Meetings, String>("end"));
        //colNote.setCellValueFactory(new PropertyValueFactory<Meetings, String>("note"));

        tableMeetings.setItems(list);
    }


    //creates a meeting and calls an update for the table view data if creation was successful
    public void createMeeting() throws SQLException {
        String modifier = "create";
        switch (checkForm(modifier)){
            //form filled out correctly, meeting created in database, table view updated
            case 0:
                labelForm.setText("Meeting successfully created");
                labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                String formattedStartDate = formatStart();
                String formattedEndDate = formatEnd();
                //insert data into meetinglist table (notes addded to different table)
                String query = "INSERT INTO meetinglist (title, start, end, agenda) VALUES ('" + inputTitle.getText() + "','" + formattedStartDate + "','" + formattedEndDate + "','" + inputAgenda.getText() + "')";
                execute(query);

                ////Adding appended note to meetingnotes table
                //get meeting ID to which the note will be appended (suboptimal solution might change later)
                Connection conn = getConnection();
                query = "SELECT meetingID FROM meetinglist WHERE title = '" + inputTitle.getText() + "'";
                Statement statement;
                statement = conn.createStatement();
                ResultSet result;
                result = statement.executeQuery(query);
                int currentID = 0;
                if(result.next()) {
                    currentID = result.getInt(1);
                }
                //insert note with corresponding meeting ID
                query = "INSERT INTO meetingnotes (noteText, meetingID) VALUES ('" + inputNote.getText() + "','" + currentID + "')";
                execute(query);
                break;
            //form not filled out correctly
            case -1:
                labelForm.setText("Error! One or more fields are not filled out");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
        }
    }
    //deleted a meeting based on entered meeting ID
    public void deleteMeeting(){
        String modifier = "delete";
        switch (checkForm(modifier)){
            //meeting is deleted from db, table view updated
            case 0:
                labelForm.setText("Meeting successfully deleted");
                labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                String query = "DELETE FROM meetingNotes WHERE meetingID = " + inputID.getText();
                execute(query);
                query = "DELETE FROM meetinglist WHERE meetingID = " + inputID.getText();
                execute(query);
                showMeetings();
                break;
            //user did not fill ID input, meeting is not deleted
            case -1:
                labelForm.setText("Error! ID field not filled out");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
        }
    }
    // updates the info of a meeting based on the entered ID and meeting data
    public void updateMeeting(){
        String modifier = "update";
        switch (checkForm(modifier)){
            //filled out correctly, meeting updated, table view updated
            case 0:
                labelForm.setText("Meeting successfully updated");
                labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                String formattedStartDate = formatStart();
                String formattedEndDate = formatEnd();
                String query = "UPDATE meetinglist SET title = '" + inputTitle.getText() + "', start = '" + formattedStartDate + "', end = '" + formattedEndDate + "', agenda = '" + inputAgenda.getText() +
                        "' WHERE meetingID = " + inputID.getText();
                execute(query);
                /*if(!inputNote.getText().isEmpty()) {
                    query = "UPDATE meetingnotes SET noteText = '" + inputNote.getText() + "' WHERE meetingID = " + inputID.getText();
                    execute(query);
                }*/
                showMeetings();
                break;
            //ID not filled in, meeting not updated
            case -1:
                labelForm.setText("Error! ID field not filled out");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            //ID filled but form data missing, meeting not updated
            case -2:
                labelForm.setText("Error! One or more input fields not filled out");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
        }
    }

    public void displayMeetingData(){
        selectedMeeting = tableMeetings.getSelectionModel().getSelectedItem();

        inputID.setText("" + selectedMeeting.getID());
        inputTitle.setText(selectedMeeting.getTitle());
        inputStart.setValue(parseDate(selectedMeeting.getStart()));
        inputEnd.setValue(parseDate(selectedMeeting.getEnd()));
        inputAgenda.setText(selectedMeeting.getAgenda());

        labelTitle.setText("TITLE : " + selectedMeeting.getTitle());
        labelStart.setText("FROM : " + selectedMeeting.getStart());
        labelEnd.setText("TO : " + selectedMeeting.getEnd());
        labelAgenda.setText("AGENDA : \n" + selectedMeeting.getAgenda());

        showNotes(selectedMeeting.getID());
    }

////////////////////////Note functionality///////////////////////////

    public void showNotes(int parentID){
        ObservableList<Notes> list = getNotesList(parentID);
        colNoteID.setCellValueFactory(new PropertyValueFactory<Notes, Integer>("noteID"));
        colParentID.setCellValueFactory(new PropertyValueFactory<Notes, Integer>("parentMeetingID"));
        colNoteText.setCellValueFactory(new PropertyValueFactory<Notes, String>("noteText"));

        tableNotes.setItems(list);
    }

    public void displayNoteData(){
        Notes selectedNote = tableNotes.getSelectionModel().getSelectedItem();

        inputNoteID.setText("" + selectedNote.getNoteID());
        inputNoteOverview.setText(selectedNote.getNoteText());
    }

    public void addNote(){
        String modifier = "createNote";
        switch (checkForm(modifier)){
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
                break;
        }

    }
    public void updateNote(){

    }
    public void deleteNote(){

    }


///////////////////////////////Additional funtionality//////////////////////////////////////

    private void execute(String query) {
        Connection connection = getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch(Exception e){
            e.printStackTrace();
        }
        System.out.println("Table populated");
        showMeetings();
    }
    
    private int checkForm(String modifier){
        int answerCode = 0;
        switch (modifier) {
            case "create":
                if (inputTitle.getText().isEmpty()) {
                    answerCode--;
                } else if (inputStart.getValue() == null) {
                    answerCode--;
                } else if (inputEnd.getValue() == null) {
                    answerCode--;
                } else if (inputAgenda.getText().isEmpty()) {
                    answerCode--;
                }
                break;
            case "delete":
                if(inputID.getText().isEmpty())
                    answerCode--;
                break;
            case "update":
                if(inputID.getText().isEmpty())
                    answerCode -= 1;
                else if (inputTitle.getText().isEmpty()) {
                    answerCode = -2;
                } else if (inputStart.getValue() == null) {
                    answerCode = -2;
                } else if (inputEnd.getValue() == null) {
                    answerCode = -2;
                } else if (inputAgenda.getText() == null) {
                    answerCode = -2;
                }
            case "createNote":
                if(selectedMeeting == null){
                    answerCode = -1;
                } else if(inputNoteOverview.getText().isEmpty()){
                    answerCode = -2;
                } else if(inputNoteOverview.getText().length() > 200){
                    answerCode = -3;
                }
            default:
                break;
        }
        return answerCode;
    }

    public LocalDate parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }
    public String formatStart(){
        LocalDate startDate = inputStart.getValue();
        return startDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    public String formatEnd(){
        LocalDate endDate = inputEnd.getValue();
        return endDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
}