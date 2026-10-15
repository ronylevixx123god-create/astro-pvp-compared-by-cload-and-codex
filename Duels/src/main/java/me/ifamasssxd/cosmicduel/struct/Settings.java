/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicduel.struct;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.inventory.CustomInventory;
import me.ifamasssxd.cosmicduel.inventory.CustomInventoryItem;
import me.ifamasssxd.cosmicduel.inventory.CustomInventoryItemCallback;
import me.ifamasssxd.cosmicduel.struct.enums.CancelReason;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelType;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.struct.newduel.GKitSelectionInventory;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmicduel.utils.CustomItem;
import me.ifamasssxd.cosmicduel.utils.EnchantUtils;
import me.ifamasssxd.cosmicduel.utils.ItemBuilder;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Settings {
    NewDuel duel;
    CustomInventory settings;
    List<Setting> currentSettings = new ArrayList<Setting>();
    short confirmData = (short)5;

    public Settings(NewDuel duel) {
        this.duel = duel;
        for (Setting setting : Setting.values()) {
            if (!duel.isRanked() && !setting.isDefaultSetting() || duel.isRanked() && !setting.isRankedDefault()) continue;
            this.currentSettings.add(setting);
        }
        this.settings = new ClassInventory("Duel Settings", 4);
        this.updateSettingsInventory();

    }

    public void updateSettingsInventory() {
        this.settings.clearInventoryItems();
        int index = 0;
        for (Setting setting : Setting.values()) {
            CustomInventoryItem ci = this.getSettingItem(setting);
            if (index == 9) {
                index = 10;
            }
            if (index == 13) {
                // empty if block
            }
            int n = ++index;
            this.settings.insertItem(n, ci, true);
        }
        String itemName = (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Kit";
        Material material = this.getDuel().getKit().getMaterial();
        ArrayList lore = Lists.newArrayList();

            lore.add((Object)ChatColor.GREEN + ChatColor.BOLD.toString() + this.getDuel().getKit().getName(true));

        lore.add((Object)ChatColor.GRAY + "Click to select a kit.");
        ItemStack kit_item = ItemBuilder.buildItem(material, 1, this.getDuel().getKit().getData(), itemName, lore);
        kit_item.addUnsafeEnchantment(EnchantUtils.getCustomEnchant(), 1);
        CustomInventoryItem cii = new CustomInventoryItem(kit_item);
        cii.setCallback((inventory, customItem, event) -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            this.openKitInventory((Player)event.getWhoClicked());
        });
        //this.settings.insertItem(this.settings.getInventorySize() - 1, cii, true);
        //this.settings.insertItem(this.settings.getInventorySize() - 9, cii, true);
        CustomInventoryItem confirm = this.getConfirmButton();
        this.settings.insertItem(this.settings.getInventorySize() - 5, confirm);
        for (int i = 0; i < this.settings.getInventorySize(); ++i) {
            if (this.settings.getItem(i) != null && this.settings.getItem(i).getType() != Material.AIR) continue;
            this.settings.insertItem(i, new CustomInventoryItem(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", " ")), true);
        }
        this.settings.updateInventoryItems();
    }

    public CustomInventoryItem getSettingItem(Setting s) {
        if (s == Setting.DROPITEMS && this.hasSetting(s) && this.getDuel().getKit() != Kit.BYOB) {
            this.currentSettings.remove((Object)s);
        }
        ItemStack is = ItemBuilder.buildItem(s.getIcon(), s.getIcondata(), (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + s.getName(), this.hasSetting(s) ? (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "ENABLED" : (Object)ChatColor.RED + ChatColor.BOLD.toString() + "DISABLED", "", (Object)ChatColor.GRAY + "Click to " + (Object)ChatColor.GRAY + ChatColor.UNDERLINE.toString() + "toggle" + (Object)ChatColor.GRAY + " this setting.");
        if (this.currentSettings.contains((Object)s)) {
            if (s.getIcon() == Material.GOLDEN_APPLE) {
                is.setDurability((short)1);
            } else {
                is.addUnsafeEnchantment(EnchantUtils.getCustomEnchant(), 1);
            }
        }
        CustomInventoryItem ci = new CustomInventoryItem(is);
        ci.setCallback((inventory, customItem, event) -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
            Player p = (Player)event.getWhoClicked();
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            this.toggleSetting(s);
        });
        return ci;
    }

    public CustomInventoryItem getConfirmButton() {
        ArrayList<String> lore = new ArrayList<String>();
        lore.add("");
        for (Setting setting : Setting.values()) {
            lore.add((Object)ChatColor.YELLOW + setting.getName() + ": " + (this.currentSettings.contains((Object)setting) ? (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "ON" : (Object)ChatColor.RED + ChatColor.BOLD.toString() + "OFF"));
        }
        lore.add((Object)ChatColor.YELLOW + "Kit: " + (this.getDuel().getKit().equals((Object)Kit.BYOB) ? (Object)ChatColor.RED + ChatColor.BOLD.toString() + this.getDuel().getKit().getName() : (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + this.getDuel().getKit().getName()));
        lore.add("");
        lore.add((Object)ChatColor.GRAY + "Click to go to the Arena Selection.");
        CustomInventoryItem confirm = new CustomInventoryItem(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, this.confirmData, (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + "Confirm Settings", lore));
        confirm.setCallback(new CustomInventoryItemCallback(){

            @Override
            public void inventoryClickEvent(CustomInventory inventory, CustomInventoryItem customItem, InventoryClickEvent event) {
                Player p = (Player)event.getWhoClicked();
                if (Settings.this.getDuel().getCreateState().equals((Object)DuelCreateState.SETTINGS)) {
                    Settings.this.getDuel().setCreateState(DuelCreateState.ARENA);
                }
                ClassInventory custom = new ClassInventory("Select an Arena", PlayerUtils.getRoundedSize((int)Arena.arenas.stream().filter(a -> !a.isRankedArena()).count()) / 9);
                int index = 0;
                for (Arena arena : Arena.arenas) {
                    if (arena.isRankedArena()) continue;
                    custom.insertItem(index++, Settings.this.getArenaInventoryItem(p, arena));
                }
                p.setMetadata("close_exempt", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDuel.get(), (Object)""));
                custom.openInventory(p);
            }
        });
        return confirm;
    }

    public void openKitInventory(Player p) {
        this.getDuel().setCreateState(DuelCreateState.KIT);
        ClassInventory inv = new ClassInventory("Select a Kit", PlayerUtils.getRoundedSize(Kit.values().length) / 9);
        int index = 0;
        for (Kit k : Kit.values()) {
            CustomInventoryItem item = this.getCustomInventoryItem(p, k);
            inv.insertItem(index++, item);
        }
        for (int i = 0; i < inv.getInventorySize(); ++i) {
            if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) continue;
            inv.insertItem(i, new CustomInventoryItem(new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7)));
        }
        p.setMetadata("close_exempt", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDuel.get(), (Object)""));
        inv.openInventory(p);
    }

    public void invitePlayer(Player p) {
        this.getDuel().setCreateState(DuelCreateState.PLAYER_SELECT);
        p.closeInventory();
        for (String s : new ArrayList<String>(this.getDuel().getPendingInvites())) {
            Player invited = Bukkit.getPlayer((String)s);
            if (invited == null) {
                p.sendMessage((Object)ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + "The player " + s + " is no longer available.");
                p.sendMessage((Object)ChatColor.RED + "The duel has been cancelled.");
                DuelAPI.cancelDuel(this.getDuel(), CancelReason.NOT_ENOUGH_PLAYERS);
                return;
            }
            if (!DuelAPI.canDuel(invited)) {
                p.sendMessage((Object)ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.RED + invited.getName() + " is currently involved in a duel.");
                p.sendMessage((Object)ChatColor.RED + "The duel has been cancelled.");
                DuelAPI.cancelDuel(this.getDuel(), CancelReason.NOT_ENOUGH_PLAYERS);
                continue;
            }
            if (this.getDuel().getDuelType() == DuelType.TEAM_VS_TEAM) {
                TeamData data = CosmicDuel.get().getTeamDataManager().tryFindTeam(p.getUniqueId());
                TeamData data2 = CosmicDuel.get().getTeamDataManager().tryFindTeam(invited.getUniqueId());

                for (UUID uuid : data.getTeamates()) {
                    if (Bukkit.getPlayer(uuid) == null) {
                        p.sendMessage(CC.translate("You cannot duel a different team when a player on your team is offline!"));
                        return;
                    }
                }
                for (UUID uuid : data2.getTeamates()) {
                    if (Bukkit.getPlayer(uuid) == null) {
                        p.sendMessage(CC.translate("You cannot duel that team since they have an offline player!"));
                        return;
                    }
                }
            }
            DuelAPI.invitePlayer(this.getDuel(), p, invited);
        }
    }

    public String getArenaStatus(Arena arena) {
        if (DuelAPI.getCurrentlyActiveDuel(arena) == null) {
            return (Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "OPEN";
        }
        if (DuelAPI.getArenaQueueSize(arena) >= DuelAPI.MAX_ARENA_QUEUE) {
            return (Object)ChatColor.RED + ChatColor.BOLD.toString() + "FULL";
        }
        return ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + ChatColor.UNDERLINE.toString() + DuelAPI.getArenaQueueSize(arena) + (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + " Duel(s) in Queue";
    }

    public CustomInventoryItem getArenaInventoryItem(final Player p, final Arena arena) {
        boolean hasActive = DuelAPI.getCurrentlyActiveDuel(arena) != null;
        CustomItem ci = new CustomItem(hasActive ? Material.STAINED_GLASS_PANE : arena.getIcon(), 1, arena.getName() + " Arena " + (Object)ChatColor.GRAY + "(Click to Select)", this.getArenaStatus(arena));
        if (hasActive) {
            ci.setDurability((short)4);
        }
        if (this.getDuel().getArena() != null && this.getDuel().getArena().equals(arena)) {
            ci.addEnchantment(EnchantUtils.getCustomEnchant(), 1);
        }
        CustomInventoryItem item = new CustomInventoryItem(ci.buildItem());
        item.setCallback((inventory, customItem, event) -> {
            if (DuelAPI.getArenaQueueSize(arena) >= DuelAPI.MAX_ARENA_QUEUE) {
                p.sendMessage((Object)ChatColor.RED + "This arena currently has a full queue.");
                return;
            }
            event.setCancelled(true);
            p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
            Settings.this.getDuel().setArena(arena);
            event.getCurrentItem().addEnchantment(EnchantUtils.getCustomEnchant(), 1);

            Settings.this.invitePlayer(p);
            CosmicDuel.logMessage("Setting arena to " + arena.getName() + " for duel id " + Settings.this.getDuel().getDuelID());
        });
        return item;
    }

    public CustomInventoryItem getCustomInventoryItem(final Player p, final Kit k) {
        ArrayList lore = Lists.newArrayList();
        String itemName = (Object)ChatColor.YELLOW + ChatColor.BOLD.toString() + k.getName() + " Kit";

            lore.add((Object)ChatColor.GRAY + "Click to select this kit.");

        CustomInventoryItem item = new CustomInventoryItem(new CustomItem(ItemBuilder.buildItem(k.getMaterial(), k.getData(), itemName, lore)).addEnchantment((Enchantment)(this.getDuel().getKit().equals((Object)k) ? EnchantUtils.getCustomEnchant() : null), 1).buildItem());
        item.setCallback(new CustomInventoryItemCallback(){

            @Override
            public void inventoryClickEvent(CustomInventory inventory, CustomInventoryItem customItem, InventoryClickEvent event) {
                event.setCancelled(true);
                if (k.getPermission() != null && !p.hasPermission(k.getPermission())) {
                    p.sendMessage((Object)ChatColor.RED + "You do not have the required rank to use this kit.");
                    p.sendMessage((Object)ChatColor.GRAY + "Purchase the rank at " + (Object)ChatColor.RED + ChatColor.UNDERLINE.toString() + "buy.astropvp.net" + (Object)ChatColor.GRAY + ".");
                    return;
                }


                p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.0f);
                Settings.this.getDuel().setKit(k);
                Settings.this.updateSettingsInventory();
                Settings.this.getDuel().setCreateState(DuelCreateState.SETTINGS);
                p.setMetadata("close_exempt", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDuel.get(), (Object)""));
                Settings.this.settings.openInventory(p);
            }
        });
        return item;
    }

    public void toggleSetting(Setting s) {

        if (this.getDuel().getKit() != Kit.BYOB && s == Setting.DROPITEMS) {
            return;
        }

        if (this.currentSettings.contains((Object)s)) {
            this.currentSettings.remove((Object)s);
        } else {
            this.currentSettings.add(s);
        }
        this.updateSettingsInventory();
        this.settings.updateInventoryItems();
    }

    public boolean hasSetting(Setting s) {
        return this.currentSettings.contains((Object)s);
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        for (Setting setting : Setting.values()) {
            builder.append(setting.getName() + ": " + (this.hasSetting(setting) ? "ON" : "OFF") + " ");
        }
        return builder.toString();
    }

    public NewDuel getDuel() {
        return this.duel;
    }

    public CustomInventory getSettings() {
        return this.settings;
    }

    public List<Setting> getCurrentSettings() {
        return this.currentSettings;
    }

    public short getConfirmData() {
        return this.confirmData;
    }
}

