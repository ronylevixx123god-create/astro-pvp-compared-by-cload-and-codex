package net.crankedpvp.shards;

import lombok.Getter;
import lombok.Setter;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;
import net.crankedpvp.shards.commands.MobcoinsAdminCommand;
import net.crankedpvp.shards.commands.MobcoinsCommand;
import net.crankedpvp.shards.managers.MobcoinManager;
import net.crankedpvp.shards.language.LanguageHandler;
import net.crankedpvp.shards.managers.MobcoinShopManager;
import net.crankedpvp.shards.utils.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
@Setter
public class MobcoinPlugin extends JavaPlugin {
    @Getter
    private static MobcoinPlugin instance;
    private MobcoinManager shardsManager;
    private LanguageHandler languageHandler;
    private CommandManager commandManager;
    private MobcoinShopManager shardsShopManager;
    private ConfigManager configManager;
    private MobcoinAPI shardsAPI;



    @Override
    public void onEnable() {
        instance = this;
        this.shardsManager = new MobcoinManager(this);
        shardsManager.onEnable();
        this.commandManager = new CommandManager(this);
        registerCommandParameters();
        commandManager.register(new MobcoinsCommand(this), new MobcoinsAdminCommand());
        this.languageHandler = new LanguageHandler(this);
        languageHandler.load();
        this.configManager = new ConfigManager(this);
        configManager.setupFiles();
        this.shardsShopManager = new MobcoinShopManager(this);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            System.out.println("[Shards] Started auto saving");
            shardsManager.purge();
            shardsManager.saveAll();
            System.out.println("[Shards] Finished auto saving");
        }, 60L, 5*20*60);
    }

    @Override
    public void onDisable() {
        shardsManager.purge();
        shardsManager.saveAll();
        shardsShopManager.save(this);
        instance = null;
    }

    private void registerCommandParameters() {
        this.commandManager.getParameterHandler().register(OfflinePlayer.class, argument -> {
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(argument.toString());
            if(offlinePlayer == null) return new TypeResult(argument);
            return new TypeResult(offlinePlayer, argument);
        });
    }
}
