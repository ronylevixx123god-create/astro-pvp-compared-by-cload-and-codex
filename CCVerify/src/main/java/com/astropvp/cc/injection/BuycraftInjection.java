package com.astropvp.cc.injection;

import com.astropvp.cc.CCVerify;
import net.buycraft.plugin.bukkit.BuycraftPlugin;
import net.buycraft.plugin.bukkit.BuycraftPluginBase;
import net.buycraft.plugin.execution.strategy.QueuedCommandExecutor;
import org.bukkit.Bukkit;

import java.lang.reflect.Field;

public class BuycraftInjection {

    private static boolean injected = false;

    public static void inject(){
        if(injected)return;
        injected = true;

        BuycraftPlugin plugin = BuycraftPlugin.getPlugin(BuycraftPlugin.class);
        InjectedCommandQueue injectedCommandQueue = new InjectedCommandQueue(plugin.getPlatform(), plugin.getCompletedCommandsTask());

        injectedCommandQueue.setRunMaxCommandsBlocking(plugin.getConfiguration().getCommandsPerTick());
        try{
            Field commandExecutor = BuycraftPluginBase.class.getDeclaredField("commandExecutor");

            commandExecutor.setAccessible(true);

            commandExecutor.set(plugin, injectedCommandQueue);

            Bukkit.getScheduler().runTaskTimer(plugin, injectedCommandQueue, 1L, 1L);

            Bukkit.getLogger().info(String.format("[%s] - Injected into Buycraft!", CCVerify.get().getName()));
        }catch(Exception exc){
            Bukkit.getLogger().info(String.format("[%s] - Couldn't inject into Buycraft!", CCVerify.get().getName()));
            exc.printStackTrace();
        }
    }
}
