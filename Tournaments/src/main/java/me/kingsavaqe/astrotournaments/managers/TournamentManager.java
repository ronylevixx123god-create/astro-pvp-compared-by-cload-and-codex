package me.kingsavaqe.astrotournaments.managers;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import lombok.Setter;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicmysterychest.CosmicMysteryChest;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.objects.TournamentMatch;
import me.kingsavaqe.astrotournaments.objects.TournamentPlayer;
import me.kingsavaqe.astrotournaments.utils.*;
import me.kingsavaqe.astrotournaments.utils.Queue;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.core.killtracker.KillTrackerAPI;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.Potion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionType;

import java.util.*;

@Getter
@Setter
public class TournamentManager extends SaveLoadManager<TournamentPlayer> {

    private final AstroTournamentsPlugin astroTournamentsPlugin;

    private final List<TournamentPlayer> tournamentParticipants;
    private final List<TournamentMatch> tournamentMatchList;
    public Queue<TournamentPlayer> playerQueue;


    private final List<SkinType> bannedSkins;
    private final List<ArmorSetType> currentArmorCrystal;
    private boolean active;
    private boolean joinable;
    private Location waitingRoomLocation;

    public TournamentManager(AstroTournamentsPlugin astroTournamentsPlugin) {
        super(astroTournamentsPlugin, StoragePlugin.getInstance().getGson(), "data/", TournamentPlayer.class);
        this.astroTournamentsPlugin = astroTournamentsPlugin;
        this.tournamentMatchList = new ArrayList<>();
        this.tournamentParticipants = new ArrayList<>();
        this.bannedSkins = loadBannedSkinsFromConfig();
        this.currentArmorCrystal = loadCurrentArmorCrystalFromConfig();
    }


    public void setupQueue() {
        tournamentParticipants.forEach(player -> {
            playerQueue.enqueue(player);
        });
    }

    public TournamentPlayer getTournamentPlayer(UUID uuid) {
        if (!contains(uuid.toString())) {
            TournamentPlayer tournamentPlayer = new TournamentPlayer(uuid);
            addToCache(tournamentPlayer);
        }
        return getFromCache(uuid.toString());
    }


    public void putNextPlayersInArenas() {

        Queue<TournamentPlayer> playersInTournament = getPlayerQueue();

        this.astroTournamentsPlugin.getArenaRegister().getArenas().values().forEach(arena -> {

            if (astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().size() == 1) {
                TournamentPlayer winnerTournamentPlayer = astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().get(0);
                Player winner = winnerTournamentPlayer.getPlayer();
                Bukkit.broadcastMessage("");
                Bukkit.broadcastMessage(Utils.fixColor("&6&lAstro&f&lPvP &6&lTournaments"));
                Bukkit.broadcastMessage(Utils.fixColor(" &f&l* &fTournament Winner: &e" + winner.getName()));
                Bukkit.broadcastMessage("");
                astroTournamentsPlugin.getTournamentManager().getTournamentMatchList().clear();
                astroTournamentsPlugin.getTournamentManager().getPlayerQueue().clear();
                astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().clear();
                astroTournamentsPlugin.getTournamentManager().setJoinable(false);
                astroTournamentsPlugin.getTournamentManager().setActive(false);
                winner.teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
                winner.getInventory().addItem(CosmicMysteryChest.get().getChestManager().getMysteryChestMap().get("TournamentLootbox").createClaimableItem(false));
                winnerTournamentPlayer.increaseWins();
                winnerTournamentPlayer.setInTournament(false);
                return;
            }

            if (!arena.isInUse() && playersInTournament.size() >= 2) {

                TournamentPlayer player1 = playersInTournament.dequeue();
                TournamentPlayer player2 = playersInTournament.dequeue();
                arena.setCurrentlyFighting(new Pair<>(player1, player2));
                arena.setInUse(true);
                this.astroTournamentsPlugin.getTournamentManager().getTournamentMatchList().add(new TournamentMatch(player1, player2, arena));
                Bukkit.getScheduler().runTaskLater(astroTournamentsPlugin, ()-> {
                    setupPlayer(player1, arena.getLocations().getLeft());
                    setupPlayer(player2, arena.getLocations().getRight());
                }, 40L);
            }
        });

    }

    private void setupPlayer(TournamentPlayer tournamentPlayer, Location location) {
        Player player = tournamentPlayer.getPlayer();
        player.closeInventory();
        player.teleport(location, PlayerTeleportEvent.TeleportCause.UNKNOWN);
        player.setHealth(20);
        player.setFlying(false);
        player.setAllowFlight(false);
        Bukkit.getScheduler().runTaskLater(astroTournamentsPlugin, () -> {
            tournamentPlayer.getPlayer().sendMessage(CC.translate("&a&l(!)&a You have been given your tournament set! Good Luck!"));
            giveItems(tournamentPlayer);
        }, 20L);
        Bukkit.getScheduler().runTaskLater(astroTournamentsPlugin, () -> {
            Blessed.blessPlayer(tournamentPlayer.getPlayer());
        }, 100L);
    }

    private void giveItems(TournamentPlayer tournamentPlayer) {
        Player player = tournamentPlayer.getPlayer();
        Potion splash = new Potion(PotionType.INSTANT_HEAL, 2);
        splash.setSplash(true);

        ItemStack stackedPot = splash.toItemStack(8);

        player.getInventory().setItem(8, tournamentPlayer.getBoots());
        player.getInventory().setItem(7, tournamentPlayer.getChestplate());
        player.getInventory().setItem(6, tournamentPlayer.getLeggings());
        player.getInventory().setItem(5, tournamentPlayer.getHelmet());
        player.getInventory().setItem(0, tournamentPlayer.getSword());
        player.getInventory().setItem(1, new ItemStack(Material.ENDER_PEARL, 16));
        player.getInventory().setItem(2, KillTrackerAPI.createSoulShard(250000));
        player.getInventory().setItem(27, new NBTWrapper(stackedPot).setString("cosmicType", "stackable").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("maxStackSize", 8).setLong("uniquePots", Long.valueOf(System.nanoTime())).get()).build());
        player.getInventory().setItem(28, new NBTWrapper(stackedPot).setString("cosmicType", "stackable").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("maxStackSize", 8).setLong("uniquePots", Long.valueOf(System.nanoTime())).get()).build())
        ;
        player.getInventory().setItem(29, new NBTWrapper(stackedPot).setString("cosmicType", "stackable").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("maxStackSize", 8).setLong("uniquePots", Long.valueOf(System.nanoTime())).get()).build())
        ;
        player.getInventory().setItem(30, new NBTWrapper(stackedPot).setString("cosmicType", "stackable").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("maxStackSize", 8).setLong("uniquePots", Long.valueOf(System.nanoTime())).get()).build())
        ;
        player.getInventory().setItem(31, new NBTWrapper(stackedPot).setString("cosmicType", "stackable").setNBTBase("conaxData", new NBTTagCompoundBuilder().setInt("maxStackSize", 8).setLong("uniquePots", Long.valueOf(System.nanoTime())).get()).build())
        ;


        while (Utils.hasAvaliableSlot(player, 1)) {
            ItemStack splashPot = splash.toItemStack(1);
            player.getInventory().addItem(splashPot);
        }
    }

    public boolean isBannedItemSkin(SkinType skinType) {
        return bannedSkins.contains(skinType);
    }

    public void banItemSkin(SkinType skinType, Player player) {
        if (!bannedSkins.contains(skinType)) {
            player.sendMessage(CC.translate("&c&l(!)&c Banned the " + skinType.getItem().getItemMeta().getDisplayName()));
            bannedSkins.add(skinType);
        } else {
            player.sendMessage(CC.translate("&a&l(!)&a UnBanned the " + skinType.getItem().getItemMeta().getDisplayName()));
            bannedSkins.remove(skinType);
        }
    }

    public List<SkinType> loadBannedSkinsFromConfig() {
        List<SkinType> bannedSkins = new ArrayList<>();
        return bannedSkins;
        //Load the banned skins
    }

    public void saveBannedSkinsToConfig() {
        //Save banned skins
    }

    public List<ArmorSetType> loadCurrentArmorCrystalFromConfig() {
        List<ArmorSetType> currentCrystal = new ArrayList<>();
        return currentCrystal;
        //load the current crystal
    }

    public void saveCurrentArmorCrystalToConfig() {
        //save the current crystal
    }

    public void addArmorCrystalToCurrentCrystal(ArmorSetType armorSetType, Player player) {
        if (currentArmorCrystal.contains(armorSetType)) {
            currentArmorCrystal.remove(armorSetType);
            player.sendMessage(CC.translate("&c&l(!)&c Removed the " + armorSetType.getDisplay() + " &cCrystal from the current tournament crystal"));
        } else {
            currentArmorCrystal.add(armorSetType);
            player.sendMessage(CC.translate("&a&l(!)&a Added the " + armorSetType.getDisplay() + " &aCrystal to the current tournament crystal"));

        }
    }

    public void end() {
        for (TournamentPlayer tournamentPlayer : getTournamentParticipants()) {

            Utils.emptyInventory(tournamentPlayer.getPlayer());
            tournamentPlayer.setInTournament(false);
            tournamentPlayer.getPlayer().teleport(Bukkit.getWorld("world").getSpawnLocation(), PlayerTeleportEvent.TeleportCause.UNKNOWN);
            tournamentPlayer.getPlayer().sendMessage(CC.translate("&c&l(!)&c The tournament has been force ended and you have been teleported to spawn!"));
        }
        astroTournamentsPlugin.getTournamentManager().getTournamentParticipants().clear();
        astroTournamentsPlugin.getTournamentManager().getTournamentMatchList().clear();
        astroTournamentsPlugin.getTournamentManager().getPlayerQueue().clear();


        astroTournamentsPlugin.getTournamentManager().setActive(false);
        astroTournamentsPlugin.getTournamentManager().setJoinable(false);
    }


}
