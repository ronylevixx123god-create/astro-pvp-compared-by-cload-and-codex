package me.astropvp.itemflip.struct;

import java.util.LinkedList;
import java.util.Collections;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Arrays;

import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import me.astropvp.itemflip.AstroItemFlipPlugin;
import me.astropvp.itemflip.inventory.CoinFlipGui;
import me.astropvp.itemflip.inventory.StakesGui;
import me.astropvp.itemflip.tasks.CountdownRunnable;
import org.bukkit.plugin.Plugin;
import java.util.UUID;
import org.bukkit.entity.Player;

import java.util.List;

public class ItemFlipMatch
{
    public static final List<Integer> PLAYER_ONE_ITEM_SLOTS;
    public static final List<Integer> PLAYER_TWO_ITEM_SLOTS;
    private final AstroItemFlipPlugin plugin;
    private ItemFlipPlayer ifPlayerOne;
    private ItemFlipPlayer ifPlayerTwo;
    private CoinFlipGui coinFlipGui;
    private Player playerOne;
    private Player playerTwo;
    private UUID playerOneUUID;
    private UUID playerTwoUUID;
    private boolean inCoinFlip;
    private boolean isCancelled;
    private boolean gavePrize;
    private MatchState coinFlipState;
    private ItemFlipColor winnerColor;
    private int countdownTimeLeft;
    private CountdownRunnable countdownRunnable;

    public ItemFlipMatch(final AstroItemFlipPlugin plugin, final Player playerOne, final Player playerTwo) {
        this.plugin = plugin;
        this.ifPlayerOne = new ItemFlipPlayer(playerOne, this.getRandomColor());
        this.ifPlayerTwo = new ItemFlipPlayer(playerTwo, this.getRandomColor());
        this.countdownTimeLeft = 10;
        this.coinFlipState = MatchState.WAITING;
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.playerOneUUID = playerOne.getUniqueId();
        this.playerTwoUUID = playerTwo.getUniqueId();
        (this.countdownRunnable = new CountdownRunnable(this, plugin)).runTaskTimer((Plugin)this.plugin, 0L, 1L);
    }

    public void cancel() {
        ItemFlipManager.getMatches().remove(this);
        this.countdownRunnable.cancel();
        this.isCancelled = true;
        this.playerOne.getOpenInventory().close();
        this.playerTwo.getOpenInventory().close();
    }

    public void start() {
        this.ifPlayerOne.setStakesGui(new StakesGui(this.plugin.getInventoryManager(), this, this.playerOne));
        this.ifPlayerTwo.setStakesGui(new StakesGui(this.plugin.getInventoryManager(), this, this.playerTwo));
        this.ifPlayerOne.getStakesGui().getInventory().open(this.playerOne);
        this.ifPlayerTwo.getStakesGui().getInventory().open(this.playerTwo);
    }

    public int getCountdownTimeLeft() {
        return this.countdownTimeLeft;
    }

    public void setCountdownTimeLeft(final int countdownTimeLeft) {
        this.countdownTimeLeft = countdownTimeLeft;
    }

    public CountdownRunnable getCountdownRunnable() {
        return this.countdownRunnable;
    }

    public CoinFlipGui getCoinFlipGui() {
        return this.coinFlipGui;
    }

    public void setCoinFlipGui(final CoinFlipGui coinFlipGui) {
        this.coinFlipGui = coinFlipGui;
    }

    public Player getPlayerOne() {
        return this.playerOne;
    }

    public Player getPlayerTwo() {
        return this.playerTwo;
    }

    public List<Player> getPlayers() {
        return new ArrayList<Player>(Arrays.asList(this.getPlayerOne(), this.getPlayerTwo()));
    }

    public boolean isCancelled() {
        return this.isCancelled;
    }

    public boolean isInCoinFlip() {
        return this.inCoinFlip;
    }

    public boolean isGavePrize() {
        return this.gavePrize;
    }

    public void setGavePrize(final boolean gavePrize) {
        this.gavePrize = gavePrize;
    }

    public StakesGui getPlayerOneStakesGui() {
        return this.ifPlayerOne.getStakesGui();
    }

    public StakesGui getPlayerTwoStakesGui() {
        return this.ifPlayerTwo.getStakesGui();
    }

    public void setInCoinFlip(final boolean inCoinFlip) {
        this.inCoinFlip = inCoinFlip;
    }

    public ItemFlipPlayer getIfPlayerOne() {
        return this.ifPlayerOne;
    }

    public ItemFlipPlayer getIfPlayerTwo() {
        return this.ifPlayerTwo;
    }

    public MatchState getCoinFlipState() {
        return this.coinFlipState;
    }

    public void setCoinFlipState(final MatchState coinFlipState) {
        this.coinFlipState = coinFlipState;
    }

    public ItemFlipColor getWinnerColor() {
        return this.winnerColor;
    }

    public void setWinnerColor(final ItemFlipColor winnerColor) {
        this.winnerColor = winnerColor;
    }

    public ItemFlipPlayer getIfPlayer(final Player player) {
        if (player.getUniqueId().equals(this.ifPlayerOne.getPlayer().getUniqueId())) {
            return this.ifPlayerOne;
        }
        if (player.getUniqueId().equals(this.ifPlayerTwo.getPlayer().getUniqueId())) {
            return this.ifPlayerTwo;
        }
        return null;
    }

    public Player getPlayerByColor(final ItemFlipColor color) {
        if (this.ifPlayerOne.getChosenColor() == color) {
            return this.playerOne;
        }
        if (this.ifPlayerTwo.getChosenColor() == color) {
            return this.playerTwo;
        }
        return null;
    }

    private ItemFlipColor getRandomColor() {
        final List<ItemFlipColor> colors = new ArrayList<ItemFlipColor>(Arrays.asList(ItemFlipColor.values()));
        if (this.getIfPlayerOne() != null && this.getIfPlayerOne().getChosenColor() != null) {
            colors.remove(this.getIfPlayerOne().getChosenColor());
        }
        if (this.getIfPlayerTwo() != null && this.getIfPlayerTwo().getChosenColor() != null) {
            colors.remove(this.getIfPlayerTwo().getChosenColor());
        }
        Collections.shuffle(colors);
        return colors.get(0);
    }

    public void redrawAllPlayers() {
        this.ifPlayerOne.getStakesGui().init(this.playerOne, (InventoryContents)new InventoryContents.Impl(this.ifPlayerOne.getStakesGui().getInventory(), this.playerOne));
        this.ifPlayerTwo.getStakesGui().init(this.playerTwo, (InventoryContents)new InventoryContents.Impl(this.ifPlayerTwo.getStakesGui().getInventory(), this.playerTwo));
    }

    public boolean isColorTaken(final ItemFlipColor color) {
        return (this.getIfPlayerOne().getChosenColor() != null && this.getIfPlayerOne().getChosenColor().equals(color)) || (this.getIfPlayerTwo().getChosenColor() != null && this.getIfPlayerTwo().getChosenColor().equals(color));
    }

    public boolean containsPlayer(final Player player) {
        return this.playerOneUUID.equals(player.getUniqueId()) || this.playerTwoUUID.equals(player.getUniqueId());
    }

    public Player getOppositePlayer(final Player player) {
        if (player.getUniqueId().equals(this.playerOne.getUniqueId())) {
            return this.playerTwo;
        }
        return this.playerOne;
    }

    static {
        PLAYER_ONE_ITEM_SLOTS = new LinkedList<Integer>() {
            {
                this.add(1);
                this.add(2);
                this.add(3);
                this.add(9);
                this.add(10);
                this.add(11);
                this.add(12);
                this.add(18);
                this.add(19);
                this.add(20);
                this.add(21);
                this.add(27);
                this.add(28);
                this.add(29);
                this.add(30);
            }
        };
        PLAYER_TWO_ITEM_SLOTS = new LinkedList<Integer>() {
            {
                this.add(5);
                this.add(6);
                this.add(7);
                this.add(14);
                this.add(15);
                this.add(16);
                this.add(17);
                this.add(23);
                this.add(24);
                this.add(25);
                this.add(26);
                this.add(32);
                this.add(33);
                this.add(34);
                this.add(35);
            }
        };
    }

    public enum MatchState
    {
        WAITING,
        STARTING,
        FLIPPING,
        DONE;
    }
}
