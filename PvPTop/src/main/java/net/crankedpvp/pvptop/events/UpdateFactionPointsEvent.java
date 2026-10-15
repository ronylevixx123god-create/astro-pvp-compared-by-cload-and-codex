package net.crankedpvp.pvptop.events;

import com.massivecraft.factions.Faction;
import lombok.Getter;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class UpdateFactionPointsEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled = false;

    @Getter
    private final Faction faction;

    @Getter
    private final long oldPoints;

    @Getter
    private final long newPoints;

    public UpdateFactionPointsEvent(Faction faction, long oldPoints, long newPoints) {
        this.oldPoints = oldPoints;
        this.newPoints = newPoints;
        this.faction = faction;
    }

    @Override
    public boolean isCancelled() {
        return this.canceled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.canceled = b;
    }

    @Override
    public HandlerList getHandlers() {
        return UpdateFactionPointsEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return UpdateFactionPointsEvent.handlers;
    }

}
