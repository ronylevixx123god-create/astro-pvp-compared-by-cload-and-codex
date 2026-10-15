package me.ifamasssxd.cosmicduel.struct;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.enums.CancelReason;
import me.ifamasssxd.cosmicduel.utils.JSONMessage;
import me.ifamasssxd.cosmicduel.utils.TimeUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class TeamDataManager {
    private final Map<UUID, TeamData> teams;

    private final int inviteExpireTime;

    private final ConfigurationWrapper config;

    public TeamDataManager() {
        this.teams = Maps.newHashMap();
        this.inviteExpireTime = 60;
        this.config = new ConfigurationWrapper("teams.yml", (JavaPlugin)CosmicDuel.get());
        initialize();
    }



    private void initialize() {
        if (this.config.getYamlConf().getConfigurationSection("teams") != null) {
            List<String> leaders = Lists.newArrayList();
            for (String keys : this.config.getYamlConf().getConfigurationSection("teams").getKeys(false))
                leaders.add(keys);
            for (String leader : leaders) {
                if (this.config.getYamlConf().getConfigurationSection("teams").contains(leader)) {
                    List<String> teamates = this.config.getStringList("teams." + leader);
                    TeamData team = new TeamData(UUID.fromString(leader));
                    if (teamates != null)
                        for (String teamate : teamates) {
                            String teamateStringFixed = teamate.replaceAll("\\[", "").replace("]", "");
                            team.addToTeam(UUID.fromString(teamateStringFixed));
                        }
                    addTeam(team);
                }
            }
        }
    }

    public void terminate() {
        this.config.setString("teams", null);
        for (Iterator<UUID> iterator = getTeams().keySet().iterator(); iterator.hasNext(); ) {
            UUID uuid = iterator.next();
            List<UUID> teamates = ((TeamData)this.teams.get(uuid)).getTeamates();
            List<String> teamatesStringList = Lists.newArrayList();
            teamates.stream().forEach(teamate -> teamatesStringList.add(teamates.toString()));
            String getLeaderUUID = uuid.toString();
            this.config.setStringList("teams." + getLeaderUUID, teamatesStringList);
        }
    }

    public void addTeam(TeamData data) {
        this.teams.put(data.getLeaderUUID(), data);
    }

    public TeamData getTeamInvite(Player player) {
        for (TeamData data : this.teams.values()) {
            if (data.getInvites().contains(player.getUniqueId()))
                return data;
        }
        return null;
    }

    public boolean alreadyHasTeamInvite(Player toCheck) {
        for (TeamData team : this.teams.values()) {
            if (team.getInvites().contains(toCheck.getUniqueId()))
                return true;
        }
        return false;
    }

    public boolean hitTeamInivteLimit(TeamData data) {
        return (data.getTeamates().size() >= 5);
    }

    public boolean hasInviteToTeam(Player player, TeamData data) {
        return data.getInvites().contains(player.getUniqueId());
    }

    public void sendTeamInvite(final TeamData data, final Player receiver) {
        String name = Bukkit.getPlayer(data.getLeaderUUID()).getName();
        receiver.sendMessage("");
        JSONMessage invite = new JSONMessage("");
        receiver.sendMessage(CC.translate("&a&l(!) &aYou have received a team invite from &f" + name + "&a!"));
        invite.addRunCommand(CC.translate("&7Click this to &a&LACCEPT&7 the invite or type &f/team join " + name + "&7!"), ChatColor.YELLOW, "/team join " + name, ChatColor.GREEN + "/team join " + name);
        invite.sendToPlayer(receiver);
        receiver.sendMessage(CC.translate("&7This invite will expire in &f" + TimeUtils.formatSeconds(this.inviteExpireTime) + "&7."));
        data.addToInvites(receiver.getUniqueId());
        Bukkit.getScheduler().scheduleAsyncDelayedTask((Plugin)CosmicDuel.get(), (Runnable)new BukkitRunnable() {
            public void run() {
                if (data == null)
                    return;
                TeamDataManager.this.removeInvite(data, receiver);
            }
        },(20L * this.inviteExpireTime));
    }

    public void removeInvite(TeamData data, Player player) {
        if (hasInviteToTeam(player, data)) {
            data.removeFromInvites(player.getUniqueId());
            player.sendMessage(CC.translate("&c&l(!) &cYour invite from &f" + Bukkit.getPlayer(data.getLeaderUUID()).getName() + "'s &cteam has been removed!"));
        }
    }

    public void showTeamMessage(Player player, TeamData data) {
        player.sendMessage("");
        player.sendMessage(CC.translate("&6&l" + Bukkit.getOfflinePlayer(data.getLeaderUUID()).getName() + "'s Team"));
        player.sendMessage(CC.translate(""));
        player.sendMessage(CC.translate("&6&lMembers"));
        if (data.getTeamates() == null) {
            player.sendMessage(CC.translate("&cNo team members"));
            player.sendMessage("");
            return;
        }
        data.getTeamates().stream().forEach(teamate -> player.sendMessage(CC.translate(" &6&l* &f" + Bukkit.getOfflinePlayer(teamate).getName())));
        player.sendMessage("");
    }

    public Map<UUID, TeamData> getTeams() {
        return this.teams;
    }

    public void removeTeam(TeamData data) {
        try {
            for (UUID uuid : data.getTeamates()) {
                Player player = Bukkit.getPlayer(uuid);
                if (player.getOpenInventory() != null) {
                    player.sendMessage(CC.Red + "Closing inventory.");

                    player.closeInventory();
                }
            }
            Bukkit.getPlayer(data.getLeaderUUID()).closeInventory();
        } catch (Exception e) {
            e.printStackTrace();
            this.teams.remove(data.getLeaderUUID());

        }
    }

    public boolean isLeaderOfTeam(UUID uuid) {
        return this.teams.containsKey(uuid);
    }

    public TeamData tryFindTeam(UUID uuid) {
        if (isLeaderOfTeam(uuid))
            return this.teams.get(uuid);
        for (TeamData data : this.teams.values()) {
            if (data.getTeamates() != null && data.getTeamates().contains(uuid))
                return data;
        }
        return null;
    }
}
