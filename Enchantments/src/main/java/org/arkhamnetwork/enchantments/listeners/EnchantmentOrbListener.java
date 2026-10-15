package org.arkhamnetwork.enchantments.listeners;

import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.EnchantmentOrb;
import org.arkhamnetwork.enchantments.libs.OrbType;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.arkhamnetwork.enchantments.util.ParticleEffect;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class EnchantmentOrbListener implements Listener {
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onPlayerInteract(final PlayerInteractEvent e) {
        if (e.hasItem() && EnchantmentOrb.isEnchantmentOrb(e.getItem())) {
            e.setCancelled(true);
            e.setUseItemInHand(Event.Result.DENY);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.getPlayer().sendMessage(ChatColor.YELLOW + "" + ChatColor.BOLD + "(!)" + ChatColor.YELLOW + " To apply this enchantment orb to an item, simply drag n' drop the orb onto the piece of equipment you'd like to increase the enchantment slots of in your inventory!");
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEnchantmentOrbApply(final InventoryClickEvent e) throws Exception {
        if (!e.getInventory().getTitle().equals("container.crafting")) {
            return;
        }
        if (e.getCursor() != null && EnchantmentOrb.isEnchantmentOrb(e.getCursor())) {
            if (e.getRawSlot() <= 3 && e.getRawSlot() >= 0 && e.getSlotType() != InventoryType.SlotType.QUICKBAR) {
                return;
            }
            if (e.getCurrentItem() != null) {
                final Player pl = (Player) e.getWhoClicked();
                if (!EnchantmentBook.isEquipment(e.getCurrentItem()) || e.getCurrentItem().getMaxStackSize() != 1) {
                    return;
                }
                final int successRate = EnchantmentOrb.getEnchantmentOrbSuccessRate(e.getCursor());
                final int maxSlots = EnchantmentOrb.getEnchantmentOrbMaxSlots(e.getCursor());
                if (EnchantmentOrb.getEnchantmentOrbType(e.getCursor()) == OrbType.ARMOR && !GeneralUtil.isArmor(e.getCurrentItem())) {
                    pl.sendMessage(ChatColor.RED + "(!) This is an Armor Enchantment Orb and can only be used on armor pieces.");
                    return;
                }
                if (EnchantmentOrb.getEnchantmentOrbType(e.getCursor()) == OrbType.WEAPON && !GeneralUtil.isWeapon(e.getCurrentItem())) {
                    pl.sendMessage(ChatColor.RED + "(!) This is a Weapon Enchantment Orb and can only be used on weapons.");
                    return;
                }
                if (EnchantmentOrb.getExtraEnchantmentSlots(e.getCurrentItem()) + 9 >= maxSlots) {
                    pl.sendMessage(ChatColor.RED + "(!) This Enchantment Orb can only increase an item's maximum enchantment slots up to " + maxSlots + ".");
                    return;
                }
                if (EnchantmentBook.getCustomEnchantmentCount(e.getCurrentItem()) >= maxSlots) {
                    pl.sendMessage(ChatColor.RED + "(!) This item has more enchantments on it than this Enchantment Orb allows.");
                    return;
                }
                if (e.getCursor().getAmount() > 1) {
                    final ItemStack orb = e.getCursor().clone();
                    orb.setAmount(orb.getAmount() - 1);
                    pl.setItemOnCursor(orb);
                } else {
                    pl.setItemOnCursor((ItemStack) null);
                    if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                        final long itemID = ItemID.getItemID(e.getCursor());
                        if (itemID != -1L) {
                            ItemID.blacklistIID(itemID, "enchantmentOrbUse");
                        }
                    }
                }
                boolean success = false;
                final ItemStack to_enchant = e.getCurrentItem();
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                if (new Random().nextInt(100) < successRate) {
                    success = true;
                    EnchantmentOrb.applyEnchantmentOrb(to_enchant, maxSlots);
                    pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 0.75f);
                    pl.playEffect(EntityEffect.WOLF_HEARTS);
                    final Location l = pl.getLocation();
                    de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(),  l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(),  l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.65f, 30);

                } else {
                    pl.playSound(pl.getLocation(), Sound.LAVA_POP, 1.0f, 0.75f);
                    pl.playEffect(EntityEffect.WOLF_HEARTS);
                    final Location l = pl.getLocation();
                    de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(),  l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.35f, 20);

                }
                pl.updateInventory();
                Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(pl, "ENCHANT_ORB", new Object[]{success, to_enchant}));
            }
        }
    }
}
