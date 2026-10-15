package net.astropvp.loottable;

import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;

import net.astropvp.loottable.commands.LootTableCommand;
import net.astropvp.loottable.data.LootTableManager;
import net.astropvp.loottable.struct.ECO_UPDATE;
import net.astropvp.loottable.struct.LootTable;
import net.astropvp.loottable.typeadapters.LootMapSerializer;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class LootTablePlugin extends JavaPlugin {
    @Getter
    private static LootTablePlugin instance;
    private LootTableManager lootTableManager;
    private CommandManager commandManager;
    private Gson gson;
    private final Type mapType = new TypeToken<Map<ItemStack, Double>>(){}.getType();
    @Override
    public void onEnable() {
        instance = this;
        this.gson = StoragePlugin.getInstance().getGson().newBuilder().registerTypeAdapter(mapType, new LootMapSerializer()).create();
        this.lootTableManager = new LootTableManager(this);
        lootTableManager.loadAll();
        this.commandManager = new CommandManager(this, true);
        registerCommandParameters();
        registerCommandCompletion();
        commandManager.register(new LootTableCommand());
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            System.out.println("[LootTable] Started auto saving");
            lootTableManager.saveAll();
            System.out.println("[LootTable] Finished auto saving");

        }, 60L, 10*20*60);
    }

    @Override
    public void onDisable() {
        lootTableManager.saveAll();
        instance = null;
    }

    private void registerCommandParameters() {
        this.commandManager.getParameterHandler().register(LootTable.class, argument -> {
            LootTable type = LootTable.valueOf(String.valueOf(argument));
            return new TypeResult(type, argument);
        });
        this.commandManager.getParameterHandler().register(ECO_UPDATE.class, argument -> {
            ECO_UPDATE ecoUpdate = ECO_UPDATE.valueOf(String.valueOf(argument));
            return new TypeResult(ecoUpdate, argument);
        });
    }

    private void registerCommandCompletion() {
        this.commandManager.getCompletionHandler().register("#loottables", input -> {
            return Arrays.stream(LootTable.values()).map(LootTable::name).collect(Collectors.toList());
        });
        this.commandManager.getCompletionHandler().register("#ecoupdates", input -> {
            return Arrays.stream(ECO_UPDATE.values()).map(ECO_UPDATE::name).collect(Collectors.toList());
        });
    }
}
