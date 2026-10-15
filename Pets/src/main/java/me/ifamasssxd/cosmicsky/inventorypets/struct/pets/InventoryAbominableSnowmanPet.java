package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.Disguise;
import me.libraryaddict.disguise.disguisetypes.DisguiseType;
import me.libraryaddict.disguise.disguisetypes.MobDisguise;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class InventoryAbominableSnowmanPet extends InventoryPet {
    int effectDuration = 10;

    @Override
    public InventoryPets getOwner() {
        return InventoryPets.ABOMINABLE_SNOWMANPET;
    }




    @Override
    public List<String> getPetDescription() {
        return Lists.newArrayList(CC.Gray + "An enormous, monstrous spirit", CC.Gray + "well trained in illusionist magic.", String.valueOf(CC.Gray), CC.WhiteB + "Ability", CC.White + "[" + CC.DarkRed + "Polymorph" + CC.White + "] " + CC.Gray + "target player, transforming", CC.Gray + "them into harmless, immoble snowman for: " + this.freezeDuration() + "s", CC.Gray + "Any dammage dealt to them will break the spell", String.valueOf(CC.Gray), CC.WhiteB + "Cooldown", CC.Gray + " " + TimeUtils.formatSeconds(this.getCooldown()));
    }

    @Override
    public int getCooldown() {

        return 600;
    }




    @Override
    public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        FPlayer surroundingPlayer;
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis((long) this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);


        if (CooldownUtils.isCooldownActive(player, petItem.getPetType().name())) {
            player.sendMessage(CC.translate("&c&lPET: &cYou are still on" + petItem.getPetType().getPetName() + " &cfor another &7" + CooldownUtils.getFormattedCooldown(player, petItem.getPetType().name() + "&c.")));
            return true;
        }

        if (player.hasMetadata("spectator")) {
            return true;
        }
        if (player.hasMetadata("in_duel")) {
            return true;
        }
        Faction faction = FactionUtils.getFaction(player);
        if (faction == null || faction.isNone()) {
            throw new PrefixedPetException("You must be in a faction to use this pet!");
        }
        Location locationClicked = player.getLocation();
        if (!WorldGuardUtils.isPvPEnabled(locationClicked)) {
            player.sendMessage(ChatColor.RED + "Combat is disabled in this region.");
            return true;
        }
        this.effectDuration = petItem.getLevel();
        double closest = Double.MAX_VALUE;
        Entity closestp = null;
        for (Entity i : player.getNearbyEntities(3.0, 3.0, 3.0)) {
            Player p;
            if (!(i instanceof Player) || (p = (Player)i).getGameMode().equals(GameMode.CREATIVE) || AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.SEXY_HELMET) || p.hasMetadata("NPC")) continue;
            if (p.hasMetadata("in_duel")) {
                 continue;
            }
            double dist = i.getLocation().distance(event.getPlayer().getLocation());
            if (closest != Double.MAX_VALUE && dist >= closest) continue;
            closest = dist;
            closestp = i;
        }
        if (closestp == null) {
            player.sendMessage(CC.Red + "PET: No targets found.");
            return true;
        }
        Player p2 = (Player)closestp;
        if (p2.hasMetadata("frozenSnowman")) {
            p2.sendMessage(CC.Red + "Player is already a Snowman! ");
            return true;
        }
        FPlayer castingPlayer = FPlayers.getInstance().getByPlayer(player);
        if (FactionUtils.canPvP(player, p2)) {
            //petItem.setLastUsed(System.currentTimeMillis());
            //petItem.setLastUsed(System.currentTimeMillis());
            CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
            this.freezeThePlayer(p2, player);
            player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1f, 3.0f);
            player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1f, 3.0f);
            return true;
        }
        return true;
    }

    private void freezeThePlayer(final Player p, Player caster) {
        p.setFlying(false);
        MobDisguise snowman = new MobDisguise(DisguiseType.SNOWMAN);
        p.playSound(p.getLocation(), Sound.STEP_SNOW, 1.0f, 1.4f);
        DisguiseAPI.disguiseEntity(p, snowman);
        p.setWalkSpeed(0.0f);
        p.removePotionEffect(PotionEffectType.JUMP);
        p.removePotionEffect(PotionEffectType.SLOW);
        p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, (int)((long) this.freezeDuration() * 20L), 128));
        p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (int)((long) this.freezeDuration() * 20L), 128));
        p.setMetadata("frozenSnowman", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
        WorldUtil.sendWorldEffect(p.getLocation().getBlock(), 57, 2001);
        p.sendMessage(CC.YellowB + "** POLYMORPH'D [" + CC.Gray + this.freezeDuration() + "s" + CC.YellowB + "] " + CC.YellowB + "[" + CC.Gray + caster.getName() + CC.YellowB + "] **");
        caster.sendMessage(CC.YellowB + "** POLYMORPH'D [" + CC.Gray + this.freezeDuration() + "s" + CC.YellowB + "] " + CC.YellowB + "[" + CC.Gray + p.getName() + CC.YellowB + "] **");
        new BukkitRunnable(){

            public void run() {
                if (p.hasMetadata("cancelrunnable")) {
                    p.removeMetadata("cancelrunnable", CosmicSkyInventoryPets.get());
                } else {
                    InventoryAbominableSnowmanPet.this.removeDisguise(p);
                }
            }
        }.runTaskLater(CosmicSkyInventoryPets.get(), (long) this.freezeDuration() * 20L);
    }

    private int freezeDuration() {
        return effectDuration;
    }

    private void removeDisguise(Player player) {
        player.removeMetadata("frozenSnowman", CosmicSkyInventoryPets.get());
        player.removeMetadata("cancelrunnable", CosmicSkyInventoryPets.get());
        player.removePotionEffect(PotionEffectType.SLOW);
        player.removePotionEffect(PotionEffectType.JUMP);
        player.setWalkSpeed(0.2f);
        DisguiseAPI.undisguiseToAll(player);
        player.setFlying(true);
        player.sendMessage(CC.Red + "You are no longer a frozen Snowman!");
    }

    @Override
    public boolean onTickInHotBar(Player var1, int var2, InventoryPetItem var3) {
        return false;
    }

    public static int getDistance(Player e) {
        Location loc = e.getLocation().clone();
        double y = loc.getBlockY();
        int distance = 0;
        for (double i = y; i >= 0.0; i -= 1.0) {
            loc.setY(i);
            if (loc.getBlock().getType().isSolid()) break;
            ++distance;
        }
        return distance;
    }
}
