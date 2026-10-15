package me.astropvp.astrospacegrinding.commands;

import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.astropvp.astrospacegrinding.AstroSpaceGrindingAPI;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

public class CommandAdminSpace implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if(!(sender instanceof Player) || !sender.isOp()) {
			return false;
		}
		
		Player player = (Player) sender;
		
		if(args.length >= 2 && args[0].equalsIgnoreCase("addlocation")) {
			MonsterType type = MonsterType.valueOf(args[1]);
			Location location = player.getLocation();
			AstroSpaceGrindingAPI.addLocation(type, location);
			player.sendMessage(CC.GreenB + "(!) " + CC.Green + "Added New Space Grinding Location at your location.");
			return false;
		}
		
		if(args.length >= 2 && args[0].equalsIgnoreCase("removelocations")) {
			MonsterType type = MonsterType.valueOf(args[1]);
			AstroSpaceGrindingAPI.removeLocations(type);
			player.sendMessage(CC.GreenB + "(!) " + CC.Green + "Removed all instances of the Space Monster '" + type.getName() + "'!");
			return false;
		}

		if(args.length >= 1 && args[0].equalsIgnoreCase("reload")) {
			AstroSpaceGrindingPlugin.getInstance().getConfigManager().reloadFiles();
			sender.sendMessage(CC.translate("&aReloaded config..."));
			return false;
		}

		if (args.length >= 2 && args[0].equalsIgnoreCase("viewitems")) {
			MonsterType type = MonsterType.valueOf(args[1].toUpperCase());
			player.openInventory(AstroSpaceGrindingAPI.getLootInventory(type));
			return false;
		}

		
		player.sendMessage("");
		player.sendMessage(CC.translate("&a&lAdmin Space Commands"));
		player.sendMessage(CC.translate( "&f* &a/aspace addlocation <monstertype>"));
		player.sendMessage(CC.translate( "&f* &a/aspace removelocations <monstertype>"));
		player.sendMessage(CC.translate( "&f* &a/aspace viewitems"));
		player.sendMessage(CC.translate("&f* &a/aspace reload"));
		player.sendMessage("");
		player.sendMessage(CC.translate("&a&lAvailable Monster Types:"));
		for (MonsterType type : MonsterType.values()) {
			player.sendMessage(CC.translate("&f* &a" + type.name()));
		}
		player.sendMessage("");
		return false;
	}

}
