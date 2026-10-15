package net.astropvp.armorsets.struct.cache;

import com.cosmicpvp.cosmicutils.utils.CC;
import lombok.Getter;
import lombok.Setter;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.antiMkit.AntiMkit;
import net.astropvp.armorsets.struct.heroicArmor.HeroicArmor;
import net.astropvp.core.utils.cache.Cache;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Setter
@Getter
public class ArmorSetCache extends Cache {
    private final Map<ArmorSetType, Integer> currentArmorSets;
    private final Map<ArmorSetType, Integer> currentSetCrystals;
    private final Map<AntiMkit, Integer> currentAntiMkitCrystals;

    private int heroicPieces;
    private int omniPieces;

    private ArmorSetType currentArmorSet;
    private final Player player;

    public ArmorSetCache(Player player) {
        super("armor_set");
        this.player = player;
        this.currentArmorSets = new HashMap<>();
        this.currentSetCrystals = new HashMap<>();
        this.currentAntiMkitCrystals = new HashMap<>();
        this.currentArmorSet = null;
        this.heroicPieces = 0;
        this.omniPieces = 0;
    }

    public void addArmorSet(ArmorSetType type) {
        if (!this.currentArmorSets.containsKey(type)) {
            this.currentArmorSets.put(type, 1);

            if (this.currentArmorSets.get(type) + omniPieces == 4) {
                this.currentArmorSet = type;

            }
        } else {
            this.currentArmorSets.put(type, this.currentArmorSets.get(type) + 1);
            if (this.currentArmorSets.containsKey(type) && this.currentArmorSets.get(type) == 4 || (this.currentArmorSets.containsKey(type) && this.currentArmorSets.get(type) + omniPieces == 4)) {
                this.currentArmorSet = type;


            }
        }

    }

    public void removeArmorSet(ArmorSetType type) {
        if (this.currentArmorSets.containsKey(type)) {
            int amount = this.currentArmorSets.get(type);
            if (amount - 1 == 0) {
                this.currentArmorSets.remove(type);
            } else {
                this.currentArmorSets.put(type, this.currentArmorSets.get(type) - 1);
            }
            if (this.currentArmorSets.containsKey(type) && this.currentArmorSets.get(type) != 4 || this.currentArmorSets.containsKey(type) && this.currentArmorSets.get(type) + omniPieces != 4) {
                this.currentArmorSet = null;
            }
        }

    }

    public void addArmorCrystal(ArmorSetType type) {
        if (this.currentSetCrystals.containsKey(type)) {
            this.currentSetCrystals.put(type, this.currentSetCrystals.get(type) + 1);
        } else {
            this.currentSetCrystals.put(type, 1);
        }

    }

    public void removeArmorCrystal(ArmorSetType type) {
        if (this.currentSetCrystals.containsKey(type)) {
            int amount = this.currentSetCrystals.get(type);
            if (amount - 1 == 0) {
                this.currentSetCrystals.remove(type);
            } else {
                this.currentSetCrystals.put(type, this.currentSetCrystals.get(type) - 1);
            }
        }

    }

    public void addMkitCrystal(AntiMkit type) {
        if (this.currentAntiMkitCrystals.containsKey(type)) {
            this.currentAntiMkitCrystals.put(type, this.currentAntiMkitCrystals.get(type) + 1);
        } else {
            this.currentAntiMkitCrystals.put(type, 1);
        }

    }

    public void removeMkitCrystal(AntiMkit type) {
        if (this.currentAntiMkitCrystals.containsKey(type)) {
            int amount = this.currentAntiMkitCrystals.get(type);
            if (amount - 1 == 0) {
                this.currentAntiMkitCrystals.remove(type);
            } else {
                this.currentAntiMkitCrystals.put(type, this.currentAntiMkitCrystals.get(type) - 1);
            }
        }

    }

    public void addHeroicArmor(ItemStack item) {
            if (heroicPieces +1 > 4) return;
            this.heroicPieces++;
    }

    public void removeHeroicArmor(ItemStack item) {
            if (this.heroicPieces -1 < 0) return;
            this.heroicPieces--;
    }

    public void addOmniPiece(ItemStack item) {
            if (omniPieces + 1 > 4) return;
            this.omniPieces++;
        for (Map.Entry<ArmorSetType, Integer> entry : currentArmorSets.entrySet()) {
            ArmorSetType type = entry.getKey();
            Integer amount = entry.getValue();

            if (this.omniPieces + amount == 4) {
                this.currentArmorSet = type;


            }
        }


    }

    public void removeOmniPiece(ItemStack item) {
            if (this.omniPieces -1 < 0) return;
            this.omniPieces--;
            if (this.currentArmorSet != null) {
                this.currentArmorSet = null;
            }
        }
}
