package net.astropvp.core.items;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.Board;
import com.massivecraft.factions.FLocation;
import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Creeper;
import org.bukkit.entity.Player;
import org.bukkit.entity.Snowball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class ThrowableCreeperEggs implements Listener {

    private final AstroCore plugin;

    private static final String EGG_STRING_ID = "throwableCreeperEgg";
    private static final String CUSTOM_ITEM_STRING_ID = "extinctCustomItem";

    public ThrowableCreeperEggs() {
        this.plugin = AstroCore.getInstance();
    }

    public static ItemStack createThrowableCreeperEgg(int amount) {
        ItemStack itemStack = new ItemBuilder(Material.MONSTER_EGG, amount, (short) 50,
                "&2&lThrowable Creeper Egg",
                "&7Right click this to throw it"
        ).colorize().build();
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        NBTWrapper nbtItem = new NBTWrapper(itemStack);
        nbtItem.setString(CUSTOM_ITEM_STRING_ID, EGG_STRING_ID);
        return nbtItem.build();
    }

    public boolean isThrowableCreeperEgg(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) return false;
        if (itemStack.getType() != Material.MONSTER_EGG) return false;
        NBTWrapper nbtItem = new NBTWrapper(itemStack);
        if (!nbtItem.hasKey(CUSTOM_ITEM_STRING_ID)) return false;
        return nbtItem.getString(CUSTOM_ITEM_STRING_ID).equalsIgnoreCase(EGG_STRING_ID);
    }

    @EventHandler
    public void onThrowableCreeperEggUse(PlayerInteractEvent event) {
        if (!isThrowableCreeperEgg(event.getItem())) return;
        Player player = event.getPlayer();
        event.setCancelled(true);

        Vector vec = player.getEyeLocation().getDirection().setZ(0);
        Material material = player.getEyeLocation().add(vec).getBlock().getType();

        Location loc = player.getLocation().add(get(player));

        Material material2 = loc.getBlock().getType();

        if (player.getGameMode() != GameMode.SURVIVAL) return;

        if (material != Material.AIR && material != Material.STATIONARY_WATER && material != Material.WATER) {
            return;
        }

        if (material2 != Material.AIR && material2 != Material.STATIONARY_WATER && material2 != Material.WATER) {
            return;
        }

        if (FactionUtils.getFactionAt(new FLocation(player.getLocation())).isWarZone()) {
            player.sendMessage(CC.translate("&c(!) You cannot use that here."));
            return;
        }

        if (player.getItemInHand().getAmount() > 1) {
            player.getItemInHand().setAmount(player.getItemInHand().getAmount() - 1);
        } else {
            player.setItemInHand(null);
        }

        Snowball snowball = player.launchProjectile(Snowball.class);
        snowball.setMetadata(EGG_STRING_ID, new FixedMetadataValue(plugin, player.getName()));
    }

    @EventHandler
    public void onProjectileLand(ProjectileHitEvent e) {
        if (e.getEntity() instanceof Snowball) {
            if (e.getEntity().hasMetadata(EGG_STRING_ID)) {
                Location location = e.getEntity().getLocation();
                FLocation flocation = new FLocation(location);

                if (FactionUtils.isWarZone(location)) return;
                if (location.getBlock().getType() == Material.WEB) return;

                spawnCreeper(e.getEntity().getLocation());
            }
        }
    }

    public void spawnCreeper(Location location) {
        if (FactionUtils.getFactionAt(new FLocation(location)).isWarZone()) return;
        Creeper creeper = location.getWorld().spawn(location, Creeper.class);
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            Location creeperLocation = creeper.getLocation();
            creeper.remove();
            if (!FactionUtils.isWarZone(location)) {
                creeperLocation.getWorld().createExplosion(creeperLocation, 3, false);
            }

        }, (5L));
    }

    public static Vector get(Player player) {
        float yaw = player.getLocation().getYaw();
        if (yaw < 0) {
            yaw += 360;
        }
        if (yaw >= 315 || yaw < 45) {
            return new Vector(0, 0, 1);
        } else if (yaw < 135) {
            return new Vector(-1, 0, 0);
        } else if (yaw < 225) {
            return new Vector(0, 0, -1);
        } else if (yaw < 315) {
            return new Vector(1, 0, 0);
        }
        return new Vector(0, 0, 1);
    }
}
