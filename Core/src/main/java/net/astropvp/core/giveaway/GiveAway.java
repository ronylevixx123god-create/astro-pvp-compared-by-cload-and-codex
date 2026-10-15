package net.astropvp.core.giveaway;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Setter
@Getter
public class GiveAway {
    public int winnersAmount;
    public ItemStack prize;
    public int giveAwayTaskId;
    public int timeInSeconds;
    public List<UUID> playersJoined;

    public GiveAway(int winnersAmount, ItemStack prize, int timeInSeconds) {
        this.winnersAmount = winnersAmount;
        this.prize = prize;
        this.timeInSeconds = timeInSeconds;
        this.playersJoined = new ArrayList<>();
        this.giveAwayTaskId = -1;

    }


}
