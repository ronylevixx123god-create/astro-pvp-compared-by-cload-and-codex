package net.astropvp.weaponbonuses;

import java.util.Arrays;

import co.aikar.commands.PaperCommandManager;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import net.astropvp.weaponbonuses.command.WeaponBonusCommand;
import net.astropvp.weaponbonuses.item.WeaponBonusExtractorItem;
import net.astropvp.weaponbonuses.item.WeaponBonusItem;
import net.astropvp.weaponbonuses.listener.BonusListener;
import org.bukkit.plugin.java.JavaPlugin;

public class AstroWeaponBonuses extends JavaPlugin {
    private static AstroWeaponBonuses instance;

    public void onEnable() {
        instance = this;
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(WeaponBonusItem.getInstance());
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(WeaponBonusExtractorItem.getInstance());
        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new WeaponBonusCommand());
        paperCommandManager.getCommandCompletions().registerCompletion("bonusWeaponTypes", (context) -> {
            return Arrays.asList("champion", "ultra", "superior");
        });
        this.getServer().getPluginManager().registerEvents(new BonusListener(), this);
    }

    public static AstroWeaponBonuses getInstance() {
        return instance;
    }

    public void onDisable() {
    }
}
