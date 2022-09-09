package com.example.meetingplanner_demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Meetings {
    private final Integer ID;
    private final String title;
    private final String start;
    private final String end;
    private final String agenda;
    //private String note;


    public Integer getID() { return ID; }
    public String getTitle() { return title; }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }
    public String getAgenda(){ return agenda; }

    public Meetings(Integer ID, String title, String start, String end, String agenda) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.end = end;
        this.agenda = agenda;
        Main.logger.trace("Meeting object created with meetingID: " + this.getID());
    }
}
