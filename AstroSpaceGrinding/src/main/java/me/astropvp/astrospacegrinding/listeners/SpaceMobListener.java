package me.astropvp.astrospacegrinding.listeners;


import com.cosmicpvp.cosmicutils.utils.CC;
import me.astropvp.astrospacegrinding.AstroSpaceGrindingPlugin;
import me.astropvp.astrospacegrinding.data.SpacePlayerData;
import me.astropvp.astrospacegrinding.enums.MonsterType;
import me.astropvp.astrospacegrinding.utils.Clickable;
import me.astropvp.astrospacegrinding.utils.StringUtils;
import me.astropvp.astrospacegrinding.utils.TimeUtils;
import me.astropvp.astrospacegrinding.utils.config.ConfigManager;
import net.astropvp.lootfilter.LootFilterAPI;
import net.astropvp.loottable.struct.LootTable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;


public class SpaceMobListener implements Listener {
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onKill(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();

        if (AstroSpaceGrindingPlugin.activeMonsters.containsKey(entity.getUniqueId())) {
            MonsterType type = AstroSpaceGrindingPlugin.activeMonsters.get(entity.getUniqueId());

            event.getDrops().clear();
            event.setDroppedExp(0);
            for (ItemStack item : type.getInstance().getRewards(event.getEntity().getKiller(), event.getEntity())) {
                LootFilterAPI.giveItem(entity.getKiller(), LootTable.SPACE, item);
                //event.getDrops().add(item);
            }
            if (event.getEntity().getKiller() != null && event.getEntity().getKiller() != null) {
                Player damager = event.getEntity().getKiller();
                SpacePlayerData data = SpacePlayerData.findPlayer(damager);
                data.increment(type);

                type.getInstance().onDeath(event, data);

                int totalMobKills = data.getKills(MonsterType.SPACE_ALIEN);


                int messagePerKills = AstroSpaceGrindingPlugin.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("messagePerKills");
                if (totalMobKills % messagePerKills == 0) {


                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendMessage("");
                        player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&8«&b✩&8» &b&l" + damager.getName() + "'s Space Land Stats" + " &8«&b✩&8»")));
                        player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7Mobs Killed:&b " + totalMobKills)));
                        player.sendMessage(StringUtils.getCenteredMessage(CC.translate("&f* &7Time Spent:&b " + TimeUtils.format(data.getTimeInSpace()))));
                        player.sendMessage("");
                        Clickable spaceClickable = new Clickable("");
                        spaceClickable.add(StringUtils.getCenteredMessage(CC.translate("&7Click &f&nhere&7 to view " + damager.getName() + "'s collected rewards.")), CC.translate("&7View " + damager.getName() + "'s inventory"), "/invsee " + damager.getName());
                        spaceClickable.sendToPlayer(player);
                        player.sendMessage("");

                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHit(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) return;
        if (!(event.getDamager() instanceof Player)) return;

        if (AstroSpaceGrindingPlugin.activeMonsters.containsKey(entity.getUniqueId())) {
            MonsterType type = AstroSpaceGrindingPlugin.activeMonsters.get(entity.getUniqueId());
            type.getInstance().onPlayerDamageMob(event, (Player) event.getDamager());
        }
    }
}
