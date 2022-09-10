package com.example.meetingplanner_demo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.itextpdf.layout.element.Cell;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.UnitValue;

import java.io.FileNotFoundException;
import java.io.IOException;
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
    private Label labelPdf;
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

    DB database = new DB();
    private Meetings selectedMeeting;

    //populates the table view on launch
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //getConnection();
        showMeetings();
    }



    public static final String LOREM_IPSUM_TEXT = "Lorem ipsum dolor sit amet, consectetur adipisici elit, sed eiusmod tempor incidunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquid ex ea commodi consequat. Quis aute iure reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint obcaecat cupiditat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.";
    public static final String TARGET_PDF = "Meeting.pdf";

    public void generatePdf() throws IOException {
        String modifier = "generatePdf";
        switch (formValidator(modifier)){
            case 0:
                PdfWriter writer = new PdfWriter(TARGET_PDF);
                PdfDocument pdf = new PdfDocument(writer);
                Document document = new Document(pdf);

                Paragraph meetingHeader = new Paragraph(selectedMeeting.getTitle() + " - Meeting Overview")
                        .setFont(PdfFontFactory.createFont(StandardFonts.COURIER))
                        .setFontSize(24)
                        .setBold()
                        .setPaddingLeft(50);
                document.add(meetingHeader);
                Paragraph meetingSchedule = new Paragraph("Meeting kick-off: " + selectedMeeting.getStart() +
                                                                "\n Meeting end: " + selectedMeeting.getEnd())
                        .setBold()
                        .setFontSize(16);
                document.add(meetingSchedule);

                Paragraph meetingAgendaHeader = new Paragraph("Meeting Agenda: \n")
                        .setBold()
                        .setFontSize(16);
                document.add(meetingAgendaHeader);
                document.add(new Paragraph(selectedMeeting.getAgenda()).setFontSize(14));

                Paragraph listHeader = new Paragraph("Meeting Notes")
                        .setFontSize(16)
                        .setBold();
                List list = new List()
                        .setSymbolIndent(12)
                        .setListSymbol("\u2022")
                        .setFontSize(14);
                ObservableList<Notes> listNotes = getNotesList(selectedMeeting.getID());
                if(listNotes.isEmpty()){
                    list.add(new ListItem("No notes appended to this meeting yet"));
                } else {
                    listNotes.forEach((Notes) -> {
                        list.add(Notes.getNoteText());
                    });
                }
                document.add(listHeader);
                document.add(list);

                //document.add(new AreaBreak());
                document.close();
                labelPdf.setText("PDF successfully created");
                labelPdf.setTextFill(Color.color(0, 0.9, 0.2));
                break;
            case -1:
                labelPdf.setText("Select a meeting from the table below first!");
                labelPdf.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                Main.logger.error("Default case log at generatePdf(); case: " + formValidator(modifier));
                break;
        }
    }

    private static Cell getHeaderCell(String s) {
        return new Cell().add(new Paragraph(s)).setBold().setBackgroundColor(ColorConstants.GRAY);
    }



    //reads all meeting entries in the database into an observable list and returns it
    public ObservableList<Meetings> getMeetingList(){
        ObservableList<Meetings> meetingList = FXCollections.observableArrayList();
        Connection conn = database.getConnection();
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
            Main.logger.error("Could not fetch meetings from database");
            e.printStackTrace();
        }
        Main.logger.trace("Meeting list successfully filled with meetings");
        return meetingList;
    }

    //reads all note entries in the database with correct parent ID into an observable list and returns it
    public ObservableList<Notes> getNotesList(int parentID){
        ObservableList<Notes> notesList = FXCollections.observableArrayList();
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
            Main.logger.error("Could not fetch notes from database");
            e.printStackTrace();
        }
        Main.logger.trace("Note list successfully filled with notes");
        return notesList;
    }
///////////////////////////////////Meeting functionality/////////////////////////////////////

    //prints the data of all meetings from the observable list into the correct columns of the table view
    public void showMeetings(){
        ObservableList<Meetings> list = getMeetingList();
        colID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colStart.setCellValueFactory(new PropertyValueFactory<>("start"));
        colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));

        tableMeetings.setItems(list);
        Main.logger.trace("Meeting table populated");
    }


    //creates a meeting and calls an update for the table view data if creation was successful
    public void createMeeting() throws SQLException {
        String modifier = "createMeeting";
        switch (formValidator(modifier)){
            //form filled out correctly, meeting created in database, table view updated
            case 0:
                int currentID = 0;
                String formattedStartDate = formatStart();
                String formattedEndDate = formatEnd();
                //insert data into meetinglist table (notes added to different table)
                String query = "INSERT INTO meetinglist (title, start, end, agenda) VALUES ('" + inputTitle.getText() + "','" + formattedStartDate + "','" + formattedEndDate + "','" + inputAgenda.getText() + "')";
                execute(query);
                ////Adding appended note to meetingNotes table/////
                //get meeting ID to which the note will be appended (suboptimal solution might change later)
                Connection conn = database.getConnection();
                query = "SELECT meetingID FROM meetinglist WHERE title = '" + inputTitle.getText() + "'";
                Statement statement;
                try {
                    statement = conn.createStatement();
                    ResultSet result;
                    result = statement.executeQuery(query);
                    while (result.next()) {
                        currentID = result.getInt(1);
                    }
                }catch (Exception e){
                    Main.logger.error("Error fetching meetingID in createMeeting()");
                    e.printStackTrace();
                }
                //insert note with corresponding meeting ID
                if(!inputNote.getText().isEmpty()) {
                    query = "INSERT INTO meetingnotes (noteText, meetingID) VALUES ('" + inputNote.getText() + "','" + currentID + "')";
                    execute(query);
                }
                labelForm.setText("Meeting successfully created");
                labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                Main.logger.trace("Meeting added to database");
                break;
            //form not filled out correctly
            case -1:
                labelForm.setText("Error! One or more fields are not filled out");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            case -2:
                labelForm.setText("Error! ID field filled incorrectly");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                Main.logger.error("Default case log at createMeeting(); case: " + formValidator(modifier));
                break;
        }
    }
    // updates the info of a meeting based on the entered ID and meeting data
    public void updateMeeting() throws SQLException {
        String modifier = "updateMeeting";
        switch (formValidator(modifier)){
            //filled out correctly, meeting updated, table view updated
            case 0:
                String formattedStartDate = formatStart();
                String formattedEndDate = formatEnd();
                String query = "UPDATE meetinglist SET title = '" + inputTitle.getText() + "', start = '" + formattedStartDate + "', end = '" + formattedEndDate + "', agenda = '" + inputAgenda.getText() +
                        "' WHERE meetingID = " + inputID.getText();
                if(!checkRows(query)){
                    labelForm.setText("No meeting with given ID found");
                    labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                } else {
                    showMeetings();
                    labelForm.setText("Meeting successfully updated");
                    labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                }
                break;
            //ID not filled in, meeting not updated
            case -1:
                labelForm.setText("Error! ID field filled incorrectly");
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
                Main.logger.error("Default case log at updateMeeting()");
                break;
        }
    }
    //deleted a meeting based on entered meeting ID
    public void deleteMeeting(){
        String modifier = "deleteMeeting";
        switch (formValidator(modifier)){
            //meeting is deleted from db, table view updated
            case 0:
                String query = "DELETE FROM meetingNotes WHERE meetingID = " + inputID.getText();
                execute(query);
                query = "DELETE FROM meetinglist WHERE meetingID = " + inputID.getText();
                execute(query);
                showMeetings();
                labelForm.setText("Meeting successfully deleted");
                labelForm.setTextFill(Color.color(0, 0.9, 0.2));
                break;
            //user did not fill ID input, meeting is not deleted
            case -1:
                labelForm.setText("Error! ID field filled incorrectly");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                break;
            default:
                labelForm.setText("Something went wrong");
                labelForm.setTextFill(Color.color(1, 0.1, 0.2 ));
                Main.logger.error("Default case log at deleteMeeting(); case: " + formValidator(modifier));
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
        colNoteID.setCellValueFactory(new PropertyValueFactory<>("noteID"));
        colParentID.setCellValueFactory(new PropertyValueFactory<>("parentMeetingID"));
        colNoteText.setCellValueFactory(new PropertyValueFactory<>("noteText"));

        tableNotes.setItems(list);
    }

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
                Main.logger.error("Default case log at addNote(); case: " + formValidator(modifier));
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
                Main.logger.error("Default case log at updateNote(); case: " + formValidator(modifier));
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
                Main.logger.error("Default case log at deleteNote(); case: " + formValidator(modifier));
                break;
        }
    }


///////////////////////////////Additional functionality//////////////////////////////////////

    private void execute(String query) {
        Connection connection = database.getConnection();
        Statement statement;
        try {
            statement = connection.createStatement();
            statement.executeUpdate(query);
        }catch(Exception e){
            Main.logger.fatal("Query execution failed");
            e.printStackTrace();
        }
        Main.logger.trace("Query execution successful");
        showMeetings();
    }
    
    private int formValidator(String modifier){
        int answerCode = 0;
        switch (modifier) {
            case "createMeeting":
                if (inputTitle.getText().isEmpty()) {
                    answerCode = -1;
                } else if (inputStart.getValue() == null) {
                    answerCode = -1;
                } else if (inputEnd.getValue() == null) {
                    answerCode = -1;
                } else if (inputAgenda.getText().isEmpty()) {
                    answerCode = -1;
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
                Main.logger.error("Default case log at form Validator(); case: " + modifier);
                break;
        }
        Main.logger.trace("Modifier passed through form validator with answerCode: " + answerCode + " on case: " + modifier);
        return answerCode;
    }

    public boolean checkRows(String query) throws SQLException {
        int rows;
        Connection conn = database.getConnection();
        Statement statement;
        statement = conn.createStatement();
        rows = statement.executeUpdate(query);
        return rows != 0;
    }
    public Boolean checkInt(String input){
        try{
            Integer.parseInt(input);
        }catch(NumberFormatException nfe){
            return false;
        }
        return true;
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