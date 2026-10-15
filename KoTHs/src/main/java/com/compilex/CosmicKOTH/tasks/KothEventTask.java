package com.compilex.CosmicKOTH.tasks;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.compilex.CosmicKOTH.customfiles.CustomConfig;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.JSONMessage;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicbosses.utils.StringUtils;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.giftcardshop.GiftcardAPI;
import net.minecraft.server.v1_8_R3.EntityFireworks;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.Arkkit.utils.RandomFireworks;
import org.arkhamnetwork.Arkkit.utils.WorldGuardUtils;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftFirework;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class KothEventTask
        implements Runnable {
    public final String KOTH_ARENA_REGION = "koth_arena";
    public final String KOTH_ARENA_CAPPING_REGION = "koth_center";
    public final String METADATA_KOTH_CAPPING_START = "KOTH_CAPPING_START";
    Field fireworkField;
    private boolean capped = false;
    private int taskId = -1;
    private Player playerCapping;
    private Player playerCapped = null;
    private int runtime;
    public static boolean running;

    public KothEventTask() {
        try {
            this.fireworkField = EntityFireworks.class.getDeclaredField("ticksFlown");
            this.fireworkField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        Bukkit.getScheduler().runTask(CosmicKOTHPlugin.get_(), new Runnable() {

            @Override
            public void run() {
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&6&lAstro&f&lPvP &8| &6&lDesert K.O.T.H")));
                Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fA &6/koth&f event is starting!")));
                Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fCapture koth to win a &610$ Buycraft Giftcard&f.")));
                Bukkit.broadcastMessage("");
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1.2f, 1.6f);
                }
            }
        });
    }

    public static boolean isRunning() {
        return running;
    }


    @SneakyThrows
    @Override
    public void run() {
        if (!this.capped) {
            ++this.runtime;
            try {
                this.playerCapHandler();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            this.advertHandler();
        }
    }

    private void advertHandler() {
        if (this.runtime % 300 == 0) {
            Bukkit.broadcastMessage("");
            Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&6&lAstro&f&lPvP &8| &6&lDesert K.O.T.H")));
            Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fA &6/koth&f event is currently running!")));
            Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fCapture koth to win a &610$ Buycraft Giftcard&f.")));
            Bukkit.broadcastMessage("");
        }
    }

    private void launchKOTHFirework(Location l) {
        Firework fw = (Firework) l.getWorld().spawnEntity(l, EntityType.FIREWORK);
        FireworkMeta fwm = fw.getFireworkMeta();
        FireworkEffect effect = FireworkEffect.builder().flicker(true).withColor(Color.AQUA).withFade(Color.PURPLE).with(FireworkEffect.Type.CREEPER).trail(true).build();
        fwm.addEffect(effect);
        fwm.setPower(0);
        fw.setFireworkMeta(fwm);
        CraftFirework craftFire = (CraftFirework) fw;
        try {
            this.fireworkField.set(craftFire.getHandle(), 1);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    private void playerCapHandler() throws Exception {
        if (this.playerCapping != null && this.playerCapping.isValid() && !this.playerCapping.isDead() && this.playerCapping.isOnline()) {
            Player playerCapping = this.playerCapping;
            if (playerCapping.hasMetadata("KOTH_CAPPING_START")) {
                String regionName = WorldGuardUtils.getRegionName(this.playerCapping.getLocation());
                if (!regionName.equals("koth_center")) {
                    this.kothBroadcast(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + ChatColor.BOLD + this.getNameOfFlagCapper() + ChatColor.RED + " is no longer capping KOTH!");
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), this.playerCapping.getLocation(), 0, 1, 0, 0.6f, 30);
                    this.playerCapping.playSound(this.playerCapping.getLocation(), Sound.ANVIL_BREAK, 1.0f, 0.7f);
                    this.playerCapping = null;
                    return;
                }
                int secondsLeft = this.getSecondsUntilCap();
                if (secondsLeft >= 300 && secondsLeft % 300 == 0) {
                    this.kothBroadcast(ChatColor.GREEN.toString() + ChatColor.BOLD + "(!) " + ChatColor.GREEN + ChatColor.BOLD + this.getNameOfFlagCapper() + ChatColor.GREEN + " is capping KOTH! " + ChatColor.GRAY + "[" + secondsLeft / 60 + "m 0s left]");
                }
                if (secondsLeft < 300 && secondsLeft > 30 && secondsLeft % 60 == 0) {
                    this.kothBroadcast(ChatColor.GREEN.toString() + ChatColor.BOLD + "(!) " + ChatColor.GREEN + ChatColor.BOLD + this.getNameOfFlagCapper() + ChatColor.GREEN + " is capping KOTH! " + ChatColor.GRAY + "[" + secondsLeft / 60 + "m 0s left]");
                }
                if (secondsLeft == 30 || secondsLeft <= 10) {
                    this.kothBroadcast(ChatColor.GREEN.toString() + ChatColor.BOLD + "(!) " + ChatColor.GREEN + ChatColor.BOLD + this.getNameOfFlagCapper() + ChatColor.GREEN + " is capping KOTH! " + ChatColor.GRAY + "[0m " + secondsLeft + "s left]");
                }
                if (secondsLeft <= 0) {
                    this.captureKOTH(this.playerCapping);
                }
                return;
            }
        }
        this.playerCapping = null;
        Player pClosest = null;
        double lowestDistance = 2.147483647E9;
        for (Player p : this.getKothPlayers()) {
            String regionName2 = WorldGuardUtils.getRegionName(p.getLocation());
            if (!regionName2.equals("koth_center") || CosmicKOTHPlugin.get_().getEss3() != null && CosmicKOTHPlugin.get_().getEss3().getUser(p).isVanished() || p.getGameMode() != GameMode.SURVIVAL)
                continue;
            double dist;
            try {
                dist = p.getLocation().distanceSquared(CosmicKOTHPlugin.get_().getKothCenter());
            } catch (Exception e) {
                dist = 0;
            }
            if (pClosest != null && dist >= lowestDistance) continue;
            pClosest = p;
            lowestDistance = dist;
        }
        if (pClosest != null) {
            this.setPlayerCapping(pClosest);
        }
    }

    public void captureKOTH(Player player) throws ParseException, UnirestException, IOException {
        FPlayer fplayer;
        this.capped = true;
        running = false;
        Bukkit.broadcastMessage("");
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&6&lAstro&f&lPvP &8| &6&lDesert K.O.T.H")));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fThe &6Koth&f has been Captured!")));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&fThe winner received a &6Koth Lootbag &fand &610$ Buycraft&f.")));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(""));
        Bukkit.broadcastMessage(StringUtils.getCenteredMessage(CC.translate("&6&lWinner&7: &f" + this.getNameOfFlagCapper())));
        Bukkit.broadcastMessage("");

        this.logWinner(player);
        this.logFactionWin(player);
        this.playerCapped = player;
        this.playerCapping = null;

        Location l = this.playerCapped.getEyeLocation();
        for (int i = 0; i < 10; ++i) {
            Bukkit.getScheduler().runTaskLater(CosmicKOTHPlugin.get_(), () -> RandomFireworks.getManager().launchRandomFirework(l), 20 * i);
        }
        if (this.playerCapped.getInventory().firstEmpty() != -1) {
            player.getInventory().addItem(CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("KothBag").createClaimableItem(false));
        } else {
            this.kothBroadcast("");
            this.kothBroadcast(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + this.playerCapped.getName() + " did not have any inventory space, so the Astro Koth Lootbag has been DROPPED!");
            this.kothBroadcast("");
            Item j = this.playerCapped.getWorld().dropItem(player.getLocation(), CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("KothBag").createClaimableItem(false));
            j.setMetadata("kothLootbag", new FixedMetadataValue(CosmicKOTHPlugin.get_(), true));
        }
        if (ArkPlugins.COMBAT_LOG.getArkPlugin().isRunning()) {
            CombatLog.flagCombat(this.playerCapped);
        }

        if (Math.random() < 0.15) {
            GiftcardAPI.createGiftCard(this.playerCapped, 10);
        } else {
            this.playerCapped.sendMessage(CC.translate("&a&l(!)&a Oh no, you got unlucky and didn't get the 10$ KOTH giftcard, better luck next time (&715% chance&a)"));
        }
        LauncherTask.countdown = 7400;

    }

    public void logWinner(Player p) {
        try {
            String faction;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date date = new Date();
            FPlayer fp = FPlayers.getInstance().getByPlayer(p);
            String facId = fp.getFaction().isNormal() ? "id=" + fp.getFactionId() : "";
            String string = faction = fp.getFaction().isNormal() ? fp.getFaction().getTag() : "N/A";
            if (!new File("plugins/CosmicKOTH/koth.log").exists()) {
                new File("plugins/CosmicKOTH").mkdir();
                new File("plugins/CosmicKOTH/koth.log").createNewFile();
            }
            Files.write(Paths.get("plugins/CosmicKOTH/koth.log"), ("\n[" + dateFormat.format(date) + "] <" + faction + "> " + p.getName() + " (" + p.getUniqueId().toString() + ") at " + CosmicKOTHPlugin.get_().getKoth().getRuntime() + facId).getBytes(), StandardOpenOption.APPEND);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void logFactionWin(Player p) throws IOException {
        String factionId = FactionUtils.getFactionId(p);
        int wins;
        if (factionId == null) return;
        wins = CosmicKOTHPlugin.get_().getKothWins().getOrDefault(factionId, 0);
        CosmicKOTHPlugin.get_().getKothWins().put(factionId, wins + 1);

        CustomConfig.get().set("kothWins", CosmicKOTHPlugin.get_().getKothWins());

        CustomConfig.save();
        CustomConfig.reload();

    }


    public String getNameOfFlagCapper() {
        if (this.playerCapping == null && this.playerCapped == null) {
            return "???";
        }
        FPlayer fp = FPlayers.getInstance().getByPlayer(this.playerCapping != null ? this.playerCapping : this.playerCapped);
        if (fp.getFaction().isNormal()) {
            return "[" + fp.getFaction().getTag() + "] " + (this.playerCapping != null ? this.playerCapping.getName() : this.playerCapped.getName());
        }
        return this.playerCapping != null ? this.playerCapping.getName() : this.playerCapped.getName();
    }

    public int getSecondsUntilCap() {
        if (this.playerCapping != null) {
            Player playerCapping = this.playerCapping;
            this.getClass();
            if (playerCapping.hasMetadata("KOTH_CAPPING_START")) {
                Player playerCapping2 = this.playerCapping;
                this.getClass();
                long capStart = playerCapping2.getMetadata("KOTH_CAPPING_START").get(0).asLong();
                long msLeft = 300000L - (System.currentTimeMillis() - capStart);
                int secondsLeft = (int) (msLeft / 1000L);
                return secondsLeft;
            }
        }
        return -1;
    }

    public void setPlayerCapping(Player p) {
        Player playerCapping = this.playerCapping = p;
        this.getClass();
        playerCapping.setMetadata("KOTH_CAPPING_START", new FixedMetadataValue(CosmicKOTHPlugin.get_(), System.currentTimeMillis()));
        this.kothBroadcast(ChatColor.GREEN.toString() + ChatColor.BOLD + "(!) " + ChatColor.GREEN + ChatColor.BOLD + this.getNameOfFlagCapper() + ChatColor.GREEN + " is capping KOTH! " + ChatColor.GRAY + "[5m 0s left]");
    }

    public void kothBroadcast(String msg) {
        for (Player p : this.getKothPlayers()) {
            p.sendMessage(msg);
        }
    }

    public void kothBroadcast(JSONMessage message) {
        for (Player p : this.getKothPlayers()) {
            message.sendToPlayer(p);
        }
    }

    public List<Player> getKothPlayers() {
        return Bukkit.getWorld("world").getPlayers();
    }

    public boolean isCapped() {
        return this.capped;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskId() {
        return this.taskId;
    }

    public Player getPlayerCapping() {
        return this.playerCapping;
    }

    public Player getPlayerCapped() {
        return this.playerCapped;
    }

    public int getRuntime() {
        return this.runtime;
    }
}

