package org.arkhamnetwork.Arkkit.plugins;

import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.utils.CommandUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class ArkPlugin {
    protected static Random random = new Random();
    private final List<Listener> listeners = new ArrayList<Listener>();
    private final List<Integer> task_ids = new ArrayList<Integer>();
    private final List<Command> commands = new ArrayList<Command>();
    public long enable_time = 0L;
    public boolean disableCalled = false;
    private Plugin bukkit_plugin = null;
    private boolean running = false;

    public ArkPlugin(Plugin p) {
        this.bukkit_plugin = p;
    }

    public void enable() throws IOException {
        this.disableCalled = false;
    }

    public void disable() throws IOException {
        this.disableCalled = true;
    }

    public void inject() {
        this.running = true;
        this.enable_time = System.currentTimeMillis();
        try {
            this.enable();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "[Compilex]" + ChatColor.WHITE.toString() + " Injected " + ChatColor.YELLOW.toString() + this.getClass().getSimpleName() + ChatColor.WHITE.toString() + " patch.");
        } catch (Exception err) {
            err.printStackTrace();
            this.kill();
        }
    }

    public void kill() {
        try {
            this.disable();
            this.unregisterListeners();
            this.unregisterTasks();
            this.unregisterCommands();
            Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "[Compilex]" + ChatColor.WHITE.toString() + " Killed " + ChatColor.RED.toString() + this.getClass().getSimpleName() + ChatColor.WHITE.toString() + " patch.");
            this.enable_time = 0L;
            this.running = false;
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    public void registerListener(Listener l) {
        this.listeners.add(l);
        Bukkit.getPluginManager().registerEvents(l, this.bukkit_plugin);
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Registered me.astropvp.outpost.listener: " + l.getClass().getSimpleName() + "!");
    }

    public void registerTask(BukkitTask bt, Class<?> c) {
        this.task_ids.add(bt.getTaskId());
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Registered task: " + c.getSimpleName() + "!");
    }

    public void registerCommand(AbstractCommand acmd) {
        acmd.register();
        this.commands.add(AbstractCommand.cmap.getCommand(acmd.command));
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Registered command: /" + acmd.command + "!");
    }

    public void unregisterCommand(Command pcmd) {
        Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Unregistered command: /" + pcmd.getName() + "!");
        CommandUtils.unRegisterBukkitCommand(pcmd);
    }

    public void unregisterListeners() {
        for (Listener l : this.listeners) {
            Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Unregistered me.astropvp.outpost.listener: " + l.getClass().getSimpleName() + "!");
            HandlerList.unregisterAll(l);
            Object var2_2 = null;
        }
        this.listeners.clear();
    }

    public void unregisterTasks() {
        for (int i : this.task_ids) {
            Bukkit.getScheduler().cancelTask(i);
            Bukkit.getLogger().info("(" + this.getClass().getSimpleName() + ") Unregistered task: #" + i + "!");
        }
        this.task_ids.clear();
    }

    public void unregisterCommands() {
        for (Command pcmd : this.commands) {
            this.unregisterCommand(pcmd);
        }
        this.commands.clear();
    }

    public Plugin getBukkitPlugin() {
        return this.bukkit_plugin;
    }

    public boolean isRunning() {
        return this.running;
    }
}

