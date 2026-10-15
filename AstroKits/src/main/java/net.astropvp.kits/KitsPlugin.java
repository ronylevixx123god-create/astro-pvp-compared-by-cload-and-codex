package net.astropvp.kits;

import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public class KitsPlugin extends JavaPlugin {

    @Getter
    private static KitsPlugin instance;
    public void onEnable() {
        instance = this;
    }


    public void onDisable() {
        instance = null;
    }
}
