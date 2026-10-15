package net.astropvp.weaponbonuses.bonus;

import java.util.HashMap;
import java.util.Map;

public enum BonusType {
    BASE_PVP_DMG("Base PvP DMG", "Base PvP DMG (+%value%%)", new HashMap<Integer, Double>() {
        {
            this.put(1, 2D);
            this.put(2, 4D);
            this.put(3, 6D);
            this.put(4, 8D);
            this.put(5, 10D);
        }
    }),
    BONUS_RAGE_STACK_DMG("Bonus Rage Stack DMG", "Bonus Rage Stack DMG (+%value%%)", new HashMap<Integer, Double>() {
        {
            this.put(1, 1.5D);
            this.put(2, 2.0D);
            this.put(3, 2.5D);
            this.put(4, 3.0D);
            this.put(5, 4.0D);
        }
    }),
    LESS_INCOMING_DMG("Less Incoming DMG", "Less Incoming DMG (-%value%%)", new HashMap<Integer, Double>() {
        {
            this.put(1, 2D);
            this.put(2, 4D);
            this.put(3, 6D);
            this.put(4, 8D);
            this.put(5, 10D);
        }
    }),
    EXTRA_HP_FROM_POTIONS("Extra HP From Potions", "Extra HP from Potions (+%value% HP)", new HashMap<Integer, Double>() {
        {
            this.put(1, 1.0D);
            this.put(2, 1.5D);
            this.put(3, 1.5D);
            this.put(4, 2.0D);
            this.put(5, 2.0D);

        }
    });

    private final String crystalName;
    private final String format;
    private final Map<Integer, Double> levelToValue;

    public final String getCrystalName() {
        return this.crystalName;
    }

    public final String getFormat() {
        return this.format;
    }

    public final Map<Integer, Double> getLevelToValue() {
        return this.levelToValue;
    }

    private BonusType(String crystalName, String format, Map<Integer, Double> levelToValue) {
        this.crystalName = crystalName;
        this.format = format;
        this.levelToValue = levelToValue;
    }
}
