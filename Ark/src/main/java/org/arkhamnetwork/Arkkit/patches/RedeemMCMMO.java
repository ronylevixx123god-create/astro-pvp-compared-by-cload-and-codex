package org.arkhamnetwork.Arkkit.patches;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import org.arkhamnetwork.Arkkit.patches.CommandAddCredits;
import org.arkhamnetwork.Arkkit.patches.CommandCredits;
import org.arkhamnetwork.Arkkit.patches.CommandRedeem;
import org.arkhamnetwork.Arkkit.patches.CommandTakeCredits;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

public class RedeemMCMMO
        extends ArkPlugin {
    private static FileConfiguration config;

    public RedeemMCMMO(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.loadConfig();
        this.registerCommand(new CommandAddCredits("addcredits", "/addcredits <player> <amount>", "Give a player credits towards MCMMO levels."));
        this.registerCommand(new CommandTakeCredits("takecredits", "/takecredits <player> <amount>", "Takes credits from a player."));
        this.registerCommand(new CommandCredits("credits", "/credits [player]", "Shows the amount of credits you have.", new ArrayList<String>(Arrays.asList("mcmmocredits", "mcmmocredit"))));
        this.registerCommand(new CommandRedeem("redeem", "/redeem <skill> <amount>", "Redeem mcmmo levels from your credits.", new ArrayList<String>(Arrays.asList("redeemcredits", "redeemmcmmo"))));
    }

    @Override
    public void disable() {
        RedeemMCMMO.saveConfig();
    }

    private void loadConfig() {
        File configFile = new File("plugins/Arkkit/redeemMCMMO.yml");
        if (!configFile.exists()) {
            try {
                configFile.createNewFile();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        config = YamlConfiguration.loadConfiguration((File)configFile);
    }

    protected static FileConfiguration getConfig() {
        return config;
    }

    protected static void saveConfig() {
        try {
            config.save(new File("plugins/Arkkit/redeemMCMMO.yml"));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
