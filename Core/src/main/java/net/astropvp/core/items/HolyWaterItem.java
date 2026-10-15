/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.exceptions.CosmicException
 *  com.cosmicpvp.cosmicutils.exceptions.PrefixedException
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.InventoryInteractable
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.InventoryItemType
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package net.astropvp.core.items;


import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import net.astropvp.core.utils.HolyWhiteScrollUtils;
import net.astropvp.core.utils.ItemUtils;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class HolyWaterItem
extends CosmicItem
implements InventoryInteractable {
    public HolyWaterItem() {
        super("holy_water");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        return this.applyItemData(ItemUtils.addGlow(new ItemBuilder(Material.MILK_BUCKET, CC.GoldB + "Holy Water [" + CC.Yellow + "Max: 4" + CC.GoldB + "]", CC.Yellow + "A rare consumable that", CC.Yellow + "increases the # of Holy White Scrolls", CC.Yellow + "that can be applied to an item by 1.").build())).build();
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }

    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper wrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent e) throws CosmicException {
        if (clicked == null || clicked.getType() == Material.AIR || clickedWrapper == null) {
            return;
        }
        if (!HolyWhiteScrollUtils.itemHasHolyWhitescroll(clicked) && !HolyWhiteScrollUtils.isCorrupt(clickedWrapper)) {
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "This item must contain a Holy White Scroll before you can apply Holy Water!");
            e.setCancelled(true);
            return;
        }
        if (cursor.getAmount() > 1) {
            throw new PrefixedException("You cannot apply stacked Holy Water to an item.");
        }
        NBTTagCompoundBuilder cosmicData = clickedWrapper.getCompoundOrCreate("conaxData");
        if (cosmicData.hasKey("holyWater")) {
            throw new PrefixedException("Holy Water has already been applied to this item!");
        }
        cosmicData.setInt("holyWater", Integer.valueOf(1));
        int scrollsApplied = HolyWhiteScrollUtils.getHolyScrollsApplied(clickedWrapper);
        e.setCancelled(true);
        e.getWhoClicked().setItemOnCursor((ItemStack)null);
        clickedWrapper.setNBTBase("conaxData", (NBTBase)cosmicData.get());
        clicked = clickedWrapper.build();
        HolyWhiteScrollUtils.applyHolyWhitescroll(clicked, scrollsApplied, 4, false);
            HolyWhiteScrollUtils.removeCorrupt(clicked, clickedWrapper);
        e.setCurrentItem(clicked);
        player.sendMessage(CC.Gold + "Successfully applied Holy Water to this item!");
    }

    public void onItemUse(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
        event.setCancelled(true);
    }
}

