package me.ifamasssxd.cosmicbosses.struct;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.*;

public class BlockModifier {
    private HashMap<BossAbility, List<Location>> toRemove;
    private HashMap<BossAbility, HashMap<Location, NewBlockData>> toReplace;
    Random random;

    public BlockModifier() {
        this.toRemove = new HashMap<BossAbility, List<Location>>();
        this.toReplace = new HashMap<BossAbility, HashMap<Location, NewBlockData>>();
        this.random = new Random();
    }

    public boolean containsLocation(final Location l) {
        for (final List<Location> listLoc : this.toRemove.values()) {
            if (listLoc.contains(l)) {
                return true;
            }
        }
        for (final HashMap<Location, NewBlockData> loc : this.toReplace.values()) {
            if (loc.containsKey(l)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsLocation(final BossAbility ability, final Location l) {
        if (this.toRemove.containsKey(ability)) {
            return this.toRemove.get(ability).contains(l);
        }
        return this.toReplace.containsKey(ability) && this.toReplace.get(ability).containsKey(l);
    }

    public void removeBlock(final BossAbility ability, final Location loc) {
        final List<Location> removed = this.toRemove.get(ability);
        final Block b = loc.getBlock();
        b.setType(Material.AIR);
        removed.remove(loc);
        this.toRemove.put(ability, removed);
    }

    public void removeAllBlocks(final boolean particles) {
        for (final BossAbility key : this.toRemove.keySet()) {
            this.removeBlocks(key, false, particles);
        }
        this.toRemove.clear();
    }

    public void removeBlocks(final BossAbility ability, final boolean remove, final boolean particles) {
        if (!this.toRemove.containsKey(ability)) {
            return;
        }
        for (final Location l : this.toRemove.get(ability)) {
            final Block b = l.getBlock();
            if (!particles || !b.getType().isSolid() || b.getType().isBlock()) {
            }
            b.setType(Material.AIR);
        }
        if (remove) {
            this.toRemove.remove(ability);
        }
    }

    public void replaceAll() {
        for (final BossAbility key : this.toReplace.keySet()) {
            this.replaceBlocks(key, false);
        }
        this.toReplace.clear();
    }

    public void replaceBlocks(final BossAbility ability, final boolean removeAfter) {
        if (!this.toReplace.containsKey(ability)) {
            return;
        }
        final HashMap<Location, NewBlockData> data = this.toReplace.get(ability);
        for (final Map.Entry<Location, NewBlockData> blockData : data.entrySet()) {
            final Block b = blockData.getKey().getBlock();
            final NewBlockData newBlockDAta = blockData.getValue();
            b.setType(newBlockDAta.getMaterial());
            b.setData(newBlockDAta.getData());
        }
        if (removeAfter) {
            this.toReplace.remove(ability);
        }
    }

    public void restoreBlock(final BossAbility ability, final Block block) {
        final Map<Location, NewBlockData> data = this.toReplace.get(ability);
        if (data == null) {
            return;
        }
        final NewBlockData blockData = data.get(block.getLocation());
        if (blockData == null) {
            return;
        }
        block.setTypeIdAndData(blockData.getMaterial().getId(), blockData.getData(), false);
    }

    public NewBlockData removeBlockAndStore(final BossAbility ability, final Location location) {
        final Map<Location, NewBlockData> storedBlocks = this.toReplace.computeIfAbsent(ability, map -> new HashMap());
        final Block block = location.getBlock();
        final NewBlockData stored = new NewBlockData(block.getType(), block.getData());
        storedBlocks.put(block.getLocation(), stored);
        block.setTypeIdAndData(Material.AIR.getId(), (byte) 0, true);
        return stored;
    }

    public NewBlockData storeBlockAndSet(final BossAbility ability, final Location location, final Material material, final byte data) {
        final Map<Location, NewBlockData> storedBlocks = this.toReplace.computeIfAbsent(ability, map -> new HashMap());
        final Block block = location.getBlock();
        final NewBlockData stored = new NewBlockData(block.getType(), block.getData());
        storedBlocks.put(block.getLocation(), stored);
        block.setTypeIdAndData(material.getId(), data, false);
        return stored;
    }

    public void storeBlockToRemove(final BossAbility ability, final Location l) {
        final List<Location> locs = this.toRemove.containsKey(ability) ? this.toRemove.get(ability) : new ArrayList<Location>();
        locs.add(l.clone());
        this.toRemove.put(ability, locs);
    }
}
