package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;

import lombok.Getter;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;

import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.stream.Collectors;

public class TravelerArmorSet extends ArmorSet implements Listener {

    @Getter
    private final Map<UUID, TravelerFallingBlock> fallingBlocks;

    public TravelerArmorSet() {
        this.fallingBlocks = new HashMap<>();
    }


    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&5&lTraveler Hood")).addLoreLines(CC.translate("&5A hood that fades in and out.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&5&lTraveler Chestplate")).addLoreLines(CC.translate("&5A chestplate that fades in and out.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&5&lTraveler Robes")).addLoreLines(CC.translate("&5Robes that fades in and out.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&5&lTraveler Sandals")).addLoreLines(CC.translate("&5A sandal that fades in and out.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }


    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        if (attacker instanceof Player) {
            if (Math.random() < 0.0075) {
                onDimensionalRift((Player) attacked);
            }
        }
    }

    @Override
    public int getLadderTier() {
        return 0;
    }


    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        if (attacker instanceof Player) {
            if (Math.random() < 0.015) {
                onDimensionalRift((Player) attacked);
            }
        }
    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 30;
        event.setDamage(event.getDamage() * (1 + (increasePercent  * 0.01)));    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 7.5;
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&5&lTRAVELER SET BONUS"));
        lore.add(CC.translate("&8● &5Deal +30% damage to all enemies."));
        lore.add(CC.translate("&8● &5Dimensional Shift Passive Ability."));
        lore.add(CC.translate("&7(&oRequires all 4 traveler items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.DarkPurple + "+7.5% Outgoing Damage");
        message.add(CC.DarkPurple + "20% Dimensional Shift Ability");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&5&lTRAVELER SET BONUS"));
        message.add(CC.translate("&8● &5Deal +30% damage to all enemies."));
        message.add(CC.translate("&8● &5Dimensional Shift Passive Ability."));
        message.add("");
        return message;
    }


    public void onDimensionalRift(Player victim) {
        FPlayer damagedFPlayer = FPlayers.getInstance().getByPlayer(victim);
        for (Entity nearbyEnt : victim.getNearbyEntities(25.0D, 32.0D, 25.0D)) {
            if (nearbyEnt instanceof Player) {
                Player nearbyPlayer = (Player)nearbyEnt;
                FPlayer fpNear = FPlayers.getInstance().getByPlayer(nearbyPlayer);
                if ((fpNear.hasFaction() && fpNear.getRelationTo(damagedFPlayer).isAtLeast(Relation.TRUCE)) || !WorldGuardUtils.isPvPEnabled(damagedFPlayer.getLocation()))
                    continue;
                if (!WorldGuardUtils.isPvPEnabled(nearbyPlayer.getLocation()))
                    continue;
                if (nearbyPlayer.hasMetadata("NPC"))
                    continue;
                nearbyPlayer.playSound(nearbyPlayer.getLocation().add(0.0D, 4.0D, 0.0D), Sound.ENDERMAN_TELEPORT, 1.0F, 1.1F);
                nearbyPlayer.playSound(nearbyPlayer.getLocation().add(0.0D, 4.0D, 0.0D), Sound.ANVIL_LAND, 1.0F, 1.1F);
                nearbyPlayer.sendMessage(CC.DarkPurpleB + "** DIMENSIONAL SHIFT (" + CC.Red + fpNear.getRelationTo(damagedFPlayer).getColor() + victim.getName() + " [4s]" + CC.DarkPurpleB + ") **");

                this.spawnDimensionalBlocks(nearbyPlayer, victim);
            }
        }
    }



    public TravelerFallingBlock getTravelerBlock(UUID fallingBlockUUID) {
        return this.fallingBlocks.get(fallingBlockUUID);
    }

    public double getRandomDamage() {
        return (4 + (new Random()).nextInt(8));
    }

    private void spawnDimensionalBlocks(Player player, Player damaged) {
        Location center = player.getLocation().add(0.0D, 10.0D, 0.0D);
        int playerX = center.getBlockX(), playerZ = center.getBlockZ(), playerY = center.getBlockY();
        int minX = playerX - 3, minZ = playerZ - 3;
        int maxX = playerX + 3, maxZ = playerZ + 3;
        for (int x = minX; x <= maxX; x++) {
            for (int z = minZ; z <= maxZ; z++) {
                double locX = x + this.getRandomOffset(5.8D);
                double locZ = z + this.getRandomOffset(5.8D);
                Location location = new Location(center.getWorld(), locX, playerY, locZ);
                Material material = (new Random()).nextBoolean() ? Material.NETHERRACK : Material.ENDER_STONE;
                FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, material, (byte)0);
                fallingBlock.setVelocity(new Vector(0, -1, 0));
                fallingBlock.setHurtEntities(false);
                fallingBlock.setDropItem(false);
                fallingBlock.setMetadata("dimensionalShift", new FixedMetadataValue(AstroArmorSets.getInstance(), Boolean.TRUE));
                this.fallingBlocks.put(fallingBlock.getUniqueId(), new TravelerFallingBlock(damaged, fallingBlock.getUniqueId()));
            }
        }
    }

    public double getRandomOffset(double baseDouble) {
        Random r = new Random();
        return (r.nextFloat() * r.nextFloat()) * baseDouble;
    }

    public static class TravelerFallingBlock {
        private Player player;

        private UUID UUID;

        public TravelerFallingBlock(Player player, UUID blockUUID) {
            this.player = player;
            this.UUID = blockUUID;
        }

        public Player getPlayer() {
            return this.player;
        }

        public void setPlayer(Player player) {
            this.player = player;
        }

        public UUID getUUID() {
            return this.UUID;
        }

        public void setUUID(UUID uUID) {
            this.UUID = uUID;
        }
    }


}
