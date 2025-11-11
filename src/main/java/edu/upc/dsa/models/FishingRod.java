package edu.upc.dsa.models;

public class FishingRod {
    String id;    // uuid
    String kind;  // tipo de caña
    int usage;    // número de usos

    public FishingRod() {
    }

    public FishingRod(String id, String kind, int usage) {
        this.id = id;
        this.kind = kind;
        this.usage = usage;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public int getUsage() {
        return usage;
    }

    public void setUsage(int usage) {
        this.usage = usage;
    }

    @Override
    public String toString() {
        return "FishingRod{" +
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", usage=" + usage +
                '}';
    }
}
