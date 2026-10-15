package net.astropvp.adminitems.items;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.cosmicpvp.cosmicutils.utils.PVPUtils;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import net.astropvp.adminitems.AdminItemsPlugin;
import net.astropvp.adminitems.AstroAdminItemsAPI;
import net.astropvp.adminitems.cooldowns.AbilityCooldown;
import net.astropvp.adminitems.language.Language;
import net.astropvp.adminitems.managers.AdminItemAbilityManager;
import net.astropvp.adminitems.struct.AdminItem;
import net.astropvp.adminitems.struct.Interactable;
import net.astropvp.adminitems.struct.annotations.ConfigValue;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.*;

public class TrapBlocker extends AdminItem implements Interactable {

    private final AdminItemAbilityManager abilityManager = AdminItemsPlugin.getInstance().getAbilityManager();


    @ConfigValue()
    private int radius = 16;

    @ConfigValue()
    private int time = 10;

    @ConfigValue()
    private int cooldown = 60;


    @Override
    public String getName() {
        return "TrapBlocker";
    }

    @Override
    public String getDisplayName() {
        return "&c&lTrap Blocker";
    }

    @Override
    public List<String> getPerks() {
        return Arrays.asList(
                "&7Upon use, people in a &f" + radius + "x" + radius + "&7 radius",
                "&7will no longer be able to:",
                "&7open doors, place or break blocks",
                "&7for a duration of &f" + time + "&7 seconds."
        );
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&7&oThose who have been trapped",
                "&7&oin life know how it is to escape",
                "&7&ofrom it's darkest dreams."
        );
    }


    @Override
    public ChatColor getColor() {
        return ChatColor.RED;
    }

    @Override
    public Material getMaterial() {
        return Material.STICK;
    }

    @Override
    public String getCooldownID() {
        return "trapBlocker";
    }

    public void procOnClickAbility(Player player) {
        List<Player> nearbyPlayers = PVPUtils.getNearbyEnemyPlayersExceptPlayer(player, radius);
        List<UUID> trapBlockedPossibleList = new ArrayList<>();
        for (Player enemy : nearbyPlayers) {
            final UUID uuid = enemy.getUniqueId();
            if (CooldownUtils.isCooldownActive(enemy, getCooldownID())) {
                String cd = CooldownUtils.getFormattedCooldown(enemy, getCooldownID());
                String msg = Language.TRAP_BLOCKER_ENEMY_ON_COOLDOWN.getMessage().replace("%time%", cd);
                msg = msg.replace("%enemy%", enemy.getName());
                player.sendMessage(CC.translate(msg));
                continue;
            }
            if (AstroItemSkinsAPI.hasSkinEquipped(enemy, SkinType.EASTER_2022_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(enemy, SkinType.EASTER_2022_MUTATED_BACKPACK)) {
                player.sendMessage("&c&l(!)&c " + enemy.getName() + " has bypassed your trapblocker due to his Door To Wonderland ItemSkin!");
                continue;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(enemy, SkinType.SPRING_BREAK_2022_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(enemy, SkinType.SPRING_BREAK_2022_MUTATED_BACKPACK)) {
                player.sendMessage("&c&l(!)&c " + enemy.getName() + " has bypassed your trapblocker via their Spray and Pray Backpack");
                continue;
            }
            trapBlockedPossibleList.add(uuid);

        }
        if (trapBlockedPossibleList.isEmpty()) {
            int uses = AstroAdminItemsAPI.getUsesFromFromItem(player.getItemInHand());
            player.setItemInHand(super.getItem(uses));
            Language.TRAP_BLOCKER_NO_NEARBY.send(player);
            return;
        }

        StringBuilder listFormatted = new StringBuilder(new StringBuilder());
        for (UUID uuid : trapBlockedPossibleList) {
            Player enemy = Bukkit.getPlayer(uuid);
            if (enemy == null) continue;
            abilityManager.trapBlock(uuid);
            Bukkit.getScheduler().runTaskLaterAsynchronously(AdminItemsPlugin.getInstance(), ()-> {
                Language.TRAP_BLOCKER_ENDED.send(Bukkit.getPlayer(uuid));
                abilityManager.getTrapBlockered().remove(uuid);
            }, time* 20L);
            CooldownUtils.registerCooldown(enemy, new AbilityCooldown(getCooldownID(), cooldown));
            String msg = Language.TRAP_BLOCKED_BY.getMessage().replace("%time%", Integer.toString(this.time));
            msg = msg.replace("%enemy%", player.getName());
            enemy.sendMessage(CC.translate(msg));
            listFormatted.append(enemy.getName()).append(", ");
            listFormatted = new StringBuilder(listFormatted.substring(0, listFormatted.length() - 2));

        }

        String msgPlayer = Language.TRAP_BLOCKED.getMessage().replace("%blockedList%", listFormatted.toString());
        msgPlayer = msgPlayer.replace("%amount%", Integer.toString(trapBlockedPossibleList.size()));
        player.sendMessage(CC.translate(msgPlayer));
        AstroAdminItemsAPI.decreaseUses(player, this);

    }

    @EventHandler
    public void onPlayerInteractWithAdminItem(PlayerInteractEvent event) {
        if (!AstroAdminItemsAPI.isAdminItem(event.getItem())) return;
        if (!AstroAdminItemsAPI.canUseAdminItem(event.getPlayer(), event.getItem(), getCooldownID(), this)) {
            return;
        }
        procOnClickAbility(event.getPlayer());
    }

    @EventHandler
    public void onBuild(BlockPlaceEvent event) {
        if (abilityManager.isTrapBlockered(event.getPlayer().getUniqueId())) {
            Language.TRAP_BLOCKER_CANNOT_INTERACT.send(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onLogout(PlayerQuitEvent event) {
        if (abilityManager.isTrapBlockered(event.getPlayer().getUniqueId())) {
            abilityManager.getTrapBlockered().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if (abilityManager.isTrapBlockered(event.getPlayer().getUniqueId())) {
            abilityManager.getTrapBlockered().remove(event.getPlayer().getUniqueId());
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (abilityManager.isTrapBlockered(event.getEntity().getUniqueId())) {
            abilityManager.getTrapBlockered().remove(event.getEntity().getUniqueId());
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (abilityManager.isTrapBlockered(event.getPlayer().getUniqueId())) {
            Language.TRAP_BLOCKER_CANNOT_INTERACT.send(event.getPlayer());

            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;
        if (!event.getClickedBlock().getType().name().endsWith("_DOOR")) return;
        if (abilityManager.isTrapBlockered(event.getPlayer().getUniqueId())) {
            Language.TRAP_BLOCKER_CANNOT_INTERACT.send(event.getPlayer());
            event.setCancelled(true);
        }
    }
}
