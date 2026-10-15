package net.astropvp.adminitems.items;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.CooldownUtils;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import net.astropvp.adminitems.AdminItemsPlugin;
import net.astropvp.adminitems.AstroAdminItemsAPI;
import net.astropvp.adminitems.cooldowns.AbilityCooldown;
import net.astropvp.adminitems.language.Language;
import net.astropvp.adminitems.managers.AdminItemAbilityManager;
import net.astropvp.adminitems.struct.AdminItem;
import net.astropvp.adminitems.struct.Interactable;
import net.astropvp.adminitems.struct.annotations.ConfigValue;
import net.astropvp.adminitems.tasks.TimeTravelerTask;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;
import java.util.List;

public class TimeTraveler extends AdminItem implements Interactable {

    private AdminItemAbilityManager abilityManager = AdminItemsPlugin.getInstance().getAbilityManager();

    @ConfigValue()
    private int time = 30;

    @ConfigValue()
    private int cooldown = 120;


    @Override
    public String getName() {
        return "TimeTraveler";
    }

    @Override
    public String getDisplayName() {
        return "&5&lTime Traveler";
    }

    @Override
    public List<String> getPerks() {
        return Arrays.asList(
                "&7Upon use, your current location will be",
                "&7saved and after &f" + time +  "&7 seconds you will",
                "&7be teleported back to that location."
        );
    }

    @Override
    public List<String> getLore() {
        return Arrays.asList(
                "&7&oMany people claim they have seen the",
                "&7&othe past but never seen the future.",
                "&7&oTime Travelers are everywhere and nowhere."
        );
    }



    @Override
    public ChatColor getColor() {
        return ChatColor.DARK_PURPLE;
    }

    @Override
    public Material getMaterial() {
        return Material.WATCH;
    }

    @Override
    public String getCooldownID() {
        return "timeTraveler";
    }


    @EventHandler
    public void onPlayerInteractWithAdminItem(PlayerInteractEvent event) {
        if (!AstroAdminItemsAPI.isAdminItem(event.getItem())) return;

        if (!AstroAdminItemsAPI.canUseAdminItem(event.getPlayer(), event.getItem(), getCooldownID(), this)) {
            return;
        }

        procOnClickAbility(event.getPlayer());

    }


    public void procOnClickAbility(Player player) {

        final Location spawnLocation = player.getLocation();
        CooldownUtils.registerCooldown(player, new AbilityCooldown(getCooldownID(), cooldown));
        Location holoLoc = spawnLocation.clone();
        holoLoc.add(0, 1.5, 0);
        Hologram hologram = HologramsAPI.createHologram(AdminItemsPlugin.getInstance(), holoLoc);
        hologram.appendTextLine(CC.translate("&d&l✰ &5&lTime Travelling &d&l✰"));
        hologram.appendTextLine(CC.translate("&d&l * &fTime: &d" + 0 + "s &fleft"));
        hologram.appendTextLine(CC.translate("&d&l * &fTraveler: &d" + player.getName()));
        player.sendMessage(CC.translate(Language.TIME_TRAVEL_USED.getMessage().replace("%time%", String.valueOf(time))));
        AstroAdminItemsAPI.decreaseUses(player, this);

        TimeTravelerTask timeTravelerTask = new TimeTravelerTask(this.time, hologram, player, spawnLocation);
        timeTravelerTask.runTaskTimer(AdminItemsPlugin.getInstance(), 0L, 20L);

        abilityManager.getTimeTravellers().put(player.getUniqueId(), timeTravelerTask);


    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (abilityManager.getTimeTravellers().containsKey(event.getEntity().getUniqueId())) {
            TimeTravelerTask timeTravelerTask = (TimeTravelerTask) abilityManager.getTimeTravellers().get(event.getEntity().getUniqueId());
            Language.TIME_TRAVEL_DIED.send(event.getEntity());
            timeTravelerTask.getHologram().delete();
            timeTravelerTask.cancel();
            abilityManager.getTimeTravellers().remove(event.getEntity().getUniqueId());
        }
    }

}