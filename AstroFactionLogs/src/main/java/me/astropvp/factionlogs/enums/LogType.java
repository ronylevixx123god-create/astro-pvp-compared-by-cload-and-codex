package me.astropvp.factionlogs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.struct.impl.*;
import org.bukkit.Material;

@AllArgsConstructor
@Getter
public enum LogType {

    SPAWNER_PLACE("Spawner Place Logs", Material.MOB_SPAWNER, "&a&l", SpawnerPlace.class),
    SPAWNER_BREAK("Spawner Break Logs", Material.MOB_SPAWNER, "&c&l", SpawnerBreak.class),
    RELATION_EDITS("Relation Edits", Material.DIAMOND_CHESTPLATE, "&d&l", RelationEdits.class),
    FACTION_RENAME("Rename Logs", Material.NAME_TAG, "&b&l", FactionRename.class),
    PLAYER_LEAVE("Player Leaves", Material.WOOD_DOOR, "&c&l", PlayerLeave.class),
    PLAYER_JOIN("Player Joins", Material.IRON_DOOR, "&a&l", PlayerJoin.class),
    CLAIM("Claim Logs", Material.GRASS, "&a&l", LandClaim.class),
    RADIUSCLAIM("Radius Claim Logs", Material.GRAVEL, "&e&l", RadiusLandClaim.class),
    UNCLAIM("Unclaim Logs", Material.DIRT, "&c&l", LandUnclaim.class),
    UNCLAIMALL("Unclaimall Logs", Material.REDSTONE_BLOCK, "&4&l", LandUnclaimAll.class),
    CHUNKBUST("Chunkbust Logs", Material.ENDER_PORTAL_FRAME, "&3&l", ChunkBust.class);



    private final String displayName;
    private final Material guiMaterial;
    private final String guiColorCode;
    private final Class<? extends FactionLog> logClass;

}
