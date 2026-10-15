package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.server.v1_8_R3.AttributeInstance;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;

import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.trial.modifier.stackable.StackableModifier;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class RaidingRainbowRoom extends TrialRoom {
    private static final String RAIDING_RAINBOW_ZOMBIE_KEY = "raidingRainbowZombie";

    private List<RaidingRainbowColor> colors;
    private List<RaidingRainbowColor> currentColors;

    public RaidingRainbowRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Raiding Rainbow";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_BLUE;
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SWORD;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Kill the mobs in the correct order before",
                "time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -271.5, 125, -23.5, -90.f, 0.f);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), -230, 124, -24));
    }

    @Override
    public TrialTier getRollUntil() {
        return TrialTier.APPRENTICE;
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        this.trial.getPlayers().stream().map(HumanEntity::getInventory).forEach(inventory -> {
            inventory.setHelmet(this.trial.getItemWithTrialLore(new ItemBuilder(Material.LEATHER_HELMET).addEnchantment(Enchantment.DURABILITY, 3).build()));
            inventory.setChestplate(this.trial.getItemWithTrialLore(new ItemBuilder(Material.LEATHER_CHESTPLATE).addEnchantment(Enchantment.DURABILITY, 3).build()));
            inventory.setLeggings(this.trial.getItemWithTrialLore(new ItemBuilder(Material.LEATHER_LEGGINGS).addEnchantment(Enchantment.DURABILITY, 3).build()));
            inventory.setBoots(this.trial.getItemWithTrialLore(new ItemBuilder(Material.LEATHER_BOOTS).addEnchantment(Enchantment.DURABILITY, 3).build()));

            ItemStack sword = new ItemStack(Material.DIAMOND_SWORD);
            sword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 5);
            sword.addUnsafeEnchantment(Enchantment.DURABILITY, 10);

            inventory.setItem(0, this.trial.getItemWithTrialLore(sword));
            inventory.setItem(1, this.trial.getItemWithTrialLore(new ItemStack(Material.ENDER_PEARL, 16)));
            inventory.setItem(2, this.trial.getItemWithTrialLore(new ItemStack(Material.GOLDEN_APPLE, 2)));
        });

        this.respawnMobs();
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_zombie_colors", new Location(this.trial.getWorld(), -215, 100, 26));

        this.colors = Lists.newLinkedList();
        List<RaidingRainbowColor> possibleColors = Arrays.stream(RaidingRainbowColor.values()).collect(Collectors.toList());
        Collections.shuffle(possibleColors);

        possibleColors.forEach(color -> {
            this.colors.add(color);
            this.colors.add(color);
        });

        this.currentColors = Lists.newArrayList();

        this.removeMobs();
    }

    private void respawnMobs() {
        this.removeMobs();

        List<RaidingRainbowColor> colorsToSpawn = Lists.newArrayList();

        List<Location> spawnLocations = Lists.newArrayList(
                new Location(this.trial.getWorld(), -303, 124, -66, 0.f, 0.f),
                new Location(this.trial.getWorld(), -272, 124, -67, 0.f, 0.f),
                new Location(this.trial.getWorld(), -241, 124, -66, 0.f, 0.f),
                new Location(this.trial.getWorld(), -241, 124, 18, 180.f, 0.f),
                new Location(this.trial.getWorld(), -272, 124, 19, 180.f, 0.f),
                new Location(this.trial.getWorld(), -303, 124, 18, 180.f, 0.f),
                new Location(this.trial.getWorld(), -315, 124, -24, -90.f, 0.f)
        );

        for (RaidingRainbowColor color : RaidingRainbowColor.values()) {
            for (int i = 0; i < 2; i++) {
                colorsToSpawn.add(color);
            }
        }

        spawnLocations.forEach(location -> {
            Location offsetLocation = location.clone().subtract(1, 0, 0);

            for (int i = 0; i < 2; i++) {
                Collections.shuffle(colorsToSpawn);

                this.spawnZombie(offsetLocation, colorsToSpawn.get(0));
                colorsToSpawn.remove(0);

                offsetLocation.add(1, 0, 0);
            }
        });
    }

    private void removeMobs() {
        this.trial.getWorld().getEntities().stream().filter(e -> e.hasMetadata(RAIDING_RAINBOW_ZOMBIE_KEY)).forEach(Entity::remove);
    }

    private void spawnZombie(Location location, RaidingRainbowColor color) {
        if (!location.getChunk().isLoaded()) location.getChunk().load();

        Zombie zombie = (Zombie)this.trial.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setVillager(false);
        zombie.setBaby(false);
        if (zombie.getVehicle() != null) zombie.getVehicle().remove();
        zombie.setMetadata(RAIDING_RAINBOW_ZOMBIE_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), color.name()));
        zombie.setMaxHealth(18.0);
        zombie.setHealth(18.0);

        zombie.getLocation().setYaw(location.getYaw());

        String customNameTag = CommonsUtil.color(color.getChatColor() + "&lRainbow Mob");

        zombie.setCustomName(customNameTag);
        zombie.setCustomNameVisible(true);

        ItemStack helmet = new ItemStack(Material.LEATHER_HELMET);
        LeatherArmorMeta itemMeta = (LeatherArmorMeta) helmet.getItemMeta();
        itemMeta.setColor(color.getArmorColor());
        helmet.setItemMeta(itemMeta);

        zombie.getEquipment().setHelmet(helmet);
        zombie.getEquipment().setChestplate(null);
        zombie.getEquipment().setLeggings(null);
        zombie.getEquipment().setBoots(null);

        EntityZombie handle = ((CraftZombie) zombie).getHandle();
        AttributeInstance movementSpeed = handle.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED);
        movementSpeed.setValue(0.23000000417232513D * 1.5D);

        int sugarRushStacks = this.trial.getModifierStacks(StackableModifier.SUGAR_RUSH);
        if (sugarRushStacks > 0) {
            movementSpeed.setValue(movementSpeed.getValue() * (1.0 + (sugarRushStacks * 0.025)));
        }

        handle.getAttributeInstance(GenericAttributes.FOLLOW_RANGE).setValue(16);

        ItemStack sword = this.trial.applySneakSkin(new ItemStack(Material.STONE_SWORD), "MEAT_CLEAVER_AXE");
        zombie.getEquipment().setItemInHand(sword);

        this.trial.getPlayers().forEach(p -> {
            /*CosmicPacketHandler.sendPacketToPlayer(
                    p,
                    new SSetEntityProperties(
                            zombie.getEntityId(),
                            new SSetEntityProperties.EntityProperties(
                                    1.5,
                                    false,
                                    false,
                                    true,
                                    customNameTag,
                                    0,
                                    0,
                                    false,
                                    ""
                            )
                    )
            );*/
        });
    }

    @EventHandler
    public void on(ChunkUnloadEvent event) {
        if (event.getWorld().getName().equals(this.trial.getWorld().getName())) event.setCancelled(true);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(EntityDamageByEntityEvent event) {
        if (event.getEntity().hasMetadata(RAIDING_RAINBOW_ZOMBIE_KEY)) {
            //event.setUseRawDamage(true);

            int wetNoodleStacks = this.trial.getModifierStacks(StackableModifier.WET_NOODLE);
            event.setDamage(6.0 * (1.0 - (wetNoodleStacks * 0.05)));
        } else if (event.getDamager().hasMetadata(RAIDING_RAINBOW_ZOMBIE_KEY) && event.getEntity() instanceof Player) {
            //event.setUseRawDamage(true);

            int fragileStacks = this.trial.getModifierStacks(StackableModifier.FRAGILE);
            event.setDamage(2.0 * (1.0 + (fragileStacks * 0.1)));
        }
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        Player killer = event.getEntity().getKiller();

        if (event.getEntity().hasMetadata(RAIDING_RAINBOW_ZOMBIE_KEY)) {
            event.getDrops().clear();
            event.setDroppedExp(0);

            if (killer != null) {
                TrialManager.getTrial(killer).ifPresent(t -> {
                    if (t.getId() == this.trial.getId()) {
                        RaidingRainbowColor killedColor = RaidingRainbowColor.valueOf(event.getEntity().getMetadata(RAIDING_RAINBOW_ZOMBIE_KEY).get(0).asString());

                        if (this.colors.get(this.currentColors.size()) == killedColor) {
                            this.trial.sendMessage("&6&l[Trials] &a" + killer.getName() + " killed a " + killedColor.getDisplayName() + " Mob&a - &a&lCORRECT!");

                            boolean nigger = killer.getName().equals("NiiteMareZ") && killer.isSneaking();

                            if (this.currentColors.size() == 2 * 7 - 1 || nigger) {
                                Location startLocation = new Location(this.trial.getWorld(), -235, 124, -25);

                                for (int z = 0; z < 3; z++) {
                                    for (int y = 0; y < 3; y++) {
                                        startLocation.clone().add(0, y, z).getBlock().setType(Material.AIR);
                                    }
                                }

                                this.removeMobs();
                                this.trial.onRoomComplete();
                            } else {
                                this.currentColors.add(killedColor);
                            }
                        } else {
                            this.trial.sendMessage("&6&l[Trials] &c" + killer.getName() + " killed a " + killedColor.getDisplayName() + " Mob&c - &c&lINCORRECT!");
                            this.trial.sendMessage("&7All slain mobs have been resurrected.");
                            this.respawnMobs();

                            this.currentColors.clear();
                        }
                    }
                });
            }
        }
    }

    private enum RaidingRainbowColor {
        RED("&cRed", Color.fromRGB(160, 54, 54), ChatColor.RED),
        BLUE("&9Blue", Color.fromRGB(48, 71, 167), ChatColor.BLUE),
        ORANGE("&6Orange", Color.fromRGB(181, 107, 40), ChatColor.GOLD),
        LIME("&aLime", Color.fromRGB(120, 186, 31), ChatColor.GREEN),
        PURPLE("&5Purple", Color.fromRGB(118, 60, 170), ChatColor.DARK_PURPLE),
        MAGENTA("&dMagenta", Color.fromRGB(164, 85, 193), ChatColor.DARK_PURPLE),
        CYAN("&3Cyan", Color.fromRGB(69, 114, 133), ChatColor.DARK_AQUA);

        private final String displayName;
        private final Color armorColor;
        private final ChatColor chatColor;

        RaidingRainbowColor(String displayName, Color armorColor, ChatColor chatColor) {
            this.displayName = displayName;
            this.armorColor = armorColor;
            this.chatColor = chatColor;
        }

        public String getDisplayName() {
            return this.displayName;
        }

        public Color getArmorColor() {
            return this.armorColor;
        }

        public ChatColor getChatColor() {
            return this.chatColor;
        }
    }
}
