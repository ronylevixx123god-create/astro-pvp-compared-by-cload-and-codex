package me.ifamasssxd.cosmicbosses.dungeonboss.planetnul;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.Tuple;
import me.ifamasssxd.cosmicbosses.CosmicBosses;
import me.ifamasssxd.cosmicbosses.dungeonboss.DungeonAbility;
import me.ifamasssxd.cosmicbosses.struct.Boss;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ArmorMagnetAbility extends DungeonAbility {
    private List<Player> playersToPull;

    // location, new block - old block
    private HashMap<Location, Tuple<Tuple<Integer, Byte>, Tuple<Integer, Byte>>> replacedBlocks;
    private int tickCount = 0;

    public ArmorMagnetAbility(Boss boss) {
        super(boss);
        this.playersToPull = new ArrayList<>();
        this.replacedBlocks = new HashMap<>();
    }

    public void moveToward(Entity entity, Location to, double speed) {
        Location loc = entity.getLocation();
        double x = loc.getX() - to.getX();
        double y = loc.getY() - to.getY();
        double z = loc.getZ() - to.getZ();
        Vector velocity = new Vector(x, y, z).normalize().multiply(-speed);
        entity.setVelocity(velocity);
    }

    @Override
    public void onTick() {
        tickCount++;

        playersToPull.stream().filter(p -> p.getLocation().getWorld().getName().equals(boss.getLocation().getWorld().getName()) && !p.isDead()).forEach(p -> {
            if (tickCount % 20 == 0) {
                p.playSound(p.getLocation(), Sound.ENDERDRAGON_GROWL, 1, 1);
            }

            if (!p.hasMetadata("no_knockback"))
                moveToward(p, new Location(p.getWorld(), 782, ((CorruptCosmonaut) this.boss).hasFloorBroke() ? 87 : 115, 729), 0.325);
        });
    }

    @Override
    public long getCooldown() {
        return TimeUnit.SECONDS.toMillis(5L);
    }

    @Override
    public String getAbilityName() {
        return "Armor Magnet";
    }

    @Override
    public boolean shouldStopTicking() {
        return System.currentTimeMillis() - this.initTime >= this.getCooldown();
    }

    @Override
    public void onAbilityEnable() {
        this.initAbility();

        int yLevel = ((CorruptCosmonaut) this.boss).hasFloorBroke() ? 87 : 115;

        for (int i = 0; i < 7; i++) {
            Location replaceLocation = new Location(this.boss.getLocation().getWorld(), 785, yLevel, 732 - i);
            this.replacedBlocks.put(replaceLocation, new Tuple<>(new Tuple<>(35, i % 2 == 0 ? (byte) 14 : (byte) 11), new Tuple<>(replaceLocation.getBlock().getTypeId(), replaceLocation.getBlock().getData())));
        }

        for (int i = 0; i < 7; i++) {
            Location replaceLocation = new Location(this.boss.getLocation().getWorld(), 785 - i, yLevel, 726);
            this.replacedBlocks.put(replaceLocation, new Tuple<>(new Tuple<>(35, i % 2 == 0 ? (byte) 14 : (byte) 11), new Tuple<>(replaceLocation.getBlock().getTypeId(), replaceLocation.getBlock().getData())));
        }

        for (int i = 0; i < 7; i++) {
            Location replaceLocation = new Location(this.boss.getLocation().getWorld(), 779, yLevel, 726 + i);
            this.replacedBlocks.put(replaceLocation, new Tuple<>(new Tuple<>(35, i % 2 == 0 ? (byte) 14 : (byte) 11), new Tuple<>(replaceLocation.getBlock().getTypeId(), replaceLocation.getBlock().getData())));
        }

        for (int i = 0; i < 7; i++) {
            Location replaceLocation = new Location(this.boss.getLocation().getWorld(), 779 + i, yLevel, 732);
            this.replacedBlocks.put(replaceLocation, new Tuple<>(new Tuple<>(35, i % 2 == 0 ? (byte) 14 : (byte) 11), new Tuple<>(replaceLocation.getBlock().getTypeId(), replaceLocation.getBlock().getData())));
        }

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Location blockLocation = new Location(this.boss.getLocation().getWorld(), 780 + j, yLevel, 727 + i);
                blockLocation.getBlock().setMetadata("armorMagnetPortal", new FixedMetadataValue(CosmicBosses.get(), true));
                this.replacedBlocks.put(blockLocation, new Tuple<>(new Tuple<>(Material.ENDER_PORTAL.getId(), (byte) 0), new Tuple<>(blockLocation.getBlock().getTypeId(), blockLocation.getBlock().getData())));

                Location blockUnderLocation = blockLocation.clone().add(0, -1, 0);
                this.replacedBlocks.put(blockUnderLocation, new Tuple<>(new Tuple<>(Material.BEDROCK.getId(), (byte) 0), new Tuple<>(blockUnderLocation.getBlock().getTypeId(), blockUnderLocation.getBlock().getData())));
            }
        }

        this.replacedBlocks.forEach((k, v) -> {
            k.getBlock().setTypeIdAndData(v.getLeft().getLeft(), v.getLeft().getRight(), true);
        });

        boss.getNearbyEntities(300.0, EntityType.PLAYER).forEach(p -> {
            boss.sendMessage((Player) p, CC.translate("&5&lYou call that protection? Pitful!"));
            this.playersToPull.add((Player) p);
        });
    }

    @Override
    public void onAbilityDisable() {
        this.replacedBlocks.forEach((k, v) -> {
            k.getBlock().setTypeIdAndData(v.getRight().getLeft(), v.getRight().getRight(), true);
        });

        this.playersToPull.clear();
        this.replacedBlocks.clear();

        this.tickCount = 0;

        ((CorruptCosmonaut) this.boss).doTrickster();
    }

    @Override
    public void onAbilityEvent(Event p0) {

    }
}
