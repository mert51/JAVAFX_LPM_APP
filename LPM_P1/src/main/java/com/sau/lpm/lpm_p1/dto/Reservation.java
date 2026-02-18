package com.sau.lpm.lpm_p1.dto;

public class Reservation {
    private int studentId;
    private int placeId;
    private String date;
    private int duration;

    public Reservation() {
    }

    public Reservation(int studentId, int placeId, String date, int duration) {
        this.studentId = studentId;
        this.placeId = placeId;
        this.date = date;
        this.duration = duration;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public int getPlaceId() {
        return placeId;
    }

    public void setPlaceId(int placeId) {
        this.placeId = placeId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
