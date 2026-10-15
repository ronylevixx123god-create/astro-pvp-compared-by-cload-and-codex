package net.astropvp.armorsets.listeners;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import de.tr7zw.nbtapi.NBTItem;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.heroicArmor.HeroicArmor;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

public class HeroicUpgradeApplyListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) throws Exception {
        if (event.getClick() != ClickType.LEFT && event.getClick() != ClickType.RIGHT) {
            return;
        }
        Player player = (Player) event.getWhoClicked();
        if (event.getWhoClicked().getInventory() == null || !event.getWhoClicked().getInventory().equals(player.getInventory())) {
            return;
        }
        ItemStack clicked = event.getCurrentItem();
        ItemStack cursor = event.getCursor();
        if (cursor == null) {
            return;
        }
        if (clicked == null || clicked.getType() == Material.AIR || !GeneralUtil.isArmor(clicked)) {
            return;
        }
        NBTWrapper cursorWrapper;
        if (cursor.getType() == Material.INK_SACK && (cursorWrapper = new NBTWrapper(cursor)).getString("cosmicType").equals("HEROIC_ARMOR_UPGRADE")) {
            event.setCancelled(true);

            NBTTagCompoundBuilder builder = cursorWrapper.getCompound("cosmicData");
            if (builder == null) {
                return;
            }
            String setType = builder.getString("setType");
            if (setType != null && !setType.isEmpty()) {
                boolean correctApplication;
                boolean allSetsAllowed = setType.equalsIgnoreCase("ALL");
                ArmorSetType requiredSet = AstroArmorSetsAPI.armorSetTypeFromName(setType);
                ArmorSetType clickedArmorSet = AstroArmorSetsAPI.getSetTypeOnItem(clicked);
                correctApplication = allSetsAllowed && clickedArmorSet != null || requiredSet != null && requiredSet.equals(clickedArmorSet);
                if (!correctApplication) {
                    player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must apply this Heroic Armor Upgrade to a " + (allSetsAllowed || requiredSet == null ? "" : StringUtils.capitaliseAllWords(requiredSet.name().replace("_", " ").toLowerCase())) + CC.Red + " Armor Set piece!");
                    return;
                }
                HeroicArmor type = HeroicArmor.getFromMaterial(clicked.getType());
                if (!clicked.getType().name().contains("DIAMOND_") || type == null) {
                    player.sendMessage(CC.RedB + "(!) " + CC.Red + "You cannot apply this to non diamond armor set pieces!");
                    return;
                }

                double chance = builder.getDouble("success");
                if (chance > 100.0) {
                    chance = 100.0;
                }
                if (chance < 0.0) {
                    chance = 0.0;
                }
                if (cursor.getAmount() > 1) {
                    cursor.setAmount(cursor.getAmount() - 1);
                } else {
                    player.setItemOnCursor(null);
                    player.updateInventory();
                }
                if (chance >= (double) (ThreadLocalRandom.current().nextInt(100) + 1)) {
                    de.inventivegames.particle.ParticleEffect.HAPPY_VILLAGER.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation(), 0f, 0.5f, 0f, 0.1f, 30);
                    player.getWorld().playSound(player.getLocation(), Sound.VILLAGER_YES, 1.4f, 0.9f);
                    player.getWorld().playSound(player.getLocation(), Sound.LEVEL_UP, 1.4f, 0.9f);
                    ItemStack retItem = AstroArmorSetsAPI.convertItemToHeroic(clicked);
                    NBTWrapper wrapper = new NBTWrapper(retItem);
                    com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder display = wrapper.hasKey("display") ? new com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder(wrapper.getNBTBase("display")) : new NBTTagCompoundBuilder();
                    int color = 0;
                    color = clickedArmorSet.getHeroicRGB();
                    display.setInt("color", color);
                    wrapper.setNBTBase("display", display.get());
                    retItem = wrapper.build();
                    ItemMeta meta = retItem.getItemMeta();
                    meta.spigot().setUnbreakable(true);
                    meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
                    retItem.setItemMeta(meta);
                    NBTItem nbtItem = new NBTItem(retItem);
                    nbtItem.setBoolean("heroic", true);
                    nbtItem.setInteger("wine-armor-override", Objects.requireNonNull(HeroicArmor.getFromMaterial(retItem.getType())).getReduction());
                    retItem = nbtItem.getItem();
                    event.setCurrentItem(retItem);
                    player.sendMessage(CC.GreenB + "(!) " + CC.Green + "You have upgraded your " + ItemUtils.getDisplayName(clicked) + CC.Green + " to the Heroic Armor version!");
                } else {
                    player.sendMessage(CC.RedB + "(!) " + CC.Red + "You failed to apply your " + ItemUtils.getDisplayName(cursor) + CC.Red + "!");
                    player.playSound(player.getLocation(), Sound.LAVA_POP, 1.0f, 0.9f);
                    de.inventivegames.particle.ParticleEffect.LAVA.sendToPlayers(Bukkit.getOnlinePlayers(), player.getLocation(), 0f, 0.5f, 0f, 0.1f, 30);
                }
            }
        }
    }
}

