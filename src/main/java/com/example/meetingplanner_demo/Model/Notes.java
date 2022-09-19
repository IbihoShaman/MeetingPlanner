package com.example.meetingplanner_demo.Model;

import com.example.meetingplanner_demo.Main;

public class Notes {
    private final Integer noteID;
    private final Integer parentMeetingID;
    private final String noteText;

    public Integer getNoteID() { return noteID; }
    public Integer getParentMeetingID() { return parentMeetingID; }
    public String getNoteText() { return noteText; }

    public Notes(Integer noteID, Integer parentMeetingID, String noteText) {
        this.noteID = noteID;
        this.parentMeetingID = parentMeetingID;
        this.noteText = noteText;
        Main.logger.trace("Note object created with ID: " + this.getNoteID() + " and parentID: " + this.getParentMeetingID());
    }
}
