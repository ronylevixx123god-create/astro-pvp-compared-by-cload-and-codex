package net.crankedpvp.staff.struct;

import com.cosmicpvp.cosmicutils.utils.CC;
import de.tr7zw.nbtapi.NBTItem;
import net.astropvp.core.AstroCore;
import net.crankedpvp.staff.managers.StaffManager;
import net.crankedpvp.staff.utils.ItemBuilder;
import net.crankedpvp.staff.utils.PlayerInventoryContents;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StaffMode {

    private final Player player;
    private final StaffManager staffManager;
    private final PlayerInventoryContents playerInventoryContents;

    public StaffMode(Player player) {
        this.player = player;
        this.staffManager = AstroCore.getInstance().getStaffManager();
        this.playerInventoryContents = new PlayerInventoryContents(player.getInventory().getContents(), player.getInventory().getArmorContents());
    }

    public void putPlayerInStaffMode() {
        this.staffManager.putPlayerInStaffMode(this.player, this);
        this.player.sendMessage(CC.translate(AstroCore.prefix + "&eYou have entered staff mode"));
        this.player.playSound(this.player.getLocation(), Sound.SUCCESSFUL_HIT, 2.0F, 2.0F);
        this.completelyClearPlayersInventory();
        this.player.setGameMode(GameMode.CREATIVE);
        this.addRandomPlayerSelector();
        this.addVanish();
    }

    public void removePlayerFromStaffMode() {
        this.player.sendMessage(CC.translate(AstroCore.prefix + "&eYou have left staff mode"));
        this.player.playSound(this.player.getLocation(), Sound.BAT_TAKEOFF, 2.0F, 2.0F);
        this.staffManager.removePlayerFromStaffMode(this.player);
        this.player.setGameMode(GameMode.SURVIVAL);
        this.reinstateInventory();
    }

    private void reinstateInventory() {
        this.playerInventoryContents.restore(this.player);
    }

    private void addRandomPlayerSelector() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.COMPASS, "&a&lRandom Player Teleporter",
                "&7Right-Click this compass to teleport to",
                "&7a random player outside of the safezone");
        NBTItem nbtItem = new NBTItem(itemBuilder.colorize().build());
        nbtItem.setString("staffModeItem", "randomPlayerSelector");
        this.player.getInventory().setItem(4, nbtItem.getItem());
    }

    private void addVanish() {
        ItemBuilder itemBuilder = new ItemBuilder(Material.FEATHER, "&b&lVanish",
                "&7Right-Click this to become invisible",
                "&7to regular players");
        NBTItem nbtItem = new NBTItem(itemBuilder.colorize().build());
        nbtItem.setString("staffModeItem", "vanish");
        this.player.getInventory().setItem(0, nbtItem.getItem());
    }

    private void completelyClearPlayersInventory() {
        this.player.getInventory().setArmorContents(new ItemStack[4]);
        this.player.getInventory().clear();
    }

}
