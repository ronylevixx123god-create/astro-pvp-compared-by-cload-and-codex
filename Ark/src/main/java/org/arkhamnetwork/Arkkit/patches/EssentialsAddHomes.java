/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  net.milkbowl.vault.permission.Permission
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Material
 *  org.bukkit.configuration.file.FileConfiguration
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 */
package org.arkhamnetwork.Arkkit.patches;

import com.earth2me.essentials.Essentials;
import net.milkbowl.vault.permission.Permission;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.IOException;
import java.util.Arrays;

public class EssentialsAddHomes
        extends ArkPlugin {
    private static Permission permission = null;
    private static Essentials ess = null;

    public EssentialsAddHomes(Plugin p) {
        super(p);
    }

    protected static Permission getPermissions() {
        return permission;
    }

    protected static Essentials getEssentials() {
        return ess;
    }

    protected static String getEssentialsGroup(int home_limit) {
        return "addhome_" + home_limit;
    }

    public static ItemStack getAddHomeItem() {
        return ItemUtils.buildItem(Material.BED, (short) 0, ChatColor.YELLOW + "" + ChatColor.BOLD + "Max Home Increase " + ChatColor.GRAY + "(Right Click)", ChatColor.GRAY + "Adds +1 " + ChatColor.YELLOW + "/home" + ChatColor.GRAY + " slots to your player.");
    }

    protected static boolean isAddHomeItem(ItemStack i) {
        return i.isSimilar(EssentialsAddHomes.getAddHomeItem());
    }

    @Override
    public void enable() {
        if (!this.setupPermissions()) {
            Bukkit.getLogger().warning("Vault permissions not hooked, disabling patch.");
            this.kill();
            return;
        }
        ess = (Essentials) Bukkit.getServer().getPluginManager().getPlugin("Essentials");
        this.populateExtraHomeGroups();
        this.registerListener(new EssentialsAddHomeItemListener());
        this.registerCommand(new CommandAddHomes("addhomes", "/<command>", "Adds extra homes to player."));
        this.registerCommand(new CommandEssentialsAddHomeItem("addhomeitem", "/<command>", "Adds an item to incr. a players max homes.", Arrays.asList("ahi")));
    }

    @Override
    public void disable() {
    }

    private void populateExtraHomeGroups() {
        FileConfiguration fg = EssentialsAddHomes.getEssentials().getConfig();
        for (int i = 0; i < 100; ++i) {
            if (EssentialsAddHomes.getEssentials().getConfig().getConfigurationSection("sethome-multiple").contains(EssentialsAddHomes.getEssentialsGroup(i)))
                continue;
            Bukkit.getLogger().info("Creating new sethome-multiple group, " + EssentialsAddHomes.getEssentialsGroup(i) + " ...");
            fg.getConfigurationSection("sethome-multiple").set(EssentialsAddHomes.getEssentialsGroup(i), i);
        }
        try {
            fg.save("plugins/Essentials/config.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean setupPermissions() {
        RegisteredServiceProvider permissionProvider = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (permissionProvider != null) {
            permission = (Permission) permissionProvider.getProvider();
        }
        return permission != null;
    }
}

