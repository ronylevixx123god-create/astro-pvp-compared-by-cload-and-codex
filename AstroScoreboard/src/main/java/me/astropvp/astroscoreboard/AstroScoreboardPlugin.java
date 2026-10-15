package me.astropvp.astroscoreboard;


import com.compilex.CosmicKOTH.CosmicKOTHPlugin;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.Faction;
import me.astropvp.astroscoreboard.board.Scoreboard;
import me.astropvp.astroscoreboard.manager.BoardManager;
import me.astropvp.astroscoreboard.utils.TimeUtils;
import me.astropvp.astrospacegrinding.data.ForceFieldData;
import me.astropvp.astrospacegrinding.data.SpacePlayerData;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import me.astropvp.hotfixplugin.HotFixPlugin;
import me.ifamasssxd.cosmicduel.DuelAPI;
import me.ifamasssxd.cosmicduel.struct.enums.DuelState;
import me.ifamasssxd.cosmicduel.struct.newduel.NewDuel;
import me.ifamasssxd.cosmichcf.api.HCFOutpostAPI;
import me.ifamasssxd.cosmichcf.managers.OutpostManager;
import me.ifamasssxd.cosmichcf.struct.Outpost;
import me.ifamasssxd.cosmichcf.struct.OutpostType;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.objects.TournamentMatch;
import me.neznamy.tab.api.TabAPI;
import net.crankedpvp.shards.MobcoinAPI;
import net.md_5.bungee.api.ProxyServer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;


public class AstroScoreboardPlugin extends JavaPlugin {

    private static AstroScoreboardPlugin astroScoreboardPlugin;
    public static BoardManager boardManager;

    public static void registerListener(Listener listener) {
        Bukkit.getServer().getPluginManager().registerEvents(listener, astroScoreboardPlugin);
    }

    public BoardManager getBoardManager() {
        return boardManager;
    }


    public void onEnable() {
        astroScoreboardPlugin = this;
        registerDefaultAndFactionScoreboard();
        registerStrongHoldScoreboard();
        registerDuelScoreboard();
        //registerSpaceScoreboard();
        registerTournamentScoreboard();
        //registerBungeeScoreboardPlaceholders();
        //registerListener(new ListenerSB());
    }


    @Override
    public void onDisable() {
        astroScoreboardPlugin = null;
    }

    public void registerBungeeScoreboardPlaceholders() {
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%sav_player_count%", getMS(1), player -> ProxyServer.getInstance().getOnlineCount());

    }

    public void registerStrongHoldScoreboard() {
        OutpostManager outpostManager = CosmicKOTHPlugin.get_().getOutpostManager();
        Outpost infernalOutpost = outpostManager.getOutpostFromName("courtyard");
        Outpost frozenOutpost = outpostManager.getOutpostFromName("great_hall");
        //Outpost spaceOutpost = outpostManager.getOutpostFromName("space");

        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%in-infernal-region%", getMS(5), player -> WorldGuardUtils.getRegionName(((Player) player.getPlayer()).getLocation()).contains("infernal"));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%in-frozen-region%", getMS(5), player -> WorldGuardUtils.getRegionName(((Player) player.getPlayer()).getLocation()).contains("frozen"));
        //TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%in-space-sh%", getMS(5), player -> WorldGuardUtils.getRegionName(((Player) player.getPlayer()).getLocation()).contains("space_cap"));

        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%infernal-controlling-faction%", getMS(5), () -> strongHoldGetFaction(infernalOutpost.getDefendingFaction()));
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%frozen-controlling-faction%", getMS(5), () -> strongHoldGetFaction(frozenOutpost.getDefendingFaction()));
        //TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%space-controlling-faction%", getMS(5), () -> strongHoldGetFaction(spaceOutpost.getDefendingFaction()));

        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%infernal-attacking-faction%", getMS(5), () -> strongHoldGetFaction(infernalOutpost.getAttackingFaction()));
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%frozen-attacking-faction%", getMS(5), () -> strongHoldGetFaction(frozenOutpost.getAttackingFaction()));
        //TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%space-attacking-faction%", getMS(5), () -> strongHoldGetFaction(spaceOutpost.getAttackingFaction()));

        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%infernal-status%", getMS(5), () -> infernalOutpost.getOutpostState().getName());
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%frozen-status%", getMS(5), () -> frozenOutpost.getOutpostState().getName());
        //TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%space-status%", getMS(5), () -> spaceOutpost.getOutpostState().getName());

        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%frozen-cap-percent%", getMS(1), () -> round(frozenOutpost.getCapPercentage(), 2));
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%infernal-cap-percent%", getMS(1), () -> round(infernalOutpost.getCapPercentage(), 2));
        //TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%space-cap-percent%", getMS(1), () -> round(spaceOutpost.getCapPercentage(), 2));
    }

    public void registerDefaultAndFactionScoreboard() {
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%has-faction%", getMS(1), player -> !FactionUtils.getFPlayer((Player) player.getPlayer()).getFaction().isWilderness());
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%kills%", getMS(5), player -> FactionUtils.getFPlayer((Player) player.getPlayer()).getKills());
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%deaths%", getMS(5), player -> FactionUtils.getFPlayer((Player) player.getPlayer()).getDeaths());
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%kdr%", getMS(5), player -> getPlayerKDR((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%date%", getMS(86400), Scoreboard::getDateSuffix);
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%exp_format%", getMS(1), player -> NumberUtils.formatMoney(((Player) player.getPlayer()).getTotalExperience()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%ping%", getMS(5), player -> HotFixPlugin.getPing((Player) player.getPlayer()));

    }

    public void registerDuelScoreboard() {
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%in-duel%", getMS(5), player -> inDuel((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%duel-opponent%", getMS(10), player -> DuelAPI.getDuel((Player) player.getPlayer()).getOppositePlayer((Player) player.getPlayer()).getName());
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%duel-runtime%", getMS(1), player -> getDuelRunTime((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%duel-arena%", getMS(10), player -> CC.strip(DuelAPI.getDuel((Player) player.getPlayer()).getArena().getName()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%duel-damage%", getMS(1), player -> getDoubleDamageString(DuelAPI.getDuel((Player) player.getPlayer())));
    }

    public void registerSpaceScoreboard() {
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%in-space%", getMS(5), player -> ((Player) player.getPlayer()).getWorld().getName().equals("space"));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%space-forcefield%", getMS(1), player -> getForceFieldString((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%space-mobs-killed%", getMS(5), player -> getSpaceMobsKilled((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%space-time-spent%", getMS(1), player -> getSpaceTimeSpentString((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%space-money-made%", getMS(5), player -> getSpaceMoneyMadePlayer((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%space-xp-earned%", getMS(5), player -> getSpaceXPEarned((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%mobcoins%", getMS(1), player -> getMobCoins((Player) player.getPlayer()));
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%has-space-sh-string%", getMS(5), player -> getStrongSpaceSh((Player) player.getPlayer()));

    }

    public void registerTournamentScoreboard() {
        TournamentManager tournamentManager = AstroTournamentsPlugin.instance.getTournamentManager();
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%in-tournament%", getMS(1), player -> ((Player) player.getPlayer()).getWorld().getName().equals("tournament_world") && tournamentManager.getTournamentPlayer(player.getUniqueId()).isInTournament());
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%match-string%", getMS(1), player -> getMatchString((Player) player.getPlayer(), tournamentManager));
        TabAPI.getInstance().getPlaceholderManager().registerServerPlaceholder("%participants%", getMS(1), () -> tournamentManager.getTournamentParticipants().size());
        TabAPI.getInstance().getPlaceholderManager().registerPlayerPlaceholder("%position-queue%", getMS(1), player -> tournamentManager.getPlayerQueue().getPosition(tournamentManager.getTournamentPlayer(player.getUniqueId())));

    }




    public String getStrongSpaceSh(Player player) {
        return HCFOutpostAPI.isControllingOutpost(player, OutpostType.SPACE) ? CC.translate("&aON") : CC.translate("&cOFF");
    }
    public String getMatchString(Player player, TournamentManager tournamentManager) {
        for (TournamentMatch match : tournamentManager.getTournamentMatchList()) {
            Player p1 = match.getPlayerOne().getPlayer();
            Player p2 = match.getPlayerTwo().getPlayer();
            if (player.getUniqueId().equals(p1.getUniqueId()) || player.getUniqueId().equals(p2.getUniqueId())) {
                return "&7" + p1.getName() + " &fVS &7" + p2.getName();
            }

        }
        return "&cnone";
    }

    public String getForceFieldString(Player player) {
        ForceFieldData fdata = ForceFieldData.findPlayer(player);
        if (fdata.isActive()) {
            return CC.translate("&aActive &7(" + fdata.getRemainingForceFieldTimeFormatted() + "&7)");
        } else {
            return CC.translate("&cInactive");
        }
    }

    public int getSpaceMobsKilled(Player player) {
        SpacePlayerData data = SpacePlayerData.findPlayer(player);
        return data.getKills(MonsterType.SPACE_ALIEN);
    }

    public String getSpaceMoneyMadePlayer(Player player) {
        SpacePlayerData data = SpacePlayerData.findPlayer(player);
        return NumberUtils.formatMoney(data.getMoneyMade());
    }

    public String getSpaceXPEarned(Player player) {
        SpacePlayerData data = SpacePlayerData.findPlayer(player);
        return NumberUtils.formatMoney(data.getXpMade());
    }

    public String getMobCoins(Player player) {
        return NumberUtils.formatMoney(MobcoinAPI.getBalance(player));
    }

    public String getSpaceTimeSpentString(Player player) {
        SpacePlayerData data = SpacePlayerData.findPlayer(player);
        return TimeUtils.format(data.getTimeInSpace());
    }

    public double getPlayerKDR(Player player) {
        double kdr;
        if (FactionUtils.getFPlayer((Player) player.getPlayer()).getDeaths() == 0) {
            kdr = FactionUtils.getFPlayer((Player) player.getPlayer()).getKills();
        } else {
            kdr = (double) FactionUtils.getFPlayer((Player) player.getPlayer()).getKills() / FactionUtils.getFPlayer((Player) player.getPlayer()).getDeaths();
        }
        return round(kdr, 2);
    }

    public String getDoubleDamageString(NewDuel duel) {
        if (duel.getGameTimer() >= 300) {
            return CC.translate("&a&lEnabled");
        } else {
            return CC.translate("&c&lDisabled");
        }
    }

    public String getDuelRunTime(Player player) {
        int seconds = DuelAPI.getDuel(player).getGameTimer();
        int minutes = seconds / 60;
        seconds = seconds - (minutes * 60);
        if (minutes > 0) {
            return +minutes + "m " + seconds + "s";
        } else {
            return seconds + "s";

        }
    }

    public boolean inDuel(Player player) {
        return DuelAPI.getDuel(player) != null && (DuelAPI.getDuel(player).getDuelState() == DuelState.STARTING || DuelAPI.getDuel(player).getDuelState() == DuelState.PLAYING);
    }

    public String strongHoldGetFaction(Faction faction) {
        if (faction == null) {
            return "N/A";
        } else {
            return faction.getTag();
        }
    }


    public int getMS(int seconds) {
        return seconds * 1000;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }


    public static AstroScoreboardPlugin getInstance() {
        return astroScoreboardPlugin;
    }

}
