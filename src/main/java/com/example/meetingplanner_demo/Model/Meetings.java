package com.example.meetingplanner_demo.Model;

public class Meetings {
    private Integer ID;
    private String title;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String agenda;

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

    public void setID(int ID){ this.ID = ID; }
    public void setTitle(String title){ this.title = title; }
    public void setStartDate(String startDate){ this.startDate = startDate; }
    public void setStartTime(String startTime){ this.startTime = startTime; }
    public void setEndDate(String endDate){ this.endDate = endDate; }
    public void setEndTime(String endTime){ this.endTime = endTime; }
    public void setAgenda(String agenda){ this.agenda = agenda; }


    public Meetings(meetingsBuilder builder){
        this.ID = builder.ID;
        this.title = builder.title;
        this.startDate = builder.startDate;
        this.startTime = builder.startTime;
        this.endDate = builder.endDate;
        this.endTime = builder.endTime;
        this.agenda = builder.agenda;
    }
        //The Builder design pattern helps with creating meetings with different amount of attributes without many constructors
        public static class meetingsBuilder {
            private Integer ID;
            private final String title;
            private String startDate;
            private String startTime;
            private String endDate;
            private String endTime;
            private String agenda;

            public meetingsBuilder(String title){
                if(title.isEmpty()){
                    this.title = "DefaultTitle";
                } else
                    this.title = title;
            }
            public meetingsBuilder ID(int ID){
                this.ID = ID;
                return this;
            }
            public meetingsBuilder startDate(String startDate){
                this.startDate = startDate;
                return this;
            }
            public meetingsBuilder startTime(String startTime){
                this.startTime = startTime;
                return this;
            }
            public meetingsBuilder endDate(String endDate){
                this.endDate = endDate;
                return this;
            }
            public meetingsBuilder endTime(String endTime){
                this.endTime = endTime;
                return this;
            }
            public meetingsBuilder agenda(String agenda){
                this.agenda = agenda;
                return this;
            }
            public Meetings build(){
                return new Meetings(this);
            }
    }
}
