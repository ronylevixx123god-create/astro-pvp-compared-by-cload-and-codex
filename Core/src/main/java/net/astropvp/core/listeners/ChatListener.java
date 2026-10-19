package net.astropvp.core.listeners;

import com.astropvp.cc.player.CosmicPlayers;
import com.compilex.CosmicKOTH.CosmicKOTHAPI;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.earth2me.essentials.utils.DateUtil;
import com.golfing8.kore.FactionsKore;
import com.golfing8.kore.feature.ShieldFeature;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.struct.ChatMode;
import me.astropvp.astrospawners.ftop.FactionsTopAPI;
import net.astropvp.core.AstroCore;
import net.astropvp.core.conaxgames.staff.StaffChat;
import net.astropvp.core.titles.PlayerTitleAPI;
import net.astropvp.core.titles.utils.Clickable;
import net.crankedpvp.staff.managers.StaffChatManager;
import net.luckperms.api.LuckPermsProvider;
import org.arkhamnetwork.Arkkit.patches.chat_filter.ChatFilter;
import org.arkhamnetwork.Arkkit.utils.EssentialsUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatListener implements Listener {

    private final Pattern ipPattern = Pattern.compile("((?<![0-9])(?:(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[.,-:; ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2})[ ]?[., ][ ]?(?:25[0-5]|2[0-4][0-9]|[0-1]?[0-9]{1,2}))(?![0-9]))", Pattern.CASE_INSENSITIVE);
    private final Pattern webpattern = Pattern.compile("[-a-zA-Z0-9@:%_\\+.~#?&//=]{2,256}\\.[a-z]{2,4}\\b(\\/[-a-zA-Z0-9@:%_\\+.~#?&//=]*)?");
    private final List<String> badWords = Arrays.asList("nigger", "jew", "kys", "demonicpvp", "jurassicpvp", "cursedpvp", "exoduspvp", "faggot", "ddos", "doss", "niggar", "hitler", "frans", "jozef", "gronckel", "straat", "lennik", "bryan", "jeetun", "savina", "dox", "doxed", "swat", "swatted", "kys", "gourley", "3dsam", "gmail", "hotmail", "rewind");
    private final ShieldFeature shieldFeature;
    private double chatCooldown;

    public ChatListener(AstroCore plugin) {
        this.shieldFeature = FactionsKore.get().getFeature(ShieldFeature.class);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getInventory().getName().endsWith("'s Inventory")) {
            event.setCancelled(true);
        }
    }


    // Chat Filter
    @EventHandler(priority = EventPriority.HIGH)
    public void playerChat(AsyncPlayerChatEvent event) throws IOException {

        if (event.isCancelled()) return;

        event.setCancelled(true);

        Player player = event.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);


        if (fPlayer.getChatMode() != ChatMode.PUBLIC) return;

        //if (StaffUtil.inStaffChat.contains(event.getPlayer())) return;

        StaffChatManager staffChatManager = AstroCore.getInstance().getStaffChatManager();
        if (staffChatManager.getInStaffChat().contains(event.getPlayer())) {
            staffChatManager.sendStaffMessage(event.getPlayer(), event.getMessage());
            return;
        }

        if (player.hasPermission("astrocore.nebula")) {
            chatCooldown = 0;
        } else if (player.hasPermission("astrocore.apollo")) {
            chatCooldown = 2;
        } else {
            chatCooldown = 4;
        }

        if (AstroCore.getInstance().getCooldownUtils().isOnCooldown(player, "chatCooldown")) {
            long msLeft = AstroCore.getInstance().getCooldownUtils().getRemainingMillis(player, "chatCooldown");
            player.sendMessage(CC.translate("&c&l(!)&c You cannot send messages for another " + msLeft + "ms."));
            player.sendMessage(CC.translate("&cTo reduce this cooldown, purchase a higher rank at &7buy.astropvp.net&c!"));
            return;
        }


        Faction faction = fPlayer.getFaction();

        //Check if chat is disable
        if (ChatFilter.isChatHalted() && !player.isOp()) {
            event.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot use chat while chat is halted."));
            return;
        }

        if (event.getMessage().contains("[item]") && event.getMessage().contains("[brag]")) return;

        Matcher ipMatcher = ipPattern.matcher(event.getMessage());

        boolean ghostMessage = false;


        if (ipMatcher.find() && !player.isOp()) {
            //player.sendMessage(CC.translate("&c&l(!)&c You are not allowed to send ip's in chat!"));
            ghostMessage = true;
            StaffChat.sendGlobal(CC.translate("&c" + player.getName() + " &ctried sending an ip in chat &7(" + event.getMessage() + "&7) &f- &bGHOSTMESSAGE"));
            //return;
        }

        Matcher webMatcher = webpattern.matcher(event.getMessage());

        if (webMatcher.find() && !player.isOp()) {
            //player.sendMessage(CC.translate("&c&l(!)&c You are not allowed to send links in chat!"));
            ghostMessage = true;
            StaffChat.sendGlobal(CC.translate("&c" + player.getName() + " &ctried sending a link in chat &7(" + event.getMessage() + "&7) &f- &bGHOSTMESSAGE"));
            //return;
        }

        for (String badword : badWords) {
            if (event.getMessage().toLowerCase().contains(badword)) {
                //player.sendMessage(CC.translate("&c&l(!)&c Please refrain from using bad words! Your message has been reported to the staff."));
                ghostMessage = true;
                StaffChat.sendGlobal(CC.translate("&c" + player.getName() + " &ctried sending a bad word in chat &7(" + event.getMessage() + "&7) &f- &bGHOSTMESSAGE"));
                //return;
            }
        }

        Set<Player> recipients;
        if (ghostMessage) {
            recipients = new HashSet<>();
            recipients.add(player);
        } else {
            recipients = event.getRecipients();
        }


        for (Player recipient : recipients) {
            String message = event.getMessage();


            FPlayer fRecipient = FPlayers.getInstance().getByPlayer(recipient);
            Faction recipientFaction = fRecipient.getFaction();

            Clickable clickable = new Clickable("");
            String nitroLeader = getNitroLeader(player);

            clickable.add(nitroLeader);

            if (!faction.isWilderness()) {
                String shieldStatus;
                if (shieldFeature.isShieldActive(faction.getId()) && shieldFeature.isSetup(faction.getId()) && shieldFeature.isTimeConfigured(faction.getId())) {
                    shieldStatus = "&aON &7(" + ChatColor.stripColor(shieldFeature.getShieldData(faction.getId(), true)) + "&7)";
                } else {
                    shieldStatus = "&cOFF";
                }

                clickable.add( "&8[" + faction.getRelationTo(recipientFaction).getColor() + fPlayer.getChatTag() + "&8]&r ",
                        "&6&lFaction Info: &f" + faction.getTag() + " \n" +
                                "&6* &eStrikes: &f" + faction.getStrikes().size() + " \n" +
                                "&6* &ePower: &a" + (int) faction.getPower() + "&8/&a" + (int) faction.getPowerMax() + " \n" +
                                "&6* &eShield: &f" + shieldStatus + "\n" +
                                "&6* &eOnline: &f" + faction.getOnlinePlayers().size() + " \n" +
                                "&6* &eFtop: &f" + FactionsTopAPI.getFactionTop(faction) + " \n" +
                                "&6* &eFaction Koth Wins: &f" + CosmicKOTHAPI.getFactionKothWins(FactionUtils.getFaction(event.getPlayer())) + " \n\n" +
                                "&7&oClick to view this faction",
                        "/f who " + player.getName()
                );
            }


            clickable.add(Objects.requireNonNull(LuckPermsProvider.get().getUserManager().getUser(player.getUniqueId())).getCachedData().getMetaData().getPrefix() + " " +  player.getName() + getTag(player),
                    "&6&lUser: &f" + player.getName() + " \n" +
                            "&6* &eNick: &f" + player.getDisplayName() + " \n" +
                            "&6* &eFaction: &f" + FactionUtils.getFaction(player).getTag() + " \n" +
                            "&6* &eBalance: &a$" + NumberUtils.formatMoney(EconomyUtils.getBalance(player)) + " \n" +
                            "&6* &eLogged in: &f" + DateUtil.formatDateDiff(EssentialsUtils.getEssentials().getUser(player).getLastLogin()) + " ago" + " \n" +
                            "&6* &eCosmic Client: &f" + ((CosmicPlayers.get().getCosmicPlayer(event.getPlayer()).isOnCosmicClient()) ? ChatColor.GREEN.toString() + ChatColor.BOLD + "✓" : ChatColor.RED.toString() + ChatColor.BOLD + "✗") + " \n\n" +
                            "&7&oClick to see when the player logged online",
                    "/stats " + player.getName()
            );



            clickable.add("&8 » ");


            if (message.contains("[item]") || message.contains("[ITEM]")) {
                ItemStack itemStack = event.getPlayer().getItemInHand();
                if (itemStack == null || !itemStack.hasItemMeta() || itemStack.getType() == Material.AIR) {
                    player.sendMessage(CC.translate("&c&l(!)&c Please hold an item when using [item]"));
                    return;
                } else {
                    String name = itemStack.getItemMeta().getDisplayName();
                    int amount = itemStack.getAmount();
                    clickable.addItem(CC.translate("&f» " + name + " &7(" + amount + "x) &f« "), itemStack);
                }
                message = message.replace("[item]", "").replace("[ITEM]", "");
            }


            if (message.contains("[brag]") || message.contains("[BRAG]")) {
                String playerName = event.getPlayer().getName();
                clickable.add(String.format("&d&o%s's Inventory", playerName), String.format("&7View %s's Inventory", playerName), "/invsee " + playerName);
                message = message.replace("[brag]", "").replace("[BRAG]", "");
            }

            ChatColor chatcolor = AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(player.getUniqueId()).getChatColor();
            if (player.isOp()) {
                chatcolor = ChatColor.DARK_RED;
            }
            String[] messageSplit = message.split(" ");
            StringBuilder newMessage = new StringBuilder();
            for (String s : messageSplit) {
                newMessage.append(chatcolor).append(s).append(" ");
            }
            clickable.add(chatcolor + newMessage.toString());
            clickable.sendToPlayer(recipient);
            if (!player.isOp()) {
                AstroCore.getInstance().getCooldownUtils().add(player, "chatCooldown", (long) (chatCooldown * 1000));
            }
        }

    }

    public String getTag(Player player) {
        String title = "";
        if (PlayerTitleAPI.getEquippedTitle(player.getUniqueId()) != null)
            if (PlayerTitleAPI.getEquippedTitle(player.getUniqueId()).equals("none")) {
                title = "";
            } else {
                title = " &8[&a" + PlayerTitleAPI.getEquippedTitle(player.getUniqueId()) + "&8]";
            }

        return title;
    }

    public String getNitroLeader(Player player) {
        return DiscordSyncBadges.getNitroLeader(player);
    }
}
