/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut
 *  org.bukkit.World
 *  org.bukkit.block.Block
 *  org.bukkit.block.Chest
 *  org.bukkit.inventory.ItemStack
 *  org.bukkit.metadata.FixedMetadataValue
 *  org.bukkit.metadata.MetadataValue
 *  org.bukkit.plugin.Plugin
 */
package com.compilex.CosmicDungeons.dungeons.libs;

import com.compilex.CosmicDungeons.CosmicDungeonsPlugin;
import com.compilex.CosmicDungeons.dungeons.events.PlanetNulEvents;
import com.compilex.CosmicDungeons.dungeons.events.planetnul.PanelColor;
import com.compilex.CosmicDungeons.dungeons.libs.Dungeon;
import com.compilex.CosmicDungeons.dungeons.libs.DungeonType;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import me.ifamasssxd.cosmicbosses.dungeonboss.planetnul.CorruptCosmonaut;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

public class PlanetNulDungeon
extends Dungeon {
    public CorruptCosmonaut boss;
    public boolean bossPendingRespawn = false;
    public boolean hasBeenDefeated = CosmicDungeonsPlugin.get().getConfig().getBoolean("planetNulDefeated", false);

    public boolean isBossPendingRespawn() {
        return this.bossPendingRespawn;
    }

    public void setBossPendingRespawn(boolean bossPendingRespawn) {
        this.bossPendingRespawn = bossPendingRespawn;
    }

    public PlanetNulDungeon(World dungeonWorld, String dungeonName, LinkedList<String> dungeonDescription) {
        super(dungeonWorld, DungeonType.PLANET_NUL, dungeonName, dungeonDescription);
    }

    public List<PanelColor> getRandomColorsForPanel() {
        ArrayList<PanelColor> allColors = new ArrayList<PanelColor>(Arrays.asList(PanelColor.values()));
        Collections.shuffle(allColors);
        return Lists.newArrayList((PanelColor)((Object)allColors.get(0)), (PanelColor)((Object)allColors.get(1)), (PanelColor)((Object)allColors.get(2)));
    }

    @Override
    public void reset() {
        super.reset();
        if (this.boss != null && this.boss.isBossSpawned()) {
            this.boss.getEntity().remove();
        }
        this.boss = null;
    }

    private void fillChest(Block block, int lootToFill, Set<UUID> eligible) {
        List<ItemStack> potentialLoot = CosmicDungeonsPlugin.get_().getDungeonLootbagListener().getAllDungeonLoot(this.getDungeonType());
        Collections.shuffle(potentialLoot);
        Chest chest = (Chest)block.getState();
        for (int i = 0; i < lootToFill && i < potentialLoot.size(); ++i) {
            ItemStack item = potentialLoot.get(i);
            if (item == null) continue;
            chest.getInventory().addItem(new ItemStack[]{item});
        }
        chest.update();
        block.setMetadata("dungeonChallengeLootChest", (MetadataValue)new FixedMetadataValue((Plugin)CosmicDungeonsPlugin.get_(), eligible));
    }

    @Override
    public PlanetNulEvents getDungeonEvents() {
        return (PlanetNulEvents)super.getDungeonEvents();
    }
}

