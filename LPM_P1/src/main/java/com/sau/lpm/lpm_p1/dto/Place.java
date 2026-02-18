package com.sau.lpm.lpm_p1.dto;

public class Place {

    private int id;
    private String building;
    private String floor;
    private String room;
    private int seat;

    public Place() {
    }

    public Place(int id, String building, String floor, String room, int seat) {this.id = id;
       this.building = building;
       this.floor = floor;
       this.room = room;
       this.seat = seat;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public int getSeat() {
        return seat;
    }

    public void setSeat(int seat) {
        this.seat = seat;
    }

    @Override
    public String toString() {
        return "Place{" +
                "id=" + id +
                ", building='" + building + '\'' +
                ", floor='" + floor + '\'' +
                ", room='" + room + '\'' +
                ", seat=" + seat +
                '}';
    }
}
