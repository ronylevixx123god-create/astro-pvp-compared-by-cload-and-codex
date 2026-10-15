package org.arkhamnetwork.enchantments.listeners;

import com.compilex.CosmicKOTH.utils.InventoryUtils;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.google.common.collect.Lists;
import com.rit.sucy.enchanting.EListener;
import me.ifamasssxd.cosmicmasks.MaskType;
import net.astropvp.core.guis.SpawnerShopGUI;
import org.arkhamnetwork.Arkkit.patches.ItemNametag;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.enchantments.armor.Reflect;
import org.arkhamnetwork.enchantments.menus.EnchanterGUI;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchanterListener implements Listener {
    private static String enchanterName;
    private static Inventory enchanterInventory;
    private static short WHITE;
    private static short GREEN;
    private static short BLUE;
    private static short YELLOW;
    private static short GOLD;

    static {
        EnchanterListener.enchanterName = "Astro Enchanter";
        EnchanterListener.WHITE = 0;
        EnchanterListener.GREEN = 5;
        EnchanterListener.BLUE = 3;
        EnchanterListener.YELLOW = 4;
        EnchanterListener.GOLD = 1;
    }

    public static double getEnchanterBookPrice(final ItemRarity ir) {
        if (ir == ItemRarity.SIMPLE) {
            return 0.0;
        }
        if (ir == ItemRarity.UNIQUE) {
            return 0.0;
        }
        if (ir == ItemRarity.ELITE) {
            return 0.0;
        }
        if (ir == ItemRarity.ULTIMATE) {
            return 0.0;
        }

        return (ir == ItemRarity.LEGENDARY) ? 0.0 : 2.147483647E9;
    }

    @EventHandler
    public void onPlayerRightClickEnchanter(final PlayerInteractEntityEvent e) {
        if (e.getRightClicked().hasMetadata("NPC") && e.getRightClicked() instanceof Player) {
            final Player pEntity = (Player) e.getRightClicked();
            if (pEntity.getName().equals(EnchanterListener.enchanterName)) {
                final Player p = e.getPlayer();
                e.setCancelled(true);
                new EnchanterGUI(p).openGUI((JavaPlugin) Enchantments.getInstance());
                p.playSound(p.getLocation(), Sound.VILLAGER_HAGGLE, 1.0f, 1.4f);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        Enchantments.playerLocations.put(e.getPlayer(), e.getPlayer().getLocation());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        Enchantments.playerLocations.remove(e.getPlayer());
    }

    @EventHandler
    public void onPlayerExamineBook(final PlayerInteractEvent e) {
        if (e.hasItem() && (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && TieredEnchantments.isMysteryEnchantmentBook(e.getItem())) {
            final Player p = e.getPlayer();
            final ItemStack i = p.getItemInHand();
            final int tier = TieredEnchantments.getBookTier(i);
            if (tier < 1) {
                return;
            }
            if (p.getInventory().firstEmpty() == -1) {
                p.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot examine a book with a full inventory!");
                return;
            }
            if (i.getAmount() > 1) {
                i.setAmount(i.getAmount() - 1);
                p.setItemInHand(i);
            } else {
                p.setItemInHand((ItemStack) null);
            }
            ItemStack book = TieredEnchantments.getRandomEnchantmentBook(tier);
            p.getInventory().addItem(new ItemStack[]{book});
            p.updateInventory();
            p.playSound(p.getLocation(), Sound.BAT_TAKEOFF, 0.7f, 1.4f);
            p.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW.toString() + "You examine the " + i.getItemMeta().getDisplayName().substring(0, i.getItemMeta().getDisplayName().indexOf(ChatColor.GRAY.toString())) + "...");
            p.sendMessage(ChatColor.YELLOW + "and discover " + book.getItemMeta().getDisplayName() + "!");
            //Bukkit.dispatchCommand((CommandSender) Bukkit.getConsoleSender(), "contest add " + p.getName() + " beopened 1");
            Color c = null;
            if (tier == 1) {
                c = Color.WHITE;
            }
            if (tier == 2) {
                c = Color.GREEN;
            }
            if (tier == 3) {
                c = Color.AQUA;
            }
            if (tier == 4) {
                c = Color.YELLOW;
            }
            if (tier == 5) {
                c = Color.BLACK;
            }
            if (tier == 6) {
                c = Color.RED;
            }
            if (c == null) {
                c = Color.BLACK;
            }
        }
    }



    @EventHandler
    public void onClick(InventoryClickEvent e) throws UserDoesNotExistException, NoLoanPermittedException {
        Player p = (Player) e.getWhoClicked();
        if (p.getOpenInventory().getTitle().equals("Astro Enchanter")) {
            e.setCancelled(true);
            if (e.getClickedInventory() == p.getOpenInventory().getTopInventory()) {
                if (p.getInventory().firstEmpty() == -1) {
                    p.sendMessage(CC.translate("&c&l(!)&c You cannot buy this with a full inventory"));
                    return;
                }
                if (e.getSlot() == 9 ) {
                    p.getInventory().addItem(EnchantmentBook.getTransmogScroll());
                } else if (e.getSlot() == 10) {
                    p.getInventory().addItem(EnchantmentBook.getWhiteScroll());
                } else if (e.getSlot() == 11) {
                    p.getInventory().addItem(EnchantmentBook.getBlackScroll(100));
                } else if (e.getSlot() == 12) {
                    p.getInventory().addItem(EnchantmentBook.getHeroicBlackscroll(100,100,false));
                } else if (e.getSlot() == 13) {
                    p.getInventory().addItem(ItemNametag.getNametagItem());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamageOtherPlayer(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player dmgr = null;
            if (event.getDamager() instanceof Player) {
                dmgr = (Player) event.getDamager();
            } else if (event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof Player) {
                dmgr = (Player) ((Projectile) event.getDamager()).getShooter();
            }
            if (dmgr == null)
                return;
            Player finalDmgr = dmgr;
            EListener.getValidEnchantments(dmgr, Lists.newArrayList(dmgr.getInventory().getArmorContents())).asMap().forEach((enchantment, levels) -> {
                if (enchantment instanceof MasteryCustomEnchantment) {
                    MasteryCustomEnchantment enchant = (MasteryCustomEnchantment) enchantment;
                    Player damaged = (Player) event.getEntity();
                    boolean riftMask = MaskType.hasEquipped(damaged, "RIFT");
                    int masteryReflect = Reflect.getReflectLevel((LivingEntity) damaged, "mastery");
                    if (!enchant.canStack()) {
                        if (riftMask && Math.random() <= 0.5D)
                            return;
                        Integer highest = levels.stream().sorted().findFirst().orElse(null);
                        if (highest == null)
                            return;
                        if (masteryReflect >= highest.intValue() && 0.02D + 0.0267D * (masteryReflect / 3) >= Math.random()) {
                            enchant.applyDamagedOtherPlayer(damaged, finalDmgr, event, highest.intValue());
                            Bukkit.getLogger().info("[Enchantments] Reflecting " + finalDmgr.getName() + "'s damage on " + damaged.getName() + " so that " + finalDmgr.getName() + " is the target: " + masteryReflect + " For " + enchant.getClass().getSimpleName());
                        } else {
                            if (masteryReflect >= highest.intValue() && 0.01D + 0.0833D * (masteryReflect / 3) >= Math.random())
                                return;
                            enchant.applyDamagedOtherPlayer(finalDmgr, damaged, event, highest.intValue());
                        }
                    }
                }
            });
        }
    }
}
