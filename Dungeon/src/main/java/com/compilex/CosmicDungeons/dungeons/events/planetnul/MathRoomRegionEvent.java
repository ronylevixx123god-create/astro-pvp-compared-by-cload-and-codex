/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  org.bukkit.entity.Player
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.utils.ChatUtils;
import com.compilex.CosmicDungeons.utils.StringReplacer;
import com.cosmicpvp.cosmicutils.utils.CC;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import org.bukkit.entity.Player;

public class MathRoomRegionEvent
extends RegionEvent {
    private int total = 0;
    private List<UUID> buttonPressedPlayers = new ArrayList<UUID>();

    public MathRoomRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
    }

    @Override
    public void run() {
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (this.dungeon.isHalfTime() ? 90000L : 120000L), "Solve Escape Sequence");
        this.dungeon.broadcastMessage("This is a Teleportation room! Your ship's captain needs your exact coordinates to teleport you to safety, quickly decode it!");
        this.total = 0;
        ArrayList<String> equations = new ArrayList<String>(Arrays.asList("((N O (((N O N) O N) O N)) O N)", "((N O (N O (N O (N O N)))) O N)", "((((N O (N O (N O N))) O N) O N) O N)", "(N O (N O ((N O N) O N)))", "(N O (N O (N O (N O N))))", "(((N O (N O N)) O N) O N)", "(((N O (N O (N O N))) O N) O N)", "(N O (N O (N O N)))", "(N O ((N O ((N O N) O N)) O N))", "(N O (N O ((N O N) O N)))"));
        ScriptEngineManager mgr = new ScriptEngineManager();
        ScriptEngine engine = mgr.getEngineByName("JavaScript");
        try {
            for (Player p : this.dungeon.getPlayers()) {
                DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p);
                if (dp == null || dp.getCurrentDungeon() != this.dungeon) continue;
                String equation = (String)equations.get(new Random().nextInt(equations.size()));
                String replacedNums = StringReplacer.replace(equation, Pattern.compile("N"), m -> Integer.toString(new Random().nextInt(88) + 10));
                String output = StringReplacer.replace(replacedNums, Pattern.compile("O"), m -> (new String[]{"x", "+", "-"})[new Random().nextInt(3)]);
                ChatUtils.sendCenteredMessage(p, "");
                ChatUtils.sendCenteredMessage(p, CC.translate((String)"&b&lEscape Sequence"));
                ChatUtils.sendCenteredMessage(p, CC.translate((String)("&7" + this.dungeon.getPlayers().stream().filter(pl -> {
                    DungeonPlayer dpPl = DungeonsAPI.get().getPlayerDungeonData((Player)pl);
                    return dpPl != null && dpPl.getCurrentDungeon() == this.dungeon;
                }).collect(Collectors.toList()).size() + " player(s) in the dungeon have received a unique")));
                ChatUtils.sendCenteredMessage(p, CC.translate((String)"&7mathematical equation to solve. The sum of each equation's"));
                ChatUtils.sendCenteredMessage(p, CC.translate((String)"&7answer is the escape sequence."));
                ChatUtils.sendCenteredMessage(p, CC.translate((String)("&b&lYour Equation: &d" + output)));
                ChatUtils.sendCenteredMessage(p, "");
                ChatUtils.sendCenteredMessage(p, "&b&lClick the button");
                ChatUtils.sendCenteredMessage(p, "&7Enter &b/answer <answer>&7 &7to solve equation");
                ChatUtils.sendCenteredMessage(p, "");
                int result = (Integer)engine.eval(output.replaceAll("x", "\\*"));
                this.total += result;
            }
        }
        catch (ScriptException e) {
            e.printStackTrace();
        }
    }

    public List<UUID> getButtonPressedPlayers() {
        return this.buttonPressedPlayers;
    }

    public int getTotal() {
        return this.total;
    }
}

