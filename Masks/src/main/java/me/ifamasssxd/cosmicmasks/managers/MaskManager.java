/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.comphenix.protocol.PacketType
 *  com.comphenix.protocol.PacketType$Play$Server
 *  com.comphenix.protocol.ProtocolLibrary
 *  com.comphenix.protocol.events.ListenerPriority
 *  com.comphenix.protocol.events.PacketAdapter
 *  com.comphenix.protocol.events.PacketContainer
 *  com.comphenix.protocol.events.PacketEvent
 *  com.comphenix.protocol.events.PacketListener
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.NumberUtils
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_7_R4.Container
 *  net.minecraft.server.v1_7_R4.Entity
 *  net.minecraft.server.v1_7_R4.EntityHuman
 *  net.minecraft.server.v1_7_R4.NBTBase
 *  org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog
 *  org.bukkit.GameMode
 *  org.bukkit.Material
 *  org.bukkit.craftbukkit.v1_7_R4.CraftWorld
 *  org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer
 *  org.bukkit.entity.Player
 *  org.bukkit.me.astropvp.cratekeys.inventory.ItemStack
 *  org.bukkit.me.astropvp.cratekeys.inventory.meta.ItemMeta
 *  org.bukkit.plugin.Plugin
 */
package me.ifamasssxd.cosmicmasks.managers;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.google.common.collect.Lists;
import me.ifamasssxd.cosmicmasks.CosmicMasks;
import me.ifamasssxd.cosmicmasks.MaskType;
import net.minecraft.server.v1_8_R3.Container;
import net.minecraft.server.v1_8_R3.Entity;
import net.minecraft.server.v1_8_R3.EntityHuman;
import org.arkhamnetwork.Arkkit.patches.combatlog.CombatLog;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class MaskManager {
    private PacketAdapter listener;

    public void onEnable(CosmicMasks plugin) {
        this.listener = new PacketAdapter(plugin, ListenerPriority.LOWEST, PacketType.Play.Server.ENTITY_EQUIPMENT, PacketType.Play.Server.WINDOW_ITEMS, PacketType.Play.Server.SET_SLOT){

            public void onPacketSending(PacketEvent event) {
                Player observing = event.getPlayer();
                if (event.getPacketType() == PacketType.Play.Server.ENTITY_EQUIPMENT) {
                    ItemStack maskedItem;
                    ItemStack item;
                    Integer entityId = null;
                    if (observing.getGameMode() == GameMode.CREATIVE) {
                        entityId = event.getPacket().getIntegers().read(0);
                        if (observing.getEntityId() == entityId.intValue()) {
                            return;
                        }
                    }
                    if ((item = event.getPacket().getItemModifier().read(0)) != null && (maskedItem = MaskManager.this.getMaskedItem(item, true)) != null) {
                        Entity entity;
                        if (entityId == null) {
                            entityId = event.getPacket().getIntegers().read(0);
                        }
                        try {
                            if ((entity = (Entity) observing.getWorld().getEntities().get(entityId)) != null && !(entity instanceof EntityHuman)) {
                                return;
                            }
                        } catch(Exception e) {

                        }
                        event.getPacket().getItemModifier().write(0, maskedItem);
                    }
                } else if (event.getPacketType() == PacketType.Play.Server.WINDOW_ITEMS) {
                    ItemStack hat;
                    ItemStack masked;
                    int slot = event.getPacket().getIntegers().read(0);
                    if (slot != ((CraftPlayer)observing).getHandle().defaultContainer.windowId) {
                        return;
                    }
                    ItemStack[] items = event.getPacket().getItemArrayModifier().read(0);
                    if (items != null && items.length >= 4 && !CombatLog.inCombat(observing) && !observing.getGameMode().equals(GameMode.CREATIVE) && (masked = MaskManager.this.getMaskedItem(hat = items[5], false)) != null) {
                        items[5] = masked;
                        event.getPacket().getItemArrayModifier().write(0, items);
                    }
                } else if (event.getPacketType() == PacketType.Play.Server.SET_SLOT) {
                    try {
                        ItemStack maskItem;
                        PacketContainer packet = event.getPacket();
                        int windowId = packet.getIntegers().read(0);
                        int slot2 = packet.getIntegers().read(1);
                        Container contain = ((CraftPlayer)observing).getHandle().defaultContainer;
                        if (contain == null || contain.windowId != windowId || observing.getGameMode().equals(GameMode.CREATIVE)) {
                            return;
                        }
                        ItemStack helmet = packet.getItemModifier().read(0);
                        if (slot2 == 5 && !CombatLog.inCombat(observing) && (maskItem = MaskManager.this.getMaskedItem(helmet, false)) != null) {
                            packet.getItemModifier().write(0, maskItem);
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ProtocolLibrary.getProtocolManager().addPacketListener(this.listener);
    }

    public ItemStack getMaskedItem(ItemStack item, boolean checkInvalidHelmets) {
        MaskType type;
        NBTTagCompoundBuilder builder;
        String mask;
        NBTWrapper itemWrapper;
        if (item == null) {
            return null;
        }
        Material material = item.getType();
        if (checkInvalidHelmets && (material == Material.PUMPKIN || material == Material.JACK_O_LANTERN)) {
            return new ItemStack(Material.AIR);
        }
        if (checkInvalidHelmets && material == Material.SKULL_ITEM && !(itemWrapper = new NBTWrapper(item)).hasKey("cosmicType") && !itemWrapper.hasKey("conaxData")) {
            return new ItemStack(Material.AIR);
        }
        if (material != Material.DIAMOND_HELMET && material != Material.LEATHER_HELMET && material != Material.IRON_HELMET && material != Material.GOLD_HELMET && material != Material.CHAINMAIL_HELMET) {
            return null;
        }
        NBTWrapper wrapper = new NBTWrapper(material == Material.DIAMOND_HELMET ? item.clone() : item);
        if (wrapper.hasKey("conaxData") && (mask = (builder = new NBTTagCompoundBuilder(wrapper.getNBTBase("conaxData"))).getString("maskOverride")) != null && !mask.isEmpty() && (type = MaskType.getFromName(mask.toUpperCase())) != null) {
            double percent;
            double durability = item.getDurability();
            double maxDurability = item.getType().getMaxDurability();
            NBTWrapper newMaskWrapper = type.applyNbt(wrapper);
            if (builder.get().hasKey("durability") && builder.get().hasKey("maxDurability")) {
                maxDurability = builder.getInt("maxDurability").intValue();
                durability = builder.getInt("durability").intValue();
                percent = durability / maxDurability * 100.0;
            } else {
                double duraLeft;
                durability = duraLeft = maxDurability - durability;
                percent = duraLeft / maxDurability * 100.0;
            }
            NBTTagCompoundBuilder data = newMaskWrapper.hasKey("conaxData") ? new NBTTagCompoundBuilder(newMaskWrapper.getNBTBase("conaxData").clone()) : new NBTTagCompoundBuilder();
            data.setInt("durability", Integer.valueOf((int)durability));
            data.setInt("maxDurability", Integer.valueOf((int)maxDurability));
            newMaskWrapper.setNBTBase("conaxData", data.get());
            ItemStack newMask = newMaskWrapper.build();
            newMask.setType(Material.SKULL_ITEM);
            newMask.setDurability((short)3);
            ItemMeta im = newMask.getItemMeta();
            if (!im.hasDisplayName()) {
                im.setDisplayName(type.getMaskData().getDisplayName());
            }
            List<String> lore = im.hasLore() ? im.getLore() : Lists.newArrayList();
            lore.add(CC.Gray + "Durability: " + (int)durability + " / " + (int)maxDurability + " (" + NumberUtils.formatMoney(percent) + "%)");
            im.setLore(lore);
            newMask.setItemMeta(im);
            return newMask;
        }
        return null;
    }

    public void onDisable() {
        if (this.listener != null) {
            ProtocolLibrary.getProtocolManager().removePacketListener(this.listener);
        }
    }
}

