package org.arkhamnetwork.enchantments.util;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class DurabilityUtil {
    public static boolean healLowestArmorPiece(final Player p, final short amount) {
        ArmorSlot lowest = null;
        short lowestDura = 0;
        for (int i = 0; i < p.getEquipment().getArmorContents().length; ++i) {
            final ItemStack armorPiece = p.getEquipment().getArmorContents()[i];
            if (armorPiece != null) {
                if (armorPiece.getType() != Material.AIR) {
                    if (armorPiece.getDurability() > 0) {
                        if (lowestDura == 0 || armorPiece.getDurability() < lowestDura) {
                            lowestDura = armorPiece.getDurability();
                            lowest = ArmorSlot.getArmorSlotByIndex(i);
                        }
                    }
                }
            }
        }
        if (lowest != null) {
            final ItemStack j = p.getEquipment().getArmorContents()[lowest.getIndex()];
            j.setDurability((short) Math.max(0, (short) (j.getDurability() - amount)));
            return true;
        }
        return false;
    }

    public static boolean healMostDamagedArmorPeice(final Player p, final short amount) {
        ArmorSlot lowest = null;
        short highestDamagedPeice = 0;
        for (int i = 0; i < p.getEquipment().getArmorContents().length; ++i) {
            final ItemStack armorPiece = p.getEquipment().getArmorContents()[i];
            if (armorPiece != null) {
                if (armorPiece.getType() != Material.AIR) {
                    if (armorPiece.getDurability() > 0) {
                        if (highestDamagedPeice == 0 || armorPiece.getDurability() > highestDamagedPeice) {
                            highestDamagedPeice = armorPiece.getDurability();
                            lowest = ArmorSlot.getArmorSlotByIndex(i);
                        }
                    }
                }
            }
        }
        if (lowest != null) {
            final ItemStack j = p.getEquipment().getArmorContents()[lowest.getIndex()];
            j.setDurability((short) Math.max(0, (short) (j.getDurability() - amount)));
            return true;
        }
        return false;
    }
}
