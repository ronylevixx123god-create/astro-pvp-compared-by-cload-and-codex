package org.arkhamnetwork.enchantments.mastery;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import net.astropvp.core.killtracker.KillTrackerAPI;
import org.arkhamnetwork.enchantments.MasteryCustomEnchantment;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.util.ArmorSlot;
import org.arkhamnetwork.enchantments.util.ArmorUtil;
import org.arkhamnetwork.enchantments.util.GeneralUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MasterySoulSiphon extends MasteryCustomEnchantment {
    public MasterySoulSiphon() {
        super("Soul Siphon", GeneralUtil.armor, 11);
        this.armorSet = "NECROMANCER";
        this.max = 4;
    }

    @Override
    public void applyDamagedOtherPlayer(final Player damager, final Player damaged, final EntityDamageByEntityEvent event, final int enchantLevel) {
        /*final double chance = 0.04 + 0.01 * enchantLevel;
        final double toHeal = 2 + enchantLevel * 2;
        final double random = Math.random();
        if (random < chance) {
            if (!new CustomEnchantProcOnEntityEvent((LivingEntity) damager, (LivingEntity) damaged, (Event) event, this, enchantLevel).callEvent()) {
                return;
            }
            //GeneralUtil.healPlayer((LivingEntity) damager, toHeal);
            final int soulCount = KillTrackerAPI.getAllSouls(damaged);
            final int toSteal = (int) Math.min(Math.max(1, soulCount), Math.ceil(enchantLevel * 25.0));
            final int toGive = toSteal / 2;
            if (soulCount >= toSteal) {
                KillTrackerAPI.removeSoulsFromGems(damaged, toSteal);
                damaged.sendMessage(CC.RedB + "* SOUL SIPHON [" + CC.Gray + "-" + toSteal + " Souls (" + CC.Gray + damager.getName() + ")" + CC.RedB + "]");
                boolean gave = false;
                for (final ItemStack item : damager.getInventory()) {
                    if (KillTrackerAPI.isSoulGem(item)) {
                        final int amount = KillTrackerAPI.getSouls(item) + toGive;
                        KillTrackerAPI.setSouls(item, amount);
                        gave = true;
                        break;
                    }
                }
                if (!gave) {
                    damager.getInventory().addItem(new ItemStack[]{KillTrackerAPI.createSoulShard(toGive)});
                }
                damager.sendMessage(CC.GreenB + "* SOUL SIPHON [" + CC.Gray + damaged.getName() + " (+" + toGive + " Souls)" + CC.GreenB + "]");
                Bukkit.getLogger().info("[Soul Siphon] Adding souls to " + damager.getName() + " from " + damaged.getName() + " toSteal: " + toSteal + ", foundSoulsToAppend: " + gave);
            } else {
                final List<Integer> armorSlots = new ArrayList<>();
                for (int i = 0; i < damaged.getInventory().getArmorContents().length; ++i) {
                    armorSlots.add(i);
                }
                Collections.shuffle(armorSlots);
                for (final int j : armorSlots) {
                    final ItemStack is = damaged.getInventory().getArmorContents()[j];
                    if (is != null) {
                        if (is.getType() == Material.AIR) {
                            continue;
                        }
                        final ArmorSlot slot = ArmorSlot.getArmorSlotByIndex(j);
                        if (slot != null) {
                            final int maxDurability = ItemUtils.getMaxDurability(is);
                            final int currentDurability = ItemUtils.getCurrentDurability(is);
                            final short duraToTake = (short) Math.min(Math.ceil(maxDurability * 0.025 + enchantLevel * 0.02), maxDurability - currentDurability);
                            final short duraToGive = (short) Math.max(1, duraToTake / 3);
                            ArmorUtil.modifyPlayerArmor(damaged, slot, -duraToTake);
                            if (ArmorUtil.hasArmorPiece(damager, slot)) {
                                ArmorUtil.modifyPlayerArmor(damager, slot, duraToGive);
                            } else {
                                for (final ArmorSlot slots : ArmorSlot.values()) {
                                    if (slots != slot && ArmorUtil.hasArmorPiece(damager, slots)) {
                                        ArmorUtil.modifyPlayerArmor(damager, slot, duraToGive);
                                        break;
                                    }
                                }
                            }
                            damaged.sendMessage(CC.RedB + "* SOUL SIPHON [" + CC.Gray + "-" + duraToTake + " Durability" + CC.RedB + "]");
                            damager.sendMessage(CC.GreenB + "* SOUL SIPHON [" + CC.Gray + "+" + duraToGive + " Durability" + CC.GreenB + "]");
                            Bukkit.getLogger().info("[Soul Siphon] Adding durability to " + slot + " for " + damager.getName() + " from " + damaged.getName() + " toTake: " + duraToTake + " toGive: " + duraToGive);
                            break;
                        }
                        continue;
                    }
                }
            }
        }
    }*/
    }
}
