package net.astropvp.core.conaxgames.staff.staffmode.staffmodules.modules;

/*
import com.cosmicpvp.cosmicutils.utils.CC;
import net.astropvp.core.conaxgames.staff.staffmode.StaffMember;
import net.astropvp.core.conaxgames.staff.staffmode.staffmodules.moduletypes.InteractModule;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class VanishModule extends InteractModule {

    public VanishModule() {
        super("Vanish", Material.SLIME_BALL, 5, "&7Right-Click this item to", "&7become invisible to other players");
    }

    @Override
    protected void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        StaffMember staffMember = super.getStaffModeManager().getStaffMember(player);

        staffMember.toggleVanish();

        String status = super.getStaffModeManager().getStaffMember(player).isVanished() ? "&a&lENABLED" : "&c&lDISABLED";

        player.sendMessage(CC.translate("&8[&a!&8] &7Vanish has been " + status));
    }
}


 */