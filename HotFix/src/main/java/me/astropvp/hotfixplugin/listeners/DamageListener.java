package me.astropvp.hotfixplugin.listeners;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import com.massivecraft.factions.*;
import me.astropvp.hotfixplugin.HotFixPlugin;
import me.astropvp.hotfixplugin.utils.DeathUtils;
import org.apache.commons.lang.StringUtils;
import org.arkhamnetwork.Arkkit.libs.JSONMessage;
import org.arkhamnetwork.Arkkit.patches.CombatLog;
import org.arkhamnetwork.Arkkit.patches.ItemLinking;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DamageListener implements Listener {


    private List<UUID> killCooldown;

    public DamageListener() {
        this.killCooldown = new ArrayList<>();
    }

    
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChatCombat(AsyncPlayerChatEvent event) {
        if ((event.getMessage().equalsIgnoreCase("/eendersee") || event.getMessage().equalsIgnoreCase("/spawn") || event.getMessage().equalsIgnoreCase("/warp spawn")) && CombatLog.inCombat(event.getPlayer())) {
            event.getPlayer().sendMessage(CC.Red + "Disabled in combat");
            event.setMessage("/null");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void echestOpenCombat(InventoryOpenEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        if (CombatLog.inCombat((Player) event.getPlayer()) && event.getInventory().getTitle().contains("Ender")) {
            event.setCancelled(true);
            event.getPlayer().closeInventory();
            event.getPlayer().sendMessage(CC.translate("&cDisabled in combat!"));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (event.getEntity().getLocation().getWorld().getName().equalsIgnoreCase("world_arcade")) {
            event.getDrops().removeIf(itemStack -> itemStack.getType() == Material.POTION);
        }
    }

/*    @EventHandler
    public void axe(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (e.getDamager().getLocation().getWorld().getName().equals("lms_world")) return;
            if (e.getDamager().getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

            if (((Player) e.getDamager()).getItemInHand() != null && ((Player) e.getDamager()).getItemInHand().getType() == Material.DIAMOND_AXE) {
                AstroDamageHandler.getInstance().getDamageHandler().reduceDamage(50, e, "axedebuff");
            }
        }
    }*/


    @EventHandler(ignoreCancelled = true, priority = EventPriority.LOWEST)
    public void onIllegalSandStacking(EntityChangeBlockEvent event) {
        if (event.getTo() != Material.SAND) return;
        Faction faction = Board.getInstance().getFactionAt(new FLocation(event.getBlock()));
        if (faction.isWarZone() || faction.isSafeZone()) event.setCancelled(true);
    }

    /*@EventHandler(priority = EventPriority.HIGH)
    public void diaPvP(EntityDamageByEntityEvent e) {
        if (e.getDamager().getLocation().getWorld().getName().equals("lms_world")) return;
        if (e.getDamager().getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();
            if (((Player) e.getDamager()).getItemInHand() == null) return;
            if (((Player) e.getDamager()).getItemInHand().getType() == Material.AIR) return;
            if (!((Player) e.getDamager()).getItemInHand().getType().name().endsWith("_SWORD")) return;
            if (!((Player) e.getDamager()).getItemInHand().getType().name().endsWith("_AXE")) return;

            double dmgBuff = 0;

            for (ItemStack armor : damaged.getInventory().getArmorContents()) {
                if (!armor.getType().name().startsWith("LEATHER_")) {
                    dmgBuff += 1500;
                }
            }
            e.setDamage(e.getDamage() * 1 + (dmgBuff * 0.01));

        }
    }*/

    @EventHandler(priority = EventPriority.HIGH)
    public void dmg(EntityDamageByEntityEvent e) {
        //if (e.getDamager().getWorld().getName().equals("space")) {
            //e.setDamage(e.getDamage() * 0.75);
        //}
        //Bukkit.broadcastMessage(CC.Red + String.valueOf(e.getFinalDamage()));
    }


    @EventHandler(priority = EventPriority.HIGH)
    public void plutoPvP(EntityDamageByEntityEvent e) {

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (!e.getDamager().getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();
            if (((Player) e.getDamager()).getItemInHand() == null) return;
            if (((Player) e.getDamager()).getItemInHand().getType() == Material.AIR) return;

            e.setDamage(e.getDamage() * 2);

        }
    }

    @EventHandler(ignoreCancelled = true)
    public void creeperDmg(EntityDamageEvent event) {
        if (event.getEntity() instanceof Creeper) {
            if (event.getCause() == EntityDamageEvent.DamageCause.DROWNING || event.getCause() == EntityDamageEvent.DamageCause.LAVA || event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                event.getEntity().remove();
            }
        }
    }


    private String getFormattedName(ItemStack hand) {
        if (hand == null || hand.getType() == Material.AIR) {
            return "their fists!";
        }
        if (hand.hasItemMeta() && hand.getItemMeta().hasDisplayName()) {
            return hand.getItemMeta().getDisplayName();
        }
        if (hand.getType() == Material.TNT) {
            return "TNT";
        }
        return StringUtils.capitaliseAllWords(hand.getType().name().toLowerCase().replace("_", " "));
    }

    @EventHandler
    public void killMsg(EntityDeathEvent e) throws NoSuchFieldException, IllegalAccessException {
        if (e.getEntity() instanceof Player && e.getEntity().getKiller() != null) {
            Player killer = e.getEntity().getKiller();
            Player died = (Player) e.getEntity();
            final UUID kuuid = killer.getUniqueId();

            if (this.killCooldown.contains(kuuid)) return;
            this.killCooldown.add(kuuid);
            Bukkit.getScheduler().runTaskLaterAsynchronously(HotFixPlugin.getInstance(), () -> {
                this.killCooldown.remove(kuuid);
            }, 5 * 20L);
            JSONMessage message = new JSONMessage("", ChatColor.GRAY);
            FPlayer fkiller = FPlayers.getInstance().getByPlayer(killer);
            FPlayer fdied = FPlayers.getInstance().getByPlayer(died);

            message.addText(CC.translate("&c" + killer.getName() + "&4[" + fkiller.getKills() + "&4]&e killed &c" + died.getName() + "&4[" + fdied.getKills() + "&4]" + " &eusing "));
            if (killer.getItemInHand() == null || killer.getItemInHand().getType() == Material.AIR) {
                message.addText(this.getFormattedName(null));
            } else {
                message.addItem(killer.getItemInHand(), ItemLinking.formatColors(ChatColor.WHITE + this.getFormattedName(killer.getItemInHand())).getText());
            }
            for (Player player : Bukkit.getOnlinePlayers()) {
                message.sendToPlayer(player);

            }
            DeathUtils.sendDeathEffect(died);
        }
    }

    @EventHandler
    public void onShoot(EntityShootBowEvent e) {
        if (e.getEntity().getWorld().getName().equals("space") || e.getEntity().getWorld().getName().equals("mars") || e.getEntity().getLocation().getWorld().getName().equals("world_outpost_pluto") ) {
            e.getEntity().sendMessage(CC.translate("&cBows are disabled here"));
            e.setCancelled(true);
        }
    }
/*
    @EventHandler
    public void onHitP6(EntityDamageByEntityEvent e) {
        if (e.getDamager().getLocation().getWorld().getName().equals("lms_world")) return;
        if (e.getDamager().getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

        if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
            Player phurt = (Player) e.getEntity();
            if (getProtectionReduction(phurt) != 0) {
                double totalReduction = getProtectionReduction(phurt);
                e.setDamage(e.getDamage() * (1 - (totalReduction * 0.01)));

            }


            if (((Player) e.getDamager()).getItemInHand() == null) {
                //e.setDamage(EntityDamageByEntityEvent.DamageModifier.BASE, e.getDamage() * 0.65);
                return;
            }




            if (((Player) e.getDamager()).getItemInHand().getType().name().endsWith("_SWORD") && ((Player) e.getDamager()).getItemInHand().getType() != Material.GOLD_SWORD || ((Player) e.getDamager()).getItemInHand().getType().name().endsWith("_AXE") && ((Player) e.getDamager()).getItemInHand().getType() != Material.GOLD_AXE) {

                e.setDamage(e.getDamage() * 0.5);
            }
        }
    }*/


    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        e.getPlayer().setAllowFlight(true);
    }


    public double getProtectionReduction(Player p) {
        double decrease = 0;
        for (ItemStack armorPiece : p.getInventory().getArmorContents()) {
            if (armorPiece == null) continue;
            if (armorPiece.getType() == Material.AIR) continue;
            if (armorPiece.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
                int protLevel = armorPiece.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL);
                if (protLevel == 6) decrease += 10;
                if (protLevel == 5) decrease += 5;
            }
        }
        return decrease;
    }
/*    @EventHandler(priority = EventPriority.MONITOR)
    public void diaSwordFix(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            if (damager.getItemInHand() != null) {
                NBTWrapper wrapper = new NBTWrapper(damager.getItemInHand());
                NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? wrapper.getCompound("cosmicData") : new NBTTagCompoundBuilder();
                if(cosmicData.hasKey("dmgIncr"))
                    e.setDamage(e.getDamage() + cosmicData.getDouble("dmgIncr"));
            }
        }
    }*/

    @EventHandler(priority = EventPriority.HIGHEST)
    public void antiNearCrash(PlayerCommandPreprocessEvent e) {
        if ((e.getMessage().toLowerCase().startsWith("/near") || e.getMessage().startsWith("/enear") || e.getMessage().toLowerCase().startsWith("/nearby") || e.getMessage().toLowerCase().startsWith("/enearby"))) {
            e.setMessage("/cnear");
        }

    }


   @EventHandler
    public void onDamageBoss(EntityDamageEvent e) {
        if (e.getEntity() != null && (!(e.getEntity() instanceof Player))) {
            String eWorld = e.getEntity().getWorld().getName();
            boolean isDungeonWorld = eWorld.equals("dungeon_yijki_a") || eWorld.equals("dungeon_destroyed_outpost") || eWorld.equals("dungeon_planet_nul");
            if (e.getEntity().hasMetadata("boss") && isDungeonWorld) {
                e.setDamage(e.getDamage() *4);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onWaterPlace(BlockPlaceEvent event) {
        if (event.isCancelled() && event.getPlayer().getWorld().getName().equals("dungeon_yijki_a") && !ItemUtils.isAirOrNull(event.getPlayer().getItemInHand()) && event.getPlayer().getItemInHand().getType().name().contains("BUCKET")) {
            event.getPlayer().setVelocity(new Vector(0, -5, 0));
        }
    }


    @EventHandler(priority = EventPriority.LOWEST)
    public void command(PlayerCommandPreprocessEvent e) {
        if ((e.getMessage().equalsIgnoreCase("/f role") || e.getMessage().equalsIgnoreCase("/f rule") || e.getMessage().equalsIgnoreCase("/f access"))) {
            e.setMessage("/f perm");
            e.setCancelled(true);
        }

    }


/*    @EventHandler(priority = EventPriority.HIGH)
    public void diaPvP(EntityDamageByEntityEvent e) {
        if (e.getDamager().getLocation().getWorld().getName().equals("lms_world")) return;
        if (e.getDamager().getLocation().getWorld().getName().equals("world_outpost_pluto")) return;

        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            Player damager = (Player) e.getDamager();
            Player damaged = (Player) e.getEntity();
            if (((Player) e.getDamager()).getItemInHand() == null) return;
            if (((Player) e.getDamager()).getItemInHand().getType() == Material.AIR) return;

            double dmgBuff = 0;

            for (ItemStack armor : damaged.getInventory().getArmorContents()) {
                if (!armor.getType().name().startsWith("LEATHER_")) {
                    dmgBuff += 1500;
                }
            }
            e.setDamage(e.getDamage() * 1 + (dmgBuff*0.01));

        }
    }*/


    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        if (((e.getBlock().getType() == Material.BEDROCK || e.getBlock().getType() == Material.BARRIER) && !e.getPlayer().isOp())) {
            e.getPlayer().sendMessage(CC.translate("&cYou are not allowed to place this block!"));
            e.setCancelled(true);
        }
    }

/*    @EventHandler
    public void onPvPInInvis(EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (!(e.getDamager() instanceof Player)) return;
        if (((Player) e.getDamager()).getGameMode() != GameMode.SURVIVAL) return;
        Player victim = (Player) e.getEntity();
        if (victim.hasPotionEffect(PotionEffectType.INVISIBILITY)) {
            victim.removePotionEffect(PotionEffectType.INVISIBILITY);
        }


    }*/

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDurabilityLoss(PlayerItemDamageEvent event) {
        //event.getPlayer().updateInventory();
        event.setCancelled(true);

    }

    @EventHandler
    public void onClickBookAndQuil(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.LEFT_CLICK_BLOCK) {
            if (event.getPlayer().getItemInHand() != null && event.getPlayer().getItemInHand().getType() != Material.AIR) {
                if (event.getPlayer().getItemInHand().getType() == Material.BOOK_AND_QUILL || event.getPlayer().getItemInHand().getType() == Material.WRITTEN_BOOK) {
                    event.setCancelled(true);
                }
            }
        }
    }





/*    @EventHandler
    public void fixSlots(InventoryClickEvent e) {
        if (e.getWhoClicked() instanceof Player) {
            if (e.getCursor() != null) {
                if (e.getCursor().getType() != null && e.getCursor().getType() == Material.PAPER) {
                    NBTWrapper wrapper = new NBTWrapper(e.getCursor());
                    if (wrapper.hasKey("xpShopPrice")) {
                        wrapper.remove("xpShopPrice");
                        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData"));


                        e.setCursor(wrapper.setNBTBase("cosmicData", builder.get()).build());
                    }

                }
            }
        }
    }*/
}

