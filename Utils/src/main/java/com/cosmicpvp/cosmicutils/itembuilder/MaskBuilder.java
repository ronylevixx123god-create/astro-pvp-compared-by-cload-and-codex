/*
 * Decompiled with CFR 0.150.
 *
 * Could not load the following classes:
 *  me.ifamasssxd.cosmicmasks.MaskType
 */
package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class MaskBuilder
        extends ItemBuilder {
    private String maskType;

    public MaskBuilder(String maskType) {
        super(Material.SKULL_ITEM);
        this.maskType = maskType;
    }

    @Override
    public ItemStack build() {
        MaskType type;
        if (this.maskType != null && CosmicUtils.get().isMasksEnabled() && (type = MaskType.getFromName((String) this.maskType)) != null) {
            return type.createMask(true);
        }
        return null;
    }
}

