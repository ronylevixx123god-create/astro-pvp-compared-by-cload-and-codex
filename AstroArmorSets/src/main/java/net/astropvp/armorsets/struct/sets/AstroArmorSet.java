package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.utils.config.ConfigManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class AstroArmorSet extends ArmorSet {
    public List<Block> blockList = new ArrayList<Block>();


    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&a&lAstro Cap")).addLoreLines(CC.translate("&aGodly astro helmet.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&a&lAstro Torso")).addLoreLines(CC.translate("&aGodly astro chestplate.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&a&lAstro Pants")).addLoreLines(CC.translate("&aGodly astro leggings.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&a&lAstro Sneakers")).addLoreLines(CC.translate("&aGodly astro boots.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }


    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 3 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("astroSetDamageDecrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * amount *  0.01)));

    }


    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 20 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("astroSetDamageDecrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent  *  0.01)));

    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 30 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("astroSetDamageIncrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent  * 0.01)));
        if (Math.random() < 0.04) {
            cageEnemy(attacker, attacked, event);
        }
    }

    public void cageEnemy(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation()))
            return;

        if (attacked.hasMetadata("caged")) return;
        if (attacker.hasMetadata("caged")) return;


        if (!CannonballAmulet.active.contains(event.getEntity().getUniqueId())) {


            if (event.getEntity() instanceof Player && event.getEntity().getLocation().distance(attacker.getLocation()) <= 2.0 && ((Player) event.getEntity()).getHealth() < attacker.getHealth()) {
                if (attacker.hasMetadata("FalloutAbility") && System.currentTimeMillis() - attacker.getMetadata("FalloutAbility").get(0).asLong() <= 120000L) {
                    return;
                }
                if (event.getDamager().getWorld().getName().equals("world_duels")) {
                    return;
                }
                attacker.setMetadata("FalloutAbility", new FixedMetadataValue(AstroArmorSets.getInstance(), System.currentTimeMillis()));
                FPlayer fp = FPlayers.getInstance().getByPlayer(attacker);
                for (Entity nearbyEnt : attacker.getNearbyEntities(32.0, 64.0, 32.0)) {
                    if (nearbyEnt instanceof Player) {
                        Player pNear = (Player) nearbyEnt;
                        FPlayer fpNear = FPlayers.getInstance().getByPlayer(pNear);
                        if (fpNear.getRelationTo(fp).isAtLeast(Relation.TRUCE) || !WorldGuardUtils.isPvPEnabled(pNear.getLocation()))
                            continue;
                        pNear.sendMessage(CC.translate("&a&l** &aAstro Cage Ability &7[" + fp.getName() + "] &a&l**"));
                    }
                    attacker.getLocation().getWorld().playSound(attacker.getLocation(), Sound.GHAST_SCREAM, 1.0f, 1.0f);
                    this.activateFalloutKillerAbility(attacker, (Player) event.getEntity());
                }
            }
        }
    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 7 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("astroSetDamageIncrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));
        if (Math.random() < 0.025) {
            cageEnemyCrystal(attacker, attacked, event);
        }
    }

    private void cageEnemyCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        if (!CannonballAmulet.active.contains(event.getEntity().getUniqueId())) {

            if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation()))
                return;

            if (attacked.hasMetadata("caged")) return;
            if (attacker.hasMetadata("caged")) return;

            if (event.getEntity() instanceof Player && event.getEntity().getLocation().distance(attacker.getLocation()) <= 2.0 && ((Player) event.getEntity()).getHealth() < attacker.getHealth()) {
                if (attacker.hasMetadata("FalloutAbility") && System.currentTimeMillis() - attacker.getMetadata("FalloutAbility").get(0).asLong() <= 120000L) {
                    return;
                }
                if (event.getDamager().getWorld().getName().equals("world_duels")) {
                    return;
                }
                attacker.setMetadata("FalloutAbility", new FixedMetadataValue(AstroArmorSets.getInstance(), System.currentTimeMillis()));
                FPlayer fp = FPlayers.getInstance().getByPlayer(attacker);
                for (Entity nearbyEnt : attacker.getNearbyEntities(32.0, 64.0, 32.0)) {
                    if (nearbyEnt instanceof Player) {
                        Player pNear = (Player) nearbyEnt;
                        FPlayer fpNear = FPlayers.getInstance().getByPlayer(pNear);
                        if (fpNear.getRelationTo(fp).isAtLeast(Relation.TRUCE) || !WorldGuardUtils.isPvPEnabled(pNear.getLocation()))
                            continue;
                        pNear.sendMessage(CC.translate("&a&l** Astro Cage Ability &7[" + fp.getName() + "] &a&l**"));
                    }
                    attacker.getLocation().getWorld().playSound(attacker.getLocation(), Sound.GHAST_SCREAM, 1.0f, 1.0f);
                    this.activateFalloutKillerAbility(attacker, (Player) event.getEntity());
                }
            }
        }
    }

    @Override
    public int getLadderTier() {
        return 2;
    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&a&lASTRO SET BONUS"));
        lore.add(CC.translate("&8● &aEnemies deal 10% less damage to you."));
        lore.add(CC.translate("&8● &aDeal +30% damage to all enemies."));
        lore.add(CC.translate("&8● &a+15% Monopoly Effect Chance."));
        lore.add(CC.translate("&8● &aAstro Cage Ability."));
        lore.add(CC.translate("&7(&oRequires all 4 astro items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.Green + "+7% Outgoing Damage");
        message.add(CC.Green + "-3% Incoming Damage");
        message.add(CC.Green + "10% Astro Cage Ability");
        message.add(CC.Green + "+5% Monopoly Effect Chance");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&a&lASTRO SET BONUS"));
        message.add(CC.translate("&8● &aEnemies deal 10% less damage to you."));
        message.add(CC.translate("&8● &aDeal +30% damage to all enemies."));
        message.add(CC.translate("&8● &a+15% Monopoly Effect Chance."));
        message.add(CC.translate("&8● &aAstro Cage Ability."));
        message.add("");
        return message;
    }

    public List<Block> setOutlinedBlocks(Location location, Material material) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(this.setBlock(location.clone().add(2.0, 0.0, 0.0), material));
        blocks.add(this.setBlock(location.clone().add(-2.0, 0.0, 0.0), material));
        blocks.add(this.setBlock(location.clone().add(0.0, 0.0, 2.0), material));
        blocks.add(this.setBlock(location.clone().add(0.0, 0.0, -2.0), material));
        blocks.add(this.setBlock(location.clone().add(2.0, 0.0, 2.0), material));
        blocks.add(this.setBlock(location.clone().add(-2.0, 0.0, 2.0), material));
        blocks.add(this.setBlock(location.clone().add(2.0, 0.0, -2.0), material));
        blocks.add(this.setBlock(location.clone().add(-2.0, 0.0, -2.0), material));
        blocks.add(this.setBlock(location.clone().add(-2.0, 0.0, -1.0), material));
        blocks.add(this.setBlock(location.clone().add(1.0, 0.0, 2.0), material));
        blocks.add(this.setBlock(location.clone().add(-1.0, 0.0, 2.0), material));
        blocks.add(this.setBlock(location.clone().add(2.0, 0.0, 1.0), material));
        blocks.add(this.setBlock(location.clone().add(2.0, 0.0, -1.0), material));
        blocks.add(this.setBlock(location.clone().add(-1.0, 0.0, -2.0), material));
        blocks.add(this.setBlock(location.clone().add(1.0, 0.0, -2.0), material));
        blocks.add(this.setBlock(location.clone().add(-2.0, 0.0, 1.0), material));
        blocks.add(this.setBlock(location.clone().add(-1.0, 0.0, -2.0), material));
        blocks.add(this.setBlock(location.clone().add(1.0, 0.0, -2.0), material));
        return blocks;
    }

    public List<Block> setInsideBlocks(Location location, Material material) {
        ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.add(this.setBlock(location, material));
        blocks.add(this.setBlock(location.clone().add(1.0, 0.0, 0.0), material));
        blocks.add(this.setBlock(location.clone().add(-1.0, 0.0, 0.0), material));
        blocks.add(this.setBlock(location.clone().add(0.0, 0.0, 1.0), material));
        blocks.add(this.setBlock(location.clone().add(0.0, 0.0, -1.0), material));
        blocks.add(this.setBlock(location.clone().add(1.0, 0.0, 1.0), material));
        blocks.add(this.setBlock(location.clone().add(-1.0, 0.0, 1.0), material));
        blocks.add(this.setBlock(location.clone().add(-1.0, 0.0, -1.0), material));
        blocks.add(this.setBlock(location.clone().add(1.0, 0.0, -1.0), material));
        return blocks;
    }

    public void activateFalloutKillerAbility(Player player, final Player target) {
        final ArrayList<Block> blocks = new ArrayList<Block>();
        blocks.addAll(this.setOutlinedBlocks(player.getLocation().add(0.0, -1.0, 0.0), Material.DIAMOND_BLOCK));
        blocks.addAll(this.setInsideBlocks(player.getLocation().add(0.0, -1.0, 0.0), Material.SOUL_SAND));
        blocks.addAll(this.setOutlinedBlocks(player.getLocation().clone().add(0.0, 0.0, 0.0), Material.IRON_FENCE));
        blocks.addAll(this.setOutlinedBlocks(player.getLocation().clone().add(0.0, 1.0, 0.0), Material.IRON_FENCE));
        blocks.addAll(this.setOutlinedBlocks(player.getLocation().clone().add(0.0, 2.0, 0.0), Material.IRON_FENCE));
        blocks.addAll(this.setOutlinedBlocks(player.getLocation().clone().add(0.0, 3.0, 0.0), Material.DIAMOND_BLOCK));
        blocks.addAll(this.setInsideBlocks(player.getLocation().clone().add(0.0, 3.0, 0.0), Material.DIAMOND_BLOCK));
        blockList.addAll(blocks);
        new BukkitRunnable() {
            int counter = 0;

            public void run() {
                ++this.counter;
                target.damage(3.5);
                if (this.counter == 6) {
                    List<Block> newBlocks = blocks.stream().filter(Objects::nonNull).collect(Collectors.toList());
                    for (Block block : newBlocks) {
                        block.setType(Material.AIR);
                    }
                    blockList.removeAll(newBlocks);
                    this.cancel();
                }
            }
        }.runTaskTimer(AstroArmorSets.getInstance(), 0L, 20L);
    }

    public Block setBlock(Location location, Material material) {
        if (location.getBlock().getType() == Material.AIR) {
            location.getBlock().setType(material);
            return location.getBlock();
        }
        return null;
    }


}
