package net.astropvp.adminitems.events;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.adminitems.struct.AdminItem;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerInteractEvent;

@Getter
public class AdminItemUseEvent extends Event implements Cancellable {

    @Setter
    private boolean cancelled;
    private final Player player;
    private final AdminItem adminItem;
    private final PlayerInteractEvent playerInteractEvent;
    private final HandlerList handlers;

    public AdminItemUseEvent(Player player, AdminItem adminItem, PlayerInteractEvent event) {
        this.player = player;
        this.adminItem = adminItem;
        this.playerInteractEvent = event;
        this.cancelled = false;
        this.handlers = new HandlerList();
    }

}
