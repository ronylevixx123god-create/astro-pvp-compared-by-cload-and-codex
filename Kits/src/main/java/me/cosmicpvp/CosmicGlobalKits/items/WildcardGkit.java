/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.compat.ASound
 *  com.cosmicpvp.cosmicutils.exceptions.CosmicException
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException
 *  me.ifamasssxd.cosmicskycustoms.exceptions.PrefixedItemException
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.CosmicItem
 *  me.ifamasssxd.cosmicskycustoms.net.astropvp.armorsets.struct.PlayerInteractable
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.World
 *  org.bukkit.entity.Player
 *  org.bukkit.event.player.PlayerInteractEvent
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 */
package me.cosmicpvp.CosmicGlobalKits.items;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.exceptions.CosmicException;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.items.WildcardMenu;
import me.ifamasssxd.cosmicskycustoms.exceptions.ItemDupedException;
import me.ifamasssxd.cosmicskycustoms.exceptions.PrefixedItemException;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import me.ifamasssxd.cosmicskycustoms.struct.PlayerInteractable;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class WildcardGkit
extends CosmicItem
implements PlayerInteractable {
    public WildcardGkit() {
        super("wildcard_gkit");
    }

    public ItemStack createItem(Player player, Object ... objects) {
        return this.applyItemData(new ItemBuilder(Material.DIAMOND, CC.GoldB + "Omni Gem (" + CC.Gold + "/gkit" + CC.GoldB + ")", new String[]{CC.Gray + "Right click to select any", CC.Gold + "/gkit" + CC.Gray + " to level up or", CC.Gray + "redeem if not unlocked!"}).build()).assignIID().build();
    }

    public void onPlayerInteractable(Player player, ItemStack item, NBTWrapper wrapper, PlayerInteractEvent e) throws CosmicException {
        e.setCancelled(true);
        if (wrapper == null) {
            return;
        }
        if (wrapper.isBlacklistedIID()) {
            throw new ItemDupedException("Gem already claimed!");
        }
        if (!player.getWorld().getName().equalsIgnoreCase(((World)Bukkit.getWorlds().get(0)).getName())) {
            throw new PrefixedItemException("You cannot claim this gem in this world!");
        }
        if (item.getAmount() > 1) {
            item.setAmount(item.getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }
        ASound.VILLAGER_HAGGLE.play(player, 10.0f, 1.1f);
        new WildcardMenu(player, wrapper).openGUI(CosmicGlobalKits.getPlugin());
    }
}

