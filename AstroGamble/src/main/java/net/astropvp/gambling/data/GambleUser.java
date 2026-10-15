package net.astropvp.gambling.data;

import lombok.Getter;
import lombok.Setter;
import net.astropvp.gambling.GamblingPlugin;
import net.astropvp.storage.utils.KeyToSave;


import java.util.UUID;

@Getter
@Setter
public class GambleUser implements KeyToSave {
    private UUID uuid;

    private int bombsWins;
    private int bombsLosses;
    private double bombsWinnings;
    private double bombsLosings;

    private int minesWins;
    private int minesLosses;
    private double minesWinnings;
    private double minesLosings;

    private int rouletteWins;
    private int rouletteLosses;
    private double rouletteWinnings;
    private double rouletteLosings;

    private int towerWins;
    private int towerLosses;
    private double towerWinnings;
    private double towerLosings;

    private int crashWins;
    private int crashLosses;
    private double crashWinnings;
    private double crashLosings;
    private double crashAutoPayout;
    private boolean crashSound;

    //private CrashGraph crashGraph;

    public GambleUser(UUID uuid) {
        this.uuid = uuid;


        this.minesWins = 0;
        this.minesWinnings = 0;
        this.minesLosings = 0;
        this.minesLosses = 0;

        this.towerWins = 0;
        this.towerWinnings = 0;
        this.towerLosings = 0;
        this.towerLosses = 0;


        this.bombsWins = 0;
        this.bombsWinnings = 0;
        this.bombsLosings = 0;
        this.bombsLosses = 0;

        this.crashWins = 0;
        this.crashWinnings = 0;
        this.crashLosings = 0;
        this.crashLosses = 0;
        this.crashAutoPayout = 2;
        this.crashSound = true;

        this.rouletteWins = 0;
        this.rouletteWinnings = 0;
        this.rouletteLosings = 0;
        this.rouletteLosses = 0;

        //this.crashGraph = new CrashGraph(uuid, new ArrayList<>());
    }






    public void addBombsWin(double amountWon) {
        this.bombsWins++;
        this.bombsWinnings +=amountWon;
        GamblingPlugin.getInstance().setOverallBombsWinnings(Math.round(GamblingPlugin.getInstance().getOverallBombsWinnings() + amountWon));
    }

    public void addBombsLoss(double amountLost) {
        this.bombsLosses++;
        this.bombsLosings +=amountLost;
        GamblingPlugin.getInstance().setOverallBombsLosings(Math.round(GamblingPlugin.getInstance().getOverallBombsLosings() + amountLost));
    }

    public String getBombsWinLossRatio(String delimeter) {
        return this.getBombsWins() + delimeter + this.getBombsLosses();
    }






    public void addTowerWin(double amountWon) {
        this.towerWins++;
        this.towerWinnings +=amountWon;
        GamblingPlugin.getInstance().setOverallTowerWinnings(Math.round(GamblingPlugin.getInstance().getOverallTowerWinnings() + amountWon));
    }

    public void addTowerLoss(double amountLost) {
        this.towerLosses++;
        this.towerLosings +=amountLost;
        GamblingPlugin.getInstance().setOverallTowerLosings(Math.round(GamblingPlugin.getInstance().getOverallTowerLosings() + amountLost));
    }

    public String getTowerWinLossRatio(String delimeter) {
        return this.getTowerWins() + delimeter + this.getTowerLosses();
    }







    public void addMinesWin(double amountWon) {
        this.minesWins++;
        this.minesWinnings +=amountWon;
        GamblingPlugin.getInstance().setOverallMinesWinnings(Math.round(GamblingPlugin.getInstance().getOverallMinesWinnings() + amountWon));
    }

    public void addMinesLoss(double amountLost) {
        this.minesLosses++;
        this.minesLosings +=amountLost;
        GamblingPlugin.getInstance().setOverallMinesLosings(Math.round(GamblingPlugin.getInstance().getOverallMinesLosings() + amountLost));
    }

    public String getMinesWinLossRatio(String delimeter) {
        return this.getMinesWins() + delimeter + this.getMinesLosses();
    }



    public void setCrashAutoPayout(double payout) {
        this.crashAutoPayout = payout;
    }

    public void addCrashWin(double amountWon) {
        this.crashWins++;
        this.crashWinnings += amountWon;
        GamblingPlugin.getInstance().setOverallCrashWinnings(Math.round(GamblingPlugin.getInstance().getOverallCrashWinnings() + amountWon));
    }

    public void addCrashLoss(double amountLost) {
        this.crashLosses++;
        this.crashLosings += amountLost;
        GamblingPlugin.getInstance().setOverallCrashLosings(Math.round(GamblingPlugin.getInstance().getOverallCrashLosings() + amountLost));
    }

    public String getCrashWinLossRatio(String delimeter) {
        return this.getCrashWins() + delimeter + this.getCrashLosses();
    }



    public void addRouletteWin(double amountWon) {
        this.rouletteWins++;
        this.rouletteWinnings += amountWon;
    }

    public void addRouletteLoss(double amountLost) {
        this.rouletteLosses++;
        this.rouletteLosings += amountLost;
    }

    public String getRouletteWinLossRatio(String delimeter) {
        return this.getRouletteWins() + delimeter + this.getRouletteLosses();
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }

}
