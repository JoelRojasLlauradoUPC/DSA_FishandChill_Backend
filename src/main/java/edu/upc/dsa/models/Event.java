package edu.upc.dsa.models;

public class Event {
    int id;
    int eventId;
    int userId;

    public Event() {
    }

    public Event(int eventId, int userId) {
        this.id = 0;
        this.eventId = eventId;
        this.userId = userId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getEventId() {
        return eventId;
    }
    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
