package org.demonicpvp.demonictrials.trial.room.impl;

import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.server.v1_8_R3.EntityZombie;
import net.minecraft.server.v1_8_R3.GenericAttributes;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftZombie;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.material.Button;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import org.demonicpvp.demonictrials.DemonicTrials;
import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.stackable.StackableModifier;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.util.WorldEditHelper;
import org.github.paperspigot.Title;

import java.util.*;
import java.util.stream.Collectors;

public class ColorCrisisRoom extends TrialRoom {
    private static final String COLOR_CRISIS_ZOMBIE_KEY = "colorCrisisZombie";

    private Location[] mainWoolLocations;
    private Location[] woolLocations;

    private CombinationColor[] colorOrder;
    private CombinationColor[] inputColors;

    private List<Location> revenantSpawnLocations;

    private Location leverLocation;
    private int wrongFlicks;

    public ColorCrisisRoom(TrialSession trial) {
        super(trial);
    }

    @Override
    public String getName() {
        return "Color Crisis";
    }

    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public Material getMaterial() {
        return Material.WOOL;
    }

    @Override
    public List<String> getDescription() {
        return Lists.newArrayList(
                "Find the correct combination of colors",
                "before the time runs out!"
        );
    }

    @Override
    public Location getSpawnLocation() {
        return new Location(this.trial.getWorld(), -103.5, 114, 86.5, 180, 0);
    }

    @Override
    public Set<Location> getExitPortalLocations() {
        return Sets.newHashSet(new Location(this.trial.getWorld(), -104, 108, 81));
    }

    @Override
    public void reset() {
        super.reset();
        WorldEditHelper.pasteSchematic("trials_color_crisis", new Location(this.trial.getWorld(), -141, 100, 50));

        // I don't feel like remaking the schem lmao
        new Location(this.trial.getWorld(), -106, 113, 56).getBlock().setType(Material.AIR);

        this.colorOrder = new CombinationColor[8];
        this.inputColors = new CombinationColor[8];

        this.wrongFlicks = 0;

        // starting from north and going clockwise
        List<CombinationColor> possibleColors = Arrays.stream(CombinationColor.values()).collect(Collectors.toList());
        Collections.shuffle(possibleColors);

        for (int i = 0; i < 8; i++) {
            this.colorOrder[i] = possibleColors.get(new Random().nextInt(possibleColors.size()));
        }

        this.leverLocation = new Location(this.trial.getWorld(), -104, 115, 81);

        this.mainWoolLocations = new Location[] {
                new Location(this.trial.getWorld(), -104, 113, 58),
                new Location(this.trial.getWorld(), -88, 113, 65),
                new Location(this.trial.getWorld(), -81, 113, 81),
                new Location(this.trial.getWorld(), -88, 113, 97),
                new Location(this.trial.getWorld(), -104, 113, 104),
                new Location(this.trial.getWorld(), -120, 113, 97),
                new Location(this.trial.getWorld(), -127, 113, 81),
                new Location(this.trial.getWorld(), -120, 113, 65)
        };

        this.woolLocations = new Location[] {
                new Location(this.trial.getWorld(), -104, 113, 56),
                new Location(this.trial.getWorld(), -86, 113, 63),
                new Location(this.trial.getWorld(), -79, 113, 81),
                new Location(this.trial.getWorld(), -86, 113, 99),
                new Location(this.trial.getWorld(), -104, 113, 106),
                new Location(this.trial.getWorld(), -122, 113, 99),
                new Location(this.trial.getWorld(), -129, 113, 81),
                new Location(this.trial.getWorld(), -122, 113, 63)
        };

        this.revenantSpawnLocations = Lists.newArrayList(
                new Location(this.trial.getWorld(), -116, 113, 93),
                new Location(this.trial.getWorld(), -116, 113, 69),
                new Location(this.trial.getWorld(), -92, 113, 69),
                new Location(this.trial.getWorld(), -92, 113, 93),
                new Location(this.trial.getWorld(), -108, 113, 93),
                new Location(this.trial.getWorld(), -116, 113, 77),
                new Location(this.trial.getWorld(), -90, 113, 80),
                new Location(this.trial.getWorld(), -108, 113, 69)
        );

        this.shuffleWool();
    }

    @Override
    public void start() {
        super.start();

        this.sendToolTipAndMessage();

        this.trial.getPlayers().stream().map(HumanEntity::getInventory).forEach(inventory -> {
            inventory.setHelmet(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 5).build()));
            inventory.setChestplate(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 5).build()));
            inventory.setLeggings(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 5).build()));
            inventory.setBoots(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 3).enchant(Enchantment.DURABILITY, 5).build()));

            inventory.setItem(0, this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 3).enchant(Enchantment.DURABILITY, 5).build()));
            inventory.setItem(1, this.trial.getItemWithTrialLore(new ItemBuilder(Material.GOLDEN_APPLE).amount(16).build()));
        });
    }

    private void shuffleWool() {
        for (int i = 0; i < 8; i++) {
            CombinationColor correctColor = this.colorOrder[i];

            List<CombinationColor> possibleColors = Arrays.stream(CombinationColor.values()).collect(Collectors.toList());
            possibleColors.remove(correctColor);
            Collections.shuffle(possibleColors);

            this.setDisplayColor(i, possibleColors.get(0));
        }
    }

    private void setDisplayColor(int index, CombinationColor color) {
        this.inputColors[index] = color;

        Location location = this.woolLocations[index];
        Location corner = location.clone().add(-1, 0, -1);

        this.mainWoolLocations[index].getBlock().setTypeIdAndData(Material.WOOL.getId(), (byte) color.getWoolData(), true);

        for (int y = 0; y <= 14; y++) {
            for (int x = 0; x < 3; x++) {
                for (int z = 0; z < 3; z++) {
                    Block block = corner.clone().add(x, y, z).getBlock();

                    if (block.getType() == Material.WOOL) {
                        block.setTypeIdAndData(Material.WOOL.getId(), (byte) color.getWoolData(), true);
                    }
                }
            }
        }
    }

    private void spawnRevenant(Location location) {
        if (!location.getChunk().isLoaded()) location.getChunk().load();

        Zombie zombie = (Zombie)this.trial.getWorld().spawnEntity(location, EntityType.ZOMBIE);
        zombie.setVillager(false);
        zombie.setBaby(false);
        if (zombie.getVehicle() != null) zombie.getVehicle().remove();
        zombie.setMetadata(COLOR_CRISIS_ZOMBIE_KEY, new FixedMetadataValue(DemonicTrials.getInstance(), true));
        zombie.setMaxHealth(30.0);
        zombie.setHealth(30.0);

        String customNameTag = CommonsUtil.color(this.trial.getCurrentTier().getColor() + "&lRevenant");

        zombie.setCustomName(customNameTag);
        zombie.setCustomNameVisible(true);

        EntityEquipment equipment = zombie.getEquipment();
        equipment.setHelmet(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_HELMET).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 10).build()));
        equipment.setChestplate(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_CHESTPLATE).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 10).build()));
        equipment.setLeggings(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_LEGGINGS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 10).build()));
        equipment.setBoots(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_BOOTS).enchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1).enchant(Enchantment.DURABILITY, 10).build()));
        equipment.setItemInHand(this.trial.getItemWithTrialLore(new ItemBuilder(Material.DIAMOND_SWORD).enchant(Enchantment.DAMAGE_ALL, 3).enchant(Enchantment.DURABILITY, 10).build()));

        equipment.setHelmetDropChance(0);
        equipment.setChestplateDropChance(0);
        equipment.setLeggingsDropChance(0);
        equipment.setBootsDropChance(0);
        equipment.setItemInHandDropChance(0);

        EntityZombie handle = ((CraftZombie) zombie).getHandle();
        handle.getAttributeInstance(GenericAttributes.c).setValue(0.3);

        int sugarRushStacks = this.trial.getModifierStacks(StackableModifier.SUGAR_RUSH);
        if (sugarRushStacks > 0) {
            handle.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).setValue(handle.getAttributeInstance(GenericAttributes.MOVEMENT_SPEED).getValue() * (1.0 + (sugarRushStacks * 0.025)));
        }

        /*this.trial.getPlayers().forEach(p -> {
            CosmicPacketHandler.sendPacketToPlayer(
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
            );
        });*/
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity damager = event.getDamager();

        if (entity.hasMetadata(COLOR_CRISIS_ZOMBIE_KEY) && damager instanceof Player) {
            Player pDamager = (Player) damager;

            if (pDamager.getItemInHand().getType() == Material.DIAMOND_SWORD) {
                //event.setUseRawDamage(true);

                int wetNoodleStacks = this.trial.getModifierStacks(StackableModifier.WET_NOODLE);
                event.setDamage(3.0 * (1.0 - (wetNoodleStacks * 0.05)));
            }

            if (Math.random() < 0.7) {
                pDamager.damage(0);

                double newHealth = Math.max(0, pDamager.getHealth() - 1);

                if (newHealth > 0) {
                    pDamager.setHealth(newHealth);
                } else {
                    CommonsUtil.sendMessage(pDamager, "&6&l[Trials] &cYou have died and been eliminated from the trial!");
                    this.trial.removePlayer(pDamager, TrialSession.TrialPlayerRemoveReason.DIED);
                }
            }

            ((Zombie)entity).setTarget(pDamager);
        } else if (damager.hasMetadata(COLOR_CRISIS_ZOMBIE_KEY) && entity instanceof Player) {
            //event.setUseRawDamage(true);

            int fragileStacks = this.trial.getModifierStacks(StackableModifier.FRAGILE);
            event.setDamage(4.0 * (1.0 + (fragileStacks * 0.1)));
        }
    }

    @EventHandler
    public void on(EntityDeathEvent event) {
        if (event.getEntity().hasMetadata(COLOR_CRISIS_ZOMBIE_KEY)) {
            event.getDrops().clear();
            event.setDroppedExp(0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void on(PlayerInteractEvent event) {
        if (!this.hasStarted || this.hasEnded) return;

        TrialManager.getTrial(event.getPlayer()).ifPresent(trial -> {
            if (trial.getCurrentRoom() != this) {
                return;
            }

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_BLOCK) {
                Block clickedBlock = event.getClickedBlock();

                if (event.getClickedBlock().getLocation().equals(this.leverLocation)) {
                    if (this.trial.getWorld().getEntities().stream().anyMatch(e -> e.hasMetadata(COLOR_CRISIS_ZOMBIE_KEY))) {
                        CommonsUtil.sendMessage(event.getPlayer(), "&c&l(!) &cYou must kill all the Revenants first!");
                        return;
                    }

                    this.trial.sendMessage("");
                    this.trial.sendMessage("&e&lCurrent Sequence");

                    boolean isCorrect = true;
                    String[] directions = new String[] { "North", "North East", "East", "South East", "South", "South West", "West", "North West" };

                    for (int i = 0; i < directions.length; i++) {
                        String direction = directions[i];

                        CombinationColor inputColor = this.inputColors[i];
                        this.trial.sendMessage("&e&l* &e" + direction + ": " + inputColor.getColor() + inputColor.getName() + "&e - " + (this.colorOrder[i] == inputColor ? "&a&l✔" : "&c&l✘"));

                        if (this.colorOrder[i] != inputColor) isCorrect = false;
                    }

                    this.trial.sendMessage("");

                    if (isCorrect) {
                        // clear glass and other blocks in the middle
                        Location startLocation = new Location(this.trial.getWorld(), -106, 113, 79);

                        for (int y = 0; y <= 2; y++) {
                            for (int x = 0; x <= 4; x++) {
                                for (int z = 0; z <= 4; z++) {
                                    Block loopBlock = startLocation.clone().add(x, y, z).getBlock();

                                    if (loopBlock.getType() != Material.STAINED_CLAY) {
                                        loopBlock.setType(Material.AIR);
                                    }
                                }
                            }
                        }

                        this.trial.onRoomComplete();
                    } else {
                        this.trial.getPlayers().forEach(p -> {
                            p.sendTitle(new Title("", CommonsUtil.color("&c&lIncorrect Sequence"), 5, 30, 5));
                        });

                        this.shuffleWool();
                        this.wrongFlicks++;
                        Collections.shuffle(this.revenantSpawnLocations);

                        for (int i = 0; i < Math.min(6, this.wrongFlicks - 1); i++) {
                            this.spawnRevenant(this.revenantSpawnLocations.get(i).clone().add(new Random().nextInt(3) * (Math.random() < 0.5 ? -1 : 1), 0, new Random().nextInt(3) * (Math.random() < 0.5 ? -1 : 1)));
                        }
                    }
                } else {
                    for (int i = 0; i < this.mainWoolLocations.length; i++) {
                        Location mainWoolLocation = this.mainWoolLocations[i];
                        Location buttonLocation = mainWoolLocation.clone().add(0, 1, 0);

                        if (clickedBlock.getLocation().equals(buttonLocation)) {
                            if (this.trial.getWorld().getEntities().stream().anyMatch(e -> e.hasMetadata(COLOR_CRISIS_ZOMBIE_KEY))) {
                                CommonsUtil.sendMessage(event.getPlayer(), "&c&l(!) &cYou must kill all the Revenants first!");
                                event.setCancelled(true);
                                return;
                            }

                            Button button = (Button) clickedBlock.getState().getData();

                            if (!button.isPowered()) {
                                button.setPowered(true);
                                Bukkit.getScheduler().runTaskLater(DemonicTrials.getInstance(), new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        button.setPowered(false);
                                    }
                                }, 3L);
                            }

                            if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                                if (this.inputColors[i].ordinal() + 1 > CombinationColor.values().length - 1) {
                                    this.setDisplayColor(i, CombinationColor.values()[0]);
                                } else {
                                    this.setDisplayColor(i,
                                            CombinationColor.values()[this.inputColors[i].ordinal() + 1]
                                    );
                                }
                            } else if (event.getAction() == Action.LEFT_CLICK_BLOCK) {
                                if (this.inputColors[i].ordinal() - 1 < 0) {
                                    this.setDisplayColor(i,
                                            CombinationColor.values()[CombinationColor.values().length - 1]
                                    );
                                } else {
                                    this.setDisplayColor(i,
                                            CombinationColor.values()[this.inputColors[i].ordinal() - 1]
                                    );
                                }
                            }

                            event.setCancelled(true);
                            break;
                        }
                    }
                }
            }
        });
    }

    private enum CombinationColor {
        RED("Red", ChatColor.RED.toString(), 14),
        ORANGE("Orange", ChatColor.GOLD.toString(), 1),
        YELLOW("Yellow", ChatColor.YELLOW.toString(), 4),
        GREEN("Green", ChatColor.GREEN.toString(), 5),
        LIGHT_BLUE("Light Blue", ChatColor.AQUA.toString(), 3),
        CYAN("Cyan", ChatColor.DARK_AQUA.toString(), 9),
        BLUE("Blue", ChatColor.BLUE.toString(), 11),
        PURPLE("Purple", ChatColor.DARK_PURPLE.toString(), 10),
        MAGENTA("Magenta", ChatColor.LIGHT_PURPLE.toString(), 2);

        private final String name;
        private final String color;
        private final int woolData;

        CombinationColor(String name, String color, int woolData) {
            this.name = name;
            this.color = color;
            this.woolData = woolData;
        }

        public String getName() {
            return this.name;
        }

        public String getColor() {
            return this.color;
        }

        public int getWoolData() {
            return this.woolData;
        }
    }
}
