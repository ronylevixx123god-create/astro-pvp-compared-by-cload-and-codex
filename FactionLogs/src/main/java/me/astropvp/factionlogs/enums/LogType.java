package me.astropvp.factionlogs.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.astropvp.factionlogs.struct.impl.SpawnerPlace;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
@Getter
public enum LogType {

    SPAWNER_PLACE("Spawner Place Logs", Material.MOB_SPAWNER, "&a&l"),
    SPAWNER_BREAK("Spawner Break Logs", Material.MOB_SPAWNER, "&c&l"),
    RELATION_EDITS("Relation Edits", Material.DIAMOND_CHESTPLATE, "&d&l"),
    FACTION_RENAME("Rename Logs", Material.NAME_TAG, "&b&l"),
    PLAYER_LEAVE("Player Leaves", Material.WOOD_DOOR, "&c&l"),
    PLAYER_JOIN("Player Joins", Material.IRON_DOOR, "&a&l"),
    CLAIM("Claim Logs", Material.GRASS, "&a&l"),
    RADIUSCLAIM("Radius Claim Logs", Material.GRAVEL, "&e&l"),
    UNCLAIM("Unclaim Logs", Material.DIRT, "&c&l"),
    UNCLAIMALL("Unclaimall Logs", Material.REDSTONE_BLOCK, "&4&l"),
    CHUNKBUST("Chunkbust Logs", Material.ENDER_PORTAL_FRAME, "&3&l");



    private final String displayName;
    private final Material guiMaterial;
    private final String guiColorCode;

}
