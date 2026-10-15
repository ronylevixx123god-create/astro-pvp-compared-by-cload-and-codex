/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.gmail.filoghost.holograms.api.HolographicDisplaysAPI
 *  org.bukkit.Effect
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.block.Block
 *  org.bukkit.block.BlockState
 *  org.bukkit.entity.Player
 *  org.bukkit.event.block.Action
 *  org.bukkit.material.Lever
 *  org.bukkit.material.MaterialData
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons.dungeons.events.planetnul;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.PlanetNulEvents;
import com.compilex.CosmicDungeons.dungeons.events.RegionEvent;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.PlanetNulDungeon;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.gmail.filoghost.holograms.api.HolographicDisplaysAPI;

import java.util.*;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Lever;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ColorsRegionEvent
extends RegionEvent {
    public List<Location> leftPanelLeverLocations;
    public List<Location> leftPanelButtonLocations = new ArrayList<Location>();
    public List<Location> leftPanelWoolLocations;
    public List<PanelColor> leftPanelCurrentColors;
    public List<Location> middlePanelLeverLocations;
    public List<Location> middlePanelButtonLocations;
    public List<Location> middlePanelWoolLocations;
    public List<PanelColor> middlePanelCurrentColors;
    public List<Location> rightPanelLeverLocations;
    public List<Location> rightPanelButtonLocations;
    public List<Location> rightPanelWoolLocations;
    public List<PanelColor> rightPanelCurrentColors;
    public List<String> panelOrder;
    public int currentPanelIndex;
    private boolean completed;

    public ColorsRegionEvent(Dungeon d, String regionName) {
        super(d, regionName);
        this.leftPanelLeverLocations = new ArrayList<Location>();
        this.leftPanelWoolLocations = new ArrayList<Location>();
        this.leftPanelLeverLocations.add(new Location(d.world, 32.0, 190.0, -48.0));
        this.leftPanelLeverLocations.add(new Location(d.world, 32.0, 190.0, -52.0));
        this.leftPanelButtonLocations.add(new Location(d.world, 32.0, 189.0, -49.0));
        this.leftPanelButtonLocations.add(new Location(d.world, 32.0, 189.0, -50.0));
        this.leftPanelButtonLocations.add(new Location(d.world, 32.0, 189.0, -51.0));
        this.leftPanelWoolLocations.add(new Location(d.world, 30.0, 190.0, -49.0));
        this.leftPanelWoolLocations.add(new Location(d.world, 30.0, 190.0, -50.0));
        this.leftPanelWoolLocations.add(new Location(d.world, 30.0, 190.0, -51.0));
        this.middlePanelButtonLocations = new ArrayList<Location>();
        this.middlePanelLeverLocations = new ArrayList<Location>();
        this.middlePanelWoolLocations = new ArrayList<Location>();
        this.middlePanelLeverLocations.add(new Location(d.world, 35.0, 190.0, -56.0));
        this.middlePanelLeverLocations.add(new Location(d.world, 39.0, 190.0, -56.0));
        this.middlePanelButtonLocations.add(new Location(d.world, 36.0, 189.0, -56.0));
        this.middlePanelButtonLocations.add(new Location(d.world, 37.0, 189.0, -56.0));
        this.middlePanelButtonLocations.add(new Location(d.world, 38.0, 189.0, -56.0));
        this.middlePanelWoolLocations.add(new Location(d.world, 36.0, 190.0, -58.0));
        this.middlePanelWoolLocations.add(new Location(d.world, 37.0, 190.0, -58.0));
        this.middlePanelWoolLocations.add(new Location(d.world, 38.0, 190.0, -58.0));
        this.rightPanelButtonLocations = new ArrayList<Location>();
        this.rightPanelLeverLocations = new ArrayList<Location>();
        this.rightPanelWoolLocations = new ArrayList<Location>();
        this.rightPanelLeverLocations.add(new Location(d.world, 42.0, 190.0, -52.0));
        this.rightPanelLeverLocations.add(new Location(d.world, 42.0, 190.0, -48.0));
        this.rightPanelButtonLocations.add(new Location(d.world, 42.0, 189.0, -51.0));
        this.rightPanelButtonLocations.add(new Location(d.world, 42.0, 189.0, -50.0));
        this.rightPanelButtonLocations.add(new Location(d.world, 42.0, 189.0, -49.0));
        this.rightPanelWoolLocations.add(new Location(d.world, 44.0, 190.0, -51.0));
        this.rightPanelWoolLocations.add(new Location(d.world, 44.0, 190.0, -50.0));
        this.rightPanelWoolLocations.add(new Location(d.world, 44.0, 190.0, -49.0));
    }

    @Override
    public void run() {
        this.dungeon.updateTimeRestrictionTask(System.currentTimeMillis() + (long)(this.dungeon.isHalfTime() ? 90000 : 150000), "Open the Airlock");
        this.dungeon.broadcastMessage("You have located the main bunker airlock - this must be where the Corrupt Cosmonaut is hiding. Open it quickly!");
        this.panelOrder = new ArrayList<String>(Arrays.asList("LEFT", "MIDDLE", "RIGHT"));
        Collections.shuffle(this.panelOrder);
        this.currentPanelIndex = 0;
        this.completed = false;
        this.leftPanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
        this.leftPanelButtonLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.leftPanelLeverLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.leftPanelWoolLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.middlePanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
        this.middlePanelButtonLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.middlePanelLeverLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.middlePanelWoolLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.rightPanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
        this.rightPanelButtonLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.rightPanelLeverLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.rightPanelWoolLocations.forEach(l -> l.setWorld(this.dungeon.getWorld()));
        this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get(), (Location)new Location(this.dungeon.world, 37.5, 191.5, -69.0), (String[])new String[]{CC.RedB + "LOCKED"}));
        this.resetLevers();
        this.updateAllPanels();
    }

    private void breakGlass() {
        ArrayList<Location> locations = new ArrayList<Location>();
        locations.add(new Location(this.dungeon.world, 35.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 39.0, 189.0, -72.0));
        locations.add(new Location(this.dungeon.world, 35.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 39.0, 190.0, -72.0));
        locations.add(new Location(this.dungeon.world, 35.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 39.0, 191.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 192.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 192.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 192.0, -72.0));
        locations.add(new Location(this.dungeon.world, 36.0, 193.0, -72.0));
        locations.add(new Location(this.dungeon.world, 37.0, 193.0, -72.0));
        locations.add(new Location(this.dungeon.world, 38.0, 193.0, -72.0));
        locations.forEach(l -> {
            l.getBlock().setType(Material.AIR);
            l.getWorld().playEffect(l.add(0.5, 0.5, 0.5), Effect.STEP_SOUND, (Object)Material.STAINED_GLASS);
        });
    }

    public void updateAllPanels() {
        Block block;
        int i;
        for (i = 0; i < this.leftPanelWoolLocations.size(); ++i) {
            block = this.leftPanelWoolLocations.get(i).getBlock();
            block.setTypeIdAndData(35, this.leftPanelCurrentColors.get(i).getDyeColor().getWoolData(), true);
        }
        for (i = 0; i < this.middlePanelWoolLocations.size(); ++i) {
            block = this.middlePanelWoolLocations.get(i).getBlock();
            block.setTypeIdAndData(35, this.middlePanelCurrentColors.get(i).getDyeColor().getWoolData(), true);
        }
        for (i = 0; i < this.rightPanelWoolLocations.size(); ++i) {
            block = this.rightPanelWoolLocations.get(i).getBlock();
            block.setTypeIdAndData(35, this.rightPanelCurrentColors.get(i).getDyeColor().getWoolData(), true);
        }
    }

    public PanelColor getNewColor(PanelColor current, Action action) {
        PanelColor[] colors;
        int index = current.ordinal();
        int nextIndex = index + 1;
        if (action == Action.LEFT_CLICK_BLOCK) {
            nextIndex = index - 1;
        }
        if ((nextIndex %= (colors = PanelColor.values()).length) == -1) {
            nextIndex = colors.length - 1;
        }
        return colors[nextIndex];
    }

    public void onLeverClick(Player player, String panel) {
        for (int i = 0; i < this.currentPanelIndex; ++i) {
            if (!this.panelOrder.get(i).equalsIgnoreCase(panel)) continue;
            player.sendMessage(CC.translate((String)"&c&l(!) &cThis airlock panel has already been solved!"));
            return;
        }
        if (!this.panelOrder.get(this.currentPanelIndex).equals(panel)) {
            this.onFailedAttempt();
        } else {
            PlanetNulEvents planetNulEvents = ((PlanetNulDungeon)this.dungeon).getDungeonEvents();
            switch (panel) {
                case "LEFT": {
                    this.onAttempt(player, panel, planetNulEvents.leftPanelColors.equals(this.leftPanelCurrentColors));
                    break;
                }
                case "MIDDLE": {
                    this.onAttempt(player, panel, planetNulEvents.middlePanelColors.equals(this.middlePanelCurrentColors));
                    break;
                }
                case "RIGHT": {
                    this.onAttempt(player, panel, planetNulEvents.rightPanelColors.equals(this.rightPanelCurrentColors));
                }
            }
        }
    }

    private void onAttempt(Player player, String panel, boolean correct) {
        if (!correct) {
            this.onFailedAttempt();
        } else {
            try {
                this.changeLevers((List)this.getClass().getField(panel.toLowerCase() + "PanelLeverLocations").get(this), true);
            }
            catch (IllegalAccessException | NoSuchFieldException reflectiveOperationException) {
                // empty catch block
            }
            this.dungeon.broadcastMessage(CC.translate((String)("&a" + player.getName() + " has solved the &a&l" + panel + "&a airlock panel!")));
            this.dungeon.getPlayers().forEach(p -> p.playSound(p.getLocation(), Sound.LEVEL_UP, 1.0f, 1.5f));
            if (this.currentPanelIndex == 2) {
                if (this.completed) {
                    String notIt = ((PlanetNulDungeon)this.dungeon).getDungeonEvents().parkourNumberToName(new ArrayList<Integer>(Arrays.asList(0, 1, 2, 3)).stream().filter(i -> i != ((PlanetNulDungeon)this.dungeon).getDungeonEvents().randomParkourChosen).findAny().get());
                    this.dungeon.broadcastMessage(CC.translate((String)("&eWhat's this? Hmmm it seems that the answer is not " + notIt + "!")));
                    this.resetLevers();
                    this.leftPanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
                    this.middlePanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
                    this.rightPanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
                    this.currentPanelIndex = 0;
                    this.updateAllPanels();
                } else {
                    this.dungeon.broadcastMessage(CC.translate((String)"The Airlock is now &a&lOPEN&f - proceed with caution!"));
                    this.resetLevers();
                    this.completed = true;
                    Collections.reverse(this.panelOrder);
                    this.currentPanelIndex = 0;
                    this.breakGlass();
                    this.dungeon.setObjectiveHologram(HolographicDisplaysAPI.createHologram((Plugin)CosmicDungeonsPlugin.get(), (Location)new Location(this.dungeon.world, 37.5, 191.0, -69.0), (String[])new String[]{CC.GreenB + "UNLOCKED", CC.Gray + "Proceed with caution!"}));
                }
                return;
            }
            ++this.currentPanelIndex;
        }
    }

    private void spawnAntiCosmonaut(Location location) {
        PigZombie skeleton = (PigZombie) location.getWorld().spawnEntity(location, EntityType.PIG_ZOMBIE);
        if(Math.random() > 0.5)
            skeleton.setBaby(true);
        ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
        sword.addUnsafeEnchantment(Enchantment.KNOCKBACK, new Random().nextInt(1) + 1);
        sword.addEnchantment(Enchantment.DAMAGE_ALL, 1);
        skeleton.getEquipment().setItemInHand(sword);
        skeleton.setMaxHealth(10.0);
        skeleton.setHealth(skeleton.getMaxHealth());
        skeleton.setCustomName(CC.AquaB + "Anti" + CC.WhiteB + "-" + CC.LightPurpleB + "Cosmonaut");
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
        skeleton.setMetadata("anti_cosmonaut", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)true));
        skeleton.setMetadata("do_not_clear", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get(), (Object)true));
    }

    private void onFailedAttempt() {
        this.dungeon.broadcastMessage(CC.translate((String)"&cHmm... something seems not quite right here!"));
        this.dungeon.getPlayers().forEach(p -> p.playSound(p.getLocation(), Sound.NOTE_PLING, 0.4f, 0.1f));
        if(this.currentPanelIndex > 0) {
            this.dungeon.getPlayers().forEach(p -> p.playSound(p.getLocation(), Sound.GHAST_SCREAM, 1f, 1f));
            Bukkit.getScheduler().runTaskLater(CosmicDungeonsPlugin.get(), () -> spawnAntiCosmonaut(new Location(this.dungeon.getWorld(), 37, 190, -49)), 40L);
        }
        for (int i = 0; i < this.currentPanelIndex; ++i) {
            switch (this.panelOrder.get(i)) {
                case "LEFT": {
                    this.leftPanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
                    break;
                }
                case "MIDDLE": {
                    this.middlePanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
                    break;
                }
                case "RIGHT": {
                    this.rightPanelCurrentColors = ((PlanetNulDungeon)this.dungeon).getRandomColorsForPanel();
                }
            }
            this.updateAllPanels();
        }
        this.resetLevers();
        this.currentPanelIndex = 0;
    }

    private void resetLevers() {
        this.changeLevers(this.leftPanelLeverLocations, false);
        this.changeLevers(this.middlePanelLeverLocations, false);
        this.changeLevers(this.rightPanelLeverLocations, false);
    }

    private void changeLevers(List<Location> locations, boolean up) {
        locations.forEach(loc -> {
            BlockState state = loc.getBlock().getState();
            Lever lever = (Lever)state.getData();
            lever.setPowered(up);
            state.setData(lever);
            state.update();
        });
    }
}

