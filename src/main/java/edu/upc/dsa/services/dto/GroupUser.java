package edu.upc.dsa.services.dto;

public class GroupUser {
    private String username;

    public GroupUser() {}

    public GroupUser(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
