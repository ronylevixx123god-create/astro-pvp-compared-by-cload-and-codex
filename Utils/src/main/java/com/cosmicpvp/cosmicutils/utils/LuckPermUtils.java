package com.cosmicpvp.cosmicutils.utils;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.data.NodeMap;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.InheritanceNode;
import net.luckperms.api.track.Track;
import net.luckperms.api.track.TrackManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LuckPermUtils {
    public static RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
    public static LuckPerms luckPerms = provider.getProvider();
    public static  UserManager lpUserManager = LuckPermsProvider.get().getUserManager();
    public static TrackManager lpTrackManager = LuckPermsProvider.get().getTrackManager();




    public static User getLpUser(Player player) {
        return lpUserManager.getUser(player.getUniqueId());
    }

    public static boolean hasPermissions(Player player, String ...permissions) {
        return hasPermissions(player, Arrays.asList(permissions));
    }

    public static boolean hasPermissions(Player player, List<String> permissions) {
        return permissions.stream().allMatch(player::hasPermission);
    }

    public static void addPermissions(Player player, long expiryTime, String ...permissions) {
        addPermissions(player, Arrays.asList(permissions), expiryTime);
    }

    public static void addPermissions(Player player, String ...permissions) {
        addPermissions(player, Arrays.asList(permissions));
    }

    public static void addPermissions(Player player, List<String> permissions) {
        User user = getLpUser(player);
        if (user == null) return;

        NodeMap userData = user.data();
        permissions.forEach(perm -> userData.add(Node.builder(perm).build()));
        lpUserManager.saveUser(user);
    }

    public static void addPermissions(Player player, List<String> permissions, long expiryTime) {
        User user = getLpUser(player);
        if (user == null) return;

        NodeMap userData = user.data();
        permissions.forEach(perm -> userData.add(Node.builder(perm)
                .expiry(Duration.ofSeconds(expiryTime))
                .build()));
        lpUserManager.saveUser(user);
    }

    public static boolean hasRanks(Player player, String ...ranks) {
        return hasRanks(player, Arrays.asList(ranks));
    }

    public static boolean hasRanks(Player player, List<String> ranks) {
        List<String> currentRanks = getRanks(player).stream().map(String::toLowerCase).collect(Collectors.toList());
        ranks = ranks.stream().map(String::toLowerCase).collect(Collectors.toList());
        return currentRanks.containsAll(ranks);
    }

    public static List<String> getRanks(Player player) {
        User user = getLpUser(player);
        if (user == null) return new ArrayList<>();

        return user.getNodes(NodeType.INHERITANCE).stream()
                .map(InheritanceNode::getGroupName)
                .collect(Collectors.toList());
    }

    public static void addRanks(Player player, long expiryTime, String ...ranks) {
        addRanks(player, Arrays.asList(ranks), expiryTime);
    }

    public static void addRanks(Player player, String ...ranks) {
        addRanks(player, Arrays.asList(ranks));
    }

    public static void addRanks(Player player, List<String> ranks) {
        User user = getLpUser(player);
        if (user == null) return;

        NodeMap userData = user.data();
        ranks.forEach(rank -> userData.add(InheritanceNode.builder(rank).build()));
        lpUserManager.saveUser(user);
    }

    public static void addRanks(Player player, List<String> ranks, long expiryTime) {
        User user = getLpUser(player);
        if (user == null) return;

        NodeMap userData = user.data();
        ranks.forEach(rank -> userData.add(InheritanceNode.builder(rank)
                .expiry(Duration.ofSeconds(expiryTime))
                .build()));
        lpUserManager.saveUser(user);
    }

    public static Group getGroup(String rankName) {
        return luckPerms.getGroupManager().getGroup(rankName);
    }

    public static Track getTrackFromGroup(Group group) {
        for (Track track : lpTrackManager.getLoadedTracks()) {
            if (track.containsGroup(group)) return track;
        }
        return null;
    }

    public static boolean hasHigherRank(Player player, String rank) {
        Group targetGroup = getGroup(rank);
        if (!targetGroup.getWeight().isPresent())  return true;

        int targetWeight = targetGroup.getWeight().getAsInt();
        Track targetTrack = getTrackFromGroup(targetGroup);

        for (String rankName : getRanks(player)) {
            Group group = getGroup(rankName);
            if (targetTrack != getTrackFromGroup(group) || !group.getWeight().isPresent()) continue;
            if (group.getWeight().getAsInt() > targetWeight) return true;
        }
        return false;
    }
}
