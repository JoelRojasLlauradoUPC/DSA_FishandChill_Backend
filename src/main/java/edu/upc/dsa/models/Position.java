package edu.upc.dsa.models;

public class Position {
    double x;
    double y;
    String zone;

    public Position() {
    }

    public Position(double x, double y, String zone) {
        this.x = x;
        this.y = y;
        this.zone = zone;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    @Override
    public String toString() {
        return "Position{" +
                "x=" + x +
                ", y=" + y +
                ", zone='" + zone + '\'' +
                '}';
    }
}
