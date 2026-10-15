package com.cosmicpvp.cosmicutils.itembuilder;

import com.cosmicpvp.cosmicutils.utils.CustomItemUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomTitleBuilder
        extends ItemBuilder {
    private String title;

    public CustomTitleBuilder(String title) {
        super((Material) null, 0);
        this.title = title;
    }

    @Override
    public ItemStack build() {
        return CustomItemUtils.getTitleVoucher(this.title);
    }
}

