package edu.upc.dsa.services.dto;

import java.util.List;

public class TeamsRanking {

    private List<TeamScore> teamsRanking;


    public TeamsRanking() {
    }

    public TeamsRanking(List<TeamScore> teamsRanking) {
        this.teamsRanking = teamsRanking;
    }

    public List<TeamScore> getTeamsRanking() {
        return teamsRanking;
    }

    public void setTeamsRanking(List<TeamScore> teamsRanking) {
        this.teamsRanking = teamsRanking;
    }

    public void addTeamScore(String name, String avatar, int points) {
        TeamScore teamScore = new TeamScore(name, avatar, points);
        this.teamsRanking.add(teamScore);
    }
}
