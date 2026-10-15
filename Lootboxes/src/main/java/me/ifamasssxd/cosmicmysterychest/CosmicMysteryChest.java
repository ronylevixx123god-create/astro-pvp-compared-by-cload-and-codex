/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  org.bukkit.Bukkit
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.event.Listener
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 */
package me.ifamasssxd.cosmicmysterychest;

import lombok.Getter;
import me.ifamasssxd.cosmicmysterychest.commands.CommandLootboxFilter;
import me.ifamasssxd.cosmicmysterychest.commands.CommandLootboxToggle;
import me.ifamasssxd.cosmicmysterychest.commands.CommandMysteryChest;
import me.ifamasssxd.cosmicmysterychest.data.LootboxFilterData;
import me.ifamasssxd.cosmicmysterychest.data.LootboxFilterManager;
import me.ifamasssxd.cosmicmysterychest.halflootbox.CommandHalfLootbox;
import me.ifamasssxd.cosmicmysterychest.halflootbox.HalfLootboxApplyListener;
import me.ifamasssxd.cosmicmysterychest.items.MysteryLootboxItem;
import me.ifamasssxd.cosmicmysterychest.listeners.MysteryChestListener;
import me.ifamasssxd.cosmicmysterychest.managers.MysteryChestManager;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.UUID;

@Getter
public class CosmicMysteryChest
        extends JavaPlugin {
    private static CosmicMysteryChest instance;
    private ShardAnimations toggle;
    private MysteryChestManager chestManager;
    private MysteryLootboxItem mysteryLootboxItem;
    private LootboxFilterManager filterManager;

    public ShardAnimations getShardAnimations() {
        return this.toggle;
    }

    public void onEnable() {
        instance = this;
        instance.saveDefaultConfig();
        this.saveDefaultConfig();
        this.toggle = new ShardAnimations("toggles.yml");
        this.chestManager = new MysteryChestManager();
        this.chestManager.onEnable(this);
        this.getCommand("mysterychest").setExecutor(new CommandMysteryChest(this));
        this.getCommand("lbtoggle").setExecutor(new CommandLootboxToggle());
        this.getCommand("givehalflootbox").setExecutor(new CommandHalfLootbox());
        this.getCommand("lbfilter").setExecutor(new CommandLootboxFilter());
        this.getServer().getPluginManager().registerEvents(new MysteryChestListener(), this);
        this.getServer().getPluginManager().registerEvents(new HalfLootboxApplyListener(), this);
        this.filterManager = new LootboxFilterManager();
        if (Bukkit.getPluginManager().isPluginEnabled("CosmicSkyCustomItems")) {
            this.mysteryLootboxItem = new MysteryLootboxItem();
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(this.mysteryLootboxItem);
        }

        filterManager.loadAll();

        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            System.out.println("[Lootboxes] Started saving lootboxes");
            filterManager.purge();
            filterManager.saveAll();
            System.out.println("[Lootboxes] Finished saving lootboxes");
        },60L, 15*20*60);

    }

    public void onDisable() {
        this.toggle.saveToggles();
        System.out.println("Started Saving Lootboxes Database");
        filterManager.purge();
        filterManager.saveAll();
        if (this.chestManager != null) {
            this.chestManager.onDisable(this);
        }

    }

    public static CosmicMysteryChest get() {
        return instance;
    }

    public MysteryChestManager getChestManager() {
        return this.chestManager;
    }

    public MysteryLootboxItem getMysteryLootboxItem() {
        return this.mysteryLootboxItem;
    }

    public static CosmicMysteryChest getInstance() {
        return instance;
    }

}

