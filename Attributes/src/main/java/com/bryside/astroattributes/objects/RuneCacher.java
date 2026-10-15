package com.bryside.astroattributes.objects;

import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.utils.Utils;
import com.cosmicpvp.cosmicutils.utils.CC;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class RuneCacher {
    public static HashSet<RuneCacher> caches = new HashSet<>();
    private int experience;
    private int level;
    private boolean canLevelUp;
    private RuneType type;
    private Player player;
    private UUID playerUuid;
    private List lore;

    public RuneCacher(ItemStack item) {
        if (item != null && item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {

                for (String l : meta.getLore()) {
                    if (l.startsWith(CC.RedB + "TITAN ATTRIBUTE:")) {
                        String name = l.substring(l.indexOf(":") + 6, l.indexOf("(") - 5);
                        int level = Integer.valueOf(l.substring(l.indexOf("Level") + 6, l.indexOf(")") - 4));
                        this.level = level;
                        this.type = RuneType.valueOf(name);
                    } else if (l.startsWith(CC.RedB + "TITAN XP:")) {
                        int experience = Integer.valueOf(l.substring(l.indexOf(":") + 6, l.indexOf("/") - 1));
                        this.experience = experience;
                        if (this.type != null && this.type.getRune().getExpForLevel(this.level) <= experience && this.level < this.type.getRune().getMaxLevel()) {
                            this.canLevelUp = true;
                        }
                    }
                }

            }
        }
    }

    public RuneCacher(ItemStack item, Player player) {
        if (item.hasItemMeta()) {
            ItemMeta meta = item.getItemMeta();
            if (meta.hasLore()) {
                if (Utils.isWeapon(item)) {
                    if (!isContained(player)) {
                        for (String l : meta.getLore()) {
                            if (l.startsWith(CC.RedB + "TITAN ATTRIBUTE:")) {
                                String name = l.substring(l.indexOf(":") + 6, l.indexOf("(") - 5);
                                this.level = Integer.parseInt(l.substring(l.indexOf("Level") + 6, l.indexOf(")") - 4));
                                this.type = RuneType.valueOf(name);
                            } else if (l.startsWith(CC.RedB + "TITAN XP:")) {
                                int experience = Integer.parseInt(l.substring(l.indexOf(":") + 6, l.indexOf("/") - 1));
                                this.experience = experience;
                                if (this.type != null && this.type.getRune().getExpForLevel(this.level) <= experience && this.level < this.type.getRune().getMaxLevel()) {
                                    this.canLevelUp = true;
                                }
                            }
                        }

                        if (this.type != null) {
                            caches.add(this);
                        }

                        this.playerUuid = player.getUniqueId();
                        this.player = player;
                        this.lore = meta.getLore();
                    }
                }
            }
        }
    }

    public static RuneCacher getRuneCacher(Player player) {
        UUID u = player.getUniqueId();
        Iterator var2 = caches.iterator();

        RuneCacher rune;
        do {
            if (!var2.hasNext()) {
                return null;
            }

            rune = (RuneCacher) var2.next();
        } while (rune == null || !rune.getUUID().equals(u));

        return rune;
    }

    public static boolean isContained(Player player) {
        return getRuneCacher(player) != null;
    }

    public static void removeCache(RuneCacher rune) {
        caches.remove(rune);
    }

    public boolean replaceCache(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        } else {
            ItemMeta meta = item.getItemMeta();
            if (!meta.hasLore()) {
                return false;
            } else {
                boolean occured = false;

                for (String l : meta.getLore()) {
                    if (l.startsWith(CC.RedB + "TITAN ATTRIBUTE:")) {
                        String name = l.substring(l.indexOf(":") + 6, l.indexOf("(") - 5);
                        int level = Integer.parseInt(l.substring(l.indexOf("Level") + 6, l.indexOf(")") - 4));
                        this.level = level;
                        this.type = RuneType.valueOf(name);
                        occured = true;
                    } else if (l.startsWith(CC.RedB + "TITAN XP:")) {
                        int experience = Integer.parseInt(l.substring(l.indexOf(":") + 6, l.indexOf("/") - 1));
                        this.experience = experience;
                        if (this.type != null && this.type.getRune().getExpForLevel(this.level) <= experience && this.level < this.type.getRune().getMaxLevel()) {
                            this.canLevelUp = true;
                        }
                    }
                }

                return occured;
            }
        }
    }

    public boolean canLevelUp() {
        return this.canLevelUp;
    }

    public RuneType getRuneType() { return this.type; }

    public int getLevel() {
        return this.level;
    }

    public int getExperience() {
        return this.experience;
    }

    public Player getPlayer() {
        return this.player;
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }

    public boolean isStillCached(ItemStack item) {
        if (!item.hasItemMeta()) {
            return false;
        } else if (!item.getItemMeta().hasLore()) {
            return false;
        } else {
            ItemMeta itemMeta = item.getItemMeta();
            return itemMeta.getLore().equals(this.lore);
        }
    }
}
