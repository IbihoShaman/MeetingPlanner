package com.example.meetingplanner_demo.DataAccessLayer;

import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DBTest {
    private final DB database = new DB("jdbc:mysql://localhost:3306/meetings");

    @Test
    void getConnection() {

    }

    @Test
    void checkUpdateTest() throws SQLException {
        String updateQuery = "UPDATE meetinglist SET title = 'TestMeeting' WHERE meetingID = '1'";
        assertTrue(database.checkUpdate(updateQuery));
    }
}