package edu.upc.dsa.services.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "EventUser", description = "User registered in an event")
public class EventUser {

    @ApiModelProperty(value = "Name", example = "Juan")
    private String name;

    @ApiModelProperty(value = "Surnames", example = "Pérez García")
    private String surnames;

    @ApiModelProperty(value = "Avatar image URL")
    private String avatar;

    public EventUser() {
    }

    public EventUser(String name, String surnames, String avatar) {
        this.name = name;
        this.surnames = surnames;
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public String getSurnames() {
        return surnames;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSurnames(String surnames) {
        this.surnames = surnames;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
