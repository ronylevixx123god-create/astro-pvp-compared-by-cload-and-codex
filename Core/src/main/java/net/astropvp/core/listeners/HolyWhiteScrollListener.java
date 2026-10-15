package net.astropvp.core.listeners;


import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.boots.MutatedSinkOrSwipFlippers;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicPersistentItem;
import net.astropvp.CollectPlugin;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.HolyWhiteScrollUtils;
import net.astropvp.struct.CollectCategory;
import net.astropvp.struct.CollectType;
import org.arkhamnetwork.Arkkit.patches.ExpBottles;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class HolyWhiteScrollListener implements Listener {
    private final HashMap<String, List<ItemStack>> savedItems = new HashMap<>();

    public HashMap<String, List<ItemStack>> getSavedItems() {
        return this.savedItems;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerApplyHolyWhiteScroll(InventoryClickEvent e) {
        if (e.getClickedInventory() == null || e.getWhoClicked().getOpenInventory() == null || e.getWhoClicked().getOpenInventory().getBottomInventory() == null || !e.getClickedInventory().equals(e.getWhoClicked().getOpenInventory().getBottomInventory()))
            return;
        if (e.getCursor() != null && HolyWhiteScrollUtils.isOldHolyWhiteScroll(e.getCursor()))
            try {
                NBTWrapper cursor = new NBTWrapper(e.getCursor());
                if (cursor.getconaxData() != null)
                    return;
                NBTWrapper wrapper = (e.getCurrentItem() != null && e.getCurrentItem().getType() != Material.AIR) ? new NBTWrapper(e.getCurrentItem()) : null;
                AstroCore.getInstance().getHolyWhitescrollItem().onInventoryClick((Player) e.getWhoClicked(), e.getCursor(), cursor, wrapper, e.getCurrentItem(), e);
            } catch (CosmicException e2) {
                e.getWhoClicked().sendMessage(e2.getMessage());
                e.setCancelled(true);
            }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (e.getEntity().getKiller() == null)
            return;
        if (e.getKeepInventory())
            return;


        if (!e.getEntity().getLocation().getWorld().getName().equals("world") && !e.getEntity().getLocation().getWorld().getName().equals("flat") && !e.getEntity().getLocation().getWorld().getName().equals("world_ruins"))
            return;
        if (!e.getEntity().getKiller().getLocation().getWorld().getName().equals("world") && !e.getEntity().getKiller().getLocation().getWorld().getName().equals("flat") && !e.getEntity().getKiller().getLocation().getWorld().getName().equals("world_ruins"))
            return;


        Player killer = e.getEntity().getKiller();
        Player killed = e.getEntity();

        boolean wearingMonopolyMask = isWearingMonopoly(killer);
        boolean hasBeenSwimEffected = false;
        boolean hasBeenTailed = false;
        List<ItemStack> savedItems = new ArrayList<>();
        List<ItemStack> drops = e.getDrops();
        List<ItemStack> toRemoveToDrops = new ArrayList<>();
        List<ItemStack> toAddToDrops = new ArrayList<>();

        System.out.println("running holies event");
        for (ItemStack i : drops) {

            if (HolyWhiteScrollUtils.itemHasHolyWhitescroll(i)) {
                boolean mask = false;
                HolyWhiteScrollUtils.removeHolyWhitescroll(i);
                boolean killerIsControllingFrozen = (FactionUtils.getFaction(killer).isNormal() && HCFOutpostAPI.isControllingOutpost(FactionUtils.getFaction(killer), OutpostType.THE_GREAT_HALL));
                boolean isBansheed = e.getEntity().hasMetadata("banshee_scrolls");
                boolean isUltraad = false;
                double monoChance = getMonoChance(killer, killerIsControllingFrozen);
                if (AstroArmorSetsAPI.getBonusCrystalsEquipped(e.getEntity().getPlayer()).containsKey(ArmorSetType.ASTRO))
                    monoChance *= 1.05;
                if (AstroArmorSetsAPI.getCurrentArmorSet(e.getEntity().getPlayer()) != null) {
                    if (AstroArmorSetsAPI.getCurrentArmorSet(e.getEntity().getPlayer()) == ArmorSetType.ASTRO) {
                        monoChance *= 1.15;
                    }
                }
                if (AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.SUMMER_2021_BOOTS)) {
                    monoChance *= 1.15;
                }
                if ((AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.SPRING_BREAK_2022_MUTATED_HELMET)) || (AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.SPRING_BREAK_2022_HELMET))) {
                    monoChance *= 1.05;
                }
                boolean isMonopolied = (wearingMonopolyMask && Math.random() <= monoChance);

                if (MutatedSinkOrSwipFlippers.sinked.contains(e.getEntity()) && AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.SPRING_BREAK_2022_MUTATED_BOOTS) && !hasBeenSwimEffected) {
                    isMonopolied = true;
                    hasBeenSwimEffected = true;
                }

                if (AstroArmorSetsAPI.getCurrentArmorSet(killer) != null && AstroArmorSetsAPI.getCurrentArmorSet(killer) == ArmorSetType.ULTRA && Math.random() < 0.4) {
                    isUltraad = true;
                }

                if ((AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.EASTER_2022_MUTATED_BELT) || AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.EASTER_2022_BELT)) && !hasBeenTailed) {
                    isMonopolied = true;
                    hasBeenTailed = true;
                    killer.sendMessage(CC.translate("&e&l* MISCHEVIOUS TAIL: &b&l100% Monopoly e&l* "));
                }
                boolean isTravAxe = false;
                boolean isKilledHoldingTravAxe = false;
                if (killer != null && AstroArmorSetsAPI.getCurrentArmorSet(killer) == ArmorSetType.DIMENSIONAL_TRAVELER) {
                    boolean flag = false;
                    for (ItemStack item : killer.getInventory().getContents()) {
                        if (item != null && item.getType() == Material.DIAMOND_AXE && (new NBTWrapper(item)).getString("cosmicType").equals("TRAVELER_AXE"))
                            flag = true;
                    }
                    isTravAxe = (Math.random() < 0.5D && flag);
                }
                if (e.getEntity() != null && AstroArmorSetsAPI.getCurrentArmorSet(e.getEntity()) == ArmorSetType.DIMENSIONAL_TRAVELER)
                    for (ItemStack item2 : e.getEntity().getInventory().getContents()) {
                        if (item2 != null && item2.getType() == Material.DIAMOND_AXE && (new NBTWrapper(item2)).getString("cosmicType").equals("TRAVELER_AXE"))
                            isKilledHoldingTravAxe = true;
                    }
                assert killer != null;
                if (!isBansheed && !isMonopolied && !isTravAxe && !isUltraad) {
                    savedItems.add(i);
                    continue;
                }
                (new ItemStack[1])[0] = i;

                if (isMonopolied && !isKilledHoldingTravAxe) {
                    if (AstroItemSkinsAPI.hasSkinEquipped(Objects.requireNonNull(e.getEntity()).getPlayer(), SkinType.CHRISTMAS_HELMET_STAR) && Math.random() < 0.33) {
                        killer.sendMessage(CC.translate("&a&l* " + e.getEntity().getName() + " 's NORTH STAR &7Blocked Monopoly Mask &a&l* "));
                        e.getEntity().getPlayer().sendMessage(CC.translate("&a&l* NORTH STAR &7Saved your item from getting monoed &a&l*"));
                        savedItems.add(i);
                        continue;
                    }

                    //holies don't save in space lands
                    if (killer.getLocation().getWorld().getName().equals("space")) continue;
                    if (killer.getLocation().getWorld().getName().equals("mars")) continue;

                    if (AstroItemSkinsAPI.hasSkinEquipped(Objects.requireNonNull(e.getEntity()).getPlayer(), SkinType.DOOMSDAY_BACKPACK_BUGOUT) && Math.random() < 0.2) {
                        killer.sendMessage(CC.translate("&a&l* " + e.getEntity().getName() + " 's BUG OUT &7Blocked Monopoly Mask on an item &a&l* "));
                        e.getEntity().getPlayer().sendMessage(CC.translate("&a&l* BUG OUT &7Saved an item from getting monoed &a&l*"));
                        savedItems.add(i);

                        continue;
                    }

                    if (AstroItemSkinsAPI.hasItemSkin(i) && AstroItemSkinsAPI.getItemSkin(i).contains(SkinType.DEATH_KNIGHT_SKULL_BLADE)) {
                        killer.sendMessage(CC.translate("&b&l* " + e.getEntity().getName() + " 's DEATH KNIGHT Skull Blade Saved b&l* "));
                        e.getEntity().getPlayer().sendMessage(CC.translate("&b&l* DEATH KNIGHT Skull was Saved &b&l*"));
                        savedItems.add(i);
                        continue;
                    }

                    if (MaskUtils.hasMaskEquipped(e.getEntity(), "ELF") && Math.random() <= 0.25) {
                        e.getEntity().sendMessage(CC.translate("&a&l * ELF MASK &7Saved an Item from &bMonopoly &a&l*"));
                        savedItems.add(i);
                        continue;
                    }
                    Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(e.getEntity(), "HOLY_WHITESCROLL_DEATH", Lists.newArrayList(new ItemStack[1])));
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ITEM_BREAK, 5.0F, 1.2F);
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.WITHER_SHOOT, 3.0F, 10.0F);
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.ITEM_BREAK, 5.0F, 1.2F);
                    e.getEntity().getWorld().playSound(e.getEntity().getLocation(), Sound.WITHER_SHOOT, 3.0F, 10.0F);
                    if (killerIsControllingFrozen && Math.random() < 0.1) {
                        e.getEntity().sendMessage(CC.RedB + "* FROZEN STRONGHOLD MONOPOLY [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                        killer.sendMessage(CC.GreenB + "* FROZEN STRONGHOLD MONOPOLY [" + CC.Gray + e.getEntity().getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.GreenB + "] *");
                    } else {
                        String prefix = getPrefixFromMono(killer);
                        e.getEntity().sendMessage(CC.RedB + "* " + prefix + " [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                        killer.sendMessage(CC.GreenB + "* "  + prefix + " [" + CC.Gray + e.getEntity().getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.GreenB + "] *");
                    }
                    continue;
                }

                if (isUltraad) {
                    e.getEntity().sendMessage(CC.AquaB + "* ULTRA HOLY NEGATION [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                    killer.sendMessage(CC.AquaB + "* ULTRA HOLY NEGATION [" + CC.Gray + e.getEntity().getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.AquaB + "] *");
                    Bukkit.getLogger().info("Dropping " + i + " due to " + killer.getName() + "'s Ultra procing.");
                    continue;
                }

                if (isBansheed) {
                    e.getEntity().sendMessage(CC.RedB + "* BANSHEE RKO [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                    killer.sendMessage(CC.GreenB + "* BANSHEE RKO [" + CC.Gray + e.getEntity().getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.GreenB + "] *");
                    Bukkit.getLogger().info("Dropping " + i + " due to " + killer.getName() + "'s Banshee RKO procing.");
                    continue;
                }
                if (!isTravAxe)
                    continue;
                e.getEntity().sendMessage(CC.RedB + "* TRAVELER AXE HOLY WHITE SCROLL NEGATION [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                killer.sendMessage(CC.GreenB + "* TRAVELER AXE HOLY WHITE SCROLL NEGATION [" + CC.Gray + e.getEntity().getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.GreenB + "] *");
            }
            if (i.getType() == Material.SKULL_ITEM) {
                NBTWrapper wrapper = new NBTWrapper(i);
                InventoryPetItem petItem = CosmicPersistentItem.constructItem(i, wrapper, InventoryPetItem.class);
                if (petItem != null && petItem.getPetType() != null) {
                    if (petItem.isLeashed()) {
                        double chance = 0;
                        if (e.getEntity().getKiller() != null && e.getEntity().getKiller() != null && MaskType.hasEquipped(e.getEntity().getKiller(), "ANIMAL_CONTROL")) {
                            chance += 0.33;
                        }
                        if (AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.VALENTINES_2022_BELT) || AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.VALENTINES_2022_MUTATED_BELT)) {
                            chance += 0.45;
                        }
                        if (AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.JULY4TH_2022_HELMET)) {
                            chance += 0.15;
                        }

                        petItem.setLeashed(false);
                        if (Math.random() < chance) {
                            if (e.getEntity().getKiller() != null && MaskType.hasEquipped(e.getEntity().getKiller(), "ANIMAL_CONTROL")) {
                                killed.sendMessage(CC.RedB + "* ANIMAL CONTROL MASK [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                                killer.sendMessage(CC.GreenB + "* ANIMAL CONTROL MASK [" + CC.Gray + killed.getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.GreenB + "] *");
                            } else if (AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.JULY4TH_2022_HELMET) || AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.MMM_JULY4TH_2022_HELMET)) {
                                killed.sendMessage(CC.RedB + "* COWBOY HAT [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                                killer.sendMessage(CC.GreenB + "* COWBOY HAT [" + CC.Gray + killed.getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.GreenB + "] *");
                            } else if (AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.VALENTINES_2022_BELT) || AstroItemSkinsAPI.hasSkinEquipped(killer, SkinType.VALENTINES_2022_MUTATED_BELT)) {
                                killed.sendMessage(CC.RedB + "* BLING BELT [" + CC.Gray + killer.getName() + " (- " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.RedB + "] *");
                                killer.sendMessage(CC.GreenB + "* BLING BELT [" + CC.Gray + killed.getName() + " (+ " + ItemUtils.getDisplayName(i) + CC.Gray + ")" + CC.GreenB + "] *");
                            }
                            toRemoveToDrops.add(i);
                            toAddToDrops.add(petItem.build());
                        } else {
                            toRemoveToDrops.add(i);
                            savedItems.add(petItem.build());
                        }
                    }
                }
            }
        }
        for (ItemStack i : savedItems)
            e.getDrops().remove(i);
        for (ItemStack i : toRemoveToDrops)
            e.getDrops().remove(i);
        for (ItemStack i : toAddToDrops)
            e.getDrops().add(i);

        if (savedItems.size() > 0)
            this.savedItems.put(e.getEntity().getUniqueId().toString(), savedItems);

        if (killed.getTotalExperience() > 0) {
            e.getDrops().add(ExpBottles.getXpBottle(killed.getTotalExperience(), killed.getName()));
            killed.setTotalExperience(0);
        }
        List<ItemStack> itemList = e.getDrops();



        itemList.removeIf(itemStack -> itemStack.getType() == Material.POTION);

        if(itemList.size() <= 0) return;
        CollectPlugin.getInstance().getCollectManager().getOrCreate(killer.getUniqueId().toString()).add(new CollectCategory(CollectType.DEATH_INVENTORY, itemList.toArray(new ItemStack[itemList.size() - 1]), CC.RedB + killed.getName() + "'s Death Inventory", System.currentTimeMillis()));
        e.getDrops().clear();
        itemList.clear();
        killer.sendMessage("");
        killer.sendMessage(CC.translate("&3&l(&b&lCOLLECT&3&l)&b The Death Loot from &f" + killed.getName() + "&b has been put into your &f/collect&b."));
        killer.sendMessage("");

    }

    private String getPrefixFromMono(Player killer) {
        return MaskUtils.hasMaskEquipped(killer, "M_MONOPOLY") ? CC.translate("MUTATED MONOPOLY") : MaskUtils.hasMaskEquipped(killer, "MONOPOLY") ? "MONOPOLY" : "";
    }

    private double getMonoChance(Player killer, boolean killerIsControllingFrozen) {
        double initial = MaskUtils.hasMaskEquipped(killer, "M_MONOPOLY") ? 0.66 : MaskUtils.hasMaskEquipped(killer, "MONOPOLY") ? 0.33 : 0;
        return (killerIsControllingFrozen ? initial + 0.1D : initial);
    }

    private boolean isWearingMonopoly(Player killer) {
        if(killer == null) return false;
        return MaskUtils.hasMaskEquipped(killer, "MONOPOLY") || MaskUtils.hasMaskEquipped(killer, "M_MONOPOLY");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        if (e.getPlayer().getLastDamageCause() == null) return;
        if (e.getPlayer().getLastDamageCause().getCause() == null) return;

        if (e.getPlayer().getLastDamageCause().getCause() == EntityDamageEvent.DamageCause.VOID) return;
        List<ItemStack> toReturn = this.savedItems.remove(e.getPlayer().getUniqueId().toString());
        if (toReturn != null) {
            for (ItemStack i : toReturn) {
                e.getPlayer().getInventory().addItem(HolyWhiteScrollUtils.removeHolyWhitescroll(i));
            }
            Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(e.getPlayer(), "HOLY_WHITESCROLL_DEATH", new Object[]{toReturn}));
        }
    }
}
