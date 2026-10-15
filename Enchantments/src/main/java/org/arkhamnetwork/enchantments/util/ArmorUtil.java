package org.arkhamnetwork.enchantments.util;

import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import net.minecraft.server.v1_8_R3.Items;
import net.minecraft.server.v1_8_R3.NBTBase;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

public class ArmorUtil {
    public static float getArmorDamageNullificationPercent(final float armor_val) {
        if (armor_val == 0.0f) {
            return 1.0f;
        }
        return 1.0f - armor_val * 0.04f;
    }

    public static boolean hasArmorPiece(final Player player, final ArmorSlot slot) {
        final ItemStack retr = player.getEquipment().getArmorContents()[slot.getIndex()];
        return retr != null && retr.getType() != Material.AIR;
    }

    public static ItemStack damagePlayerArmorByPercent(final Player player, final ArmorSlot slot, final double percent) {
        final EntityEquipment equipment = player.getEquipment();
        final ItemStack item = equipment.getArmorContents()[slot.getIndex()];
        if (item == null || item.getType() == Material.AIR) {
            return null;
        }
        final int maxDurability = ItemUtils.getMaxDurability(item);
        if (maxDurability <= 0) {
            return null;
        }
        final double amount = (int) (maxDurability * 0.01 * percent);
        return modifyPlayerArmor(player, slot, (int) (-amount), false);
    }

    public static ItemStack modifyPlayerArmor(final Player player, final ArmorSlot slot, final int amount) {
        return modifyPlayerArmor(player, slot, amount, false);
    }

    public static ItemStack modifyPlayerArmor(final Player player, final ArmorSlot slot, final int amount, final boolean checkNoDura) {
        final EntityEquipment equipment = player.getEquipment();
        ItemStack item = equipment.getArmorContents()[slot.getIndex()];
        if (item != null) {
            if (checkNoDura && item.getDurability() <= 0) {
                return item;
            }
            if (amount > 0) {
                item = ItemUtils.addDurability(item, amount);
            } else {
                item = ItemUtils.subtractDurability(item, Math.abs(amount));
            }
            if (item != null && item.getDurability() > item.getType().getMaxDurability()) {
                final short dura = item.getDurability();
                item = null;
                player.playSound(player.getLocation(), Sound.ITEM_BREAK, 3.0f, 0.8f);
                Bukkit.getLogger().info("[Enchantments] Clearing ArmorSlot " + slot.name() + " for " + player.getName() + " damaged " + amount + " dura, new dura: " + dura);
            }
        }
        if (slot == ArmorSlot.HELMET) {
            player.getInventory().setHelmet(item);
        } else if (slot == ArmorSlot.CHESTPLATE) {
            player.getInventory().setChestplate(item);
        } else if (slot == ArmorSlot.LEGGINGS) {
            player.getInventory().setLeggings(item);
        } else if (slot == ArmorSlot.BOOTS) {
            player.getInventory().setBoots(item);
        }
        return item;
    }

    public static float getEnchantDamageModifier(final Player pl) {
        final ItemStack weapon = pl.getItemInHand();
        float damage_mod = 0.0f;
        if (weapon != null) {
            if (weapon.getType() != Material.BOW && weapon.getEnchantments().containsKey(Enchantment.DAMAGE_ALL)) {
                damage_mod = weapon.getEnchantments().get(Enchantment.DAMAGE_ALL) * 0.625f;
            }
            if (weapon.getType() == Material.BOW && weapon.getEnchantments().containsKey(Enchantment.ARROW_DAMAGE)) {
                damage_mod = 0.25f * (weapon.getEnchantments().get(Enchantment.ARROW_DAMAGE) + 1.0f);
            }
        }
        return damage_mod;
    }

    public static int getArmorValue(final Player pl) {
        int armor_val = 0;
        for (net.minecraft.server.v1_8_R3.ItemStack item : ((CraftPlayer) pl).getHandle().inventory.getArmorContents()) {
            Label_0343:
                    try {
                        final Item items = (Item) item.getItem();
                        if (items == Items.LEATHER_HELMET) {
                            ++armor_val;
                        }
                        if (items == Items.LEATHER_CHESTPLATE) {
                            armor_val += 3;
                        }
                        if (items == Items.LEATHER_LEGGINGS) {
                            armor_val += 2;
                        }
                        if (items == Items.LEATHER_BOOTS) {
                            ++armor_val;
                        }
                        if (items == Items.CHAINMAIL_HELMET) {
                            armor_val += 2;
                        }
                        if (items == Items.CHAINMAIL_CHESTPLATE) {
                            armor_val += 5;
                        }
                        if (items == Items.CHAINMAIL_LEGGINGS) {
                            armor_val += 4;
                        }
                        if (items == Items.CHAINMAIL_BOOTS) {
                            ++armor_val;
                        }
                        if (items == Items.IRON_HELMET) {
                            armor_val += 2;
                        }
                        if (items == Items.IRON_CHESTPLATE) {
                            armor_val += 6;
                        }
                        if (items == Items.IRON_LEGGINGS) {
                            armor_val += 5;
                        }
                        if (items == Items.IRON_BOOTS) {
                            armor_val += 2;
                        }
                        if (items == Items.DIAMOND_HELMET) {
                            armor_val += 3;
                        }
                        if (items == Items.DIAMOND_CHESTPLATE) {
                            armor_val += 8;
                        }
                        if (items == Items.DIAMOND_LEGGINGS) {
                            armor_val += 6;
                        }
                        if (items == Items.DIAMOND_BOOTS) {
                            armor_val += 3;
                        }
                        if (items == Items.GOLDEN_HELMET) {
                            armor_val += 2;
                        }
                        if (items == Items.GOLDEN_CHESTPLATE) {
                            armor_val += 5;
                        }
                        if (items == Items.GOLDEN_LEGGINGS) {
                            armor_val += 3;
                        }
                        if (items == Items.GOLDEN_BOOTS) {
                            ++armor_val;
                        }
                    } catch (Exception ex) {

                    }
                }


        return armor_val;

    }
}
