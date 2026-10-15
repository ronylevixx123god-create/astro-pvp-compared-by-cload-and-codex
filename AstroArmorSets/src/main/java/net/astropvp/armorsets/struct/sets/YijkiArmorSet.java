package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.UpgradeUtils;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.ArmorSetWeapon;
import net.astropvp.armorsets.utils.LocationUtils;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.enchantments.armor.Phoenix;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class YijkiArmorSet extends ArmorSet implements ArmorSetWeapon {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&f&lYijki Mask")).addLoreLines(CC.translate("&fYijki face mask.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&f&lYijki Torso")).addLoreLines(CC.translate("&fThe forbidden mantel of yijki.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&f&lYijki Robeset")).addLoreLines(CC.translate("&fThe fabled robes of yijki.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&f&lYijki Footwraps")).addLoreLines(CC.translate("&fLight as feather, heavy as stone.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }


    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) throws Exception {
        double reductionPercent = 5;
        event.setDamage(event.getDamage() * (1 - (reductionPercent * amount * 0.01)));
        if (Math.random() < 0.0075 && attacker instanceof Player) {
            if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation()))
                return;

            activateYijki(attacker.getLocation(), attacked);
        }
    }

    @Override
    public int getLadderTier() {
        return 0;
    }


    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) throws Exception {
        double reductionPercent = 30;
        event.setDamage(event.getDamage() * (1-(reductionPercent*0.01)));
        if (Math.random() < 0.0125 && attacker instanceof Player) {
            if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation()))
                return;

            activateYijki(attacker.getLocation(), attacked);
        }

    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&f&lYIJKI SET BONUS"));
        lore.add(CC.translate("&8● &fEnemies deal 30% less damage to you."));
        lore.add(CC.translate("&8● &fRevenge of Yijki Passive Ability."));
        lore.add(CC.translate("&7(&oRequires all 4 yijki items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.White + "-5% Incoming Damage");
        message.add(CC.White + "20% Revenge of Yijki Ability");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&f&lYIJKI SET BONUS"));
        message.add(CC.translate("&8● &fEnemies deal 30% less damage to you."));
        message.add(CC.translate("&8● &fRevenge of Yijki Passive Ability."));
        message.add("");
        return message;
    }

    public List<ItemStack> getWeapons() {
        List<ItemStack> weapons = new ArrayList<>();
        ItemStack sword = new NBTWrapper(new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(CC.translate("&f&lYijki World Ender"))
                .addLoreLines(CC.Gray + "They laughed at you, they"
                        , CC.Gray + "An Absolute massive, corruped"
                        , CC.Gray + "executioner blade imbued with"
                        , CC.Gray + "highly destructive dark magic."
                        , ""
                        , CC.WhiteB + "YIJKI WEAPON BONUS"
                        , CC.translate("&8● &fDeal +20% damage to all enemies.")
                        , CC.translate("&8● &f+25% Revenge of Yijki Ability.")
                        , CC.Gray + "(" + CC.GrayI + "Requires all 4 yijki items." + CC.Gray + ")")
                .addEnchantment(Enchantment.DAMAGE_ALL, 5).addEnchantment(Enchantment.DURABILITY, 3).build()).setString("cosmicType", "YIJKI_SWORD").build();

        weapons.add(sword);
        return weapons;
    }

    public void activateYijki(Location center, Player player) throws Exception {
        ArrayList<Location> procLocations = new ArrayList<Location>();
        ArrayList<Player> nearbyPlayers = new ArrayList<Player>();
        FPlayer fp = FPlayers.getInstance().getByPlayer(player);
        for (int i = 0; i < 16; ++i) {
            Location l = LocationUtils.getNearbyLocation(center, 8, 2, 0);
            l.getWorld().playSound(l, Sound.WITHER_SPAWN, 1.0f, 0.4f);
            de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), l.clone(), 0f, 1f, 0f, 0.7f, 32);
            de.inventivegames.particle.ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), l.clone(), 0f, 0f, 0f, 0.7f, 32);
            procLocations.add(l);
        }
        for (Entity nearbyEnt : player.getNearbyEntities(32.0, 64.0, 32.0)) {
            if (!(nearbyEnt instanceof Player)) continue;
            Player pNear = (Player) nearbyEnt;
            FPlayer fpNear = FPlayers.getInstance().getByPlayer(pNear);
            if (AstroItemSkinsAPI.hasSkinEquipped(player, SkinType.CUPID_AMULET)) {
                player.sendMessage(CC.translate("&c&l* Amulet of Angles &7(Blocked Yijki) &c&l*"));
                continue;
            }

            if (CannonballAmulet.active.contains(nearbyEnt.getUniqueId())) {
                continue;
            }

            if (fpNear.getRelationTo(fp).isAtLeast(Relation.TRUCE) || !WorldGuardUtils.isPvPEnabled(pNear.getLocation()))
                continue;
            pNear.sendMessage(ChatColor.DARK_PURPLE + "" + ChatColor.BOLD + "** REVENGE OF YIJKI (" + ChatColor.RED + fp.getName() + " [1.5s]" + ChatColor.DARK_PURPLE + ChatColor.BOLD + ") **");
            nearbyPlayers.add(pNear);
        }
        Bukkit.getScheduler().runTaskLater(AstroArmorSets.getInstance(), () -> {
            for (Location l : procLocations) {
                l.getWorld().strikeLightningEffect(l);
                l.getWorld().playSound(l, Sound.WITHER_DEATH, 1.0f, 0.4f);
                try {
                    de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), l, 0f, 0.0f, 0f, 1.2f, 20);
                    de.inventivegames.particle.ParticleEffect.LARGE_EXPLODE.sendToPlayers(Bukkit.getOnlinePlayers(), l, 0f, 1.0f, 0f, 1.2f, 20);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                for (Player pNear : nearbyPlayers) {
                    FPlayer fpNear;
                    if (!(pNear.getLocation().distanceSquared(l) <= 2.0) || (fpNear = FPlayers.getInstance().getByPlayer(pNear)).getRelationTo(fp).isAtLeast(Relation.TRUCE) || !WorldGuardUtils.isPvPEnabled(pNear.getLocation()))
                        continue;
                    int damage = 8;
                    try {
                        if (Phoenix.hasPhoenixEquipped(pNear) && Phoenix.procPhoenix(pNear, player, damage)) continue;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    pNear.setHealth(Math.max(1.0, pNear.getHealth() - (double) damage));
                }
            }
        }, 20L);
    }


}
