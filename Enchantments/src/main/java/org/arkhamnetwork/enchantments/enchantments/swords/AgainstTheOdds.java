package org.arkhamnetwork.enchantments.enchantments.swords;

import com.rit.sucy.CustomEnchantment;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.LongSupplier;

/** New opt-in enchantment. Does not alter any original enchantment. */
public class AgainstTheOdds extends CustomEnchantment implements Listener {
    private static final long COMBAT_WINDOW_MS = 6000;
    private final Map<UUID, Map<UUID, Long>> combat = new HashMap<>();

    private final LongSupplier clock;
    private final BiPredicate<Player, Player> allies;

    public AgainstTheOdds() {
        this(System::currentTimeMillis, GeneralUtil::isAlly);
    }

    AgainstTheOdds(LongSupplier clock, BiPredicate<Player, Player> allies) {
        super("Against the Odds", GeneralUtil.swords, 2);
        this.clock = clock;
        this.allies = allies;
        this.description = "Gain bonus sword damage against three or more active opponents within 17 blocks, unless your side has more active players.";
        max = 3;
        base = 15.0;
        interval = 5.0;
        Bukkit.getPluginManager().registerEvents(this, Enchantments.getInstance());
        Bukkit.getScheduler().runTaskTimer(Enchantments.getInstance(), () -> {
            long now = clock.getAsLong();
            for (Map<UUID, Long> entries : combat.values())
                entries.values().removeIf(time -> now - time > COMBAT_WINDOW_MS);
            combat.values().removeIf(Map::isEmpty);
        }, 120L, 120L);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void trackCombat(EntityDamageByEntityEvent event) {
        Entity source = event.getDamager();
        if (source instanceof Projectile) {
            Object shooter = ((Projectile) source).getShooter();
            if (!(shooter instanceof Player)) return;
            source = (Player) shooter;
        }
        if (!(source instanceof Player) || !(event.getEntity() instanceof Player)
                || event.isCancelled() || event.getFinalDamage() <= 0) return;
        Player attacker = (Player) source, victim = (Player) event.getEntity();
        if (attacker.equals(victim) || allies.test(attacker, victim)) return;
        long now = clock.getAsLong();
        remember(attacker.getUniqueId(), victim.getUniqueId(), now);
        remember(victim.getUniqueId(), attacker.getUniqueId(), now);
    }

    private void remember(UUID player, UUID opponent, long now) {
        combat.computeIfAbsent(player, key -> new HashMap<>()).put(opponent, now);
    }

    private boolean recent(Player player, Player opponent, long now) {
        Map<UUID, Long> entries = combat.get(player.getUniqueId());
        if (entries == null) return false;
        entries.values().removeIf(time -> now - time > COMBAT_WINDOW_MS);
        return entries.containsKey(opponent.getUniqueId());
    }

    @EventHandler
    public void quit(PlayerQuitEvent event) {
        UUID id = event.getPlayer().getUniqueId();
        combat.remove(id);
        for (Map<UUID, Long> entries : combat.values()) entries.remove(id);
    }

    public static double multiplier(int level, int opponents, int teammates) {
        if (level < 1 || level > 3 || opponents < 3 || opponents < teammates + 1) return 1.0;
        return new double[]{1.03, 1.06, 1.10}[level - 1];
    }

    private boolean eligible(Player viewer, Player player) {
        return player.isOnline() && !player.isDead() && player.getGameMode() != GameMode.SPECTATOR
                && !player.getMetadata("vanished").stream().anyMatch(value -> value.asBoolean()) && viewer.canSee(player);
    }

    @Override
    public void applyEffect(LivingEntity user, LivingEntity target, int level, EntityDamageByEntityEvent event) {
        if (!(user instanceof Player) || !(target instanceof Player) || event.isCancelled()
                || event.getDamager() != user || allies.test((Player) user, (Player) target)) return;
        Player player = (Player) user;
        if (!eligible(player, player) || !eligible(player, (Player) target)) return;
        long now = clock.getAsLong();
        List<Player> enemies = new ArrayList<>(), friends = new ArrayList<>();
        for (Player other : player.getWorld().getPlayers()) {
            if (other.equals(player) || !eligible(player, other)
                    || player.getLocation().distanceSquared(other.getLocation()) > 17 * 17) continue;
            if (allies.test(player, other)) friends.add(other);
            else if (recent(player, other, now)) enemies.add(other);
        }
        int teammates = 0;
        for (Player friend : friends) {
            for (Player enemy : enemies) {
                if (recent(friend, enemy, now)) { teammates++; break; }
            }
        }
        event.setDamage(event.getDamage() * multiplier(level, enemies.size(), teammates));
    }
}
