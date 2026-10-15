package net.astropvp.core.listeners;

import com.astropvp.cc.player.CosmicPlayers;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.StringUtils;
import com.golfing8.kore.FactionsKore;
import com.golfing8.kore.feature.GracePeriodFeature;
import net.astropvp.core.AstroCore;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.github.paperspigot.Title;

public class MOTD implements Listener {
    private GracePeriodFeature gracePeriodFeature;

    public MOTD() {
        this.gracePeriodFeature = (GracePeriodFeature) FactionsKore.get().getFeature("grace");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {

        Bukkit.getScheduler().runTaskLaterAsynchronously(AstroCore.getInstance(), () -> {
            Player player = e.getPlayer();
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&c "));
            player.sendMessage(CC.translate("&f"));
            player.sendMessage(CC.translate(StringUtils.getCenteredMessage("&fWelcome, &6" + player.getName() + "&f to &6&lAstro&f&lPvP&f!")));
            player.sendMessage(CC.translate(""));
            player.sendMessage(CC.translate(StringUtils.getCenteredMessage("&a&lSHOP &fbuy.astropvp.net")));
            player.sendMessage(CC.translate(StringUtils.getCenteredMessage("&6&lDISCORD &fdiscord.astropvp.net")));
            player.sendMessage(CC.translate(StringUtils.getCenteredMessage("&c&lRULES &frules.astropvp.net")));
            //player.sendMessage(CC.translate(StringUtils.getCenteredMessage("&e&lCRYPTO &fcrypto.astropvp.net")));

            player.sendMessage("");
            player.sendMessage(CC.translate(StringUtils.getCenteredMessage("&f&lWE RECOMMEND YOU TO USE &6CosmicClient &f&lOR &61.8.9")));
            player.sendMessage("");


            if (gracePeriodFeature.isGracePeriod()) {
                player.sendMessage(CC.translate("&c&l[!]&c Grace Period is enabled &7(" + gracePeriodFeature.getGraceString() + "&7)&c. All Explosives are Disabled."));
                player.sendMessage("");
            }



            player.playSound(player.getLocation(), Sound.GHAST_MOAN, 5, 5);
        }, 15L);


        Bukkit.getScheduler().runTaskLater(AstroCore.getInstance(), () -> {
            Player player = e.getPlayer();
            if (CosmicPlayers.get().getCosmicPlayer(player) != null) {
                if (CosmicPlayers.get().getCosmicPlayer(player).isOnCosmicClient()) {
                    player.sendMessage("");
                    player.sendMessage(CC.translate("&e&l(!)&e Thank you for using the Cosmic Client, " + player.getName() + "!"));
                    player.sendMessage(CC.translate("&e&l* &7Passive 1.5x XP/Key Chance multiplier activated!"));
                    player.sendMessage("");
                }
            } else {
                player.sendMessage("");
                player.sendMessage(CC.translate("&a&l[!]&a Hello &f" + player.getName() + "&a, you are not on the supported version of &f&nCosmicClient&a."));
                player.sendMessage(CC.translate("&7Please join our discord at &f&ndiscord.astropvp.net&7 and follow the turtorial in &f#cc-features&7."));
                player.sendMessage(CC.translate("&7You must be on a supported version of &fCosmicClient&7 to see skins, ping, roam etc..."));
                player.sendMessage(CC.translate("&eYou will also get 1.5x EXP and 1.5x Grinding Lootboxes chance."));
                player.sendTitle(new Title(CC.translate("&aPlease download Official CosmicClient!"), CC.translate("&7You can find it in &f#cc-features&7 in &f/discord&7."), 20, 120, 20));
                player.sendMessage("");
            }
        }, 80L);


    }




}
