package com.conaxgames.customskulls.listeners;

import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.enums.AbilityType;
import com.conaxgames.customskulls.guis.CrateKeyOpenerGUI;
import com.conaxgames.customskulls.guis.SlotBotOpenerGUI;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.FactionUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class CrateKeyGUIListener implements Listener {


    @EventHandler
    public void onRightClick(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = e.getClickedBlock();
        if (clickedBlock.getType() != Material.NOTE_BLOCK) return;

        if (FactionUtils.getFaction(e.getPlayer()) != FactionUtils.getFactionAt(clickedBlock.getLocation()) && !e.getPlayer().isOp()) {
            e.getPlayer().sendMessage(CC.translate("&c&l(!)&c You need to be in the " + FactionUtils.getFactionAt(clickedBlock.getLocation()).getTag()) + " faction to open this grinder block!");
            return;
        }

        if (AbilityData.getByUUID(AbilityData.getUUID(clickedBlock.getLocation())) != null) {
            e.setCancelled(true);
            AbilityData data = AbilityData.getByUUID(AbilityData.getUUID(clickedBlock.getLocation()));
            if (data.getAbilityType() == AbilityType.SECURITY_SYSTEM) {
                CrateKeyOpenerGUI crateKeyOpenerGUI = new CrateKeyOpenerGUI(data, e.getClickedBlock(), p);
                crateKeyOpenerGUI.getCustomBlockGui().open(p);
            } else if (data.getAbilityType() == AbilityType.COSMIC_TOKEN_GENERATOR) {
                SlotBotOpenerGUI slotBotOpenerGUI = new SlotBotOpenerGUI(data, e.getClickedBlock(), p);
                slotBotOpenerGUI.getCustomBlockGui().open(p);
            }
        }
    }


}
