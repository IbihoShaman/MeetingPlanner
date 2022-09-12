package com.example.meetingplanner_demo;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Meetings {
    private final Integer ID;
    private final String title;
    private final String startDate;
    private final String startTime;
    private final String endDate;
    private final String endTime;
    private final String agenda;
    //private String note;


    public Integer getID() { return ID; }
    public String getTitle() { return title; }
    public String getStartDate() {
        return startDate;
    }
    public String getStartTime() {
        return startTime;
    }
    public String getEndDate() {
        return endDate;
    }
    public String getEndTime() {
        return endTime;
    }
    public String getAgenda(){ return agenda; }

    public Meetings(Integer ID, String title, String startDate, String startTime, String endDate, String endTime, String agenda) {
        this.ID = ID;
        this.title = title;
        this.startDate = startDate;
        this.startTime = startTime;
        this.endDate = endDate;
        this.endTime = endTime;
        this.agenda = agenda;
        Main.logger.trace("Meeting object created with meetingID: " + this.getID());
    }
}
