package com.example.meetingplanner_demo.BusinessLayer;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class configurationLogicTest {

    @Test
    void testConfigConnectionString() {
        assertEquals(configurationLogic.getConfiguration("connectionString"),"jdbc:mysql://localhost:3306/meetings" );
    }
    @Test
    void testConfigUser(){
        assertEquals(configurationLogic.getConfiguration("DBUser"), "root");
    }
    @Test
    void testConfigPassword(){
        assertEquals(configurationLogic.getConfiguration("DBPassword"), "root");
    }
    @Test
    void testConfigPDF(){
        assertEquals(configurationLogic.getConfiguration("PdfName"), "Meeting.pdf");
    }

}