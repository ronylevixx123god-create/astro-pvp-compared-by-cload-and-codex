package net.astropvp.customblocks.struct.impl;

import net.astropvp.customblocks.guis.CrateKeyOpenerGUI;
import net.astropvp.customblocks.struct.CustomBlock;
import net.astropvp.customblocks.struct.CustomBlockType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class KeyOpener extends CustomBlock {
    private List<ItemStack> items;

    public KeyOpener() {
        super(CustomBlockType.SECURITY_SYSTEM);
        this.items = new ArrayList<>();
    }

    @Override
    public void onClick(Player clicker) {
        CrateKeyOpenerGUI crateKeyOpenerGUI = new CrateKeyOpenerGUI(this, clicker);
        crateKeyOpenerGUI.getCustomBlockGui().open(clicker);
    }
}
