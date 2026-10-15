package com.rit.sucy.enchanting;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import com.rit.sucy.config.LanguageNode;
import com.rit.sucy.config.RootConfig;
import com.rit.sucy.config.RootNode;
import com.rit.sucy.service.ENameParser;
import com.rit.sucy.service.PermissionNode;
import lombok.SneakyThrows;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.enchantments.armor.Reflect;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockEvent;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.enchantment.PrepareItemEnchantEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.EnchantingInventory;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class EListener implements Listener {
    public static Material[] armor;
    EnchantmentAPI plugin;
    Hashtable<String, TableTask> tasks;
    public static boolean excuse;
    public static boolean cosmicOutposts;

    public EListener(final EnchantmentAPI plugin) {
        this.tasks = new Hashtable<String, TableTask>();
        plugin.getServer().getPluginManager().registerEvents((Listener) this, (Plugin) plugin);
        this.plugin = plugin;

        Bukkit.getScheduler().runTaskLater((Plugin) plugin, (Runnable) new Runnable() {
            @Override
            public void run() {
                EListener.cosmicOutposts = true;
                Bukkit.getLogger().info("[EnchantmentAPI] Outposts = " + EListener.cosmicOutposts);
            }
        }, 20L);
    }

    @SneakyThrows
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHit(final EntityDamageByEntityEvent event) {
        if (EListener.excuse) {
            EListener.excuse = false;
            return;
        }
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        boolean offenseProcs = true;
        if (event.getDamager().hasMetadata("shootForce") && event.getDamager().getMetadata("shootForce").get(0).asFloat() < 0.75f) {
            offenseProcs = false;
        }
        final LivingEntity damaged = (LivingEntity) event.getEntity();
        if (event.getDamager() instanceof Projectile && !(((Projectile) event.getDamager()).getShooter() instanceof LivingEntity)) {
            return;
        }
        final LivingEntity damager = (event.getDamager() instanceof LivingEntity) ? (LivingEntity) event.getDamager() : ((event.getDamager() instanceof Projectile && ((Projectile) event.getDamager()).getShooter() instanceof LivingEntity) ? (LivingEntity) ((Projectile) event.getDamager()).getShooter() : null);
        if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.PROJECTILE) {
            return;
        }
        if (damager != null && offenseProcs) {
            if (damager instanceof Player) {
                final Player pDamager = (Player) damager;
                final int reflectLevel = this.getReflectLevel(damaged, "normal");
                final int heroicLevel = this.getReflectLevel(damaged, "heroic");
                final int mastery = this.getReflectLevel(damaged, "mastery");
                for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments(pDamager.getItemInHand(), pDamager).entries()) {
                    final CustomEnchantment enchant = entry.getKey();
                    final int tier = enchant.getTier();
                    int reflectUsing = 0;
                    boolean reflect = false;
                    if (mastery > 0 && tier == 8 && mastery >= entry.getValue()) {
                        reflectUsing = mastery;
                    } else if (heroicLevel > 0 && tier <= 7 && heroicLevel >= entry.getValue()) {
                        reflectUsing = heroicLevel;
                    } else if (reflectLevel > 0 && tier <= 5 && reflectLevel >= entry.getValue()) {
                        reflectUsing = reflectLevel;
                    }
                    if (reflectUsing > 0 && 0.02 + 0.01 * (reflectUsing / 3) >= Math.random()) {
                        reflect = true;
                    }

                    if (reflect) {
                        if (!(damaged instanceof Player)) {
                            continue;
                        }
                        if (damaged.hasMetadata("inInvisibleMaze") || damager.hasMetadata("inInvisibleMaze")) {
                            continue;
                        }
                        enchant.applyEffect(damaged, damager, entry.getValue(), event);
                    } else {
                        if (damaged.hasMetadata("inInvisibleMaze") || damager.hasMetadata("inInvisibleMaze")) {
                            continue;
                        }
                        enchant.applyEffect(damager, damaged, entry.getValue(), event);
                    }
                }
            } else {
                for (final Map.Entry<CustomEnchantment, Integer> entry2 : getValidEnchantments(damager, this.getItems(damager)).entries()) {
                    if (!damager.hasMetadata("inInvisibleMaze") && !damaged.hasMetadata("inInvisibleMaze")) {
                        entry2.getKey().applyEffect(damager, damaged, entry2.getValue(), event);
                    }
                }
            }
        }
        final boolean silenced = damaged.hasMetadata("noDefenseProcs");
        boolean riftMask = false;
        int masteryReflect = 0;
        if (damager instanceof Player) {
            riftMask = MaskType.hasEquipped((Player) damager, "RIFT");
            masteryReflect = Reflect.getReflectLevel(damager, "mastery");
        }
        for (final Map.Entry<CustomEnchantment, Integer> entry3 : getValidEnchantments(damaged, this.getItems(damaged)).entries()) {
            if (silenced && !entry3.getKey().name().equals("Phoenix")) {
                continue;
            }
            if (entry3.getKey() instanceof MasteryCustomEnchantment) {
                if (riftMask && Math.random() <= 0.5) {
                    continue;
                }
                if (masteryReflect > entry3.getValue() && Math.random() <= 0.25) {
                    continue;
                }
            }
            if (damaged.hasMetadata("inInvisibleMaze")) {
                continue;
            }
            entry3.getKey().applyDefenseEffect(damaged, damager, entry3.getValue(), (EntityDamageEvent) event);
        }
    }

    public int getReflectLevel(final LivingEntity player, final String key) {
        final List<MetadataValue> found = (List<MetadataValue>) player.getMetadata(key + "_reflect_enchant");
        if (found != null && !found.isEmpty()) {
            return found.get(0).asInt();
        }
        return 0;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamaged(final EntityDamageEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        final LivingEntity damaged = (LivingEntity) event.getEntity();
        if (!(damaged instanceof Player) && ((CraftLivingEntity) damaged).getHandle().fromMobSpawner) {
            return;
        }
        final boolean silenced = damaged.hasMetadata("noDefenseProcs");
        for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments(damaged, this.getItems(damaged)).entries()) {
            if (silenced && !entry.getKey().name().equals("Phoenix") && Math.random() < 0.5) {
                continue;
            }
            if (damaged.hasMetadata("inInvisibleMaze")) {
                continue;
            }
            entry.getKey().applyDefenseEffect(damaged, null, entry.getValue(), event);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamaged(final EntityDamageByBlockEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) {
            return;
        }
        final LivingEntity damaged = (LivingEntity) event.getEntity();
        for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments(damaged, this.getItems(damaged)).entries()) {
            if (!damaged.hasMetadata("inInvisibleMaze")) {
                entry.getKey().applyDefenseEffect(damaged, null, entry.getValue(), (EntityDamageEvent) event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDamageBlock(final BlockDamageEvent event) {
        if (event.getItemInHand().getType() != Material.DIAMOND_PICKAXE && event.getItemInHand().getType() != Material.IRON_PICKAXE) {
            return;
        }
        for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments((LivingEntity) event.getPlayer(), this.getItemInHand((LivingEntity) event.getPlayer())).entries()) {
            if (!event.getPlayer().hasMetadata("inInvisibleMaze")) {
                entry.getKey().applyToolEffect(event.getPlayer(), event.getBlock(), entry.getValue(), (BlockEvent) event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBreakBlock(final BlockBreakEvent event) {
        for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments((LivingEntity) event.getPlayer(), this.getItemInHand((LivingEntity) event.getPlayer())).entries()) {
            if (!event.getPlayer().hasMetadata("inInvisibleMaze")) {
                entry.getKey().applyToolEffect(event.getPlayer(), event.getBlock(), entry.getValue(), (BlockEvent) event);
            }
        }
        if (event.getBlock().getType() == Material.ENCHANTMENT_TABLE) {
            for (final TableTask task : this.tasks.values()) {
                task.restore();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(final PlayerInteractEvent event) {
        if (event.hasBlock() && event.hasItem() && (event.getAction() == Action.LEFT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) && event.getClickedBlock().getType() == Material.OBSIDIAN) {
            for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments((LivingEntity) event.getPlayer(), this.getItemInHand((LivingEntity) event.getPlayer())).entries()) {
                if (!event.getPlayer().hasMetadata("inInvisibleMaze")) {
                    entry.getKey().applyMiscEffect(event.getPlayer(), entry.getValue(), event);
                }
            }
        }
        if (event.hasItem() && (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) && event.getItem().getMaxStackSize() == 1 && isArmor(event.getItem())) {
            new EEquip(event.getPlayer()).runTaskLater((Plugin) this.plugin, 1L);
        }
    }

    public static boolean isArmor(final ItemStack i) {
        for (final Material m : EListener.armor) {
            if (i.getType() == m) {
                return true;
            }
        }
        return false;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(final PlayerInteractEntityEvent event) {
        for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments((LivingEntity) event.getPlayer(), this.getItems((LivingEntity) event.getPlayer())).entries()) {
            if (!event.getPlayer().hasMetadata("inInvisibleMaze")) {
                entry.getKey().applyEntityEffect(event.getPlayer(), entry.getValue(), event);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEquip(final InventoryClickEvent event) {
        if (event.getInventory().getTitle().equals("container.crafting")) {
            if (event.getCurrentItem() == null && event.getCursor() == null) {
                return;
            }
            if (!event.isShiftClick()) {
                if (event.getSlotType() != InventoryType.SlotType.ARMOR) {
                    return;
                }
            } else if (event.getCursor() == null && event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR && event.getCurrentItem().getMaxStackSize() != 1) {
                return;
            }
            new EEquip(this.plugin.getServer().getPlayer(event.getWhoClicked().getName())).runTaskLater((Plugin) this.plugin, 1L);
        }
    }

    @EventHandler
    public void onPlayerRespawn(final PlayerRespawnEvent e) {
        new EEquip(e.getPlayer()).runTaskLater((Plugin) this.plugin, 1L);
    }

    @EventHandler
    public void onBreak(final PlayerItemBreakEvent event) {
        new EEquip(event.getPlayer()).runTaskLater((Plugin) this.plugin, 1L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onConnect(final PlayerJoinEvent event) {
        EEquip.loadPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDisconnect(final PlayerQuitEvent event) {
        EEquip.unequipPlayer(event.getPlayer(), false);
        final TableTask found = this.tasks.remove(event.getPlayer().getName());
        if (found != null) {
            found.cancel();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onProjectile(final ProjectileLaunchEvent event) {
        if (event.getEntity().getShooter() == null) {
            return;
        }
        if (event.getEntity().hasMetadata("shootForce") && event.getEntity().getMetadata("shootForce").get(0).asFloat() < 0.75f) {
            return;
        }
        if (event.getEntity().getShooter() instanceof Player) {
            final Player pShooter = (Player) event.getEntity().getShooter();
            for (final Map.Entry<CustomEnchantment, Integer> entry : getValidEnchantments(pShooter.getItemInHand(), pShooter).entries()) {
                if (!pShooter.hasMetadata("inInvisibleMaze")) {
                    entry.getKey().applyProjectileEffect((LivingEntity) pShooter, entry.getValue(), event);
                }
            }
        } else if (event.getEntity().getShooter() instanceof LivingEntity) {
            for (final Map.Entry<CustomEnchantment, Integer> entry2 : getValidEnchantments((LivingEntity) event.getEntity().getShooter(), this.getItems((LivingEntity) event.getEntity().getShooter())).entries()) {
                if (!((LivingEntity) event.getEntity().getShooter()).hasMetadata("inInvisibleMaze")) {
                    entry2.getKey().applyProjectileEffect((LivingEntity) event.getEntity().getShooter(), entry2.getValue(), event);
                }
            }
        }
    }

    @EventHandler
    public void onClick(final InventoryClickEvent event) {
        final Inventory inv = event.getInventory();
        if (event.getInventory().getType() == InventoryType.ENCHANTING && event.isShiftClick() && this.tasks.containsKey(event.getWhoClicked().getName())) {
            this.tasks.get(event.getWhoClicked().getName()).restore();
        }
    }

    @EventHandler
    public void onOpen(final InventoryOpenEvent event) {
        if (event.getInventory().getType() == InventoryType.ENCHANTING && event.getPlayer().hasPermission(PermissionNode.TABLE.getNode())) {
            this.tasks.put(event.getPlayer().getName(), new TableTask((Plugin) this.plugin, this.plugin.getServer().getPlayer(event.getPlayer().getName())));
        }
    }

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        if (event.getInventory() instanceof EnchantingInventory && this.tasks.containsKey(event.getPlayer().getName())) {
            this.tasks.get(event.getPlayer().getName()).restore();
            this.tasks.get(event.getPlayer().getName()).cancel();
            this.tasks.remove(event.getPlayer().getName());
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEnchant(final EnchantItemEvent event) {
        if (!event.getEnchanter().hasPermission(PermissionNode.TABLE.getNode()) || !this.tasks.containsKey(event.getEnchanter().getName())) {
            return;
        }
        event.setCancelled(true);
        if (EnchantmentAPI.getEnchantments(event.getItem()).size() > 0) {
            return;
        }
        if (event.getEnchanter().getLevel() < event.getExpLevelCost() && event.getEnchanter().getGameMode() != GameMode.CREATIVE) {
            return;
        }
        final ItemStack storedItem = this.tasks.get(event.getEnchanter().getName()).stored;
        if (storedItem.getAmount() > 1) {
            storedItem.setAmount(storedItem.getAmount() - 1);
            event.getEnchanter().getInventory().addItem(new ItemStack[]{storedItem.clone()});
            storedItem.setAmount(1);
        }
        final int maxEnchants = this.plugin.getModuleForClass(RootConfig.class).getInt(RootNode.MAX_ENCHANTS);
        final EnchantResult result = EEnchantTable.enchant(event.getEnchanter(), storedItem, event.getExpLevelCost(), maxEnchants);
        final ItemStack item = result.getItem();
        if (item == null) {
            return;
        }
        event.getInventory().clear();
        event.getEnchantsToAdd().clear();
        final boolean randomName = this.plugin.getModuleForClass(RootConfig.class).getBoolean(RootNode.ITEM_LORE);
        if (randomName && event.getEnchanter().hasPermission(PermissionNode.NAMES.getNode())) {
            String name = "\ufffd";
            int random = (int) (Math.random() * 14.0) + 49;
            if (random > 57) {
                random += 39;
            }
            String format = this.plugin.getConfig().getStringList(LanguageNode.NAME_FORMAT.getFullPath()).get(0);
            format = format.replace("{adjective}", this.plugin.getAdjective((result.getLevel() / 11 + 1 > 4) ? 4 : (result.getLevel() / 11 + 1)));
            format = format.replace("{weapon}", this.plugin.getWeapon(item.getType().name()));
            format = format.replace("{suffix}", this.plugin.getSuffix(result.getFirstEnchant()));
            name = name + (char) random + format;
            final ItemMeta meta = item.hasItemMeta() ? item.getItemMeta() : this.plugin.getServer().getItemFactory().getItemMeta(item.getType());
            meta.setDisplayName(name);
            item.setItemMeta(meta);
        }
        event.getInventory().addItem(new ItemStack[]{item});
        if (event.getEnchanter().getGameMode() != GameMode.CREATIVE) {
            event.getEnchanter().setLevel(event.getEnchanter().getLevel() - event.getExpLevelCost());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPrepareEnchant(final PrepareItemEnchantEvent event) {
        if (event.getItem() != null && event.getItem().hasItemMeta() && event.getItem().getItemMeta().hasLore() && event.getItem().getItemMeta().getLore().contains(this.tasks.get(event.getEnchanter().getName()).cantEnchant)) {
            event.setCancelled(true);
        }
    }

    public static Multimap<CustomEnchantment, Integer> getValidEnchantments(final LivingEntity le, final ArrayList<ItemStack> items) {
        final Multimap<CustomEnchantment, Integer> validEnchantments = ArrayListMultimap.create();
        for (final ItemStack item : items) {
            final ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                continue;
            }
            if (!meta.hasLore()) {
                continue;
            }
            for (final String lore : meta.getLore()) {
                final String name = ENameParser.parseName(lore);
                final int level = ENameParser.parseLevel(lore);
                if (name == null) {
                    continue;
                }
                if (level == 0) {
                    continue;
                }
                if (!EnchantmentAPI.isRegistered(name)) {
                    continue;
                }
                final CustomEnchantment ce = EnchantmentAPI.getEnchantment(name);
                if (EListener.cosmicOutposts) {
                    if (le.getWorld().getName().equals("world_outpost_pluto")) {
                        continue;
                    }
                }
                if (validEnchantments.containsKey(ce) && !enchantmentStacks(ce)) {
                    if ((int) validEnchantments.get(ce).toArray()[0] >= level) {
                        continue;
                    }
                    validEnchantments.removeAll(ce);
                }

                validEnchantments.put(ce, level);

            }
        }
        return validEnchantments;
    }

    @Deprecated
    public static Multimap<CustomEnchantment, Integer> getValidEnchantments(final ArrayList<ItemStack> items) {
        final Multimap<CustomEnchantment, Integer> validEnchantments = ArrayListMultimap.create();
        for (final ItemStack item : items) {
            final ItemMeta meta = item.getItemMeta();
            if (meta == null) {
                continue;
            }
            if (!meta.hasLore()) {
                continue;
            }
            for (final String lore : meta.getLore()) {
                final String name = ENameParser.parseName(lore);
                final int level = ENameParser.parseLevel(lore);
                if (name == null) {
                    continue;
                }
                if (level == 0) {
                    continue;
                }
                if (!EnchantmentAPI.isRegistered(name)) {
                    continue;
                }
                final CustomEnchantment ce = EnchantmentAPI.getEnchantment(name);
                if (validEnchantments.containsKey(ce) && !enchantmentStacks(ce)) {
                    if ((int) validEnchantments.get(ce).toArray()[0] >= level) {
                        continue;
                    }
                    validEnchantments.removeAll(ce);
                }
                validEnchantments.put(ce, level);
            }
        }
        return validEnchantments;
    }

    public static Multimap<CustomEnchantment, Integer> getValidEnchantments(final ItemStack item, final Player p) {
        final Multimap<CustomEnchantment, Integer> validEnchantments = ArrayListMultimap.create();
        final Multimap<CustomEnchantment, Integer> trimmedValidEnchantments = ArrayListMultimap.create();
        final ItemMeta meta = item.getItemMeta();
        if (meta == null) {
            return validEnchantments;
        }
        if (!meta.hasLore()) {
            return validEnchantments;
        }
        final int maxEnchantments = 20;
        for (final String lore : meta.getLore()) {
            final String name = ENameParser.parseName(lore);
            if (name != null) {
                if (!EnchantmentAPI.isRegistered(name)) {
                    continue;
                }
                final int level = ENameParser.parseLevel(lore);
                if (level == 0) {
                    continue;
                }
                final int tier = TieredEnchantments.getEnchantmentTier(name);
                if (EListener.cosmicOutposts) {
                    if (p.getWorld().getName().equals("world_outpost_pluto")) {
                        continue;
                    }
                }
                final CustomEnchantment ce = EnchantmentAPI.getEnchantment(name);
                if (validEnchantments.containsKey(ce) && !enchantmentStacks(ce)) {
                    if ((int) validEnchantments.get(ce).toArray()[0] >= level) {
                        continue;
                    }
                    validEnchantments.removeAll(ce);
                }

                validEnchantments.put(ce, level);

            }
        }
        //if (!p.getWorld().getName().startsWith("world_duels") && validEnchantments.size() > maxEnchantments) {
        if (validEnchantments.size() > maxEnchantments) {
            int maxRarity = 0;
            while (trimmedValidEnchantments.size() < maxEnchantments) {
                ++maxRarity;
                for (final Map.Entry<CustomEnchantment, Integer> ce2 : validEnchantments.entries()) {
                    if (trimmedValidEnchantments.size() == maxEnchantments) {
                        break;
                    }
                    if (TieredEnchantments.getEnchantmentTier(ce2.getKey().name()) > maxRarity) {
                        continue;
                    }
                    if (trimmedValidEnchantments.containsKey(ce2.getKey())) {
                        continue;
                    }
                    trimmedValidEnchantments.put(ce2.getKey(), ce2.getValue());
                }
            }
            if (!p.hasMetadata("lastWeaponAlert") || System.currentTimeMillis() - p.getMetadata("lastWeaponAlert").get(0).asLong() > 60000L) {
                String disabledEnchantments = "";
                int curRarity = 6;
                while (curRarity-- > 0) {
                    for (final CustomEnchantment ce3 : validEnchantments.keySet()) {
                        if (!trimmedValidEnchantments.containsKey(ce3) && TieredEnchantments.getEnchantmentTier(ce3.name()) == curRarity) {
                            disabledEnchantments = disabledEnchantments + TieredEnchantments.getTierColor(ce3) + ce3.name() + ", ";
                        }
                    }
                }
                if (disabledEnchantments.endsWith(", ")) {
                    disabledEnchantments = disabledEnchantments.substring(0, disabledEnchantments.length() - 2);
                }
                p.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " You are not skilled enough to fully use your weapon.");
                p.sendMessage(ChatColor.RED.toString() + ChatColor.UNDERLINE + "Locked:" + ChatColor.GRAY + " " + disabledEnchantments);
                if (maxEnchantments < 15) {
                    p.sendMessage(ChatColor.GRAY + "** Purchase a Rank at " + ChatColor.RED + ChatColor.UNDERLINE + "buy.astropvp.net" + ChatColor.GRAY + " to increase the maximum custom enchantments you can use!");
                }
                p.setMetadata("lastWeaponAlert", (MetadataValue) new FixedMetadataValue((Plugin) EnchantmentAPI.get(), System.currentTimeMillis()));
            }
            return trimmedValidEnchantments;
        }
        return validEnchantments;
    }

    private static boolean enchantmentStacks(final CustomEnchantment ce) {
        final String n = ce.name();
        return n.equals("Armored") || n.equals("Paladin Armored") || n.equals("Valor") || n.equals("Martyr Valor") || n.equals("Reinforced Tank") || n.equals("Tank") || n.equals("Angelic") || n.equals("Heavy") || n.equals("Anti Gravity") || n.equals("Aquatic") || n.equals("Drunk") || n.equals("Gears") || n.equals("Glowing") || n.equals("Obsidianshield") || n.equals("Overload") || n.equals("Springs");
    }

    private ArrayList<ItemStack> getItems(final LivingEntity entity) {
        final ItemStack weapon = entity.getEquipment().getItemInHand();
        final ArrayList<ItemStack> items = new ArrayList<ItemStack>(Arrays.asList(entity.getEquipment().getArmorContents()));
        items.add(weapon);
        return items;
    }

    private ArrayList<ItemStack> getItemInHand(final LivingEntity entity) {
        final ItemStack weapon = entity.getEquipment().getItemInHand();
        return new ArrayList<ItemStack>(Arrays.asList(weapon));
    }

    static {
        EListener.armor = new Material[]{Material.LEATHER_HELMET, Material.LEATHER_CHESTPLATE, Material.LEATHER_LEGGINGS, Material.LEATHER_BOOTS, Material.GOLD_HELMET, Material.GOLD_CHESTPLATE, Material.GOLD_LEGGINGS, Material.GOLD_BOOTS, Material.CHAINMAIL_HELMET, Material.CHAINMAIL_CHESTPLATE, Material.CHAINMAIL_LEGGINGS, Material.CHAINMAIL_BOOTS, Material.IRON_HELMET, Material.IRON_CHESTPLATE, Material.IRON_LEGGINGS, Material.IRON_BOOTS, Material.DIAMOND_HELMET, Material.DIAMOND_CHESTPLATE, Material.DIAMOND_LEGGINGS, Material.DIAMOND_BOOTS};
        EListener.excuse = false;
        EListener.cosmicOutposts = false;
    }
}
