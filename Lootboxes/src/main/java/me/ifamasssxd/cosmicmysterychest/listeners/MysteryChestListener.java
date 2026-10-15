/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.compat.ASound
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerCommandPreprocessEvent
 *  org.bukkit.event.player.PlayerInteractEntityEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerJoinEvent
 *  org.bukkit.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmysterychest.listeners;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.data.LootboxFilterData;
import me.ifamasssxd.cosmicmysterychest.exceptions.MysteryChestException;
import me.ifamasssxd.cosmicmysterychest.guis.ActiveMysteryChestGUI;
import me.ifamasssxd.cosmicmysterychest.guis.FilterGUI;
import me.ifamasssxd.cosmicmysterychest.guis.MysteryChestGUI;
import me.ifamasssxd.cosmicmysterychest.struct.GlobalMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;

public class MysteryChestListener
        implements Listener {
    @EventHandler
    public void onEntityView(PlayerInteractEntityEvent event) {
        if (event.getRightClicked() instanceof Player && event.getRightClicked().hasMetadata("NPC") && ((Player) event.getRightClicked()).getName().equalsIgnoreCase("Lootbox Merchant")) {
            event.setCancelled(true);
            GlobalMysteryChest chest = CosmicMysteryChest.get().getChestManager().getActiveMysteryChest();
            if (chest != null) {
                new ActiveMysteryChestGUI(event.getPlayer(), CosmicMysteryChest.get().getChestManager().getActiveMysteryChest()).openGUI(CosmicMysteryChest.get());
                ASound.BAT_TAKEOFF.play(event.getPlayer(), 1.1f);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerPreProcess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage() == null) {
            return;
        }
        String msg = event.getMessage().toLowerCase();
        if (msg.startsWith("/lootbox")) {
            event.setMessage(msg.replace("/lootbox", "/mc"));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerBlock(BlockPlaceEvent event) {
        NBTTagCompoundBuilder builder;
        NBTWrapper wrapper;
        if (event.getItemInHand() != null && event.getItemInHand().getType() != Material.AIR && (wrapper = new NBTWrapper(event.getItemInHand())).hasKey("conaxData") && (builder = wrapper.getCompound("conaxData")) != null && builder.hasKey("mysteryChest")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot place a Lootbox!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onNBTInteract(PlayerInteractEvent event) {
        ItemStack d = new ItemStack(Material.DIAMOND);
        if (d.getAmount() > 1) {
            event.setCancelled(false);
        }

        if ((event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) && event.hasItem()) {
            ItemStack item = event.getItem().clone();
            if (item == null || item.getType() == Material.AIR) {
                return;
            }

            NBTTagCompoundBuilder builder = null;
            NBTWrapper wrapper = new NBTWrapper(item);
            try {
                builder = wrapper.getCompound("conaxData");
            } catch (NullPointerException ignored) {
            }
            if (builder == null) {
                try {
                    builder = wrapper.getCompound("conaxData");
                } catch (NullPointerException ignored) {
                }
            }
            if (builder != null && builder.hasKey("mysteryChest")) {
                event.setCancelled(true);
                Player player = event.getPlayer();
                try {
                    MysteryChest chest = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(builder.getString("mysteryChest"));
                    if (chest == null) return;
                    int bundleSize = chest.getItemBundleSize();
                    if (bundleSize == 0) {
                        bundleSize = chest.getGarunteedItems().size();
                    }
                    if (getEmptySlots(player) < bundleSize) {
                        throw new MysteryChestException(CC.translate("&c&l(!)&c Please clear some inventory slots before opening this lootbox!"));
                    }

                    if (player.getLocation().getWorld().getName().equals("world_duels")) {
                        player.sendMessage(CC.translate("&c&l(!)&c You cannot open lootboxes in a duel match"));
                        event.setCancelled(true);
                        return;
                    }

                    if (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                        new FilterGUI(player, chest).getFilterGUI().open(player);
                        return;
                    }

                    if (event.getPlayer().isSneaking()){
                        int counter = 0;
                        while (counter != event.getItem().getAmount()) {
                            new MysteryChestGUI(player, chest, 0L).skipGUI(CosmicMysteryChest.get());
                            counter++;
                        }
                        player.setItemInHand(null);
                    } else {
                        int amt = event.getItem().getAmount();
                        if (amt == 1) {
                            player.setItemInHand(null);
                        } else if (amt > 1) {
                            ItemStack i = event.getItem();
                            i.setAmount(i.getAmount() - 1);
                            player.setItemInHand(i);
                        }

                        new MysteryChestGUI(player, chest, 0L).skipGUI(CosmicMysteryChest.get());
                    }
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 3.0f);


                } catch (MysteryChestException exception) {
                    player.sendMessage(CC.RedB + CC.Red + exception.getMessage());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void joinEvent(PlayerJoinEvent e) {
        CosmicMysteryChest.get().getShardAnimations().anotoggle(e.getPlayer());
    }

    public int getEmptySlots(Player p) {
        PlayerInventory inventory = p.getInventory();
        ItemStack[] cont = inventory.getContents();
        int i = 0;
        for (ItemStack item : cont)
            if (item != null && item.getType() != Material.AIR) {
                i++;
            }
        return 36 - i;
    }
}

