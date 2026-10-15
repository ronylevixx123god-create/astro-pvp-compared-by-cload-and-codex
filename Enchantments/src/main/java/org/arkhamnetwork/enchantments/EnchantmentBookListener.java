package org.arkhamnetwork.enchantments;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagListBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.patches.ItemID;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.enchantments.util.TieredEnchantments;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class EnchantmentBookListener implements Listener {
    File f;

    public EnchantmentBookListener() {
        this.f = new File("no_enchant_limits");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onInventoryClick(final InventoryClickEvent e) throws Exception {
        if (!e.getInventory().getTitle().equals("container.crafting")) {
            return;
        }
        if (e.getCursor() != null && EnchantmentBook.isEnchantmentBook(e.getCursor())) {
            if (e.getRawSlot() < 9 && e.getRawSlot() >= 0) {
                final Player pl = (Player) e.getWhoClicked();
                pl.sendMessage(ChatColor.RED + "You cannot apply enchantments to your armor while it is equipped.");
                return;
            }
            if (e.getCurrentItem() != null) {
                final Player pl = (Player) e.getWhoClicked();
                final CustomEnchantment ce = EnchantmentBook.getEnchantment(e.getCursor());
                if (ce == null) {
                    return;
                }
                if (!ce.canEnchantOnto(e.getCurrentItem()) || e.getCurrentItem().getMaxStackSize() != 1) {
                    return;
                }
                final Material currentItemType = e.getCurrentItem().getType();
                if (currentItemType == Material.ENCHANTED_BOOK) {
                    return;
                }
                final ItemStack to_enchant = e.getCurrentItem();
                if (EnchantmentBook.getCustomEnchantmentCount(to_enchant) > EnchantmentBook.getMaxEnchantmentCountAllowed(pl) + EnchantmentOrb.getExtraEnchantmentSlots(to_enchant) && !EnchantmentAPI.itemHasEnchantment(to_enchant, ce.name()) && (!ce.heroicEnchantment || !EnchantmentAPI.itemHasEnchantment(to_enchant, ce.nonHeroicVersion.name()))) {
                    pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You are not skilled enough to add another enchantment to this item.");
                    pl.sendMessage(ChatColor.GRAY + "Purchase a Rank (or an Enchantment Orb) at " + ChatColor.RED + "buy.astropvp.net" + ChatColor.GRAY + " to increase your max. custom enchants per item!");
                    return;
                }
                final ItemStack ench_book = e.getCursor().clone();
                if (EnchantmentAPI.itemHasEnchantment(to_enchant, ce.name())) {
                    CustomEnchantment toCheck = ce;
                    final Map<CustomEnchantment, Integer> enchants = (Map<CustomEnchantment, Integer>) EnchantmentAPI.getEnchantments(to_enchant);
                    Integer level = enchants.get(toCheck);
                    if (level == null) {
                        if (toCheck.heroicEnchantment) {
                            toCheck = ce.nonHeroicVersion;
                        } else {
                            toCheck = ce.heroicVersion;
                        }
                        level = enchants.get(toCheck);
                    }
                    if (level == null) {
                        pl.sendMessage(ChatColor.RED + "Unable to apply enchantment to this item!");
                        return;
                    }
                    if (level >= EnchantmentBook.getEnchantmentLevel(ench_book)) {
                        pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "That item already has " + toCheck.name() + " " + level + "!");
                        return;
                    }
                }
                if (to_enchant.getAmount() > to_enchant.getMaxStackSize() && to_enchant.getAmount() > 1) {
                    pl.sendMessage(ChatColor.RED + "You cannot apply enchantments to stacked items!");
                    return;
                }
                if (!EnchantmentAPI.itemHasEnchantment(to_enchant, ce.name()) && ce.heroicEnchantment && ce.nonHeroicVersion != null) {
                    if (!EnchantmentAPI.itemHasEnchantment(to_enchant, ce.nonHeroicVersion.name())) {
                        pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " This is a heroic enchantment book. In order to apply it to an item, the item must have the max level of the non-heroic version (" + ce.nonHeroicVersion.name() + ") first.");
                        return;
                    }
                    final int nonHeroicLevel = EnchantmentAPI.getEnchantments(to_enchant).get(ce.nonHeroicVersion);
                    if (nonHeroicLevel < ce.nonHeroicVersion.getMaxLevel()) {
                        pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " This is a heroic enchantment book. In order to apply it to an item, the item must have the max level of the non-heroic version (" + ce.nonHeroicVersion.name() + ") first.");
                        return;
                    }
                }
                if (ce.heroicVersion != null && EnchantmentAPI.itemHasEnchantment(to_enchant, ce.heroicVersion.name())) {
                    pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD.toString() + "(!)" + ChatColor.RED + " This item already has the heroic version of " + ce.name() + " (" + ce.heroicVersion.name() + ") on it.");
                    return;
                }
                if (currentItemType != Material.AIR) {
                    final NBTWrapper clickedWrapper = new NBTWrapper(e.getCurrentItem());
                    final NBTTagCompoundBuilder cosmicData = clickedWrapper.getCompound("conaxData");
                    boolean allowed = false;
                    if (cosmicData != null && cosmicData.hasKey("noEnchants")) {
                        final NBTWrapper cursor = new NBTWrapper(e.getCursor());
                        final String abilitySet = cosmicData.getString("abilitySet");
                        final NBTTagCompoundBuilder builder;
                        if ((builder = cursor.getCompound("conaxData")) != null && builder.hasKey("masteryShard") && cosmicData.hasKey("abilitySet") && new NBTTagListBuilder(builder.getNBTBase("masteryShard")).contains(cosmicData.getString("abilitySet"))) {
                            allowed = true;
                        }
                        if (!allowed) {
                            pl.sendMessage(CC.RedB + "(!) " + CC.Red + "This armor is too powerful to modify its enchantments!");
                            pl.sendMessage(CC.Gray + "Use a " + CC.GrayU + StringUtils.capitaliseAllWords(abilitySet.toLowerCase().replace("_", " ")) + " Mastery Shard" + CC.Gray + " on this book to allow it to be applied!");
                            pl.sendMessage(CC.Gray + "Mastery Shards can be obtained by trading your Mastery Kit Items to the " + CC.GrayU + "/tinkerer" + CC.Gray + "!");
                            return;
                        }
                    }
                }

                //THIS IS WHERE YOU FIW APPLYING STACKED BOOKS

                if (pl.getItemOnCursor().getAmount() > 1) {
                    pl.getItemOnCursor().setAmount(pl.getItemOnCursor().getAmount()-1);
                } else {
                    pl.setItemOnCursor((ItemStack) null);

                }

                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                final boolean success = EnchantmentBook.applyEnchantmentBook(pl, ench_book, e.getCurrentItem());
                if (ArkPlugins.ITEM_ID.getArkPlugin().isRunning() && pl.getItemOnCursor() == null) {
                    final long itemID = ItemID.getItemID(ench_book);
                    if (itemID != -1L) {
                        ItemID.blacklistIID(itemID, "enchantmentBookUse");
                    }
                }
                if (success) {
                    pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 0.75f);
                    pl.playEffect(EntityEffect.WOLF_HEARTS);
                    final Location l = pl.getLocation();
                    de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);


                    //TODO: Check this first
                    /*if (ench_book.getAmount() > 1) {
                        ItemStack book = ench_book.clone();
                        book.setAmount(book.getAmount()-1);

                        pl.getInventory().addItem(book);
                    }*/

                    Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(pl, "APPLY_ENCHANT", new Object[]{ench_book, EnchantmentBook.getEnchantmentBookSuccessRate(ench_book)}));
                } else {
                    pl.playSound(pl.getLocation(), Sound.LAVA_POP, 1.0f, 0.75f);
                    pl.playEffect(EntityEffect.WOLF_HEARTS);
                    final Location l = pl.getLocation();
                    try {
                        de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.35f, 20);

                    } catch (Exception e2) {
                        e2.printStackTrace();
                    }
                }
                pl.updateInventory();
                Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(pl, "FAILED_ENCHANT", new Object[]{ench_book, EnchantmentBook.getEnchantmentBookSuccessRate(ench_book)}));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWhitescrollApply(final InventoryClickEvent e) {
        if (!e.getInventory().getTitle().equals("container.crafting")) {
            return;
        }
        if (e.getCursor() != null && EnchantmentBook.isWhitescroll(e.getCursor())) {
            if (e.getRawSlot() <= 3 && e.getRawSlot() >= 0 && e.getSlotType() != InventoryType.SlotType.QUICKBAR) {
                return;
            }
            if (e.getCurrentItem() != null) {
                final Player pl = (Player) e.getWhoClicked();
                if (!EnchantmentBook.isEquipment(e.getCurrentItem()) || EnchantmentBook.hasWhitescroll(e.getCurrentItem()) || e.getCurrentItem().getMaxStackSize() != 1) {
                    return;
                }
                if (e.getCursor().getAmount() > 1) {
                    final ItemStack whitescroll = e.getCursor().clone();
                    whitescroll.setAmount(whitescroll.getAmount() - 1);
                    pl.setItemOnCursor(whitescroll);
                } else {
                    pl.setItemOnCursor((ItemStack) null);
                }
                final ItemStack to_enchant = e.getCurrentItem();
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                EnchantmentBook.applyWhitescroll(to_enchant);
                pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 0.75f);
                pl.playEffect(EntityEffect.WOLF_HEARTS);
                final Location l = pl.getLocation();
                try {
                    de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.65f, 30);

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                pl.updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onRandomizationScrollApply(final InventoryClickEvent e) {
        if (!e.getInventory().getTitle().equals("container.crafting")) {
            return;
        }
        if (e.getCursor() != null && RandomizationScroll.isRandomizationScroll(e.getCursor())) {
            if (e.getRawSlot() <= 3 && e.getRawSlot() >= 0 && e.getSlotType() != InventoryType.SlotType.QUICKBAR) {
                return;
            }
            if (e.getCurrentItem() != null) {
                final Player pl = (Player) e.getWhoClicked();
                if (!EnchantmentBook.isEnchantmentBook(e.getCurrentItem())) {
                    return;
                }
                if (e.getCurrentItem().getAmount() > 1) {
                    pl.sendMessage(ChatColor.RED.toString() + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot apply scrolls to stacked books.");
                    return;
                }
                final int bookRarity = CosmicSpaceChestsAPI.getItemRarity(CosmicSpaceChestsAPI.getItemRarityByColor(e.getCursor()));
                if (bookRarity != 6 && bookRarity != CosmicSpaceChestsAPI.getItemRarity(CosmicSpaceChestsAPI.getItemRarityByColor(e.getCurrentItem()))) {
                    pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You need a(n) " + CosmicSpaceChestsAPI.getItemRarityByColor(e.getCurrentItem()).name + " rarity scroll to reroll that book.");
                    return;
                }
                final CustomEnchantment bookEnchant = EnchantmentBook.getEnchantment(e.getCurrentItem());
                if (bookEnchant.heroicEnchantment) {
                    pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot use Randomization Scrolls on Heroic Enchantment Books.");
                    return;
                }
                final ItemStack scroll = e.getCursor().clone();
                if (e.getCursor().getAmount() > 1) {
                    scroll.setAmount(scroll.getAmount() - 1);
                    pl.setItemOnCursor(scroll);
                } else {
                    pl.setItemOnCursor((ItemStack) null);
                }
                final ItemStack enchantmentBook = e.getCurrentItem();
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                RandomizationScroll.applyRandomizationScroll(enchantmentBook, scroll);
                pl.playSound(pl.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.75f);
                final Location l = pl.getLocation();
                try {
                    de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                    de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 0.75, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.68f, 10);

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                pl.updateInventory();
                Bukkit.getPluginManager().callEvent((Event) new CustomCosmicPlayerEvent(pl, "RAND_SCROLL_USE", new Object[]{bookRarity}));
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlackscrollApply(final InventoryClickEvent e) throws Exception {
        if (!e.getInventory().getTitle().equals("container.crafting")) {
            return;
        }
        boolean heroicBlackscroll = false;
        NBTWrapper wrapper = null;
        final Material type = (e.getCursor() != null) ? e.getCursor().getType() : null;
        if (e.getCursor() != null && ((heroicBlackscroll = (type == Material.INK_SACK && (wrapper = new NBTWrapper(e.getCursor())).hasKey("cosmicType") && wrapper.getString("cosmicType").equals("heroicBlackscroll"))) || EnchantmentBook.isBlackscroll(e.getCursor()))) {
            if (e.getRawSlot() <= 3 && e.getRawSlot() >= 0 && e.getSlotType() != InventoryType.SlotType.QUICKBAR) {
                return;
            }
            if (e.getCurrentItem() != null) {
                final Player pl = (Player) e.getWhoClicked();
                final ItemStack to_enchant = e.getCurrentItem();
                final Map<CustomEnchantment, Integer> allEnchants;
                if (!EnchantmentBook.isEquipment(to_enchant) || e.getCurrentItem().getMaxStackSize() != 1 || (allEnchants = (Map<CustomEnchantment, Integer>) EnchantmentAPI.getEnchantments(to_enchant)).isEmpty()) {
                    return;
                }
                if (e.getSlotType() == InventoryType.SlotType.ARMOR) {
                    e.setCancelled(true);
                    pl.sendMessage(CC.RedB + "(!) " + CC.Red + "Please remove your armor before attempting to Black Scroll it!");
                    return;
                }
                final ItemStack blackscroll = e.getCursor().clone();
                final List<CustomEnchantment> validEnchantsToExtract = new ArrayList<>();
                for (final CustomEnchantment enchant : allEnchants.keySet()) {
                    if (enchant instanceof MasteryCustomEnchantment) {
                        continue;
                    }
                    if (enchant.heroicEnchantment && !heroicBlackscroll) {
                        continue;
                    }
                    validEnchantsToExtract.add(enchant);
                }
                final Random random = ThreadLocalRandom.current();
                if (validEnchantsToExtract.isEmpty()) {
                    pl.sendMessage(ChatColor.RED + "You can only apply a Black Scroll to an item that contains non heroic enchantments!");
                    return;
                }

                final NBTWrapper clickedWrapper = new NBTWrapper(e.getCurrentItem());
                final NBTTagCompoundBuilder cosmicData = clickedWrapper.getCompound("conaxData");
                if (cosmicData != null && cosmicData.hasKey("noEnchants")) {
                    pl.sendMessage(CC.RedB + "(!) " + CC.Red + "This armor is too powerful to have its enchantments extracted by black scrolls!");
                    return;
                }

                CustomEnchantment ce = validEnchantsToExtract.get(random.nextInt(validEnchantsToExtract.size()));
                if (ce.heroicEnchantment && !heroicBlackscroll) {
                    ce = ce.nonHeroicVersion;
                }
                final int level = allEnchants.get(ce);
                int blackscrollMod = 0;

                if (level > ce.getMaxLevel()) {
                    pl.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot blackscroll an admin item enchant!");
                    return;
                }

                int chance;
                if (heroicBlackscroll && wrapper.hasKey("conaxData")) {
                    final NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder(wrapper.getNBTBase("conaxData"));
                    final int minChance = builder.getInt("min");
                    int newMaxChance;
                    final int maxChance = newMaxChance = builder.getInt("max");
                    final int dif = maxChance - minChance;
                    if (random.nextBoolean() && minChance < 100) {
                        newMaxChance = Math.max(minChance, maxChance / 2);
                        if (dif > 5 && maxChance == minChance) {
                            newMaxChance += random.nextInt(5);
                            if (newMaxChance <= maxChance) {
                                newMaxChance = maxChance;
                            }
                        }
                    }
                    chance = minChance + random.nextInt(newMaxChance - minChance + 1);
                } else {
                    chance = EnchantmentBook.getBlackScrollBookSuccessRate(blackscroll) + blackscrollMod;
                }
                if (pl.hasMetadata("blackscrollPet_Effect")) {
                    final int petLevel = pl.getMetadata("blackscrollPet_Effect").get(0).asInt();
                    chance += petLevel;
                    pl.removeMetadata("blackscrollPet_Effect", Bukkit.getPluginManager().getPlugin("CosmicSkyInventoryPets"));
                }

                if (e.getCursor().getAmount() > 1) {
                    blackscroll.setAmount(blackscroll.getAmount() - 1);
                    pl.setItemOnCursor(blackscroll);
                } else {
                    if (heroicBlackscroll && ArkPlugins.ITEM_ID.getArkPlugin().isRunning()) {
                        final long itemID = ItemID.getItemID(blackscroll);
                        if (itemID != -1L) {
                            ItemID.blacklistIID(itemID, "blackScrollUse");
                        }
                    }
                    pl.setItemOnCursor((ItemStack) null);
                }
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);

                e.setCurrentItem(ce.removeFromItem(e.getCurrentItem()));
                final ItemStack book = EnchantmentBook.getEnchantmentBook(ce, level, 100, 0);
                PlayerUtil.giveOrDropProtectedItem(pl, book, -1);
                pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 0.75f);
                pl.playEffect(EntityEffect.WOLF_HEARTS);
                final Location l = pl.getLocation();
                de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.65f, 30);
                pl.updateInventory();
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onMagicDustApply(final InventoryClickEvent e) {
        if (!e.getInventory().getTitle().equals("container.crafting")) {
            return;
        }
        if (e.getCursor() != null && EnchantmentBook.isMagicDust(e.getCursor())) {
            if (e.getRawSlot() <= 3 && e.getRawSlot() >= 0 && e.getSlotType() != InventoryType.SlotType.QUICKBAR) {
                return;
            }
            if (e.getCurrentItem() != null) {
                final Player pl = (Player) e.getWhoClicked();
                if (!EnchantmentBook.isEnchantmentBook(e.getCurrentItem()) || EnchantmentBook.getEnchantmentBookSuccessRate(e.getCurrentItem()) >= 100 || e.getCurrentItem().getAmount() > 1) {
                    return;
                }
                final ItemRarity bookRarity = CosmicSpaceChestsAPI.getItemRarity(TieredEnchantments.getEnchantmentTier(EnchantmentBook.getEnchantment(e.getCurrentItem()).name()));
                ItemRarity dustRarity = ItemRarity.valueOf(ChatColor.stripColor(e.getCursor().getItemMeta().getDisplayName().split(" ")[0].toUpperCase()));
                if (dustRarity == ItemRarity.SOUL) {
                    dustRarity = ItemRarity.GOD;
                }
                if (dustRarity == ItemRarity.SIMPLE && !e.getCursor().getItemMeta().getDisplayName().startsWith(ChatColor.WHITE.toString())) {
                    return;
                }
                if (dustRarity == ItemRarity.UNIQUE && !e.getCursor().getItemMeta().getDisplayName().startsWith(ChatColor.GREEN.toString())) {
                    return;
                }
                if (dustRarity == ItemRarity.ELITE && !e.getCursor().getItemMeta().getDisplayName().startsWith(ChatColor.AQUA.toString())) {
                    return;
                }
                if (dustRarity == ItemRarity.ULTIMATE && !e.getCursor().getItemMeta().getDisplayName().startsWith(ChatColor.YELLOW.toString())) {
                    return;
                }
                if (dustRarity == ItemRarity.LEGENDARY && !e.getCursor().getItemMeta().getDisplayName().startsWith(ChatColor.GOLD.toString())) {
                    return;
                }
                if (dustRarity == ItemRarity.SOUL && !e.getCursor().getItemMeta().getDisplayName().startsWith(ChatColor.RED.toString())) {
                    return;
                }
                if (bookRarity != dustRarity) {
                    return;
                }
                final CustomEnchantment bookEnchantment = EnchantmentBook.getEnchantment(e.getCurrentItem());
                if (bookEnchantment.heroicEnchantment) {
                    pl.sendMessage(ChatColor.RED + "" + ChatColor.BOLD + "(!) " + ChatColor.RED + "You cannot apply Magic Dust to Heroic Enchantment Books.");
                    return;
                }
                final ItemStack is_dust = e.getCursor().clone();
                if (e.getCursor().getAmount() > 1) {
                    final ItemStack dust = e.getCursor().clone();
                    dust.setAmount(dust.getAmount() - 1);
                    pl.setItemOnCursor(dust);
                } else {
                    pl.setItemOnCursor((ItemStack) null);
                }
                final ItemStack to_enchant = e.getCurrentItem();
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                e.setCurrentItem(EnchantmentBook.applyDust(is_dust, to_enchant));
                pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 0.75f);
                pl.playEffect(EntityEffect.WOLF_HEARTS);
                final Location l = pl.getLocation();
                try {
                    de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(), l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.65f, 30);

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                pl.updateInventory();
            }
        }
    }

    @EventHandler
    public void onSecretDustExamine(final PlayerInteractEvent e) {
        final Player pl = e.getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.hasItem() && EnchantmentBook.isSecretDust(e.getItem())) {
            if (pl.getInventory().firstEmpty() == -1) {
                pl.sendMessage(ChatColor.RED + "You need an empty inventory slot to examine secret dust.");
                return;
            }
            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
            ItemStack newDust = null;
            if (Math.random() < 0.75) {
                newDust = EnchantmentBook.getJunkDust();
                pl.playSound(pl.getLocation(), Sound.LAVA_POP, 1.0f, 0.96f);
                final Location l = pl.getLocation();
                try {
                    de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(),l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.35f, 20);

                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            } else {
                final ItemRarity ir = ItemRarity.valueOf(ChatColor.stripColor(e.getItem().getItemMeta().getDisplayName().split(" ")[0].toUpperCase()));
                final boolean primalDust = Math.random() < 0.05;
                int successAdd = new Random().nextInt(EnchantmentBook.getSecretDustMaxSuccessIncrease(e.getItem())) + 1;
                if (primalDust) {
                    successAdd = Math.max(10, successAdd * 3);
                }
                if (primalDust) {
                    newDust = EnchantmentBook.getPlatinumMagicDust(ir, successAdd);
                } else {
                    newDust = EnchantmentBook.getMagicDust(ir, successAdd);
                }
                final Location i = pl.getLocation();
                try {
                    de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(),i.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(),i.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.65f, 30);

                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                pl.playSound(pl.getLocation(), Sound.ORB_PICKUP, 1.0f, 0.96f);
            }
            if (e.getItem().getAmount() > 1) {
                final ItemStack secretDust = e.getItem();
                secretDust.setAmount(secretDust.getAmount() - 1);
                pl.setItemInHand(secretDust);
            } else {
                pl.setItemInHand((ItemStack) null);
            }
            pl.getInventory().addItem(new ItemStack[]{newDust});
            pl.updateInventory();
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onTransmogscrollApply(final InventoryClickEvent e) {
        if (!e.getInventory().getTitle().equals("container.crafting")) {
            return;
        }
        if (e.getCursor() != null && EnchantmentBook.isTransmogScroll(e.getCursor())) {
            if (e.getRawSlot() <= 3 && e.getRawSlot() >= 0 && e.getSlotType() != InventoryType.SlotType.QUICKBAR) {
                return;
            }
            if (e.getCurrentItem() != null) {
                final Player pl = (Player) e.getWhoClicked();
                if (!EnchantmentBook.isEquipment(e.getCurrentItem()) || e.getCurrentItem().getMaxStackSize() != 1) {
                    return;
                }
                if (e.getCursor().getAmount() > 1) {
                    final ItemStack whitescroll = e.getCursor().clone();
                    whitescroll.setAmount(whitescroll.getAmount() - 1);
                    pl.setItemOnCursor(whitescroll);
                } else {
                    pl.setItemOnCursor((ItemStack) null);
                }
                final ItemStack to_enchant = e.getCurrentItem();
                e.setCancelled(true);
                e.setResult(Event.Result.DENY);
                EnchantmentBook.applyTransmogScroll(to_enchant);
                pl.playSound(pl.getLocation(), Sound.LEVEL_UP, 1.0f, 0.75f);
                pl.playEffect(EntityEffect.WOLF_HEARTS);
                final Location l = pl.getLocation();
                try {
                    de.inventivegames.particle.ParticleEffect.SPELL.sendToPlayers(Bukkit.getOnlinePlayers(),l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.8f, 85);
                    de.inventivegames.particle.ParticleEffect.MAGIC_CRIT.sendToPlayers(Bukkit.getOnlinePlayers(),l.add(0.0, 1.0, 0.0), new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat(), 0.65f, 30);
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                pl.updateInventory();
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(final PlayerInteractEvent e) {
        final Player pl = e.getPlayer();
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.hasItem() && EnchantmentBook.isEnchantmentBook(e.getItem())) {
            if (e.getItem().getDurability() != 0) {
                e.getItem().setDurability((short) 0);
            }
            pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "To apply this enchantment to an item, simply drag n' drop the book onto the item you'd like to enchant in your inventory!");
            pl.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7The &bSuccess Rate&7 is the chance of the book successfully being applied to your equipment. The &bDestruction Chance&7 is the percent chance of your piece of equipment being &nDESTROYED&7 if the book fails to apply."));
            pl.updateInventory();
        }
        if ((e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) && e.hasItem() && EnchantmentBook.isWhitescroll(e.getItem())) {
            e.setCancelled(true);
            e.setUseInteractedBlock(Event.Result.DENY);
            e.setUseItemInHand(Event.Result.DENY);
            pl.sendMessage(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "To apply this White Scroll to an item, simply drag n' drop the scroll onto the item you'd like to protect in your inventory!");
            pl.updateInventory();
        }
    }
}
