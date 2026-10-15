/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.CC
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.NBTWrapper
 *  com.cosmicpvp.cosmicutils.net.astropvp.armorsets.utils.TimeUtils
 *  com.google.common.collect.Lists
 *  net.minecraft.server.v1_8_R3.NBTBase
 *  org.bukkit.Material
 *  org.bukkit.inventory.ItemStack
 */
package com.compilex.CosmicDungeons.items;

import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.compilex.CosmicDungeons.items.KeyPair;
import com.compilex.CosmicDungeons.utils.ItemUtils;
import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.TimeUtils;
import com.google.common.collect.Lists;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class DungeonKeyItem
extends ItemBuilder {
    private DungeonType dungeonType;
    private long expiration;

    public DungeonKeyItem(DungeonType type) {
        this(type, System.currentTimeMillis() + TimeUnit.DAYS.toMillis(7L));
    }

    public DungeonKeyItem(DungeonType type, long expiration) {
        super(Material.TRIPWIRE_HOOK);
        this.expiration = expiration;
        this.dungeonType = type;
    }

    public static DungeonType getDungeonFromKey(ItemStack item) {
        String type;
        NBTTagCompoundBuilder builder;
        NBTWrapper wrapper = new NBTWrapper(item);
        if (wrapper.getString("cosmicType").equals("DUNGEON_KEY") && (builder = wrapper.getCompound("cosmicData")) != null && (type = builder.getString("dungeonType")) != null && !type.isEmpty()) {
            return DungeonType.getFromName(type);
        }
        return null;
    }

    public static KeyPair getDataFromKey(NBTWrapper wrapper) {
        DungeonType dungeonType;
        String type;
        NBTTagCompoundBuilder builder;
        if (wrapper == null) {
            return null;
        }
        if (wrapper.getString("cosmicType").equals("DUNGEON_KEY") && (builder = wrapper.getCompound("cosmicData")) != null && (type = builder.getString("dungeonType")) != null && !type.isEmpty() && (dungeonType = DungeonType.getFromName(type)) != null) {
            return new KeyPair(dungeonType, builder.getLong("expiration"));
        }
        return null;
    }

    public ItemStack build() {
        LinkedList lore = Lists.newLinkedList();
        lore.add(CC.Gray + "Consumed upon entering a");
        lore.add(CC.Gray + this.dungeonType.getDungeonName() + " /dungeon portal.");
        if (this.expiration > 0L) {
            lore.add(CC.RedB + "Valid Until " + CC.RedU + TimeUtils.formatTimeToDate((long)this.expiration, (String)"MMM d'%s' hh:mmaa", (String)"CST").replace(" 0", " ") + " CST");
        } else {
            lore.add(CC.Red + "NO EXPIRATION");
        }
        ItemStack item = ItemUtils.buildItem(Material.TRIPWIRE_HOOK, (short)0, (Object)this.dungeonType.getColor() + CC.Bold + this.dungeonType.getDungeonName() + " Dungeon Portal Key", lore);
        NBTWrapper wrapper = new NBTWrapper(item);
        wrapper.setString("cosmicType", "DUNGEON_KEY");
        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder();
        if (this.expiration != 0L) {
            builder.setLong("expiration", Long.valueOf(this.expiration));
        }
        wrapper.setNBTBase("cosmicData", (NBTBase)builder.setString("dungeonType", this.dungeonType.name()).get());
        wrapper.assignIID();
        return wrapper.build();
    }
}

