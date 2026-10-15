package me.astropvp.factionlogs.gui;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.massivecraft.factions.Faction;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.astropvp.factionlogs.FactionLogs;
import me.astropvp.factionlogs.enums.LogType;
import me.astropvp.factionlogs.struct.FactionLog;
import me.astropvp.factionlogs.struct.impl.*;
import me.astropvp.factionlogs.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;
import java.util.stream.Collectors;

public class FactionLogsGUI {

    private static final File file;
    private static final FileConfiguration outputFile;

    static {
        File filePath = new File(FactionLogs.getInstance().getDataFolder().getPath() + "errors.yml");

        if (!filePath.isFile()) {
            try {
                filePath.createNewFile();
            } catch (IOException exc) {
                exc.printStackTrace();
            }
        }

        file = filePath;
        outputFile = YamlConfiguration.loadConfiguration(filePath);
    }

    @Getter
    private final PaginatedGui fLogGui;

    public FactionLogsGUI(Player player, Faction faction) {
        this.fLogGui = new PaginatedGui(3, CC.translate(CC.DarkGray + faction.getTag() + " Faction Logs"));


        for (LogType logType : LogType.values()) {
            ItemBuilder item = new ItemBuilder(logType.getGuiMaterial(), CC.translate(logType.getGuiColorCode() + logType.getDisplayName()))
                    .addLoreLine(CC.translate("&7Below you can find the 10 latest logs:"))
                    .addLoreLine("")
                    .addLoreLine(CC.translate("&f&l" + logType.getDisplayName() + " &7(" + FactionsLogsAPI.getFactionLogs(faction, logType).size() + " total)"));
            String prefix = logType.getGuiColorCode() + " * &e";

            FactionsLogsAPI.getFactionLogs(faction, logType).sort(Collections.reverseOrder());
            for (FactionLog log : FactionsLogsAPI.getFactionLogs(faction, logType).stream().limit(10).collect(Collectors.toList())) {
                try {


                    switch (logType) {
                        case CLAIM:
                            LandClaim landClaim = (LandClaim) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(landClaim.getUuid())).getName() + " &7claimed land at &d" + LocationUtils.locToString(landClaim.getFLocation()) + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case RADIUSCLAIM:
                            RadiusLandClaim radiusLandClaim = (RadiusLandClaim) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(radiusLandClaim.getUuid())).getName() + " &7claimed land in a &d" + radiusLandClaim.getRadius() + " &7radius at &d" + LocationUtils.locToString(radiusLandClaim.getFLocation()) + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case UNCLAIM:
                            LandUnclaim landUnClaim = (LandUnclaim) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(landUnClaim.getUuid())).getName() + " &7unclaimed land at &d" + LocationUtils.locToString(landUnClaim.getFLocation()) + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case UNCLAIMALL:
                            LandUnclaimAll landUnclaimAll = (LandUnclaimAll) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(landUnclaimAll.getUuid())).getName() + " &7unclaimed ALL land. " + "&8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case PLAYER_JOIN:
                            PlayerJoin playerJoin = (PlayerJoin) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(playerJoin.getUuid())).getName() + " &7join the faction due to &e" + playerJoin.getPlayerJoinReason().name().toLowerCase() + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case PLAYER_LEAVE:
                            PlayerLeave playerLeave = (PlayerLeave) log;
                            try {
                                item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(playerLeave.getUuid())).getName() + " &7left the faction due to &e" + playerLeave.getPlayerLeaveReason().name().toLowerCase() + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            } catch (Exception exc) {
                                System.out.println("Player null: " + Bukkit.getOfflinePlayer(UUID.fromString(playerLeave.getUuid())).getName());

                                ConfigurationSection currentSection = outputFile.createSection(Long.toString(System.currentTimeMillis()));

                                currentSection.set("p-null", Bukkit.getOfflinePlayer(UUID.fromString(playerLeave.getUuid())) == null);
                                currentSection.set("reason", playerLeave.getPlayerLeaveReason() == null);

                                try {
                                    outputFile.save(file);
                                } catch (Exception ioException) {
                                    ioException.printStackTrace();
                                }
                            }
                            break;
                        case CHUNKBUST:
                            ChunkBust chunkBust = (ChunkBust) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(chunkBust.getUuid())).getName() + " &7placed a chunkbuster at &d" + LocationUtils.locToString(chunkBust.getLocation()) + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case SPAWNER_BREAK:
                            SpawnerBreak spawnerBreak = (SpawnerBreak) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(spawnerBreak.getUuid())).getName() + " &7broke &ex" + spawnerBreak.getAmount() + " " + EntityUtils.getEntityName(EntityType.valueOf(spawnerBreak.getEntityType())) + "&7 at &d" + LocationUtils.locToString(spawnerBreak.getLocation()) + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case SPAWNER_PLACE:
                            SpawnerPlace spawnerPlace = (SpawnerPlace) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(spawnerPlace.getUuid())).getName() + " &7placed &ex" + spawnerPlace.getAmount() + " " + EntityUtils.getEntityName(EntityType.valueOf(spawnerPlace.getEntityType())) + "&7 at &d" + LocationUtils.locToString(spawnerPlace.getLocation()) + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case RELATION_EDITS:
                            RelationEdits relationEdits = (RelationEdits) log;
                            item.addLoreLine(CC.translate(prefix + relationEdits.getSender() + " &7changed relation: &d" + relationEdits.getOldRelation() + " &7-> &d" + relationEdits.getNewRelation() + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                        case FACTION_RENAME:
                            FactionRename factionRename = (FactionRename) log;
                            item.addLoreLine(CC.translate(prefix + Bukkit.getOfflinePlayer(UUID.fromString(factionRename.getPlayer())).getName() + " &7renamed the faction tag &d" + factionRename.getOldTag() + " &7-> &d" + factionRename.getNewTag() + "&7. &8(&7" + TimeDateUtils.splitToComponentTimes((System.currentTimeMillis() - log.getTime()) / 1000) + "ago&8)"));
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            item.addLoreLine("");
            item.addLoreLine(CC.translate("&7&nClick&7 to view all the " + FactionsLogsAPI.getFactionLogs(faction, logType).size() + " " + logType.getDisplayName()));
            this.fLogGui.addItem(new GuiItem(item.build(), event -> {
                Bukkit.getScheduler().runTaskAsynchronously(FactionLogs.getInstance(), () -> {
                    if (FactionsLogsAPI.getFactionLogs(faction, logType).isEmpty()) {
                        player.sendMessage(CC.translate("&6&lFactions &8⎜ &cError&8: &eThere are no " + logType.getDisplayName() + " available!"));
                        return;
                    }
                    HastebinUtils.sendLog(player, faction.getId(), logType, false);
                });
            }));

        }

        this.fLogGui.setDefaultClickAction(event -> event.setCancelled(true));

    }

}
