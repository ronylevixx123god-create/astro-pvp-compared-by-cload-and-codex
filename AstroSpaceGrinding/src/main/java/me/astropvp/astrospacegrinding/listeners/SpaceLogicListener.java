package me.astropvp.astrospacegrinding.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingAPI;
import me.astropvp.astrospacegrinding.data.ForceFieldData;
import me.astropvp.astrospacegrinding.data.SpacePlayerData;
import net.astropvp.core.utils.OutpostUtil;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.listeners.PlayerPotionEffectListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.potion.PotionEffect;

import java.util.Arrays;
import java.util.List;


public class SpaceLogicListener implements Listener {


    private static List<String> allowedCommands = Lists.newArrayList();

    static {
        allowedCommands.addAll(Arrays.asList("msg", "message", "staffmode", "staff", "freeze", "lms", "ft", "filter", "r", "bless", "blessme", "response", "space", "bless", "heal", "cf", "coinflip", "jackpot",
                "feed", "f", "fix", "filter edit", "filter toggle", "ft", "bless", "r", "reply", "spawn", "warp", "pots", "filters", "outpost", "pin", "sc", "staffchat", "mute", "ban", "tempban", "pin"));
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onWorldLeave(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (event.getTo() == null) {
            return;
        }
        if (!AstroSpaceGrindingAPI.isSpaceWorld(event.getFrom().getWorld())) {
            if(AstroSpaceGrindingAPI.isSpaceWorld(event.getTo().getWorld())) {
            //if (event.getTo().getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName())) {
                for (PotionEffect pe : player.getActivePotionEffects()) {
                    player.removePotionEffect(pe.getType());
                }
                Bukkit.getScheduler().runTaskLater(Enchantments.getInstance(), () -> {
                    if (!player.getPlayer().isDead() && player.getPlayer().isValid()) {
                        PlayerPotionEffectListener.rescanPlayerArmor(player);
                    }
                }, 5L);
                new SpacePlayerData(event.getPlayer());
            }
            return;
        }

        if (!event.getFrom().getWorld().getName().equals(event.getTo().getWorld().getName())) {
            if (CombatLog.inCombat(player)) {
                return;
            }
            player.sendMessage(CC.RedB + "(!) " + CC.Red + "As a result of leaving the Space Lands, "
                    + "you have lost your /space stats and your forcefield if you had one!");
            player.playSound(player.getLocation(), Sound.AMBIENCE_THUNDER, 0.75F, 2F);
            ForceFieldData.remove(player);
            SpacePlayerData.remove(player);

        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        ForceFieldData.remove(e.getPlayer());
        SpacePlayerData.remove(e.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerKickEvent  e) {
        ForceFieldData.remove(e.getPlayer());
        SpacePlayerData.remove(e.getPlayer());
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        SpacePlayerData.remove(e.getEntity());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        SpacePlayerData.findPlayer(e.getPlayer());
    }


    @EventHandler(priority = EventPriority.MONITOR)
    public void enchantProc(CustomEnchantProcOnEntityEvent e) {
        if(!AstroSpaceGrindingAPI.isSpaceWorld(e.getPlayer().getWorld())) {
        //if (!e.getPlayer().getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName())) {
            return;
        }
        if (e.getEnchant().getTier() > 5) e.setCancelled(true);
    }


    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onRightClickEgg(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return;
        }
        if (event.getClickedBlock().getType() == Material.DRAGON_EGG) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onForcefieldDamage(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();
            if(!AstroSpaceGrindingAPI.isSpaceWorld(damager.getWorld()) || !AstroSpaceGrindingAPI.isSpaceWorld(damaged.getWorld())) {
            //if (!damager.getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName()) || !damaged.getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName())) {
                return;
            }

            SpacePlayerData dataDamager = SpacePlayerData.findPlayer(damager);
            SpacePlayerData dataDamaged = SpacePlayerData.findPlayer(damaged);

            if (ForceFieldData.findPlayer(dataDamager.getPlayer()).isActive()) {
                if (WorldGuardUtils.getRegionsIn(damager.getLocation()).contains("space_cap_pvp")) return;
                e.setCancelled(true);
                damager.sendMessage(CC.translate("&b&l(!)&b You cannot hit " + damaged.getName() + " because you are still on &f&lFORCEFIELD &bprotection."));
                damager.playSound(damager.getLocation(), Sound.VILLAGER_NO, 5, 5);
            }

            if (ForceFieldData.findPlayer(dataDamaged.getPlayer()).isActive()) {
                if (WorldGuardUtils.getRegionsIn(damaged.getLocation()).contains("space_cap_pvp")) return;
                e.setCancelled(true);
                damager.sendMessage(CC.translate("&b&l(!)&b You cannot hit " + damaged.getName() + " because he is still on &f&lFORCEFIELD &bprotection."));
                damager.playSound(damager.getLocation(), Sound.VILLAGER_NO, 5, 5);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onOutpostHit(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();
            if(!AstroSpaceGrindingAPI.isSpaceWorld(damager.getWorld()) || !AstroSpaceGrindingAPI.isSpaceWorld(damaged.getWorld())) {
            //if (!damager.getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName()) || !damaged.getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName())) {
                return;
            }

            if (OutpostUtil.hasMarsOutpost(damager)) {
                e.setDamage(e.getDamage() * 1.25);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onFlight(PlayerToggleFlightEvent event) {
        Player player = event.getPlayer();
        if (player.isOp()) {
            return;
        }
        if(!AstroSpaceGrindingAPI.isSpaceWorld(player.getWorld())) {
        //if (!player.getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName())) {
            return;
        }
        if (event.isFlying()) {
            player.setAllowFlight(false);
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        if(!AstroSpaceGrindingAPI.isSpaceWorld(player.getWorld())) {
        //if (!player.getWorld().getName().equals(AstroSpaceGrindingAPI.getWorldName())) {
            return;
        }

        if (player.isOp()) {
            return;
        }

        if (player.hasPermission("astrocore.staffmode")) {
            return;
        }
        if (allowedCommands.contains(event.getMessage().split(" ")[0].replace("/", "").toLowerCase())) {
            return;
        }
        player.sendMessage(CC.translate(  "&c&l(!) &c" + event.getMessage() + " is denied in this world!"));
        event.setMessage("/null");
        event.setCancelled(true);
    }


    @EventHandler
    public void PlayerSpawnMob(PlayerInteractEvent e) {
        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getMaterial() == Material.MONSTER_EGG) {
            if(AstroSpaceGrindingAPI.isSpaceWorld(e.getPlayer().getWorld())) {
                e.setCancelled(true);
                e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You cannot use spawn eggs in the space."));
            }
        }
    }


}
