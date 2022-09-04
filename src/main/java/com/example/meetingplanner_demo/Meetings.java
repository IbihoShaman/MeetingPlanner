package com.example.meetingplanner_demo;

import javafx.scene.text.Text;

import java.util.Date;

public class Meetings {
    private Integer ID;
    private String title;
    private String start;
    private String end;
    //private String note;


    public Integer getID() { return ID; }
    public String getTitle() { return title; }
    public String getStart() {
        return start;
    }
    public String getEnd() {
        return end;
    }
    //public String getNote() { return note; }

    public Meetings(Integer ID, String title, String start, String end) {
        this.ID = ID;
        this.title = title;
        this.start = start;
        this.end = end;
        //this.note = note;
    }
}
