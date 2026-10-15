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
    private final Faction sender;
    private final Faction target;
    private final Relation oldRelation;
    private final Relation newRelation;

    public RelationEdits(Faction sender, Faction target, Relation oldRelation, Relation newRelation) {
        super(LogType.RELATION_EDITS);
        this.sender = sender;
        this.target = target;
        this.oldRelation = oldRelation;
        this.newRelation = newRelation;
    }

    public RelationEdits(Faction sender, Faction target, Relation oldRelation, Relation newRelation, long time) {
        super(LogType.RELATION_EDITS);
        this.sender = sender;
        this.target = target;
        this.oldRelation = oldRelation;
        this.newRelation = newRelation;
        super.setTime(time);
    }

    @Override
    public String serialize() {
        return (this.getTime() + ";-;" + sender.getId() + ";-;" + target.getId() + ";-;" + oldRelation.name() + ";-;" + newRelation.name());
    }

    public static FactionLog deserialize(String string) {
        String[] splitString = string.split(";-;");
        Faction sender = Factions.getInstance().getFactionById(splitString[1]);
        if (sender == null) {
            sender = Factions.getInstance().getNone();
        }
        Faction target = Factions.getInstance().getFactionById(splitString[2]);
        if (target == null) {
            target = Factions.getInstance().getNone();
        }
        Relation oldRel = Relation.fromString(splitString[3]);
        Relation newRel = Relation.fromString(splitString[4]);
        try {


            return new RelationEdits(sender, target, oldRel, newRel, Long.parseLong(splitString[0]));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "(" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - getTime()) / 1000) + "ago) " + getSender().getTag() + " changed relation: " + getOldRelation().nicename + " -> " + getNewRelation().nicename + ".";

    }
}
