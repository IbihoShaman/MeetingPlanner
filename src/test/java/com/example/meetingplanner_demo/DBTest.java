package com.example.meetingplanner_demo;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DBTest {

    @Test
    void getConnection() {
        DB database = new DB();
        assertNotNull(database.getConnection());
    }
}