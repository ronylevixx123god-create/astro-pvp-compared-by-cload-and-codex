package net.crankedpvp.pvptop.data;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.massivecraft.factions.Faction;
import lombok.Getter;
import lombok.Setter;
import net.astropvp.storage.utils.KeyToSave;
import net.crankedpvp.pvptop.PvPTopAPI;
import net.crankedpvp.pvptop.PvPTopPlugin;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter @Setter
public class FactionData implements KeyToSave {

    private String factionId;
    private Map<Points, Integer> points;

    public void addPoint(Points points) {
        if(this.points.containsKey(points)) {
            int amount = this.points.get(points);
            amount += 1;
            this.points.put(points, amount);
            return;
        }
        this.points.put(points, 1);
    }

    public void removePoint(Points points) {
        if(this.points.containsKey(points)) {
            int amount = this.points.get(points);
            amount -= 1;
            this.points.put(points, amount);
            return;
        }
        this.points.put(points, 0);
    }

    public int calculateAllPoints() {
        int amount = 0;
        for(Points points : this.points.keySet()) {
            if(points == Points.DEATH) {
                amount -= this.points.get(points);
                continue;
            }
            amount += this.points.get(points) * points.getPoints();
        }
        return amount;
    }

    public FactionData(String factionId) {
        this.factionId = factionId;
        this.points = new HashMap<>();
    }

    public int getPointsFor(Points points) {
        return this.points.getOrDefault(points, 0);
    }

    public int getKills() {
        return this.points.getOrDefault(Points.KILL, 0);
    }

    public int getDeaths() {
        return this.points.getOrDefault(Points.DEATH, 0);
    }

    public int getKothCaptured() {
        return this.points.getOrDefault(Points.KOTH_CAPTURE, 0);
    }

    /*
    public int getStrongholdControlled() {
        return this.points.getOrDefault(Points.STRONHOLD_CONTROL, 0);
    }

    public int getCitadelCaptured() {
        return this.points.getOrDefault(Points.CITADEL_CAPTURE, 0);
    }

    public int getDeadmansChestCaptures() {
        return this.points.getOrDefault(Points.DEADMANS_CHEST, 0);
    }

    public int getWarpKothCaptured() {
        return this.points.getOrDefault(Points.WARP_KOTH_CAPTURE, 0);
    }

    public int getCerberusKills() {
        return this.points.getOrDefault(Points.CERBERUS_KILLED, 0);
    }

     */

    @Override
    public String getKey() {
        return this.factionId;
    }

    @Override
    public void setKey(String string) {
        this.factionId = string;
    }

    public enum Points {
        KOTH_CAPTURE(50, "&d&lKoth Capture"),
        //WARP_KOTH_CAPTURE(75, "&5&lWarp Koth Capture"),
        KILL(1, "&a&lPlayer Kill"),
        DEATH(1, "&c&lMember Death"),
        //STRONHOLD_CONTROL(15, "&4&lStronghold Control"),
       // DEADMANS_CHEST(100, "&d&lDead Mans Chest Capture"),
        //CERBERUS_KILLED(150, "&6&lCerberus Slain"),
       // CITADEL_CAPTURE(1000, "&e&lCitadel Capture"),
        ;

        @Getter
        private final int points;
        @Getter
        private final  String due;
        Points(int points, String due) {
            this.points = points;
            this.due = due;
        }

        public void add(Faction faction) {
            FactionData factionData = PvPTopPlugin.getInstance().getPvpTopManager().getOrCreate(faction);
            String plural = this.getPoints() > 1 ? "points" : "point";
            for(Player onlinePlayer : faction.getOnlinePlayers()) {
                onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + this.getPoints() + " &7faction " + plural + " due to " + this.getDue()));
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            factionData.addPoint(this);
        }

        public void remove(Faction faction) {
            FactionData factionData = PvPTopPlugin.getInstance().getPvpTopManager().getOrCreate(faction);
            String plural = this.getPoints() > 1 ? "points" : "point";
            for(Player onlinePlayer : faction.getOnlinePlayers()) {
                onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has lost &c" + this.getPoints() + " &7faction " + plural + " due to " + this.getDue()));
                onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
            }
            factionData.removePoint(this);
        }
    }


        /*
    public final static transient int KOTH_CAPTURE_POINTS = 50;
    public final static transient int WARP_KOTH_CAPTURE_POINTS = 75;
    public final static transient int KILL_POINTS = 1;
    public final static transient int DEATH_POINTS = 1;
    public final static transient int STRONGHOLD_HOLD_POINTS = 15;
    public final static transient int DEADMANS_CHEST_POINTS = 100;
    public final static transient int CITADEL_CAPTURE_POINTS = 1000;
    public final static transient int CERBERUS_KILLED = 150;


     */

    /*
    public void addCerberusKilled() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.CERBERUS_KILLED.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + Points.CERBERUS_KILLED.getPoints() + " &7faction " + plural + " due to " + "&9&lCerberus Killed"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        this.cerberusKilled += 1;
    }

    public void addKothCapture() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.KOTH_CAPTURE.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + Points.KOTH_CAPTURE.getPoints() + " &7faction " + plural + " due to " + "&d&lKoth Capture"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        this.kothsCaptured += 1;
    }

    public void addWarpKothCapture() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.WARP_KOTH_CAPTURE.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + Points.WARP_KOTH_CAPTURE.getPoints() + " &7faction " + plural + " due to " + "&6&lWarp Koth Capture"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        this.warpKothsCaptured += 1;
    }

    public void addCitadelCapture() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.CITADEL_CAPTURE.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + Points.CITADEL_CAPTURE.getPoints() + " &7faction " + plural + " due to " + "&e&lCitadel Capture"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        this.citadelsCaptured += 1;
    }

    public void addStrongholdHold() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.STRONHOLD_CONTROL.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + Points.STRONHOLD_CONTROL.getPoints() + " &7faction " + plural + " due to " + "&4&lStronghold Hold"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        this.strongholdHeld += 1;
    }

    public void addPlayerKill() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.KILL.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + Points.KILL.getPoints() + " &7faction " + plural + " due to " + "&a&lEnemy Kill"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        this.kills += 1;
    }

    public void addPlayerDeath() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.DEATH.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has lost &c" + Points.DEATH.getPoints() + " &7faction " + plural + " due to " + "&c&lMember Death"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LAVA_POP, 1.0f, 1.0f);
        }
        this.deaths += 1;
    }

    public void addDeadMansChestCapture() {
        Faction faction = Factions.getInstance().getFactionById(this.factionId);
        String plural = Points.DEADMANS_CHEST.getPoints() > 1 ? "points" : "point";
        for(Player onlinePlayer : faction.getOnlinePlayers()) {
            onlinePlayer.sendMessage(CC.translate(PvPTopAPI.PREFIX + "Your faction has gained &a" + Points.DEADMANS_CHEST.getPoints() + " &7faction " + plural + " due to " + "&d&lDead Mans Chest Capture"));
            onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        this.deadmansChestCaptured += 1;
    }

     */
}
