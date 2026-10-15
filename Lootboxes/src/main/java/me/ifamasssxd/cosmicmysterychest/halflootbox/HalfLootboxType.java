package me.ifamasssxd.cosmicmysterychest.halflootbox;

public enum HalfLootboxType {
    LEFT("Left"),RIGHT("Right");

    private String halfLootboxString;

    HalfLootboxType(String halfLootboxString) {
        this.halfLootboxString = halfLootboxString;
    }

    public String getHalfLootboxString() {
        return halfLootboxString;
    }

}
