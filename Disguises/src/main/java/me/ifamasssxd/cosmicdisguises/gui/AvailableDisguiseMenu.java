/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.InventoryItem
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.ItemBuilder
 *  com.google.common.collect.Lists
 *  me.libraryaddict.disguise.DisguiseAPI
 *  me.libraryaddict.disguise.disguisetypes.Disguise
 *  me.libraryaddict.disguise.disguisetypes.DisguiseType
 *  me.libraryaddict.disguise.disguisetypes.MobDisguise
 *  org.apache.commons.lang.StringUtils
 *  org.bukkit.Bukkit
 *  org.bukkit.Material
 *  org.bukkit.entity.Entity
 *  org.bukkit.entity.EntityType
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicdisguises.gui;

import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.gui.InventoryItem;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicdisguises.CosmicDisguises;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Set;

public class AvailableDisguiseMenu
extends CosmicGUI {
    private long lastChange = 0L;
    private final Set disguiseTypes;

    public AvailableDisguiseMenu(Player player, Set availableTypes) {
        super(player, "Available Disguises", 36);
        this.disguiseTypes = availableTypes;
    }

    public void redraw() {
        int index = 0;
        Disguise active = DisguiseAPI.getDisguise(this.player);
        for (DisguiseType type : DisguiseType.values()) {
            if (CosmicDisguises.get().getDisguiseManager().isDisabledDisguise(type)) continue;
            if (index >= this.size) break;
            EntityType entityType = type.getEntityType();
            boolean equipped = active != null && active.getType() == type;
            boolean unlocked = this.disguiseTypes.contains(type);
            ArrayList lore = Lists.newArrayList("", equipped ? CC.GreenB + "CURRENTLY ACTIVE" : (!unlocked ? CC.RedB + "LOCKED" : CC.GreenB + "UNLOCKED"), "", !unlocked ? CC.Gray + "Unlock disguises at " + CC.GrayU + "buy.astropvp.net" + CC.Gray + "!" : CC.Gray + "Click to " + (equipped ? "de-" : "") + "activate this disguise!");
            String eggName = StringUtils.capitaliseAllWords(type.name().replace("_", " ").toLowerCase());
            this.setItem(index++, new InventoryItem(new ItemBuilder(new ItemStack(Material.MONSTER_EGG, 1, (entityType == EntityType.WITHER ? EntityType.ENDERMAN : (entityType == EntityType.ENDER_DRAGON ? EntityType.BAT : entityType)).getTypeId())).name((equipped ? CC.GreenB : CC.RedB) + eggName).lore(lore).build()).click(() -> {
                if (this.lastChange != 0L && System.currentTimeMillis() - this.lastChange <= 1000L) {
                    this.player.sendMessage(CC.Red + "Please wait at least 1 second in between disguise changes!");
                } else if (!unlocked) {
                    this.player.sendMessage(CC.Red + "Unlock access to disguises at " + CC.RedU + "buy.astropvp.net" + CC.Red + "!");
                } else {
                    String timeTillCanRun = CosmicDisguises.get().getDisguiseManager().getDisguiseCooldownString(this.player);
                    if (timeTillCanRun != null) {
                        this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "You must wait " + CC.RedU + timeTillCanRun + CC.Red + " before disguising again!");
                    } else {
                        this.lastChange = System.currentTimeMillis();
                        DisguiseAPI.undisguiseToAll(this.player);
                        if (equipped) {
                            this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Disguise De-activated!");
                        } else {
                            Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicDisguises.get(), () -> {
                                if (type == DisguiseType.ENDER_DRAGON || type == DisguiseType.ZOMBIE_VILLAGER || type == DisguiseType.WITHER || type == DisguiseType.GIANT || type == DisguiseType.WITHER_SKELETON) {
                                    this.player.sendMessage(CC.Red + "This disguise is disabled.");
                                    return;
                                }
                                DisguiseAPI.disguiseToAll(this.player, new MobDisguise(type));
                            });
                            if (type == DisguiseType.ENDER_DRAGON || type == DisguiseType.ZOMBIE_VILLAGER || type == DisguiseType.WITHER || type == DisguiseType.GIANT || type == DisguiseType.WITHER_SKELETON) {
                                return;
                            }
                            this.player.sendMessage(CC.GreenB + "(!) " + CC.Green + "You have activated the " + CC.GreenU + StringUtils.capitaliseAllWords(type.name().toLowerCase().replace("_", " ")) + CC.Green + " disguise!");
                            this.player.closeInventory();
                        }
                        this.redraw();
                    }
                }
            }));
        }
    }
}

