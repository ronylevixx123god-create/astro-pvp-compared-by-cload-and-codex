package me.astropvp.discordsync.managers;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import me.astropvp.discordsync.DiscordSyncPlugin;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.entity.Player;

public class SyncManager {
    private final DiscordSyncPlugin plugin;
    private final HashMap<Integer, UUID> activeSyncMap = new HashMap<>();
    private static final long ASTRO_GUILD_ID = 470324147411615744L;
    private final Guild astroDiscord;

    public HashMap<Integer, UUID> getActiveSyncMap() {
        return this.activeSyncMap;
    }

    public SyncManager(DiscordSyncPlugin plugin) {
        this.plugin = plugin;
        this.astroDiscord = plugin.getJda().getGuildById(ASTRO_GUILD_ID);
    }

    public int getRandomCode() {
        int randomCode;
        for(randomCode = 0; this.isActiveCode(randomCode) || randomCode == 0; randomCode = ThreadLocalRandom.current().nextInt(10000, 99999)) {

        }

        return randomCode;
    }

    public int getActiveSyncCode(Player player) {
        return (Integer)this.activeSyncMap.keySet().stream().filter((code) -> ((UUID)this.activeSyncMap.get(code)).equals(player.getUniqueId())).findFirst().orElse(-1);
    }

    public User getLinkedUser(UUID uuid) {
        if (!this.plugin.getDatabaseManager().contains(uuid)) {
            return null;
        } else {
            try {
                return this.plugin.getJda().getUserById(this.plugin.getDatabaseManager().get(uuid));
            } catch (Exception var3) {
                var3.printStackTrace();
                return null;
            }
        }
    }

    public boolean isCurrentlySyncing(Player player) {
        return this.activeSyncMap.containsValue(player.getUniqueId());
    }

    public boolean isAccountLinked(UUID uuid) {
        return this.getLinkedUser(uuid) != null;
    }

    public boolean isActiveCode(int code) {
        return this.activeSyncMap.containsKey(code);
    }

    public DiscordSyncPlugin getPlugin() {
        return this.plugin;
    }

    public Guild getAstroDiscord() {
        return this.astroDiscord;
    }

}
