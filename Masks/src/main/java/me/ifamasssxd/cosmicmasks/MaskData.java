/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder
 *  com.cosmicpvp.cosmicutils.me.astropvp.cratekeys.net.astropvp.armorsets.utils.CC
 *  net.minecraft.server.v1_7_R4.GameProfileSerializer
 *  net.minecraft.server.v1_7_R4.NBTTagCompound
 *  net.minecraft.util.com.mojang.authlib.GameProfile
 *  net.minecraft.util.com.mojang.authlib.properties.Property
 */
package me.ifamasssxd.cosmicmasks;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.ifamasssxd.cosmicmasks.struct.CosmicMask;
import net.minecraft.server.v1_8_R3.GameProfileSerializer;
import net.minecraft.server.v1_8_R3.NBTTagCompound;

import java.util.List;
import java.util.UUID;

public class MaskData {
    private String base64;
    private String fullyEncoded;
    private String username;
    private UUID uuid;
    private boolean active = false;
    private String displayName;
    private String specialAbility;
    private List<String> lore;
    private NBTTagCompoundBuilder skullOwnerBuilder;
    private CosmicMask internalMask;

    public MaskData(String base, String fullyEncoded, String username, boolean active, UUID uuid, String displayName, List<String> lore) {
        this(null, base, fullyEncoded, username, active, uuid, displayName, null, lore);
    }

    public MaskData(CosmicMask internal, String base, String fullyEncoded, String username, boolean active, UUID uuid, String displayName, String special, List<String> lore) {
        this.active = false;
        this.base64 = base;
        this.specialAbility = special;
        this.uuid = uuid;
        this.internalMask = internal;
        this.username = username;
        this.fullyEncoded = fullyEncoded;
        this.active = active;
        GameProfile profile = new GameProfile(this.uuid, this.username);
        profile.getProperties().put("textures", new Property("textures", this.fullyEncoded));
        this.skullOwnerBuilder = new NBTTagCompoundBuilder();
        GameProfileSerializer.serialize(this.skullOwnerBuilder.get(), profile);
        this.displayName = displayName;
        (this.lore = lore).add("");
        this.lore.add(String.valueOf(String.valueOf(CC.GrayI)) + "Attach this mask to any helmet");
        this.lore.add(String.valueOf(String.valueOf(CC.GrayI)) + "to give it a visual override!");
        this.lore.add("");
        this.lore.add(String.valueOf(String.valueOf(CC.Gray)) + "To equip, place this mask on a helmet.");
        this.lore.add(String.valueOf(String.valueOf(CC.Gray)) + "To remove, right-click helmet while attached.");
    }

    public String getBase64() {
        return this.base64;
    }

    public String getFullyEncoded() {
        return this.fullyEncoded;
    }

    public String getUsername() {
        return this.username;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public boolean isActive() {
        return this.active;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getSpecialAbility() {
        return this.specialAbility;
    }

    public List<String> getLore() {
        return this.lore;
    }

    public NBTTagCompoundBuilder getSkullOwnerBuilder() {
        return this.skullOwnerBuilder;
    }

    public CosmicMask getInternalMask() {
        return this.internalMask;
    }
}

