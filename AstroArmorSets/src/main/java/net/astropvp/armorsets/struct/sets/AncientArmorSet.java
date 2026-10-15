package net.astropvp.armorsets.struct.sets;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;

import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import net.astropvp.armorsets.AstroArmorSets;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.utils.config.ConfigManager;
import net.astropvp.damagehandler.AstroDamageHandler;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.enchantments.axes.Blessed;
import org.arkhamnetwork.enchantments.listeners.AutoBlessListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class AncientArmorSet extends ArmorSet {
    public ItemStack getHelmet() {
        return (new ItemBuilder(Material.DIAMOND_HELMET)).setName(CC.translate("&3&lAncient Cap")).addLoreLines(CC.translate("&3Powerful martine helmet.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getChestplate() {
        return (new ItemBuilder(Material.DIAMOND_CHESTPLATE)).setName(CC.translate("&3&lAncient Body")).addLoreLines(CC.translate("&3Powerful martine chestplate.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getLeggings() {
        return (new ItemBuilder(Material.DIAMOND_LEGGINGS)).setName(CC.translate("&3&lAncient Legs")).addLoreLines(CC.translate("&3Powerful martine leggings.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    public ItemStack getBoots() {
        return (new ItemBuilder(Material.DIAMOND_BOOTS)).setName(CC.translate("&3&lAncient Boots")).addLoreLines(CC.translate("&3Powerful martine boots.")).addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 4).addEnchantment(Enchantment.DURABILITY, 3).build();
    }

    @Override
    public int getLadderTier() {
        return 1;
    }


    public void onDamagedCrystal(Entity attacker, Player attacked, EntityDamageByEntityEvent event, int amount) {
        double reductionPercent = 3 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ancientSetDamageDecrease")  *  0.01));

        if ((AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.EASTER_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped((Player) attacker, SkinType.EASTER_2022_MUTATED_SWORD)) && Math.random() < 0.15) {
            amount--;
            attacker.sendMessage(CC.translate("&e&l** &eQueens Verdict &7Blocked an &6Ancient Crystal &7of " + attacked.getName() + "'s Set &e&l**"));
        }
        event.setDamage(event.getDamage() * (1 - (reductionPercent * amount) * 0.01));
    }


    public void onDamaged(Entity attacker, Player attacked, EntityDamageByEntityEvent event) {
        double reductionPercent = 15 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ancientSetDamageDecrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 - (reductionPercent * 0.01)));
        attacked.setVelocity(attacked.getVelocity().multiply(0.8));

    }

    public void onAttack(Player attacker, Entity attacked, EntityDamageByEntityEvent event) {
        double increasePercent = 30 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ancientSetDamageIncrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent * 0.01)));
        if (Math.random() < 0.01 && attacked instanceof Player) {
            if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation()))
                return;
            startAncientEffect(attacker, (Player) attacked);
        }

    }

    public void onAttackCrystal(Player attacker, Entity attacked, EntityDamageByEntityEvent event, int amount) {
        double increasePercent = 7 * (1 + (AstroArmorSets.getInstance().getConfigManager().getConfig(ConfigManager.ConfigFile.SETTINGS).getInt("ancientSetDamageIncrease")  *  0.01));
        event.setDamage(event.getDamage() * (1 + (increasePercent * amount * 0.01)));
        if (Math.random() < 0.01 && attacked instanceof Player) {
            if (!WorldGuardUtils.isPvPEnabled(attacker.getLocation()) || !WorldGuardUtils.isPvPEnabled(attacked.getLocation()))
                return;

            startAncientEffect(attacker, (Player) attacked);
        }

    }

    public List<String> getArmorLore() {
        List<String> lore = new ArrayList<>();
        lore.add(CC.translate("&3&lANCIENT SET BONUS"));
        lore.add(CC.translate("&8● &3Enemies deal 15% less damage to you."));
        lore.add(CC.translate("&8● &3Deal +35% damage to all enemies."));
        lore.add(CC.translate("&8● &3+10% Damage from Outgoing Soul Enchants."));
        lore.add(CC.translate("&8● &3-50% Knockback."));
        lore.add(CC.translate("&8● &3Auto Bless."));
        lore.add(CC.translate("&8● &3Leviathan's Curse Ability."));
        lore.add(CC.translate("&7(&oRequires all 4 ancient items.&7)"));
        return lore;
    }

    public List<String> getArmorCrystalLore() {
        List<String> message = new ArrayList<>();
        message.add(CC.DarkAqua + "+7% Outgoing Damage");
        message.add(CC.DarkAqua + "-3% Incoming Damage");
        message.add(CC.DarkAqua + "10% Leviathan's Curse Ability");
        return message;
    }

    public List<String> getArmorEquipMessage() {
        List<String> message = new ArrayList<>();
        message.add("");
        message.add(CC.translate("&3&lANCIENT SET BONUS"));
        message.add(CC.translate("&8● &3Enemies deal 15% less damage to you."));
        message.add(CC.translate("&8● &3Deal +30% damage to all enemies."));
        message.add(CC.translate("&8● &3+10% Damage from Outgoing Soul Enchants."));
        message.add(CC.translate("&8● &3-50% Knockback."));
        message.add(CC.translate("&8● &3Auto Bless."));
        message.add(CC.translate("&8● &3Leviathan's Curse Ability."));
        message.add("");
        return message;
    }

    private void startAncientEffect(final Player player, final Player pTarget) {

        if (pTarget.hasMetadata("banshee_scrolls") || pTarget.hasMetadata("bloodHoundNoBless") || player.hasMetadata("banshee_active")) {
            return;
        }

        if (CannonballAmulet.active.contains(pTarget.getUniqueId())) {
            return;
        }

        this.focusTargetPlayer(player, pTarget);
        player.sendMessage("");
        player.sendMessage(CC.translate(("&3&l      * LEVIATHAN'S CURSE &7(" + pTarget.getName() + ")&3&l *")));
        player.sendMessage(CC.translate(("&3&l      * &7+150% damage to &f" + pTarget.getName() + "&7 for 10s")));
        player.sendMessage("");
        pTarget.setMetadata("noDefenseProcs", new FixedMetadataValue(Enchantments.getInstance(), true));
        pTarget.setMetadata("bloodHoundNoBless", new FixedMetadataValue(CosmicSkyInventoryPets.get(), "ancient"));
        pTarget.removePotionEffect(PotionEffectType.SLOW);
        pTarget.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 200, 0));
        pTarget.sendMessage("");
        pTarget.sendMessage(CC.translate(("&3&l* LEVIATHAN'S CURSE &7(" + player.getName() + "'s Ancient Armor Set) &3&l*")));
        pTarget.sendMessage(CC.translate(("&3&l   * &7+150% damage from " + player.getName() + " for 10s")));
        pTarget.sendMessage(CC.translate("&3&l      * &710s Silence and Slowness I"));
        pTarget.sendMessage(CC.translate("&3&l        * &730s visible on /near"));
        pTarget.sendMessage("");
        player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 10.0f, 10.0f);
        Bukkit.getScheduler().runTaskLaterAsynchronously(CosmicSkyInventoryPets.get(), () -> {
            pTarget.removeMetadata("noDefenseProcs", Enchantments.getInstance());
            pTarget.removeMetadata("bloodHoundNoBless", CosmicSkyInventoryPets.get());
            //CmdUnfocus.unfocusPlayer(FPlayers.i.get(player).getFaction(), pTarget);
        }, 100L);

    }

    private void focusTargetPlayer(Player player, Player pTarget) {
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction myFaction = fPlayer.getFaction();
        FPlayer targetPlayer = FPlayers.getInstance().getByPlayer(pTarget);
        //CmdFocus.focusPlayer(fPlayer, myFaction, targetPlayer, false);

    }

    public void onEquip(Player player) {
        Blessed.blessPlayer(player);
        AutoBlessListener.blessedPlayers.add(player.getUniqueId());
    }

    public void onUnequip(Player player) {
        AutoBlessListener.blessedPlayers.remove(player.getUniqueId());

    }

}
