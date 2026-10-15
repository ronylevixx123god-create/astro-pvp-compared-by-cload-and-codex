package org.arkhamnetwork.enchantments.enchantments.heroic;

import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import me.astropvp.astropvpskins.utils.DamageIndicatorUtils;
import org.arkhamnetwork.enchantments.Enchantments;
import org.arkhamnetwork.enchantments.events.CustomEnchantProcOnEntityEvent;
import org.arkhamnetwork.enchantments.listeners.CustomEnchantListener;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HeroicMetaphysical extends HeroicCustomEnchantment {
    private Map<UUID, Integer> equippedMetaphysical;

    public HeroicMetaphysical() {
        super("Polymorphic Metaphysical", "Metaphysical", GeneralUtil.boots, 1);
        this.equippedMetaphysical = new HashMap<UUID, Integer>();
        this.max = 4;
        Bukkit.getPluginManager().registerEvents(new HeroicMetaphysicalListener(this), (Plugin) Enchantments.getInstance());
    }

    public void applyEquipEffect(final Player player, final int enchantLevel) {
        final Integer stored = this.equippedMetaphysical.get(player.getUniqueId());
        if (stored != null && stored >= enchantLevel) {
            return;
        }
        this.equippedMetaphysical.put(player.getUniqueId(), enchantLevel);
        player.setMetadata("metaphysicalEnchant", new FixedMetadataValue((Plugin) Enchantments.getInstance(), enchantLevel));
    }

    public void applyUnequipEffect(final Player player, final int enchantLevel) {
        final Integer stored = this.equippedMetaphysical.get(player.getUniqueId());
        if (stored != null && stored <= enchantLevel) {
            this.equippedMetaphysical.remove(player.getUniqueId());
            player.removeMetadata("metaphysicalEnchant", (Plugin) Enchantments.getInstance());
        }
    }

    public Map<UUID, Integer> getEquippedMetaphysical() {
        return this.equippedMetaphysical;
    }

    class HeroicMetaphysicalListener extends CustomEnchantListener<HeroicMetaphysical> {
        public HeroicMetaphysicalListener(final HeroicMetaphysical enchant) {
            super(enchant);
        }

        @EventHandler
        public void onIceAspectProc(final CustomEnchantProcOnEntityEvent event) {
            if (event.getEnchant() == Enchantments.getInstance().getIceAspectEnchant() && event.getTarget() != null) {
                final Integer damagedEquipped = ((HeroicMetaphysical) this.customEnchant).getEquippedMetaphysical().get(event.getTarget().getUniqueId());
                if (damagedEquipped != null) {
                    final double chance = 0.2 * damagedEquipped;
                    if (HeroicMetaphysical.this.random.nextDouble() <= chance) {
                        if (event.getTarget() instanceof Player) {
                            ((Player) event.getTarget()).sendMessage(CC.LightPurpleB + "** POLYMORPHIC METAPHYSICAL (" + CC.Gray + "Ice Aspect blocked!" + CC.LightPurpleB + ") **");
                            DamageIndicatorUtils.sendDamageIndicator((Player) event.getTarget(), CC.LightPurpleB + "* POLYMORPHIC METAPHYSICAL *", "METAPHYSICAL");


                        }
                    }
                }
            }
        }
    }
}
