package net.astropvp.weaponbonuses.util;

public final class Constants {
    public static class NBT {
        public static final String DATA_COMPOUND_KEY = "weaponBonusData";
        public static final String BONUS_LEVEL_KEY = "weaponBonusLevel";
        public static final String BONUSES_KEY = "weaponBonuses";
        public static final String EXPERIENCE_KEY = "weaponBonusExperience";
    }

    public static class Lore {
        public static final String WEAPON_LEVEL = "&%color%&lWEAPON TIER: &a%current% &7/ %max%".replace("&", "§");
        public static final String WEAPON_LEVEL_STARTS_WITH = "§.§lWEAPON TIER: §a.+";
        public static final String WEAPON_EXPERIENCE = "&%color%&lWEAPON XP: &7(&a%current% &7/ %max% XP)".replace("&", "§");
        public static final String WEAPON_EXPERIENCE_STARTS_WITH = "§.§lWEAPON XP: §7\\(§a\\d+ §7/ \\d+ XP\\)";
    }
}
