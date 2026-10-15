package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.avaje.ebean.validation.NotNull;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.helmets.UndertowHelmet;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.utils.CrankedAmuletUtil;
import me.ifamasssxd.cosmicsky.inventorypets.utils.DamageIndicatorUtils;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InventoryMutatedWorldDestroyerPet extends InventoryPet {
    public static ArrayList<Location> locations = new ArrayList<>();

    public InventoryPets getOwner() {
        return InventoryPets.MUTATED_WORLD_DESTROYER;
    }

    public List<String> getPetDescription() {
        return Lists.newArrayList(CC.Gray + "A Mutated fragment of the",
                CC.Gray + "legendary World Destroyer, Yijki.",
                String.valueOf(CC.Gray),
                CC.DarkGreenB + "Ability",
                CC.Gray + "Summon the power of Yijki's",
                CC.Gray + "[" + CC.Green + "Mutated Dimensional Cage" + CC.Gray + "] ability (" + this.cageDuration + "s)",
                CC.translate("&a* &7Bypass all ItemSkins"),
                CC.translate("&a* &72x Cage duration"),
                CC.translate("&a* &72x Tick damage"),
                String.valueOf(CC.Gray),
                CC.DarkGreenB + "Cooldown",
                CC.Gray + " " + TimeUtils.formatSeconds(getCooldown()));
    }

    private double cageDuration = 10.0;


    public int getCooldown() {
        return 900;
    }

    public int getExpForLevel(int level) {
        return 1700 * 5;
    }

    public int getMaxLevel() {
        return 5;
    }


    public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis(this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);


        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new PrefixedPetException("Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));

        if (player.hasMetadata("in_duel"))
            return true;
        if (player.hasMetadata("caged"))
            return true;
        if (player.hasMetadata("spectator"))
            return true;
        Faction faction = FactionUtils.getFaction(player);
        Location locationClicked = player.getLocation();
        Player target = null;
        for (Entity other : player.getNearbyEntities(4.0D, 4.0D, 4.0D)) {
            if (other instanceof Player) {
                Player nearPlayer = (Player) other;
                if (!WorldGuardUtils.isPvPEnabled(nearPlayer.getLocation()))
                    continue;
                if (!player.canSee(nearPlayer))
                    continue;
                if (nearPlayer.getGameMode() == GameMode.CREATIVE)
                    continue;
                Faction nearFac = FactionUtils.getFaction(nearPlayer);
                Relation rel = nearFac.getRelationTo(faction);
                if (rel.isAlly() || rel.isTruce())
                    continue;
                if (rel.isMember())
                    continue;
                double distance = (event == null) ? 10.9D : 6.9D;
                if (player.getLocation().distance(other.getLocation()) < distance) {
                    target = nearPlayer;
                    break;
                }
            }
        }


        if (MaskType.hasEquipped(player, "ELF")) {
            cageDuration = 10;
        } else if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.VALENTINES_2022_MUTATED_BOOTS)) {
            cageDuration = 8.5;

        } else {
            cageDuration = 7.5;
        }


        if (WorldGuardUtils.isPvPDisabled(locationClicked))
            throw new PrefixedPetException("Not allowed in this region!");
        if (player.hasMetadata("inDungeonParkour"))
            throw new PrefixedPetException("You cannot use this pet while in the dungeon parkour.");
        if (target == null || (event != null && target.getLocation().distance(player.getLocation()) > 4.0D))
            throw new PrefixedPetException("Could not find a valid target!");
        /*if (CageCooldown.isOnCageCooldown(target))
            throw new PrefixedPetException(target.getName() + " is still on Cage Cooldown! This lasts 20 seconds.");*/




/*        if (CosmicSkyInventoryPets.get().getCooldownUtils().isOnCooldown(target, "cageCooldown")) {
            player.sendMessage(CC.translate("&c&lPET: &c" + target.getName() + "&c is still on cage cooldown for &n" + CosmicSkyInventoryPets.get().getCooldownUtils().getRemainingTimeFormatted(target, "cageCooldown") + "&c."));
            return false;
        }*/


        //CageCooldown.enterCageCooldown(target);
        player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
        player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);

        target.setMetadata("caged", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
        player.setMetadata("caged", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));

        player.sendMessage("");
        player.sendMessage(CC.translate("&a&l*** &2&l&nMUTATED&a&l WORLD DESTROYER PET [&f" + target.getName() + "&a&l] - CAGED! **"));
        player.sendMessage("");

        target.sendMessage("");
        target.sendMessage(CC.translate("&a&l*** " + player.getName() + "&a&l's &2&l&nMUTATED&a&l WORLD DESTROYER PET [&f" + cageDuration + "s&a&l] - CAGE! **"));
        target.sendMessage("");
        DamageIndicatorUtils.sendDamageIndicator(target, CC.translate("&a&l* &2&l&nMUTATED&a&l WORLD DESTROYER [&77s&a&l] *"), "RKO");

        CrankedAmuletUtil.tryHatchAbility(target, player);


        int cageCooldown = 30;
        //CosmicSkyInventoryPets.get().getCooldownUtils().add(target, "cageCooldown", cageCooldown * 1000);

        petItem.setLastUsed(System.currentTimeMillis());
        cagePlayer(player, target);


        Player finalTarget1 = target;
        Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), () -> {
            try {
                player.removeMetadata("caged", CosmicSkyInventoryPets.get());
                finalTarget1.removeMetadata("caged", CosmicSkyInventoryPets.get());
            } catch (Exception ignored) {
            }

        }, (long) this.cageDuration * 20L);

        return true;
    }

    public ArrayList<Player> getPlayersInRadius(Player player, int radius) {
        ArrayList<Player> list = new ArrayList<>();
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (!(entity instanceof Player))
                continue;
            Player p = (Player) entity;
            if (p.hasMetadata("spectator"))
                continue;
            list.add(p);
        }
        return list;
    }


    private static List<Location> setInside(Location baseLocation, Material material) {
        List<Location> locs = new ArrayList<>(Arrays.asList(baseLocation, baseLocation

                .clone().add(-1.0D, 0.0D, 0.0D), baseLocation
                .clone().add(0.0D, 0.0D, 1.0D), baseLocation
                .clone().add(-1.0D, 0.0D, 1.0D)));
        return setWithLocations(material, locs);
    }

    private static List<Location> setOutside(Location baseLocation, Material material) {
        List<Location> locs = new ArrayList<>(Arrays.asList(baseLocation
                        .clone().add(0.0D, 0.0D, -1.0D), baseLocation
                        .clone().add(1.0D, 0.0D, 0.0D), baseLocation
                        .clone().add(1.0D, 0.0D, -1.0D), baseLocation
                        .clone().add(1.0D, 0.0D, 1.0D), baseLocation
                        .clone().add(1.0D, 0.0D, 2.0D), baseLocation
                        .clone().add(0.0D, 0.0D, 2.0D), baseLocation
                        .clone().add(-1.0D, 0.0D, 2.0D), baseLocation
                        .clone().add(-2.0D, 0.0D, 2.0D), baseLocation
                        .clone().add(-2.0D, 0.0D, 0.0D), baseLocation
                        .clone().add(-2.0D, 0.0D, 1.0D),
                baseLocation
                        .clone().add(-2.0D, 0.0D, -1.0D), baseLocation
                        .clone().add(-1.0D, 0.0D, -1.0D)));
        return setWithLocations(material, locs);
    }

    @NotNull
    private static List<Location> setWithLocations(Material material, List<Location> locs) {
        List<Location> filteredLocations = new ArrayList<>();
        for (Location loc : locs) {
            if (loc.getBlock().getType() == Material.AIR) {
                filteredLocations.add(loc);
                loc.getBlock().setType(material);
                loc.getBlock().setMetadata("cage", new FixedMetadataValue(CosmicSkyInventoryPets.get(), "cage"));
            }
        }
        return filteredLocations;
    }

    public static void cagePlayer(final Player player, final Player affected) {
        final List<Location> list = new ArrayList<Location>();
        final List<Location> toPlace = new ArrayList<Location>();
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        final Block block = affected.getLocation().getBlock();
        final Block current = null;
        final Location baseLocation = affected.getLocation();
        list.addAll(setInside(baseLocation.clone().add(0.0, -1.0, 0.0), Material.EMERALD_BLOCK));
        list.addAll(setInside(baseLocation.clone().add(0.0, 3.0, 0.0), Material.EMERALD_BLOCK));
        list.addAll(setOutside(baseLocation.clone().add(0.0, -1.0, 0.0), Material.EMERALD_BLOCK));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 3.0, 0.0), Material.EMERALD_BLOCK));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 0.0, 0.0), Material.IRON_FENCE));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 1.0, 0.0), Material.IRON_FENCE));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 2.0, 0.0), Material.IRON_FENCE));
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0f, 1.4f);
        InventoryWorldDestroyerPet.locations.addAll(list);
        scheduler.scheduleSyncDelayedTask(CosmicSkyInventoryPets.get(), (Runnable) new BukkitRunnable() {
            int count = 0;

            public void run() {
                if (this.count >= 6) {
                    InventoryWorldDestroyerPet.removeBlocks(list);
                    this.cancel();
                }
                if (!affected.isDead() && WorldGuardUtils.isPvPEnabled(affected.getLocation())) {
                    final int dmg = 3;


                    AstroDamageHandler.getInstance().getDamageHandler().dealDamage(affected, dmg, "&e&lCage Ticks");

                }
                if (affected.getHealth() <= 0.0 || affected.isDead()) {
                    InventoryWorldDestroyerPet.removeBlocks(list);
                    this.cancel();
                }

                ++this.count;
            }
        }.runTaskTimer(CosmicSkyInventoryPets.get(), 20L, 20L));
    }

    public static String stringLocation(Location loc) {
        int x = (int) loc.getX();
        int y = (int) loc.getY();
        int z = (int) loc.getZ();
        String world = loc.getWorld().getName();
        String result = world + " " + x + " " + y + " " + z;
        return result;
    }

    public static Location stringLocationConvert(String str) {
        String[] list = str.split(" ");
        World world = Bukkit.getWorld(list[0]);
        int x = Integer.valueOf(list[1]).intValue();
        int y = Integer.valueOf(list[2]).intValue();
        int z = Integer.valueOf(list[3]).intValue();
        return new Location(world, x, y, z);
    }

    public static Block[] getBlockList(List<String> list) {
        Block[] blocks = new Block[list.size()];
        for (int i = 0; i < list.size(); i++)
            blocks[i] = stringLocationConvert(list.get(i)).getBlock();
        return blocks;
    }

    public static void removeBlocks(List<Location> list) {
        for (int i = 0; i < list.size(); i++) {
            Location location = list.get(i);
            Block block = location.getBlock();
            if (block != null) {
                block.setType(Material.AIR);
                locations.remove(location);
            }
        }
    }

    public static ArrayList<Location> getLocations() {
        return locations;
    }

    public boolean onTickInHotBar(Player player, int inventorySlot, InventoryPetItem item) {
        return false;
    }

}
