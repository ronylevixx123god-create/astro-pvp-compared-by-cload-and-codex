package me.astropvp.factionlogs.struct.impl;

import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import com.massivecraft.factions.perms.Relation;
import lombok.Getter;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.utils.TimeDateUtils;

@Getter
public class RelationEdits extends FactionLog {
    private final String sender;
    private final String target;
    private final String oldRelation;
    private final String  newRelation;

    public RelationEdits(Faction sender, Faction target, Relation oldRelation, Relation newRelation) {
        super(LogType.RELATION_EDITS);
        this.sender = sender.getTag();
        this.target = target.getTag();
        this.oldRelation = oldRelation.nicename;
        this.newRelation = newRelation.nicename;
    }

    public RelationEdits(Faction sender, Faction target, Relation oldRelation, Relation newRelation, long time) {
        super(LogType.RELATION_EDITS);
        this.sender = sender.getTag();
        this.target = target.getTag();
        this.oldRelation = oldRelation.nicename;
        this.newRelation = newRelation.nicename;
        super.setTime(time);
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + getSender() + " changed relation: " + getOldRelation() + " -> " + getNewRelation() + ".";

    }
}
