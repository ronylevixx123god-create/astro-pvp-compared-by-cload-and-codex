package me.astropvp.itemflip.tasks;

import java.util.concurrent.ThreadLocalRandom;

import me.astropvp.itemflip.AstroItemFlipPlugin;
import me.astropvp.itemflip.inventory.CoinFlipGui;
import me.astropvp.itemflip.struct.ItemFlipMatch;
import org.bukkit.scheduler.BukkitRunnable;

public class CountdownRunnable extends BukkitRunnable
{
    private AstroItemFlipPlugin plugin;
    private ItemFlipMatch match;
    private boolean stop;
    private boolean run;
    private int counter;

    public CountdownRunnable(final ItemFlipMatch match, final AstroItemFlipPlugin plugin) {
        this.counter = 0;
        this.plugin = plugin;
        this.match = match;
    }

    public void doStop() {
        this.stop = true;
    }

    public void doRun() {
        this.run = true;
    }

    public void run() {
        this.run = (this.match.getIfPlayerOne().isConfirmed() && this.match.getIfPlayerTwo().isConfirmed());
        if (this.stop) {
            this.stop = false;
            this.run = false;
            this.match.setCountdownTimeLeft(10);
            this.match.getIfPlayerOne().setConfirmed(false);
            this.match.getIfPlayerTwo().setConfirmed(false);
            this.match.redrawAllPlayers();
        }
        if (this.run) {
            ++this.counter;
            if (this.counter >= 20) {
                this.counter = 0;
                if (this.match.getCountdownTimeLeft() != 1) {
                    this.match.setCountdownTimeLeft(this.match.getCountdownTimeLeft() - 1);
                    this.match.redrawAllPlayers();
                }
                else {
                    final CoinFlipGui coinFlipGui = new CoinFlipGui(this.match, this.plugin);
                    this.match.setCoinFlipGui(coinFlipGui);
                    this.match.setInCoinFlip(true);
                    this.match.getPlayers().forEach(p -> coinFlipGui.getInventory().open(p));
                    if (ThreadLocalRandom.current().nextBoolean()) {
                        this.match.setWinnerColor(this.match.getIfPlayerOne().getChosenColor());
                    }
                    else {
                        this.match.setWinnerColor(this.match.getIfPlayerTwo().getChosenColor());
                    }
                    this.cancel();
                }
            }
        }
    }
}
