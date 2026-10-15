package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.avaje.ebean.validation.NotNull;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.helmets.UndertowHelmet;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
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
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class InventoryWorldDestroyerPet extends InventoryPet {
    public static ArrayList<Location> locations = new ArrayList<>();

    public InventoryPets getOwner() {
        return InventoryPets.WORLD_DESTROYER;
    }

    public List<String> getPetDescription() {
        return Lists.newArrayList(CC.Gray + "A fragment of the legendary",
                CC.Gray + "World Destroyer, Yijki.", String.valueOf(CC.Gray),
                CC.WhiteB + "Ability",
                CC.Gray + "Summon the power of Yijki's",
                CC.Gray + "[" + CC.DarkRed + "Dimensional Cage" + CC.Gray + "] ability (" + this.cageDuration + "s)",
                String.valueOf(CC.Gray), CC.WhiteB + "Cooldown",
                CC.Gray + " " + TimeUtils.formatSeconds(getCooldown()));
    }

    public double cageDuration = 5.0;


    public int getCooldown() {
        return 300;
    }

    public int getExpForLevel() {
        return 1700 * 5;
    }

    public int getMaxLevel() {
        return 5;
    }



    public boolean onPetItemInteract(Player player, ItemStack item, InventoryPetItem petItem, PlayerInteractEvent event) throws PetException {
        //player.sendMessage(CC.translate("&c&lPET: &cThis pet has been disabled"));
        //player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0f, 1.0f);
        //if(1==1) return false;
        long lastUse = petItem.getLastUsed();
        long delay = TimeUnit.SECONDS.toMillis(this.getCooldown());
        long timeTillCanUse = delay - (System.currentTimeMillis() - lastUse);

        if (lastUse > 0L && timeTillCanUse > 0L && (!player.isOp() || !player.isSneaking()))
            throw new NonPrefixedPetException(CC.RedB + "PET: " + CC.Red + "Ability on cooldown: " + CC.RedU + TimeUtils.formatSeconds((int) (timeTillCanUse / 1000L)));
        if (CooldownUtils.isCooldownActive(player, petItem.getPetType().name())) {
            player.sendMessage(CC.translate("&c&lPET: &cYou are still on" + petItem.getPetType().getPetName() + " &cfor another &7" + CooldownUtils.getFormattedCooldown(player, petItem.getPetType().name() + "&c.")));
            return true;
        }

        if (player.hasMetadata("in_duel"))
            return true;
        if (player.hasMetadata("spectator"))
            return true;
        Faction faction = FactionUtils.getFaction(player);
        Location locationClicked = player.getLocation();
        Player target = null;
        for (Entity other : player.getNearbyEntities(6.0D, 6.0D, 6.0D)) {
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
            cageDuration = 7.5;
        } else if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.VALENTINES_2022_MUTATED_BOOTS)) {
            cageDuration = 6.5;

        } else {
            cageDuration = 5.0;
        }


        if (WorldGuardUtils.isPvPDisabled(locationClicked))
            throw new PrefixedPetException("Not allowed in this region!");
        if (player.hasMetadata("inDungeonParkour"))
            throw new PrefixedPetException("You cannot use this pet while in the dungeon parkour.");
        if (target == null || (event != null && target.getLocation().distance(player.getLocation()) > 4.0D))
            throw new PrefixedPetException("Could not find a valid target!");
        /*if (CageCooldown.isOnCageCooldown(target))
            throw new PrefixedPetException(target.getName() + " is still on Cage Cooldown! This lasts 20 seconds.");*/
        if (CooldownUtils.isCooldownActive(target, "cageCooldown")) {
            throw new PrefixedPetException("&c&lPET: &c" + target.getName() + "&c is still on cage cooldown for &n" + CooldownUtils.getFormattedCooldown(target, "cageCooldown") + "&c.");
        }
        if (AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.SEXY_HELMET) && Math.random() < 0.5D) {
            //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
            CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), 10));
            player.sendMessage(CC.translate("&d&l* CHAD SUNGLASSES BLOCKED &7[&c" + target.getName() + "&7] &d&l*"));
            target.sendMessage(CC.translate("&d&l* CHAD SUNGLASSES &7[&cBlocked World Destroyer&7] &d&l*"));
            return true;
        }

        if (UndertowHelmet.undertowField.contains(target)) {
            //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
            CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), 10));
            target.sendMessage(CC.translate("&a&l* UNDERTOW HELMET &7Blocked &4World Destroyer Pet &7[&f" + player.getName() + "&7] &a&l*"));
            player.sendMessage(CC.translate("&a&l* &f" + target.getName() + " &7Blocked &4World Destroyer Pet &7[&a&lUndertow Helmet&7] &a&l*"));
            return true;
        }
        if (target.getInventory().getChestplate() != null && AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.BACK_TO_SCHOOL_2021_AMULET)) {
            //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
            CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), 10));
            player.sendMessage(CC.translate("&8&l* WATCHMAN AMULET &7[&c" + target.getName() + "&7] &8&l*"));
            target.sendMessage(CC.translate("&8&l* WATCHMAN AMULET &7[&cBlocked World Destroyer&7] &8&l*"));
            return true;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.THANKSGIVING_2022_BELT)) {
            //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
            CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), 10));
            player.sendMessage(CC.translate("&e&l* MATRIX MASTER BELT &7[&c" + target.getName() + "&7] &e&l*"));
            target.sendMessage(CC.translate("&e&l* MATRIX MASTER BELT &7[&cBlocked World Destroyer&7] &e&l*"));
            return true;
        }


        if (AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.BACK_TO_SCHOOL_2022_HELMET)) {
            //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
            CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), 10));
            player.sendMessage(CC.translate("&2&l* LAMP SHADE &7[&c" + target.getName() + "&7] &2&l*"));
            target.sendMessage(CC.translate("&2&l* LAMP SHADE &7[&cBlocked World Destroyer&7] &2&l*"));
            return true;
        }

        if (target.getInventory().getBoots() != null && AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.HALLOWEEN_2021_BOOTS)) {

            if ((AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.EASTER_2022_MUTATED_BELT) || AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.EASTER_2022_BELT)) && Math.random() < 0.5) {
                target.sendMessage(CC.translate("&e&l* " + player.getName() + "'s Mischievous Tail Bypassed &7Bloodstained Galoshes &e&l*"));
            } else {

                //CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));
                CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), 10));
                player.sendMessage(CC.translate("&8&l* BLOODSTAINED GALOSHES &7[&c" + target.getName() + "&7] &8&l*"));
                target.sendMessage(CC.translate("&8&l* BLOODSTAINED GALOSHES &7[&cBlocked World Destroyer&7] &8&l*"));
                return true;
            }
        }




/*        if (CosmicSkyInventoryPets.get().getCooldownUtils().isOnCooldown(target, "cageCooldown")) {
            player.sendMessage(CC.translate("&c&lPET: &c" + target.getName() + "&c is still on cage cooldown for &n" + CosmicSkyInventoryPets.get().getCooldownUtils().getRemainingTimeFormatted(target, "cageCooldown") + "&c."));
            return false;
        }


        */

        CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), 10));
        petItem.setLastUsed(System.currentTimeMillis());

        if (AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.BACK_TO_SCHOOL_2022_BOOTS) && Math.random() < 0.3) {
            cagePlayer(target, player, cageDuration);
            player.sendMessage(CC.translate("&2&l* " + target.getName() + "'s CROCS GATORS &7[Cage Reflect] &2&l*"));
            target.sendMessage(CC.translate("&2&l* CROCS GATORS &7[Cage Reflect] &2&l*"));
        } else {
            cagePlayer(player, target, cageDuration);
        }


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

    public static void cagePlayer(final Player player, final Player target, double cageDuration) {
        CrankedAmuletUtil.tryHatchAbility(target, player);
        int cageCooldown = 60;
        CooldownUtils.registerCooldown(target, new PetCooldown("cageCooldown", cageCooldown));

        //CosmicSkyInventoryPets.get().getCooldownUtils().add(target, "cageCooldown", cageCooldown * 1000);
        player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
        player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);

        target.setMetadata("caged", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));
        player.setMetadata("caged", new FixedMetadataValue(CosmicSkyInventoryPets.get(), true));


        player.sendMessage("");
        player.sendMessage(CC.translate("&a&l*** WORLD DESTROYER PET [&f" + target.getName() + "&a&l] - CAGED! **"));
        player.sendMessage("");

        target.sendMessage("");
        target.sendMessage(CC.translate("&a&l*** " + player.getName() + "&a&l's WORLD DESTROYER PET [&f" + cageDuration + "s&a&l] - CAGE! **"));
        target.sendMessage("");
        DamageIndicatorUtils.sendDamageIndicator(target, CC.translate("&a&l* WORLD DESTROYER [&77s&a&l] *"), "RKO");
        final List<Location> list = new ArrayList<Location>();
        final List<Location> toPlace = new ArrayList<Location>();
        final BukkitScheduler scheduler = Bukkit.getScheduler();
        final Block block = target.getLocation().getBlock();
        final Block current = null;
        final Location baseLocation = target.getLocation();
        list.addAll(setInside(baseLocation.clone().add(0.0, -1.0, 0.0), Material.OBSIDIAN));
        list.addAll(setInside(baseLocation.clone().add(0.0, 3.0, 0.0), Material.OBSIDIAN));
        list.addAll(setOutside(baseLocation.clone().add(0.0, -1.0, 0.0), Material.OBSIDIAN));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 3.0, 0.0), Material.OBSIDIAN));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 0.0, 0.0), Material.IRON_FENCE));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 1.0, 0.0), Material.IRON_FENCE));
        list.addAll(setOutside(baseLocation.clone().add(0.0, 2.0, 0.0), Material.IRON_FENCE));
        player.playSound(player.getLocation(), Sound.ANVIL_LAND, 1.0f, 1.4f);
        InventoryWorldDestroyerPet.locations.addAll(list);
        List<Player> pl = baseLocation.getWorld().getNearbyEntities(baseLocation, 4, 4, 4)
                .stream()
                .filter(e -> e instanceof Player)
                .map(e -> (Player)e).collect(Collectors.toList());
        scheduler.scheduleSyncDelayedTask(CosmicSkyInventoryPets.get(), (Runnable) new BukkitRunnable() {
            int count = 0;

            public void run() {
                if (this.count >= 6) {
                    InventoryWorldDestroyerPet.removeBlocks(list);
                    this.cancel();
                }
                if (!target.isDead() && WorldGuardUtils.isPvPEnabled(target.getLocation()) && !(AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.VALENTINES_2022_AMULET))) {
                    final int dmg = 1;

                    if (AstroItemSkinsAPI.hasSkinEquipped(target, SkinType.VALENTINES_2022_MUTATED_AMULET)) {
                        AstroDamageHandler.getInstance().getDamageHandler().healPlayer(target, dmg, "&e&lReverted Cage Ticks");

                    } else {
                        AstroDamageHandler.getInstance().getDamageHandler().dealDamage(target, dmg, "&e&lCage Ticks");
                    }
                }
                if (target.getHealth() <= 0.0 || target.isDead()) {
                    InventoryWorldDestroyerPet.removeBlocks(list);
                    this.cancel();
                }
                /*
                pl.forEach(p -> {
                    if(p.getLocation().getBlock().getType() == Material.IRON_BARDING) {
                        p.teleport(p.getLocation().clone().add(0.5, 0.0, 0.5));
                    }
                    if(p.getEyeLocation().getBlock().getType() == Material.IRON_BARDING) {
                        p.teleport(p.getLocation().clone().add(0.5, 0.0, 0.5));
                    }

                    if(p.getLocation().getBlock().getType() == Material.OBSIDIAN) {
                        p.teleport(p.getLocation().clone().add(0.0, 1.0, 0.0));
                    }
                    if(p.getEyeLocation().getBlock().getType() == Material.OBSIDIAN) {
                        p.teleport(p.getLocation().clone().add(0.0, 1.0, 0.0));
                    }
                });

                 */
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
