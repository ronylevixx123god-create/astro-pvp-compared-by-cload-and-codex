package org.demonicpvp.demonictrials.trial.modifier.madness.impl;

import co.aikar.commands.lib.expiringmap.ExpiringMap;
import com.cosmicpvp.cosmicutils.utils.CommonsUtil;
import com.cosmicpvp.cosmicutils.utils.LocationUtils;
import com.google.common.collect.Sets;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.demonicpvp.demonictrials.trial.TrialSession;
import org.demonicpvp.demonictrials.trial.modifier.madness.AbstractMadnessModifier;
import org.demonicpvp.demonictrials.util.Utils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class StickyKeysModifier extends AbstractMadnessModifier implements Listener {
    private final ExpiringMap<String, Boolean> stuckBlocks;
    private final Set<String> allowedBlocks;

    public StickyKeysModifier(TrialSession trial) {
        super(trial);
        this.stuckBlocks = ExpiringMap.builder().expiration(5L, TimeUnit.SECONDS).build();
        this.allowedBlocks = Sets.newHashSet();
    }

    @Override
    public String getName() {
        return "Sticky Keys";
    }

    @Override
    public String getDescription() {
        return "Buttons and levers have a 50% chance to not work for 5 seconds on use.";
    }

    @Override
    public Material getMaterial() {
        return Material.SLIME_BALL;
    }

    @Override
    public void onRoomEnd() {
        this.clearCooldown();
    }

    @Override
    public void reset() {
        this.stuckBlocks.clear();
        this.allowedBlocks.clear();
        this.sendInfiniteCooldown();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void on(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (this.getTrial().getPlayers().contains(player)) {
            Block clickedBlock = event.getClickedBlock();

            if (event.getAction() == Action.RIGHT_CLICK_BLOCK && (clickedBlock.getType() == Material.STONE_BUTTON || clickedBlock.getType() == Material.WOOD_BUTTON || clickedBlock.getType() == Material.LEVER)) {
                String location = Utils.locationToString(clickedBlock.getLocation());

                if (this.stuckBlocks.containsKey(location)) {
                    this.sendMessage(player, "This block is stuck!");
                    event.setCancelled(true);
                } else {
                    if (Math.random() < 0.5 && !this.allowedBlocks.contains(location)) {
                        this.sendMessage(player, "This block has been disabled for 5s!");
                        this.stuckBlocks.put(location, true);
                        this.allowedBlocks.add(location);
                        event.setCancelled(true);
                    }
                }

                if (!event.isCancelled()) {
                    this.allowedBlocks.remove(location);
                }
            }
        }
    }
}
