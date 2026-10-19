package me.kingsavaqe.astroenvoy.listeners;


import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemUtils;
import me.kingsavaqe.astroenvoy.EnvoyPlugin;
import me.kingsavaqe.astroenvoy.utils.CustomItemUtils;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EnvoySummonerListener implements Listener {

    private final EnvoyPlugin plugin;
    public EnvoySummonerListener(EnvoyPlugin envoyPlugin) {
        this.plugin = envoyPlugin;
    }

    @EventHandler
    public void useItem(PlayerInteractEvent event) {
        if(event.getPlayer() == null) return;
        if(event.getPlayer().getItemInHand() == null) return;
        ItemStack itemStack = event.getPlayer().getItemInHand();
        if(!CustomItemUtils.isCrankedCustomItem(itemStack)) return;
        if(!CustomItemUtils.getCrankedCustomItem(itemStack).equals("envoySummoner")) return;
        if(!this.plugin.getEnvoyManager().isActive() || this.plugin.getEnvoyManager().canSpawnEnvoy()) {
            event.setCancelled(true);
            if (this.plugin.getEnvoyManager().tryStartEnvoyEvent()) {
                event.getPlayer().setItemInHand(ItemUtils.decreaseItemStackAmountByOne(itemStack));
            } else {
                event.getPlayer().sendMessage(CC.translate("&cThe envoy cannot start: not enough available crate locations. Your summoner was kept."));
            }
            return;
        }

        event.setCancelled(true);
        event.getPlayer().sendMessage(CC.translate("&c&l(!) &cThere is already an active envoy event"));
        event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.ANVIL_LAND, 2.0F, 2.0F);

    }
}
