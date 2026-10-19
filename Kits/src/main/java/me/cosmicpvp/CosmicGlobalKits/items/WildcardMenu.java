/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.compat.ASound
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.gui.CosmicGUI
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.InventoryUtils
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.ItemBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.PlayerUtil
 *  me.ifamasssxd.cosmicevolutionkits.CosmicEvolutionKits
 *  org.bukkit.Bukkit
 *  org.bukkit.entity.Player
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.plugin.Plugin
 */
package me.cosmicpvp.CosmicGlobalKits.items;

import com.cosmicpvp.cosmicutils.compat.ASound;
import com.cosmicpvp.cosmicutils.gui.CosmicGUI;
import com.cosmicpvp.cosmicutils.utils.*;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKits;
import me.cosmicpvp.CosmicGlobalKits.CosmicGlobalKitsAPI;
import me.cosmicpvp.CosmicGlobalKits.libs.GlobalKits;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitData;
import me.cosmicpvp.CosmicGlobalKits.struct.GlobalKitUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.Map;

public class WildcardMenu
extends CosmicGUI {
    private final NBTWrapper itemWrapper;
    private final WildcardClaim claim;

    public WildcardMenu(Player player, NBTWrapper itemWrapper) {
        super(player, "Select /gkit to unlock/level", InventoryUtils.fitSize((int)GlobalKits.values().length));
        this.itemWrapper = itemWrapper;
        this.claim = new WildcardClaim(new WildcardClaim.Actions() {
            public boolean isItemAlreadyClaimed() {
                return WildcardMenu.this.itemWrapper.isBlacklistedIID();
            }

            public void markItemClaimed() {
                WildcardMenu.this.itemWrapper.blacklistIID(WildcardMenu.this.player.getName() + " claimed");
            }

            public void returnItem() {
                WildcardMenu.this.returnItem();
            }

            public void closeAfterUnlock() {
                WildcardMenu.this.closeWithDelay();
            }

            public void closeClaimedElsewhere() {
                WildcardMenu.this.close();
                WildcardMenu.this.player.sendMessage(CC.RedB + "(!) " + CC.Red + "Item has been claimed since you're inventory was opened!");
            }
        });
    }

    public void redraw() {
        int index = 0;
        GlobalKitUser user = CosmicGlobalKits.getPlugin().getGlobalKitUser(this.player.getUniqueId());
        Map<GlobalKits, GlobalKitData> kitData = user.getGlobalKits();
        for (GlobalKits kit : GlobalKits.values()) {
            ItemBuilder builder = new ItemBuilder(kit.createMenuItem(kitData, this.player, false));
            if (CosmicGlobalKitsAPI.canLevelUpGkit(this.player, kit)) {
                builder.addLoreLine(new String[]{CC.Green + "Click to unlock or level up!"});
            } else {
                builder.addLoreLine(new String[]{CC.RedB + "MAX LEVEL"});
            }
            this.setItem(index++, builder.build(), () -> this.claim.click(done -> kit.unlockOrLevelUp(this.player, done)));
        }
    }

    public void onInventoryClose() {
        this.claim.inventoryClosed();
    }

    private void returnItem() {
        PlayerUtil.giveOrDropProtectedItem((Player)this.player, (ItemStack)this.itemWrapper.build(), (int)-1);
        ASound.CHICKEN_EGG_POP.play(this.player, 3.0f, 1.1f);
        Bukkit.getScheduler().runTaskLater((Plugin)CosmicGlobalKits.getPlugin(), () -> {
            if (this.player.isValid()) {
                this.player.updateInventory();
            }
        }, 1L);
    }
}

