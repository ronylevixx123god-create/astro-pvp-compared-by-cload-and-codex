package me.astropvp.proxypach.tasks;

import com.massivecraft.factions.Factions;
import lombok.Getter;
import me.astropvp.proxypach.ProxyPatch;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ProxyPatchTask extends BukkitRunnable {

    private int count = 0;

    @Getter
    private final List<String> factionNames;


    public ProxyPatchTask() {
        this.factionNames = new ArrayList<>();
        factionNames.add("monkeys");
        factionNames.add("zodiac");
        factionNames.add("rush");
        factionNames.add("bermuda");
        factionNames.add("hated");
        factionNames.add("Acid");
        factionNames.add("WhySoSalty");
    }

    @Override
    public void run() {
        if (count >= ProxyPatch.getInstance().getConfig().getInt("max-accounts")) return;
        if(count >= ProxyPatch.playerNames.size()-1) count = 0;
        String playerName = ProxyPatch.playerNames.get(count);
        count++;
        Set<String> redo = new HashSet<>();
        if(Bukkit.getPlayer(playerName) != null) return;
        for (int i = 0; i < 1; i++) {
            if(Bukkit.getPlayer(playerName) != null) continue;
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spoof add " + playerName);
            redo.add(playerName);
            //ServerCommandEvent serverCommandEvent = new ServerCommandEvent(Bukkit.getConsoleSender(), "spoof add " + playerName);
            //serverCommandEvent.callEvent();
        }
        Bukkit.getScheduler().runTaskLater(ProxyPatch.getInstance(), ()-> {
            if (Bukkit.getPlayer(playerName) == null) return;
            for (String faction : factionNames) {
                if (Factions.getInstance().getByTag(faction) == null) {
                    Bukkit.getPlayer(playerName).chat("/f create " + faction);
                    Bukkit.getPlayer(playerName).chat("/f open");
                    System.out.println("[Spoof] Creating a faction : " + faction);
                } else {
                    int index = new Random().nextInt(factionNames.size()-1) ;
                    Bukkit.getPlayer(playerName).chat("/f join " + factionNames.get(index));
                    System.out.println("[Spoof] Joining a faction : " + factionNames.get(index));
                }
            }
        }, 20L * 5L);

        Bukkit.getScheduler().runTaskLater(ProxyPatch.getInstance(), ()-> {
            for (String s : redo) {
                for (int i = 0; i < 1; i++) {
                    if(Bukkit.getPlayer(s) != null) continue;
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spoof add " + s);
                }
            }
        }, 1L);



    }

    public void end() {
        cancel();
        for(String playerName : ProxyPatch.playerNames) {
            //if(Bukkit.getPlayer(playerName) == null) continue; // continue if player exists :p
            //ServerCommandEvent serverCommandEvent = new ServerCommandEvent(Bukkit.getConsoleSender(), "spoof remove " + playerName);
            //serverCommandEvent.callEvent();
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spoof remove " + playerName);

        }
    }


}
