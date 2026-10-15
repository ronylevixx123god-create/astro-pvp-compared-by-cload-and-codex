/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.compilex.CosmicKOTH.me.astropvp.cratekeys.net.astropvp.armorsets.utils.RewardUtils
 *  com.compilex.CosmicOutposts.CosmicOutpostsAPI
 *  com.cosmicpvp.cosmichcftrophies.CosmicHCFTrophies
 *  com.cosmicpvp.cosmicutils.CosmicUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.factions.points.FactionPointUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.FactionUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.GeneralUtil
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.MaskUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.StringUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.UserScoreboard
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.WorldGuardUtils
 *  com.cosmicpvp.scoreboard.CosmicScoreboardPlugin
 *  com.gmail.filoghost.holograms.api.Hologram
 *  com.google.common.collect.Lists
 *  com.massivecraft.factions.FPlayer
 *  com.massivecraft.factions.FPlayers
 *  com.massivecraft.factions.Faction
 *  com.massivecraft.factions.iface.RelationParticipator
 *  net.minecraft.server.v1_7_R4.Block
 *  net.minecraft.server.v1_7_R4.EntityHuman
 *  net.minecraft.server.v1_7_R4.EntityPlayer
 *  net.minecraft.server.v1_7_R4.ItemStack
 *  net.minecraft.server.v1_7_R4.Packet
 *  net.minecraft.server.v1_7_R4.PacketPlayOutBlockBreakAnimation
 *  net.minecraft.server.v1_7_R4.World
 *  net.minecraft.server.v1_7_R4.WorldServer
 *  org.arkhamnetwork.Arkkit.patches.EquipmentLootboxes
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.OfflinePlayer
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockFace
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Chest
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.BlockBreakEvent
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.player.PlayerTeleportEvent$TeleportCause
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.material.MaterialData
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.scoreboard.DisplaySlot
 *  org.bukkit.util.Vector
 */
package me.ifamasssxd.cosmichcf.struct;

import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.compilex.CosmicKOTH.utils.RewardUtils;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.utils.*;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import net.minecraft.server.v1_8_R3.*;
import org.arkhamnetwork.Arkkit.patches.EquipmentLootboxes;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class Outpost {
    private String prefix = "(Outpost)";
    private final double MAX_CAP = 100.0;
    private final int DEFAULT_COUNTDOWN_TIME = 300;
    private final int DEFAULT_BLOCK_DURABILITY = 15;
    private final int SCOREBOARD_SLOT = -3;
    private final Set<Block> placedBlocks = new HashSet<Block>();
    private final Map<Block, BlockDurabilityData> blockDurability = new HashMap<Block, BlockDurabilityData>();
    private String lastFactionToCap;
    private String lastPlayerToCap;
    private String lastFactionToCapTag;
    private final String internalOutpostName;
    private final String regionName;
    private final String blockRegionNames;
    private OutpostState outpostState = OutpostState.OPEN;
    private Faction defendingFaction;
    private Faction attackingFaction;
    private final String outpostName;
    private String currentFactionCappingId;
    private String nameColor;
    private double capPercentage = 0.0;
    private long lastCapturedTime;
    private long lastCapPercentageUpdatedTime;
    private long lastContestedNotifyTime;
    private long lastTrophySpawned;
    private Location teleportLocation;
    private Location trophyChestLocation;
    private Location sellRoomLocation;
    private final boolean running = false;
    private final OutpostType outpostType;
    private final Set<Player> outpostScoreboard = new HashSet<Player>();
    private final Map<UUID, Hologram> blockMiningHolograms = new HashMap<UUID, Hologram>();
    private final List<BlockFace> faces = Lists.newArrayList(BlockFace.EAST, BlockFace.WEST, BlockFace.NORTH, BlockFace.SOUTH);
    private final DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
    private final DecimalFormat distanceDecimalFormat = new DecimalFormat("#,###");
    private final List<Material> whitelistedMaterials = Lists.newArrayList(Material.STONE, Material.DIRT, Material.GRASS, Material.COBBLESTONE, Material.WOOD, Material.LOG, Material.LOG_2, Material.SANDSTONE, Material.GLASS, Material.THIN_GLASS, Material.STAINED_GLASS, Material.STAINED_CLAY, Material.HARD_CLAY, Material.CLAY_BRICK, Material.STEP, Material.WOOD_STEP, Material.DOUBLE_STEP, Material.WOOD_DOUBLE_STEP, Material.ACACIA_STAIRS, Material.COBBLESTONE_STAIRS, Material.SMOOTH_STAIRS, Material.BRICK_STAIRS, Material.WOOD_STAIRS, Material.SANDSTONE_STAIRS, Material.SPRUCE_WOOD_STAIRS, Material.BIRCH_WOOD_STAIRS, Material.QUARTZ_STAIRS, Material.BRICK, Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE, Material.PUMPKIN, Material.MELON_BLOCK, Material.MYCEL, Material.NETHER_BRICK, Material.ENDER_STONE, Material.HAY_BLOCK, Material.SMOOTH_BRICK, Material.REDSTONE_LAMP_OFF, Material.REDSTONE_LAMP_ON, Material.QUARTZ_BLOCK, Material.WOOL);

    public Outpost(String internalName, String outpostName, String regionName, String blockRegionNames, String lastFactionToCap, long lastCapturedTime) {
        this.regionName = regionName;
        this.outpostName = StringUtils.translate(outpostName);
        if (this.outpostName.startsWith("\u00a7")) {
            this.nameColor = this.outpostName.substring(0, 2);
        }
        this.blockRegionNames = blockRegionNames;
        this.internalOutpostName = internalName;
        if (lastFactionToCap != null && !lastFactionToCap.isEmpty()) {
            this.lastFactionToCap = lastFactionToCap;
            Faction faction = FactionUtils.getFactionById(this.lastFactionToCap);
            if (faction == null || !FactionUtils.isNormal(faction)) {
                this.lastFactionToCap = null;
                this.lastFactionToCapTag = null;
            } else {
                this.lastFactionToCapTag = faction.getTag();
                this.defendingFaction = faction;
                this.capPercentage = 100.0;
                this.outpostState = OutpostState.CONTROLLED;
                Bukkit.getLogger().info("Setting cap percentage to " + this.capPercentage + " due to being controlled by " + faction.getTag());
            }
        }
        this.lastCapturedTime = lastCapturedTime;
        this.outpostType = OutpostType.getByInternalName(this.internalOutpostName);
        try {
            Class.forName("me.ifamasssxd.cosmichcf.struct.BlockDurabilityData");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        this.prefix = "(" + CosmicKOTHPlugin.get_().getBaseName() + ")";
    }

    public boolean isRunning() {
        return this.running;
    }

    public void onOutpostUpdate(Set<Player> playersInside, Map<Player, Set<String>> allPlayerRegions) {
        boolean oneCapping;
        Faction highestFaction;
        Faction found;
        if (playersInside.isEmpty()) {
            this.attackingFaction = null;
            return;
        }
        double startincCapPercent = this.capPercentage;
        Map<Faction, Set<FPlayer>> cappingFPlayers = this.getCappingFPlayers(playersInside);
        this.checkDeadCapturings();
        if (!(this.lastFactionToCap == null || (found = FactionUtils.getFactionById(this.lastFactionToCap)) != null && found.getFPlayers().size() > 0 && found.isNormal())) {
            this.lastFactionToCap = null;
            this.lastFactionToCapTag = null;
            if (this.defendingFaction != null && this.attackingFaction != null && this.attackingFaction.equals(this.defendingFaction)) {
                this.attackingFaction = null;
            }
            if (this.defendingFaction != null && this.defendingFaction.equals(found)) {
                this.defendingFaction = null;
            }
            Bukkit.getLogger().info("Outpost cap resetting " + this.getOutpostName() + " due to lastFactionToCap being " + this.lastFactionToCap + ", attacker: " + (this.attackingFaction != null ? this.attackingFaction.getTag() : "None") + " Cap Percent: " + this.capPercentage);
            if (this.attackingFaction == null) {
                this.capPercentage = 0.0;
            }
        }
        if (this.attackingFaction != null && !cappingFPlayers.containsKey(this.attackingFaction)) {
            this.attackingFaction = null;
            Bukkit.getLogger().info("Removing attacking faction due to no longer being inside!");
        }
        if ((highestFaction = this.getHighestCapping(cappingFPlayers)) == null) {
            this.attackingFaction = null;
        }
        if ((this.defendingFaction == null || this.capPercentage <= 0.0) && highestFaction != null && !CosmicKOTHPlugin.get_().getOutpostManager().doesControllAnotherOutpost(highestFaction)) {
            this.defendingFaction = highestFaction;
            Bukkit.getLogger().info("(HCFOutposts) " + highestFaction.getTag() + " has started to capture the " + this.outpostName + " due to noone holding it or percentage being too low? " + this.capPercentage);
            this.capPercentage = 0.0;
        }
        if (cappingFPlayers.size() > 1) {
            this.setOutpostState(OutpostState.CONTESTED);
        } else if (this.lastFactionToCap == null && this.defendingFaction == null) {
            this.setOutpostState(OutpostState.OPEN);
        }
        if (!(highestFaction == null || this.defendingFaction != null && highestFaction.equals(this.defendingFaction))) {
            this.attackingFaction = highestFaction;
        }
        if (this.defendingFaction != null && this.attackingFaction != null) {
            if (this.defendingFaction.equals(this.attackingFaction)) {
                this.attackingFaction = null;
            } else {
                double decrement = this.getCapPercentIncreaseInterval(cappingFPlayers, this.attackingFaction, true);
                if (decrement > 0.0 && this.capPercentage - decrement <= 0.0 && cappingFPlayers.size() == 1) {
                    this.capPercentage = Math.abs(this.capPercentage - decrement);
                    this.startCapturing(this.attackingFaction);
                    if (this.defendingFaction != null) {
                        Bukkit.getLogger().info("Starting capture percentage with overlap: " + this.capPercentage + " for " + this.defendingFaction.getTag() + " decrement: " + decrement);
                    }
                } else if (cappingFPlayers.size() == 1) {
                    this.capPercentage = Math.max(0.0, this.capPercentage - decrement);
                }
            }
        }
        if (this.defendingFaction != null && this.defendingFaction.equals(highestFaction) && !this.defendingFaction.equals(this.attackingFaction)) {
            this.capPercentage = Math.min(100.0, this.capPercentage + this.getCapPercentIncreaseInterval(cappingFPlayers, highestFaction, false));
        }
        boolean bl = oneCapping = cappingFPlayers.size() == 1;
        if (oneCapping && this.capPercentage >= 100.0 && !this.outpostState.equals(OutpostState.CONTROLLED) && this.defendingFaction != null && this.defendingFaction.equals(highestFaction)) {
            if (CosmicKOTHPlugin.get_().getOutpostManager().doesControllAnotherOutpost(highestFaction) && (this.lastFactionToCap == null || !highestFaction.getId().equals(this.lastFactionToCap))) {
                this.setOutpostState(OutpostState.BLOCKED);
            } else {
                Bukkit.getLogger().info("(HCFOutposts) Faction: " + highestFaction.getTag() + " Capturing outpost " + this.getOutpostName() + " percentage: " + this.capPercentage + " status: " + this.outpostState);
                OutpostState before = this.outpostState;
                this.setOutpostState(OutpostState.CONTROLLED);
                if (this.defendingFaction == null || before != OutpostState.CONTESTED) {
                    this.onCap(highestFaction);
                }
            }
        } else if (cappingFPlayers.size() > 1) {
            this.setOutpostState(OutpostState.CONTESTED);
        } else if (oneCapping && this.defendingFaction != null && this.defendingFaction.equals(highestFaction) && this.capPercentage < 100.0) {
            this.setOutpostState(OutpostState.CAPTURING);
        } else if (oneCapping && highestFaction != null && !highestFaction.equals(this.defendingFaction) && highestFaction.equals(this.attackingFaction)) {
            this.setOutpostState(OutpostState.UNDER_ATTACK);
        }
        if (!(this.attackingFaction == null || this.defendingFaction != null || this.lastFactionToCap != null && this.lastFactionToCap.equals(this.attackingFaction.getId()) || this.canCap(this.attackingFaction))) {
            this.setOutpostState(OutpostState.BLOCKED);
            Bukkit.getLogger().info("(CosmicOutposts) Blocking " + this.attackingFaction.getTag() + " from capturing: " + this.getOutpostName());
        } else if (this.attackingFaction != null && this.defendingFaction == null && oneCapping) {
            this.startCapturing(this.attackingFaction);
            Bukkit.getLogger().info("(HCFOutposts) Outpost: " + this.getOutpostName() + " starting capture for " + this.defendingFaction.getTag() + " due to defendingFaction being null!");
        }
        if (this.outpostState == OutpostState.UNDER_ATTACK) {
            if (System.currentTimeMillis() - this.lastCapPercentageUpdatedTime >= TimeUnit.SECONDS.toMillis(30L)) {
                this.alertDefenderOfAttack();
            }
        } else if (this.outpostState == OutpostState.CAPTURING && this.capPercentage != startincCapPercent) {
            if (System.currentTimeMillis() - this.lastCapPercentageUpdatedTime >= TimeUnit.SECONDS.toMillis(30L)) {
                this.lastCapPercentageUpdatedTime = System.currentTimeMillis();
                this.sendOutpostMessage(this.getNameColor() + CC.Bold + this.prefix + " " + this.getOutpostName() + this.getNameColor() + " is being captured! " + this.getPrettyFormattedCapturePercent());
            }
        } else if (this.outpostState == OutpostState.CONTESTED && System.currentTimeMillis() - this.lastContestedNotifyTime >= TimeUnit.SECONDS.toMillis(30L)) {
            this.alertContested();
        }
    }

    public void onPlayerLeave(Player player) {
        if (this.outpostScoreboard.remove(player)) {
            //this.updateScoreboard(player, player.getUniqueId(), true);
        }
    }

    public void updateScoreboard(Set<Player> playerRegions) {
        HashSet<UUID> validInsideIDs = new HashSet<UUID>();
        for (Player inside : playerRegions) {
            validInsideIDs.add(inside.getUniqueId());
            this.outpostScoreboard.add(inside);
            //this.updateScoreboard(inside, inside.getUniqueId(), false);
        }
        ArrayList toRemove = Lists.newArrayList();
        for (Player previouslyUpdated : this.outpostScoreboard) {
            if (validInsideIDs.contains(previouslyUpdated.getUniqueId())) continue;
            toRemove.add(previouslyUpdated);
            //this.updateScoreboard(previouslyUpdated, previouslyUpdated.getUniqueId(), true);
        }
        toRemove.forEach(id -> this.outpostScoreboard.remove(id));
    }

    // TODO: Implement our scoreboard [this is the board for outpost, we will need to make a return method like events in arenapvp]
    /*public void updateScoreboard(Player player, UUID uuid, boolean remove) {
        UserScoreboard scoreboard = (UserScoreboard)CosmicScoreboardPlugin.getPlugin().getPlaceholderManager().getStoredScoreboards().get(uuid);
        if (scoreboard != null) {
            if (remove) {
                if (scoreboard.hasLine(-3)) {
                    scoreboard.removeLine(-3);
                }
                if (scoreboard.hasLine(-4)) {
                    scoreboard.removeLine(-4);
                }
                player.removeMetadata("boss_scoreboard", CosmicKOTHPlugin.get_());
            } else {
                scoreboard.addLongLine(-3, this.getOutpostName() + " " + this.getPrettyFormattedCapturePercent(), -3);
                scoreboard.addLongLine(-4, CC.Gray + " " + this.getOutpostState().getName() + CC.Green + (this.getOutpostState() == OutpostState.CONTROLLED && this.defendingFaction != null ? " " + CC.Gray + "(" + CC.Green + this.defendingFaction.getTag() + CC.Gray + ")" : ""), -4);
                player.setMetadata("boss_scoreboard", new FixedMetadataValue(CosmicKOTHPlugin.get_(), System.currentTimeMillis() + 30000L));
                scoreboard.setSlot(DisplaySlot.SIDEBAR);
            }
        }
    }*/

    public void teleportToMain(Player player) {
        player.setMetadata("ignore_combat_tag", new FixedMetadataValue(CosmicKOTHPlugin.get_(), ""));
        player.teleport(this.getTeleportLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.removeMetadata("ignore_combat_tag", CosmicKOTHPlugin.get_());
    }

    public String getPrettyFormattedCapturePercent() {
        return CC.Gray + "[" + this.decimalFormat.format(this.capPercentage) + "%]";
    }

    public void setOutpostState(OutpostState newState) {
        if (this.outpostState != OutpostState.UNDER_ATTACK && newState == OutpostState.UNDER_ATTACK) {
            this.alertDefenderOfAttack();
        }
        this.outpostState = newState;
    }

    public void alertDefenderOfAttack() {
        this.lastCapPercentageUpdatedTime = System.currentTimeMillis();
        this.sendOutpostMessage(CC.RedB + this.prefix + " " + CC.Red + this.getOutpostName() + this.getNameColor() + " is under attack! " + this.getPrettyFormattedCapturePercent());
    }

    public void alertContested() {
        this.lastContestedNotifyTime = System.currentTimeMillis();
        this.sendOutpostMessage(this.getNameColor() + this.prefix + " " + CC.Red + this.getOutpostName() + this.getNameColor() + " is being contested! " + this.getPrettyFormattedCapturePercent());
    }

    public void sendOutpostMessage(String message) {
        if (this.getTeleportLocation() == null) {
            if (this.defendingFaction != null) {
                this.defendingFaction.sendMessage(message);
            }
            return;
        }
        this.getTeleportLocation().getWorld().getPlayers().stream().filter(pl -> this.outpostScoreboard.contains(pl) || pl.getLocation().distanceSquared(this.getTeleportLocation()) <= 10000.0).forEach(p -> p.sendMessage(message));
    }

    public void onCap(Faction capped) {
        boolean shouldAlert;
        boolean bl = shouldAlert = capped != null && (this.lastFactionToCap == null || !this.lastFactionToCap.equals(capped.getId()));
        if (capped != null) {
            this.lastFactionToCap = capped.getId();
            this.lastFactionToCapTag = capped.getTag();
        } else {
            this.lastFactionToCap = null;
            this.lastFactionToCapTag = null;
        }
        this.defendingFaction = capped;
        this.setLastCapturedTime(System.currentTimeMillis());
        if (this.defendingFaction != null) {
            this.defendingFaction.getFPlayers().forEach(pl -> {
                if (pl.getPlayer() != null) {
                    pl.getPlayer().playSound(pl.getPlayer().getLocation(), Sound.ANVIL_LAND, 0.3f, 1.1f);
                }
            });
        }
        if (this.getTeleportLocation() != null && this.defendingFaction != null && shouldAlert) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                FPlayer fplayer = FPlayers.getInstance().getByPlayer(p);
                if (fplayer == null) continue;
                p.sendMessage("");
                p.sendMessage(this.getNameColor() + CC.Bold + this.prefix + " " + this.defendingFaction.getRelationTo(fplayer).getColor() + this.defendingFaction.getTag() + this.getNameColor() + " has taken control of " + this.getOutpostName() + this.getNameColor() + "!");
                p.sendMessage(ChatColor.GRAY + "Use " + this.getNameColor() + ChatColor.BOLD + "/strongholds" + ChatColor.GRAY + " to view more information.");
                p.sendMessage("");
            }
        }
        this.restoreBrokenBlocks();
        this.restorePlacedBlocks();
        for (Player pl2 : PlayerUtil.getOnlinePlayers()) {
            Set regions = WorldGuardUtils.getRegionsIn(pl2.getLocation());
            FPlayer fplayer = FactionUtils.getFPlayer(pl2);
            if (fplayer == null || this.defendingFaction.getFPlayers().contains(fplayer) || !this.contains("sell_room", regions) || !this.contains(this.getInternalOutpostName(), regions)) continue;
            pl2.teleport(this.getTeleportLocation());
            pl2.sendMessage(CC.RedB + "(!) " + CC.Red + "You are no longer allowed inside the Sell Room!");
            Bukkit.getLogger().info("Returning " + pl2.getName() + " to ");
        }
    }

    public boolean contains(String str, Set<String> lists) {
        str = str.toLowerCase();
        for (String string : lists) {
            if (!string.contains(str)) continue;
            return true;
        }
        return false;
    }

    public void startCapturing(Faction capturing) {
        if (this.defendingFaction != null && !this.defendingFaction.equals(capturing)) {
            for (FPlayer fplayer : FactionUtils.getFPlayers(false)) {
                Player pl;
                if (!fplayer.isOnline() || (pl = fplayer.getPlayer()) == null) continue;
                pl.sendMessage(this.getNameColor() + CC.Bold + this.prefix + " " + this.defendingFaction.getRelationTo(fplayer).getColor() + this.defendingFaction.getTag() + this.getNameColor() + " is no longer controlling " + this.getOutpostName() + this.getNameColor() + "!");
            }
            this.lastFactionToCap = null;
            this.lastFactionToCapTag = null;
            Bukkit.getLogger().info("Last faction to cap setting null!");
        }
        if (CosmicKOTHPlugin.get_().getOutpostManager().doesControllAnotherOutpost(capturing)) {
            if (capturing.equals(this.attackingFaction)) {
                this.attackingFaction = null;
                this.capPercentage = 0.0;
                this.outpostState = OutpostState.BLOCKED;
            }
            this.defendingFaction = null;
            return;
        }
        this.defendingFaction = capturing;
        this.outpostState = OutpostState.CAPTURING;
        this.sendOutpostMessage(this.getNameColor() + CC.Bold + this.prefix + " " + this.getNameColor() + this.getOutpostName() + this.getNameColor() + " is now being captured!");
    }

    private boolean canCap(Faction faction) {
        return !CosmicKOTHPlugin.get_().getOutpostManager().doesControllAnotherOutpost(faction);
    }

    private double getCapPercentIncreaseInterval(Map<Faction, Set<FPlayer>> factionsOnCap, Faction strongest, boolean decrease) {
        if (factionsOnCap.size() > 1) {
            return 0.0;
        }
        Set<FPlayer> cappingMembers = factionsOnCap.get(strongest);
        if (cappingMembers == null) {
            Bukkit.getLogger().info("No capping members found yet strongest is " + strongest.getTag());
            return 0.0;
        }
        if (cappingMembers.stream().filter(fp -> fp.getPlayer() != null && fp.getPlayer().isSneaking() && FactionUtils.getPlayerName(fp).equalsIgnoreCase("iFamasssxD")).findFirst().orElse(null) != null) {
            return 5.0;
        }
        double retr = Math.min(0.5f, 0.11f * (float)cappingMembers.size());
        retr = decrease ? (this.capPercentage - retr < 0.0 ? this.capPercentage : retr) : Math.min(retr, 100.0 - this.capPercentage);
        double mult = 1.0;
        boolean heroOutpost = false;
        boolean strongholdMask = false;
        if (CosmicUtils.get().isOutpostsEnabled()) {
            for (FPlayer capping : cappingMembers) {
                Player pl = FactionUtils.getPlayer(capping);

                if (pl == null || !MaskUtils.hasMaskEquipped(pl, "STRONGHOLD")) continue;
                strongholdMask = true;
            }
        }
        mult = heroOutpost && strongholdMask ? 4.0 : (heroOutpost || strongholdMask ? 2.0 : 1.0);
        return retr * mult;
    }

    public void onBlockBreak(BlockBreakEvent event) {
        Block broken = event.getBlock();
        Player player = event.getPlayer();
        Set<String> regionsInside = WorldGuardUtils.getRegionsIn(broken.getLocation());
        if (regionsInside.contains(this.blockRegionNames) && !regionsInside.contains(this.regionName) && !regionsInside.contains("infernalnobuild") && !regionsInside.contains("frozennobuild")) {
            event.setCancelled(true);
            BlockDurabilityData durability = this.blockDurability.computeIfAbsent(broken, dura -> new BlockDurabilityData(new MaterialData(broken.getType(), broken.getData()), (short) 15));
            broken.getWorld().playEffect(broken.getLocation().add(0.5, 0.5, 0.5), Effect.STEP_SOUND, (Object)broken.getType());
            if (durability.getDurability() == 1) {
                broken.breakNaturally(new ItemStack(Material.AIR));
                this.blockMiningHolograms.remove(player.getUniqueId());
            } else {
                if (durability.getDurability() > 0) {
                    durability.setDurability((short)(durability.getDurability() - 1));
                }
                String text = CC.GreenB + durability.getDurability() + " / " + 15;
                Vector direction = player.getLocation().getDirection();
                if (direction.length() != 0.0) {
                    direction.normalize();
                }
                direction.setY(direction.getY() / 2.0);
                direction.divide(new Vector(2, 2, 2));
                double y = direction.getY() > 0.5 ? 0.5 : (direction.getY() < -0.5 ? -0.5 : (direction.getY() != 0.0 && Math.abs(direction.getY()) < 0.1 ? 0.25 : direction.getY()));
                Location desiredHolo = broken.getLocation().add(direction.getX() + 0.35, 1.0 + y, direction.getZ() + 0.35);
                int maxBreaks = 10;
                double percentLeft = (double)durability.getDurability() * 1.0 / 15.0;
                int breaks = (int)Math.min(maxBreaks, Math.floor((double)maxBreaks - percentLeft * (double)maxBreaks));
                player.sendMessage(CC.Aqua + "Block Durability: " + CC.AquaB + durability.getDurability());
                PacketPlayOutBlockBreakAnimation animation = new PacketPlayOutBlockBreakAnimation(0, new BlockPosition(broken.getX(), broken.getY(), broken.getZ()), breaks);
                List<Entity> pls = player.getNearbyEntities(16.0, 16.0, 16.0).stream().filter(ent -> ent instanceof Player).collect(Collectors.toList());
                pls.add(player);
                pls.forEach(ent -> ((CraftPlayer)ent).getHandle().playerConnection.sendPacket(animation));
            }
        }
    }

    public void spawnTrophyChest(int trophyCount) {
        Chest chest;
        if (this.trophyChestLocation == null) {
            return;
        }
        this.lastTrophySpawned = System.currentTimeMillis();
        Block block = this.trophyChestLocation.getBlock();
        BlockState state = block.getState();
        if (state instanceof Chest) {
            chest = (Chest)state;
            for (org.bukkit.inventory.ItemStack i : chest.getInventory()) {
                if (i == null || i.getType() == Material.AIR) continue;
                return;
            }
        } else {
            block.setType(Material.CHEST);
            chest = (Chest)block.getState();
        }
        if (chest != null) {
            List possibleKothLoot;
            org.bukkit.inventory.ItemStack points;
            block.setMetadata("strongholdChest", new FixedMetadataValue(CosmicKOTHPlugin.get_(), true));

            if (CosmicKOTHPlugin.get_().isVanillaPlanet()) {
                for (int i = 0; i < 4; ++i) {
                    double random = Math.random();
                    int tier = random <= 0.8 ? 1 : (random <= 0.95 ? 2 : 3);
                    chest.getInventory().addItem(RewardUtils.getRandomVanillaKOTHItem(tier));
                }
            } else if (CosmicUtils.get().isCosmicGalaxy() && (possibleKothLoot = RewardUtils.getKOTHLoot("Stronghold")) != null) {
                Collections.shuffle(possibleKothLoot);
                org.bukkit.inventory.ItemStack item = (org.bukkit.inventory.ItemStack)possibleKothLoot.get(0);
                if (item != null) {
                    if (GeneralUtil.isArmor(item) && ArkPlugins.EQUIPMENT_LOOTBOX.getArkPlugin().isRunning()) {
                        Bukkit.getLogger().info("[CosmicHCFOutposts] Replacing " + item + " with Equipment Lootbox!");
                    }
                    chest.getInventory().addItem(item);
                }
            }
            chest.update();
            for (BlockFace face : this.faces) {
                Block bl = block.getRelative(face);
                if (bl == null || bl.getType() != Material.CHEST) continue;
                bl.setMetadata("strongholdChest", new FixedMetadataValue(CosmicKOTHPlugin.get_(), true));
            }
        }
        Bukkit.getLogger().info("Spawning " + trophyCount + " trophies in trophy chest at " + this.getOutpostName() + "!");
    }

    public void onBlockPlace(final BlockPlaceEvent event) {
        final Block placed = event.getBlockPlaced();
        final Set<String> regionsInside = (Set<String>)WorldGuardUtils.getRegionsIn(placed.getLocation());
        if (regionsInside != null && regionsInside.contains(this.blockRegionNames) && !regionsInside.contains(this.regionName)) {
            final Faction in = FactionUtils.getFaction(event.getPlayer());
            if (in == null || !in.isNormal() || this.defendingFaction == null || !in.getTag().equals(this.defendingFaction.getTag())) {
                event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "Your faction must control the " + CosmicKOTHPlugin.get_().getBaseName() + " in order to place blocks here!");
                event.setCancelled(true);
                return;
            }
            final Material itemInHand = (event.getItemInHand() != null) ? event.getItemInHand().getType() : null;
            if (itemInHand == Material.OBSIDIAN) {
                event.setCancelled(true);
                event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot place Obsidian in the " + CosmicKOTHPlugin.get_().getBaseName() + " area!");
                return;
            }
            if (event.getItemInHand() != null && !this.whitelistedMaterials.contains(itemInHand)) {
                event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot place that block inside the " + CosmicKOTHPlugin.get_().getBaseName() + " area!");
                event.setCancelled(true);
                return;
            }
            event.setCancelled(true);
            event.getPlayer().setMetadata("allowedBlockPlace", new FixedMetadataValue(CosmicKOTHPlugin.get_(), System.currentTimeMillis()));
        }
    }

    public void onBlockPlaceMONITOR(final BlockPlaceEvent event) {
        if (event.getPlayer().hasMetadata("allowedBlockPlace")) {
            event.setCancelled(false);
            event.getPlayer().removeMetadata("allowedBlockPlace", CosmicKOTHPlugin.get_());
            this.blockDurability.remove(event.getBlockPlaced());
            this.placedBlocks.add(event.getBlockPlaced());
        }
    }

    public void restorePlacedBlocks() {
        this.placedBlocks.stream().filter(block -> !this.blockDurability.containsKey(block)).forEach(bl -> bl.setTypeIdAndData(0, (byte)0, false));
        this.placedBlocks.clear();
    }

    public void restoreBrokenBlocks() {
        this.blockDurability.forEach((block, durabilityData) -> {
            if (this.placedBlocks.contains(block)) {
                return;
            }
            block.setTypeIdAndData(durabilityData.getBlockType().getItemTypeId(), durabilityData.getBlockType().getData(), true);
        });
        this.blockDurability.clear();
        this.blockMiningHolograms.forEach((id, holo) -> holo.delete());
        this.blockMiningHolograms.clear();
    }

    private void breakBlockNaturally(Player player, Block bukkitBlock) {
        EntityPlayer entityPlayer = ((CraftPlayer)player).getHandle();
        WorldServer server = ((CraftWorld)bukkitBlock.getWorld()).getHandle();
        net.minecraft.server.v1_8_R3.Block block =  server.getType(new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ())).getBlock();
        server.a((EntityHuman)entityPlayer, new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ()));
        net.minecraft.server.v1_8_R3.ItemStack itemstack = entityPlayer.bA();
        boolean flag = d(server, entityPlayer, bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ());
        if (itemstack != null) {
            itemstack.a(server, (net.minecraft.server.v1_8_R3.Block) block, new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ()), (EntityHuman)entityPlayer);
            if (itemstack.count == 0)
                entityPlayer.ca();
        }
        if (flag)
            block.a(server, new BlockPosition(bukkitBlock.getX(), bukkitBlock.getY(), bukkitBlock.getZ()), block.getBlockData(), (EntityHuman)entityPlayer);
    }

    public boolean isInsideSellRoom(Player player, Set<String> regions) {
        return this.contains("sell_room", regions) && (this.contains(this.getInternalOutpostName(), regions) || this.contains(this.getRegionName(), regions));
    }

    public boolean equals(Object obj) {
        if (obj instanceof Outpost) {
            Outpost then = (Outpost)obj;
            if (this.regionName == null && then.regionName == null) {
                return false;
            }
            if (this.internalOutpostName.equals(then.internalOutpostName) && then.regionName.equals(this.regionName)) {
                return true;
            }
        }
        return super.equals(obj);
    }

    private void checkDeadCapturings() {
        Faction attackingFound;
        if (this.attackingFaction != null && ((attackingFound = FactionUtils.getFactionById(this.attackingFaction.getId())) == null || attackingFound.getFPlayers().size() <= 0)) {
            this.attackingFaction = null;
        }
        if (this.defendingFaction != null && ((attackingFound = FactionUtils.getFactionById(this.defendingFaction.getId())) == null || attackingFound.getFPlayers().size() <= 0)) {
            Bukkit.getLogger().info("Removing capturing faction " + this.defendingFaction.getTag() + " due to no longer being valid.");
            this.defendingFaction = null;
        }
    }

    private Faction getHighestCapping(Map<Faction, Set<FPlayer>> map) {
        int highestCount = -1;
        Faction faction = null;
        for (Map.Entry<Faction, Set<FPlayer>> entry : map.entrySet()) {
            if (entry.getValue().size() <= highestCount && faction != null) continue;
            faction = entry.getKey();
            highestCount = entry.getValue().size();
        }
        return faction != null && faction.isNone() ? null : faction;
    }

    public Map<Faction, Set<FPlayer>> getCappingFPlayers(Set<Player> players) {
        HashMap<Faction, Set<FPlayer>> retr = new HashMap<Faction, Set<FPlayer>>();
        for (Player pl : players) {
            Faction fac;
            FPlayer fplayer = FactionUtils.getFPlayer(pl);
            if (fplayer == null || (fac = FactionUtils.getFaction(fplayer)) == null) continue;
            retr.computeIfAbsent(fac, se -> new HashSet()).add(fplayer);
        }
        return retr;
    }

    private boolean d(WorldServer world, EntityPlayer player, int i, int j, int k) {
        net.minecraft.server.v1_8_R3.Block block = world.getType(new BlockPosition(i, j, k)).getBlock();
        block.a((net.minecraft.server.v1_8_R3.World) world, new BlockPosition(i, j, k), block.getBlockData(), (EntityHuman)player);
        boolean flag = world.setAir(new BlockPosition(i, j, k));
        if (flag)
            block.postBreak(world, new BlockPosition(i, j, k), block.getBlockData());
        return flag;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public double getMAX_CAP() {
        this.getClass();
        return 100.0;
    }

    public int getDEFAULT_COUNTDOWN_TIME() {
        this.getClass();
        return 300;
    }

    public int getDEFAULT_BLOCK_DURABILITY() {
        this.getClass();
        return 15;
    }

    public int getSCOREBOARD_SLOT() {
        this.getClass();
        return -3;
    }

    public Set<Block> getPlacedBlocks() {
        return this.placedBlocks;
    }

    public Map<Block, BlockDurabilityData> getBlockDurability() {
        return this.blockDurability;
    }

    public String getLastFactionToCap() {
        return this.lastFactionToCap;
    }

    public String getLastPlayerToCap() {
        return this.lastPlayerToCap;
    }

    public String getLastFactionToCapTag() {
        return this.lastFactionToCapTag;
    }

    public String getInternalOutpostName() {
        return this.internalOutpostName;
    }

    public String getRegionName() {
        return this.regionName;
    }

    public String getBlockRegionNames() {
        return this.blockRegionNames;
    }

    public OutpostState getOutpostState() {
        return this.outpostState;
    }

    public String getOutpostName() {
        return this.outpostName;
    }

    public String getCurrentFactionCappingId() {
        return this.currentFactionCappingId;
    }

    public String getNameColor() {
        return this.nameColor;
    }

    public long getLastCapturedTime() {
        return this.lastCapturedTime;
    }

    public long getLastCapPercentageUpdatedTime() {
        return this.lastCapPercentageUpdatedTime;
    }

    public long getLastContestedNotifyTime() {
        return this.lastContestedNotifyTime;
    }

    public long getLastTrophySpawned() {
        return this.lastTrophySpawned;
    }

    public Set<Player> getOutpostScoreboard() {
        return this.outpostScoreboard;
    }

    public Map<UUID, Hologram> getBlockMiningHolograms() {
        return this.blockMiningHolograms;
    }

    public List<BlockFace> getFaces() {
        return this.faces;
    }

    public List<Material> getWhitelistedMaterials() {
        return this.whitelistedMaterials;
    }

    public void setDefendingFaction(Faction defendingFaction) {
        this.defendingFaction = defendingFaction;
    }

    public void setAttackingFaction(Faction attackingFaction) {
        this.attackingFaction = attackingFaction;
    }

    public Faction getDefendingFaction() {
        return this.defendingFaction;
    }

    public Faction getAttackingFaction() {
        return this.attackingFaction;
    }

    public void setCapPercentage(double capPercentage) {
        this.capPercentage = capPercentage;
    }

    public double getCapPercentage() {
        return this.capPercentage;
    }

    public void setLastCapturedTime(long lastCapturedTime) {
        this.lastCapturedTime = lastCapturedTime;
    }

    public void setLastCapPercentageUpdatedTime(long lastCapPercentageUpdatedTime) {
        this.lastCapPercentageUpdatedTime = lastCapPercentageUpdatedTime;
    }

    public void setLastContestedNotifyTime(long lastContestedNotifyTime) {
        this.lastContestedNotifyTime = lastContestedNotifyTime;
    }

    public void setLastTrophySpawned(long lastTrophySpawned) {
        this.lastTrophySpawned = lastTrophySpawned;
    }

    public void setTeleportLocation(Location teleportLocation) {
        this.teleportLocation = teleportLocation;
    }

    public void setTrophyChestLocation(Location trophyChestLocation) {
        this.trophyChestLocation = trophyChestLocation;
    }

    public void setSellRoomLocation(Location sellRoomLocation) {
        this.sellRoomLocation = sellRoomLocation;
    }

    public Location getTeleportLocation() {
        return this.teleportLocation;
    }

    public Location getTrophyChestLocation() {
        return this.trophyChestLocation;
    }

    public Location getSellRoomLocation() {
        return this.sellRoomLocation;
    }

    public OutpostType getOutpostType() {
        return this.outpostType;
    }

    public DecimalFormat getDecimalFormat() {
        return this.decimalFormat;
    }

    public DecimalFormat getDistanceDecimalFormat() {
        return this.distanceDecimalFormat;
    }
}

