package org.arkhamnetwork.enchantments.enchantments.swords;

import org.arkhamnetwork.enchantments.Enchantments;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class AgainstTheOddsTest {
    private final AtomicLong now = new AtomicLong(100000);
    private final List<Player> players = new ArrayList<>();
    private final Set<UUID> team = new HashSet<>();
    private World world;
    private Player user;
    private AgainstTheOdds enchant;
    private Enchantments plugin;

    private static void set(Object object, Class<?> owner, String name, Object value) throws Exception {
        Field f = owner.getDeclaredField(name); f.setAccessible(true); f.set(object, value);
    }
    @Before public void setup() throws Exception {
        Server server = mock(Server.class);
        when(server.getPluginManager()).thenReturn(mock(org.bukkit.plugin.PluginManager.class));
        when(server.getScheduler()).thenReturn(mock(BukkitScheduler.class));
        set(null, Bukkit.class, "server", server);
        plugin = mock(Enchantments.class);
        set(plugin, JavaPlugin.class, "isEnabled", true);
        set(null, Enchantments.class, "instance", plugin);
        world = mock(World.class);
        when(world.getPlayers()).thenReturn(players);
        user = player(0);
        team.add(user.getUniqueId());
        enchant = new AgainstTheOdds(now::get, (a, b) -> team.contains(a.getUniqueId()) && team.contains(b.getUniqueId()));
    }
    private Player player(double x) {
        Player p = mock(Player.class);
        when(p.getUniqueId()).thenReturn(UUID.randomUUID());
        when(p.isOnline()).thenReturn(true);
        when(p.getGameMode()).thenReturn(GameMode.SURVIVAL);
        when(p.getWorld()).thenReturn(world);
        when(p.getLocation()).thenReturn(new Location(world, x, 64, 0));
        when(p.getMetadata("vanished")).thenReturn(Collections.emptyList());
        when(p.canSee(any(Player.class))).thenReturn(true);
        players.add(p); return p;
    }
    private EntityDamageByEntityEvent hit(Entity attacker, Player victim) {
        return new EntityDamageByEntityEvent(attacker, victim, EntityDamageEvent.DamageCause.ENTITY_ATTACK, 20);
    }
    private Player opponent(double x) {
        Player p = player(x); enchant.trackCombat(hit(p, user)); return p;
    }
    private Player[] three() { return new Player[]{opponent(2), opponent(3), opponent(4)}; }
    private Player friend(double x, Player enemy, boolean active) {
        Player p = player(x); team.add(p.getUniqueId());
        if (active) enchant.trackCombat(hit(p, enemy));
        return p;
    }
    private double damage(Player target, int level) {
        EntityDamageByEntityEvent event = hit(user, target);
        enchant.applyEffect(user, target, level, event); return event.getDamage();
    }
    @Test public void legendarySwordHasThreeLevels() {
        assertEquals("Against the Odds", enchant.name());
        assertEquals(3, enchant.getMaxLevel()); assertEquals(5, enchant.getTier());
    }
    @Test public void allThreeLevelsBoostNormalMeleeDamage() {
        Player[] enemies = three();
        assertEquals(20.6, damage(enemies[0], 1), 1e-9);
        assertEquals(21.2, damage(enemies[0], 2), 1e-9);
        assertEquals(22, damage(enemies[0], 3), 1e-9);
    }
    @Test public void twoActiveOpponentsAreInsufficient() {
        Player target = opponent(2); opponent(3);
        assertEquals(20, damage(target, 3), 0);
    }
    @Test public void inactiveNearbyPlayersDoNotCount() {
        Player target = opponent(2); opponent(3); player(4); player(5);
        assertEquals(20, damage(target, 3), 0);
    }
    @Test public void equalThreeVersusThreeFightQualifies() {
        Player[] enemies = three(); friend(6, enemies[0], true); friend(7, enemies[1], true);
        assertEquals(22, damage(enemies[0], 3), 1e-9);
    }
    @Test public void friendlyNumbersAdvantageDoesNotQualify() {
        Player[] enemies = three();
        friend(6, enemies[0], true); friend(7, enemies[1], true); friend(8, enemies[2], true);
        assertEquals(20, damage(enemies[0], 3), 0);
    }
    @Test public void inactiveFriendsDoNotArtificiallyDisableBonus() {
        Player[] enemies = three();
        friend(6, enemies[0], false); friend(7, enemies[0], false); friend(8, enemies[0], false);
        assertEquals(22, damage(enemies[0], 3), 1e-9);
    }
    @Test public void seventeenthBlockIsIncludedButFurtherPlayersAreExcluded() {
        Player target = opponent(2); opponent(3); Player edge = opponent(17);
        assertEquals(22, damage(target, 3), 1e-9);
        when(edge.getLocation()).thenReturn(new Location(world, 17.001, 64, 0));
        assertEquals(20, damage(target, 3), 0);
    }
    @Test public void combatExpiresAfterSixSeconds() {
        Player[] enemies = three(); now.addAndGet(6000);
        assertEquals(22, damage(enemies[0], 3), 1e-9);
        now.incrementAndGet(); assertEquals(20, damage(enemies[0], 3), 0);
    }
    @Test public void cancelAndZeroDamageDoNotCreateActiveOpponents() {
        Player target = opponent(2); opponent(3); Player other = player(4);
        EntityDamageByEntityEvent cancelled = hit(other, user); cancelled.setCancelled(true);
        enchant.trackCombat(cancelled);
        assertEquals(20, damage(target, 3), 0);
        EntityDamageByEntityEvent zero = hit(other, user); zero.setDamage(0); enchant.trackCombat(zero);
        assertEquals(20, damage(target, 3), 0);
    }
    @Test public void alliesNeverCountAsOpponents() {
        Player target = opponent(2); opponent(3); Player ally = friend(4, target, false);
        enchant.trackCombat(hit(ally, user)); assertEquals(20, damage(target, 3), 0);
    }
    @Test public void invisibleAndSpectatorOpponentsAreExcluded() {
        Player[] enemies = three(); when(user.canSee(enemies[2])).thenReturn(false);
        assertEquals(20, damage(enemies[0], 3), 0);
        when(user.canSee(enemies[2])).thenReturn(true);
        when(enemies[2].getGameMode()).thenReturn(GameMode.SPECTATOR);
        assertEquals(20, damage(enemies[0], 3), 0);
    }
    // The 1.8 API contains a legacy getShooter return-type bridge; use a concrete
    // covariant declaration so Mockito preserves the modern bridge as CraftArrow does.
    public abstract static class TestProjectile implements Projectile {
        @Override public abstract LivingEntity getShooter();
    }
    @Test public void projectileActivityCountsButSwordBonusDoesNotApplyToArrows() {
        Player target = opponent(2); opponent(3); Player archer = player(4);
        TestProjectile arrow = mock(TestProjectile.class); when(arrow.getShooter()).thenReturn(archer);
        enchant.trackCombat(hit(arrow, user)); assertEquals(22, damage(target, 3), 1e-9);
        when(arrow.getShooter()).thenReturn(user);
        EntityDamageByEntityEvent event = hit(arrow, target);
        enchant.applyEffect(user, target, 3, event); assertEquals(20, event.getDamage(), 0);
    }
    @Test public void quittingRemovesCombatActivity() {
        Player[] enemies = three(); enchant.quit(new PlayerQuitEvent(enemies[2], ""));
        assertEquals(20, damage(enemies[0], 3), 0);
    }
    @Test public void cancelledHitNeverReceivesBonus() {
        Player[] enemies = three(); EntityDamageByEntityEvent event = hit(user, enemies[0]); event.setCancelled(true);
        enchant.applyEffect(user, enemies[0], 3, event); assertEquals(20, event.getDamage(), 0);
    }
    @Test public void invalidLevelsDoNotBoostDamage() {
        Player[] enemies = three(); assertEquals(20, damage(enemies[0], 0), 0); assertEquals(20, damage(enemies[0], 4), 0);
    }
    @Test public void existingDescriptionsArePreservedAndNewEnchantHasFallback() {
        org.bukkit.configuration.file.YamlConfiguration config = new org.bukkit.configuration.file.YamlConfiguration();
        when(plugin.getConfig()).thenReturn(config);
        when(Bukkit.getServer().getLogger()).thenReturn(java.util.logging.Logger.getAnonymousLogger());
        org.arkhamnetwork.enchantments.util.EnchantmentDescriptions.reload();
        assertEquals(enchant.getDescription(), org.arkhamnetwork.enchantments.util.EnchantmentDescriptions.getDescription(enchant));
        config.set("descriptions", Collections.singletonList("Against the Odds=Custom = balanced"));
        org.arkhamnetwork.enchantments.util.EnchantmentDescriptions.reload();
        assertEquals("Custom = balanced", org.arkhamnetwork.enchantments.util.EnchantmentDescriptions.getDescription(enchant));
        org.arkhamnetwork.enchantments.util.EnchantmentDescriptions.reload();
    }
}
