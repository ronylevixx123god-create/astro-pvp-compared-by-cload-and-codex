package me.ifamasssxd.cosmicbosses.struct;

import com.cosmicpvp.CosmicSpaceChests.CosmicSpaceChestsAPI;
import com.cosmicpvp.CosmicSpaceChests.libs.ItemRarity;
import com.cosmicpvp.CosmicSpaceChests.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.customevents.CustomCosmicPlayerEvent;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.PlayerUtil;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.bosses.GuardBoss;
import me.ifamasssxd.cosmicbosses.struct.enums.BossType;
import me.ifamasssxd.cosmicbosses.struct.enums.DamageType;
import me.ifamasssxd.cosmicbosses.struct.enums.Difficulty;
import me.ifamasssxd.cosmicbosses.struct.enums.Equipable;
import me.ifamasssxd.cosmicbosses.utils.*;
import net.astropvp.core.AstroCore;
import net.astropvp.core.struct.AstroPlayer;
import net.crankedpvp.shards.MobcoinAPI;
import net.minecraft.server.v1_8_R3.EntityInsentient;
import net.minecraft.server.v1_8_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_8_R3.PathfinderGoalRandomLookaround;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftLivingEntity;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public abstract class Boss extends Equipable implements Healable {
    public Boss(Location location, BossType bossType) {
        this.loops = 0;
        this.lootPool = new LinkedList<>();
        this.damageTracker = new DamageTracker();
        this.phase = Phase.ONE;
        this.random = new Random();
        this.naturallySpawned = false;
        this.dead = false;
        this.format = new DecimalFormat("#.##");
        this.totalPlayerDamageReceived = 0;
        this.location = location;
        this.bossType = bossType;
        this.antiFarm = new AntiFarm(this);
    }

    public LivingEntity spawnBoss() {
        LivingEntity e = (LivingEntity)this.location.getWorld().spawnEntity(this.location.clone(), getEntityType());
        e.setRemoveWhenFarAway(false);
        setupBoss(e);
        return e;
    }

    public double getHealthPercent() {
        if (getEntity() == null || !getEntity().isValid())
            return 0.0D;
        return getEntity().getHealth() / getEntity().getMaxHealth() * 100.0D;
    }

    protected int getSeconds(int amount) {
        return amount * 4;
    }

    public LivingEntity setupBoss(LivingEntity e) {
        this.uuid = e.getUniqueId();
        (this.entity = e).setMetadata("do_not_clear", new FixedMetadataValue(CosmicBosses.getInstance(), ""));
        e.setMetadata("boss", new FixedMetadataValue(CosmicBosses.getInstance(), ""));
        e.setMaxHealth(getMaxHealth());
        e.setRemoveWhenFarAway(false);
        e.setHealth(e.getMaxHealth());
        setEnt(e);
        e.setCustomName(getColor(true) + getCustomName());
        e.setCustomNameVisible(true);
        e.setRemoveWhenFarAway(false);
        bosses.put(e.getUniqueId(), this);
        return e;
    }

    public void onBossSpawn() {
        spawnBoss();
        alert();
    }

    public void onTick() {}

    public void onUpdate() {
        LivingEntity li = getEntity();
        if (li.hasPotionEffect(PotionEffectType.SLOW))
            li.removePotionEffect(PotionEffectType.SLOW);
        if (li.hasPotionEffect(PotionEffectType.WEAKNESS))
            li.removePotionEffect(PotionEffectType.WEAKNESS);
        if (this.loops % 120 == 0 && this.loops > 1) {
            BossMessage bossMessage = getBossMessage(getPhase().getPhase());
            if (bossMessage == null || this.random.nextInt(4) == 0)
                bossMessage = getGeneralMessage();
            if (bossMessage != null)
                for (Entity pl : getNearbyEntities(20.0D, EntityType.PLAYER)) {
                    Player p = (Player)pl;
                    sendMessage(p, ChatColor.ITALIC + bossMessage.getMessage());
                }
        }
    }

    public void onAttackPlayer(EntityDamageByEntityEvent event, Player player) {
        Location loc = getEntity().getLocation();
        if (!loc.getWorld().getName().equals("world_private") && !loc.getWorld().getName().startsWith("cosmic-station") && loc.getY() < loc.getWorld().getHighestBlockYAt(loc))
            event.setDamage(event.getDamage() * 3.0D);
/*        if (DungeonsAPI.hasDungeonDebuff(player, DungeonDebuff.BOSSES_DEAL_MORE_DAMAGE))
            event.setDamage(event.getDamage() * 2.5D);*/
    }

    public void onDamageTaken(EntityDamageByEntityEvent event, Entity damager) {
        if (event.isCancelled())
            return;
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager() instanceof org.bukkit.entity.Arrow) {
            if (((Projectile)event.getDamager()).getShooter() instanceof Player) {
                Player p = (Player)((Projectile)event.getDamager()).getShooter();
/*                if (DungeonsAPI.hasDungeonDebuff(p, DungeonDebuff.NO_BOW_DAMAGE_TO_BOSSES)) {
                    p.sendMessage(CC.translate("&c&l(!) &cYou cannot use bows to damage bosses in this heroic dungeon!"));
                    event.setCancelled(true);
                }*/
            }
            return;
        }
        if (event.getDamager() instanceof Player) {
            double mult = 1.0D;
                mult =  1.0D;

/*            if (DungeonsAPI.get().getPlayerDungeonData((Player)damager).getCurrentDungeon() != null && DungeonsAPI.get().getPlayerDungeonData((Player)damager).getCurrentDungeon().getBossSlayerTrinket())
                mult += 0.5D;*/
            event.setDamage(event.getDamage() * mult);
        }
        if (event.getDamager() instanceof Player) {
/*            if (DungeonsAPI.hasDungeonDebuff((Player)event.getDamager(), DungeonDebuff.BOSSES_TAKE_LESS_DAMAGE))
                event.setDamage(event.getDamage() * 0.25D);
            if (DungeonsAPI.hasDungeonDebuff((Player)event.getDamager(), DungeonDebuff.DEAL_30HP_MAX_PER_HIT) &&
                    event.getDamage() > 30.0D)
                event.setDamage(30.0D);*/
        }
        if (this instanceof GuardBoss)
            ((GuardBoss)this).onDamage(this, event);
    }

    public void onDamageTaken(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.FIRE || event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
            event.setCancelled(true);
            Entity ent = event.getEntity();
            Bukkit.getScheduler().scheduleSyncDelayedTask(CosmicBosses.getInstance(), () -> {
                if (ent != null && !ent.isDead())
                    ent.setFireTicks(0);
            },1L);
        }
        Location loc = getEntity().getLocation();
        if (!loc.getWorld().getName().equals("world_private") && loc.getWorld().getName().startsWith("cosmic-station") && loc.getY() < loc.getWorld().getHighestBlockYAt(loc))
            event.setDamage(event.getDamage() / 2.0D);
    }

    public void onEntityChangeBlock(EntityChangeBlockEvent event, Entity boss) {}

    public void onEntityTeleport(EntityTeleportEvent event, Entity boss) {}

    public void onDeath(EntityDeathEvent event) {
        if (this.dead)
            return;
        cleanup();
        if (event != null && !(this instanceof MiniBoss) && (!(this instanceof me.ifamasssxd.cosmicbosses.bosses.plaguebloater.PlagueBloater) || !this.location.getWorld().getName().equals(CosmicBosses.battlefieldWorld)) && !(this instanceof me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki) && !(this instanceof me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut)) {
            event.getDrops().clear();
            String msg = StringUtils.getCenteredMessage(ChatColor.RED + ChatColor.BOLD.toString() + "The " + getName() + " has been defeated!");
            String starting = ChatColor.RED + "Defeated by ";
            String killMsg = getKillMessage();
            for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                AstroPlayer astroPlayer = AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(player.getUniqueId());
                if (!astroPlayer.isBossMessagesEnabled()) continue;
                if (this instanceof GlobalBoss || BossUtils.isWarzone(player.getLocation()) || WorldGuardUtils.getRegionName(player.getLocation()).contains("spawn")) {
                    player.sendMessage(msg);
                    if (killMsg == null || killMsg.length() <= starting.length())
                        continue;
                    player.sendMessage(killMsg + "!");
                }
            }
            try {
                HashMap<UUID, List<ItemStack>> loot = givePlayerLootPool();
                alertDeathStats(loot);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (event != null && event.getEntity().hasMetadata("heroicEnvoyBoss")) {
            event.getDrops().clear();
            getLootPool().clear();
            getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
            getLootPool().add(CosmicSpaceChestsAPI.getDropPackageChest(ItemRarity.GOD));
            try {
                HashMap<UUID, List<ItemStack>> loot2 = givePlayerLootPool();
                alertDeathStats(loot2);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        if (event != null)
            event.getEntity().setMetadata("killed", new FixedMetadataValue(CosmicBosses.getInstance(), ""));
        this.dead = true;
        clearScoreboard();
    }

    private String getKillMessage() {
        String starting = ChatColor.RED + "Defeated by ";
        StringBuilder killers = new StringBuilder(starting);
        int place = 0;
        int minRequiredDamage = (int)(getMaxHealth() * 0.1D);
        List<Player> players = getTopDamage(10, false);
        for (Player p : players) {
            place++;
            int damage = getDamageTracker().get(p.getUniqueId()).intValue();
            if (damage >= minRequiredDamage) {
                ChatColor color;
                if (place == 1) {
                    color = ChatColor.GOLD;
                } else if (place == 2) {
                    color = ChatColor.YELLOW;
                } else if (place == 3) {
                    color = ChatColor.RED;
                } else {
                    color = ChatColor.GREEN;
                }
                killers.append(color + p.getName() + color + ", ");
            }
        }
        String killMsg = killers.toString();
        if (killMsg.equals(starting))
            if (players.size() > 0) {
                Player top = players.get(0);
                if (top != null) {
                    killMsg = killMsg + top.getName();
                } else {
                    killMsg = "Cosmonauts";
                }
            } else {
                killMsg = "Cosmonauts!";
            }
        if (killMsg.endsWith(", "))
            killMsg = killMsg.substring(0, killMsg.length() - 4);
        return StringUtils.getCenteredMessage(killMsg);
    }

    protected void alert() {
        String str1;
        if (this instanceof me.ifamasssxd.cosmicbosses.bosses.soulreaper.SoulReaper || this instanceof me.ifamasssxd.cosmicbosses.bosses.holidayspirit.HolidaySpirit || this instanceof me.ifamasssxd.cosmicbosses.bosses.xmasbattlefield.BattlefieldSnowman || this instanceof me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut || this instanceof me.ifamasssxd.cosmicbosses.dungeonboss.DungeonYijki || this instanceof me.ifamasssxd.cosmicbosses.evilabomination.EvilAbomination || (this instanceof me.ifamasssxd.cosmicbosses.bosses.plaguebloater.PlagueBloater && getEntity().getWorld().getName().equals(CosmicBosses.battlefieldWorld)))
            return;
        str1 = getColor(true) + "(!) " + getColor(false) + "A " + getColor(true) + ChatColor.UNDERLINE + getCustomName() + getColor(false) + " Boss has been summoned to the /spawn WarZone, at " + getColor(true) + this.location.getBlockX() + "x, " + this.location.getBlockY() + "y, " + this.location.getBlockZ() + "z " + getColor(false) + "defend AstroPvP!";
        if (this instanceof me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragon)
            str1 = CC.RedB + "(!) " + CC.Red + "A " + CC.DarkRedB + CC.Underline + getCustomName() + CC.Red + " has been awoken in the Ender Dimension. Travel to " + CC.DarkRed + "/warp end" + CC.Red + " to subdue it before it finds a way into the Cosmonaut realm!";
        Location loc = getLocation();
        String debug = ChatColor.YELLOW + "Spawning boss " + getCustomName() + ChatColor.YELLOW + " at " + ChatColor.YELLOW + ChatColor.BOLD + "x" + loc.getBlockX() + " y" + loc.getBlockY() + " z" + loc.getBlockZ() + "!";
        Collection<? extends Player> pls = Bukkit.getOnlinePlayers();

        boolean dragon = this instanceof me.ifamasssxd.cosmicbosses.bosses.dragon.EndDragon;
        for (Player pl : pls) {
            AstroPlayer astroPlayer = AstroCore.getInstance().getAstroPlayerManager().getAstroPlayer(pl.getUniqueId());
            if (!astroPlayer.isBossMessagesEnabled()) continue;
            pl.sendMessage("");
            pl.sendMessage(str1);
            pl.playSound(pl.getLocation(), dragon ? Sound.ENDERDRAGON_GROWL : Sound.CHICKEN_EGG_POP, 4.0F, 1.2F);
            pl.sendMessage("");
            if (pl.isOp())
                pl.sendMessage(debug);
        }
        Bukkit.getLogger().info("Spawning boss " + getCustomName() + " at " + getLocation().toString() + "!");
    }

    public BossMessage getBossMessage(int phase) {
        for (BossMessage msg : getGeneralMessages()) {
            if (msg.getPhase() == phase)
                return msg;
        }
        return null;
    }

    public BossMessage getStartingMessage() {
        for (BossMessage msg : getGeneralMessages()) {
            if (msg.getPhase() == -1)
                return msg;
        }
        return null;
    }

    public BossMessage getGeneralMessage() {
        List<BossMessage> msg = getGeneralMessages().stream().filter(bMsg -> (bMsg.getPhase() == 0 && !bMsg.isPhaseDependent())).collect(Collectors.toList());
        if (msg.size() <= 0)
            return null;
        return msg.get(this.random.nextInt(msg.size()));
    }

    public boolean checkStuck() {
        return getAntiFarm().checkStuck();
    }

    public List<Entity> getMinions() {
        if (this.minions == null)
            this.minions = new ArrayList<>();
        return this.minions;
    }

    public void removeMinions() {
        if (this.minions == null)
            return;
        Bukkit.getLogger().info("Clearing minions for " + getName());
        for (Entity minion : this.minions) {
            if (minion != null)
                minion.remove();
        }
        this.minions.clear();
    }

    public HashMap<BossAbility, Long> getAbilityCooldown() {
        if (this.abilityCooldowns == null)
            this.abilityCooldowns = new HashMap<>();
        return this.abilityCooldowns;
    }

    public List<Entity> getNearbyEntities(double radius, EntityType type) {
        List<Entity> entities = new ArrayList<>();
        for (Entity e : getEntity().getNearbyEntities(radius, radius, radius)) {
            if (type != null && e.getType() != type)
                continue;
            entities.add(e);
        }
        return entities;
    }

    public boolean hasMinions() {
        return (this.minions != null);
    }

    public void removeMinion(Entity ent) {
        if (hasMinions())
            this.minions.remove(ent);
    }

    public void updateScoreboard() {
        if (!CosmicBosses.getInstance().isScoreboardEnabled())
            return;
        for (Entity ent : getNearbyEntities(32.0D, EntityType.PLAYER)) {
            Player p = (Player)ent;
            ScoreboardUtils.updateBossScoreboard(p, this);
        }
    }

    public void clearScoreboard() {
        DamageTracker tracker = getDamageTracker();
        for (UUID uuid : tracker.keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null)
                ScoreboardUtils.clearBossScoreboard(p);
        }
    }

    public Entity spawnMinion(EntityType type, Location l) {
        Entity ent = l.getWorld().spawnEntity(l, type);
        ent.setMetadata("minion", new FixedMetadataValue(CosmicBosses.getInstance(), getEntity().getUniqueId()));
        ent.setMetadata("do_not_clear", new FixedMetadataValue(CosmicBosses.getInstance(), ""));
        getMinions().add(ent);
        if (ent instanceof LivingEntity) {
            LivingEntity li = (LivingEntity)ent;
            li.setRemoveWhenFarAway(false);
            li.setCustomName(getColor(true) + getName() + "'s Minion");
            li.setCustomNameVisible(true);
            if (li.getEquipment() != null) {
                li.getEquipment().setItemInHandDropChance(0.0F);
                li.getEquipment().setHelmetDropChance(0.0F);
                li.getEquipment().setChestplateDropChance(0.0F);
                li.getEquipment().setLeggingsDropChance(0.0F);
                li.getEquipment().setBootsDropChance(0.0F);
            }
        }
        if (type == EntityType.HORSE) {
            EntityInsentient insent = (EntityInsentient)((CraftLivingEntity)ent).getHandle();
            BossUtils.clearGoalSelectors(insent);
            BossUtils.addSelector(insent, 0, new PathfinderGoalFloat(insent));
            BossUtils.addSelector(insent, 8, new PathfinderGoalRandomLookaround(insent));
        }
        return ent;
    }

    public boolean isVulnerable(DamageType damageType) {
        return true;
    }

    public boolean isBossSpawned() {
        return (getEntity() != null && getEntity().isValid());
    }

    public void onHeal(int amount) {
        if (getEntity().isDead())
            return;
        if (getEntity().getHealth() + amount > getEntity().getMaxHealth()) {
            getEntity().setHealth(getEntity().getMaxHealth());
        } else {
            getEntity().setHealth(getEntity().getHealth() + amount);
        }
    }

    public void alertNearby(int radius, String message, Sound sound, float speed) {
        for (Entity e : getNearbyEntities(radius, EntityType.PLAYER)) {
            Player p = (Player)e;
            p.sendMessage(message);
            if (sound != null)
                p.playSound(p.getLocation(), sound, 5.0F, speed);
        }
    }

    public void sendMessage(String msg) {
        sendMessage(msg, null, 0.0F);
    }

    public int getMessageRadius() {
        return 20;
    }

    public void sendMessage(String msg, Sound sound, float speed) {
        List<Player> players = Lists.newArrayList();
        for (UUID uuid : getDamageTracker().keySet()) {
            Player p = Bukkit.getPlayer(uuid);
            if (p != null)
                players.add(p);
        }
        for (Entity ent : getNearbyEntities(getMessageRadius(), EntityType.PLAYER)) {
            Player pl = (Player)ent;
            if (!players.contains(pl))
                players.add(pl);
        }
        for (Player p2 : players) {
            if (p2 != null && p2.getWorld().equals(getEntity().getWorld()) && p2.getLocation().distanceSquared(getEntity().getLocation()) <= 1000.0D) {
                sendMessage(p2, msg);
                if (sound == null)
                    continue;
                p2.playSound(p2.getLocation(), sound, 2.0F, speed);
            }
        }
    }

    public void sendMessage(Player player, String msg) {
        player.sendMessage(getColor(true) + getName() + " >> " + ChatColor.WHITE + msg.replace("[NAME]", player.getName()));
    }

    public HashMap<UUID, List<ItemStack>> givePlayerLootPool() {
        HashMap<UUID, List<ItemStack>> loot = new HashMap<>();
        LinkedList<Player> topDamage = getTopDamage(getLootPool().size(), true);
        int amountPerPerson = (int)Math.floor(getLootPool().size() / topDamage.size());
        if (amountPerPerson > 2)
            amountPerPerson = 2;
        ItemStack title = null;
        if (getTitle() != null)
            title = ItemUtils.getTitleVoucher(getTitle());
        if (topDamage.size() > 0) {
            Player most = topDamage.get(0);
            if (most != null) {
                Bukkit.getPluginManager().callEvent(new CustomCosmicPlayerEvent(most, "BOSS_TOP_DAMAGE", new Object[] { this.bossType.name(), getEntity() }));
            }
        }
        Random random = new Random();
        int placement = 1;
        for (Player player : topDamage) {
            boolean noRoom = title != null && this.random.nextInt(100) == 0 && !addItem(player, title);
            int coinsAmount = random.nextInt(250 - 100) + 100;
            MobcoinAPI.addMobcoins(player, coinsAmount);
            player.sendMessage(CC.translate("&6&l+ &6⛃&e" + coinsAmount));
            System.out.println("Top Damage: " + player.getName() + " Amount Per: " + amountPerPerson);
            List<ItemStack> itemsReceived = Lists.newArrayList();
            for (int i = 0; i < amountPerPerson && !getLootPool().isEmpty(); i++) {
                ItemStack reward = getLootPool().remove();
                itemsReceived.add(reward);
                if (!addItem(player, reward))
                    noRoom = true;
                player.playSound(player.getLocation(), Sound.LEVEL_UP, 3.0F, 0.4F);
            }
            if (noRoom)
                (new BukkitRunnable() {
                    public void run() {
                        if (player.isValid())
                            player.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "You have new items in your " + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "/collect" + ChatColor.YELLOW + " stash!");
                    }
                }).runTaskLater(CosmicBosses.getInstance(), 5L);
            if (!itemsReceived.isEmpty())
                loot.put(player.getUniqueId(), itemsReceived);
            //Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "contest add " + player.getName() + " bossKills 1");
        }
        if (getLootPool().size() > 0) {
            Bukkit.getLogger().info("Found left over loot: " + getLootPool());
            for (Player pl : topDamage) {
                if (getLootPool().isEmpty())
                    break;
                List<ItemStack> lootGiven = loot.containsKey(pl.getUniqueId()) ? loot.get(pl.getUniqueId()) : new ArrayList<>();
                if (lootGiven.size() >= 2)
                    continue;
                ItemStack reward2 = getLootPool().remove();
                lootGiven.add(reward2);
                if (!addItem(pl, reward2))
                    (new BukkitRunnable() {
                        public void run() {
                            if (pl.isValid())
                                pl.sendMessage(ChatColor.YELLOW + ChatColor.BOLD.toString() + "(!) " + ChatColor.YELLOW + "You have new items in your " + ChatColor.YELLOW + ChatColor.BOLD + ChatColor.UNDERLINE + "/collect" + ChatColor.YELLOW + " stash!");
                        }
                    }).runTaskLater(CosmicBosses.getInstance(), 5L);
                loot.put(pl.getUniqueId(), lootGiven);
            }
        }
        return loot;
    }

    private boolean addItem(Player player, ItemStack item) {
        PlayerUtil.giveOrDropProtectedItem(player, item.clone(), 1);
        return true;
    }

    public LinkedHashMap<Player, Integer> getPlayerMap(LinkedHashMap<UUID, Integer> uuidMap) {
        LinkedHashMap<Player, Integer> map = new LinkedHashMap<>();
        for (Map.Entry<UUID, Integer> entry : uuidMap.entrySet()) {
            Player pl = Bukkit.getPlayer(entry.getKey());
            if (pl == null ||
                    map.containsKey(pl))
                continue;
            map.put(pl, entry.getValue());
        }
        return map;
    }

    public void alertDeathStats(HashMap<UUID, List<ItemStack>> lootGiven) {
        LinkedHashMap<UUID, Integer> topPlayers = MapUtils.sortByComparator(getDamageTracker(), false, 10);
        LinkedHashMap<Player, Integer> playerMap = getPlayerMap(topPlayers);
        if (this instanceof GlobalBoss) {
            for (Player player : Bukkit.getServer().getOnlinePlayers())
                alertDeathStatPlayer(player, topPlayers, playerMap, lootGiven);
        } else {
            for (Entity e : getEntity().getNearbyEntities(20.0D, 20.0D, 20.0D)) {
                if (e instanceof Player) {
                    Player player2 = (Player)e;
                    alertDeathStatPlayer(player2, topPlayers, playerMap, lootGiven);
                }
            }
            int minRequiredDamage = (int)(getTotalDamageTaken(playerMap) * 0.1D);
            String centered = StringUtils.getCenteredMessage(ChatColor.RED + "You did not do enough damage to earn loot!");
            for (Map.Entry<Player, Integer> players : playerMap.entrySet()) {
                Player pl = players.getKey();
                if (players.getValue().intValue() < minRequiredDamage) {
                    pl.sendMessage(centered);
                    continue;
                }
                if (!lootGiven.containsKey(pl.getUniqueId()))
                    continue;
                List<ItemStack> items = lootGiven.get(pl.getUniqueId());
                boolean alreadySent = false;
                for (ItemStack reward : items) {
                    if (!alreadySent) {
                        pl.sendMessage("");
                        pl.sendMessage(StringUtils.getCenteredMessage(ChatColor.GRAY + "You earned some loot for doing " + NumberUtils.addCommas(getDamageTracker().get(pl.getUniqueId()).intValue()) + " DMG!"));
                        alreadySent = true;
                    }
                    pl.sendMessage(StringUtils.getCenteredMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "+ " + reward.getItemMeta().getDisplayName().replace("(Right Click)", "")));
                }
            }
        }
    }

    public void alertDeathStatPlayer(Player p, LinkedHashMap<UUID, Integer> sortedPlayers, LinkedHashMap<Player, Integer> playerMap, HashMap<UUID, List<ItemStack>> items) {
        p.sendMessage("");
        p.sendMessage(StringUtils.getCenteredMessage(ChatColor.RED + ChatColor.BOLD.toString() + "TOP DAMAGE DEALT"));
        int index = 0;
        int sent = 0;
        for (Map.Entry<Player, Integer> entry : playerMap.entrySet()) {
            if (sent >= 5)
                continue;
            Player topDamageDealer = entry.getKey();
            double damage = getDamageTracker().get(topDamageDealer.getUniqueId());
            double percent = (damage / getMaxHealth()) * 100.0D;
            if (percent > 100) {
                percent = 100;
            }
            sent++;
            String message = ChatColor.GREEN + ChatColor.BOLD.toString() + ++index + ". " + ChatColor.GREEN + topDamageDealer.getName() + ChatColor.GREEN + ChatColor.BOLD + " - " + this.format.format(percent) + "%";
            if (items.containsKey(topDamageDealer.getUniqueId()))
                message = message + " - " + getLootString(items.get(topDamageDealer.getUniqueId()));
            p.sendMessage(StringUtils.getCenteredMessage(message));
        }
        if (index == 0) {
            int shown = 0;
            for (Map.Entry<Player, Integer> entry2 : playerMap.entrySet()) {
                if (shown >= 10)
                    break;
                Player pl = entry2.getKey();
                double damage = getDamageTracker().get(pl.getUniqueId());

                double percent = (damage / getMaxHealth()) * 100.0D;
                if (percent > 100) {
                    percent = 100;
                }
                index++;
                String message = StringUtils.getCenteredMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + index + ". " + ChatColor.GREEN + pl.getName() + ChatColor.GREEN + ChatColor.BOLD + " - " + this.format.format(percent) + "%");
                p.sendMessage(message);
                shown++;
            }
        }
    }

    public String getLootString(List<ItemStack> items) {
        return ChatColor.GOLD.toString() + ChatColor.BOLD + "OP Key";
    }

    public LinkedList<Player> getTopDamage(int amount, boolean checkDamage) {
        LinkedList<Player> topPlayer = new LinkedList<>();
        LinkedHashMap<UUID, Integer> sortedMap = MapUtils.sortByComparator(getDamageTracker(), false, amount);
        int minRequiredDamage = (int)(getTotalDamageTaken(null) * 0.1D);
        System.out.println("Min REquired DAmage: " + minRequiredDamage);
        for (Map.Entry<UUID, Integer> uuid : sortedMap.entrySet()) {
            if (topPlayer.size() < amount && (!checkDamage || uuid.getValue().intValue() > minRequiredDamage)) {
                Player p = Bukkit.getPlayer(uuid.getKey());
                if (p == null)
                    continue;
                topPlayer.add(p);
            }
        }
        return topPlayer;
    }

    private int getTotalDamageTaken(Map<Player, Integer> playerMap) {
        int totalDamage = 0;
        if (playerMap != null) {
            for (Map.Entry<Player, Integer> entry : playerMap.entrySet())
                totalDamage += entry.getValue().intValue();
        } else {
            for (Map.Entry<UUID, Integer> entry2 : getDamageTracker().entrySet())
                totalDamage += entry2.getValue().intValue();
        }
        return totalDamage;
    }

    public void onMinionUpdate(Entity minion, Event e) {}

    public String getColor(boolean includeBold) {
        return includeBold ? (getDifficulty().getColor() + ChatColor.BOLD.toString()) : getDifficulty().getColor().toString();
    }

    public int getLoops() {
        return this.loops;
    }

    public void setLoops(int loops) {
        this.loops = loops;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public Location getLocation() {
        return this.location;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public LinkedList<ItemStack> getLootPool() {
        return this.lootPool;
    }

    public DamageTracker getDamageTracker() {
        return this.damageTracker;
    }

    public Phase getPhase() {
        return this.phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public long getGetLastDamageDealt() {
        return this.getLastDamageDealt;
    }

    public void setGetLastDamageDealt(long getLastDamageDealt) {
        this.getLastDamageDealt = getLastDamageDealt;
    }

    public BlockModifier getModifier() {
        return this.modifier;
    }

    public AntiFarm getAntiFarm() {
        return this.antiFarm;
    }

    public BossType getBossType() {
        return this.bossType;
    }

    public boolean isNaturallySpawned() {
        return this.naturallySpawned;
    }

    public void setNaturallySpawned(boolean naturallySpawned) {
        this.naturallySpawned = naturallySpawned;
    }

    public static ConcurrentHashMap<UUID, Boss> bosses = new ConcurrentHashMap<>();

    protected int loops;

    public LivingEntity entity;

    public Location location;

    protected UUID uuid;

    private List<Entity> minions;

    private final LinkedList<ItemStack> lootPool;

    private final DamageTracker damageTracker;

    private Phase phase;

    private HashMap<BossAbility, Long> abilityCooldowns;

    private long getLastDamageDealt;

    protected BlockModifier modifier;

    protected Random random;

    protected AntiFarm antiFarm;

    private final BossType bossType;

    private boolean naturallySpawned;

    private boolean dead;

    DecimalFormat format;

    private final int totalPlayerDamageReceived;

    public abstract EntityType getEntityType();

    public abstract String getCustomName();

    public abstract String getName();

    public abstract void cleanup();

    public abstract String getTitle();

    public abstract void onPhaseChange(Phase paramPhase);

    public abstract void onMiscUpdate(Event paramEvent);

    public abstract DamageType getDamageType();

    public abstract Difficulty getDifficulty();

    public abstract int getMaxHealth();

    public abstract List<BossMessage> getGeneralMessages();
}
