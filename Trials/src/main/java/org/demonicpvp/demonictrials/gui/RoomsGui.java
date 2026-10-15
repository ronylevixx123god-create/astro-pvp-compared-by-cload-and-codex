package org.demonicpvp.demonictrials.gui;

import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.inventory.ClickableItem;
import com.cosmicpvp.cosmicutils.inventory.InventoryListener;
import com.cosmicpvp.cosmicutils.inventory.SmartInventory;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryContents;
import com.cosmicpvp.cosmicutils.inventory.content.InventoryProvider;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;

import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import org.demonicpvp.demonictrials.manager.TrialManager;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.util.Utils;
import org.github.paperspigot.Title;
import org.javatuples.Pair;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.google.common.collect.Lists;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.demonicpvp.demonictrials.trial.TrialTier;
import org.demonicpvp.demonictrials.trial.room.TrialRoom;
import org.demonicpvp.demonictrials.trial.room.impl.*;
import org.demonicpvp.demonictrials.util.Utils;

import java.util.List;
import java.util.UUID;

public class RoomsGui implements InventoryProvider {
    private final SmartInventory inventory;
    private final List<TrialRoom> allRooms;

    public RoomsGui(SmartInventory parent) {
        this.allRooms = Lists.newArrayList(
                new PortalPanicRoom(null),
                new ParkourRelayRoom(null),
                new TargetPracticeRoom(null),
                new MazeSweeperRoom(null),
                new SimonRoom(null),
                new ColorCrisisRoom(null),
                new CodeSwitchingRoom(null),
                new RaidingRainbowRoom(null),
                new HotscotchRoom(null),
                new ThunderstruckRoom(null)
        );

        this.inventory = SmartInventory.builder()
                .title("Trial Rooms")
                .id(UUID.randomUUID().toString())
                .size((int) Math.ceil((this.allRooms.size() + 1) / 9.0), 9)
                .manager(CosmicUtils.get().getInventoryManager())
                .provider(this)
                .parent(parent)
                .build();
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        contents.add(ClickableItem.of(
                CommonsUtil.createItem(
                        Material.ARROW,
                        "&c&lBack",
                        "&7Click to return to",
                        "&7the main GUI."
                ), event -> {
                    this.inventory.getParent().ifPresent(parent -> parent.open(player));
                }
        ));


        for (TrialRoom room : this.allRooms) {
            List<String> lore = Lists.newArrayList("&8Trial Room");

            for (String line : room.getDescription()) {
                lore.add("&7" + line);
            }

            if (room.getRollUntil() != TrialTier.DEMONIC) {
                lore.add("");
                lore.add("&c&l(!) &cDoes not roll above " + room.getRollUntil().getColor() + "&l" + room.getRollUntil().getName() + " &ctier.");
            }

            contents.add(ClickableItem.empty(
                    CommonsUtil.createItem(
                            room.getMaterial(),
                            room.getColor() + ChatColor.BOLD.toString() + room.getName(),
                            lore
                    )
            ));
        }

        Utils.fillEmptySlots(this.inventory, contents, "&f&l???");
    }

    public SmartInventory getInventory() {
        return this.inventory;
    }
}
