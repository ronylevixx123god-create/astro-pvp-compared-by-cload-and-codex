package me.astropvp.itemflip.struct;

import me.astropvp.itemflip.inventory.StakesGui;
import org.bukkit.inventory.ItemStack;
import java.util.LinkedList;
import org.bukkit.entity.Player;

public class ItemFlipPlayer
{
    private final Player player;
    private StakesGui stakesGui;
    private ItemFlipColor chosenColor;
    private boolean isConfirmed;
    private LinkedList<ItemStack> wageredItems;

    public ItemFlipPlayer(final Player player, final ItemFlipColor chosenColor) {
        this.player = player;
        this.chosenColor = chosenColor;
        this.wageredItems = new LinkedList<ItemStack>();
    }

    public ItemFlipPlayer(final Player player, final StakesGui stakesGui) {
        this.player = player;
        this.stakesGui = stakesGui;
    }

    public LinkedList<ItemStack> getWageredItems() {
        return this.wageredItems;
    }

    public boolean isConfirmed() {
        return this.isConfirmed;
    }

    public void setConfirmed(final boolean confirmed) {
        this.isConfirmed = confirmed;
    }

    public ItemFlipColor getChosenColor() {
        return this.chosenColor;
    }

    public void setChosenColor(final ItemFlipColor chosenColor) {
        this.chosenColor = chosenColor;
    }

    public void setStakesGui(final StakesGui stakesGui) {
        this.stakesGui = stakesGui;
    }

    public Player getPlayer() {
        return this.player;
    }

    public StakesGui getStakesGui() {
        return this.stakesGui;
    }
}
