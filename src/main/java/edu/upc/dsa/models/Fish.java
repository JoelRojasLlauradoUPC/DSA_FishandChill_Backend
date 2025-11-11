package edu.upc.dsa.models;

public class Fish {
    String id;      // uuid
    String kind;    // especie
    double depth;   // profundidad
    double weight;  // peso

    public Fish() {
    }

    public Fish(String id, String kind, double depth, double weight) {
        this.id = id;
        this.kind = kind;
        this.depth = depth;
        this.weight = weight;
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

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @Override
    public String toString() {
        return "Fish{" +
                "id='" + id + '\'' +
                ", kind='" + kind + '\'' +
                ", depth=" + depth +
                ", weight=" + weight +
                '}';
    }
}
