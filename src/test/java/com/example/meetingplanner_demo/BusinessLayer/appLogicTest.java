package com.example.meetingplanner_demo.BusinessLayer;

import com.example.meetingplanner_demo.Model.Meetings;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

class appLogicTest {
    private static final appLogic logicApp = new appLogic();

    @BeforeAll
    static void beforeAll(){
        Meetings testMeeting = new Meetings.meetingsBuilder("TestMeeting")
                .ID(1)
                .startDate("1/1/2000")
                .startTime("00:00")
                .endDate("31/12/2999")
                .endTime("23:59")
                .agenda("crudLogicMeeting unit test")
                .build();
        logicApp.setSelectedMeeting(testMeeting);
        logicApp.setSelectedID(logicApp.getSelectedMeeting().getID());
    }

    @Test
    void getMeetingListTest(){
        assertNotNull(logicApp.getMeetingList());
    }

    @Test
    void getNoteListTest(){
        assertNotNull(logicApp.getNotesList(logicApp.getSelectedID()));
    }

    @Test
    void generatePdfTest() throws IOException {
        assertEquals(0, logicApp.generatePdf());
    }
}