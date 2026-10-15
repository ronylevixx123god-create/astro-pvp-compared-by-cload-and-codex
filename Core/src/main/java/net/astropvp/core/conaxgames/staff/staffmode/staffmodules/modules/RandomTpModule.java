package net.astropvp.core.conaxgames.staff.staffmode.staffmodules.modules;

/*
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import net.astropvp.core.conaxgames.staff.staffmode.StaffMember;
import net.astropvp.core.conaxgames.staff.staffmode.staffmodules.moduletypes.InteractModule;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.Random;

public class RandomTpModule extends InteractModule {

    public RandomTpModule() {
        super("Random TP", Material.MINECART, 3, "&7Right-Click this item to", "&7teleport to a random player on the server");
    }

    @Override
    protected void onPlayerInteract(PlayerInteractEvent event) {
        Player target = null;
        StaffMember staffMember = super.getStaffModeManager().getStaffMember(event.getPlayer());

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!WorldGuardUtils.getRegionsIn(player.getLocation()).contains("spawn_region")) {
                target = player;
            }
        }
        if (target == null) {
            staffMember.sendMessage(CC.translate("&cNo Valid Target found!"));
        }

        staffMember.getPlayer().teleport(target, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }
}

 */
