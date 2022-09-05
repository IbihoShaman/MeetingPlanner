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
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Scanner;

public class Controller1 implements Initializable {
    @FXML
    private Label labelForm;
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
    private Button buttonCreate;
    @FXML
    private Button buttonDelete;
    @FXML
    private Button buttonUpdate;
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
    private TableColumn<Meetings, String> colNote;

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
                meeting = new Meetings(results.getInt("meetingID"), results.getString("title"), results.getString("start"), results.getString("end"));
                meetingList.add(meeting);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return meetingList;
    }

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

    //populates the table view on launch
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //getConnection();
        showMeetings();
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
                String query = "UPDATE meetinglist SET title = '" + inputTitle.getText() + "', start = '" + formattedStartDate + "', end = '" + formattedEndDate +
                        "' WHERE meetingID = " + inputID.getText();
                execute(query);
                //checks if agenda input is empty and updates the note if not
                if(!inputAgenda.getText().isEmpty()) {
                    query = "UPDATE meetinglist SET agenda = '" + inputAgenda.getText() + "' WHERE meetingID = " + inputID.getText();
                    execute(query);
                }
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
        Meetings selectedMeeting = tableMeetings.getSelectionModel().getSelectedItem();

        inputID.setText("" + selectedMeeting.getID());
        inputTitle.setText(selectedMeeting.getTitle());
        inputStart.setValue(parseDate(selectedMeeting, selectedMeeting.getStart()));
        inputEnd.setValue(parseDate(selectedMeeting, selectedMeeting.getEnd()));
        //inputNote.setText(selectedMeeting.getNote());
    }

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
    
    public int checkForm(String modifier){
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
                }
            default:
                break;
        }
        return answerCode;
    }

    public LocalDate parseDate(Meetings meeting, String date){
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