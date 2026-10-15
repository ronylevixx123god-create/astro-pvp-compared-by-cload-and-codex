package net.crankedpvp.arcade.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class SignListener implements Listener {

    @EventHandler
    public void onSign(SignChangeEvent e) {
        int currentLine = 0;
        for (String line : e.getLines()) {
            if (line.equalsIgnoreCase("[Elevator]") && !e.getPlayer().isOp()) {
                e.setCancelled(true);
                return;
            }
            if (line.equalsIgnoreCase("[Elevator]")) {
                if (currentLine < 3) {
                    if (e.getLine(currentLine + 1).equalsIgnoreCase("Up")) {
                        e.setLine(0, "");
                        e.setLine(1, ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"));
                        e.setLine(2, ChatColor.translateAlternateColorCodes('&', "Up"));
                        e.setLine(3, "");
                    } else {
                        e.getBlock().breakNaturally();
                        e.getPlayer().sendMessage(CC.translate("&cInvalid Direction!"));
                    }
                }
            }
            currentLine++;
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if(e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        if (e.getClickedBlock() != null) {
            if (e.getClickedBlock().getState() instanceof Sign) {
                Sign sign = (Sign) e.getClickedBlock().getState();

                if (sign.getLine(1).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"))) {
                    boolean teleported = false;
                    if (sign.getLine(2).equalsIgnoreCase("Up")) {
                        for (int i = e.getClickedBlock().getY() + 2; i < 256; i++) {
                            BlockState state = e.getClickedBlock().getWorld().getBlockAt(new Location(e.getClickedBlock().getWorld(), e.getClickedBlock().getX(), i, e.getClickedBlock().getZ())).getState();
                            if (state instanceof Sign) {
                                if (((Sign) state).getLine(1).equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&9[Elevator]"))) {
                                    e.getPlayer().teleport(new Location(state.getWorld(), state.getLocation().getBlockX() + 0.5, state.getLocation().getBlockY(), state.getLocation().getBlockZ() + 0.5, e.getPlayer().getLocation().getYaw(), e.getPlayer().getLocation().getPitch()), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                                    teleported = true;
                                    break;
                                }
                            }
                        }
                    }
                    //if(!teleported) e.getPlayer().sendMessage(CC.translate("&cNo Elevator Sign found!"));
                }
            }
        }
    }
}
