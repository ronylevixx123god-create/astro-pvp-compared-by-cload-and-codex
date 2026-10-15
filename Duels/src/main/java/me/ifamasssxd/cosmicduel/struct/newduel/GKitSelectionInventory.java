/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits
 *  org.bukkit.Sound
 *  org.bukkit.entity.Player
 *  org.bukkit.event.me.astropvp.cratekeys.inventory.InventoryClickEvent
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicduel.struct.newduel;

import me.ifamasssxd.cosmicduel.CosmicDuel;
import me.ifamasssxd.cosmicduel.inventory.CustomInventory;
import me.ifamasssxd.cosmicduel.inventory.CustomInventoryItem;
import me.ifamasssxd.cosmicduel.struct.Kit;
import me.ifamasssxd.cosmicduel.struct.enums.DuelCreateState;
import me.ifamasssxd.cosmicduel.utils.EnchantUtils;
import me.ifamasssxd.cosmicduel.utils.ItemBuilder;
import me.ifamasssxd.cosmicduel.utils.KitUtils;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class GKitSelectionInventory
extends CustomInventory {
    NewDuel duel;

    public GKitSelectionInventory(NewDuel duel) {
        super("Select a Duel GKit", 6);
        this.duel = duel;
    }


}

