package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import lombok.SneakyThrows;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.listeners.CustomEnchantListener;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MasteryMarkOfTheBeast extends MasteryCustomEnchantment {
    private static Map<UUID, Long> markedPlayers;
    private MarkOfTheBeastListener listener;

    public MasteryMarkOfTheBeast() {
        super("Mark of the Beast", GeneralUtil.armor, 11);
        this.markedPlayers = new ConcurrentHashMap<UUID, Long>();
        this.listener = new MarkOfTheBeastListener(this);
        this.armorSet = "GHOST";
        Bukkit.getPluginManager().registerEvents((Listener) this.listener, (Plugin) Enchantments.getInstance());
        this.max = 6;
        this.base = 15.0;
    }

    @SneakyThrows
    public void applyDefenseEffect(final LivingEntity damagedPlayer, final LivingEntity attackerEnt, final int enchantLevel, final EntityDamageEvent event) {
    }

    public static void applyMarkEffect(final LivingEntity damagedPlayer, final LivingEntity attackerEnt, final int enchantLevel, final EntityDamageEvent event) throws Exception {
        if (!(damagedPlayer instanceof Player) || !(attackerEnt instanceof Player)) {
            return;
        }
        if (event.isCancelled()) return;
        final Player damaged = (Player) damagedPlayer;
        final Player attacker = (Player) attackerEnt;
        if (0.0075 >= Math.random()) {
            if (GeneralUtil.isAlly(damaged, attacker)) {
                return;
            }
            final double seconds = enchantLevel / 3 + 2;
            final UUID uuid = attacker.getUniqueId();
            if (!markedPlayers.containsKey(uuid)) {
                markedPlayers.put(uuid, (long) (System.currentTimeMillis() + seconds * 1000.0));
                de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), damaged.getEyeLocation(), 0f, 0.5f, 0f, 0.6f, 20);

                for (final Entity entity : attacker.getNearbyEntities((double) (20 + enchantLevel * 2), (double) (20 + enchantLevel * 2), (double) (20 + enchantLevel * 2))) {
                    if (entity instanceof Player) {
                        ((Player) entity).sendMessage(CC.YellowB + "* MARK OF THE BEAST [" + CC.Gray + attacker.getName() + ": " + NumberUtils.formatSeconds(seconds) + "s" + CC.YellowB + "] *");
                    }
                }

                attacker.sendMessage(CC.RedB + "* MARK OF THE BEAST [" + CC.Gray + NumberUtils.formatSeconds(seconds) + "s" + CC.RedB + "] *");
                Player player = (Player) attackerEnt;
                Bukkit.getScheduler().scheduleSyncDelayedTask((Plugin) Enchantments.getInstance(), () -> {
                    if (player.isValid()) {
                        player.sendMessage(CC.RedB + "* MARK OFF " + CC.RedB + "*");
                    }
                    markedPlayers.remove(uuid);
                }, (long) (20.0 * seconds));
            }
        }
    }

    public Map<UUID, Long> getMarkedPlayers() {
        return this.markedPlayers;
    }

    public MarkOfTheBeastListener getListener() {
        return this.listener;
    }

    class MarkOfTheBeastListener extends CustomEnchantListener<MasteryMarkOfTheBeast> {
        public MarkOfTheBeastListener(final MasteryMarkOfTheBeast enchant) {
            super(enchant);
        }

        @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
        public void onPLayerDamage(final EntityDamageEvent event) {
            if (!(event.getEntity() instanceof Player)) {
                return;
            }
            final Player player = (Player) event.getEntity();
            final Long expirations = ((MasteryMarkOfTheBeast) this.customEnchant).getMarkedPlayers().get(player.getUniqueId());
            if (expirations != null && expirations > System.currentTimeMillis()) {
                event.setDamage(event.getDamage() * 1.25);
                player.sendMessage(CC.Red + "* MARK OF THE BEAST [2X DMG] *");

            }
        }
    }
}
