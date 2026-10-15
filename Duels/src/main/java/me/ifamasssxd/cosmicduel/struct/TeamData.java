package me.ifamasssxd.cosmicduel.struct;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;

public class TeamData {
    private UUID leader;

    private List<UUID> teamates;

    private List<UUID> invites;

    public TeamData(UUID leader) {
        this.leader = leader;
        this.teamates = Lists.newArrayList();
        this.invites = Lists.newArrayList();
    }

    public TeamData addToTeam(UUID uuid) {
        this.teamates.add(uuid);
        return this;
    }

    public TeamData removeFromTeam(UUID uuid) {
        this.teamates.remove(uuid);
        return this;
    }

    public TeamData addToInvites(UUID uuid) {
        this.invites.add(uuid);
        return this;
    }

    public TeamData removeFromInvites(UUID uuid) {
        this.invites.remove(uuid);
        return this;
    }

    public boolean isLeader(UUID uuid) {
        return getLeaderUUID().toString().equalsIgnoreCase(uuid.toString());
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public UUID getLeaderUUID() {
        return this.leader;
    }

    public List<UUID> getTeamates() {
        return this.teamates;
    }

    public List<UUID> getInvites() {
        return this.invites;
    }

    public int getTotalSize() {
        return getTeamates().size() + 1;
    }
}
