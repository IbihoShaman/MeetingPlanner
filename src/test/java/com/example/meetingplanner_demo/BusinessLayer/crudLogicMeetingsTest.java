package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.Model.Meetings;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class crudLogicMeetingsTest {
    private static Meetings testMeeting;
    private final crudLogicMeetings logicMeetings = new crudLogicMeetings();
    private int answerCode;

    @BeforeAll
    static void beforeALl(){
        testMeeting = new Meetings.meetingsBuilder("TestMeeting")
                .startDate("1/1/2000")
                .startTime("00:00")
                .endDate("31/12/2999")
                .endTime("23:59")
                .agenda("crudLogicMeeting unit test")
                .build();
    }

    @Test
    void addMeetingValidationTest() {
        TextArea note = new TextArea();
        note.setText("Adding note from crudLogicNotes unit test");
        TextField inputID = new TextField();
        inputID.setText("1");
        //correct input
        answerCode = logicMeetings.crudMeetingValidator("createMeeting", inputID, testMeeting, note);
        assertEquals(0, answerCode);
        //incorrect time format
        testMeeting.setStartTime("asdf");
        answerCode = logicMeetings.crudMeetingValidator("createMeeting", inputID, testMeeting, note);
        assertEquals(-2, answerCode);
    }
    @Test
    void updateMeetingValidationTest() {
        TextArea note = new TextArea();
        TextField inputID = new TextField();
        inputID.setText("1");
        //correct input
        answerCode = logicMeetings.crudMeetingValidator("updateMeeting", inputID, testMeeting, note);
        assertEquals(0, answerCode);
        //The user also entered a new note while updating the meeting and gets a warning
        note.setText("Adding note from crudLogicNotes unit test");
        answerCode = logicMeetings.crudMeetingValidator("updateMeeting", inputID, testMeeting, note);
        assertEquals(-3, answerCode);
    }
    @Test
    void deleteMeetingValidationTest() {
        TextArea note = new TextArea();
        TextField inputID = new TextField();
        //correct input
        inputID.setText("1");
        answerCode = logicMeetings.crudMeetingValidator("updateMeeting", inputID, testMeeting, note);
        assertEquals(0, answerCode);
        //wrong input, user entered no ID or entered a wrong ID format
        inputID.clear();
        answerCode = logicMeetings.crudMeetingValidator("updateMeeting", inputID, testMeeting, note);
        assertEquals(-1, answerCode);
    }
}