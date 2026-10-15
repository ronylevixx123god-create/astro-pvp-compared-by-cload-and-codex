/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.exceptions.CosmicException
 *  com.cosmicpvp.cosmicutils.exceptions.PrefixedException
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.InventoryInteractable
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.InventoryItemType
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.Event$Result
 *  org.bukkit.event.inventory.InventoryClickEvent
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.inventory.meta.ItemMeta
 */
package com.compilex.CosmicDungeons.items;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import java.util.List;
import java.util.UUID;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryInteractable;
import me.ifamasssxd.cosmicskycustoms.struct.InventoryItemType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class HeroicDungeonScrollItem extends CosmicItem implements InventoryInteractable {
    public HeroicDungeonScrollItem() {
        super("heroic_dungeon_scroll");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        DungeonType dungeon = DungeonType.valueOf(objects[0].toString());
        return new NBTWrapper(this.applyItemData(new ItemBuilder(Material.EMPTY_MAP, CC.translate((String)("&b&k!!!&f&l&nHeroic&5&k!!!&r &f&lDungeon Scroll: " + dungeon.getDungeonInstance().getRealDungeonName())), new String[]{CC.Gray + "Upgrades a " + dungeon.getDungeonInstance().getRealDungeonName() + CC.Gray + " /dungeon", CC.Gray + "portal to give all participants OP rewards", CC.Gray + "for increasing the difficulty of the dungeon.", "", CC.Gray + "Hover over the skull in /dungeon", CC.Gray + "for more information.", "", CC.Gray + "Drag n' drop onto item to apply.", "", CC.RedB + "WARNING: " + CC.Red + "Will remove all trinkets when applied."}).build()).build()).setString("random", UUID.randomUUID().toString()).setString("dungeon", dungeon.getInternalName()).build();
    }

    public boolean acceptsInventoryClickType(InventoryItemType inventoryItemType) {
        return inventoryItemType == InventoryItemType.CURSOR;
    }

    public void onInventoryClick(Player player, ItemStack cursor, NBTWrapper wrapper, NBTWrapper clickedWrapper, ItemStack clicked, InventoryClickEvent e) throws CosmicException {
        if (clicked == null || clicked.getType() == Material.AIR || clickedWrapper == null) {
            return;
        }
        if (!DungeonsAPI.get().isDungeonPortal(clicked) || DungeonsAPI.get().isHeroicPortal(clicked)) {
            return;
        }
        if (cursor.getAmount() > 1) {
            throw new PrefixedException("You cannot apply stacked Heroic Dungeon Scrolls to an item.");
        }
        if (clicked.getAmount() > 1) {
            throw new PrefixedException("You cannot apply Heroic Dungeon Scrolls to stacked portals.");
        }
        Dungeon dungeon = DungeonsAPI.get().getDungeonFromPortalItem(clicked);
        if (!wrapper.getString("dungeon").equals(dungeon.getDungeonType().getInternalName())) {
            throw new PrefixedException("This scroll is not for the " + dungeon.getRealDungeonName() + CC.Red + " dungeon!");
        }
        e.setCancelled(true);
        e.getWhoClicked().setItemOnCursor(null);
        clicked = clickedWrapper.build();
        ItemMeta meta = clicked.getItemMeta();
        meta.setDisplayName(meta.getDisplayName().replace(dungeon.getRealDungeonName(), CC.translate((String)"&b&k!!!&f&l&nHeroic&5&k!!!&r ") + dungeon.getRealDungeonName()));
        List lore = meta.getLore();
        int trinketIdx = -1;
        for (int i = 0; i < lore.size(); ++i) {
            if (!((String)lore.get(i)).contains("TRINKET")) continue;
            trinketIdx = i;
        }
        if (trinketIdx != -1) {
            lore.remove(trinketIdx);
        }
        lore.set(1, ((String)lore.get(1)).replace(dungeon.getRealDungeonName(), CC.translate((String)"&b&k!!!&f&l&nHeroic&5&k!!!&r ") + dungeon.getRealDungeonName()));
        meta.setLore(lore);
        clicked.setItemMeta(meta);
        clickedWrapper = new NBTWrapper(clicked);
        if (clickedWrapper.getconaxData().hasKey("dungeonTrinket")) {
            clickedWrapper.getconaxData().remove("dungeonTrinket");
        }
        e.setCurrentItem(clickedWrapper.build());
    }

    public void onItemUse(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
        event.setCancelled(true);
        event.setUseItemInHand(Event.Result.DENY);
        player.updateInventory();
    }
}

