package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Maps;
import com.massivecraft.factions.iface.RelationParticipator;
import com.massivecraft.factions.perms.Relation;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.ArmorSetWeapon;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EngineerArmorSet extends ArmorSet implements ArmorSetWeapon {

    public  HashMap<Entity, Player> blocksAndPlayers = new HashMap<>();

    public  HashMap<Entity, Player> blocksAndProccer = Maps.newHashMap();

    public  List<String> uuidsE = new ArrayList<>();

    public  List<String> entityList = new ArrayList<>();

    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&6&lEngineer Face Mask")).addLoreLines(CC.translate("&6Engineer face mask.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&6&lEngineer Explosion-Proof Torso")).addLoreLines(CC.translate("&6The mantel of engineers.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&6&lEngineer Protective Chaps")).addLoreLines(CC.translate("&6The fabled robes of engineers.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&6&lEngineer Steel Toed Boots")).addLoreLines(CC.translate("&6Light as feather, heavy as stone.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 0;
    }


    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 9;
        event.setDamage(event.getDamage() * (1-(reductionPercent*amount*0.01)));

        if (attacker instanceof Player) {
            if (ThreadLocalRandom.current().nextInt(100) <= 4) {
                if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation())) return;

                onEngineerAbility((Player) attacker);

            }
        }
    }


    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 35;
        event.setDamage(event.getDamage() * (1-(reductionPercent*0.01)));
        if (attacker instanceof Player) {
            if (ThreadLocalRandom.current().nextInt(100) <= 4) {
                if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation()))
                    return;

                onEngineerAbility((Player) attacker);
            }
        }

    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&6&lENGINEER SET BONUS"));
        lore.add(CC.translate("&8● &6Enemies deal 35% less damage to you."));
        lore.add(CC.translate("&8● &6Invisible on /near."));
        lore.add(CC.translate("&8● &6Immune to Teleblock Enchant."));
        lore.add(CC.translate("&8● &6Gears IV."));
        lore.add(CC.translate("&8● &6B-2 Bomber Passive Ability."));
        lore.add(CC.translate("&7(&oRequires all 4 engineer items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.Gold + "-9% Incoming Damage");
        message.add(CC.Gold + "20% B-2 Bomber Passive Ability");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&6&lENGINEER SET BONUS"));
        message.add(CC.translate("&8● &6Enemies deal 35% less damage to you."));
        message.add(CC.translate("&8● &6Invisible on /near."));
        message.add(CC.translate("&8● &6Immune to Teleblock Enchant."));
        message.add(CC.translate("&8● &6Gears IV."));
        message.add(CC.translate("&8● &6B-2 Bomber Passive Ability."));
        message.add("");
        return message;
    }

    public List<ItemStack> getWeapons() {
        List<ItemStack> weapons = new ArrayList<>();
        ItemStack sword = new NBTWrapper(new ItemBuilder(Material.DIAMOND_SWORD)
                .setName(CC.translate("&6&lEngineer Hammer"))
                .addLoreLines(CC.Gray + "A titan-forged grandmaster"
                        ,CC.Gray + "warhammer that crushes all."
                        , ""
                        , CC.GoldB + "ENGINEER WEAPON BONUS"
                        , CC.translate("&8● &6Deal +15% damage to all enemies.")
                        , CC.translate("&8● &6+20% B-2 Bomber Ability.")
                        , CC.translate("&8● &6Immune to World Destroyer Pet.")
                        , CC.translate("&8● &6Immune to Obliterate, Shockwave & Guardian Enchant.")
                        , CC.Gray + "(" + CC.GrayI + "Requires all 4 yijki items." + CC.Gray + ")")
                .addEnchantment(Enchantment.DAMAGE_ALL, 5).addEnchantment(Enchantment.DURABILITY, 3).build()).setString("cosmicType", "ENGINEER_SWORD").build();

        weapons.add(sword);
        return weapons;
    }

    public void onEngineerAbility(Player player) {

            List<Entity> affected = player.getNearbyEntities(25.0D, 25.0D, 25.0D).stream().filter(ent -> {
            if (!(ent instanceof Player))
                return false;
            Player p = (Player)ent;
            if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.VIRUS_HELMET_HAZMAT)) {
                p.sendMessage(CC.translate("&2&l* PRISON BREAK &7(Hazmat Helmet) &2&l*"));
                return false;
            }

                if (CannonballAmulet.active.contains(p.getUniqueId())) {
                    return false;
                }

                    if (p.hasMetadata("immune_freeze")) {
                p.sendMessage(CC.DarkGrayB + "* DRAGON SLAYER [" + ChatColor.GRAY + "B-2 Bomb blocked!" + CC.DarkGrayB + "] *");
                return false;
            }
            Relation relation = FactionUtils.getRelationTo((RelationParticipator)FactionUtils.getFaction(p), (RelationParticipator)FactionUtils.getFaction(player));
            return (relation != Relation.TRUCE && relation != Relation.MEMBER && relation != Relation.ALLY);
        }).collect(Collectors.toList());
        for (Entity ent2 : affected) {
            //FallingBlock block = ent2.getWorld().spawnFallingBlock(ent2.getLocation().add(0.0D, 10.0D, 0.0D), Material.TNT, (byte)9);
            //addEntityUUIDE(block.getUniqueId());
            //blocksAndPlayers.put(block, (Player)ent2);
            //blocksAndProccer.put(block, player);
        }
        player.sendMessage(CC.translate("&a&l* B-2 BOMBER DEPLOYED [&7" + affected.size() + " &7players&a&l] *"));
    }

    public void addEntityUUIDE(UUID id) {
        String uuid = id.toString();
        uuidsE.add(uuid);
    }


}
