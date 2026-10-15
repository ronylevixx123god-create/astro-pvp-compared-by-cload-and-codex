package org.arkhamnetwork.Arkkit.patches;

import java.util.Arrays;
import org.arkhamnetwork.Arkkit.patches.CommandFill;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.bukkit.plugin.Plugin;

public class FillCommand
        extends ArkPlugin {
    public FillCommand(Plugin p) {
        super(p);
    }

    @Override
    public void enable() {
        this.registerCommand(new CommandFill("fill", "/<command> [args]", "Automatically fill nearby dispensers with items from inventory.", Arrays.asList("tntfill")));
    }

    @Override
    public void disable() {
    }
}
