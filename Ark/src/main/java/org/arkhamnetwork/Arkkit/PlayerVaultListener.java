package org.arkhamnetwork.Arkkit;

import com.drtshock.playervaults.vaultmanagement.VaultOperations;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.List;

class PlayerVaultListener
        implements Listener {
    private final PlayerVaultGUIManager manager;
    List<String> vaultAliases = Lists.newArrayList("/pv", "/vault", "/chest", "/playervaults");

    public PlayerVaultListener(PlayerVaultGUIManager man) {
        this.manager = man;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerPreProcess(PlayerCommandPreprocessEvent event) {
        if (this.vaultAliases.contains(event.getMessage().toLowerCase())) {
            event.setCancelled(true);
            event.setMessage("/null");
            event.getPlayer().openInventory(this.manager.getVaultsInventory(event.getPlayer()));
        }
    }

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getName().equals("Select Vault Icon")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            ItemStack item = event.getCurrentItem();
            if (item == null || item.getType() == Material.AIR) {
                return;
            }
            if (event.getRawSlot() < 0 || event.getRawSlot() >= event.getInventory().getSize()) {
                return;
            }
            if (player.hasMetadata("pv_icon_edit")) {
                CustomPV pv;
                int vaultNumber = (player.getMetadata("pv_icon_edit").get(0)).asInt();
                if (!VaultOperations.checkPerms(player, vaultNumber)) {
                    player.sendMessage(ChatColor.RED + "That vault is no longer valid!");
                    player.closeInventory();
                    return;
                }
                CustomPlayerVaults customVaults = (CustomPlayerVaults) this.manager.getCustomPvs().get(player.getUniqueId());
                if (customVaults == null) {
                    customVaults = new CustomPlayerVaults();
                    this.manager.getCustomPvs().put(player.getUniqueId(), customVaults);
                }
                CustomPV customPV = pv = customVaults.isCustomPV(vaultNumber) ? customVaults.get(vaultNumber) : new CustomPV();
                if (!customVaults.isCustomPV(vaultNumber)) {
                    customVaults.put(vaultNumber, pv);
                }
                pv.setIcon(item.getType());
                player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "Saved Vault #" + vaultNumber + " icon to " + ChatColor.GREEN + ChatColor.UNDERLINE.toString() + StringUtils.capitaliseAllWords(item.getType().name().toLowerCase().replace("_", " ")));
                player.playSound(player.getLocation(), Sound.CLICK, 1.0f, 0.8f);
                player.removeMetadata("pv_icon_edit", Arkkit.get());
                player.closeInventory();
                player.chat("/pv");
            } else {
                player.closeInventory();
            }
        } else if (event.getInventory().getName().equals("Player Vaults")) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            ItemStack clicked = event.getCurrentItem();
            if (clicked == null || clicked.getType() == Material.AIR) {
                return;
            }
            if (!(clicked.hasItemMeta() && clicked.getItemMeta().hasDisplayName() && clicked.getItemMeta().hasLore())) {
                return;
            }
            if (event.getRawSlot() < 0 || event.getRawSlot() >= event.getInventory().getSize()) {
                return;
            }
            if (clicked.getItemMeta().getLore().size() <= 0) {
                return;
            }
            String lore = clicked.getItemMeta().getLore().get(0);
            if (ChatColor.stripColor(lore).startsWith("/pv ")) {
                String number = lore.split("/pv ")[1];
                if (!StringUtils.isNumeric(number)) {
                    return;
                }
                int num = Integer.parseInt(number);
                if (event.getClick() == ClickType.LEFT) {
                    player.closeInventory();
                    player.setMetadata("viewing_pvs", new FixedMetadataValue(Arkkit.get(), ""));
                    player.chat("/pv " + num);
                } else if (event.getClick() == ClickType.RIGHT) {
                    player.closeInventory();
                    player.openInventory(this.manager.getMaterialSelection());
                    player.setMetadata("pv_icon_edit", new FixedMetadataValue(Arkkit.get(), num));
                } else if (event.getClick() == ClickType.MIDDLE) {
                    player.closeInventory();
                    player.sendMessage("");
                    player.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "(!) " + ChatColor.AQUA + "Please enter your new desired Vault name.");
                    player.sendMessage(ChatColor.GRAY + "If you wish to cancel type '" + ChatColor.RED + "cancel" + ChatColor.GRAY + "' in the chat.");
                    player.sendMessage("");
                    player.setMetadata("pv_name_edit", new FixedMetadataValue(Arkkit.get(), num));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (player.hasMetadata("pv_name_edit")) {
            CustomPV customPv;
            CustomPlayerVaults vaults;
            event.setCancelled(true);
            int vaultNumber = (player.getMetadata("pv_name_edit").get(0)).asInt();
            String name = event.getMessage();
            if (name.equalsIgnoreCase("cancel") || vaultNumber <= 0 || name.trim().isEmpty()) {
                player.removeMetadata("pv_name_edit", Arkkit.get());
                player.sendMessage(ChatColor.RED + "Vault naming " + ChatColor.RED + ChatColor.BOLD.toString() + "CANCELLED!");
                return;
            }
            if (name.length() >= 32) {
                player.sendMessage(ChatColor.RED + "Vault names cannot be longer than 32 characters.");
                return;
            }
            player.removeMetadata("pv_name_edit", Arkkit.get());
            CustomPlayerVaults customPlayerVaults = vaults = this.manager.getCustomPvs().containsKey(player.getUniqueId()) ? (CustomPlayerVaults) manager.getCustomPvs().get(player.getUniqueId()) : new CustomPlayerVaults();
            if (!manager.getCustomPvs().containsKey(player.getUniqueId())) {
                manager.getCustomPvs().put(player.getUniqueId(), vaults);
            }
            CustomPV customPV = customPv = vaults.isCustomPV(vaultNumber) ? vaults.get(vaultNumber) : new CustomPV();
            if (!vaults.isCustomPV(vaultNumber)) {
                vaults.storePV(vaultNumber, customPv);
            }
            customPv.setCustomName(name);
            player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "Vault #" + vaultNumber + " renamed to " + name);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Arkkit.get(), () -> player.chat("/pv"));
            event.setMessage(" ");
        }
    }

    @EventHandler
    public void onPLayerOpen(InventoryOpenEvent event) {
        Player player = (Player) event.getPlayer();
        if (player.hasMetadata("pv_name_edit")) {
            player.removeMetadata("pv_name_edit", Arkkit.get());
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + "Vault naming cancelled.");
        }
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        Player player;
        if (event.getPlayer().hasMetadata("pv_icon_edit")) {
            event.getPlayer().removeMetadata("pv_icon_edit", Arkkit.get());
        }
        if ((player = (Player) event.getPlayer()).hasMetadata("viewing_pvs")) {
            player.removeMetadata("viewing_pvs", Arkkit.get());
            if (ChatColor.stripColor(event.getInventory().getName()).startsWith("Vault #")) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(Arkkit.get(), () -> player.chat("/pv"), 1L);
            }
        }
    }
}

