package me.astropvp.astrospawners.guis;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import dev.triumphteam.gui.guis.GuiItem;
import dev.triumphteam.gui.guis.PaginatedGui;
import lombok.Getter;
import me.astropvp.astrospawners.AstroSpawners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.Random;
import java.util.UUID;

@Getter
public class AntiAFKGrindingGUI {
    private final PaginatedGui antiAfkGrindingGui;
    private final AstroSpawners astroSpawners;
    private final Player player;

    public AntiAFKGrindingGUI(Player player) {
        this.astroSpawners = AstroSpawners.getInstance();
        this.antiAfkGrindingGui = new PaginatedGui(1, 1, CC.translate("&8Anti AFK Grinding Verification"));
        this.player = player;
        UUID uuid = player.getUniqueId();
        if (!astroSpawners.getWaitingForAfkGrindingVerification().contains(uuid)) {
            astroSpawners.getWaitingForAfkGrindingVerification().add(uuid);
        }


        this.antiAfkGrindingGui.setDefaultClickAction(event -> event.setCancelled(true));
        Random random = new Random();
        int randomInt = random.nextInt(9) + 1;
        this.antiAfkGrindingGui.setItem(1, randomInt, new GuiItem(new ItemBuilder(Material.NETHER_STAR, CC.translate("&a&lVerify!")).addLoreLine(CC.translate("&7Click here to verify you are not afk grinding!")).build(), event -> {
            astroSpawners.getAfkGrinders().put(uuid, 1);
            astroSpawners.getWaitingForAfkGrindingVerification().remove(uuid);
            player.sendMessage(CC.translate("&a&l(!)&a You have completed the anti afk grinding captcha! You can now enjoy grinding again."));
            player.closeInventory();
        }));
    }


}
