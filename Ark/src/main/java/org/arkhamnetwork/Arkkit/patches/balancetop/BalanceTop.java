/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.earth2me.essentials.Essentials
 *  com.google.common.collect.Lists
 *  net.milkbowl.vault.economy.Economy
 *  org.bukkit.Bukkit
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.RegisteredServiceProvider
 */
package org.arkhamnetwork.Arkkit.patches.balancetop;

import com.earth2me.essentials.Essentials;
import com.google.common.collect.Lists;
import net.milkbowl.vault.economy.Economy;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

public class BalanceTop
        extends ArkPlugin {
    public static Economy economy;

    public BalanceTop(Plugin p) {
        super(p);
    }

    public static Economy getEconomy() {
        return economy;
    }

    @Override
    public void enable() {
        this.setupEconomy();
        this.registerListeners();
        this.registerCommands(true);
        try {
            BalanceManager.loadBalances();
        } catch (Exception e) {
            e.printStackTrace();
        }
        BalanceManager.getPages();
    }

    @Override
    public void disable() {
        System.out.println("Saving baltop balances..");
        BalanceManager.saveBalances();
    }

    public void registerCommands(boolean coolkidway) {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            if (coolkidway) {
                System.out.println("Unregistering /baltop..");
                Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
                ess.getCommand("balancetop").setExecutor(new CommandBalanceTop());
                ess.getCommand("balancetop").setUsage("");
            } else {
                Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
                CommandUtils.unRegisterBukkitCommand(ess, ess.getCommand("balancetop"));
            }
        } else {
            this.registerCommand(new CommandBalanceTopAbstract("balancetop", "/<command>", "View top balances on the server.", Lists.newArrayList("baltop", "ebaltop", "ebalancetop")));
        }
    }

    public void registerListeners() {
        this.registerListener(new PlayerListener());
    }

    private boolean setupEconomy() {
        RegisteredServiceProvider rsp = Arkkit.get().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = (Economy) rsp.getProvider();
        return economy != null;
    }
}

