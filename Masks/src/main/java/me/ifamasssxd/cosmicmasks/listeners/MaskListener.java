/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ParticleEffect
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CustomItemBuilder
 *  org.arkhamnetwork.Arkkit.patches.ItemID
 *  org.arkhamnetwork.Arkkit.plugins.ArkPlugins
 *  org.bukkit.Bukkit
 *  org.bukkit.GameMode
 *  org.bukkit.Location
 *  org.bukkit.Material
 *  org.bukkit.Sound
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.EventHandler
 *  org.bukkit.event.EventPriority
 *  org.bukkit.event.Listener
 *  org.bukkit.event.block.BlockPlaceEvent
 *  org.bukkit.event.entity.EntityDamageByEntityEvent
 *  org.bukkit.event.entity.EntityDamageEvent
 *  org.bukkit.event.entity.EntityDamageEvent$DamageCause
 *  org.bukkit.event.entity.EntityDeathEvent
 *  org.bukkit.event.entity.FoodLevelChangeEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.ClickType
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryType$SlotType
 *  org.bukkit.event.player.PlayerGameModeChangeEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicmasks.listeners;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicmasks.CosmicMasks;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicmasks.items.MultiMaskItem;
import me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MaskListener implements Listener {
    public static List<MaskType> availableMasks = Lists.newArrayList(MaskType.PILGRIM, MaskType.TURKEY, MaskType.SCARECROW, MaskType.PURGE, MaskType.SANTA, MaskType.NECROMANCER, MaskType.RIFT, MaskType.ZEUS, MaskType.GLITCH, MaskType.REINDEER, MaskType.DUNGEON);
    public static List<MaskType> rareMasks = Lists.newArrayList(MaskType.MONOPOLY, MaskType.PARTY_HAT, MaskType.DEATH_KNIGHT, MaskType.LOVER, MaskType.JOKER, MaskType.BUNNY, MaskType.BOSS, MaskType.STRONGHOLD, MaskType.OUTPOST);

    public MaskType getRandomMask() {
        if (Math.random() <= 0.1) {
            return rareMasks.get(ThreadLocalRandom.current().nextInt(rareMasks.size()));
        }
        return availableMasks.get(ThreadLocalRandom.current().nextInt(availableMasks.size()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        if (event.getNewGameMode() == GameMode.CREATIVE) {
            Bukkit.getScheduler().runTaskLater(CosmicMasks.get(), () -> event.getPlayer().updateInventory(), 1L);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        NBTWrapper wrapper;
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.CHEST && (wrapper = new NBTWrapper(item)).getString("cosmicType").equalsIgnoreCase("RANDOM_MASK")) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            if (!player.getWorld().getName().equalsIgnoreCase(Bukkit.getWorlds().get(0).getName()) && !player.getWorld().getName().contains("_nether")) {
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must open this in the Overworld!");
                return;
            }
            if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                long id = ItemID.getItemID(item);
                if (id != -1L && ItemID.blacklist_iid.contains(id)) {
                    player.sendMessage(CC.RedB + "(!) " + CC.Red + "It seems you tried to claim a duplicate Random Mask, it has been removed from your inventory.");
                    player.setItemInHand(null);
                    player.updateInventory();
                    return;
                }
                if (id != -1L) {
                    ItemID.blacklistIID(id, "random mask: " + player.getName());
                }
            }
            player.setItemInHand(null);
            MaskType type = this.getRandomMask();
            player.setItemInHand(type.createMask(true));
            Bukkit.broadcastMessage(CC.YellowB + "(!) " + CC.Yellow + player.getName() + " has received a " + type.getMaskData().getDisplayName() + CC.Yellow + " from their Mask Generator!");
            player.updateInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.1f);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() != null && event.getItemInHand().getType() == Material.SKULL_ITEM && new NBTWrapper(event.getItemInHand()).getString("cosmicType").equals("COSMIC_MASK")) {
            event.setCancelled(true);
            event.getPlayer().sendMessage(CC.RedB + "(!) " + CC.Red + "You can only apply this Mask to a helmet!");
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamageOther(EntityDamageByEntityEvent event) throws Exception {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            MaskType equipped = MaskType.fromPlayer(player);
            if (equipped == MaskType.PURGE) {
                event.setDamage(event.getDamage() * 1.025);
            } else if (equipped == MaskType.DRAGON_MASK) {
                event.setDamage(event.getDamage() * 1.05);
            } else if (equipped == MaskType.PARTY_HAT) {
                event.setDamage(event.getDamage() * 1.04);
            } else if (equipped == MaskType.DEATH_KNIGHT) {
                event.setDamage(event.getDamage() * 1.025);
            }
        }
        if (event.getEntity() instanceof Player) {
            MaskType found = MaskType.fromPlayer((Player) event.getEntity());
            if (Math.random() <= 0.02 && found == MaskType.TURKEY) {
                event.setCancelled(true);
                event.getEntity().sendMessage(CC.YellowB + "* DODGED [" + CC.Gray + "Turkey Mask" + CC.YellowB + "]");
                de.inventivegames.particle.ParticleEffect.CLOUD.sendToPlayers(Bukkit.getOnlinePlayers(), event.getEntity().getLocation().add(0.0, 0.5, 0.0), 0.2f, 0.2f, 0.2f, 0.1f, 10);

            } else if (found == MaskType.MONOPOLY || found == MaskType.PARTY_HAT) {
                event.setDamage(event.getDamage() * 0.95);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        Player killer;
        if (event.getEntity() instanceof Player) {
            return;
        }
        if (event.getEntity().getKiller() != null && MaskType.PILGRIM.hasEquipped(killer = event.getEntity().getKiller())) {
            event.setDroppedExp((int) ((double) event.getDroppedExp() * 1.25));
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerFoodLoss(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player && event.getFoodLevel() < ((Player) event.getEntity()).getFoodLevel() && MaskType.SCARECROW.hasEquipped((Player) event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onDamageByFire(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && MaskType.DRAGON_MASK.hasEquipped((Player) event.getEntity()) && (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA)) {
            event.setCancelled(true);
            event.setDamage(0.0);
        }
    }

    @EventHandler(priority=EventPriority.HIGH, ignoreCancelled=true)
    public void onPlayerEquip(InventoryClickEvent event) {
        ItemStack applying;
        ItemStack cursor = event.getCursor();
        boolean applyingInvalidHelmet = cursor != null && this.isInvalid(cursor) && (event.getClick().isLeftClick() || event.getClick().isRightClick()) && event.getSlotType() == InventoryType.SlotType.ARMOR;
        if (!applyingInvalidHelmet && event.getClick() == ClickType.NUMBER_KEY && (applying = event.getWhoClicked().getInventory().getItem(event.getHotbarButton())) != null && this.isInvalid(applying) && event.getSlotType() == InventoryType.SlotType.ARMOR) {
            applyingInvalidHelmet = true;
        }
        if (applyingInvalidHelmet) {
            Player player = (Player) event.getWhoClicked();
            if (player.isOp()) {
                player.sendMessage(CC.Red + "Allowing you to equip this due to your OP status.");
                return;
            }
            event.setCancelled(true);
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot wear this item!");
        }
    }

    private boolean isInvalid(ItemStack clicked) {
        NBTWrapper wrapper;
        Material type = clicked.getType();
        return type == Material.PUMPKIN || type == Material.JACK_O_LANTERN || type == Material.SKULL_ITEM && (!(wrapper = new NBTWrapper(clicked)).hasKey("cosmicType") || wrapper.getString("cosmicType").equalsIgnoreCase("inventory_pet"));
    }

    private boolean isValidWorld(String world) {
        return world.equalsIgnoreCase("world") || world.equalsIgnoreCase("world_nether") || world.contains("_end");
    }



    @EventHandler(priority = EventPriority.HIGH)
    public void onInventoryClick(InventoryClickEvent event) {
        MaskType type2;
        NBTWrapper clickedWrapper;
        String mask2;
        NBTWrapper cursorWrapper;
        if (event.getClickedInventory() == null || !(event.getWhoClicked() instanceof Player)) {
            return;
        }


        Player player = (Player) event.getWhoClicked();
        if (!event.getClickedInventory().getName().equals("container.inventory")) {
            return;
        }

        if (event.getSlotType() != InventoryType.SlotType.CONTAINER && event.getSlotType() != InventoryType.SlotType.QUICKBAR && event.getSlotType() != InventoryType.SlotType.ARMOR) {
            return;
        }
        ItemStack clicked = event.getCurrentItem();
        Material clickedType = clicked != null ? clicked.getType() : null;
        ItemStack cursor = event.getCursor();
        Material cursorType = null;
        if (cursor != null && (cursorType = cursor.getType()) != Material.AIR && event.getClick() == ClickType.LEFT && cursorType == Material.SKULL_ITEM && clickedType != null && this.isHelmet(clickedType) && (cursorWrapper = new NBTWrapper(cursor)).getString("cosmicType").equals("COSMIC_MASK")) {
            NBTWrapper other = new NBTWrapper(clicked);
            String mask = new NBTTagCompoundBuilder(other.getNBTBase("conaxData")).getString("maskOverride");
            if (mask != null && !mask.isEmpty()) {
                event.setCancelled(true);
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You can only equip a single mask on a helmet!");
                return;
            }
            if (clicked.getAmount() > 1) {
                event.setCancelled(true);
                player.sendMessage(CC.RedB + "(!)" + CC.Red + " You cannot equip a mask on stacked items!");
                return;
            }
            if (!this.isValidWorld(player.getWorld().getName())) {
                event.setCancelled(true);
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You can only attach a Mask in the main world!");
                return;
            }
            if (cursor.getAmount() > 1) {
                event.setCancelled(true);
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot apply stacked Masks!");
                return;
            }
            mask = new NBTTagCompoundBuilder(cursorWrapper.getNBTBase("conaxData")).getString("maskType");
            MaskType type = MaskType.getFromName(mask.toUpperCase());
            if (type == null) {
                return;
            }
            event.setCancelled(true);
            event.setCurrentItem(type.applyMaskNBT(cursorWrapper, other));
            player.setItemOnCursor(null);
            player.updateInventory();
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 2.0f, 1.4f);
            player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 3.0f, 1.1f);
            return;
        }
        if (!(clicked == null || clickedType == Material.AIR || event.getClick() != ClickType.RIGHT || cursorType != null && cursorType != Material.AIR || (mask2 = new NBTTagCompoundBuilder((clickedWrapper = new NBTWrapper(clicked)).getNBTBase("conaxData")).getString("maskOverride")) == null || mask2.isEmpty() || (type2 = MaskType.getFromName(mask2.toUpperCase())) == null)) {
            if (!this.isValidWorld(player.getWorld().getName())) {
                event.setCancelled(true);
                player.sendMessage(CC.RedB + "(!) " + CC.Red + "You can only un-attach a Mask in the main world!");
                return;
            }

            if (event.getClickedInventory() != event.getWhoClicked().getInventory()) return;

            event.setCancelled(true);
            if (type2 == MaskType.MULTI_MASK) {
                String[] masks = MultiMaskItem.listToString(MultiMaskItem.getMasks(clickedWrapper)).split(",");
                player.setItemOnCursor(new CustomItemBuilder("multi_mask", masks).build());
            } else {
                player.setItemOnCursor(type2.createMask(true));
            }
            event.setCurrentItem(type2.removeMaskNBT(clickedWrapper, clickedType));
            player.updateInventory();
            player.playSound(player.getLocation(), Sound.ANVIL_USE, 3.0f, 1.1f);
            player.playSound(player.getLocation(), Sound.HORSE_ARMOR, 3.0f, 1.3f);
        }
    }

    private boolean isHelmet(Material material) {
        return material == Material.LEATHER_HELMET || material == Material.DIAMOND_HELMET || material == Material.GOLD_HELMET || material == Material.IRON_HELMET || material == Material.CHAINMAIL_HELMET;
    }
}

