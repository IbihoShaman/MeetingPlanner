package com.example.meetingplanner_demo.BusinessLayer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class configurationLogicTest {

    @Test
    void getConfigurationTest() {
        assertEquals(configurationLogic.getConfiguration("connectionString"),"jdbc:mysql://localhost:3306/meetings" );
        assertEquals(configurationLogic.getConfiguration("DBUser"), "root");
        assertEquals(configurationLogic.getConfiguration("DBPassword"), "root");
        assertEquals(configurationLogic.getConfiguration("PdfName"), "Meeting.pdf");
    }

}