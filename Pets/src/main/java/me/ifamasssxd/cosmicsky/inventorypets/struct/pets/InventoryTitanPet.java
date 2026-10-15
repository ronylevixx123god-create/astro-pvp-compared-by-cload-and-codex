package me.ifamasssxd.cosmicsky.inventorypets.struct.pets;

import com.cosmicpvp.cosmicutils.utils.*;
import com.google.common.collect.Lists;
import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FPlayers;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.perms.Relation;
import com.rit.sucy.EnchantmentAPI;
import me.ifamasssxd.cosmicsky.inventorypets.CosmicSkyInventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.NonPrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.exceptions.PrefixedPetException;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPet;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPetItem;
import me.ifamasssxd.cosmicsky.inventorypets.struct.InventoryPets;
import me.ifamasssxd.cosmicsky.inventorypets.struct.PetCooldown;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class InventoryTitanPet extends InventoryPet {
    public int petLevel = 0;

    public InventoryPets getOwner() {
        return InventoryPets.TITAN;
    }

    public List getPetDescription() {
        return Lists.newArrayList(CC.Gray + "Mark your target for assured destruction.", CC.Gray, CC.WhiteB + "Ability", CC.Gray + "Activates " + CC.DarkRedB + "[Unbreakable]:", CC.DarkRed + "* " + CC.Gray + "Immune to Freezes(10s) ", CC.DarkRed + "* " + CC.Gray + "Immune to Teleblock (10s) ", CC.DarkRed + "* " + CC.Gray + "-50% Incoming damage (10s) ", CC.DarkRed + "* " + CC.Gray + "1.25x DMG to all players (10s) ", CC.DarkRed + "* " + CC.Gray + "Gears IV (20s) ", CC.DarkRed + "* " + CC.Gray + "Shockwave II effect ", CC.DarkRed + "* " + CC.Gray + "Clears all freezes ", CC.DarkRed + "* " + CC.Gray + "Automatic /fix all ", CC.Gray, CC.WhiteB + "Cooldown", CC.Gray + " " + TimeUtils.formatSeconds((long) this.getCooldown()));
    }

    public int getExpForLevel(int level) {
        return 700 + 100 * level;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getCooldown() {
        return 360;
    }

    public boolean onPetItemInteract(Player player, ItemStack itemStack, InventoryPetItem petItem, PlayerInteractEvent event) throws Exception {

            Faction faction = FactionUtils.getFaction(player);
            if (faction != null && !faction.isNone()) {
                if (!player.hasMetadata("in_duel")) {
                    Location locationClicked = player.getLocation();
                    if (!player.hasMetadata("spectator") && WorldGuardUtils.isPvPEnabled(locationClicked)) {
                        this.petLevel = petItem.getLevel();
                        //petItem.setLastUsed(System.currentTimeMillis());
                        CooldownUtils.registerCooldown(player, new PetCooldown(petItem.getPetType().name(), getCooldown()));

                        this.resetInventoryToFull(player);
                        this.startTenSecondAbilities(player);
                        boolean xpGain = true;
                        player.playSound(player.getLocation(), Sound.DIG_SNOW, 1.1F, 3.0F);
                        player.playSound(player.getLocation(), Sound.WITHER_SHOOT, 1.1F, 3.0F);
                    }
                }
                return true;
            } else {
                throw new PrefixedPetException("You must be in a faction to use this pet!");
            }
    }

    private void applyShockwaveDefence(Player player) throws Exception {
        Iterator var2 = player.getNearbyEntities(3.0D, 3.0D, 3.0D).iterator();

        while (true) {
            while (true) {
                Entity ent;
                Player p;
                do {
                    do {
                        do {
                            do {
                                if (!var2.hasNext()) {
                                    return;
                                }

                                ent = (Entity) var2.next();
                            } while (!(ent instanceof Player));

                            p = (Player) ent;
                        } while (EssentialsUtils.isVanished(p));
                    } while (p.hasMetadata("NPC"));
                } while (p.hasMetadata("in_duel"));

                if (p.hasMetadata("teleblockTerminator")) {
                    p.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "* TERMINATOR MASK: Blocked Shockwave (" + ChatColor.GRAY + player.getName() + ChatColor.DARK_RED + ")");
                    player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "* TERMINATOR MASK: Blocked Shockwave (" + ChatColor.GRAY + p.getName() + ChatColor.DARK_RED + ")");
                } else {
                    FPlayer castingPlayer = (FPlayer) FPlayers.getInstance().getByPlayer(player);
                    FPlayer surroundingPlayer = (FPlayer) FPlayers.getInstance().getByPlayer(p);
                    if (castingPlayer.getRelationTo(surroundingPlayer) == Relation.ENEMY || castingPlayer.getRelationTo(surroundingPlayer) == Relation.NEUTRAL) {
                        p.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "* TITAN UNBREAKABLE: (" + ChatColor.GRAY + player.getName() + ChatColor.DARK_RED + ")");
                        player.sendMessage("" + ChatColor.DARK_RED + ChatColor.BOLD + "* TITAN UNBREAKABLE: (" + ChatColor.GRAY + p.getName() + ChatColor.DARK_RED + ")");
                        GeneralUtil.pushAwayEntity(player, ent, 1.7000000476837158D);
                        Location l = ent.getLocation();
                        de.inventivegames.particle.ParticleEffect.ENCHANTMENT_TABLE.sendToPlayers(Bukkit.getOnlinePlayers(), l, (float) Math.random() * 2.0F, (float) Math.random() * 2.0F, (float) Math.random() * 2.0F, 0.73F, 100);
                    }
                }
            }
        }
    }

    private void resetInventoryToFull(Player player) {
        this.repair(player);
        ItemStack[] arrayOfItemStack;
        int localItemStack1 = (arrayOfItemStack = player.getInventory().getArmorContents()).length;

        for (int ix = 0; ix < localItemStack1; ++ix) {
            ItemStack i = arrayOfItemStack[ix];
            this.repair(i);
        }

    }

    private void startTenSecondAbilities(final Player player) throws Exception {
        player.removePotionEffect(PotionEffectType.SLOW);
        player.setWalkSpeed(0.2F);
        this.applyShockwaveDefence(player);
        player.setMetadata("dragon_slayer_workaround_titan", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
        player.setMetadata("immune_freeze", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
        player.setMetadata("immune_to_teleblock", new FixedMetadataValue(CosmicSkyInventoryPets.get(), System.currentTimeMillis()));
        EnchantmentAPI.getEnchantment("Gears").applyEquipEffect(player, 4);
        (new BukkitRunnable() {
            public void run() {
                player.removeMetadata("dragon_slayer_workaround_titan", CosmicSkyInventoryPets.get());
                player.removeMetadata("immune_freeze", CosmicSkyInventoryPets.get());
                player.removeMetadata("immune_to_teleblock", CosmicSkyInventoryPets.get());
                EnchantmentAPI.getEnchantment("Gears").applyUnequipEffect(player, 4);
            }
        }).runTaskLater(CosmicSkyInventoryPets.get(), 200L);
    }

    public boolean onTickInHotBar(Player var1, int var2, InventoryPetItem var3) {
        return false;
    }

    private void repair(ItemStack i) {
        try {
            if (this.isRepairable(i.getType().name())) {
                i.setDurability((short) 0);
            }
        } catch (Exception var3) {
        }

    }

    public void repair(Player p) {
        for (int i = 0; i <= 36; ++i) {
            try {
                ItemStack w = p.getInventory().getItem(i);
                if (this.isRepairable(w.getType().name())) {
                    p.getInventory().getItem(i).setDurability((short) 0);
                }
            } catch (Exception var4) {
            }
        }

    }

    private boolean isRepairable(String itemType) {
        return itemType.contains("HELMET") || itemType.contains("CHESTPLATE") || itemType.contains("LEGGINGS") || itemType.contains("BOOTS") || itemType.contains("AXE") || itemType.contains("SWORD") || itemType.contains("HOE") || itemType.contains("PICKAXE") || itemType.contains("SPADE") || itemType.contains("CARROT_STICK") || itemType.contains("FLINT_AND_STEEL") || itemType.contains("SHEARS") || itemType.contains("BOW") || itemType.contains("FISHING_ROD");
    }
}
