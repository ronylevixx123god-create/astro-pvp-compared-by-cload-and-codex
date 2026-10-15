package org.arkhamnetwork.enchantments.enchantments.swords;

import com.cosmicpvp.cosmicutils.nms.MinecraftServerUtils;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.WorldGuardUtils;
import com.rit.sucy.CustomEnchantment;
import de.inventivegames.particle.ParticleEffect;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.astropvp.astropvpskins.objects.skins.amulets.CannonballAmulet;
import me.astropvp.astropvpskins.objects.skins.axes.BoostedChainsaw;
import net.astropvp.core.AstroCore;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.cmds.CommandEnchDust;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class DivineImmolation extends CustomEnchantment {
    public DivineImmolation() {
        super("Divine Immolation", GeneralUtil.swords, 2);
        this.max = 4;
        this.base = 10.0;
        this.interval = 10.0;
        Bukkit.getPluginManager().registerEvents(new DivineImmolationListener(), Enchantments.getInstance());
    }

    public void applyEffect(final LivingEntity player, final LivingEntity playerHit, final int level, final EntityDamageByEntityEvent e) {
        if (!CommandEnchDust.soulEnabled) {
            return;
        }


        final Player p = (Player) player;
        double radius = level;
        double damage = Math.floor((double)level * 2.0);

        if (AstroItemSkinsAPI.hasSkinEquipped(p, SkinType.CHRISTMAS_SWORD_ORNAMENTAL)) {
            radius += 2.0;
        }
        if (player.hasMetadata("leviathanTrophySilence") && player.getMetadata("leviathanTrophySilence").get(0).asLong() > System.currentTimeMillis()) {
            return;
        }

        if (p.hasMetadata("noSoulProc")) {
            return;
        }

        if (p.hasMetadata("noDivine")) return;

        if (playerHit instanceof Player) {
            if (CannonballAmulet.affected.contains(playerHit)) {
                return;
            }
        }


        if (((BoostedChainsaw) SkinType.BACK_TO_SCHOOL_2021_AXE.getItemSkin()).getChainSawedPlayers().contains(p.getName())) {

            return;
        }


        if (playerHit instanceof Player) {
            if ((AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.EASTER_2022_MUTATED_SWORD)) && Math.random() < 0.2)
                return;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.DOOMSDAY_SWORD)) {
            damage *= 1.25;
        }



        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.EASTER_2022_SWORD) || AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.EASTER_2022_MUTATED_SWORD)) {
            damage *= 1.3;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.THANKSGIVING_SWORD_HARVESTER)) {
            damage *= 1.3;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.SUMMER_2021_AMULET)) {
            damage *= 1.25;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.CHRISTMAS_2022_AMULET_ACTIVE)) {
            damage*=1.15;
        }

        if (player.hasMetadata("carver_damage")) {
            damage *= 1.3;
        }
        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.SUMMER_2021_SWORD)) {
            damage *= 1.15;
        }

        if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.JULY_2021_BACKPACK) && player.getHealth() <= player.getMaxHealth() / 2 && !player.hasMetadata("noCape")) {
            damage *= 1.15;
        }

        if (player.hasMetadata("soulEnchantMultiplier")) {
            damage *= player.getMetadata("soulEnchantMultiplier").get(0).asDouble();
        }


        if (playerHit instanceof Player) {



            if (((Player) playerHit).getPlayer().getInventory().getChestplate() != null && (AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.THANKSGIVING_2021_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(((Player) playerHit).getPlayer(), SkinType.THANKSGIVING_2021_MUTATED_BACKPACK)) && Math.random() < 0.075) {

                return;
            }

            if ((AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2022_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2022_MUTATED_HELMET)) && playerHit.getHealth() < playerHit.getMaxHealth() / 4) {
                damage *= 0.85;
            }

            if (AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2021_HELMET) || AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.CHRISTMAS_2021_MUTATED_HELMET)) {
                damage *= 0.9;
            }

        }
        if (player.hasMetadata("witcher_negate") && player.getMetadata("witcher_negate").get(0).asLong() > System.currentTimeMillis()) {

            return;
        }
        if (p.hasMetadata("lastItemHeldChange") && MinecraftServerUtils.getCurrentServerTick() - p.getMetadata("lastItemHeldChange").get(0).asLong() <= 5L) {
            return;
        }

        if (Math.random() < 0.1 && AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.SUMMER_2021_AMULET)) {
            damage = Math.floor(level * 1.5);
        }
        if (KillTrackerAPI.hasGodMode(p) && KillTrackerAPI.getAllSouls(p) > 0) {
            if (!p.hasMetadata("lastSoulRemoveEvent") || System.currentTimeMillis() - p.getMetadata("lastSoulRemoveEvent").get(0).asLong() > 1000L) {
                KillTrackerAPI.removeSoulsFromGems(p, 20);
            }

            final List<Entity> ents = playerHit.getNearbyEntities(radius, radius, radius);
            if (playerHit instanceof Player) {
                ents.add(playerHit);
            }
            for (final Entity ent : ents) {
                if (ent instanceof Player) {
                    Player pEnt;
                    pEnt = (Player) ent;
                    if (p.getUniqueId() == pEnt.getUniqueId()) {
                        continue;
                    }
                    if (GeneralUtil.isAtleastTruce(p, pEnt)) {
                        continue;
                    }
                    if (pEnt.getGameMode() != GameMode.SURVIVAL) {
                        continue;
                    }
                    if (!p.canSee(pEnt)) {
                        continue;
                    }





                    if (pEnt.getInventory() == null) continue;
                    if ((pEnt.getPlayer().getInventory().getChestplate() != null)) {
                        if ((AstroItemSkinsAPI.hasSkinEquipped(pEnt.getPlayer(), SkinType.THANKSGIVING_2021_BACKPACK) || AstroItemSkinsAPI.hasSkinEquipped(pEnt.getPlayer(), SkinType.THANKSGIVING_2021_MUTATED_BACKPACK)) && Math.random() < 0.075) {

                            continue;
                        }
                    }
                    if (pEnt.hasMetadata("NPC")) {
                        continue;
                    }
                    if (WorldGuardUtils.isPvPDisabled(ent.getLocation())) {
                        continue;
                    }
                    if (ent.hasMetadata("spectator")) {
                        continue;
                    }

                    if (ent.getWorld().getName().equals("space")) {
                        continue;
                    }

                    if(AstroCore.getInstance().getFreezeManager().getFrozenPlayers().containsKey(ent.getUniqueId())) {
                        return;
                    }

                    final LivingEntity le = (LivingEntity) ent;
                    final Player attacker = (Player) player;

                    le.damage(damage);

                    if (AstroItemSkinsAPI.hasSkinEquipped(pEnt, SkinType.THANKSGIVING_SWORD_HARVESTER) && Math.random() < 0.5) {
                        pEnt.sendMessage(CC.translate("&e&l** SOUL HARVESTER [&7Blocked Divine Immolation&e&l] **"));
                        continue;
                    }

                    if (AstroItemSkinsAPI.hasSkinEquipped(pEnt, SkinType.CHRISTMAS_2022_AMULET_ACTIVE)) {
                        continue;
                    }

                    if (AstroItemSkinsAPI.hasSkinEquipped(pEnt, SkinType.CHRISTMAS_2022_HELMET_ACTIVE) && Math.random() < 0.5) {
                        pEnt.sendMessage(CC.translate("&9&l** FROST MONSTER TROPHY [&7Blocked Divine Immolation&9&l] **"));
                        continue;
                    }

                    if (AstroItemSkinsAPI.hasSkinEquipped(pEnt, SkinType.THANKSGIVING_2022_SWORD_GREEN) && Math.random() < 0.33) {
                        pEnt.sendMessage(CC.translate("&e&l** RAY SABRE [&7Blocked Divine Immolation&e&l] **"));
                        continue;
                    }

                    if (AstroItemSkinsAPI.hasSkinEquipped(pEnt, SkinType.PRISONS_DARKSTAR_PICKAXE_BLUE) && Math.random() < 0.15) {
                        pEnt.sendMessage(CC.translate("&3&l** DARKSTAR CHASER [&7Blocked Divine Immolation&3&l] **"));
                        continue;
                    }
                    if (AstroItemSkinsAPI.hasSkinEquipped(pEnt, SkinType.BACK_TO_SCHOOL_2022_AXE) && Math.random() < 0.5) {
                        pEnt.sendMessage(CC.translate("&8&l** CALCULATOR [&7Blocked Divine Immolation&8&l] **"));
                        continue;
                    }


                    if (playerHit instanceof Player && AstroItemSkinsAPI.hasSkinEquipped((Player) playerHit, SkinType.VALENTINES_2022_MUTATED_BACKPACK)) {
                        continue;
                    }

                    double multiplier = 1.0;
                    if (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.DOOMSDAY_SWORD) || (AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.THANKSGIVING_SWORD_HARVESTER) && le instanceof Player && !AstroItemSkinsAPI.hasSkinEquipped((Player) player, SkinType.VALENTINES_2021_HELMET)) || player.hasMetadata("soulEnchantMultiplier")) {
                        multiplier += 0.25;
                    }


                    try {
                        ParticleEffect.FLAME.sendToPlayers(Bukkit.getOnlinePlayers(), le.getLocation(), 1, 1, 1, 0.05f, 10);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }



                    ((Player) le).playSound(le.getLocation(), Sound.FIREWORK_BLAST, 1.0f, 0.3f);
                    ((Player) le).playSound(le.getLocation(), Sound.ZOMBIE_PIG_ANGRY, 0.8f, 0.5f);





                    le.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, (int)(damage * 10.0), 1));
                ent.setMetadata("divineFire", new FixedMetadataValue(Enchantments.getInstance(), System.currentTimeMillis()));
                ent.setMetadata("divineFireLevel", new FixedMetadataValue(Enchantments.getInstance(), multiplier * level));
                }
            }
        }
    }

    private List<Player> getNearbyPlayers(final List<Entity> ents) {
        final List<Player> players = new ArrayList<>();
        for (final Entity e : ents) {
            if (e instanceof Player) {
                players.add((Player) e);
            }
        }
        return players;
    }
}
