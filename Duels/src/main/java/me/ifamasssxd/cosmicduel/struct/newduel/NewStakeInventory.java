/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.factionupgrades.net.astropvp.armorsets.utils.ItemBuilder
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.inventory.Inventory
 *  org.bukkit.inventory.InventoryHolder
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package me.ifamasssxd.cosmicduel.struct.newduel;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.Kit;
import me.ifamasssxd.cosmicduel.struct.TeamData;
import me.ifamasssxd.cosmicduel.struct.enums.CancelReason;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.enums.Setting;
import me.ifamasssxd.cosmicduel.utils.ItemBuilder;
import me.ifamasssxd.cosmicduel.utils.PlayerUtils;
import net.astropvp.CollectPlugin;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class NewStakeInventory {
    public static String INVENTORY_NAME = "    Host                   Rival";
    Inventory teamALeaderInventory;
    Inventory teamBLeaderInventory;
    Inventory playerOneInventory;
    Inventory playerTwoInventory;
    Player p1;
    Player p2;
    TeamData teamA;
    TeamData teamB;
    NewDuel duel;
    ItemStack pendingItem;
    ItemStack acceptedItem;
    boolean modified = false;
    HashMap<String, PlayerType> playerTypes = new HashMap();
    HashMap<String, PlayerType> teamLeaderTypes;
    HashMap<TeamData, PlayerType> teamTypes;
    LinkedHashMap<Integer, Integer> validSlots;
    BukkitTask runnable;
    private ItemStack selected;

    public void setSelected(ItemStack selected) {
        this.selected = selected;
    }

    public ItemStack getSelected() {
        return this.selected;
    }

    public Inventory getTeamInventory(Player player) {
        if (this.p1 == player) {
            return this.teamALeaderInventory;
        } else {
            return this.p2 == player ? this.teamBLeaderInventory : null;
        }
    }

    public NewStakeInventory(NewDuel duel, Player p1, Player p2) {
        this.validSlots = new LinkedHashMap<Integer, Integer>() {
            private static final long serialVersionUID = 1L;

            {
                this.put(1, 5);
                this.put(2, 6);
                this.put(3, 7);
                this.put(9, 14);
                this.put(10, 15);
                this.put(11, 16);
                this.put(12, 17);
                this.put(18, 23);
                this.put(19, 24);
                this.put(20, 25);
                this.put(21, 26);
            }
        };
        this.runnable = null;
        this.duel = duel;
        this.p1 = p1;
        this.p2 = p2;
        this.playerTypes.put(p1.getName(), PlayerType.ONE);
        this.playerTypes.put(p2.getName(), PlayerType.TWO);
        this.playerOneInventory = Bukkit.createInventory(null, 81, getTradeInventoryTitle(p1.getName(), p2.getName()));
        this.playerTwoInventory = Bukkit.createInventory(null, 81, getTradeInventoryTitle(p1.getName(), p2.getName()));
        this.setupInventory(this.playerOneInventory, this.p2);
        this.setupInventory(this.playerTwoInventory, this.p1);
    }

    public NewStakeInventory(TeamDuel duel, TeamData teamA, TeamData teamB, Player teamALeader, Player teamBLeader) {
        this.teamTypes = new HashMap();
        this.teamLeaderTypes = new HashMap();
        this.validSlots = new LinkedHashMap<Integer, Integer>() {
            private static final long serialVersionUID = 1L;

            {
                this.put(1, 5);
                this.put(2, 6);
                this.put(3, 7);
                this.put(9, 14);
                this.put(10, 15);
                this.put(11, 16);
                this.put(12, 17);
                this.put(18, 23);
                this.put(19, 24);
                this.put(20, 25);
                this.put(21, 26);
            }
        };
        this.runnable = null;
        this.duel = duel;
        this.p1 = teamALeader;
        this.p2 = teamBLeader;
        this.playerTypes.put(this.p1.getName(), NewStakeInventory.PlayerType.ONE);
        this.playerTypes.put(this.p2.getName(), NewStakeInventory.PlayerType.TWO);
        this.teamA = teamA;
        this.teamB = teamB;
        this.teamTypes.put(teamA, NewStakeInventory.PlayerType.TEAM_A);
        this.teamTypes.put(teamB, NewStakeInventory.PlayerType.TEAM_B);
        this.teamLeaderTypes.put(Bukkit.getPlayer(teamA.getLeaderUUID()).getName(), NewStakeInventory.PlayerType.TEAM_A);
        this.teamLeaderTypes.put(Bukkit.getPlayer(teamB.getLeaderUUID()).getName(), NewStakeInventory.PlayerType.TEAM_B);
        this.teamALeaderInventory = Bukkit.createInventory(null, 90, getTradeInventoryTitle(Bukkit.getPlayer(teamA.getLeaderUUID()).getName() + "'s Team", Bukkit.getPlayer(teamB.getLeaderUUID()).getName()) + "'s Team");
        this.teamBLeaderInventory = Bukkit.createInventory(null, 90, getTradeInventoryTitle(Bukkit.getPlayer(teamA.getLeaderUUID()).getName() + "'s Team", Bukkit.getPlayer(teamB.getLeaderUUID()).getName()) + "'s Team");
        this.setupTeamInventory(this.teamALeaderInventory, Bukkit.getPlayer(teamB.getLeaderUUID()), teamB);
        this.setupTeamInventory(this.teamBLeaderInventory, Bukkit.getPlayer(teamA.getLeaderUUID()), teamA);
    }

    private void setupInventory(Inventory inventory, Player oppositePlayer) {
        List<String> lore = Lists.newArrayList("", ChatColor.YELLOW + ChatColor.BOLD.toString() + "Settings");
        Setting[] var4 = Setting.values();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            Setting setting = var4[var6];
            if (this.duel.getSettings().hasSetting(setting)) {
                lore.add(ChatColor.YELLOW + setting.getName() + ": " + ChatColor.GREEN + ChatColor.BOLD.toString() + "ON");
            } else {
                lore.add(ChatColor.YELLOW + setting.getName() + ": " + ChatColor.RED + ChatColor.BOLD.toString() + "OFF");
            }
        }

        lore.add(CC.GreenB + "Arena: " + CC.Green + (this.duel.getArena() == null ? "N/A" : this.duel.getArena().getName()));
        this.pendingItem = ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)14, ChatColor.YELLOW + "Click to ACCEPT Stake", lore);
        this.acceptedItem = ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)5, ChatColor.YELLOW + "Stake ACCEPTED", lore);
        this.prepareStake(inventory, oppositePlayer);
    }

    private void setupTeamInventory(Inventory inventory, Player oppositePlayer, TeamData team) {
        List<String> lore = Lists.newArrayList("", ChatColor.YELLOW + ChatColor.BOLD.toString() + "Settings");
        Setting[] var5 = Setting.values();
        int var6 = var5.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            Setting setting = var5[var7];
            if (this.duel.getSettings().hasSetting(setting)) {
                lore.add(ChatColor.YELLOW + setting.getName() + ": " + ChatColor.GREEN + ChatColor.BOLD.toString() + "ON");
            } else {
                lore.add(ChatColor.YELLOW + setting.getName() + ": " + ChatColor.RED + ChatColor.BOLD.toString() + "OFF");
            }
        }



        lore.add(CC.GreenB + "Arena: " + CC.Green + (this.duel.getArena() == null ? "N/A" : this.duel.getArena().getName()));
        this.pendingItem = ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)14, ChatColor.YELLOW + "Click to ACCEPT Stake", lore);
        this.acceptedItem = ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)5, ChatColor.YELLOW + "Stake ACCEPTED", lore);
        this.prepareTeamStake(inventory, oppositePlayer, team);
    }

    public void prepareStake(Inventory inv, Player oppositePlayer) {
        inv.setItem(0, this.pendingItem);
        inv.setItem(8, this.pendingItem);

        int i;
        for(i = 4; i < inv.getSize() - 45; i += 9) {
            inv.setItem(i, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        }

        for(i = 0; i < 9; ++i) {
            inv.setItem(27 + i, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        }

        inv.setItem(72, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(73, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(76, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(79, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(80, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        ItemStack[] armor = oppositePlayer.getEquipment().getArmorContents();
        ItemStack[] items = oppositePlayer.getInventory().getContents();

        for(int ii = 0; ii < items.length; ++ii) {
            ItemStack item = items[ii];
            if (item != null && item.getType() != Material.AIR) {
                item = item.clone();
            }

            inv.setItem(36 + ii, item);
        }

        inv.setItem(inv.getSize() - 7, armor[3]);
        inv.setItem(inv.getSize() - 6, armor[2]);
        inv.setItem(inv.getSize() - 4, armor[1]);
        inv.setItem(inv.getSize() - 3, armor[0]);
    }

    public void prepareTeamStake(Inventory inv, Player oppositePlayer, TeamData team) {
        List<Player> players = Lists.newArrayList();
        players.add(Bukkit.getPlayer(team.getLeaderUUID()));
        team.getTeamates().forEach((teammate) -> {
            players.add(Bukkit.getPlayer(teammate));
        });
        inv.setItem(0, this.pendingItem);
        inv.setItem(8, this.pendingItem);

        int index;
        for(index = 4; index < inv.getSize() - 45; index += 9) {
            inv.setItem(index, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        }

        for(index = 0; index < 9; ++index) {
            inv.setItem(27 + index, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        }

        inv.setItem(72, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(73, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(76, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(79, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        inv.setItem(80, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));

        for(index = 81; index < 90; ++index) {
            inv.setItem(index, ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", (" ")));
        }

        this.setEnemyInventory(inv, oppositePlayer);
        index = 10;

        for (Player player : players) {
            --index;
            ItemStack item = this.getSkullItem(player);
            if (ChatColor.stripColor(item.getItemMeta().getDisplayName()).equalsIgnoreCase(oppositePlayer.getName())) {
                inv.setItem(inv.getSize() - index, this.getSelectedIcon(item));
            } else {
                inv.setItem(inv.getSize() - index, item);
            }
        }

    }

    public ItemStack getSelectedIcon(ItemStack icon) {
        Player player = Bukkit.getPlayer(ChatColor.stripColor(icon.getItemMeta().getDisplayName()));
        ItemStack item = ItemBuilder.createSkull(player.getName(), CC.translate("&c&l" + player.getName()));
        SkullMeta meta = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
        meta.setDisplayName(CC.translate("&c&l" + player.getName()));
        List<String> lore = Lists.newArrayList();
        lore.add(CC.translate("&7Only a leader has permission to click on this icon"));
        lore.add(CC.translate("&7and let their team view the enemy's inventory contents."));
        lore.add(CC.translate("&7"));
        lore.add(CC.translate("&a&lSELECTED"));
        meta.setOwner(player.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack getSkullItem(Player player) {
        if (player == null) {
            Material type = Material.matchMaterial("SKULL_ITEM");
            ItemStack item = new ItemStack(type, 1);
            SkullMeta meta = (SkullMeta)Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
            meta.setDisplayName(CC.translate("&c&l" + player.getName()));
            List<String> lore = Lists.newArrayList();
            lore.add(CC.translate("&7Only a leader has permission to click on this icon"));
            lore.add(CC.translate("&7and let their team view the enemy's inventory contents."));
            meta.setOwner(player.getName());
            meta.setLore(lore);
            item.setItemMeta((ItemMeta)meta);
            return item;
        } else {
            ItemStack item = ItemBuilder.createSkull(player.getName(), CC.translate("&c&l" + player.getName()));
            SkullMeta meta = (SkullMeta) Bukkit.getItemFactory().getItemMeta(Material.SKULL_ITEM);
            meta.setDisplayName(CC.translate("&c&l" + player.getName()));
            List<String> lore = Lists.newArrayList();
            lore.add(CC.translate("&7Only a leader has permission to click on this icon"));
            lore.add(CC.translate("&7and let their team view the enemy's inventory contents."));
            meta.setOwner(player.getName());
            meta.setLore(lore);
            item.setItemMeta(meta);
            return item;
        }
    }

    public void setEnemyInventory(Inventory inv, Player player) {
        ItemStack[] armor = player.getEquipment().getArmorContents();
        ItemStack[] items = player.getInventory().getContents();

        for(int i = 0; i < items.length; ++i) {
            ItemStack item = items[i];
            if (item != null && item.getType() != Material.AIR) {
                item = item.clone();
            }

            inv.setItem(36 + i, item);
        }

        inv.setItem(inv.getSize() - 9 - 7, armor[3]);
        inv.setItem(inv.getSize() - 9 - 6, armor[2]);
        inv.setItem(inv.getSize() - 9 - 4, armor[1]);
        inv.setItem(inv.getSize() - 9 - 3, armor[0]);
    }

    private static String getTradeInventoryTitle(String lPName, String rPName) {
        String title;
        for(title = lPName; title.length() + rPName.length() < 32; title = title + " ") {
        }

        title = title + rPName;
        return title.length() <= 32 ? title : title.substring(0, 32);
    }

    public void onInventoryEvent(InventoryClickEvent e) {
        e.setCancelled(true);
        e.setResult(Result.DENY);
        if (this.getDuel().getCreateState() == DuelCreateState.STAKES) {
            Player p = (Player)e.getWhoClicked();
            PlayerType pt = this.playerTypes.get(p.getName());
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                Inventory inventory = p.getUniqueId().equals(this.p1.getUniqueId()) ? this.playerOneInventory : this.playerTwoInventory;
                Inventory inventory2 = p.getUniqueId().equals(this.p1.getUniqueId()) ? this.playerTwoInventory : this.playerOneInventory;
                ItemStack is;
                if (e.getRawSlot() < e.getInventory().getSize()) {
                    if (e.getRawSlot() == 0 && pt == NewStakeInventory.PlayerType.ONE) {
                        this.modified = true;
                        if (inventory.getItem(0).equals(this.pendingItem)) {
                            inventory.setItem(0, this.acceptedItem);
                            inventory2.setItem(0, this.acceptedItem);
                            this.checkDone();
                        } else {
                            inventory.setItem(0, this.pendingItem);
                            inventory2.setItem(0, this.pendingItem);
                        }
                    } else if (e.getRawSlot() == 8 && pt == NewStakeInventory.PlayerType.TWO) {
                        this.modified = true;
                        if (inventory.getItem(8).equals(this.pendingItem)) {
                            inventory.setItem(8, this.acceptedItem);
                            inventory2.setItem(8, this.acceptedItem);
                            this.checkDone();
                        } else {
                            inventory.setItem(8, this.pendingItem);
                            inventory2.setItem(8, this.pendingItem);
                        }
                    }

                    if (!this.validSlots(pt).contains(e.getRawSlot())) {
                        return;
                    }

                    if (p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(ChatColor.RED + "Your inventory is full.");
                        return;
                    }

                    this.modified = true;
                    is = e.getCurrentItem();
                    this.playerOneInventory.setItem(e.getRawSlot(), (ItemStack)null);
                    this.playerTwoInventory.setItem(e.getRawSlot(), (ItemStack)null);
                    p.getInventory().addItem(new ItemStack[]{is});
                    System.out.println("B");
                    p.updateInventory();
                    this.p2.updateInventory();
                    if (this.isSomeoneReady()) {
                        System.out.println("C");
                        this.resetAcceptButtons();
                    }
                } else {
                    if (e.getRawSlot() < e.getInventory().getSize()) {
                        return;
                    }

                    if (this.isFull(pt, inventory)) {
                        p.sendMessage(ChatColor.RED + "The Stake inventory is full.");
                        p.sendMessage(ChatColor.RED + "Please remove some stake items to make room.");
                        return;
                    }

                    this.modified = true;
                    is = e.getCurrentItem();
                    p.getInventory().removeItem(new ItemStack[]{is});
                    p.updateInventory();
                    int firstEmpty = this.getFirstEmpty(pt, inventory);
                    if (firstEmpty == -1) {
                        return;
                    }

                    inventory.setItem(firstEmpty, is);
                    inventory2.setItem(firstEmpty, is);
                    if (is != null) {
                        CosmicDuel.logMessage("Adding Stake item for " + p.getName() + " -> " + is.toString());
                    }

                    if (this.isSomeoneReady()) {
                        this.resetAcceptButtons();
                    }
                }

            }
        }
    }


    public void onTeamInventoryEvent(InventoryClickEvent e) {

        e.setCancelled(true);
        e.setResult(Result.DENY);
        if (this.getDuel().getCreateState() == DuelCreateState.STAKES) {
            Player p = (Player)e.getWhoClicked();
            PlayerType pt = this.playerTypes.get(p.getName());
            if (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) {
                Inventory inventory = p.getUniqueId().equals(this.p1.getUniqueId()) ? this.teamALeaderInventory : this.teamBLeaderInventory;
                Inventory inventory2 = p.getUniqueId().equals(this.p1.getUniqueId()) ? this.teamBLeaderInventory : this.teamALeaderInventory;
                ItemStack is;
                if (e.getRawSlot() < e.getInventory().getSize()) {
                    if (e.getRawSlot() == 0 && pt == NewStakeInventory.PlayerType.ONE) {
                        this.modified = true;
                        if (inventory.getItem(0).equals(this.pendingItem)) {
                            inventory.setItem(0, this.acceptedItem);
                            inventory2.setItem(0, this.acceptedItem);
                            this.checkTeamDone();
                        } else {
                            inventory.setItem(0, this.pendingItem);
                            inventory2.setItem(0, this.pendingItem);
                        }
                    } else if (e.getRawSlot() == 8 && pt == NewStakeInventory.PlayerType.TWO) {
                        this.modified = true;
                        if (inventory.getItem(8).equals(this.pendingItem)) {
                            inventory.setItem(8, this.acceptedItem);
                            inventory2.setItem(8, this.acceptedItem);
                            this.checkTeamDone();
                        } else {
                            inventory.setItem(8, this.pendingItem);
                            inventory2.setItem(8, this.pendingItem);
                        }
                    }

                    if (!this.validSlots(pt).contains(e.getRawSlot())) {
                        return;
                    }

                    if (p.getInventory().firstEmpty() == -1) {
                        p.sendMessage(ChatColor.RED + "Your inventory is full.");
                        return;
                    }

                    this.modified = true;
                    is = e.getCurrentItem();
                    this.teamALeaderInventory.setItem(e.getRawSlot(), null);
                    this.teamBLeaderInventory.setItem(e.getRawSlot(), null);
                    p.getInventory().addItem(is);
                    System.out.println("B");
                    p.updateInventory();
                    this.p2.updateInventory();
                    this.updateInventory(this.teamA, this.teamB);
                    if (this.isSomeoneReady()) {
                        System.out.println("C");
                        this.resetAcceptButtons();
                    }
                } else {
                    if (e.getRawSlot() < e.getInventory().getSize()) {
                        return;
                    }

                    if (this.isFull(pt, inventory)) {
                        p.sendMessage(ChatColor.RED + "The Stake inventory is full.");
                        p.sendMessage(ChatColor.RED + "Please remove some stake items to make room.");
                        return;
                    }

                    this.modified = true;
                    is = e.getCurrentItem();
                    p.getInventory().removeItem(is);
                    p.updateInventory();
                    int firstEmpty = this.getFirstEmpty(pt, inventory);
                    if (firstEmpty == -1) {
                        return;
                    }

                    inventory.setItem(firstEmpty, is);
                    inventory2.setItem(firstEmpty, is);
                    if (is != null) {
                        CosmicDuel.logMessage("Adding Stake item for " + p.getName() + " -> " + is.toString());
                    }

                    if (this.isSomeoneReady()) {
                        this.resetAcceptButtons();
                    }
                }

            }
        }
    }

    private void updateInventory(TeamData teamA, TeamData teamB) {
        List<UUID> players = Lists.newArrayList();
        players.add(teamA.getLeaderUUID());
        teamA.getTeamates().stream().forEach((teamate) -> {
            players.add(teamate);
        });
        players.add(teamB.getLeaderUUID());
        teamB.getTeamates().stream().forEach((teamate) -> {
            players.add(teamate);
        });
        Iterator var4 = players.iterator();

        while(var4.hasNext()) {
            UUID uuid = (UUID)var4.next();
            Bukkit.getPlayer(uuid).updateInventory();
        }

    }

    public void resetAcceptButtons() {
        this.p1.playSound(this.p1.getLocation(), Sound.ORB_PICKUP, 0.4F, 0.4F);
        this.p2.playSound(this.p2.getLocation(), Sound.ORB_PICKUP, 0.4F, 0.4F);
        this.playerOneInventory.setItem(0, this.pendingItem);
        this.playerTwoInventory.setItem(0, this.pendingItem);
        this.playerOneInventory.setItem(8, this.pendingItem);
        this.playerTwoInventory.setItem(8, this.pendingItem);
    }

    public boolean isSomeoneReady() {
        try {
            return this.playerOneInventory.getItem(0).equals(this.acceptedItem) || this.playerOneInventory.getItem(8).equals(this.acceptedItem);
        } catch (Exception e) {
            for (HumanEntity p : playerOneInventory.getViewers()) {
                p.closeInventory();
                DuelAPI.cancelDuel(duel, CancelReason.CLOSED_INVENTORY);
            }
            for (HumanEntity p : playerTwoInventory.getViewers()) {
                p.closeInventory();
                DuelAPI.cancelDuel(duel, CancelReason.CLOSED_INVENTORY);

            }
        }
        return false;
    }

    public void checkDone() {
        if (this.runnable == null) {
            if (this.playerOneInventory.getItem(0).equals(this.acceptedItem) && this.playerOneInventory.getItem(8).equals(this.acceptedItem)) {
                this.modified = false;
                this.runnable = (new BukkitRunnable() {
                    final AtomicInteger time = new AtomicInteger(6);

                    public void run() {
                        if (NewStakeInventory.this.runnable.getTaskId() != this.getTaskId()) {
                            this.cancel();
                        } else {
                            NewDuel duel = NewStakeInventory.this.getDuel();
                            if (duel.isCancelled()) {
                                this.cancel();
                                NewStakeInventory.this.runnable = null;
                            } else if (NewStakeInventory.this.modified) {
                                NewStakeInventory.this.modified = false;
                                NewStakeInventory.this.runnable = null;
                                this.cancel();
                                NewStakeInventory.this.setMiddle(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", ("")));
                                NewStakeInventory.this.resetAcceptButtons();
                            } else {
                                Iterator var2;
                                Player p;
                                if (this.time.decrementAndGet() > 0) {
                                    var2 = duel.getPlayers().iterator();

                                    while(var2.hasNext()) {
                                        p = (Player)var2.next();
                                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.4F);
                                    }

                                    short data = 13;
                                    switch(this.time.get()) {
                                        case 1:
                                        case 2:
                                            data = 14;
                                            break;
                                        case 3:
                                        case 4:
                                            data = 1;
                                            break;
                                        case 5:
                                            data = 4;
                                    }

                                    ItemStack is = ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, data, ChatColor.YELLOW + ChatColor.BOLD.toString() + this.time.get(), (""));
                                    is.setAmount(this.time.get());
                                    NewStakeInventory.this.setMiddle(is);
                                } else {
                                    this.cancel();
                                    var2 = duel.getPlayers().iterator();

                                    while(var2.hasNext()) {
                                        p = (Player)var2.next();
                                        if (PlayerUtils.inCombat(p)) {
                                            duel.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + p.getName() + " was in combat and cancelled the duel.", false);
                                            DuelAPI.cancelDuel(duel, CancelReason.IN_COMBAT_STAKES);
                                            return;
                                        }
                                    }

                                    List<ItemStack> stakes = NewStakeInventory.this.getStakes(NewStakeInventory.PlayerType.ONE);
                                    List<ItemStack> stakesTwo = NewStakeInventory.this.getStakes(NewStakeInventory.PlayerType.TWO);
                                    duel.getStakes().put(NewStakeInventory.this.p1.getUniqueId(), stakes);
                                    duel.getStakes().put(NewStakeInventory.this.p2.getUniqueId(), stakesTwo);
                                    if (DuelAPI.getArenaQueueSize(duel.getArena()) > 0 || DuelAPI.getCurrentlyActiveDuel(duel.getArena()) != null) {
                                        duel.setCreateState(DuelCreateState.WAITING);
                                        duel.duelState = DuelState.IN_QUEUE;
                                        duel.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "This Arena currently has a queue of " + ChatColor.AQUA + ChatColor.BOLD.toString() + DuelAPI.getArenaQueueSize(duel.getArena()) + " Duel(s).", true);
                                        duel.sendMessage(ChatColor.GRAY + "You must wait for the current duels to be over or you can use '/duel leave' to retrieve your items back.", true);
                                        NewStakeInventory.this.p1.closeInventory();
                                        NewStakeInventory.this.p2.closeInventory();
                                        DuelAPI.queueDuel(duel.getArena(), duel);
                                        return;
                                    }

                                    duel.setCreateState(DuelCreateState.DONE);
                                    NewStakeInventory.this.p1.closeInventory();
                                    NewStakeInventory.this.p2.closeInventory();
                                    duel.onStart();
                                    CosmicDuel.logMessage("Starting duel with " + NewStakeInventory.this.p1.getName() + " -> " + stakes.toString() + " " + NewStakeInventory.this.p2.getName() + " stakes -> " + stakesTwo.toString() + "!");
                                }

                            }
                        }
                    }
                }).runTaskTimer(CosmicDuel.get(), 20L, 20L);
            }

        }
    }

    public void checkTeamDone() {
        if (this.runnable == null) {
            if (this.teamALeaderInventory.getItem(0).equals(this.acceptedItem) && this.teamALeaderInventory.getItem(8).equals(this.acceptedItem)) {
                this.modified = false;
                this.runnable = (new BukkitRunnable() {
                    final AtomicInteger time = new AtomicInteger(6);

                    public void run() {
                        if (NewStakeInventory.this.runnable.getTaskId() != this.getTaskId()) {
                            this.cancel();
                        } else {
                            NewDuel duel = NewStakeInventory.this.getDuel();
                            if (duel.isCancelled()) {
                                this.cancel();
                                NewStakeInventory.this.runnable = null;
                            } else if (NewStakeInventory.this.modified) {
                                NewStakeInventory.this.modified = false;
                                NewStakeInventory.this.runnable = null;
                                this.cancel();
                                NewStakeInventory.this.setTeamMiddle(ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, (short)7, " ", ("")));
                                NewStakeInventory.this.resetAcceptButtons();
                            } else {
                                Iterator var2;
                                Player p;
                                if (this.time.decrementAndGet() > 0) {
                                    var2 = duel.getPlayers().iterator();

                                    while(var2.hasNext()) {
                                        p = (Player)var2.next();
                                        p.playSound(p.getLocation(), Sound.ORB_PICKUP, 1.0F, 1.4F);
                                    }

                                    short data = 13;
                                    switch(this.time.get()) {
                                        case 1:
                                        case 2:
                                            data = 14;
                                            break;
                                        case 3:
                                        case 4:
                                            data = 1;
                                            break;
                                        case 5:
                                            data = 4;
                                    }

                                    ItemStack is = ItemBuilder.buildItem(Material.STAINED_GLASS_PANE, data, ChatColor.YELLOW + ChatColor.BOLD.toString() + this.time.get(), (""));
                                    is.setAmount(this.time.get());
                                    NewStakeInventory.this.setTeamMiddle(is);
                                } else {
                                    this.cancel();
                                    var2 = duel.getPlayers().iterator();

                                    while(var2.hasNext()) {
                                        p = (Player)var2.next();
                                        if (PlayerUtils.inCombat(p)) {
                                            duel.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "(!) " + ChatColor.RED + p.getName() + " was in combat and cancelled the duel.", false);
                                            DuelAPI.cancelDuel(duel, CancelReason.IN_COMBAT_STAKES);
                                            return;
                                        }
                                    }

                                    List<ItemStack> stakes = NewStakeInventory.this.getTeamStakes(NewStakeInventory.PlayerType.ONE);
                                    List<ItemStack> stakesTwo = NewStakeInventory.this.getTeamStakes(NewStakeInventory.PlayerType.TWO);
                                    duel.getStakes().put(NewStakeInventory.this.p1.getUniqueId(), stakes);
                                    duel.getStakes().put(NewStakeInventory.this.p2.getUniqueId(), stakesTwo);
                                    if (DuelAPI.getArenaQueueSize(duel.getArena()) > 0 || DuelAPI.getCurrentlyActiveDuel(duel.getArena()) != null) {
                                        duel.setCreateState(DuelCreateState.WAITING);
                                        duel.duelState = DuelState.IN_QUEUE;
                                        duel.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "This Arena currently has a queue of " + ChatColor.AQUA + ChatColor.BOLD.toString() + DuelAPI.getArenaQueueSize(duel.getArena()) + " Duel(s).", true);
                                        duel.sendMessage(ChatColor.GRAY + "You must wait for the current duels to be over or you can use '/duel leave' to retrieve your items back.", true);
                                        NewStakeInventory.this.p1.closeInventory();
                                        NewStakeInventory.this.p2.closeInventory();
                                        DuelAPI.queueDuel(duel.getArena(), duel);
                                        return;
                                    }

                                    duel.setCreateState(DuelCreateState.DONE);
                                    NewStakeInventory.this.p1.closeInventory();
                                    NewStakeInventory.this.p2.closeInventory();
                                    duel.onStart();
                                    CosmicDuel.logMessage("Starting duel with " + NewStakeInventory.this.p1.getName() + " -> " + stakes.toString() + " " + NewStakeInventory.this.p2.getName() + " stakes -> " + stakesTwo.toString() + "!");
                                }

                            }
                        }
                    }
                }).runTaskTimer(CosmicDuel.get(), 20L, 20L);
            }

        }
    }

    public void setMiddle(ItemStack is) {
        this.playerOneInventory.setItem(4, is);
        this.playerOneInventory.setItem(13, is);
        this.playerOneInventory.setItem(22, is);
        this.playerTwoInventory.setItem(4, is);
        this.playerTwoInventory.setItem(13, is);
        this.playerTwoInventory.setItem(22, is);
    }

    public void setTeamMiddle(ItemStack is) {
        this.teamALeaderInventory.setItem(4, is);
        this.teamALeaderInventory.setItem(13, is);
        this.teamALeaderInventory.setItem(22, is);
        this.teamBLeaderInventory.setItem(4, is);
        this.teamBLeaderInventory.setItem(13, is);
        this.teamBLeaderInventory.setItem(22, is);
    }

    public List<ItemStack> getStakes(NewStakeInventory.PlayerType pt) {
        List<ItemStack> items = new ArrayList();
        Inventory inventory = pt == NewStakeInventory.PlayerType.ONE ? this.playerOneInventory : this.playerTwoInventory;
        Iterator var4 = this.validSlots(pt).iterator();

        while(var4.hasNext()) {
            int i = (Integer)var4.next();
            if (inventory.getItem(i) != null) {
                items.add(inventory.getItem(i));
            }
        }

        return items;
    }

    public List<ItemStack> getTeamStakes(NewStakeInventory.PlayerType pt) {
        List<ItemStack> items = new ArrayList();
        Inventory inventory = pt == NewStakeInventory.PlayerType.ONE ? this.teamALeaderInventory : this.teamBLeaderInventory;
        Iterator var4 = this.validSlots(pt).iterator();

        while(var4.hasNext()) {
            int i = (Integer)var4.next();
            if (inventory.getItem(i) != null) {
                items.add(inventory.getItem(i));
            }
        }

        return items;
    }

    public void closeInventory(NewDuel duel) {
        Iterator var2;
        String p_name;
        NewStakeInventory.PlayerType pt;
        Player p;
        Iterator var6;
        ItemStack is;
        if (!(duel instanceof TeamDuel)) {
            var2 = this.playerTypes.keySet().iterator();

            while(true) {
                do {
                    if (!var2.hasNext()) {
                        this.playerOneInventory.clear();
                        this.playerTwoInventory.clear();
                        return;
                    }

                    p_name = (String)var2.next();
                    pt = this.playerTypes.get(p_name);
                    p = Bukkit.getPlayer(p_name);
                } while(p == null);

                for(var6 = this.getStakes(pt).iterator(); var6.hasNext(); p.updateInventory()) {
                    is = (ItemStack)var6.next();
                    PlayerUtil.giveOrDropProtectedItem(p, is, 60);
                }

                p.closeInventory();
            }
        } else {
            var2 = this.playerTypes.keySet().iterator();

            while(true) {
                do {
                    if (!var2.hasNext()) {
                        this.teamALeaderInventory.clear();
                        this.teamBLeaderInventory.clear();
                        return;
                    }

                    p_name = (String)var2.next();
                    pt = this.playerTypes.get(p_name);
                    p = Bukkit.getPlayer(p_name);
                } while(p == null);

                for(var6 = this.getTeamStakes(pt).iterator(); var6.hasNext(); p.updateInventory()) {
                    is = (ItemStack)var6.next();
                    PlayerUtil.giveOrDropProtectedItem(p, is, 60);
                }

                p.closeInventory();
            }
        }
    }

    public int getFirstEmpty(NewStakeInventory.PlayerType pt, Inventory inv) {
        Iterator var3 = this.validSlots(pt).iterator();

        int i;
        do {
            if (!var3.hasNext()) {
                return -1;
            }

            i = (Integer)var3.next();
        } while(inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR);

        return i;
    }

    public boolean isFull(NewStakeInventory.PlayerType pt, Inventory inv) {
        int amount = 0;
        Iterator var4 = this.validSlots(pt).iterator();

        while(var4.hasNext()) {
            int i = (Integer)var4.next();
            if (inv.getItem(i) != null && inv.getItem(i).getType() != Material.AIR) {
                ++amount;
            }
        }

        return amount >= this.validSlots(pt).size();
    }

    public boolean isEmpty(Inventory inv) {
        ListIterator var2 = inv.iterator();

        ItemStack is;
        do {
            if (!var2.hasNext()) {
                return true;
            }

            is = (ItemStack)var2.next();
        } while(is == null || is.getType() == Material.AIR);

        return false;
    }

    public List<Integer> validSlots(NewStakeInventory.PlayerType pt) {
        if (pt == NewStakeInventory.PlayerType.ONE) {
            return this.validSlots.keySet().stream().collect(Collectors.toList());
        } else {
            return pt == NewStakeInventory.PlayerType.TWO ? this.validSlots.values().stream().collect(Collectors.toList()) : null;
        }
    }

    public Inventory getPlayerOneInventory() {
        return this.playerOneInventory;
    }

    public Inventory getPlayerTwoInventory() {
        return this.playerTwoInventory;
    }

    public Inventory getTeamALeaderInventory() {
        return this.teamALeaderInventory;
    }

    public Inventory getTeamBLeaderInventory() {
        return this.teamBLeaderInventory;
    }

    public Player getP1() {
        return this.p1;
    }

    public Player getP2() {
        return this.p2;
    }

    public TeamData getTeamA() {
        return this.teamA;
    }

    public TeamData getTeamB() {
        return this.teamB;
    }

    public NewDuel getDuel() {
        return this.duel;
    }

    public enum PlayerType {
        ONE,
        TWO,
        TEAM_A,
        TEAM_B
    }
}
