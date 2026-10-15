package com.conaxgames.customskulls.struct;

import com.conaxgames.customskulls.CoreSkulls;
import com.conaxgames.customskulls.ability.AbilityData;
import com.conaxgames.customskulls.blocks.impl.MobGrinder;
import com.conaxgames.customskulls.data.SaveCustomBlock;
import com.conaxgames.customskulls.util.CC;
import com.conaxgames.customskulls.util.ChunkUtil;
import com.cosmicpvp.cosmicutils.utils.EconomyUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import de.inventivegames.particle.ParticleEffect;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Blaze;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class MobGrinderManager {
    private final Map<String, AbilityData> activeMobGrinders;

    public MobGrinderManager() {
        this.activeMobGrinders = new HashMap<>();
    }

    public AbilityData get(String uuid) {
        return AbilityData.getByUUID(uuid);
    }

    public boolean canActivate(Player player, String uuid) {
        boolean blazeFound = false;

        AbilityData data = AbilityData.getByUUID(uuid);
/*        Chunk chunk = ChunkUtil.getIfLoaded(data.getLocation().getBlockX() >> 4, data.getLocation().getBlockZ() >> 4, data.getLocation().getWorld());
        if (chunk == null) return false;
        for (Entity entity : ChunkUtil.getEntities(chunk)) {
            if (entity instanceof Blaze) {
                blazeFound = true;
                break;
            }
        }
        if (!blazeFound) {
            player.sendMessage(CC.translate("&c&l(!)&c Could not activate auto mob grinder because there is no nearby blaze in this chunk"));
            return false;
        }*/

        if (!EconomyUtils.hasBalance(player, data.getAbilityType().getPricePerAction())) {
            player.sendMessage(CC.translate("&c&l(!)&c You do not have enough money to reactivate this custom block."));
            return false;
        }

        return true;
    }

    public void activate(Player player, String uuid) {
        if (!canActivate(player, uuid)) return;
        AbilityData data = AbilityData.getByUUID(uuid);
        EconomyUtils.withdrawBalance(player, data.getAbilityType().getPricePerAction());
        SaveCustomBlock.saveCustomBlock((Player) player, data.getAbilityType(), data.getLocation().getBlock());
        player.sendMessage(CC.translate("&a&l(!)&a Reactivated the CustomBlock"));
        player.sendMessage(CC.translate("&c&l- $" + NumberUtils.formatMoney(data.getAbilityType().getPricePerAction())));
        player.playSound(player.getLocation(), Sound.VILLAGER_YES, 20, 20);
        try {
            ParticleEffect.WITCH_MAGIC.sendToPlayers(Bukkit.getOnlinePlayers(), data.getLocation(), 2, 2, 2, 0.05f, 60);
        } catch (Exception e) {
            e.printStackTrace();
        }
        get(uuid).setLastActivated(System.currentTimeMillis());
        //rescanBlaze(uuid);
        activeMobGrinders.put(uuid, get(uuid));
    }

    public void deActivate(String uuid) {
        if (get(uuid).isActive()) {
            activeMobGrinders.remove(uuid);
        }
    }

    public void rescanBlaze(String uuid) {
        AbilityData data = AbilityData.getByUUID(uuid);
        if (data == null) return;

        Chunk chunk = ChunkUtil.getIfLoaded(data.getLocation().getBlockX() >> 4, data.getLocation().getBlockZ() >> 4, data.getLocation().getWorld());
        if (chunk == null) return;
        for (Entity entity : ChunkUtil.getEntities(chunk)) {
            if (entity instanceof Blaze) {
                data.setBlaze(entity);
                return;
            }
        }
    }


}
