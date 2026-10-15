package net.astropvp.customblocks.struct.impl;

import net.astropvp.customblocks.guis.XPShopGui;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import net.astropvp.customblocks.struct.abilities.ChunkAble;
import org.bukkit.entity.Player;

public class DarkMatter extends CustomBlock implements ChunkAble {

    public DarkMatter() {
        super(CustomBlockType.DARK_MATTER);
    }

    @Override
    public void onClick(Player clicker) {
        XPShopGui xpShopGui = new XPShopGui(clicker);
        xpShopGui.getExpShopUI().open(clicker);
    }
}