package net.astropvp.armorsets;

import lombok.Getter;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import net.astropvp.armorsets.commands.*;
import net.astropvp.armorsets.items.AntiMKitCrystalItem;
import net.astropvp.armorsets.items.ArmorCrystalItem;
import net.astropvp.armorsets.items.CrystalExtractionItem;
import net.astropvp.armorsets.items.OmniCrystal;
import net.astropvp.armorsets.listeners.*;
import net.astropvp.armorsets.managers.HeadlessAbilityManager;
import net.astropvp.armorsets.utils.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class AstroArmorSets extends JavaPlugin {
    @Getter
    private static AstroArmorSets instance;
    @Getter
    private ConfigManager configManager;

    @Getter
    private HeadlessAbilityManager headlessAbilityManager;





    public void onEnable() {
        instance = this;
        registerCommands();
        registerCustomItems();

        this.configManager = new ConfigManager(this);
        this.configManager.setupFiles();
        registerListeners();

        System.out.println("[AstroArmorSets] Plugin has been enabled!");
        headlessAbilityManager  = new HeadlessAbilityManager();
    }


    public void onDisable() {
        System.out.println("[AstroArmorSets] Plugin has been disabled!");
        this.configManager = null;
        instance = null;
    }

    public void registerCommands() {
        getCommand("heroicarmor").setExecutor(new CommandGiveHeroicArmor());
        getCommand("givearmorset").setExecutor(new CommandGiveArmorSet());
        getCommand("heroicupgrade").setExecutor(new CommandGiveHeroicUpgrade());
        getCommand("debugarmorsets").setExecutor(new CommandDebugArmorSet());
        getCommand("armorsets").setExecutor(new CommandOpenGUI());
        getCommand("aarmorset").setExecutor(new CommandAdminArmorSet());
        getCommand("heroicfix").setExecutor(new CommandHeroicFix());



    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new ArmorSetApplyListener(this), this);
        getServer().getPluginManager().registerEvents(new HeroicUpgradeApplyListener(), this);
        getServer().getPluginManager().registerEvents(new HeroicArmorWeaponDamageListener(), this);
        getServer().getPluginManager().registerEvents(new AntiMkitDamagedListener(), this);
        getServer().getPluginManager().registerEvents(new OmniArmorDamageListener(), this);
        getServer().getPluginManager().registerEvents(new GUIInventoryListener(), this);
        getServer().getPluginManager().registerEvents(new ArmorSetDamageListener(), this);
        getServer().getPluginManager().registerEvents(new RangerSetListener(), this);





    }

    public void registerCustomItems() {
        if (Bukkit.getPluginManager().isPluginEnabled("CosmicSkyCustomItems")) {
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new CrystalExtractionItem());
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new ArmorCrystalItem());
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new AntiMKitCrystalItem());
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new OmniCrystal());


        }
    }
}
