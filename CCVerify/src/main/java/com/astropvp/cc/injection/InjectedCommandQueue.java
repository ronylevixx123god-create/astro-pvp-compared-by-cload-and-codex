package com.astropvp.cc.injection;

import net.buycraft.plugin.IBuycraftPlatform;
import net.buycraft.plugin.data.QueuedPlayer;
import net.buycraft.plugin.execution.strategy.CommandExecutor;
import net.buycraft.plugin.execution.strategy.PostCompletedCommandsTask;
import net.buycraft.plugin.execution.strategy.ToRunQueuedCommand;
import net.buycraft.plugin.platform.NoBlocking;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class InjectedCommandQueue implements CommandExecutor, Runnable {
    private static final long MAXIMUM_NOTIFICATION_TIME = TimeUnit.MILLISECONDS.toNanos(15L);

    private final IBuycraftPlatform platform;

    private final boolean blocking;

    private final Set<ToRunQueuedCommand> commandQueue = new LinkedHashSet<>();

    private final PostCompletedCommandsTask completedCommandsTask;

    private int runMaxCommandsBlocking = 10;

    private final PurchaseHandler purchaseHandler;

    public InjectedCommandQueue(IBuycraftPlatform platform, PostCompletedCommandsTask completedCommandsTask) {
        this.platform = platform;
        this.blocking = !platform.getClass().isAnnotationPresent(NoBlocking.class);
        this.completedCommandsTask = completedCommandsTask;

        this.purchaseHandler = new PurchaseHandler();
    }

    public void queue(ToRunQueuedCommand command) {
        synchronized (this.commandQueue) {
            this.commandQueue.add(command);
        }
    }

    public void run() {
        List<ToRunQueuedCommand> runThisTick = new ArrayList<>();

        synchronized (this.commandQueue) {
            ArrayList<Integer> queuedCommandIds = new ArrayList<>();
            Set<ToRunQueuedCommand> removeSet = new HashSet<>();
            for (ToRunQueuedCommand command : this.commandQueue) {
                if (queuedCommandIds.contains(command.getCommand().getId())) {
                    removeSet.add(command);
                    continue;
                }
                queuedCommandIds.add(command.getCommand().getId());
                if (command.canExecute(this.platform)) {
                    runThisTick.add(command);
                    removeSet.add(command);
                }
                if (this.blocking && runThisTick.size() >= this.runMaxCommandsBlocking)
                    break;
            }
            this.commandQueue.removeAll(removeSet);
        }
        long start = System.nanoTime();
        QueuedPlayer currentPlayer = null;
        for (ToRunQueuedCommand command : runThisTick) {
            if (this.completedCommandsTask.getRetained().contains(command.getCommand().getId())) {
                synchronized (this.commandQueue) {
                    this.commandQueue.remove(command);
                }
                continue;
            }
            if (command.canExecute(this.platform)) {
                String finalCommand = this.platform.getPlaceholderManager().doReplace(command.getPlayer(), command.getCommand());
                this.platform.log(Level.INFO, String.format("Dispatching command '%s' for player '%s'.", finalCommand, command.getPlayer().getName()));
                try {
                    this.platform.dispatchCommand(finalCommand);
                    this.completedCommandsTask.add(command.getCommand().getId());

                    if(currentPlayer != command.getPlayer())this.purchaseHandler.runPurchase(command.getCommand().getPackageId(), command.getPlayer().getName());
                } catch (Exception e) {
                    this.platform.log(Level.SEVERE, String.format("Could not dispatch command '%s' for player '%s'. This is typically a plugin error, not an issue with BuycraftX.", finalCommand, command
                            .getPlayer().getName()), e);
                }
            }
            currentPlayer = command.getPlayer();
        }
        long fullTime = System.nanoTime() - start;
        if (fullTime > MAXIMUM_NOTIFICATION_TIME) {
            BigDecimal timeMs = (new BigDecimal(fullTime)).divide(new BigDecimal("1000000"), 2, 2);
            if (this.blocking) {
                this.platform.log(Level.INFO, "Command execution took " + timeMs.toPlainString() + "ms to complete. This may indicate an issue with one of your server's plugins, which can cause lag.");
            } else {
                this.platform.log(Level.INFO, "Command execution took " + timeMs.toPlainString() + "ms to complete. This may indicate an issue with one of your server's plugins, which will slow command execution.");
            }
        }
    }

    public void setRunMaxCommandsBlocking(int runMaxCommandsBlocking) {
        this.runMaxCommandsBlocking = runMaxCommandsBlocking;
    }
}
