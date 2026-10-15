package net.astropvp.customblocks.struct.abilities;

import org.bukkit.entity.Player;

public interface ActivateAble {
    boolean isActive();
    void activate(Player player);
    double getActivationPrice();
    void setLastActivated(long lastActivated);

}
