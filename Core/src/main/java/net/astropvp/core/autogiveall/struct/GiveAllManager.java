/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest
 *  me.ifamasssxd.cosmicmysterychest.net.astropvp.armorsets.struct.MysteryChest
 *  org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package net.astropvp.core.autogiveall.struct;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import me.astropvp.cratekeys.enums.CrateType;
import me.astropvp.cratekeys.items.CrateKeyItem;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import net.astropvp.CollectPlugin;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import net.astropvp.struct.PlayerCollect;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.HashMap;

public class GiveAllManager {
    private static long nextGiveAll = 2400;
    private static ItemStack giveAllItem = new ItemStack(Material.DIAMOND);

    public static long getNextGiveAll() {
        return nextGiveAll;
    }

    public static void setNextGiveAll(long time) {
        nextGiveAll = time;
    }

    public static void doGiveAll() {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (!p.isOnline()) continue;
            PlayerCollect collect = CollectPlugin.getInstance().getCollectManager().getOrCreate(p.getUniqueId().toString());
            boolean b = false;
            CollectCategory giveall = null;
            for (CollectCategory cat : collect.getCategories()) {
                if (cat.getType() == CollectType.GIVEALL) {
                    b = true;
                    giveall = cat;
                    break;
                }
            }
            p.sendMessage(CC.translate("&c&l(COLLECT&c&l)&c The Auto Giveall Loot has been put into your &n/collect&c."));
            giveAllItem.setAmount(12);
            if (b) {
                ItemStack[] items = giveall.getItems();
                ItemStack[] newarray = new ItemStack[items.length + 1];
                System.arraycopy(items, 0, newarray, 0, items.length);
                newarray[newarray.length - 1] = giveAllItem;
                giveall.setItems(newarray);
            } else {

                ItemStack[] items = new ItemStack[1];
                items[0] = giveAllItem;
                collect.add(new CollectCategory(CollectType.GIVEALL, items, CC.YellowB + "Auto Giveall Loot", System.currentTimeMillis()));
            }
            nextGiveAll = 2400;
        }
    }

    public static ItemStack getGiveAllItem() {
        return giveAllItem;
    }
}

