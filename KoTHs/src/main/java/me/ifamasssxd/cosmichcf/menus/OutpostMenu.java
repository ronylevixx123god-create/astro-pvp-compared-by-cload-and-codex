/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.google.common.collect.Lists
 *  org.bukkit.Bukkit
 *  org.bukkit.Location
 *  org.bukkit.entity.Player
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmichcf.menus;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import me.ifamasssxd.cosmichcf.struct.Outpost;
import me.ifamasssxd.cosmichcf.struct.OutpostState;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class OutpostMenu
extends CosmicGUI {
    private static SimpleDateFormat format = new SimpleDateFormat("haa");

    public OutpostMenu(Player player) {
        super(player, CosmicKOTHPlugin.get_().getBaseName() + "s", 9);
    }

    public void redraw() {
        int index = 3;
        Calendar current = Calendar.getInstance();
        Calendar cal = Calendar.getInstance();
        cal.setTimeZone(TimeZone.getTimeZone("CST"));
        current.setTimeZone(TimeZone.getTimeZone("CST"));
        for (Outpost outpost : CosmicKOTHPlugin.get_().getOutpostManager().getOutposts()) {
            OutpostType type = outpost.getOutpostType();
            if (type == null) continue;
            String coordString = "Unknown";
            if (outpost.getTeleportLocation() != null) {
                Location l = outpost.getTeleportLocation();
                String colorB = outpost.getNameColor();
                String color = outpost.getNameColor();

                coordString = "  " + CC.White + l.getBlockX() + colorB + "x " + CC.White+ l.getBlockY() + colorB + "y " + CC.White + l.getBlockZ() + colorB + "z" + (this.player.getWorld().equals((Object)outpost.getTeleportLocation().getWorld()) ? color + " (" + colorB + outpost.getDistanceDecimalFormat().format(outpost.getTeleportLocation().distance(this.player.getLocation())) + "m" + color + " away)" : "");
            }
            String color = outpost.getNameColor();

            ArrayList<String> lore = Lists.newArrayList(CC.translate("&7Every bend is meant to test you, every junction made")
                    , CC.translate("&7to bring you closer to that place where love and")
                    , CC.translate("&7sacrifice meet.")
                    , ""
                    , CC.translate(color + "Stronghold Coords&f:")
                    , CC.translate(coordString),
                    "",
                    CC.translate(color + "Current Info&f:"),
                    CC.translate(outpost.getOutpostState().getName() + (outpost.getOutpostState() == OutpostState.CONTROLLED ? CC.Gray + " (" + CC.Green + outpost.getLastFactionToCapTag() + CC.Gray + ")" : "") + CC.Gray + " " + outpost.getPrettyFormattedCapturePercent())
                    , ""
                    , CC.GoldB + "Rewards When Captured");

            type.getRewardStrings().forEach(re -> lore.add(" " + re));
            lore.add("");
            lore.add(CC.translate(CC.Gray + "Control the &n" + CosmicKOTHPlugin.get_().getBaseName() + "&7 to gain access to"));
            lore.add(CC.translate( "&7these rewards for &nyou&7 and your &nfaction&7!"));
            this.setItem(index, new ItemBuilder(type.getGuiMaterial().getItemType(), outpost.getOutpostName(), new String[0]).lore((List)lore).data((short)type.getGuiMaterial().getData()).build(), () -> Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicKOTHPlugin.get_(), () -> {
                //this.player.closeInventory();
                //this.player.performCommand(CosmicKOTHPlugin.get_().getBaseName().toLowerCase());
            }));
            index += 2;
        }
    }

    static {
        format.setTimeZone(TimeZone.getTimeZone("CST"));
    }
}

