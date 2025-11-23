package edu.upc.dsa.services.dto;

public class FishingRod extends edu.upc.dsa.models.FishingRod {

    private String name;  // name of the rod
    private double speed; // speed of reeling
    private double power; // power of the rod
    private int rarity;  // rarity level
    private int durability;    // total number of uses
    private int price;  // price in game currency
    String url;

    public FishingRod() {
        super();
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
        this.url = "/img/fishing_rod/" + fishingRod.getName().toLowerCase().replace(" ", "_");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
