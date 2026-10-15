package net.astropvp.customblocks;

import com.comphenix.protocol.ProtocolLibrary;
import lombok.Getter;
import me.mattstudios.mf.base.CommandManager;
import me.mattstudios.mf.base.components.TypeResult;
import net.astropvp.customblocks.commands.AddCBlockCommand;
import net.astropvp.customblocks.commands.CustomBlockCommand;
import net.astropvp.customblocks.commands.RemoveCustomBlockCommand;
import net.astropvp.customblocks.data.CustomBlockManager;
import net.astropvp.customblocks.listeners.CustomBlockBreakListener;
import net.astropvp.customblocks.listeners.CustomBlockListener;
import net.astropvp.customblocks.listeners.CustomBlockPacketListener;
import net.astropvp.customblocks.listeners.CustomBlockPlaceListener;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.typeadapters.CustomBlockDeserializer;
import net.astropvp.customblocks.tasks.ChunkCustomBlocksTask;
import net.astropvp.customblocks.tasks.TickAbleBlocksRunnable;
import net.astropvp.customblocks.utils.config.ConfigManager;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.gson.Gson;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.stream.Collectors;

@Getter
public class CustomBlocks extends JavaPlugin {
    @Getter
    public static CustomBlocks instance;
    private CustomBlockManager customBlockManager;
    private CommandManager commandManager;
    private ConfigManager configManager;
    private Gson gson;

    public void onEnable() {
        instance = this;
        this.gson = StoragePlugin.getInstance()
                .getGson()
                .newBuilder()
                .registerTypeAdapter(CustomBlock.class, new CustomBlockDeserializer()).setLenient()
                .create();
        this.commandManager = new CommandManager(this, true);
        this.registerCommandParameters();
        this.registerCommandCompletion();
        commandManager.register(new CustomBlockCommand());
        commandManager.register(new AddCBlockCommand());
        commandManager.register(new RemoveCustomBlockCommand());
        this.customBlockManager = new CustomBlockManager(this);
        customBlockManager.loadAll();
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, ()-> {
            customBlockManager.saveAll();
        }, 40L, 15*60*20L);

        this.configManager = new ConfigManager(this);
        configManager.setupFiles();
        registerListeners();
        registerTasks();
        ProtocolLibrary.getProtocolManager().addPacketListener(new CustomBlockPacketListener());

    }

    private void registerCommandParameters() {
        this.commandManager.getParameterHandler().register(CustomBlockType.class, argument -> {
            CustomBlockType ecoUpdate = CustomBlockType.valueOf(String.valueOf(argument));
            return new TypeResult(ecoUpdate, argument);
        });
    }

    private void registerCommandCompletion() {
        this.commandManager.getCompletionHandler().register("#cblocks", input -> {
            return Arrays.stream(CustomBlockType.values()).map(CustomBlockType::name).collect(Collectors.toList());
        });
    }

    public void onDisable() {
        customBlockManager.saveAll();
        instance = null;
    }

    public void registerListeners() {
        getServer().getPluginManager().registerEvents(new CustomBlockListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomBlockBreakListener(this), this);
        getServer().getPluginManager().registerEvents(new CustomBlockPlaceListener(this), this);
    }

    public void registerTasks() {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new ChunkCustomBlocksTask(), 40L, 20L * 10);
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(this, new TickAbleBlocksRunnable(), 40L, 20L);
    }
}
