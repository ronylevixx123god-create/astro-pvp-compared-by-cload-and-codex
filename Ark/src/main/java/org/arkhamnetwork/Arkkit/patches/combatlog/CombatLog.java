/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.DecimalFormatType
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.MaskUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NumberUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.UpgradeUtils
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldguard.bukkit.WorldGuardPlugin
 *  com.sk89q.worldguard.protection.flags.DefaultFlag
 *  com.sk89q.worldguard.protection.flags.StateFlag$State
 *  net.citizensnpcs.api.CitizensAPI
 *  net.citizensnpcs.api.npc.NPC
 *  net.minecraft.server.v1_7_R4.MinecraftServer
 *  org.arkhamnetwork.enchantments.enchantments.axes.Bleed
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Location
 *  org.bukkit.block.Block
 *  org.bukkit.entity.LivingEntity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package org.arkhamnetwork.Arkkit.patches.combatlog;

import com.cosmicpvp.cosmicutils.struct.DecimalFormatType;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.rit.sucy.EnchantmentAPI;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.AstroItemSkinsPlugin;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.swords.MauisHookSkin;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.enchantments.enchantments.axes.Bleed;
import org.arkhamnetwork.enchantments.mastery.OpeningCombo;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CombatLog
        extends ArkPlugin {
    protected static boolean duel_plugin = false;
    protected static boolean mask_plugin = false;
    protected static boolean block_enderpearls = false;
    protected static long tagDuration;
    protected static boolean cosmicPVP;
    protected static boolean displayOnScoreboard;
    private static WorldGuardPlugin wg = null;

    static {
        cosmicPVP = false;
        displayOnScoreboard = false;
    }

    public CombatLog(Plugin p) {
        super(p);
    }

    protected static WorldGuardPlugin getWorldGuard() {
        return wg;
    }

    private static WorldGuardPlugin hookWorldGuard() {
        Plugin plugin = Arkkit.get().getServer().getPluginManager().getPlugin("WorldGuard");
        if (plugin == null || !(plugin instanceof WorldGuardPlugin)) {
            return null;
        }
        return (WorldGuardPlugin) plugin;
    }

    protected static boolean isPvPDisabled(Location l) {
        try {
            Class<?> bukkitUtil = CombatLog.getWorldGuard().getClass().getClassLoader().loadClass("com.sk89q.worldguard.bukkit.BukkitUtil");
            Method toVector = bukkitUtil.getMethod("toVector", Block.class);
            Vector blockVector = (Vector) toVector.invoke(null, new Object[]{l.getBlock()});
            List<String> regionSet = CombatLog.getWorldGuard().getGlobalRegionManager().get(l.getWorld()).getApplicableRegionsIDs(blockVector);
            if (regionSet == null || regionSet.size() < 1) {
                try {
                    return CombatLog.getWorldGuard().getGlobalRegionManager().get(l.getWorld()).getRegion("__global__").getFlags().get(DefaultFlag.PVP) == StateFlag.State.DENY;
                } catch (Exception err) {
                    return false;
                }
            }
            boolean return_flag = false;
            int return_priority = -1;
            for (String region : regionSet) {
                if (!CombatLog.getWorldGuard().getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().containsKey(DefaultFlag.PVP))
                    continue;
                StateFlag.State pvp_flag = (StateFlag.State) CombatLog.getWorldGuard().getGlobalRegionManager().get(l.getWorld()).getRegion(region).getFlags().get(DefaultFlag.PVP);
                int region_priority = CombatLog.getWorldGuard().getGlobalRegionManager().get(l.getWorld()).getRegion(region).getPriority();
                if (return_priority == -1) {
                    return_flag = pvp_flag == StateFlag.State.DENY;
                    return_priority = region_priority;
                    continue;
                }
                if (region_priority <= return_priority) continue;
                return_flag = pvp_flag == StateFlag.State.DENY;
                return_priority = region_priority;
            }
            return return_flag;
        } catch (Exception exception) {
            return false;
        }
    }

    public static boolean inCombat(Player pl) {
        try {
            List val = pl.getMetadata("cl_combat");
            if (val != null && !val.isEmpty() && System.currentTimeMillis() - ((MetadataValue) val.get(0)).asLong() <= CombatLog.getCombatTagDuration(pl)) {
                return true;
            }
        } catch (Exception err) {
            err.printStackTrace();
            //pl.setMetadata("cl_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
        }
        return false;
    }

    public static long getTimeLeftInCombat(Player pl) {
        try {
            List values = pl.getMetadata("cl_combat");
            if (values != null && values.size() > 0) {
                Long time = ((MetadataValue) values.get(0)).asLong();
                long duration = CombatLog.getCombatTagDuration(pl);
                if (System.currentTimeMillis() - time <= duration) {
                    return duration - (System.currentTimeMillis() - time);
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
            //pl.setMetadata("cl_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
        }
        return 0L;
    }

    public static long getCombatTagDuration(Player player) {
        long toRetr = tagDuration;

        if (mask_plugin && MaskUtils.hasMaskEquipped(player, "JOKER")) {
            toRetr -= 3000L;
        }

        if (player.hasMetadata("affectedByJokerMask") && player.getMetadata("affectedByJokerMask").get(0).asLong() > (long) MinecraftServer.currentTick) {
            toRetr += 4000L;
        }
        return toRetr;
    }

    public static synchronized void flagCombat(Player pl) {
        boolean wasInCombat = CombatLog.inCombat(pl);
        pl.setMetadata("cl_combat", new FixedMetadataValue(Arkkit.get(), System.currentTimeMillis()));
        if (!wasInCombat) {
            pl.setMetadata("cl_combat_monitor_task", new FixedMetadataValue(Arkkit.get(), CombatExpireTask.combatTagged.add(new WeakReference<Player>(pl))));
            pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You have entered combat. Do not log out for " + NumberUtils.format((double) CombatLog.getCombatTagDuration(pl) / 1000.0, DecimalFormatType.SECONDS) + "s.");

/*            if (AstroItemSkinsAPI.hasSkinEquipped(pl, SkinType.THANKSGIVING_AMULET_AUTUMN)) {
                pl.setMetadata("autumn_amulet", new FixedMetadataValue(Arkkit.get(), 0));
                pl.sendMessage(CC.translate("&e&l* AUTUMN AMULET &7ItemSkin &e&l**"));
                pl.sendMessage(CC.translate("&a+ Absorbtion &7(20s)"));
                pl.sendMessage(CC.translate("&a-15% Incoming Damage &7(15s)"));
                pl.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 5 * 20, 4));
            }*/
            if (AstroItemSkinsAPI.hasSkinEquipped(pl, SkinType.CHRISTMAS_2022_SWORD_ACTIVE)) {
                if (!OpeningCombo.isOnCooldown(pl)) {
                    OpeningCombo.proc(pl, 6);
                    pl.sendMessage(CC.translate("&4&lOPENING COMBO - ACTIVE &7(&bFrostbound Bonus&7)"));
                }
            } else if (EnchantmentAPI.getEnchantments(pl.getEquipment().getItemInHand()).containsKey(EnchantmentAPI.getEnchantment("Opening Combo"))) {
                int openingComboLevel = EnchantmentAPI.getEnchantments(pl.getEquipment().getItemInHand()).get(EnchantmentAPI.getEnchantment("Opening Combo"));
                if (!OpeningCombo.isOnCooldown(pl)) {
                    OpeningCombo.proc(pl, openingComboLevel);
                    pl.sendMessage(CC.DarkRedB + "OPENING COMBO - ACTIVE");
                }
            }
        }
        pl.setAllowFlight(false);
        pl.setFlying(false);
        if (pl.getOpenInventory() != null && (pl.getOpenInventory().getTopInventory().getName().startsWith("Shop") || pl.getOpenInventory().getTopInventory().getName().equals("Auction House"))) {
            pl.closeInventory();
        }
        CombatTagEvent event = new CombatTagEvent(pl, wasInCombat);
        Bukkit.getServer().getPluginManager().callEvent(event);
    }

    public static void unflagCombat(Player pl) {
/*        if (pl.hasMetadata("autumn_amulet")) {
            pl.removeMetadata("autumn_amulet", Arkkit.get());
        }*/

        if (MauisHookSkin.mauiEffect.containsKey(pl) && !CombatLog.inCombat(pl)) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(Arkkit.get(), () -> {
                MauisHookSkin.mauiEffect.remove(pl);
            }, 1200L);
        }
        if (pl.hasMetadata("cl_combat_monitor_task")) {
            pl.removeMetadata("cl_combat_monitor_task", Arkkit.get());
        }
        if (Bukkit.getPluginManager().isPluginEnabled("Enchantments")) {
            Bleed.clearBleedEffect(pl);
        }
        pl.sendMessage(ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.GREEN + " You have left combat. You may now safely logout.");
        pl.setAllowFlight(true);

        pl.updateInventory();
    }

    @Override
    public void enable() {
        wg = CombatLog.hookWorldGuard();
        duel_plugin = new File("plugins/MCLegendsDuels.jar").exists();
        mask_plugin = new File("plugins/CosmicMasks.jar").exists();
        block_enderpearls = Arkkit.get().getConfig().getBoolean("settings.combat_log.block_enderpearls");
        tagDuration = Arkkit.get().getConfig().getLong("settings.combat_log.combat_duration");
        displayOnScoreboard = this.getBukkitPlugin().getConfig().getBoolean("settings.combat_log.show_on_scoreboard", false);
        cosmicPVP = new File("plugins/CosmicCustomTNT.jar").exists();
        for (String pName : this.getBukkitPlugin().getConfig().getStringList("settings.combatLog.playersToKill")) {
            CombatLogLogoutListener.playersToKill.add(pName);
        }
        this.registerListener(new CombatLogCombatListener());
        this.registerListener(new CombatLogLogoutListener(this));
        this.registerCommand(new CommandLogout("logout", "/<command>", "Safetly logs you out of the server.", new ArrayList<String>(Arrays.asList("quit", "exit", "leave"))));
        this.registerTask(new CombatExpireTask().runTaskTimer(Arkkit.get(), 10L, 10L), CombatExpireTask.class);
    }

    @Override
    public void disable() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.removeMetadata("cl_combat", Arkkit.get());
        }
        this.cleanNPCs();
        this.getBukkitPlugin().getConfig().set("settings.combatLog.playersToKill", new ArrayList<String>(CombatLogLogoutListener.playersToKill));
        this.getBukkitPlugin().saveConfig();
        wg = null;
    }

    private void cleanNPCs() {
        for (NPC n : CombatLogLogoutListener.combatNPCs.values()) {
            n.despawn();
            n.destroy();
            CitizensAPI.getNPCRegistry().deregister(n);
        }
        for (NPC n : CitizensAPI.getNPCRegistry()) {
            if (n.getEntity() == null || !n.getEntity().hasMetadata("combatNPC_PlayerName")) continue;
            n.despawn();
            n.destroy();
            CitizensAPI.getNPCRegistry().deregister(n);
        }
    }
}

