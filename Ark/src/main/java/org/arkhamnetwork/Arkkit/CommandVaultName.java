package org.arkhamnetwork.Arkkit;

import com.drtshock.playervaults.vaultmanagement.VaultOperations;
import com.google.common.collect.Lists;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.libs.AbstractCommand;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandVaultName
        extends AbstractCommand {
    private final PlayerVaultGUIManager guiManager;

    public CommandVaultName(PlayerVaultGUIManager manager) {
        super("namevault", "/<command> <vaultNumber> <name>", "Rename your vaults", Lists.newArrayList("vaultname", "renamevault"));
        this.guiManager = manager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage(ChatColor.RED + "Invalid Usage: /namevault <vaultNumber> <name>");
            return true;
        }
        if (StringUtils.isNumeric(args[0])) {
            int vaultId = Integer.parseInt(args[0]);
            if (vaultId < 1 || vaultId > 100) {
                sender.sendMessage(ChatColor.RED + "Invalid Vault ID.");
                return true;
            }
            Player player = (Player) sender;
            if (!VaultOperations.checkPerms(sender, vaultId)) {
                sender.sendMessage(ChatColor.RED + "You do not have access to that Vault number!");
                return true;
            }
            CustomPlayerVaults vaults = (CustomPlayerVaults) this.guiManager.getCustomPvs().computeIfAbsent(player.getUniqueId(), e -> new CustomPlayerVaults());
            CustomPV pv = vaults.computeIfAbsent(vaultId, newPV -> new CustomPV());
            StringBuilder newNameBuilder = new StringBuilder();
            for (int i = 1; i < args.length; ++i) {
                newNameBuilder.append(args[i]);
            }
            String done = newNameBuilder.toString().trim();
            if (done.length() >= 32) {
                player.sendMessage(ChatColor.RED + "Vault names cannot be longer than 32 characters.");
                return true;
            }
            pv.setCustomName(done);
            player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "(!) " + ChatColor.GREEN + "Vault #" + vaultId + " renamed to " + done);
            Bukkit.getScheduler().scheduleSyncDelayedTask(Arkkit.get(), () -> player.chat("/pv"));
            return true;
        }
        sender.sendMessage(ChatColor.RED + "Invalid Usage: /namevault <vaultNumber> <name>");
        return false;
    }
}

