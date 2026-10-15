/*
 * Decompiled with CFR 0.150.
 */
package org.arkhamnetwork.Arkkit.libs;

import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;

import java.util.List;

public abstract class ArkPluginCommand<T extends ArkPlugin>
        extends AbstractCommand {
    protected T patch;

    public ArkPluginCommand(T patch, String command) {
        super(command);
        this.patch = patch;
    }

    public ArkPluginCommand(T patch, String command, String usage, String description) {
        super(command, usage, description);
        this.patch = patch;
    }

    public ArkPluginCommand(T patch, String command, String usage, String description, List<String> aliases) {
        super(command, usage, description, aliases);
        this.patch = patch;
    }
}

