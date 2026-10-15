/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.compat.ASound
 *  com.cosmicpvp.cosmicutils.exceptions.CosmicException
 *  com.cosmicpvp.cosmicutils.exceptions.PrefixedException
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.struct.LootTable
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemUtils
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.PlayerUtil
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.RandomFireworks
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.PlayerInteractable
 *  org.bukkit.Material
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.ifamasssxd.cosmicmysterychest.items;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.exceptions.PrefixedException;
import com.cosmicpvp.cosmicutils.struct.LootTable;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.ifamasssxd.cosmicmysterychest.struct.MysteryChest;
import me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.PlayerInteractable;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class MysteryLootboxItem
        extends CosmicItem
        implements PlayerInteractable {
    private LootTable<MysteryChest> availableChests;

    public MysteryLootboxItem() {
        super("mystery_lootbox");
        this.calculateLootTable();
    }

    public void calculateLootTable() {
        this.availableChests = new LootTable();
        ArrayList<String> boxes = Lists.newArrayList(new String[]{"lootbox_baked", "lootbox_genesis", "lootbox_sugarrush", "lootbox_lucky", "lootbox_darkdreams", "lootbox_chocolates", "lootbox_meltandrenew", "lootbox_aprilshowers", "lootbox_survival", "lootbox_snowday", "lootbox_hangover", "lootbox_easter2019", "lootbox_icy", "lootbox_stackable", "lootbox_summersavage", "lootbox_dungeoncrawler"});
        for (String box : boxes) {
            MysteryChest fromName = CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get(box);
            if (fromName == null) continue;
            this.availableChests.addItem(fromName, 1.0);
        }
    }

    public ItemStack createItem(Player player, Object... objects) {
        ArrayList lore = Lists.newArrayList( new String[]{CC.Gray + "Click to receive one of the", CC.Gray + "following random lootboxes:"});
        for (MysteryChest chest : this.availableChests.getLoot()) {
            lore.add(CC.WhiteB + " * " + CC.Gray + chest.getDisplayName());
        }
        return this.applyItemData(new ItemBuilder(Material.CHEST, CC.GoldB + "Mystery Lootbox " + CC.Gray + "(Right Click)", new String[0]).lore((List) lore).build()).assignIID().build();
    }

    public void onPlayerInteractable(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent event) throws CosmicException {
        event.setCancelled(true);
        if (wrapper.isBlacklistedIID()) {
            throw new ItemDupedException("Mystery Lootbox already claimed!");
        }
        if (player.getWorld().getName().contains("duels")) {
            throw new PrefixedException("You cannot use this item in this world!");
        }
        wrapper.blacklistIID("lootbox claimed " + player.getName());
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
        MysteryChest chest = (MysteryChest) this.availableChests.getRandomLoot();
        ItemStack is = chest.createClaimableItem(true);
        PlayerUtil.giveOrDropProtectedItem((Player) player, (ItemStack) is, (int) -1);
        player.sendMessage(CC.GoldB + "(!) " + CC.Gold + "You have received a " + ItemUtils.getDisplayName((ItemStack) is) + CC.Gold + " from your Mystery Lootbox!");
        player.updateInventory();
        ASound.LEVEL_UP.play(player, 2.0f, 3.0f);
        RandomFireworks.getManager().launchRandomFirework(player.getLocation());
    }
}

