package com.compilex.CosmicDungeons.dungeons.events.destroyedoutpost;

import com.compilex.CosmicDungeons.dungeons.libs.DestroyedOutpostDungeon;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.MapMaker;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class JunkDimensionTask extends BukkitRunnable {
    private Map<Player, Integer> junkDimensionPlayerTimers = (new MapMaker()).concurrencyLevel(32).weakKeys().makeMap();

    private List<Location> junkChestLocations;

    private List<Location> chestsWithKeys = Lists.newArrayList();

    private Location junkDimensionLocation;

    private DestroyedOutpostDungeon dungeon;

    public JunkDimensionTask(DestroyedOutpostDungeon dungeon) {
        this.dungeon = dungeon;
        this.junkChestLocations = getLocations((Dungeon)this.dungeon, Lists.newArrayList());
        this.junkDimensionLocation = new Location(dungeon.getWorld(), -442.0D, 69.0D, 14.0D);
    }

    public void run() {
        this.junkDimensionPlayerTimers.forEach((player, timeLeft) -> {
            if (!player.isValid() || player.isDead() || !player.getWorld().getName().equals(this.dungeon.getWorldName())) {
                this.junkDimensionPlayerTimers.remove(player);
                return;
            }
            if (timeLeft.intValue() <= 0) {
                this.dungeon.broadcastMessage(CC.White + player.getName() + "'s couldn't escape the Junk Dimension in time, they have been lost in the void!");
                player.setHealth(0.0D);
                this.junkDimensionPlayerTimers.remove(player);
            } else {
                if (timeLeft.intValue() % 15 == 0 || timeLeft.intValue() <= 10) {
                    player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must escape this dimension in " + timeLeft + "s or you will be lost forever!");
                    player.playSound(player.getLocation(), Sound.NOTE_PIANO, 1.0F, 1.4F);
                }
                this.junkDimensionPlayerTimers.put(player, timeLeft = Integer.valueOf(timeLeft.intValue() - 1));
            }
        });
    }

    public void startJunkTimer(Player player) {
        for (Location loc : this.junkChestLocations)
            loc.getBlock().setTypeId(Material.CHEST.getId(), false);
        this.junkDimensionPlayerTimers.put(player, Integer.valueOf(30));
        this.dungeon.teleportPlayer(this.junkDimensionLocation, player, true);
        player.sendMessage(CC.GoldB + "(DUNGEON) " + CC.White + "You have been transported to a mysterious dimension... Maybe there's a key somewhere?");
        fillRandomChest();
    }

    private void fillRandomChest() {
        Location chest = this.junkChestLocations.get(ThreadLocalRandom.current().nextInt(this.junkChestLocations.size()));
        Block block = chest.getBlock();
        Material type = block.getType();
        if (type != Material.CHEST && type != Material.TRAPPED_CHEST)
            block.setType(Material.CHEST);
        Chest chestBlock = (Chest)block.getState();
        if (chestBlock != null) {
            chestBlock.getInventory().addItem(new ItemStack[] { this.dungeon.createJunkReturningItem() });
            chestBlock.update();
            this.chestsWithKeys.add(chest);
        }
    }

    public void onPlayerInteract(PlayerInteractEvent event) {
        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) && event.hasItem() &&
                this.dungeon.isJunkOfReturning(event.getItem())) {
            Player player = event.getPlayer();
            event.setCancelled(true);
            event.setUseItemInHand(Event.Result.DENY);
            player.getInventory().remove(event.getItem());
            player.updateInventory();
            if (!WorldGuardUtils.isInRegionsContainsFast(player.getLocation(), Lists.newArrayList(new String[] { "junk_realm" }))) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot use this item outside of the Junk Dimension!");
                player.playSound(player.getLocation(), Sound.GHAST_SCREAM, 1.0F, 0.9F);
                return;
            }
            this.junkDimensionPlayerTimers.remove(player);
            player.sendMessage(CC.GoldB + "(DUNGEON) " + CC.White + "You managed to find your way back to the Portal Dimension!");
            if ((this.dungeon.getDungeonEvents()).reversingPortalOrder) {
                this.dungeon.getDungeonEvents().teleportPlayerToLastPortal(player);
            } else {
                this.dungeon.getDungeonEvents().teleportPlayerToFirstPortal(player);
            }
            player.playSound(player.getLocation(), Sound.AMBIENCE_CAVE, 4.0F, 1.1F);
            player.playSound(player.getLocation(), Sound.GHAST_MOAN, 1.0F, 1.1F);
            player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 40, 0));
            return;
        }
        Block block = event.getClickedBlock();
        if (block == null)
            return;
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK && this.chestsWithKeys.remove(block.getLocation())) {
            event.getPlayer().sendMessage(CC.GreenB + "(!) " + CC.Green + "You have located the Clock of Returning!");
            event.getPlayer().sendMessage(CC.Gray + "Right-Click it to return to the Portal Dimension!");
        }
    }

    public void onPlayerTeleport(PlayerTeleportEvent event) {}

    public List<Location> getLocations(Dungeon d, List<Location> list) {
        list.add(new Location(d.getWorld(), -446.0D, 69.0D, 7.0D));
        list.add(new Location(d.getWorld(), -445.0D, 69.0D, 9.0D));
        list.add(new Location(d.getWorld(), -443.0D, 69.0D, 9.0D));
        list.add(new Location(d.getWorld(), -441.0D, 69.0D, 9.0D));
        list.add(new Location(d.getWorld(), -441.0D, 69.0D, 9.0D));
        list.add(new Location(d.getWorld(), -440.0D, 69.0D, 7.0D));
        list.add(new Location(d.getWorld(), -440.0D, 69.0D, 7.0D));
        list.add(new Location(d.getWorld(), -450.0D, 69.0D, 17.0D));
        list.add(new Location(d.getWorld(), -448.0D, 69.0D, 12.0D));
        list.add(new Location(d.getWorld(), -450.0D, 69.0D, 11.0D));
        list.add(new Location(d.getWorld(), -446.0D, 69.0D, 21.0D));
        list.add(new Location(d.getWorld(), -445.0D, 69.0D, 19.0D));
        list.add(new Location(d.getWorld(), -443.0D, 69.0D, 19.0D));
        list.add(new Location(d.getWorld(), -441.0D, 69.0D, 19.0D));
        list.add(new Location(d.getWorld(), -440.0D, 69.0D, 21.0D));
        list.add(new Location(d.getWorld(), -438.0D, 69.0D, 16.0D));
        list.add(new Location(d.getWorld(), -436.0D, 69.0D, 17.0D));
        list.add(new Location(d.getWorld(), -445.0D, 69.0D, 24.0D));
        list.add(new Location(d.getWorld(), -443.0D, 69.0D, 24.0D));
        list.add(new Location(d.getWorld(), -441.0D, 69.0D, 24.0D));
        list.add(new Location(d.getWorld(), -439.0D, 69.0D, 22.0D));
        list.add(new Location(d.getWorld(), -438.0D, 69.0D, 21.0D));
        list.add(new Location(d.getWorld(), -437.0D, 69.0D, 20.0D));
        list.add(new Location(d.getWorld(), -436.0D, 69.0D, 19.0D));
        list.add(new Location(d.getWorld(), -435.0D, 69.0D, 18.0D));
        list.add(new Location(d.getWorld(), -433.0D, 69.0D, 16.0D));
        list.add(new Location(d.getWorld(), -433.0D, 69.0D, 14.0D));
        list.add(new Location(d.getWorld(), -433.0D, 69.0D, 12.0D));
        list.add(new Location(d.getWorld(), -435.0D, 69.0D, 10.0D));
        list.add(new Location(d.getWorld(), -436.0D, 69.0D, 9.0D));
        list.add(new Location(d.getWorld(), -437.0D, 69.0D, 8.0D));
        list.add(new Location(d.getWorld(), -438.0D, 69.0D, 7.0D));
        list.add(new Location(d.getWorld(), -439.0D, 69.0D, 6.0D));
        list.add(new Location(d.getWorld(), -441.0D, 69.0D, 4.0D));
        list.add(new Location(d.getWorld(), -443.0D, 69.0D, 4.0D));
        list.add(new Location(d.getWorld(), -445.0D, 69.0D, 4.0D));
        list.add(new Location(d.getWorld(), -447.0D, 69.0D, 6.0D));
        list.add(new Location(d.getWorld(), -448.0D, 69.0D, 7.0D));
        list.add(new Location(d.getWorld(), -449.0D, 69.0D, 8.0D));
        list.add(new Location(d.getWorld(), -450.0D, 69.0D, 6.0D));
        list.add(new Location(d.getWorld(), -453.0D, 69.0D, 12.0D));
        list.add(new Location(d.getWorld(), -453.0D, 69.0D, 14.0D));
        list.add(new Location(d.getWorld(), -453.0D, 69.0D, 16.0D));
        list.add(new Location(d.getWorld(), -451.0D, 69.0D, 18.0D));
        list.add(new Location(d.getWorld(), -450.0D, 69.0D, 19.0D));
        list.add(new Location(d.getWorld(), -449.0D, 69.0D, 20.0D));
        list.add(new Location(d.getWorld(), -448.0D, 69.0D, 21.0D));
        list.add(new Location(d.getWorld(), -447.0D, 69.0D, 22.0D));
        return list;
    }
}
