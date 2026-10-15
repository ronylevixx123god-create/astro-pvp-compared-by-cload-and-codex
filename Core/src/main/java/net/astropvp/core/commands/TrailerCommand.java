package net.astropvp.core.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import net.astropvp.core.AstroCore;
import net.astropvp.core.cooldown.RealtimeUUIDCooldown;
import net.astropvp.core.utils.YouTubeVideoUrl;
import org.apache.commons.io.IOUtils;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Locale;
import java.util.UUID;
import java.util.Objects;
import java.util.stream.Collectors;

public class TrailerCommand implements CommandExecutor {
    public static String REQUIRED_VIDEO_TITLE = "AstroPvP Map 11 Trailer | CosmicTest";

    public static List<String> claimedVideoIds = AstroCore.getInstance().getConfig().getStringList("players-trailers").stream()
            .map(YouTubeVideoUrl::normalizeStoredId)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());

    private Map<UUID, VideoInfoFetchTask> fetcherTaskMap = Maps.newHashMap();

    private RealtimeUUIDCooldown cooldownMap = new RealtimeUUIDCooldown("trailer");

    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command is only available to players.");
            return true;
        }
        Player player = (Player) sender;
        if (AstroCore.getInstance().getConfig().getString("youtube-api-key", "").trim().isEmpty()) {
            player.sendMessage(CC.translate("&cTrailer verification is currently unavailable."));
            return true;
        }
        if (args.length == 0) {
            player.sendMessage(CC.translate("&c&l(!) &cPlease enter a video url!"));
            return true;
        }
        final String videoId = getVideoIdFromUrl(args[0]);
        if (this.fetcherTaskMap.containsKey(player.getUniqueId())) {
            player.sendMessage( CC.translate("&cPlease wait..."));
            return true;
        }
        if (claimedVideoIds.contains(videoId)) {
            player.sendMessage( CC.translate("&c&l(!) Rewards Already Claimed"));
            player.sendMessage( CC.translate("&7You have already claimed your rewards for this video!"));
            return true;
        }
        if (videoId == null) {
            player.sendMessage( CC.translate("&c&l(!) Link Invalid"));
            player.sendMessage( CC.translate("&7You must have a valid youtube video url."));
            player.sendMessage( CC.translate("&7(It should be in this format: https://www.youtube.com/watch?v=)"));
            return true;
        }
        if (this.cooldownMap.isOnCooldown(player, "trailer")) {
            player.sendMessage( CC.translate("&c&l(!) &cPlease wait a little bit before doing that."));
            return true;
        }
        this.cooldownMap.add(player, System.currentTimeMillis() + 15000L);
        player.sendMessage( CC.translate("&aPlease wait while your video is verified..."));
        final VideoInfoFetchTask task = new VideoInfoFetchTask(player.getUniqueId(), videoId);
        (new BukkitRunnable() {
            private int attempts;

            public void run() {
                if (!player.isOnline()) {
                    TrailerCommand.this.fetcherTaskMap.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                if (!task.isComplete() && ++attempts < 30) {
                    return;
                }
                if (claimedVideoIds.contains(videoId)) {
                    player.sendMessage(CC.translate("&cRewards have already been claimed for this video."));
                    TrailerCommand.this.fetcherTaskMap.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                String videoTitle = task.getTitle();
                String videoDescription = task.getDescription();
                if (videoTitle == null || videoDescription == null) {
                    player.sendMessage( CC.translate("&c&lERROR&c: Please try again in a bit."));
                    TrailerCommand.this.fetcherTaskMap.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                if (!videoTitle.equalsIgnoreCase(TrailerCommand.REQUIRED_VIDEO_TITLE)) {
                    player.sendMessage( CC.translate("&c&l(!) Invalid Video Title"));
                    player.sendMessage( CC.translate("&7Your video title doesn't match the required title."));
                    player.sendMessage( CC.translate(""));
                    player.sendMessage( CC.translate("&c&lYour Title:"));
                    player.sendMessage( CC.translate("&7\"&c&m" + videoTitle + "&7\""));
                    player.sendMessage( "");
                    player.sendMessage( CC.translate("&a&lRequired Title:"));
                    player.sendMessage( CC.translate("&7\"&a" + TrailerCommand.REQUIRED_VIDEO_TITLE + "&7\""));
                    player.sendMessage( "");
                    player.sendMessage( CC.translate("&7After changing title, allow time to update."));
                    TrailerCommand.this.fetcherTaskMap.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                if (!videoDescription.toLowerCase(Locale.ROOT).contains(sender.getName().toLowerCase(Locale.ROOT))) {
                    player.sendMessage( CC.translate("&c&l(!) Confirmation Required"));
                    player.sendMessage( "");
                    player.sendMessage( CC.Gray + videoDescription);
                    player.sendMessage( "");
                    player.sendMessage( CC.translate("&7Please insert &f'" + sender.getName() + "'&7 in the description of your video."));
                    player.sendMessage( CC.translate("&7Then please redo &f/claim <link> &7to confirm once again."));
                    TrailerCommand.this.fetcherTaskMap.remove(player.getUniqueId());
                    cancel();
                    return;
                }
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0F, 1.0F);
                player.sendMessage( CC.translate("&a&l(!) Rewards Claimed"));
                player.sendMessage( CC.translate("&7You have successfully claimed your SOTW rewards."));
                TrailerCommand.claimedVideoIds.add(videoId);
                AstroCore.getInstance().getConfig().set("players-trailers", claimedVideoIds);
                AstroCore.getInstance().saveConfig();
                AstroCore.getInstance().reloadConfig();
                player.getInventory().addItem(CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("TrailerLootbox").createClaimableItem(false));
                TrailerCommand.this.fetcherTaskMap.remove(player.getUniqueId());
                cancel();
            }
        }).runTaskTimer((Plugin)AstroCore.getInstance(), 20L, 20L);
        return true;
    }

    public String getVideoIdFromUrl(String url) {
        return YouTubeVideoUrl.parse(url);
    }

    public class VideoInfoFetchTask extends BukkitRunnable {
        private UUID playerUUID;

        private String videoId;

        private String title;

        private String description;

        public UUID getPlayerUUID() {
            return this.playerUUID;
        }

        public String getVideoId() {
            return this.videoId;
        }

        private volatile boolean complete = false;

        public boolean isComplete() {
            return this.complete;
        }

        public VideoInfoFetchTask(UUID uuid, String videoId) {
            TrailerCommand.this.fetcherTaskMap.put(uuid, this);
            this.playerUUID = uuid;
            this.videoId = videoId;
            runTaskAsynchronously((Plugin) AstroCore.getInstance());
        }

        public void run() {
            HttpURLConnection con = null;
            try {
                String urlString = "https://www.googleapis.com/youtube/v3/videos?part=id%2Csnippet&id=" + this.videoId
                        + "&key=" + java.net.URLEncoder.encode(AstroCore.getInstance().getConfig().getString("youtube-api-key", "").trim(), "UTF-8");
                con = (HttpURLConnection) new URL(urlString).openConnection();
                con.setConnectTimeout(5000);
                con.setReadTimeout(10000);
                con.setRequestMethod("GET");
                try (InputStream inputStream = con.getInputStream()) {
                    StringWriter writer = new StringWriter();
                    IOUtils.copy(inputStream, writer, "UTF-8");
                    JsonObject response = new Gson().fromJson(writer.toString(), JsonObject.class);
                    JsonArray items = response.getAsJsonArray("items");
                    if (items == null || items.size() == 0) {
                        return;
                    }
                    JsonObject snippet = items.get(0).getAsJsonObject().getAsJsonObject("snippet");
                    this.title = snippet.get("title").getAsString();
                    this.description = snippet.get("description").getAsString();
                }
            } catch (Exception failure) {
                AstroCore.getInstance().getLogger().warning("Trailer verification failed (" + failure.getClass().getSimpleName() + ").");
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                this.complete = true;
            }
        }

        public String getDescription() {
            if (!this.complete)
                return null;
            return this.description;
        }

        public String getTitle() {
            if (!this.complete)
                return null;
            return this.title;
        }
    }
}
