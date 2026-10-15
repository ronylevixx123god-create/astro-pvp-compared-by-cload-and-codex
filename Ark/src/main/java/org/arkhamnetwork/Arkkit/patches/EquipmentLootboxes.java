/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.compilex.CosmicArmorSets.CosmicArmorSetsAPI
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.ArmorSet
 *  com.compilex.CosmicArmorSets.me.astropvp.cratekeys.enums.HeroicArmorType
 *  com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.GeneralUtil
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  com.google.common.collect.Lists
 *  me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  me.cosmicpvp.CosmicGlobalKits.libs.MasteryKits
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.bukkit.Bukkit
 *  org.bukkit.ChatColor
 *  org.bukkit.Color
 *  org.bukkit.DyeColor
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.command.Command
 *  org.bukkit.command.CommandSender
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.Action
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.LeatherArmorMeta
 *  org.bukkit.plugin.Plugin
 *  org.bukkit.plugin.java.JavaPlugin
 *  org.bukkit.scheduler.BukkitRunnable
 *  org.bukkit.scheduler.BukkitTask
 */
package org.arkhamnetwork.Arkkit.patches;

import com.compilex.CosmicArmorSets.CosmicArmorSetsAPI;
import com.compilex.CosmicArmorSets.enums.ArmorSet;
import com.compilex.CosmicArmorSets.enums.HeroicArmorType;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.libs.MasteryKits;
import org.arkhamnetwork.Arkkit.Arkkit;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.arkhamnetwork.Arkkit.libs.JSONMessage;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugin;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class EquipmentLootboxes
        extends ArkPlugin {
    private final ItemStack lootboxItem = new ItemBuilder(Material.CHEST, CC.GoldB + "Equipment Lootbox " + CC.Gray + "(Right Click)", "", CC.GrayU + "Right-click" + CC.Gray + " to receive a random", CC.Gray + "item from one of these armor sets!", "", CC.GoldB + "Available Armor Sets", CC.GoldB + " * " + CC.RedB + "Phantom Armor Set", CC.GoldB + " * " + CC.AquaB + "Yeti Armor Set", CC.GoldB + " * " + CC.WhiteB + "Mother Of Yijki Armor Set", CC.GoldB + " * " + CC.GreenB + "Ranger Armor Set", CC.GoldB + " * " + CC.DarkRedB + "Supreme Armor Set", CC.GoldB + " * " + CC.DarkPurpleB + "Dimensional Traveler Armor Set").build();
    private final ItemStack heroicLootboxItem = new NBTWrapper(new ItemBuilder(Material.CHEST, CC.GoldB + "Heroic Equipment Lootbox " + CC.Gray + "(Right Click)", "", CC.GrayU + "Right-click" + CC.Gray + " to receive a random", CC.Gray + "heroic armor piece from", CC.Gray + "one of these armor sets!", "", CC.GoldB + "Available Armor Sets", CC.GoldB + " * " + CC.RedB + "Phantom Armor Set", CC.GoldB + " * " + CC.AquaB + "Yeti Armor Set", CC.GoldB + " * " + CC.WhiteB + "Mother Of Yijki Armor Set", CC.GoldB + " * " + CC.GreenB + "Ranger Armor Set", CC.GoldB + " * " + CC.DarkRedB + "Supreme Armor Set", CC.GoldB + " * " + CC.DarkPurpleB + "Dimensional Traveler Armor Set").build()).setString("conaxData", "heroicLootbox").build();

    public EquipmentLootboxes(Plugin p) {
        super(p);
    }





    @Override
    public void enable() throws IOException {
        super.enable();
        this.registerCommand(new LootboxCommand("elootbox", "/<command>", "Lootbox admin command"));
        this.registerListener(new LootboxListener());
    }

    @Override
    public void disable() throws IOException {
        super.disable();
        this.unregisterCommands();
        this.unregisterListeners();
    }

    public ItemStack getRandomGkitItem() {
        GlobalKits kit = GlobalKits.values()[random.nextInt(GlobalKits.values().length)];
        List items = CosmicGlobalKitsAPI.getGlobalKitItems(kit, "Heroic Armor GKit Generator");
        return (ItemStack) items.get(random.nextInt(items.size()));
    }

    public ItemStack getRandomMkitItem() {
        ArrayList kits = Lists.newArrayList( new MasteryKits[]{MasteryKits.DEATH_KNIGHT, MasteryKits.GHOST, MasteryKits.NECROMANCER});
        MasteryKits random = (MasteryKits) kits.get(ThreadLocalRandom.current().nextInt(kits.size()));
        List items = random.getKit().getMasteryItems(null, random, true);
        items.removeIf(item -> item == null || !GeneralUtil.isArmor((ItemStack) item));
        Collections.shuffle(items);
        return items.isEmpty() ? null : (ItemStack) items.get(ThreadLocalRandom.current().nextInt(items.size()));
    }

    class LootboxOpenMenuAnimation
            extends CosmicGUI {
        private final int MAX_ANIMATION_TICKS;
        private final boolean heroic;
        BukkitTask animationTask;
        List<ArmorSet> validArmorSets;
        private int animationTick;
        private boolean finished;
        private boolean readyToGive;
        private ItemStack lastArmorPeice;

        public LootboxOpenMenuAnimation(Player player, boolean heroicArmor) {
            super(player, (heroicArmor ? "Heroic " : "") + "Equipment Lootbox", 9);
            this.MAX_ANIMATION_TICKS = 150;
            this.finished = false;
            this.readyToGive = false;
            this.validArmorSets = Lists.newArrayList(ArmorSet.MOTHER_OF_YIJKI, ArmorSet.PHANTOM, ArmorSet.YETI, ArmorSet.SUPREME, ArmorSet.RANGER);
            this.heroic = heroicArmor;
        }

        public void openGUI(JavaPlugin owning) {
            super.openGUI(owning);
            this.startAnimation();
        }

        private void startAnimation() {
            if (this.animationTask != null && Bukkit.getScheduler().isCurrentlyRunning(this.animationTask.getTaskId())) {
                this.animationTask.cancel();
            }
            this.animationTask = new BukkitRunnable() {

                public void run() {
                    if (!LootboxOpenMenuAnimation.this.player.isValid() || LootboxOpenMenuAnimation.this.player.getOpenInventory() == null || !LootboxOpenMenuAnimation.this.inventory.equals(LootboxOpenMenuAnimation.this.player.getOpenInventory().getTopInventory())) {
                        this.cancel();
                        Bukkit.getLogger().info("Cancelling task for " + LootboxOpenMenuAnimation.this.player.getName() + " due to not having inventory open!");
                        return;
                    }
                    if (LootboxOpenMenuAnimation.this.animationTick > LootboxOpenMenuAnimation.this.MAX_ANIMATION_TICKS) {
                        this.cancel();
                        LootboxOpenMenuAnimation.this.readyToGive = true;
                        LootboxOpenMenuAnimation.this.player.playSound(LootboxOpenMenuAnimation.this.player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.5f);
                        Bukkit.getScheduler().scheduleSyncDelayedTask(Arkkit.get(), () -> {
                            if (LootboxOpenMenuAnimation.this.player.isValid() && LootboxOpenMenuAnimation.this.player.getOpenInventory() != null && LootboxOpenMenuAnimation.this.inventory.equals(LootboxOpenMenuAnimation.this.player.getOpenInventory().getTopInventory()) && LootboxOpenMenuAnimation.this.readyToGive) {
                                LootboxOpenMenuAnimation.this.readyToGive = false;
                                LootboxOpenMenuAnimation.this.givePrize(true);
                                LootboxOpenMenuAnimation.this.player.closeInventory();
                            }
                        }, 100L);
                        return;
                    }
                    if (LootboxOpenMenuAnimation.this.animationTick < LootboxOpenMenuAnimation.this.MAX_ANIMATION_TICKS) {
                        if (LootboxOpenMenuAnimation.this.animationTick < 30) {
                            LootboxOpenMenuAnimation.this.player.playSound(LootboxOpenMenuAnimation.this.player.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.92f);
                            if (ThreadLocalRandom.current().nextBoolean()) {
                                LootboxOpenMenuAnimation.this.player.playSound(LootboxOpenMenuAnimation.this.player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.1f);
                            }
                            LootboxOpenMenuAnimation.this.redraw();
                        } else {
                            int ticksToSkip = 0;
                            int n = LootboxOpenMenuAnimation.this.animationTick > 80 ? 4 : (LootboxOpenMenuAnimation.this.animationTick > 60 ? 3 : (ticksToSkip = LootboxOpenMenuAnimation.this.animationTick > 40 ? 2 : 2));
                            if (LootboxOpenMenuAnimation.this.animationTick % ticksToSkip == 0) {
                                LootboxOpenMenuAnimation.this.redraw();
                                LootboxOpenMenuAnimation.this.player.playSound(LootboxOpenMenuAnimation.this.player.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.92f);
                                if (ThreadLocalRandom.current().nextBoolean()) {
                                    LootboxOpenMenuAnimation.this.player.playSound(LootboxOpenMenuAnimation.this.player.getLocation(), Sound.WOOD_CLICK, 1.0f, 1.1f);
                                }
                            }
                        }
                    }
                    LootboxOpenMenuAnimation.this.animationTick++;
                    if (LootboxOpenMenuAnimation.this.animationTick >= LootboxOpenMenuAnimation.this.MAX_ANIMATION_TICKS) {
                        LootboxOpenMenuAnimation.this.readyToGive = true;
                    }
                }
            }.runTaskTimer(Arkkit.get(), 20L, 1L);
        }

        private void givePrize(boolean announce) {
            if (this.finished) {
                return;
            }
            this.finished = true;
            this.readyToGive = false;
            if (this.lastArmorPeice == null) {
                this.lastArmorPeice = this.getRandomArmorSetItem();
            }

            if (announce) {
                Bukkit.broadcastMessage(CC.GoldB + "(!) " + CC.Gold + this.player.getName() + " received a " + this.lastArmorPeice.getItemMeta().getDisplayName() + CC.Gold + " from a " + (this.heroic ? "Heroic " : "") + "Equipment Lootbox!");
            } else {
                this.player.sendMessage(CC.GoldB + "(!) " + CC.Gold + "You found a " + this.lastArmorPeice.getItemMeta().getDisplayName() + CC.Gold + " inside a" + (this.heroic ? " Heroic" : "n") + " Equipment Lootbox!");
            }
            this.player.playSound(this.player.getLocation(), Sound.LEVEL_UP, 1.1f, 0.9f);
            PlayerUtil.giveOrDropProtectedItem(this.player, this.lastArmorPeice, -1);
            Bukkit.getLogger().info("{EquipmentLootbox] Giving: " + this.lastArmorPeice + " to " + this.player.getName() + "!");
            this.player.updateInventory();
            Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(this.player, "EQUIP_LOOTBOX_OPEN", this.lastArmorPeice));
            if (this.heroic) {
                Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(this.player, "EQUIP_HEROIC_LOOTBOX_OPEN", this.lastArmorPeice));
            }
        }


        private ItemStack getRandomArmorSetItem() {
            ItemStack retr;
            if (Math.random() <= 0.02) {
                retr = ArmorSet.DIMENSIONAL_TRAVELER.getRandomPeice(true);
            } else {
                ItemStack weapon;
                ArmorSet set = this.validArmorSets.get(ThreadLocalRandom.current().nextInt(this.validArmorSets.size()));
                if (!this.heroic && Math.random() <= 0.05 && (weapon = CosmicArmorSetsAPI.getWeaponFromArmorSet(set)) != null) {
                    return weapon;
                }
                retr = set.getRandomPeice(true);
            }
            if (this.heroic) {
                retr = CosmicArmorSetsAPI.convertItemToHeroic(retr, HeroicArmorType.getFromMaterial(retr.getType()), true);
            }
            return retr;
        }

        public void redraw() {
            if (this.lastArmorPeice == null) {
                for (int i = 0; i < this.size; ++i) {
                    this.setItem(i, new ItemBuilder(Material.STAINED_GLASS_PANE).name(" ").data(DyeColor.GRAY.getWoolData()).build(), null);
                }
            }
            this.lastArmorPeice = this.getRandomArmorSetItem();
            this.setItem(4, new InventoryItem(this.lastArmorPeice).click(() -> this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must wait to claim this item!")));
            this.player.updateInventory();
        }
    }

    class LootboxListener
            implements Listener {
        LootboxListener() {
        }

        @EventHandler(priority = EventPriority.HIGHEST)
        public void onPlayerInteract(PlayerInteractEvent event) {
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                ItemStack item = event.getItem();
                if (!(item != null && item.getType() != Material.AIR && item.hasItemMeta() && item.getItemMeta().hasLore() && item.getItemMeta().hasDisplayName())) {
                    return;
                }
                Player player = event.getPlayer();
                if (player.getItemInHand() == null || !player.getItemInHand().equals(item)) {
                    return;
                }
                NBTWrapper isWrapper = new NBTWrapper(item);
                boolean gkitGenerator = false;
                boolean heroicLootbox = false;
                boolean mkitGenerator = false;
                gkitGenerator = item.getType() == Material.CHEST && isWrapper.getString("conaxData").equals("GKIT_GENERATOR");
                if (gkitGenerator || (heroicLootbox = isWrapper.getString("conaxData").equals("heroicLootbox")) || (mkitGenerator = isWrapper.getString("conaxData").equals("MKIT_GENERATOR")) || item.getItemMeta().getDisplayName().equals(EquipmentLootboxes.this.lootboxItem.getItemMeta().getDisplayName()) && ItemUtils.isLoreSameWithoutIID(EquipmentLootboxes.this.lootboxItem.getItemMeta().getLore(), item.getItemMeta().getLore())) {
                    event.setCancelled(true);
                    event.setUseItemInHand(Event.Result.DENY);
                    if (player.getWorld().getName().startsWith("world_duels")) {
                        player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot use this in the duel world!");
                        return;
                    }
                    if (item.getAmount() > 1) {
                        item.setAmount(1);
                    } else {
                        player.setItemInHand(null);
                    }
                    player.updateInventory();
                    player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1.0f, 1.1f);
                    if (gkitGenerator) {
                        int attempts;
                        ItemStack retr = EquipmentLootboxes.this.getRandomGkitItem();
                        for (attempts = 0; retr == null || !GeneralUtil.isArmor(retr) && attempts < 1000000; ++attempts) {
                            retr = EquipmentLootboxes.this.getRandomGkitItem();
                        }
                        if (!GeneralUtil.isArmor(retr)) {
                            Bukkit.getLogger().info("[HeroicGkitGenerator] Unable to find armor for " + player.getName());
                            retr = CosmicArmorSetsAPI.createHeroicPeice(HeroicArmorType.values()[random.nextInt(HeroicArmorType.values().length)]);
                        } else {
                            HeroicArmorType type = null;
                            String name = retr.getType().name();
                            Object object = name.endsWith("_HELMET") ? HeroicArmorType.HELMET : (name.endsWith("_CHESTPLATE") ? HeroicArmorType.CHESTPLATE : (name.endsWith("_LEGGINGS") ? HeroicArmorType.LEGGINGS : (type = name.endsWith("_BOOTS") ? HeroicArmorType.BOOTS : null)));
                            if (type != null) {
                                retr.setType(type.getMaterial());
                                NBTWrapper wrapper = new NBTWrapper(retr);
                                wrapper.setString("conaxData", "HEROIC_ARMOR");
                                wrapper.setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("durability", Integer.valueOf(type.getDurability())).setInt("maxDurability", Integer.valueOf(type.getDurability())).setInt("armorModifier", Integer.valueOf(type.getArmorModifier())).get());
                                retr = wrapper.build();
                                LeatherArmorMeta im = (LeatherArmorMeta) retr.getItemMeta();
                                List lore = im.getLore();
                                if (lore == null) {
                                    lore = Lists.newArrayList();
                                }
                                lore.add("");
                                lore.add(CC.Gray + "+" + type.getArmorModifier() + " Armor Value");
                                lore.add(CC.Gray + type.getDurability() + " Durability");
                                lore.add(CC.DarkRed + "This armor is stronger than diamond.");
                                im.setLore(lore);
                                im.setColor(Color.RED);
                                retr.setItemMeta(im);
                            }
                        }
                        Bukkit.getLogger().info("[HeroicGkitGenerator] Giving " + retr + " to " + player.getName() + " in " + attempts + " attempts!");
                        PlayerUtil.giveOrDropProtectedItem(player, retr, -1, null, CC.Red + "Inventory Full - Heroic Armor dropped on ground.");
                        JSONMessage message = new JSONMessage("");
                        message.addText(null, CC.GoldB + "(!) " + CC.Gold + "You have received a ", ChatColor.GOLD);
                        message.addItem(retr, ItemUtils.getDisplayName(retr), ChatColor.GOLD);
                        message.addText(null, CC.Gold + " from the Heroic G-Kit Generator!", ChatColor.GOLD);
                        message.sendToPlayer(player);
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.4f);
                        player.updateInventory();
                    } else if (mkitGenerator) {
                        ItemStack mkit = EquipmentLootboxes.this.getRandomMkitItem();
                        Bukkit.getLogger().info("[MkitGenerator] Giving " + mkit + " to " + player.getName() + " in 1 attempt!");
                        PlayerUtil.giveOrDropProtectedItem(player, mkit, -1, null, CC.Red + "Inventory Full - Mastery Kit Armor dropped on ground.");
                        JSONMessage message = new JSONMessage("");
                        message.addText(null, CC.GoldB + "(!) " + CC.Gold + "You have received a ", ChatColor.GOLD);
                        message.addItem(mkit, ItemUtils.getDisplayName(mkit), ChatColor.GOLD);
                        message.addText(null, CC.Gold + " from the Random M-Kit Generator!", ChatColor.GOLD);
                        message.sendToPlayer(player);
                        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.4f);
                        player.updateInventory();
                    } else {
                        new LootboxOpenMenuAnimation(player, heroicLootbox).openGUI(Arkkit.get());
                    }
                }
            }
        }
    }

    class LootboxCommand
            extends AbstractCommand {
        public LootboxCommand(String command, String usage, String description) {
            super(command, usage, description);
        }

        @Override
        public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
            boolean heroic;
            if (!sender.isOp()) {
                return true;
            }
            boolean bl = heroic = args.length == 2 && args[1].equalsIgnoreCase("heroic");
            if (args.length == 1 || heroic) {
                Player pl = Bukkit.getPlayer(args[0]);
                if (pl == null) {
                    sender.sendMessage(CC.Red + "Invalid player!");
                    return true;
                }
                pl.sendMessage(CC.GoldB + "(!) " + CC.Gold + "You have been given an " + CC.GoldB + "Equipment Lootbox!");
                sender.sendMessage(CC.Red + "Given lootbox to " + pl.getName() + "!");
                return true;
            }
            sender.sendMessage(CC.Red + "Usage: /lootbox <name> [heroic]");
            return false;
        }
    }
}

