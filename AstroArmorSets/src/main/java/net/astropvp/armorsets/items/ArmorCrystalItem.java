package net.astropvp.armorsets.items;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagListBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.core.utils.OutpostUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class ArmorCrystalItem extends CosmicItem implements InventoryInteractable {
    public ArmorCrystalItem() {
        super("armorCrystal");
    }

    public static ItemStack applyCrystalToItem(List<ArmorSetType> set, ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("cosmicData");
        NBTTagListBuilder list = new NBTTagListBuilder();
        StringBuilder loreBuild = new StringBuilder();
        for (ArmorSetType s : set) {
            list.addString(s.name());
            loreBuild.append(s.getDisplay()).append(CC.Gray).append(", ");
        }
        data.setNBTBase("crystalBonuses", list.build());
        data.setNBTBase("bonuses", list.build());
        wrapper.setNBTBase("cosmicData", data.get());
        item = wrapper.build();
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore() == null ? new ArrayList<>() : im.getLore();
        String str = loreBuild.toString();
        if (str.endsWith(", ")) {
            str = str.substring(0, str.length() - 2);
        }
        lore.add(CC.GoldB + "Armor Crystal (" + str + CC.GoldB + ")");
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public ItemStack createItem(Player player, Object... objects) {
        List<ArmorSetType> set = new ArrayList<>();
        Number chance = null;
        for (Object object : objects) {
            if (object instanceof Number) {
                chance = (Number) object;
                continue;
            }
            if (object instanceof ArmorSetType) {
                set.add((ArmorSetType) object);
                continue;
            }
            if (object instanceof List) {
                set = (List<ArmorSetType>) object;
                continue;
            }
            Number num = NumberUtils.parseNumberOrNull(object.toString());
            if (num != null) {
                chance = num;
                continue;
            }
            ArmorSetType fromName = AstroArmorSetsAPI.armorSetTypeFromName(object.toString());
            if (fromName == null) continue;
            set.add(fromName);
        }
        if (set.isEmpty() || chance == null) {
            return null;
        }
        boolean mutatedFound = false;
        ArrayList<String> lore = Lists.newArrayList(CC.Green + chance.intValue() + "% Success Rate", CC.Gray + "Can be applied to any non", CC.Gray + "armor set that is not", CC.Gray + "already equipped with a", CC.Gray + "bonus crystal to gain", CC.Gray + "a passive advantage!", "");
        lore.add(CC.GoldB + "Crystal Bonus:");
        NBTTagListBuilder list = new NBTTagListBuilder();
        for (ArmorSetType s : set) {
            if (s.name().contains("MUTATED")) {
                mutatedFound = true;
            }
            list.addString(s.name());
            lore.add(" " + s.getColor() + CC.Bold + s.getDisplay());
            for (String str : s.getArmorSet().getArmorCrystalLore()) {
                if (str.contains("&")) {
                    lore.add(CC.translate("  " + s.getColor() + CC.Bold + "  " + s.getColor() + str));
                } else {
                    lore.add("  " + CC.DarkGrayB + "● " + s.getColor() + str);
                }
            }

        }
        String name = null;
        if (set.size() > 1) {
            StringBuilder builder = new StringBuilder();
            for (ArmorSetType found : set) {
                builder.append(found.getFirstDisplayNameLetter()).append(CC.Gold).append(", ");
            }
            String combined = builder.toString();
            if (combined.endsWith(", ")) {
                combined = combined.substring(0, combined.length() - 2);
            }
            name = CC.GoldB + "Multi Armor Set Crystal (" + combined + CC.GoldB + ")";
        } else if (!set.isEmpty()) {
            name = CC.GoldB + "Armor Crystal (" + set.stream().findFirst().orElse(null).getDisplay() + CC.GoldB + ")";
        }
        ItemStack crystal = this.applyItemData(new ItemBuilder(Material.NETHER_STAR, name).lore(lore).build()).setNBTBase("cosmicData", new NBTTagCompoundBuilder().setNBTBase("bonuses", list.build()).setDouble("bonusChance", chance.doubleValue()).get()).assignIID().build();

        if (mutatedFound) {
            NBTWrapper wrapper = new NBTWrapper(crystal);
            NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
            cosmicData.setString("tintColor", "-10237440"); //-10237440
            return crystal;
        } else {
            return this.applyItemData(new ItemBuilder(Material.NETHER_STAR, name).lore(lore).build()).setNBTBase("cosmicData", new NBTTagCompoundBuilder().setNBTBase("bonuses", list.build()).setDouble("bonusChance", chance.doubleValue()).get()).assignIID().build();

        }
    }


    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }


    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper cursorWrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent event) throws CosmicException {
        if (clickedWrapper == null || !GeneralUtil.isArmor(clicked)) {
            return;
        }
        event.setCancelled(true);
        NBTTagCompoundBuilder data = cursorWrapper.getCompound("cosmicData");
        if (data == null) {
            return;
        }
        List<ArmorSetType> sets = AstroArmorSetsAPI.getArmorCrystalsFromCrystalItem(data);
        if (sets != null) {
            double chanceToApply;
            ArmorSetType itemSet = AstroArmorSetsAPI.getSetTypeOnItem(clicked);
            if (itemSet != null) {
                throw new PrefixedException("You cannot apply an Armor Set Crystal to an existing Armor Set piece!");
            }

            if (!AstroArmorSetsAPI.isHeroic(clicked)) {
                throw new PrefixedException("You cannot apply crystals to non-heroic armor");
            }
            if (cursorWrapper.isBlacklistedIID()) {
                throw new ItemDupedException("Armor set crystal already used!");
            }
            if (!player.getWorld().getName().equals("world") && !player.getWorld().getName().equals("flat")) {
                throw new PrefixedException("You must apply this Armor Set Crystal in the Overworld!");
            }
            List<ArmorSetType> bonusApplied = AstroArmorSetsAPI.getCrystalsFromItem(clicked);
            if (bonusApplied != null && !bonusApplied.isEmpty()) {
                throw new PrefixedException("You cannot apply multiple Armor Set Crystals to a single piece of gear!");
            }
            if (cursor.getAmount() > 1) {
                cursor.setAmount(cursor.getAmount() - 1);
                player.setItemOnCursor(cursor);
            } else {
                player.setItemOnCursor(null);
            }

            chanceToApply = data.hasKey("bonusChance") ? data.getDouble("bonusChance") : 20.0;

            if (player.hasPermission("astrocore.galaxy")) {
                chanceToApply += 10;
                player.sendMessage(CC.translate("&a+10% Crystal Apply Chance &7(Galaxy Rank Perk)"));
            } else if (player.hasPermission("astrocore.nebula")) {
                chanceToApply += 4;
                player.sendMessage(CC.translate("&a+4% Crystal Apply Chance &7(Nebula Rank Perk)"));
            } else if (player.hasPermission("astrocore.apollo")) {
                chanceToApply += 2;
                player.sendMessage(CC.translate("&a+2% Crystal Apply Chance &7(Apollo Rank Perk)"));
            }

            if (OutpostUtil.hasPlutoOutpost(player)) {
                chanceToApply+=7.5;
                player.sendMessage(CC.translate("&a+10% Crystal Apply Chance &7(Pluto Outpost)"));
            }


            if (Math.random() > chanceToApply * 0.01) {
                ASound.GLASS.play(player, 1.1f, 0.8f);
                Bukkit.getLogger().info(player.getName() + " failed to apply " + sets + " armor set crystal!");
                player.updateInventory();
                throw new PrefixedException("Your Armor Crystal " + ItemUtils.getDisplayName(cursor) + CC.Red + " fails to apply!");
            }
            event.setCurrentItem(AstroArmorSetsAPI.applyCrystalToItem(sets, clicked));
            player.sendMessage("");
            player.sendMessage(CC.GoldB + "(!) " + ItemUtils.getDisplayName(cursor) + " " + CC.Gold + "applied to " + clicked.getItemMeta().getDisplayName() + CC.Gold + "!");
            player.sendMessage(CC.GoldB + "Crystal Bonus:");
            for (ArmorSetType set : sets) {
                player.sendMessage(" " + set.getDisplay() + ":");
                for (String line : set.getArmorSet().getArmorCrystalLore()) {
                    player.sendMessage(CC.GoldB + "  ● " + set.getColor() + line);
                }
                Bukkit.getLogger().info("Applying " + set.name() + " Set Bonus Crystal to " + ItemUtils.getDisplayName(clicked));
            }
            player.sendMessage("");
            ASound.LEVEL_UP.play(player, 1.1f);
            player.updateInventory();
        }
    }
}

