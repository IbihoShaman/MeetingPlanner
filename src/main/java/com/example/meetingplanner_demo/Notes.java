package com.example.meetingplanner_demo;

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
    }
}
