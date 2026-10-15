package me.astropvp.cratekeys.enums;

import com.cosmicpvp.cosmicutils.utils.CC;

public enum CrateType {
    SOLAR("Solar", "&e", ""),
    COSMIC("Cosmic", "&b", ""),
    SEASON("Christmas", "&f", "&c");
    private final String name;

    private final String prefix;

    private final String prefix2;

    CrateType(String name, String prefix, String prefix2) {
        this.name = name;
        this.prefix = CC.translate(prefix);
        this.prefix2 = CC.translate(prefix2);

    }

    public String getName() {
        return this.name;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public String getPrefixedName() {
        if (this == SEASON) {
            StringBuilder prefix = new StringBuilder();
            boolean color = true;
            String currentColor;
            for (String letter : this.getName().split("")) {
                letter = letter.toUpperCase();
                if (color) {
                    currentColor = this.prefix;
                    color = false;
                } else {
                    currentColor = this.prefix2;
                    color = true;
                }
                currentColor += "&l&n";
                prefix.append(currentColor).append(letter);
            }
            return CC.translate(prefix  + "&r&l" + " &f&lCrate Key");
        } else {
            return CC.translate(getPrefix() + CC.Bold + getName() + " &f&lCrate Key");
        }
    }
}
