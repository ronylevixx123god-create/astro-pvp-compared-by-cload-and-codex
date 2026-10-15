package me.ifamasssxd.cosmicbosses.utils;

import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BossEggUtils {
    private static HashMap<BossType, Boss> deadInstances;

    public static ItemStack getBossEgg(final BossType type) {
        final Boss boss = BossEggUtils.deadInstances.containsKey(type) ? BossEggUtils.deadInstances.get(type) : type.createInstance(null);
        BossEggUtils.deadInstances.put(type, boss);
        for (final BossType bossType : BossType.values()) {
            if (boss.getClass().isAssignableFrom(bossType.getBossClass())) {
                final List<String> lore = Lists.newArrayList();
                lore.add(ChatColor.GRAY + "This monster egg contains a");
                lore.add(ChatColor.RED + ChatColor.UNDERLINE.toString() + "dangerous" + ChatColor.GRAY + " warzone boss");
                lore.add("");
                lore.add(boss.getColor(true) + "LORE");
                lore.addAll(formatLore(type.getLore(), ChatColor.GRAY.toString() + ChatColor.ITALIC));
                lore.add("");
                lore.add(boss.getColor(true) + "DIFFICULTY");
                lore.add(boss.getColor(false) + boss.getDifficulty().getName());
                final ItemStack is = ItemBuilder.buildItem(Material.MONSTER_EGG, bossType.getEggData(), bossType.getEggName(), lore);
                return is;
            }
        }
        return null;
    }

    public static List<String> formatLore(final String s, final String color) {
        final List<String> lore_l = new ArrayList<String>();
        if (s == null) {
            return lore_l;
        }
        if (s.length() <= 38) {
            lore_l.add(color + s);
            return lore_l;
        }
        if (s.contains(" ")) {
            String line = color;
            for (final String word : s.split(" ")) {
                if (line.length() - 1 + word.length() <= 38) {
                    line = line + word + " ";
                } else {
                    if (line.endsWith(" ")) {
                        line = line.substring(0, line.length() - 1);
                    }
                    lore_l.add(line);
                    line = color + word + " ";
                }
            }
            if (ChatColor.stripColor(line).length() > 0) {
                lore_l.add(line);
            }
            return lore_l;
        }
        lore_l.add(s);
        return lore_l;
    }

    public static Boss getBossInstance(final BossType boss) {
        Boss retr = null;
        if (BossEggUtils.deadInstances.containsKey(boss)) {
            retr = BossEggUtils.deadInstances.get(boss);
        } else {
            retr = boss.createInstance(null);
            BossEggUtils.deadInstances.put(boss, retr);
        }
        return retr;
    }

    public static BossType getBossType(final ItemStack is) {
        if (is == null || !is.hasItemMeta() || !is.getItemMeta().hasDisplayName()) {
            return null;
        }
        for (final BossType bossType : BossType.values()) {
            if (bossType.getEggData() == is.getDurability() && is.getItemMeta().getDisplayName().equals(bossType.getEggName())) {
                return bossType;
            }
        }
        return null;
    }

    static {
        BossEggUtils.deadInstances = new HashMap<BossType, Boss>();
    }
}
