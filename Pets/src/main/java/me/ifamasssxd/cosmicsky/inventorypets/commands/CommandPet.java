package me.ifamasssxd.cosmicsky.inventorypets.commands;

import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.menu.PetHelpMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandPet implements CommandExecutor {
   public boolean onCommand(CommandSender sender, Command cmd, String lbl, String[] args) {
      (new PetHelpMenu((Player)sender)).openGUI(CosmicSkyInventoryPets.get());
      return false;
   }
}
