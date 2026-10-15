package net.astropvp.adminitems.items;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import net.astropvp.adminitems.AstroAdminItemsAPI;
import net.astropvp.adminitems.cooldowns.AbilityCooldown;
import net.astropvp.adminitems.language.Language;
import net.astropvp.adminitems.struct.AdminItem;
import net.astropvp.adminitems.struct.Interactable;
import net.astropvp.adminitems.struct.annotations.ConfigValue;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GrappleHook extends AdminItem implements Interactable {

    @ConfigValue()
    private int cooldown = 10;

    private final HashMap<Player, Vector> grapple = new HashMap<>();


    @Override
    public String getName() {
        return "GrappleHook";
    }

    @Override
    public String getDisplayName() {
        return "&b&lGrapple Hook";
    }

    @Override
    public List<String> getPerks() {
        return Arrays.asList(
                "&7Grapple yourself across the world",
                "&7and pull other enemies towards you.",
                "&7There is a " + cooldown + " seconds cooldown",
                "&7between every grapple use."
        );
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&7&oEscape from all your problems with",
                "&7&othe grappler hook. Escape from reality.",
                "&7&oand start a new adventure on astro."
        );
    }


    @Override
    public ChatColor getColor() {
        return ChatColor.AQUA;
    }

    @Override
    public Material getMaterial() {
        return Material.FISHING_ROD;
    }

    @Override
    public String getCooldownID() {
        return "grappleHook";
    }

    @EventHandler
    public void onGrappleLaunch(ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player)) {
            return;
        }
        final Player shooter = (Player) e.getEntity().getShooter();
        if (!AstroAdminItemsAPI.isAdminItem(shooter.getItemInHand())) {
            return;
        }
        if (AstroAdminItemsAPI.getAdminItemFromItem(shooter.getItemInHand()) != this) {
            return;
        }

        this.grapple.put(shooter, e.getEntity().getVelocity());
    }

    @EventHandler
    public void onPlayerFish(PlayerFishEvent e) {
        final ItemStack item = e.getPlayer().getItemInHand();
        if (!AstroAdminItemsAPI.isAdminItem(item)) return;

        if (!AstroAdminItemsAPI.canUseAdminItem(e.getPlayer(), item, getCooldownID(), this)) {
            return;
        }

        if (e.getState() == PlayerFishEvent.State.FISHING) return;
        final Player p = e.getPlayer();
        if (!this.grapple.containsKey(p)) {
            return;
        }

        if (e.getCaught() instanceof Player) {
            final Location loc = e.getCaught().getLocation();
            final Location hookLoc = p.getLocation();
            final double dis = loc.distance(hookLoc);
            Vector direction = e.getCaught().getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
            final double Y = (1.0 + 0.12 * dis) * (hookLoc.getY() - loc.getY()) / dis - -0.04 * dis;
            direction.setY(Y);
            direction.setX(direction.getX() * -1 * (0.11 * dis));
            direction.setZ(direction.getZ() * -1 * (0.11 * dis));
            e.getCaught().setVelocity(direction);
            p.sendMessage(CC.translate(Language.GRAPPLED_ENEMY.getMessage()).replace("%enemy%", e.getCaught().getName()));
            e.getCaught().sendMessage(CC.translate(Language.GRAPPLED_BY_PLAYER.getMessage()).replace("%player%", p.getName()));

        } else {
            final double distance = p.getLocation().distance(e.getHook().getLocation());
            final Location loc = p.getLocation();
            final Location hookLoc = e.getHook().getLocation();
            final double dis = loc.distance(hookLoc);
            final double X = (1.0 + 0.24 * dis) * (hookLoc.getX() - loc.getX()) / dis;
            final double Y = (1.0 + 0.12 * dis) * (hookLoc.getY() - loc.getY()) / dis - -0.04 * dis;
            final double Z = (1.0 + 0.24 * dis) * (hookLoc.getZ() - loc.getZ()) / dis;
            final Vector v = p.getVelocity();
            v.setX(X);
            v.setY(Y);
            v.setZ(Z);
            p.setVelocity(v);
            p.setVelocity(this.grapple.get(p).multiply(distance * 0.3).setY((distance * 0.1 > 1.0) ? 1.0 : ((p.getLocation().getPitch() < -70.0f) ? 1.25 : ((p.getLocation().getPitch() < -50.0f) ? 1.125 : 1.0))));
            Language.GRAPPLED.send(p);
        }
        this.grapple.remove(p);
        item.setDurability((short) 0);
        p.setItemInHand(item);
        AstroAdminItemsAPI.decreaseUses(p, this);
        CooldownUtils.registerCooldown(p, new AbilityCooldown(getCooldownID(), cooldown));
    }

}


