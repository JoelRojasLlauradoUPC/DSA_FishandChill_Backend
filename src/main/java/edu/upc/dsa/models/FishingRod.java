package edu.upc.dsa.models;

import java.util.Objects;

import static java.util.UUID.randomUUID;

public class FishingRod {
    private int id;    // uuid
    private String name;  // name of the rod
    private double speed; // speed of reeling
    private double power; // power of the rod
    private int rarity;  // rarity level
    private int durability;    // total number of uses
    private int price;  // price in game currency

    public FishingRod() {
    }

    public FishingRod(String name, double speed, double power, int rarity, int durability, int price) {
        this.id = 0;
        this.name = name;
        this.speed = speed;
        this.power = power;
        this.rarity = rarity;
        this.durability = durability;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FishingRod that = (FishingRod) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "FishingRod{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", speed=" + speed +
                ", power=" + power +
                ", rarity=" + rarity +
                ", durability=" + durability +
                ", price=" + price +
                '}';
    }
}
