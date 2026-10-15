package me.astropvp.astropvpskins.objects;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.entity.*;
import org.bukkit.inventory.*;
import me.ifamasssxd.cosmicskycustoms.struct.*;
import org.bukkit.event.inventory.*;
import com.cosmicpvp.cosmicutils.utils.*;
import org.bukkit.*;
import org.bukkit.inventory.meta.*;
import java.util.*;
import com.cosmicpvp.cosmicutils.exceptions.*;
import org.bukkit.event.player.*;

public class BackpackSocketItem extends CosmicItem implements InventoryInteractable, PlayerInteractable
{
    public static final String BACKPACK_SOCKET_NBT_KEY = "hasBackpackSocket";

    public BackpackSocketItem() {
        super("backpack_socket");
    }

    public ItemStack createItem(final Player player, final Object... objects) {
        Integer successRate = (objects != null && objects.length >= 1) ? NumberUtils.parseIntOrNull(objects[0].toString()) : null;
        if (successRate == null) {
            successRate = 1;
        }
        ItemBuilder builder = new ItemBuilder(Material.TRIPWIRE_HOOK, CC.translate("&3&lArmor Socket: &b&nBackpack"), new String[] { CC.translate("&3&lArmor Type: &bCHESTPLATE"),
                CC.translate("&3&lLimit: &b1 backpack(s)"),
                CC.translate("&3&lChance: &b" + successRate + "%"),
                "",
                CC.translate("&7Armor Sockets add speciality slots to"),
                CC.translate("&7your items, allowing you to equip powerful"),
                CC.translate("&7accessories with game altering powers.") });

        return this.applyItemData(builder.build())
                .setInteger("successRate", successRate).build();
    }

    public boolean acceptsInventoryClickType(final InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }

    public void onInventoryClick(final Player player, final ItemStack cursor, final NBTWrapper cursorWrapper, NBTWrapper clickedWrapper, final ItemStack clicked, final InventoryClickEvent event) throws CosmicException {
        if (clicked == null || clicked.getType() == Material.AIR || clickedWrapper == null) {
            return;
        }
        event.setCancelled(true);
        if (!clicked.getType().toString().endsWith("_CHESTPLATE")) {
            player.sendMessage(CC.translate("&c&l(!) &cThis socket type (Backpack) can only be added to Chestplate armor items."));
            return;
        }

        if (clicked.getType() != Material.LEATHER_CHESTPLATE || !AstroArmorSetsAPI.isHeroic(clicked)) {
            player.sendMessage(CC.translate("&c&l(!) &cYou can only apply this to heroic chestplates!."));
            return;
        }
        if (clickedWrapper != null && clickedWrapper.getconaxData() != null && clickedWrapper.getconaxData().hasKey("hasBackpackSocket")) {
            player.sendMessage(CC.translate("&c&l(!) &cThis item already has a Backpack socket on it."));
            return;
        }
        final int successRate = cursorWrapper.getInt("successRate");
        if (player.getItemOnCursor().getAmount() > 1) {
            player.getItemOnCursor().setAmount(player.getItemOnCursor().getAmount() - 1);
        }
        else {
            player.setItemOnCursor(null);
        }
        if (Math.random() * 100.0 < successRate) {
            final ItemMeta meta = clicked.getItemMeta();
            final List<String> lore = meta.getLore();
            lore.add(CC.translate("&3&l+ BACKPACK [&8N/A&3&l]"));
            meta.setLore(lore);
            clicked.setItemMeta(meta);

            NBTWrapper wrapper = new NBTWrapper(clicked);
            NBTTagCompoundBuilder conaxData = wrapper.getCompoundOrCreate("conaxData");
            conaxData.setBoolean("hasBackpackSocket", true);
            wrapper.setNBTBase("conaxData", (NBTBase) conaxData.get());

            ItemStack finalItem = wrapper.build();
            event.setCurrentItem(finalItem);

            player.sendMessage(CC.translate("&a&l(!) &aYour socket successfully applied."));
            player.playSound(player.getLocation(), Sound.LEVEL_UP, 1.0f, 1.0f);
        }
        else {
            player.sendMessage(CC.translate("&c&l(!) &cThe socket failed to apply."));
            player.playSound(player.getLocation(), Sound.ITEM_BREAK, 1.0f, 1.0f);
        }
    }

    public void onPlayerInteractable(final Player player, final ItemStack itemStack, final NBTWrapper nbtWrapper, final PlayerInteractEvent event) {
        player.sendMessage(CC.translate("&c&l(!) &cTo add this Armor socket to an item, drag-and-drop it onto the item in your inventory."));
        event.setCancelled(true);
    }
}
