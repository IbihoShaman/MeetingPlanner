package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.DataAccessLayer.dbAccessMeetings;
import com.example.meetingplanner_demo.DataAccessLayer.dbAccessNotes;
import com.example.meetingplanner_demo.Model.Meetings;
import com.example.meetingplanner_demo.Model.Notes;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.List;
import com.itextpdf.layout.element.ListItem;
import com.itextpdf.layout.element.Paragraph;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;

public class appLogic {
    //DB database = new DB(configurationLogic.getConfiguration("connectionString"));
    private final dbAccessMeetings DBMeetings = new dbAccessMeetings();
    private final dbAccessNotes DBNotes = new dbAccessNotes();

    public dbAccessMeetings getDBMeetings() {
        return DBMeetings;
    }
    public dbAccessNotes getDBNotes() { return  DBNotes; }

    private final Logger appLogicLogger = LogManager.getLogger(appLogic.class.getName());

    private Meetings selectedMeeting;
    private int selectedID;
    public void setSelectedID(int selectedID){ this.selectedID = selectedID; }
    public int getSelectedID(){ return this.selectedID; }

    public void setSelectedMeeting(Meetings selectedMeeting){ this.selectedMeeting = selectedMeeting; }
    public Meetings getSelectedMeeting(){ return this.selectedMeeting; }


    //creates an observable list, calls database method to fill the list with entries and returns it
    public ObservableList<Meetings> getMeetingList() {
        ObservableList<Meetings> meetingList = FXCollections.observableArrayList();
        DBMeetings.fetchMeetingData(meetingList);
        return meetingList;
    }
    public ObservableList<Notes> getNotesList(int parentID){
        ObservableList<Notes> notesList = FXCollections.observableArrayList();
        DBNotes.fetchNoteData(parentID, notesList);
        return notesList;
    }

    //generates a PDF report based on provided meeting object data
    public int generatePdf() throws IOException {
        String TARGET_PDF = configurationLogic.getConfiguration("PdfName");
        String modifier = "generatePdf";
        int resultCode = appValidator(modifier);
        switch (resultCode){
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
                Paragraph meetingSchedule = new Paragraph("Meeting kick-off: " + selectedMeeting.getStartDate() + " " + selectedMeeting.getStartTime() +
                        "\n Meeting end: " + selectedMeeting.getEndDate() + " " + selectedMeeting.getEndTime())
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
                    listNotes.forEach((Notes) -> list.add(Notes.getNoteText()));
                }
                document.add(listHeader);
                document.add(list);

                //document.add(new AreaBreak());
                document.close();
                break;
            case -1:
                appLogicLogger.trace("PDF generation unsuccessful - user did not select meeting first");
                break;
            default:
                appLogicLogger.error("Default case log at generatePdf(); case: " + appValidator(modifier));
                break;
        }
        return resultCode;
    }

    public int appValidator(String modifier){
        int answerCode = 0;
        switch (modifier){
            case "generatePdf":
                if(selectedMeeting == null){
                    answerCode = -1;
                }
                break;
            default:
                appLogicLogger.error("Default case log at formValidator(); case: " + modifier);
                break;
        }
        return answerCode;
    }

    public LocalDate parseDate(String date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return LocalDate.parse(date, formatter);
    }
    public String parseDateToString(LocalDate date){
        return date.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    public boolean parseTime(String time){
        try {
            DateTimeFormatter strictTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")
                    .withResolverStyle(ResolverStyle.STRICT);
            LocalTime.parse(time, strictTimeFormatter);
            System.out.println("Valid time string: " + time);
            return true;
        } catch (DateTimeParseException | NullPointerException e) {

            appLogicLogger.info("Invalid time input in parseTime()");
            System.out.println("Invalid time string: " + time);
        }
        return false;
    }

    public Boolean checkInt(String input){
        try{
            Integer.parseInt(input);
        }catch(NumberFormatException nfe){
            return false;
        }
        return true;
    }
}
