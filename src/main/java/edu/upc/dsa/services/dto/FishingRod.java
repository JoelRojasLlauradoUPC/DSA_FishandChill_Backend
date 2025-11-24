package edu.upc.dsa.services.dto;

public class FishingRod {

    private String name;  // name of the rod
    private double speed; // speed of reeling
    private double power; // power of the rod
    private int rarity;  // rarity level
    private int durability;    // total number of uses
    private int price;  // price in game currency
    String url;

    public FishingRod() {
    }

    public FishingRod(edu.upc.dsa.models.FishingRod fishingRod) {
        String name = fishingRod.getName();
        double speed = fishingRod.getSpeed();
        double power = fishingRod.getPower();
        int rarity = fishingRod.getRarity();
        int durability = fishingRod.getDurability();
        int price = fishingRod.getPrice();

        this.setName(name);
        this.setSpeed(speed);
        this.setPower(power);
        this.setRarity(rarity);
        this.setDurability(durability);
        this.setPrice(price);
        this.url = "/img/fishing_rods/" + fishingRod.getName().toLowerCase().replace(" ", "_") +".png";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public double getPower() {
        return power;
    }

    public void setPower(double power) {
        this.power = power;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}
