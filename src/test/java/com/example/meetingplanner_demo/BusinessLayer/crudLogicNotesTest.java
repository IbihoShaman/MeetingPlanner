package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.Model.Meetings;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class crudLogicNotesTest {
    private static Meetings testMeeting;
    private final crudLogicNotes logicNotes = new crudLogicNotes();
    private int answerCode;
    //private static TextArea noteText = new TextArea();
    //private static TextField noteID = new TextField();

    @BeforeAll
    static void beforeAll(){
         testMeeting = new Meetings.meetingsBuilder("TestMeeting")
                .build();
    }
    @BeforeAll
    static void initJfxRuntime() {
        Platform.startup(() -> {});
    }

    @Test
    void addNoteValidationTest() {
        TextArea noteText = new TextArea();
        noteText.setText("Adding note from crudLogicNotes unit test");
        TextField noteID = new TextField();
        noteID.setText("1");

        //no meeting selected, return code should be -1
        answerCode = logicNotes.crudNoteValidator("addNote", noteText, noteID, null, 1);
        assertEquals(-1, answerCode);

        //correct addNote parameters
        answerCode = logicNotes.crudNoteValidator("addNote", noteText, noteID, testMeeting, 1);
        assertEquals(0, answerCode);
    }
    @Test
    void updateNoteValidationTest() throws SQLException {
        TextArea noteText = new TextArea();
        TextField noteID = new TextField();
        noteText.setText("CrudLogicNotes unit test");
        noteID.setText("1");

        //incorrect data, inputID contains ID that is not in the database
        noteID.setText("9999999");
        answerCode = logicNotes.updateNote("updateNote", noteText, noteID, testMeeting, 1 );
        assertEquals(-5, answerCode);

        //correctly filled out data
        answerCode = logicNotes.crudNoteValidator("updateNote", noteText, noteID, testMeeting, 1 );
        assertEquals(0, answerCode);
    }
    @Test
    void deleteNoteValidationTest() {
        TextArea noteText = new TextArea();
        TextField noteID = new TextField();
        noteText.setText("CrudLogicNotes unit test");
        noteID.setText("1");
        //correct input
        answerCode = logicNotes.crudNoteValidator("deleteNote", noteText, noteID, testMeeting, 1);
        assertEquals(0, answerCode);
        //Wrong input, ID input is a string not an integer
        noteID.setText("one");
        answerCode = logicNotes.deleteNote("deleteNote", noteID, testMeeting, 1);
        assertEquals(-2, answerCode);
    }
}