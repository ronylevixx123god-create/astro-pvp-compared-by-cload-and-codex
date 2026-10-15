package net.astropvp.astroevents.game;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.astroevents.utils.PlayerInventoryContents;
import net.astropvp.astroevents.utils.Utils;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class MiniGamePlayer {

    private final Player player;
    @Setter
    private long joinGameTime;
    private final PlayerInventoryContents playerInventoryContents;
    @Setter
    private boolean damageEnabled;

    public MiniGamePlayer(Player player) {
        this.player = player;
        this.joinGameTime = System.currentTimeMillis();
        this.playerInventoryContents = new PlayerInventoryContents(player.getInventory().getContents().clone(), player.getInventory().getArmorContents().clone());
        this.damageEnabled = false;
    }

    public void sendMessage(String text) {
        this.player.sendMessage(Utils.color(text));
    }

    public UUID getUUID() {
        return this.player.getUniqueId();
    }
}