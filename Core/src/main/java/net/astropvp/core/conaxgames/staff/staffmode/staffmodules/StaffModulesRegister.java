package net.astropvp.core.conaxgames.staff.staffmode.staffmodules;

/*
import lombok.Getter;
import net.astropvp.core.AstroCore;
import net.astropvp.core.conaxgames.staff.staffmode.staffmodules.modules.*;

import java.util.HashSet;
import java.util.Set;

public class StaffModulesRegister {

    private final AstroCore plugin;
    @Getter
    private final Set<StaffModule> modules;

    public StaffModulesRegister(AstroCore plugin) {
        this.plugin = plugin;
        this.modules = new HashSet<>();
    }

    public void initialize() {
        this.registerAll(new FreezeModule(), new InventoryExaminerModule(), new RandomTpModule(), new VanishModule(), new CompassModule());
    }

    private void registerAll(StaffModule... modules) {
        for (StaffModule module : modules) {
            register(module);
        }
    }

    private void register(StaffModule module) {
        try {
            this.modules.add(module);

            plugin.getServer().getPluginManager().registerEvents(module, plugin);

        } catch (Exception exception) {
            System.err.println("Failed to register staff module: " + module.getItemName());
        }
    }
}


 */