package me.kingsavaqe.cagepvp.menus;

import me.kingsavaqe.cagepvp.AstroCagePvP;
import me.kingsavaqe.cagepvp.combattag.CombatTagManager;
import me.kingsavaqe.cagepvp.factions.FactionsManager;
import me.kingsavaqe.cagepvp.immune.ImmuneManager;
import me.kingsavaqe.cagepvp.playerdata.PlayerData;
import me.kingsavaqe.cagepvp.utils.*;
import me.kingsavaqe.cagepvp.utils.inventory.Placeholder;
import me.kingsavaqe.cagepvp.utils.inventory.nInventoryUtil;
import me.kingsavaqe.cagepvp.wildpvp.WildPvP;
import me.kingsavaqe.cagepvp.wildpvp.WildPvPManager;
import me.kingsavaqe.cagepvp.worldguard.WorldGuardManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.UUID;

public class Matches {
   private static Matches instance;

   public static Matches getInstance() {
      if (instance == null) {
         instance = new Matches();
      }

      return instance;
   }

   public void open(final Player player) {
      final AstroCagePvP instance = AstroCagePvP.getInstance();
      Bukkit.getServer().getScheduler().runTaskAsynchronously(instance, () -> {
         final WorldGuardManager worldGuardManager = instance.getWorldGuardManager();
         final CombatTagManager combatTagManager = instance.getCombatTagManager();
         final FactionsManager factionsManager = instance.getFactionsManager();
         final WildPvPManager wildPvPManager = instance.getWildPvPManager();
         final ImmuneManager immuneManager = instance.getImmuneManager();
         final FileConfiguration configLoad = instance.getConfig();
         final PlayerData playerData = instance.getPlayerDataManager().getPlayerData(player);
         final nInventoryUtil inv = new nInventoryUtil(player, event -> {
            ItemStack is = event.getItem();
            if (is.getType() == Material.FENCE_GATE) {
               player.playSound(player.getLocation(), Sound.CHEST_CLOSE, 1.0F, 1.0F);
            } else if (is.getType() == Material.ARROW && (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Next.Unavailable.Displayname"))) || is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Previous.Unavailable.Displayname"))))) {
               event.setWillClose(false);
               event.setWillDestroy(false);
               player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
            } else if (is.getType() == Material.ARROW) {
               event.setWillClose(false);
               event.setWillDestroy(false);
               if (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Next.Available.Displayname")))) {
                  playerData.setPage(playerData.getPage() + 1);
               } else if (is.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Previous.Available.Displayname")))) {
                  playerData.setPage(playerData.getPage() - 1);
               }

               Matches.this.open(player);
               player.playSound(player.getLocation(), Sound.ARROW_HIT, 1.0F, 1.0F);
            } else if (is.getType() == Material.BOOK) {
               event.setWillClose(false);
               event.setWillDestroy(false);
               if (wildPvPManager.hasWildPvP(player)) {
                  Bukkit.getServer().dispatchCommand(player, "1v1 leave");
                  Matches.this.open(player);
                  return;
               }

               if (immuneManager.hasImmune(player)) {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Immune.Create.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                  return;
               }

               if (combatTagManager.isTagged(player)) {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.CombatTag.Create.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                  return;
               }

               if (worldGuardManager.hasWorldGuard() && worldGuardManager.isRegionAtLocation(player.getLocation())) {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Region.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                  return;
               }

               if (factionsManager.hasFactions() && !factionsManager.isWilderness(player.getLocation())) {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Wilderness.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                  return;
               }

               if (wildPvPManager.hasWildPvP(player.getLocation().getChunk())) {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Chunk.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                  return;
               }

               if (EntityUtil.getPlayersInChunk(player.getLocation().getChunk()).size() - 1 != 0) {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Players.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                  return;
               }

               WildPvP wildPvPx = wildPvPManager.createWildPvP(player, true);
               wildPvPx.showBorder(player);
               player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Queued.Player.Message")));
               player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1.0F, 1.0F);
               if (configLoad.getBoolean("WildPvP.Match.Title")) {
                  TitleManager.sendTitle(player, ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Queued.Player.Title.Title")), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Queued.Player.Title.SubTitle")), 0, 30, 10);
               }

               player.closeInventory();
               if (configLoad.getBoolean("WildPvP.Match.Broadcast")) {

                  for (Player all : Bukkit.getOnlinePlayers()) {
                     if (!all.getUniqueId().equals(player.getUniqueId())) {
                        all.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Queued.Broadcast.Message").replace("%player", player.getName())));
                     }
                  }
               }
            } else if (is.getType() == Material.STAINED_GLASS_PANE) {
               event.setWillClose(false);
               event.setWillDestroy(false);
               player.playSound(player.getLocation(), Sound.GLASS, 1.0F, 1.0F);
            } else if (is.getType() == Material.SKULL_ITEM) {
               event.setWillClose(false);
               event.setWillDestroy(false);
               UUID uuid = UUID.fromString(Objects.requireNonNull(NBTUtil.getTag(is, "Match")));
               Player creator = Bukkit.getServer().getPlayer(uuid);
               if (wildPvPManager.hasWildPvP(creator)) {
                  final WildPvP wildPvP = wildPvPManager.getWildPvP(creator);
                  if (wildPvP.isParticipant(player)) {
                     Bukkit.getServer().dispatchCommand(player, "1v1 leave");
                     Matches.this.open(player);
                     return;
                  }

                  if (event.getClick() == ClickType.LEFT) {
                     if (wildPvPManager.hasWildPvP(player)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Already.Message")));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                        return;
                     }

                     if (wildPvP.hasJoined()) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Started.Message")));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                        return;
                     }

                     if (immuneManager.hasImmune(player)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Immune.Join.Message")));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                        return;
                     }

                     if (combatTagManager.isTagged(player)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.CombatTag.Join.Message")));
                        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
                        return;
                     }

                     wildPvP.setJoinLocation(player.getLocation().clone());
                     wildPvP.teleport(player);
                     wildPvP.setJoined(player);
                     wildPvP.startMatch();
                     Bukkit.getServer().getScheduler().runTaskLater(instance, new Runnable() {
                        public void run() {
                           wildPvP.showBorder(player);
                        }
                     }, 30L);
                  } else if (event.getClick() == ClickType.RIGHT) {
                     Invsee.getInstance().open(player, creator);
                     player.playSound(player.getLocation(), Sound.WOOD_CLICK, 1.0F, 1.0F);
                  }
               } else {
                  player.sendMessage(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Listeners.Inventory.Matches.Available.Message")));
                  player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0F, 1.0F);
               }
            }

         });
         inv.setTitle(ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Title")));
         inv.setRows(3);
         if (wildPvPManager.hasWildPvP(player)) {
            inv.addItem(inv.createItem(new ItemStack(Material.BOOK), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Information.Displayname")), configLoad.getStringList("Menus.Matches.Items.Information.Leave.Lore"), new Placeholder[]{new Placeholder("%active", NumberUtil.formatNumberByCommas(wildPvPManager.getActiveWildPvPs().size())), new Placeholder("%queued", NumberUtil.formatNumberByCommas(wildPvPManager.getPendingWildPvPs().size()))}, null, null), 22);
         } else {
            inv.addItem(inv.createItem(new ItemStack(Material.BOOK), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Information.Displayname")), configLoad.getStringList("Menus.Matches.Items.Information.Create.Lore"), new Placeholder[]{new Placeholder("%active", NumberUtil.formatNumberByCommas(wildPvPManager.getActiveWildPvPs().size())), new Placeholder("%queued", NumberUtil.formatNumberByCommas(wildPvPManager.getPendingWildPvPs().size()))}, null, null), 22);
         }

         int index = playerData.getPage() * 36 - 36;
         int endIndex = index >= wildPvPManager.getPendingWildPvPs().size() ? wildPvPManager.getPendingWildPvPs().size() - 1 : index + 36;
         int invSlot = -1;
         int nextEndIndex = 0;
            for(; index < endIndex; ++index) {
               if (wildPvPManager.getPendingWildPvPs().size() > index) {
                  ++invSlot;
                  WildPvP wildPvP = wildPvPManager.getPendingWildPvPs().get(index);
                  Placeholder[] placeholder;
                  if (factionsManager.hasFactions()) {
                     placeholder = new Placeholder[]{new Placeholder("%playersnearby", String.valueOf(EntityUtil.getPlayersNearby(wildPvP.getLocation(), configLoad.getInt("WildPvP.Queued.PlayersNearby.Radius")).size() - 1)), new Placeholder("%player", wildPvP.getCreator().getName()), new Placeholder("%health", String.valueOf((int)wildPvP.getCreator().getHealth())), new Placeholder("%faction", factionsManager.getFactionName(wildPvP.getCreator()))};
                  } else {
                     placeholder = new Placeholder[]{new Placeholder("%playersnearby", String.valueOf(EntityUtil.getPlayersNearby(wildPvP.getLocation(), configLoad.getInt("WildPvP.Queued.PlayersNearby.Radius")).size() - 1)), new Placeholder("%player", wildPvP.getCreator().getName()), new Placeholder("%health", String.valueOf((int)wildPvP.getCreator().getHealth())), new Placeholder("%faction", "")};
                  }

                  if (wildPvP.isCreator(player)) {
                     inv.addItem(inv.createItem(SkullUtil.getPlayerSkull(wildPvP.getCreator()), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Match.Displayname").replace("%player", wildPvP.getCreator().getName())), configLoad.getStringList("Menus.Matches.Items.Match.Leave.Lore"), placeholder, null, null), invSlot);
                  } else {
                     inv.addItem(inv.createItem(SkullUtil.getPlayerSkull(wildPvP.getCreator()), ChatColor.translateAlternateColorCodes('&', configLoad.getString("Menus.Matches.Items.Match.Displayname").replace("%player", wildPvP.getCreator().getName())), configLoad.getStringList("Menus.Matches.Items.Match.Enter.Lore"), placeholder, null, null), invSlot);
                  }

                  inv.createInventory();
                  inv.addItemStack(NBTUtil.addTag(inv.getInventory().getItem(invSlot), "Match", wildPvP.getCreator().getUniqueId().toString()), invSlot);
               }


            nextEndIndex = wildPvPManager.getPendingWildPvPs().size() - playerData.getPage() * 36;
         }

         if (playerData.getPage() == 1) {
            inv.addItem(inv.createItem(new ItemStack(Material.ARROW), configLoad.getString("Menus.Matches.Items.Previous.Unavailable.Displayname"), null, null, null, null), 18);
         } else {
            inv.addItem(inv.createItem(new ItemStack(Material.ARROW), configLoad.getString("Menus.Matches.Items.Previous.Available.Displayname"), null, null, null, null), 18);
         }

         if (nextEndIndex != 0 && nextEndIndex >= 0) {
            inv.addItem(inv.createItem(new ItemStack(Material.ARROW), configLoad.getString("Menus.Matches.Items.Next.Available.Displayname"), null, null, null, null), 26);
         } else {
            inv.addItem(inv.createItem(new ItemStack(Material.ARROW), configLoad.getString("Menus.Matches.Items.Next.Unavailable.Displayname"), null, null, null, null), 26);
         }

         Bukkit.getServer().getScheduler().runTask(instance, inv::open);
      });
   }
}
