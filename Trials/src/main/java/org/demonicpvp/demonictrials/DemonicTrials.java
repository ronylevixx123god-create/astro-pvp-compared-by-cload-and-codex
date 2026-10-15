package org.demonicpvp.demonictrials;


import co.aikar.commands.PaperCommandManager;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import org.demonicpvp.demonictrials.command.AdminTrialsCommand;
import org.demonicpvp.demonictrials.command.TrialsCommand;
import org.demonicpvp.demonictrials.item.TrialPortalCustomItem;
import org.demonicpvp.demonictrials.item.TrialTrinketCustomItem;
import org.demonicpvp.demonictrials.listener.MobListener;
import org.demonicpvp.demonictrials.listener.TrialSanityListener;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.runnable.TrialScoreboardRunnable;
import org.demonicpvp.demonictrials.trial.TrialSession;

import java.io.File;
import java.util.Iterator;

public class DemonicTrials extends JavaPlugin {
    private static DemonicTrials instance;

    private TrialPortalCustomItem trialPortalItem;
    private TrialTrinketCustomItem trialTrinketItem;
    private PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        instance = this;
        File file = new File(this.getDataFolder() + File.separator + "config.yml");
        if (!file.exists()) {
            saveDefaultConfig();
        }
        this.trialPortalItem = new TrialPortalCustomItem();
        this.trialTrinketItem = new TrialTrinketCustomItem();

        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(this.trialPortalItem);
        CosmicSkyCustomItems.get().getItemManager().registerCustomItem(this.trialTrinketItem);

        this.getServer().getPluginManager().registerEvents(new TrialSanityListener(), this);
        this.getServer().getPluginManager().registerEvents(new MobListener(this), this);
        this.getServer().getPluginManager().registerEvents(this.trialPortalItem, this);
        this.commandManager = new PaperCommandManager(this);
        commandManager.registerCommand(new TrialsCommand());
        commandManager.registerCommand(new AdminTrialsCommand());

        new TrialScoreboardRunnable().runTaskTimer(this, 0L, 10L);
    }

    @Override
    public void onDisable() {


        TrialManager.getTrials().forEach(trialSession -> {
            Iterator<Player> iterator = trialSession.getPlayers().iterator();

            while (iterator.hasNext()) {
                Player p = iterator.next();
                iterator.remove();
                trialSession.removePlayer(p, TrialSession.TrialPlayerRemoveReason.PLUGIN_DISABLE);
            }
        });
    }

    public TrialTrinketCustomItem getTrialTrinketItem() {
        return this.trialTrinketItem;
    }

    public static DemonicTrials getInstance() {
        return instance;
    }
}
