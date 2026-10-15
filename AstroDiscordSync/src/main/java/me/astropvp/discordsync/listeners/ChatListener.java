package me.astropvp.discordsync.listeners;


import me.astropvp.discordsync.DiscordSyncPlugin;
import me.astropvp.discordsync.utils.CC;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class ChatListener implements Listener {
    private static final long ASTRO_GUILD_ID = 470324147411615744L;
    public static final String LEADER_BADGE = CC.translate("&9&l❖");
    public static final String BOOSTER_BADGE = CC.translate("&d&l✪");
    private static final Set<UUID> nitroBoosterList = new HashSet<>();
    private static final Set<UUID> factionLeaderList = new HashSet<>();
    private static final Set<UUID> youtuberList = new HashSet<>();
    private static final Set<UUID> famousList = new HashSet<>();

    private final String nitroBoosterRole;
    private final String factionLeaderRole;
    private final String mediaRole;
    private final String famousRole;

    private final String galaxyRole;

    private final Guild astroDiscord;
    private final DiscordSyncPlugin plugin;

    public ChatListener(DiscordSyncPlugin plugin) {
        this.plugin = plugin;
        this.astroDiscord = plugin.getJda().getGuildById(ASTRO_GUILD_ID);
        this.nitroBoosterRole = "646509833448587325";
        this.factionLeaderRole = "709442838592618608";
        this.mediaRole = "517312548136026112";
        this.famousRole = "981939448755392512";
        this.galaxyRole = "914174702853951508";

        (new BukkitRunnable() {
            public void run() {
                ChatListener.this.updateNitroBoosters();
            }
        }).runTaskTimer(plugin, 40L, 2000L);
    }

    public void updateNitroBoosters() {
        if (this.plugin.getDatabaseManager().isConnected()) {
            Bukkit.getScheduler().runTask(this.plugin, () -> {
                Map<UUID, Long> uuidUserMap = this.plugin.getDatabaseManager().fetchMap();
                Set<UUID> toAdd = new HashSet<>();
                Set<UUID> toAddLeaders = new HashSet<>();
                Set<UUID> toAddYoutubers = new HashSet<>();
                Set<UUID> toAddFamous = new HashSet<>();
                Set<UUID> toAddGalaxyRanks = new HashSet<>();

                for (UUID uuid : uuidUserMap.keySet()) {
                    Long discordId = uuidUserMap.get(uuid);
                    User user = this.plugin.getJda().getUserById(discordId);
                    this.plugin.getJda().getGuilds().stream().filter((g) -> g.getName().contains("AstroPvP")).findFirst().ifPresent((g) -> {
                        Member member = this.getMemberById(user, g);
                        if (user != null && member != null) {
                            if (member.getRoles().stream().anyMatch((r) -> r.getId().equals(nitroBoosterRole))) {
                                toAdd.add(uuid);
                            }

                            if (member.getRoles().stream().anyMatch((r) -> r.getId().equals(factionLeaderRole))) {
                                toAddLeaders.add(uuid);
                            }

                            if (member.getRoles().stream().anyMatch((r) -> r.getId().equals(mediaRole))) {
                                toAddYoutubers.add(uuid);
                            }

                            if (member.getRoles().stream().anyMatch((r) -> r.getId().equals(famousRole))) {
                                toAddFamous.add(uuid);
                            }
                        }
                    });
                }




                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    factionLeaderList.addAll(toAddLeaders);
                });
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    nitroBoosterList.addAll(toAdd);
                });
                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    youtuberList.addAll(toAddYoutubers);
                });

                Bukkit.getScheduler().runTask(this.plugin, () -> {
                    famousList.addAll(toAddFamous);
                });
                for (Player player : Bukkit.getOnlinePlayers()) {
                    checkPermissions(player);
                }

            });
        }
    }

    private void checkPermissions(Player player) {
        if (player.hasPermission("essentials.kit.nitro") && !isNitroBooster(player)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kit.nitro false");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kits.nitro false");

        } else if (isNitroBooster(player) && !player.hasPermission("essentials.kit.nitro")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kits.nitro true");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kit.nitro true");
        }
        if (player.hasPermission("essentials.kit.faction") && !isFactionLeader(player)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kit.faction false");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kits.faction false");
        } else if (isFactionLeader(player) && !player.hasPermission("essentials.kit.faction")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kits.faction true");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " permission set essentials.kit.faction true");
        }

        if (player.hasPermission("essentials.kit.media") && !isYoutuber(player)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " parent set interstellar");
        } else if (isYoutuber(player) && !player.hasPermission("essentials.kit.media")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " parent add media");
        }

        if (player.hasPermission("essentials.kit.famous") && !isFamous(player)) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " parent set interstellar");
        } else if (isFamous(player) && !player.hasPermission("essentials.kit.famous")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getUniqueId() + " parent add famous");
        }
    }

    public Member getMember(User user) {
        try {
            return this.astroDiscord.getMember(user);
        } catch (Exception var3) {
            return null;
        }
    }

    public Member getMemberById(User user, Guild guild) {
        try {
            return guild.getMemberById(user.getId());
        } catch (Exception var4) {
            return null;
        }
    }



/*    @EventHandler(
            priority = EventPriority.HIGH
    )
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (isFactionLeader(player)) {
            event.setFormat(LEADER_BADGE + event.getFormat());
        }

        if (isNitroBooster(player)) {
            event.setFormat(BOOSTER_BADGE + event.getFormat());
        }

    }*/

    public static boolean isNitroBooster(Player player) {
        return nitroBoosterList.contains(player.getUniqueId());
    }

    public static boolean isYoutuber(Player player) {
        return youtuberList.contains(player.getUniqueId());
    }

    public static boolean isFamous(Player player) {
        return famousList.contains(player.getUniqueId());
    }

    public static boolean isFactionLeader(Player player) {
        return factionLeaderList.contains(player.getUniqueId());
    }

    public Set<UUID> getNitroBoosterList() {
        return nitroBoosterList;
    }
}
