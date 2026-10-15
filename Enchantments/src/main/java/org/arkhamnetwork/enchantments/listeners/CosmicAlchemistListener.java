package org.arkhamnetwork.enchantments.listeners;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.google.common.collect.Lists;
import org.arkhamnetwork.enchantments.CosmicAlchemist;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.ExpUtils;
import org.arkhamnetwork.enchantments.util.ItemUtils;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class CosmicAlchemistListener implements Listener {
    @EventHandler
    public void onPlayerInteractEntityEvent(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Player && e.getRightClicked().hasMetadata("NPC")) {
            final Player pNPC = (Player) e.getRightClicked();
            if (pNPC.getName().equals("Cosmic Alchemist")) {
                e.getPlayer().openInventory(CosmicAlchemist.getExchangeInventory());
                e.getPlayer().playSound(e.getPlayer().getLocation(), Sound.BAT_TAKEOFF, 1.0f, 0.4f);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCosmicAlchemistInventoryClick(final InventoryClickEvent e) throws Exception {
        if (e.getInventory().getTitle().equals("Cosmic Alchemist")) {
            e.setCancelled(true);
            e.setResult(Event.Result.DENY);
            final Player p = (Player) e.getWhoClicked();
            final Inventory wizardInv = p.getOpenInventory().getTopInventory();
            if (EnchantmentBook.isEnchantmentBook(e.getCurrentItem()) || EnchantmentBook.isMagicDust(e.getCurrentItem())) {
                final ItemStack currentItem = e.getCurrentItem().clone();
                if (e.getWhoClicked().getInventory().equals(wizardInv)) {
                    if (e.getRawSlot() == 3 || e.getRawSlot() == 5) {
                        e.setCurrentItem((ItemStack) null);
                        if (p.getInventory().firstEmpty() != -1) {
                            p.getInventory().setItem(p.getInventory().firstEmpty(), currentItem);
                        } else {
                            p.getInventory().addItem(new ItemStack[]{currentItem});
                        }
                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.25f);
                    }
                    if (wizardInv.getItem(22).equals((Object) CosmicAlchemist.confirmed)) {
                        wizardInv.setItem(22, CosmicAlchemist.pending);
                        wizardInv.setItem(3, (ItemStack) null);
                        wizardInv.setItem(5, (ItemStack) null);
                        wizardInv.setItem(13, (ItemStack) null);
                    }
                } else if (wizardInv.firstEmpty() != -1) {
                    final List<ItemStack> currentOffer = this.getAlchemistItems(wizardInv);
                    if (currentOffer.size() > 0 && ((EnchantmentBook.isMagicDust(currentItem) && !EnchantmentBook.isMagicDust(currentOffer.get(0))) || (EnchantmentBook.isEnchantmentBook(currentItem) && !EnchantmentBook.isEnchantmentBook(currentOffer.get(0))))) {
                        p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You may only combine dust (with dust) or books (with books).");
                        return;
                    }
                    currentOffer.add(e.getCurrentItem());
                    if (!this.isOfferValid(currentOffer, p)) {
                        return;
                    }
                    final ItemStack itemToDeposit = currentItem.clone();
                    itemToDeposit.setAmount(1);
                    if (e.getCurrentItem().getAmount() > 1) {
                        currentItem.setAmount(currentItem.getAmount() - 1);
                        e.setCurrentItem(currentItem);
                    } else {
                        e.setCurrentItem((ItemStack) null);
                    }
                    wizardInv.setItem(wizardInv.firstEmpty(), itemToDeposit);
                    p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.5f);
                } else {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You may only combine up to TWO (2) items at once.");
                }
                if (wizardInv.firstEmpty() == -1) {
                    wizardInv.setItem(22, CosmicAlchemist.getAcceptItem(p, this.getAlchemistItems(wizardInv)));
                    wizardInv.setItem(13, ItemUtils.addGlow(this.getCombinedItem(this.getAlchemistItems(wizardInv))));
                } else {
                    wizardInv.setItem(22, CosmicAlchemist.pending);
                    wizardInv.setItem(13, CosmicAlchemist.preview);
                }
                return;
            }
            if (e.getRawSlot() != 22 && e.getCurrentItem() != null) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "The Cosmic Alchemist does not want that item.");
                p.sendMessage(ChatColor.GRAY + "This NPC will only accept Enchantment Books and Magic Dust.");
                return;
            }
            if (e.getRawSlot() == 22 && e.getCurrentItem().equals((Object) CosmicAlchemist.getAcceptItem(p, this.getAlchemistItems(wizardInv))) && wizardInv.firstEmpty() == -1) {
                final List<ItemStack> items = this.getAlchemistItems(wizardInv);
                final int cost = CosmicAlchemist.getAlchemistCost(p, items);
                if (ExpUtils.getTotalExperience(p) < cost) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You do not have enough EXP to combine these items.");
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "COST " + ChatColor.GRAY + cost + ChatColor.RED + "XP");
                    p.sendMessage(ChatColor.GRAY + "Use /xp to view your EXP points.");
                    return;
                }
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Your inventory is full.");
                    return;
                }
                final List<ItemStack> finalOffer = new ArrayList<ItemStack>();
                finalOffer.add(wizardInv.getItem(3));
                finalOffer.add(wizardInv.getItem(5));
                if (!this.isOfferValid(finalOffer, p)) {
                    return;
                }
                final ItemStack newItem = this.getCombinedItem(finalOffer);
                ExpUtils.setTotalExperience(p, ExpUtils.getTotalExperience(p) - cost);
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + " - " + cost + "XP");

                wizardInv.setItem(3, ItemUtils.setTypeAndDurability(wizardInv.getItem(3), Material.STAINED_GLASS_PANE, (short) 5));
                wizardInv.setItem(5, ItemUtils.setTypeAndDurability(wizardInv.getItem(5), Material.STAINED_GLASS_PANE, (short) 5));
                wizardInv.setItem(22, CosmicAlchemist.confirmed);
                p.getInventory().addItem(new ItemStack[]{newItem});
                p.closeInventory();
                p.playSound(p.getLocation(), Sound.VILLAGER_YES, 1.0f, 0.3f);
                de.inventivegames.particle.ParticleEffect.HAPPY_VILLAGER.sendToPlayers(Bukkit.getOnlinePlayers(), p.getLocation(), 0f,0.5f,0f,0.55f,16);

                //Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "contest add " + p.getName() + " alchemistExch 1");
                Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(p, "ALCHEMIST_TRADE", new Object[]{items, Lists.newArrayList( new ItemStack[]{newItem})}));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onCosmicAlchemistInventoryClose(final InventoryCloseEvent e) {
        if (e.getInventory().getTitle().equals("Cosmic Alchemist")) {
            final Player p = (Player) e.getPlayer();
            for (int i = 3; i <= 5; ++i) {
                if (i != 4) {
                    if ((e.getInventory().getItem(i) != null && EnchantmentBook.isEnchantmentBook(e.getInventory().getItem(i))) || EnchantmentBook.isMagicDust(e.getInventory().getItem(i))) {
                        if (p.getInventory().firstEmpty() != -1) {
                            p.getInventory().addItem(new ItemStack[]{e.getInventory().getItem(i)});
                        } else {
                            p.getWorld().dropItem(p.getLocation(), e.getInventory().getItem(i));
                        }
                    }
                }
            }
            Bukkit.getScheduler().runTaskLater((Plugin) Enchantments.getInstance(), (Runnable) new Runnable() {
                @Override
                public void run() {
                    p.updateInventory();
                }
            }, 2L);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryCloseTinkerer(final InventoryCloseEvent e) {
        if (e.getInventory().getTitle().equals("Cosmic Alchemist")) {
            e.getInventory().clear();
        }
    }

    @EventHandler
    public void onPlayerPickupItem(final PlayerPickupItemEvent e) {
        if (e.getPlayer().getOpenInventory().getTitle().equals("Cosmic Alchemist")) {
            e.setCancelled(true);
        }
    }

    private List<ItemStack> getAlchemistItems(final Inventory wizardInv) {
        final List<ItemStack> items = new ArrayList<ItemStack>();
        if (wizardInv.getItem(3) != null) {
            items.add(wizardInv.getItem(3));
        }
        if (wizardInv.getItem(5) != null) {
            items.add(wizardInv.getItem(5));
        }
        return items;
    }

    private ItemStack getCombinedItem(final List<ItemStack> finalOffer) {
        ItemStack newItem = null;
        if (EnchantmentBook.isEnchantmentBook(finalOffer.get(0))) {
            newItem = CosmicAlchemist.getCombinedBook(finalOffer);
        } else if (EnchantmentBook.isMagicDust(finalOffer.get(0))) {
            newItem = CosmicAlchemist.getCombinedDust(finalOffer);
        }
        return newItem;
    }

    private boolean isOfferValid(final List<ItemStack> offer, final Player p) {
        if (EnchantmentBook.isEnchantmentBook(offer.get(0))) {
            if (!CosmicAlchemist.areBooksSameType(offer)) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You may only combine books of the same enchantment type.");
                return false;
            }
            if (!CosmicAlchemist.areBooksSameLevel(offer)) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You may only combine books with the same enchantment level.");
                return false;
            }
            for (final ItemStack i : offer) {
                if (EnchantmentBook.getEnchantmentLevel(i) >= EnchantmentBook.getEnchantment(i).getMaxLevel()) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "The Cosmic Alchemist cannot combine max level enchantment books.");
                    return false;
                }
                if (TieredEnchantments.getEnchantmentTier(EnchantmentBook.getEnchantment(i).name()) >= 6) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "The Cosmic Alchemist cannot combine soul or heroic enchantment books.");
                    return false;
                }
            }
        }
        if (EnchantmentBook.isMagicDust(offer.get(0))) {
            if (!CosmicAlchemist.isDustSameTier(offer)) {
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "You may only combine dust of the same tier.");
                return false;
            }
            for (final ItemStack i : offer) {
                if (CosmicAlchemist.getMagicDustRarity(i) == ItemRarity.LEGENDARY || CosmicAlchemist.getMagicDustRarity(i) == ItemRarity.UNKNOWN) {
                    p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "The Cosmic Alchemist cannot combine legendary dust.");
                    return false;
                }
            }
        }
        return true;
    }
}
