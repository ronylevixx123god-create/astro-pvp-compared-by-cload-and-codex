package com.bryside.astroattributes.listeners;

import com.bryside.astroattributes.DragonTitanAttributesAPI;
import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.events.RuneApplyEvent;
import com.bryside.astroattributes.utils.Utils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ParticleEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class RuneApplyListener implements Listener {
    @EventHandler(
            ignoreCancelled = true
    )
    public void apply(InventoryClickEvent e) {
        if (e.getCursor() != null && e.getCurrentItem() != null && e.getWhoClicked() instanceof Player) {
            ItemStack cursor = e.getCursor();
            ItemStack current = e.getCurrentItem();
            if (cursor.hasItemMeta() && current.hasItemMeta() && cursor.getItemMeta().hasLore() && cursor.getItemMeta().hasDisplayName() && current.getItemMeta().hasLore()) {
                if (Utils.isWeapon(current)) {
                    RuneType type = RuneType.getType(cursor);
                    Player player = (Player) e.getWhoClicked();
                    if (type != null) {
                        ItemMeta currentMeta = current.getItemMeta();
                        Iterator var7 = currentMeta.getLore().iterator();

                        String l;
                        do {
                            if (!var7.hasNext()) {
                                if (DragonTitanAttributesAPI.isRuneCorrupted(current)) {
                                    player.sendMessage(CC.Red + "ERROR: That item is Titan Corrupted. You need to apply a Holy Grail to uncorrupt it.");
                                    return;
                                }

                                int success = RuneType.getSuccessRateOfRune(cursor);
                                int level = RuneType.getLevelOfRune(cursor);
                                RuneApplyEvent event = new RuneApplyEvent(player, current, type, success, level);
                                Bukkit.getServer().getPluginManager().callEvent(event);
                                if (cursor.getAmount() == 1) {
                                    player.setItemOnCursor((ItemStack) null);
                                } else {
                                    cursor.setAmount(cursor.getAmount() - 1);
                                }

                                player.updateInventory();
                                e.setCancelled(true);
                                return;
                            }

                            l = (String) var7.next();
                        } while (!l.startsWith(CC.RedB + "TITAN ATTRIBUTE:"));

                    }
                }
            }
        }
    }

    @EventHandler(
            priority = EventPriority.MONITOR,
            ignoreCancelled = true
    )
    public void apply(RuneApplyEvent e) throws Exception {
        Player player = e.getPlayer();
        int success = e.getSuccess();
        int level = e.getLevel();
        ItemStack item = e.getItem();
        RuneType type = e.getRuneType();
        Location l = player.getLocation();

        if (item.getItemMeta().getLore().contains(CC.translate("&9&l * &7Extra HP From Potions (+1.0 HP)"))) {
            e.setCancelled(true);
            player.getInventory().addItem(e.getRuneType().getRune().getItemStack(level, success));
            player.sendMessage(CC.translate("&cYou cannot apply a titan attribute to an item with weapon bonus runes."));
            return;
        }


        if (success < ThreadLocalRandom.current().nextInt(1, 101)) {
            player.sendMessage("");
            player.sendMessage(CC.RedB + "TITAN ATTRIBUTE FAILED TO APPLY");
            player.sendMessage("");
            player.sendMessage(CC.DarkRedB + "Attribute: " + CC.Red + type.name());
            player.sendMessage(CC.DarkRedB + "Level: " + CC.Red + level);
            player.sendMessage(CC.DarkRedB + "Success Rate: " + CC.Red + success + "%");
            player.playSound(player.getLocation(), Sound.LAVA_POP, 1.0F, 0.75F);
            de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0D, 1.0D, 0.0D), (new Random()).nextFloat(), (new Random()).nextFloat(), (new Random()).nextFloat(), 0.35F, 20);
        } else {
            type.apply(item, type, level);
            player.sendMessage("");
            player.sendMessage(CC.GreenB + "TITAN ATTRIBUTE APPLIED");
            player.sendMessage("");
            player.sendMessage(CC.DarkGreenB + "Attribute: " + CC.Green + type.name());
            player.sendMessage(CC.DarkGreenB + "Level: " + CC.Green + level);
            player.sendMessage(CC.DarkGreenB + "Success Rate: " + CC.Green + success + "%");
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 0.75F);
            de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0D, 1.0D, 0.0D), (new Random()).nextFloat(), (new Random()).nextFloat(), (new Random()).nextFloat(), 0.8F, 85);
            de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0D, 1.0D, 0.0D), (new Random()).nextFloat(), (new Random()).nextFloat(), (new Random()).nextFloat(), 0.8F, 35);

        }
    }
}

