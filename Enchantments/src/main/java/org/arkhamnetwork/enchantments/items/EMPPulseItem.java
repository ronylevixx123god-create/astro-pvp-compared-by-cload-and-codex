package org.arkhamnetwork.enchantments.items;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicskycustoms.exceptions.ItemException;
import me.ifamasssxd.cosmicskycustoms.exceptions.PrefixedItemException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.PlayerInteractable;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class EMPPulseItem extends CosmicItem implements PlayerInteractable, Listener {
    private final String cooldownMetadataKey = "lastEMPTrinketUse";
    private final int cooldownSeconds = 300;
    private final int soulCost = 300;

    public EMPPulseItem() {
        super("emp_pulse");
    }

    public ItemStack createItem(final Player player, final Object... objects) {
        final String displayName = CC.DarkAquaB + "EMP Pulse " + CC.Gray + "(Right Click)";
        final List<String> lore = (List<String>) Lists.newArrayList(new String[]{CC.Gray + "Emits a large Electromagnetic Pulse,", CC.Gray + "combat tagging ALL players within the", CC.Gray + "device's radius. Players can only be", CC.Gray + "affected by 1 EMP every 3 minutes.", "", CC.DarkAquaU + "Radius:" + CC.Aqua + " 7x128x7", CC.DarkAquaU + "Soul Cost:" + CC.Aqua + " " + 300 + " souls", CC.DarkAquaU + "Cooldown:" + CC.Aqua + " " + 300 + "s"});
        return this.applyItemData(new ItemBuilder(Material.REDSTONE_TORCH_ON, displayName, new String[0]).lore((List) lore).build()).assignIID().build();
    }

    public void onPlayerInteractable(final Player player, final ItemStack itemStack, final NBTWrapper nbtWrapper, final PlayerInteractEvent playerInteractEvent) throws CosmicException {
        if (playerInteractEvent.getAction() == Action.RIGHT_CLICK_BLOCK || playerInteractEvent.getAction() == Action.RIGHT_CLICK_AIR) {
            playerInteractEvent.setCancelled(true);
            playerInteractEvent.setUseItemInHand(Event.Result.DENY);
            playerInteractEvent.setUseInteractedBlock(Event.Result.DENY);
            if (player.hasMetadata("lastEMPTrinketUse") && System.currentTimeMillis() - player.getMetadata("lastEMPTrinketUse").get(0).asLong() < 300000L) {
                throw new PrefixedItemException("You cannot use EMP Pulse for another " + ChatColor.GRAY + (300000L - (System.currentTimeMillis() - player.getMetadata("lastEMPTrinketUse").get(0).asLong())) / 1000L + "s");
            }
            if (this.playerRecentlyTeleported(player)) {
                throw new PrefixedItemException("You cannot use this trinket so quickly after teleporting!");
            }
            if (!WorldGuardUtils.isPvPEnabled(player.getLocation())) {
                throw new PrefixedItemException("You cannot use this trinket in a PvP-disabled region.");
            }
            if (!KillTrackerAPI.hasSoulAmount(player, 300)) {
                player.updateInventory();
                throw new ItemException(CC.RedB + " *** NOT ENOUGH SOULS " + ChatColor.RED + "(REQ: " + 300 + ")" + ChatColor.BOLD + " ***");
            }
            KillTrackerAPI.removeSoulsFromGems(player, 300);
            player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.7f, 1.2f);
            int affectedPlayers = 0;
            for (final Entity entity : player.getNearbyEntities(7.0, 128.0, 7.0)) {
                if (entity instanceof Player) {
                    final Player pNear = (Player) entity;
                    if (!WorldGuardUtils.isPvPEnabled(pNear.getLocation())) {
                        continue;
                    }
                    if (!player.canSee(pNear)) {
                        continue;
                    }
                    if (pNear.getGameMode() != GameMode.SURVIVAL) {
                        continue;
                    }
                    if (pNear.hasMetadata("lastEMPApply") && System.currentTimeMillis() - pNear.getMetadata("lastEMPApply").get(0).asLong() < 180000L) {
                        continue;
                    }
                    if (this.playerRecentlyTeleported(pNear)) {
                        continue;
                    }
                    pNear.setMetadata("lastEMPApply", (MetadataValue) new FixedMetadataValue((Plugin) CosmicUtils.get(), (Object) System.currentTimeMillis()));
                    pNear.getWorld().strikeLightningEffect(pNear.getLocation());
                    pNear.sendMessage(CC.DarkAquaB + "** EMP Pulse **");
                    CombatLog.flagCombat(pNear);
                    ++affectedPlayers;
                }
            }
            player.sendMessage(CC.DarkAquaB + "** EMP Pulse [" + CC.Aqua + affectedPlayers + " players affected" + CC.DarkAquaB + "] **");
            player.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + " -" + 300 + " SOULS (" + ChatColor.WHITE + KillTrackerAPI.getAllSouls(player) + ChatColor.RED + ChatColor.BOLD + ")");
            player.setMetadata("lastEMPTrinketUse", (MetadataValue) new FixedMetadataValue((Plugin) CosmicDungeonsPlugin.get_(), (Object) System.currentTimeMillis()));
            player.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            e.getPlayer().setMetadata("lastTeleportEvent", (MetadataValue) new FixedMetadataValue((Plugin) Enchantments.getInstance(), (Object) System.currentTimeMillis()));
        }
    }

    private boolean playerRecentlyTeleported(final Player player) {
        return player.hasMetadata("lastTeleportEvent") && System.currentTimeMillis() - player.getMetadata("lastTeleportEvent").get(0).asLong() < 7000L;
    }
}
