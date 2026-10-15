/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.boydti.fawe.FaweAPI
 *  com.boydti.fawe.object.visitor.FastIterator
 *  com.boydti.fawe.util.EditSessionBuilder
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.RandomFireworks
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TitleManager$TitleColor
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.WorldGuardUtils
 *  com.decimatemc.decimatelootboxes.api.LootboxManager
 *  com.decimatemc.decimatelootboxes.lootbox.DecimateRewardItem
 *  com.earth2me.essentials.User
 *  com.gmail.filoghost.holograms.api.HolographicDisplaysAPI
 *  com.gmail.nossr50.events.experience.McMMOPlayerXpGainEvent
 *  com.gmail.nossr50.events.skills.abilities.McMMOPlayerAbilityActivateEvent
 *  com.gmail.nossr50.events.skills.unarmed.McMMOPlayerDisarmEvent
 *  com.google.common.collect.Lists
 *  com.sk89q.worldedit.EditSession
 *  com.sk89q.worldedit.Vector
 *  com.sk89q.worldedit.regions.CuboidRegion
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.struct.Boss
 *  me.ifamasssxd.cosmicbosses.net.astropvp.armorsets.struct.enums.BossType
 *  net.ess3.api.MaxMoneyException
 *  org.arkhamnetwork.Arkkit.libs.JSONMessage
 *  org.arkhamnetwork.enchantments.libs.PlayerPotionEffects
 *  org.arkhamnetwork.enchantments.util.Title
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Chunk
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.block.Chest
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandExecutor
 *  org.bukkit.command.CommandSender
 *  org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity
 *  org.bukkit.enchantments.Enchantment
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.entity.Skeleton
 *  org.bukkit.entity.Skeleton$SkeletonType
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.ProjectileHitEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.event.player.PlayerMoveEvent
 *  org.bukkit.event.player.PlayerQuitEvent
 *  org.bukkit.event.player.PrePlayerDeathEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 *  org.bukkit.material.Button
 *  org.bukkit.material.Lever
 *  org.bukkit.material.MaterialData
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.potion.PotionEffect
 *  org.bukkit.potion.PotionEffectType
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package com.compilex.CosmicDungeons.dungeons.events;

import com.boydti.fawe.FaweAPI;
import com.boydti.fawe.object.visitor.FastIterator;
import com.boydti.fawe.util.EditSessionBuilder;
import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.DungeonsRewardsAPI;
import com.compilex.CosmicDungeons.dungeons.events.DungeonEvents;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.BossRoomRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.ColorsRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.EntityCustomFireball;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.FindCorruptRoomRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.FireballAlleyRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.FirstParkourRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.GlassBreakRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.InvisibleMazeRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.MathRoomRegionEvent;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.PanelColor;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.PlanetNulFireballTask;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.PlanetNulInvisibleMazeTask;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.PlanetNulMazeTask;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.PlanetNulParkourRestrictionTask;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.SecondParkourRegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonPlayer;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonState;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.compilex.CosmicDungeons.dungeons.libs.PlanetNulDungeon;
import com.compilex.CosmicDungeons.events.DungeonStateChangeEvent;
import com.compilex.CosmicDungeons.struct.DungeonChallenge;
import com.compilex.CosmicDungeons.utils.ChatUtils;
import com.compilex.CosmicDungeons.utils.TimeUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.*;
import com.earth2me.essentials.User;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;
import com.google.common.collect.Lists;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.regions.CuboidRegion;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.CustomLoot;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.LootTableAPI;
import net.astropvp.loottable.struct.LootTable;
import net.ess3.api.MaxMoneyException;
import org.arkhamnetwork.Arkkit.libs.JSONMessage;
import org.arkhamnetwork.enchantments.libs.PlayerPotionEffects;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Button;
import org.bukkit.material.Lever;
import org.bukkit.material.MaterialData;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.github.paperspigot.Title;

public class PlanetNulEvents
extends DungeonEvents
implements CommandExecutor {
    private PlanetNulDungeon dungeon;
    public List<Location> chestLocations;
    public List<Location> announcedChestLocations;
    private List<Location> possibleChestLocations;
    private List<Location> lockedDoorLocations;
    private List<Location> lockedPressurePlateLocations;
    private List<Location> enderAssassinLocations;
    public List<PanelColor> leftPanelColors;
    public List<PanelColor> middlePanelColors;
    public List<PanelColor> rightPanelColors;
    private Map<Location, List<Location>> mazeLevers;
    private Map<Location, Long> mazeLeversCooldown;
    private Map<Location, Boolean> extraChests;
    private boolean airlockKeyUnlocked = false;
    public int randomParkourChosen;

    public PlanetNulEvents(Dungeon d) {
        super(d);
        CosmicDungeonsPlugin.get().getCommand("answer").setExecutor((CommandExecutor)this);
        this.dungeon = (PlanetNulDungeon)d;
        this.chestLocations = new ArrayList<>();
        this.announcedChestLocations = new ArrayList<>();
        this.possibleChestLocations = new ArrayList<>();
        this.enderAssassinLocations = new ArrayList<>();
        this.lockedDoorLocations = new ArrayList<>();
        this.lockedPressurePlateLocations = new ArrayList<>();
        this.loadTasks();
        this.leftPanelColors = this.dungeon.getRandomColorsForPanel();
        this.middlePanelColors = this.dungeon.getRandomColorsForPanel();
        this.rightPanelColors = this.dungeon.getRandomColorsForPanel();
        this.randomParkourChosen = 0;
        this.lockedPressurePlateLocations.add(new Location(this.dungeon.getWorld(), 36.0, 202.0, -20.0));
        this.lockedPressurePlateLocations.add(new Location(this.dungeon.getWorld(), 37.0, 202.0, -20.0));
        this.lockedPressurePlateLocations.add(new Location(this.dungeon.getWorld(), 38.0, 202.0, -20.0));
        this.lockedDoorLocations.add(new Location(this.dungeon.getWorld(), 36.0, 203.0, -21.0));
        this.lockedDoorLocations.add(new Location(this.dungeon.getWorld(), 36.0, 202.0, -21.0));
        this.lockedDoorLocations.add(new Location(this.dungeon.getWorld(), 37.0, 203.0, -21.0));
        this.lockedDoorLocations.add(new Location(this.dungeon.getWorld(), 37.0, 202.0, -21.0));
        this.lockedDoorLocations.add(new Location(this.dungeon.getWorld(), 38.0, 203.0, -21.0));
        this.lockedDoorLocations.add(new Location(this.dungeon.getWorld(), 38.0, 202.0, -21.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 58.0, 223.0, 56.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 32.0, 223.0, 57.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 22.0, 223.0, 57.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 55.0, 223.0, 53.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 46.0, 223.0, 58.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 62.0, 223.0, 49.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 63.0, 223.0, 31.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 64.0, 223.0, 36.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 40.0, 223.0, 9.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 44.0, 223.0, 17.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 56.0, 223.0, -7.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 24.0, 223.0, 4.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 63.0, 223.0, -18.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 57.0, 223.0, -27.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 62.0, 223.0, 6.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 66.0, 223.0, -22.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 63.0, 223.0, -24.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 62.0, 223.0, -39.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 57.0, 223.0, -57.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 23.0, 223.0, -2.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 42.0, 223.0, -37.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 24.0, 223.0, -32.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 12.0, 223.0, -27.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -5.0, 223.0, -26.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 8.0, 223.0, -51.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -25.0, 223.0, -53.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -24.0, 223.0, -47.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -22.0, 223.0, -32.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -51.0, 223.0, -54.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -44.0, 223.0, -41.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -54.0, 223.0, -26.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -15.0, 223.0, 7.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -24.0, 223.0, 10.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -14.0, 223.0, 12.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -51.0, 223.0, 6.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -45.0, 223.0, 57.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -37.0, 223.0, 53.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -16.0, 223.0, 24.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -17.0, 223.0, 29.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -12.0, 223.0, 23.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 18.0, 223.0, 36.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), 4.0, 223.0, 57.0));
        this.possibleChestLocations.add(new Location(this.dungeon.getWorld(), -3.0, 223.0, 55.0));
        this.extraChests = new LinkedHashMap<Location, Boolean>(){
            {
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 37.0, 198.0, -12.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 1.0, 217.0, -83.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 29.0, 203.0, -9.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 15.0, 157.0, -33.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 870.0, 213.0, 301.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 811.0, 198.0, 455.0), false);
            }
        };
        this.mazeLevers = new HashMap<>();
        this.mazeLeversCooldown = new HashMap<>();
        this.loadRegionTriggerEvents();
    }

    private ItemStack getColorHintNote(String panel, int colorNum, int hintNum) {
        List<PanelColor> panelColors = panel.equalsIgnoreCase("left") ? this.leftPanelColors : (panel.equalsIgnoreCase("right") ? this.rightPanelColors : this.middlePanelColors);
        PanelColor color = panelColors.get(colorNum - 1);
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.translate((String)("&f&lAirlock Hint #" + hintNum + "/7")));
        meta.setLore(Lists.newArrayList(CC.translate((String)"&7Use the following hint to crack"), CC.translate((String)"&7the Vault Airlock combination:"), CC.translate((String)"&f&l* ") + (Object)color.getChatColor() + panel + "-" + colorNum + "-" + color.getName()));
        item.setItemMeta(meta);
        return new NBTWrapper(item).setString("cosmicType", "dungeonColorHint").build();
    }

    private ItemStack getAirlockKey() {
        return new NBTWrapper(new ItemBuilder(Material.TRIPWIRE_HOOK, CC.AquaB + "Airlock Key", CC.translate((String)"&7Use this key to unlock the"), CC.translate((String)"&7doors to the Vault Airlock.")).build()).setString("cosmicType", "dungeonAirlockKey").build();
    }

    private boolean isAirlockKey(ItemStack item) {
        return item != null && item.getType() == Material.TRIPWIRE_HOOK && item.hasItemMeta() && item.getItemMeta().hasDisplayName() && item.getItemMeta().hasLore() && item.getItemMeta().getDisplayName().equals(CC.AquaB + "Airlock Key");
    }

    private void fillMazeChests() {
        int i;
        ArrayList<Location> chestLocationsCopy = new ArrayList<Location>(this.possibleChestLocations);
        Collections.shuffle(chestLocationsCopy);
        int leftColorNum = 0;
        int middleColorNum = 0;
        int rightColorNum = 0;
        ArrayList<String> randomPanels = Lists.newArrayList("LEFT", "LEFT", "MIDDLE", "MIDDLE", "RIGHT", "RIGHT");
        Collections.shuffle(randomPanels);
        ArrayList<String> randomAdditionPanels = Lists.newArrayList("LEFT", "MIDDLE", "RIGHT");
        Collections.shuffle(randomAdditionPanels);
        randomPanels.add(randomAdditionPanels.get(0));
        for (i = 0; i < this.extraChests.entrySet().size(); ++i) {
            Map.Entry<Location, Boolean> entry = new ArrayList<>(this.extraChests.entrySet()).get(i);
            if(!(entry.getKey().getBlock().getState() instanceof Chest)) {
                BlockState state = entry.getKey().getBlock().getState();
                state.setType(Material.CHEST);
                state.update(true);
            }
            if (i == 0) {
                ((Chest)entry.getKey().getBlock().getState()).getInventory().addItem(this.getClockOfReturning());
                continue;
            }
            if (!(Math.random() < 0.7))
                continue;
            ((Chest)entry.getKey().getBlock().getState()).getInventory().addItem(this.getTotemOfUndying());
        }
        for (i = 0; i < 8; ++i) {
            Location chestLocation = chestLocationsCopy.get(i);
            Block block = chestLocation.getBlock();
            BlockState state = block.getState();
            state.setType(Material.CHEST);
            state.update(true);
            this.chestLocations.add(chestLocation);
            if (i == 7) {
                ((Chest)chestLocation.getBlock().getState()).getInventory().addItem(this.getAirlockKey());
                continue;
            }
            String panelStr = (String)randomPanels.get(i);
            int colorNum = 0;
            if (panelStr.equals("LEFT")) {
                colorNum = ++leftColorNum;
            } else if (panelStr.equals("MIDDLE")) {
                colorNum = ++middleColorNum;
            } else if (panelStr.equals("RIGHT")) {
                colorNum = ++rightColorNum;
            }
            ((Chest)chestLocation.getBlock().getState()).getInventory().addItem(this.getColorHintNote(panelStr, colorNum, i + 1));
        }
    }

    private Skeleton spawnEnderAssassin(Location location) {
        Skeleton skeleton = (Skeleton)location.getWorld().spawnEntity(location, EntityType.SKELETON);
        skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, new Random().nextInt(1) + 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        skeleton.getEquipment().setItemInHand(sword);
        skeleton.setMaxHealth(100.0);
        skeleton.setHealth(skeleton.getMaxHealth());
        skeleton.setCustomName(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + "Ender Assassin");
        skeleton.setCustomNameVisible(true);
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, Integer.MAX_VALUE, 1));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, new Random().nextInt(3)));
        skeleton.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, new Random().nextInt(2)));
        skeleton.getEquipment().setHelmet(new ItemStack(Material.DIAMOND_HELMET));
        skeleton.getEquipment().setChestplate(new ItemStack(Material.DIAMOND_CHESTPLATE));
        skeleton.getEquipment().setLeggings(new ItemStack(Material.DIAMOND_LEGGINGS));
        skeleton.getEquipment().setBoots(new ItemStack(Material.DIAMOND_BOOTS));
        skeleton.setCanPickupItems(false);
        skeleton.getEquipment().setItemInHandDropChance(0.0f);
        skeleton.getEquipment().setBootsDropChance(0.0f);
        skeleton.getEquipment().setLeggingsDropChance(0.0f);
        skeleton.getEquipment().setChestplateDropChance(0.0f);
        skeleton.getEquipment().setHelmetDropChance(0.0f);
        skeleton.setMetadata("ender_assassin", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)true));
        skeleton.setMetadata("do_not_clear", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)true));
        return skeleton;
    }



    private boolean activateRegion(String regionName) {
        if (this.exploredRegions.contains(regionName)) {
            return false;
        }
        this.regionEvents.get(regionName).run();
        this.exploredRegions.add(regionName);
        return true;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata("ender_assassin") || event.getEntity().hasMetadata("invisibleMazeZombie")) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        if (this.dungeon.getPlayers().contains((Object)event.getPlayer())) {
            DungeonsAPI.get().getPlayerDungeonData(event.getPlayer()).leaveDungeon(true);
        }
    }

    public List<Chunk> getNoUnloadChunks() {
        ArrayList<Chunk> chunks = new ArrayList<Chunk>();
        EditSession editSession = new EditSessionBuilder(FaweAPI.getWorld((String)this.dungeon.world.getName())).build();
        for (Vector pt : new FastIterator((Iterable)new CuboidRegion(new Vector(121, 245, -108), new Vector(-110, 242, 109)), editSession)) {
            Chunk chunk = this.dungeon.world.getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ()).getChunk();
            if (chunks.contains((Object)chunk)) continue;
            chunks.add(chunk);
        }
        return chunks;
    }

    private void pullMazeLever(Location leverLocation) {
        List<Location> leaves = this.mazeLevers.get((Object)leverLocation);
        Location first = leaves.get(0).clone();
        first.setY(223.0);
        Material setTo = first.getBlock().getType() == Material.AIR ? Material.LEAVES : Material.AIR;
        leaves.forEach(loc -> {
            for (int i = 0; i < 17; ++i) {
                Location clone = loc.clone();
                clone.setY((double)(223 + i));
                clone.getBlock().setType(setTo);
            }
        });
        Location soundLocation = leaves.get(0).clone();
        soundLocation.setY(224.0);
        this.dungeon.getPlayers().forEach(p -> p.playSound(soundLocation, Sound.PISTON_RETRACT, 1.0f, 1.6f));
    }

    private void setMazeLeverLocations() {
        this.mazeLevers.clear();
        ArrayList<ArrayList> leverOpenings = new ArrayList<ArrayList>(Arrays.asList(new ArrayList<Location>(Arrays.asList(new Location[]{new Location(this.dungeon.world, -10.0, 0.0, 1.0), new Location(this.dungeon.world, -10.0, 0.0, 2.0), new Location(this.dungeon.world, -10.0, 0.0, 3.0), new Location(this.dungeon.world, -11.0, 0.0, 1.0), new Location(this.dungeon.world, -11.0, 0.0, 2.0), new Location(this.dungeon.world, -11.0, 0.0, 3.0), new Location(this.dungeon.world, 0.0, 0.0, -25.0), new Location(this.dungeon.world, 0.0, 0.0, -24.0), new Location(this.dungeon.world, 1.0, 0.0, -24.0), new Location(this.dungeon.world, 1.0, 0.0, -25.0), new Location(this.dungeon.world, 1.0, 0.0, -23.0), new Location(this.dungeon.world, 2.0, 0.0, -23.0), new Location(this.dungeon.world, 2.0, 0.0, -24.0), new Location(this.dungeon.world, 3.0, 0.0, -24.0), new Location(this.dungeon.world, 3.0, 0.0, -25.0)})), new ArrayList<Location>(Arrays.asList(new Location[]{new Location(this.dungeon.world, -27.0, 0.0, -13.0), new Location(this.dungeon.world, -27.0, 0.0, -12.0), new Location(this.dungeon.world, -27.0, 0.0, -11.0), new Location(this.dungeon.world, -26.0, 0.0, -11.0), new Location(this.dungeon.world, -26.0, 0.0, -12.0), new Location(this.dungeon.world, -25.0, 0.0, -12.0), new Location(this.dungeon.world, -25.0, 0.0, -11.0), new Location(this.dungeon.world, -25.0, 0.0, -10.0), new Location(this.dungeon.world, -24.0, 0.0, -10.0), new Location(this.dungeon.world, -24.0, 0.0, -11.0), new Location(this.dungeon.world, -42.0, 0.0, -19.0), new Location(this.dungeon.world, -43.0, 0.0, -19.0), new Location(this.dungeon.world, -43.0, 0.0, -20.0), new Location(this.dungeon.world, -42.0, 0.0, -20.0), new Location(this.dungeon.world, -44.0, 0.0, -20.0), new Location(this.dungeon.world, -45.0, 0.0, -20.0), new Location(this.dungeon.world, -43.0, 0.0, -21.0), new Location(this.dungeon.world, -44.0, 0.0, -21.0), new Location(this.dungeon.world, -45.0, 0.0, -21.0), new Location(this.dungeon.world, -45.0, 0.0, -22.0), new Location(this.dungeon.world, -44.0, 0.0, -22.0)})), new ArrayList<Location>(Arrays.asList(new Location[]{new Location(this.dungeon.world, 0.0, 0.0, -28.0), new Location(this.dungeon.world, 1.0, 0.0, -28.0), new Location(this.dungeon.world, -1.0, 0.0, -28.0), new Location(this.dungeon.world, -1.0, 0.0, -29.0), new Location(this.dungeon.world, -1.0, 0.0, -27.0), new Location(this.dungeon.world, 3.0, 0.0, -32.0), new Location(this.dungeon.world, 3.0, 0.0, -31.0), new Location(this.dungeon.world, 3.0, 0.0, -33.0), new Location(this.dungeon.world, 4.0, 0.0, -32.0), new Location(this.dungeon.world, 2.0, 0.0, -32.0), new Location(this.dungeon.world, 22.0, 0.0, -14.0), new Location(this.dungeon.world, 21.0, 0.0, -14.0), new Location(this.dungeon.world, 21.0, 0.0, -13.0), new Location(this.dungeon.world, 21.0, 0.0, -15.0), new Location(this.dungeon.world, 20.0, 0.0, -15.0), new Location(this.dungeon.world, 20.0, 0.0, -14.0), new Location(this.dungeon.world, 19.0, 0.0, -14.0), new Location(this.dungeon.world, 19.0, 0.0, -15.0), new Location(this.dungeon.world, 19.0, 0.0, -16.0)}))));
        Collections.shuffle(leverOpenings);
        this.mazeLevers.put(new Location(this.dungeon.world, 65.0, 224.0, 45.0), (List<Location>)leverOpenings.get(0));
        this.mazeLevers.put(new Location(this.dungeon.world, 44.0, 224.0, -40.0), (List<Location>)leverOpenings.get(1));
        this.mazeLevers.put(new Location(this.dungeon.world, 25.0, 224.0, 10.0), (List<Location>)leverOpenings.get(2));
    }

    private void fillChest(Block block, int lootToFill, Set<UUID> eligible) {
        Chest chest = (Chest)block.getState();
        for (int i = 0; i < lootToFill; ++i) {
            ItemStack item = LootTableAPI.getRandomLootItem(LootTable.PLANET_NUL_DUNGEON);
            if (item == null) continue;
            chest.getInventory().addItem(item);
        }
        chest.update();
        block.setMetadata("dungeonChallengeLootChest", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), eligible));
    }

    @EventHandler
    public void onDungeonstatusChange(DungeonStateChangeEvent event) {
        if (!event.getDungeon().equals(this.dungeon)) {
            return;
        }
        if (event.getOldState() == DungeonState.IN_PROGRESS && event.getNewState() == DungeonState.COMPLETE) {
            double minutesCompletedIn = (double)this.dungeon.getTimeSinceDungeonStart() / 1000.0 / 60.0;
            if (this.extraChests.values().stream().allMatch(v -> v)) {
                this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.DUNGEON_WHISPERER);
            }
            StringBuilder playerList = new StringBuilder();
            final HashSet<UUID> validLooters = new HashSet<UUID>();
            List<Player> winners = this.dungeon.getPlayers();
            int total = 0;
            for (Player p2 : winners) {
                DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p2);
                if (dp == null || dp.getCurrentDungeon() == null || !dp.getCurrentDungeon().equals(this.dungeon)) continue;
                ++total;
                Bukkit.getPluginManager().callEvent((Event)new CustomCosmicPlayerEvent(p2, "DUNGEON_COMPLETE", new Object[]{this.dungeon.dungeonType.name()}));
                if (!dp.getCooldowns().canPlayerDoDungeon(this.dungeon)) {
                    p2.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You cannot receive rewards from the " + this.dungeon.getDungeonName() + (Object)ChatColor.RED + " dungeon for " + TimeUtils.formatDifference(dp.getCooldowns().getCooldown(this.dungeon)) + ".");
                    continue;
                }
                if (!WorldGuardUtils.getRegionName((Location)p2.getLocation()).equals("math_room")) {
                    p2.sendMessage((Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "(!) " + (Object)ChatColor.RED + "You were not standing in the math room of the dungeon, so you do not get a reward!");
                    continue;
                }
                validLooters.add(p2.getUniqueId());
                double moneyReward = random.nextInt(10000000) + 25000000;
                p2.sendMessage((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "+1 Dungeon Loot Bag");
                p2.sendMessage(CC.translate((String)"&a&l+ $" + NumberUtils.formatMoney(moneyReward) +  " (&7Planet Nul dungeon complete!&a&l)"));
                User eUser = CosmicDungeonsPlugin.get().ess3.getUser(p2);
                try {
                    eUser.setMoney(eUser.getMoney().add(new BigDecimal(moneyReward)));
                }
                catch (MaxMoneyException e) {
                    e.printStackTrace();
                }

                LootFilterAPI.giveItem(p2.getPlayer(), LootTable.PLANET_NUL_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.PLANET_NUL_DUNGEON));
                LootFilterAPI.giveItem(p2.getPlayer(), LootTable.PLANET_NUL_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.PLANET_NUL_DUNGEON));
                LootFilterAPI.giveItem(p2.getPlayer(), LootTable.PLANET_NUL_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.PLANET_NUL_DUNGEON));
                LootFilterAPI.giveItem(p2.getPlayer(), LootTable.PLANET_NUL_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.PLANET_NUL_DUNGEON));
                LootFilterAPI.giveItem(p2.getPlayer(), LootTable.PLANET_NUL_DUNGEON, LootTableAPI.getRandomLootItem(LootTable.PLANET_NUL_DUNGEON));

                playerList.append(p2.getName()).append(", ");
                long bonusCD = dp.getCooldowns().addDungeonCooldown(this.dungeon, 300000L);
                dp.logDungeonComplete(this.dungeon);
            }
            if (playerList.toString().endsWith(", ")) {
                playerList = new StringBuilder(playerList.substring(0, playerList.length() - 2));
            }
            this.dungeon.broadcastTitle("DUNGEON COMPLETE!", "+ Dungeon Loot", ChatColor.GREEN, ChatColor.GRAY);
            Bukkit.broadcastMessage((String)"");
            Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "" + (Object)ChatColor.BOLD + "*-*-* DUNGEON COMPLETE! *-*-*"));
            Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + "The " + this.dungeon.getDungeonName() + (Object)ChatColor.GOLD + " has been cleared by: " + (Object)ChatColor.GRAY + playerList + (Object)ChatColor.GOLD + " in " + TimeUtils.formatDifference(this.dungeon.getTimeSinceDungeonStart())));
            if (this.dungeon.isHeroic()) {
                Bukkit.broadcastMessage((String)ChatUtils.getCenteredMessage(CC.translate((String)"&7with the following Heroic Dungeon debuffs:")));
                Bukkit.broadcastMessage((String)ChatUtils.getCenteredMessage(CC.translate((String)"&6&l* &7-50% Objective Time")));
                Bukkit.broadcastMessage((String)ChatUtils.getCenteredMessage(CC.translate((String)"&6&l* &7Bosses take 75% less incoming damage")));
                Bukkit.broadcastMessage((String)ChatUtils.getCenteredMessage(CC.translate((String)"&6&l* &7Bosses deal 2.5x outgoing damage")));
                this.dungeon.getRandomDungeonDebuffs().forEach(debuff -> Bukkit.broadcastMessage((String)ChatUtils.getCenteredMessage(CC.translate((String)("&6&l* &7" + debuff.getName())))));
                this.dungeon.getTopHeroicDungeonTime().checkLatestTime((int)(this.dungeon.getTimeSinceDungeonStart() / 1000L), this.dungeon, winners, validLooters);
            } else {
                this.dungeon.getTopDungeonTime().checkLatestTime((int)(this.dungeon.getTimeSinceDungeonStart() / 1000L), this.dungeon, winners, validLooters);
            }
            final ArrayList<Location> chestLocations = new ArrayList<Location>(Arrays.asList(new Location(this.dungeon.world, 726.0, 148.0, 474.0), new Location(this.dungeon.world, 721.0, 148.0, 474.0), new Location(this.dungeon.world, 720.0, 148.0, 470.0), new Location(this.dungeon.world, 721.0, 148.0, 466.0), new Location(this.dungeon.world, 726.0, 148.0, 466.0)));
            Bukkit.broadcastMessage((String)"");
            if (this.dungeon.getDungeonChallengesCompleted().size() > 0) {
                Bukkit.broadcastMessage((String)((Object)ChatColor.GOLD + ChatColor.BOLD.toString() + "(!) " + (Object)ChatColor.GOLD + "Their team has completed " + (Object)ChatColor.GOLD + (Object)ChatColor.BOLD + ChatColor.UNDERLINE.toString() + this.dungeon.getDungeonChallengesCompleted().size() + (Object)ChatColor.GOLD + " Dungeon Challenges:"));
                this.dungeon.getDungeonChallengesCompleted().forEach(challenge -> Bukkit.broadcastMessage((String)(" " + (Object)ChatColor.GOLD + (Object)ChatColor.BOLD + "* " + (Object)ChatColor.GREEN + challenge.getName())));
                Bukkit.broadcastMessage((String)((Object)ChatColor.GRAY + "Loot the challenge chests for bonus dungeon items!"));
                Bukkit.broadcastMessage((String)"");
                this.dungeon.getPlayers().forEach(pl -> pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 8.0f, 0.6f));
                final AtomicInteger index = new AtomicInteger(0);
                final double percentageEligible = (double)validLooters.size() / (double)total;
                final boolean smallTeam = validLooters.size() <= 4;
                new BukkitRunnable(){

                    public void run() {
                        if (PlanetNulEvents.this.dungeon.getDungeonChallengesCompleted().isEmpty() || index.get() >= chestLocations.size()) {
                            this.cancel();
                            return;
                        }
                        DungeonChallenge nextToAnnounce = PlanetNulEvents.this.dungeon.getDungeonChallengesCompleted().stream().findFirst().orElse(null);
                        if (nextToAnnounce == null) {
                            this.cancel();
                            return;
                        }
                        int itemsPerChest = smallTeam ? ThreadLocalRandom.current().nextInt(2) + 1 : (percentageEligible <= 0.25 ? 1 : (percentageEligible <= 0.75 ? 2 : 3));
                        PlanetNulEvents.this.dungeon.getDungeonChallengesCompleted().remove((Object)nextToAnnounce);
                        Location nextToSet = (Location)chestLocations.get(index.getAndIncrement());
                        Block block = nextToSet.getBlock();
                        block.setType(Material.CHEST);
                        PlanetNulEvents.this.fillChest(block, itemsPerChest, validLooters);
                        block.getWorld().playEffect(nextToSet.clone().add(0.5, 0.5, 0.5), Effect.STEP_SOUND, Material.CHEST.getId());
                        PlanetNulEvents.this.dungeon.broadcastTitle(nextToAnnounce.getName(), "", ChatColor.GREEN, ChatColor.GOLD);
                        RandomFireworks.getManager().launchRandomFirework(nextToSet.add(0.5, 1.0, 0.5));
                    }
                }.runTaskTimer((Plugin)CosmicDungeonsPlugin.get_(), 20L, 20L);
            }
        } else if (event.getOldState() == DungeonState.LOADING && event.getNewState() == DungeonState.IN_PROGRESS) {
            this.dungeon.setObjectiveHologram(null);
            this.activateRegion("maze_start_area");
            this.dungeon.broadcastMessage((Object)ChatColor.WHITE + "The dungeon portal has closed, it is time to begin!");
            if (this.dungeon.isHeroic()) {
                this.dungeon.getPlayers().forEach(p -> {
                    ChatUtils.sendCenteredMessage(p, this.dungeon.getDungeonName());
                    ChatUtils.sendCenteredMessage(p, CC.translate((String)"&f&l* &7-50% Objective Time"));
                    ChatUtils.sendCenteredMessage(p, CC.translate((String)"&f&l* &7Bosses take 75% less incoming damage"));
                    ChatUtils.sendCenteredMessage(p, CC.translate((String)"&f&l* &7Bosses deal 2.5x outgoing damage"));
                    this.dungeon.getRandomDungeonDebuffs().forEach(debuff -> ChatUtils.sendCenteredMessage(p, CC.translate((String)("&f&l* &7" + debuff.getName()))));
                });
            }
            EditSession editSession = new EditSessionBuilder(FaweAPI.getWorld((String)this.dungeon.world.getName())).build();
            ArrayList<Location> possibleSpawnLocations = new ArrayList<Location>();
            for (Vector pt : new FastIterator((Iterable)new CuboidRegion(new Vector(92, 223, -81), new Vector(-91, 223, 66)), editSession)) {
                Block block = this.dungeon.world.getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
                if (block.getType() != Material.AIR || block.getLocation().add(0.0, -1.0, 0.0).getBlock().getType() != Material.DIRT) continue;
                possibleSpawnLocations.add(block.getLocation().add(0.0, 1.0, 0.0));
            }
            this.enderAssassinLocations.clear();
            Collections.shuffle(possibleSpawnLocations);
            for (int i = 0; i < 80; ++i) {
                if (possibleSpawnLocations.size() - 1 < i) continue;
                this.enderAssassinLocations.add((Location)possibleSpawnLocations.get(i));
            }
            this.setMazeLeverLocations();
            Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get(), () -> this.pullMazeLever(new Location(this.dungeon.world, 44.0, 224.0, -40.0)), 5L);
            this.enderAssassinLocations.forEach(this::spawnEnderAssassin);
            this.fillMazeChests();
            for (Player p3 : this.dungeon.getPlayers()) {
                DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(p3);
                dp.logDungeonAttempt(this.dungeon);
            }
        }
    }

    private boolean attemptTakeTotemOfUndying(Player player) {
        ItemStack toRemove = null;
        for (ItemStack i : player.getInventory()) {
            if (i == null || i.getType() != Material.BONE || !new NBTWrapper(i).getString("cosmicType").equals("dungeonTotemOfUndying")) continue;
            if (i.getAmount() > 1) {
                i.setAmount(i.getAmount() - 1);
                return true;
            }
            toRemove = i;
        }
        if (toRemove != null) {
            player.getInventory().remove(toRemove);
            return true;
        }
        return false;
    }

    private boolean attemptTakeClockOfReturning(Player player) {
        ItemStack toRemove = null;
        for (ItemStack i : player.getInventory()) {
            if (i == null || i.getType() != Material.WATCH || !new NBTWrapper(i).getString("cosmicType").equals("dungeonClockOfReturning")) continue;
            if (i.getAmount() > 1) {
                i.setAmount(i.getAmount() - 1);
                return true;
            }
            toRemove = i;
        }
        if (toRemove != null) {
            player.getInventory().remove(toRemove);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager().hasMetadata("invisibleMazeZombie") && event.getEntity() instanceof Player && Math.random() < 0.25) {
            Player player = (Player)event.getEntity();
            if (event.getEntity().hasMetadata("inInvisibleMaze") && this.attemptTakeTotemOfUndying(player)) {
                player.sendTitle(new Title(CC.translate("&6Totem of Undying"), "", 5, 35, 5));
                player.setHealth(player.getMaxHealth());
                event.setCancelled(true);
            } else {
                player.sendMessage(CC.translate((String)"&c&l(!) &cYou have succumbed to the mob's ricochet wrath!"));
                player.sendMessage(CC.translate((String)"&7You have been teleported back to the start."));
                this.dungeon.teleportPlayer(new Location(this.dungeon.getWorld(), 817.0, 199.0, 457.0), player, true);
                this.dungeon.getDungeonChallengesCompleted().remove((Object)DungeonChallenge.PATH_FINDER);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if (!event.getTo().getWorld().getName().startsWith("dungeon_")) {
            return;
        }
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(event.getPlayer());
        if (dp != null && dp.getCurrentDungeon() != null && dp.getCurrentDungeon().equals(this.dungeon) && WorldGuardUtils.getRegionsIn((Location)event.getPlayer().getLocation()).contains("first_parkour") && event.getPlayer().getLocation().getBlockY() <= 140) {
            if (this.attemptTakeClockOfReturning(event.getPlayer())) {
                Location tpLocation = new Location(this.dungeon.getWorld(), 8.0, 154.0, -56.0);
                tpLocation.setPitch(0.0f);
                tpLocation.setYaw(0.0f);
                event.getPlayer().sendTitle(new Title(CC.translate("&6Clock of Returning"), "", 5, 35, 5));
                this.dungeon.teleportPlayer(tpLocation, event.getPlayer(), true);
            } else {
                this.dungeon.broadcastMessage(CC.translate((String)("&c" + event.getPlayer().getName() + " fell into the void - everyone in the parkour has been teleported back to the start!")));
                this.dungeon.getDungeonChallengesCompleted().remove((Object)DungeonChallenge.TWINKE_TOES);
                this.dungeon.getPlayers().forEach(p -> {
                    if (WorldGuardUtils.getRegionsIn((Player)p).contains("first_parkour")) {
                        Location tpLocation = new Location(this.dungeon.getWorld(), 8.0, 154.0, -56.0);
                        tpLocation.setPitch(0.0f);
                        tpLocation.setYaw(0.0f);
                        this.dungeon.teleportPlayer(tpLocation, (Player)p, true);
                    }
                });
            }
        }
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return true;
        }
        Player player = (Player)sender;
        MathRoomRegionEvent mathRoomRegionEvent = this.getMathRoomRegionEvent();
        if (mathRoomRegionEvent != null && mathRoomRegionEvent.getButtonPressedPlayers().contains(player.getUniqueId()) && this.dungeon.getPlayers().contains((Object)player)) {
            if (args.length == 1 && args[0].equalsIgnoreCase("cancel")) {
                player.sendMessage(CC.translate((String)"&c&l(!) &cCancelled input."));
                mathRoomRegionEvent.getButtonPressedPlayers().remove(player.getUniqueId());
                return true;
            }
            if (args.length == 1) {
                if (!args[0].equalsIgnoreCase(Integer.toString(mathRoomRegionEvent.getTotal()))) {
                    player.sendMessage(CC.translate((String)"&c&l(!) &cIncorrect answer."));
                    mathRoomRegionEvent.getButtonPressedPlayers().remove(player.getUniqueId());
                    return true;
                }
                this.dungeon.broadcastMessage(CC.translate((String)("&a" + player.getName() + " has successfully solved the dungeon escape sequence with the answer: &n" + mathRoomRegionEvent.getTotal() + "&a!")));
                mathRoomRegionEvent.getButtonPressedPlayers().clear();
                Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get(), new BukkitRunnable(){

                    public void run() {
                        Block dungeon_portalBlockA = PlanetNulEvents.this.dungeon.getWorld().getBlockAt(723, 148, 470);
                        Block dungeon_portalBlockB = PlanetNulEvents.this.dungeon.getWorld().getBlockAt(723, 149, 470);
                        dungeon_portalBlockA.setMetadata("dungeonExitPortalBlock", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)PlanetNulEvents.this.dungeon.getDungeonID()));
                        dungeon_portalBlockB.setMetadata("dungeonExitPortalBlock", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), (Object)PlanetNulEvents.this.dungeon.getDungeonID()));
                        dungeon_portalBlockA.setTypeIdAndData(90, (byte)0, false);
                        dungeon_portalBlockB.setTypeIdAndData(90, (byte)0, false);
                        HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)dungeon_portalBlockB.getLocation().add(0.5, 1.25, 0.5), (String[])new String[]{(Object)ChatColor.RED + "" + (Object)ChatColor.BOLD + "Exit"});
                        PlanetNulEvents.this.dungeon.setPortalPlaceCooldown(System.currentTimeMillis() + 120000L);
                        PlanetNulEvents.this.dungeon.setDungeonStatus(DungeonState.COMPLETE);
                        PlanetNulEvents.this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 120000L, "Leave the Dungeon");
                    }
                }, 1L);
            }
        }

        return false;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(player);
        if (dp == null || dp.getCurrentDungeon() == null || dp.getCurrentDungeon().getDungeonType() != DungeonType.PLANET_NUL) {
            return;
        }
        if (event.getAction() == Action.PHYSICAL) {
            if (this.lockedPressurePlateLocations.contains((Object)event.getClickedBlock().getLocation()) && !this.airlockKeyUnlocked) {
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
            }
        } else if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getLocation().equals((Object)new Location(event.getClickedBlock().getWorld(), 714.0, 152.0, 470.0))) {
                MathRoomRegionEvent mathRoomRegionEvent = this.getMathRoomRegionEvent();
                if (mathRoomRegionEvent.getButtonPressedPlayers() != null && !mathRoomRegionEvent.getButtonPressedPlayers().contains(event.getPlayer().getUniqueId())) {
                    event.getPlayer().sendMessage(CC.translate((String)"&b&l(!) &bPlease enter the dungeon's escape sequence using /answer <answer>!"));
                    event.getPlayer().sendMessage(CC.translate((String)"&7If you wish to cancel, enter '&c/answer cancel&7' in the chat."));
                    mathRoomRegionEvent.getButtonPressedPlayers().add(event.getPlayer().getUniqueId());
                }
            } else {
                if (this.lockedDoorLocations.contains((Object)event.getClickedBlock().getLocation()) && this.isAirlockKey(event.getPlayer().getItemInHand()) && !this.airlockKeyUnlocked) {
                    this.dungeon.broadcastMessage(CC.translate((String)("&a" + event.getPlayer().getName() + " has unlocked the doors to the Vault Airlock!")));
                    this.dungeon.getPlayers().forEach(p -> p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f));
                    this.airlockKeyUnlocked = true;
                    event.setCancelled(true);
                    event.setUseItemInHand(null);
                    event.getPlayer().setItemInHand(null);
                    return;
                }
                if (this.mazeLevers.containsKey((Object)event.getClickedBlock().getLocation())) {
                    event.setCancelled(true);
                    if (this.mazeLeversCooldown.containsKey((Object)event.getClickedBlock().getLocation()) && this.mazeLeversCooldown.get((Object)event.getClickedBlock().getLocation()) > System.currentTimeMillis()) {
                        player.sendMessage(CC.translate((String)("&c&l(!)&c You cannot do this for another " + (this.mazeLeversCooldown.get((Object)event.getClickedBlock().getLocation()) - System.currentTimeMillis()) + "ms.")));
                        return;
                    }
                    this.dungeon.broadcastMessage(CC.translate((String)("&f" + player.getName() + " pulled a &dmagical lever&f hidden in the depths of the forest. Perhaps new areas are now accessible?")));
                    this.pullMazeLever(event.getClickedBlock().getLocation());
                    this.mazeLeversCooldown.put(event.getClickedBlock().getLocation(), System.currentTimeMillis() + 750L);
                    Block leverBlock = event.getClickedBlock();
                    Lever lever = (Lever)leverBlock.getState().getData();
                    lever.setPowered(!lever.isPowered());
                    leverBlock.setData(lever.getData());
                    leverBlock.getState().setData((MaterialData)lever);
                    leverBlock.getState().update(true);
                }
            }
        }
        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            ColorsRegionEvent colorsRegion = (ColorsRegionEvent)this.regionEvents.get("colors");
            Location clickedLocation = event.getClickedBlock().getLocation();
            if (clickedLocation.getBlockX() == -14 && clickedLocation.getBlockY() == 234 && clickedLocation.getBlockZ() == 94) {
                for (Player p2 : this.dungeon.getPlayers()) {
                    PlayerPotionEffects.untrackPotionEffect((Player)p2, (PotionEffectType)PotionEffectType.SLOW);
                    PlayerPotionEffects.untrackPotionEffect((Player)p2, (PotionEffectType)PotionEffectType.SLOW_DIGGING);
                    p2.sendMessage(ChatColor.YELLOW.toString() + (Object)ChatColor.BOLD + "BLESSED!");
                    p2.sendMessage((Object)ChatColor.GRAY + "The Gods have looked down and cast their blessing upon the weary Cosmonauts about to embark on this mission!");
                }
            }
            if (event.getClickedBlock().getType() == Material.STONE_BUTTON && colorsRegion != null) {
                int idx;
                ArrayList<String> solvedPanels = new ArrayList<>();
                for (int i = 0; i < colorsRegion.currentPanelIndex; ++i) {
                    solvedPanels.add(colorsRegion.panelOrder.get(i));
                }
                boolean playSound = false;
                if (colorsRegion.leftPanelButtonLocations.contains((Object)clickedLocation) && !solvedPanels.contains("LEFT")) {
                    idx = colorsRegion.leftPanelButtonLocations.indexOf((Object)clickedLocation);
                    colorsRegion.leftPanelCurrentColors.set(idx, colorsRegion.getNewColor(colorsRegion.leftPanelCurrentColors.get(idx), event.getAction()));
                    playSound = true;
                } else if (colorsRegion.middlePanelButtonLocations.contains((Object)clickedLocation) && !solvedPanels.contains("MIDDLE")) {
                    idx = colorsRegion.middlePanelButtonLocations.indexOf((Object)clickedLocation);
                    colorsRegion.middlePanelCurrentColors.set(idx, colorsRegion.getNewColor(colorsRegion.middlePanelCurrentColors.get(idx), event.getAction()));
                    playSound = true;
                } else if (colorsRegion.rightPanelButtonLocations.contains((Object)clickedLocation) && !solvedPanels.contains("RIGHT")) {
                    idx = colorsRegion.rightPanelButtonLocations.indexOf((Object)clickedLocation);
                    colorsRegion.rightPanelCurrentColors.set(idx, colorsRegion.getNewColor(colorsRegion.rightPanelCurrentColors.get(idx), event.getAction()));
                    playSound = true;
                }
                if (playSound) {
                    final Button button = (Button)event.getClickedBlock().getState().getData();
                    if (!button.isPowered()) {
                        button.setPowered(true);
                        Bukkit.getScheduler().runTaskLater((Plugin)CosmicDungeonsPlugin.get(), new BukkitRunnable(){
                            public void run() {
                                button.setPowered(false);
                            }
                        }, 3L);
                    }
                    player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0f, 1.6f);
                    event.setCancelled(true);
                } else if (WorldGuardUtils.getRegionsIn((Location)event.getClickedBlock().getLocation()).contains("colors")) {
                    player.sendMessage(CC.translate((String)"&c&l(!) &cThis airlock panel has already been solved!"));
                    event.setCancelled(true);
                }
                colorsRegion.updateAllPanels();
            } else if (event.getClickedBlock().getType() == Material.LEVER && event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                if (colorsRegion.leftPanelLeverLocations.contains((Object)clickedLocation)) {
                    colorsRegion.onLeverClick(player, "LEFT");
                } else if (colorsRegion.middlePanelLeverLocations.contains((Object)clickedLocation)) {
                    colorsRegion.onLeverClick(player, "MIDDLE");
                } else if (colorsRegion.rightPanelLeverLocations.contains((Object)clickedLocation)) {
                    colorsRegion.onLeverClick(player, "RIGHT");
                }
                event.setCancelled(true);
                event.setUseInteractedBlock(Event.Result.DENY);
            }
        }
    }

    @EventHandler
    public void onProjectileLaunch(ProjectileHitEvent event) {
        if (event.getEntity().hasMetadata("fireballAlley")) {
            event.getEntity().setBounce(false);
        }
    }

    @Override
    public void loadRegionTriggerEvents() {
        this.regionEvents.put("maze_start_area", new GlassBreakRegionEvent(this.dungeon, "maze_start_area"));
        this.regionEvents.put("colors", new ColorsRegionEvent(this.dungeon, "colors"));
        this.regionEvents.put("first_parkour", new FirstParkourRegionEvent(this.dungeon, "first_parkour"));
        this.regionEvents.put("fireball_alley", new FireballAlleyRegionEvent(this.dungeon, "fireball_alley"));
        this.regionEvents.put("invisible_maze", new InvisibleMazeRegionEvent(this.dungeon, "invisible_maze"));
        this.regionEvents.put("find_corrupt_room", new FindCorruptRoomRegionEvent(this.dungeon, "find_corrupt_room"));
        this.regionEvents.put("boss_room", new BossRoomRegionEvent(this.dungeon, "boss_room"));
        this.regionEvents.put("second_parkour", new SecondParkourRegionEvent(this.dungeon, "second_parkour"));
        this.regionEvents.put("math_room", new MathRoomRegionEvent(this.dungeon, "math_room"));
    }

    @EventHandler
    private void onPrePlayerDeath(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player))
            return;
        if(!(event.getFinalDamage() - ((Player) event.getEntity()).getHealth() <= 0))
            return;
        if (event.getEntity().hasMetadata("inInvisibleMaze") && this.attemptTakeTotemOfUndying((Player) event.getEntity())) {
            ((Player) event.getEntity()).sendTitle(new Title(CC.translate("&6Totem of Undying"), "", 5, 35, 5));
            ((Player) event.getEntity()).setHealth(((Player) event.getEntity()).getMaxHealth());
            event.setCancelled(true);
        }

        //might not work
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (DungeonsAPI.get().getPlayerDungeonData(event.getPlayer()).getCurrentDungeon() != null && DungeonsAPI.get().getPlayerDungeonData(event.getPlayer()).getCurrentDungeon().getDungeonType() == DungeonType.PLANET_NUL && event.getAction() == Action.RIGHT_CLICK_BLOCK && event.hasBlock() && event.getClickedBlock().getType().equals((Object)Material.CHEST)) {
            Location clickedLocation = event.getClickedBlock().getLocation();
            if (this.chestLocations.contains(clickedLocation) && !this.announcedChestLocations.contains(clickedLocation)) {
                Chest chest = (Chest)event.getClickedBlock().getState();
                ItemStack item = chest.getInventory().getContents()[0];
                JSONMessage message = new JSONMessage(CC.translate((String)"&6&l(DUNGEON) &f"));
                message.addText(CC.translate((String)(player.getName() + " opened an &eancient chest &fhidden in the depths of the forest, which contained the following items:\n&6&l* &f" + item.getAmount() + "x ")));
                message.addItem(item, item.getItemMeta().getDisplayName());
                this.dungeon.getPlayers().forEach(p -> {
                    message.sendToPlayer(p);
                    p.playSound(p.getLocation(), Sound.CHEST_OPEN, 0.4f, 1.0f);
                });
                this.announcedChestLocations.add(clickedLocation);
            } else {
                Location setToTrue = null;
                for (Map.Entry<Location, Boolean> entry : this.extraChests.entrySet()) {
                    ItemStack item;
                    Location k = entry.getKey();
                    Boolean v = entry.getValue();
                    if (k.getX() != clickedLocation.getX() || k.getY() != clickedLocation.getY() || k.getZ() != clickedLocation.getZ() || v.booleanValue()) continue;
                    Chest chest = (Chest)event.getClickedBlock().getState();
                    ItemStack itemStack = item = chest.getInventory().getContents().length == 0 ? null : chest.getInventory().getContents()[0];
                    if (item != null) {
                        JSONMessage message = new JSONMessage(CC.translate((String)"&6&l(DUNGEON) &f"));
                        message.addText(CC.translate((String)("What's this?! " + player.getName() + " brushes aside the cobwebs atop a suspicious chest. Inside they find:\n&6&l* &f1x &e&l")));
                        message.addItem(item, item.getItemMeta().getDisplayName());
                        this.dungeon.getPlayers().forEach(p -> {
                            message.sendToPlayer(p);
                            p.playSound(p.getLocation(), Sound.NOTE_PLING, 0.7f, 1.5f);
                        });
                    } else {
                        player.sendMessage(CC.translate((String)"&c&l(!) &cYou open a chest which has been hidden in a suspicious looking area. But there's nothing inside?!"));
                    }
                    setToTrue = k;
                }
                if (setToTrue != null) {
                    this.extraChests.replace(setToTrue, true);
                }
            }
        }
    }

    @EventHandler(priority=EventPriority.LOW)
    public void onCorruptCosmonautDeath(EntityDeathEvent event) {
        Boss boss = (Boss)Boss.bosses.get(event.getEntity().getUniqueId());
        if (boss != null && boss.getBossType() == BossType.getBossType("CORRUPT_COSMONAUT") && boss.equals((Object)this.dungeon.boss)) {
            World world = this.dungeon.getWorld();
            EditSession editSession = new EditSessionBuilder(FaweAPI.getWorld((String)world.getName())).build();
            if (boss.getDamageTracker().size() == 1) {
                this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.CRUSADER);
            }
            for (Vector pt : new FastIterator((Iterable)new CuboidRegion(new Vector(777, 84, 686), new Vector(787, 82, 720)), editSession)) {
                world.getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ()).setType(Math.random() < 0.5 ? Material.NETHERRACK : Material.NETHER_BRICK);
            }
            for (Vector pt : new FastIterator((Iterable)new CuboidRegion(new Vector(777, 85, 696), new Vector(787, 85, 721)), editSession)) {
                world.getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ()).setType(Math.random() < 0.5 ? Material.NETHERRACK : Material.NETHER_BRICK);
            }
            for (Vector pt : new FastIterator((Iterable)new CuboidRegion(new Vector(778, 90, 685), new Vector(787, 85, 685)), editSession)) {
                Block block = world.getBlockAt(pt.getBlockX(), pt.getBlockY(), pt.getBlockZ());
                if (block.getType() != Material.STAINED_GLASS || block.getData() != 1) continue;
                block.setType(Material.AIR);
            }
            for (Player p : this.dungeon.getPlayers()) {
                p.setWalkSpeed(0.2f);
            }
            this.dungeon.broadcastMessage(CC.White + "You did it! The Corrupt Cosmonaut - one of the most dangerous criminals in the Cosmoverse - has been defeated! However, it appears your battle has destabilized Planet Nul's Core, and the world is imploding! You must escape back to the surface, and quickly!");
            this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + 120000L, "Escape the destabilized planet");
            this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get_(), (Location)new Location(this.dungeon.getWorld(), 782.5, 89.0, 671.0), (String[])new String[]{(Object)ChatColor.GREEN + ChatColor.BOLD.toString() + "UNLOCKED", "Proceed quickly!"}));
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) {
            return;
        }
        Player player = (Player)event.getEntity();
        DungeonPlayer dp = DungeonsAPI.get().getPlayerDungeonData(player);
        if ((((CraftEntity)event.getDamager()).getHandle() instanceof EntityCustomFireball || event.getDamager().hasMetadata("fireballAlley")) && dp.getCurrentDungeon() != null && dp.getCurrentDungeon().getDungeonType() == DungeonType.PLANET_NUL) {
            this.dungeon.teleportPlayer(new Location(this.dungeon.getWorld(), 876.0, 214.0, 303.0), player, true);
            player.getLocation().setPitch(0.0f);
            player.getLocation().setYaw(0.0f);
            player.sendMessage(CC.translate((String)"&c&l(!) &cOuch! You were hit by a fiery flame and received 2nd degree burns!"));
            player.sendMessage(CC.translate((String)"&7You have been teleported back to the start."));
            event.setCancelled(true);
        }
    }

    private PlanetNulFireballTask fireballTask;
    private PlanetNulInvisibleMazeTask invisibleMazeTask;
    private PlanetNulMazeTask mazeTask;
    private PlanetNulParkourRestrictionTask parkourRestrictionTask;

    public void unregisterTasks() {
        if(this.fireballTask != null) {
            this.fireballTask.cancel();
            this.fireballTask = null;
        }
        if(this.invisibleMazeTask != null) {
            this.invisibleMazeTask.cancel();
            this.invisibleMazeTask = null;
        }
        if(this.mazeTask != null) {
            this.mazeTask.cancel();
            this.mazeTask = null;
        }
        if(this.parkourRestrictionTask != null) {
            this.parkourRestrictionTask.cancel();
            this.parkourRestrictionTask = null;
        }
    }

    private void loadTasks() {
        this.parkourRestrictionTask = new PlanetNulParkourRestrictionTask(this.dungeon);
        this.parkourRestrictionTask.runTaskTimer((Plugin)CosmicDungeonsPlugin.get_(), 10L, 10L);
        this.fireballTask = new PlanetNulFireballTask(this.dungeon);
        this.fireballTask.runTaskTimer((Plugin)CosmicDungeonsPlugin.get_(), 0L, 20L);
        this.invisibleMazeTask = new PlanetNulInvisibleMazeTask(this.dungeon);
        this.invisibleMazeTask.runTaskTimer((Plugin)CosmicDungeonsPlugin.get_(), 0L, 5L);
        this.mazeTask = new PlanetNulMazeTask(this.dungeon);
        this.mazeTask.runTaskTimer((Plugin)CosmicDungeonsPlugin.get_(), 0L, 5L);
    }

    private void updateLocations() {
        this.possibleChestLocations.forEach(loc -> loc.setWorld(this.dungeon.getWorld()));
        this.enderAssassinLocations.forEach(loc -> loc.setWorld(this.dungeon.getWorld()));
        this.lockedPressurePlateLocations.forEach(loc -> loc.setWorld(this.dungeon.getWorld()));
        this.lockedDoorLocations.forEach(loc -> loc.setWorld(this.dungeon.getWorld()));
        this.extraChests.forEach((k, v) -> k.setWorld(this.dungeon.getWorld()));
    }

    private ItemStack getClockOfReturning() {
        return new NBTWrapper(new ItemBuilder(Material.WATCH, CC.YellowB + "Clock of Returning", CC.translate("&7Upon falling into the void of the"), CC.translate((String)"&7Teleportation Portal Conduit area,"), CC.translate((String)"&7instead of you and your entire team,"), CC.translate((String)"&7only you will be sent back to the"), CC.translate((String)"&7start in exchange for this clock.")).build()).setString("cosmicType", "dungeonClockOfReturning").build();
    }

    private ItemStack getTotemOfUndying() {
        return new NBTWrapper(new ItemBuilder(Material.BONE, CC.YellowB + "Totem of Undying", CC.translate("&7This item will be consumed when you get"), CC.translate((String)"&7sent back or killed on the invisible maze.")).build()).setString("cosmicType", "dungeonTotemOfUndying").build();
    }

    public void restart() {
        if (this.dungeon.getWorld() == null) {
            this.dungeon.reset();
            Bukkit.getLogger().info("[CosmicDungeons] Forced to reload: " + this.dungeon.getWorldName() + " new world: " + (Object)this.dungeon.getWorld());
        }
        this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.TWINKE_TOES);
        this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.PATH_FINDER);
        this.dungeon.getDungeonChallengesCompleted().add(DungeonChallenge.APESTOS);
        this.loadTasks();
        this.updateLocations();
    }

    public String parkourNumberToName(int num) {
        switch (num) {
            case 0: {
                return "Far Left";
            }
            case 1: {
                return "Middle Left";
            }
            case 2: {
                return "Middle Right";
            }
            case 3: {
                return "Far Right";
            }
        }
        return "";
    }

    public void reset() {
        this.randomParkourChosen = new Random().nextInt(4);
        this.airlockKeyUnlocked = false;
        this.exploredRegions.clear();
        this.chestLocations.clear();
        this.announcedChestLocations.clear();
        this.leftPanelColors = this.dungeon.getRandomColorsForPanel();
        this.middlePanelColors = this.dungeon.getRandomColorsForPanel();
        this.rightPanelColors = this.dungeon.getRandomColorsForPanel();
        this.extraChests = new LinkedHashMap<Location, Boolean>(){
            {
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 37.0, 198.0, -12.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 1.0, 217.0, -83.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 29.0, 203.0, -9.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 15.0, 157.0, -33.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 870.0, 213.0, 301.0), false);
                this.put(new Location(PlanetNulEvents.this.dungeon.getWorld(), 811.0, 198.0, 455.0), false);
            }
        };
        this.unregisterTasks();
    }

    public FireballAlleyRegionEvent getFireballRegionEvent() {
        return (FireballAlleyRegionEvent)this.regionEvents.get("fireball_alley");
    }

    public InvisibleMazeRegionEvent getInvisibleMazeRegionEvent() {
        return (InvisibleMazeRegionEvent)this.regionEvents.get("invisible_maze");
    }

    public MathRoomRegionEvent getMathRoomRegionEvent() {
        return (MathRoomRegionEvent)this.regionEvents.get("math_room");
    }
}

