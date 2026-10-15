package com.cosmicpvp.cosmicutils.itembuilder;

import com.compilex.CosmicDungeons.DungeonsAPI;
import com.compilex.CosmicDungeons.DungeonsRewardsAPI;
import com.cosmicpvp.cosmicutils.CosmicUtils;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import me.ifamasssxd.cosmicconquest.CosmicConquestAPI;
import me.ifamasssxd.cosmicmasks.CosmicMasks;
import me.ifamasssxd.cosmicmasks.MaskType;
import org.arkhamnetwork.Arkkit.patches.*;
import org.arkhamnetwork.Arkkit.plugins.ArkPlugins;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class CustomItemBuilder
        extends ItemBuilder {
    private CustomItemType type;

    public CustomItemBuilder(CustomItemType type) {
        super((Material) null, 0);
        this.type = type;
    }

    public CustomItemBuilder(CustomItemType type, int amount) {
        super((Material) null, amount);
        this.type = type;
    }

    @Override
    public ItemStack build() {
        ItemStack retr = null;
        try {
            if (this.type == CustomItemType.NAMETAG && ArkPlugins.ITEM_NAMETAG.getArkPlugin().isRunning()) {
                retr = ItemNametag.getNametagItem();

            } else if (this.type == CustomItemType.ITEM_NAMETAG && ArkPlugins.ITEM_NAMETAG.getArkPlugin().isRunning()) {
                retr = ItemNametag.getNametagItem();
            } else if (this.type == CustomItemType.LORETAG && ArkPlugins.LORE_TAG.getArkPlugin().isRunning()) {
                retr = LoreTag.getLoreTagItem();
            } else if ((this.type == CustomItemType.FACTION_CRYSTAL || this.type == CustomItemType.HEROIC_FACTION_CRYSTAL) && CosmicUtils.get().isConquestsEnabled()) {
                retr = this.type == CustomItemType.FACTION_CRYSTAL ? CosmicConquestAPI.createFactionToken((int) 1) : CosmicConquestAPI.createHeroicToken((int) 1);
            } else if (this.type == CustomItemType.TRANSMOG_SCROLL && CosmicUtils.get().isEnchantmentsEnabled()) {
                retr = EnchantmentBook.getTransmogScroll();
            } else if (this.type == CustomItemType.SHOWCASE_EXPANDER && ArkPlugins.SHOWCASE_EXPANDER.getArkPlugin().isRunning()) {
                retr = ShowcaseExpander.getShowcaseExpanderItem();
            } else if (this.type == CustomItemType.ADD_HOME_ITEM && ArkPlugins.ESSENTIALS_ADD_HOMES.getArkPlugin().isRunning()) {
                retr = EssentialsAddHomes.getAddHomeItem();
            } else if (this.type == CustomItemType.WHITE_SCROLL && CosmicUtils.get().isEnchantmentsEnabled()) {
                retr = EnchantmentBook.getWhiteScroll();
            } else if (this.type == CustomItemType.BLACKSCROLL && CosmicUtils.get().isEnchantmentsEnabled()) {
                retr = EnchantmentBook.getBlackScroll();
            } else if (this.type == CustomItemType.HEROIC_BLACKSCROLL && CosmicUtils.get().isEnchantmentsEnabled()) {
                retr = EnchantmentBook.getHeroicBlackscroll((boolean) false);
            } else if ((this.type == CustomItemType.EQUIPMENT_LOOTBOX || this.type == CustomItemType.HEROIC_EQUIPMENT_LOOTBOX) && ArkPlugins.EQUIPMENT_LOOTBOX.getArkPlugin().isRunning()) {
                EquipmentLootboxes boxes = (EquipmentLootboxes) ArkPlugins.EQUIPMENT_LOOTBOX.getArkPlugin();
            } else if (this.type == CustomItemType.GKIT_GENERATOR && ArkPlugins.EQUIPMENT_LOOTBOX.getArkPlugin().isRunning()) {
            } else if (this.type == CustomItemType.MKIT_GENERATOR && ArkPlugins.EQUIPMENT_LOOTBOX.getArkPlugin().isRunning()) {
            } else if ((this.type == CustomItemType.DUNGEON_PORTAL_ABANDONED_SHIP || this.type == CustomItemType.DUNGEON_PORTAL_DESTROYED_OUTPOST) && CosmicUtils.get().isDungeonsEnabled()) {
                retr = DungeonsAPI.get().getDungeon(this.type == CustomItemType.DUNGEON_PORTAL_ABANDONED_SHIP ? 0 : 1).getDungeonPortal();
            } else {
                CosmicMasks masks;
                Double version;
                if (this.type == CustomItemType.HOLY_WHITE_SCROLL && CosmicUtils.get().isDungeonsEnabled()) {
                    return DungeonsRewardsAPI.getHolyWhiteScroll();
                }
                if (this.type == CustomItemType.RANDOM_MASK && CosmicUtils.get().isMasksEnabled() && (version = NumberUtils.parseDoubleOrNull((masks = (CosmicMasks) Bukkit.getPluginManager().getPlugin("CosmicMasks")).getDescription().getVersion().trim())) != null && version >= 1.1) {
                    return MaskType.createRandomMaskItem((boolean) true);
                }
            }
        } catch (Exception e) {
            Bukkit.getLogger().info("Unable to generate " + this.type + " CustomItem!");
            e.printStackTrace();
        }
        if (retr == null) {
            Bukkit.getLogger().info("[CosmicPvPUtils] unable to find valid item for " + this.type.name());
            retr = new ItemStack(Material.DIRT);
        }
        if (this.amount > 0) {
            retr.setAmount(this.amount);
        }
        return retr;
    }

    public static enum CustomItemType {
        NAMETAG,
        LORETAG,
        FACTION_CRYSTAL,
        HEROIC_FACTION_CRYSTAL,
        MYSTERY_MOBSPAWNER,
        TRANSMOG_SCROLL,
        SHOWCASE_EXPANDER,
        ADD_HOME_ITEM,
        WHITE_SCROLL,
        HOLY_WHITE_SCROLL,
        BLACKSCROLL,
        HEROIC_BLACKSCROLL,
        EQUIPMENT_LOOTBOX,
        GKIT_GENERATOR,
        MKIT_GENERATOR,
        DUNGEON_PORTAL_ABANDONED_SHIP,
        DUNGEON_PORTAL_DESTROYED_OUTPOST,
        ITEM_NAMETAG,
        RESET_KIT,
        RANDOM_MASK,
        HEROIC_EQUIPMENT_LOOTBOX;

    }
}

