package net.astropvp.adminitems;

import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;

import net.astropvp.adminitems.commands.AdminItemCommand;
import net.astropvp.adminitems.language.LanguageHandler;

import net.astropvp.adminitems.managers.AdminItemAbilityManager;
import net.astropvp.adminitems.struct.AdminItem;
import net.astropvp.adminitems.struct.AdminItemRegister;
import net.astropvp.adminitems.struct.config.AdminItemConfigManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.stream.Collectors;

@Getter
public class AdminItemsPlugin extends JavaPlugin {

    @Getter
    public static AdminItemsPlugin instance;
    public LanguageHandler languageHandler;
    private AdminItemRegister adminItemRegister;
    private CommandManager commandManager;
    private AdminItemConfigManager petConfigManager;
    private AdminItemAbilityManager abilityManager;

    @Override
    public void onEnable() {
        instance = this;

        this.abilityManager = new AdminItemAbilityManager();


        this.languageHandler = new LanguageHandler(this);
        this.languageHandler.load();

        this.adminItemRegister = new AdminItemRegister();
        this.adminItemRegister.initialize();

        this.petConfigManager = new AdminItemConfigManager(this);
        this.petConfigManager.setupFiles();

        this.commandManager = new CommandManager(this);
        this.registerCommandParameters();
        this.registerCommandCompletion();
        this.commandManager.register(new AdminItemCommand(this));


    }

    private void registerCommandParameters() {
        this.commandManager.getParameterHandler().register(AdminItem.class, argument -> {
            AdminItem pet = this.adminItemRegister.getAdminItemFromID(this.adminItemRegister.getAdminItemIDFromName(String.valueOf(argument)));
            return new TypeResult(pet, argument);
        });
    }

    private void registerCommandCompletion() {
        this.commandManager.getCompletionHandler().register("#adminItems", input -> {
            return this.adminItemRegister.getAdminItems().stream().map(AdminItem::getID).collect(Collectors.toList());
        });
    }

    @Override
    public void onDisable() {
        instance = null;
    }
}
