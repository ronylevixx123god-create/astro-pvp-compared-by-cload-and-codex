package net.crankedpvp.shards.events;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.crankedpvp.shards.struct.MobcoinBalanceUpdateType;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@RequiredArgsConstructor
@Getter
public class PlayerMobcoinBalanceUpdateEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private boolean canceled = false;

    @Getter
    private final long oldBalance;

    @Getter
    private final long newBalance;

    @Getter
    private final MobcoinBalanceUpdateType shardBalanceUpdateType;

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
        return PlayerMobcoinBalanceUpdateEvent.handlers;
    }

    public static HandlerList getHandlerList() {
        return PlayerMobcoinBalanceUpdateEvent.handlers;
    }

}
