package me.ifamasssxd.cosmicfactionbanners.struct;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import com.massivecraft.factions.Faction;
import com.massivecraft.factions.Factions;
import me.ifamasssxd.cosmicfactionbanners.CosmicFactionBanners;
import me.ifamasssxd.cosmicfactionbanners.managers.BannerManager;
import me.ifamasssxd.cosmicskycustoms.struct.CosmicItem;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BannerMeta;

public class BannerItem extends CosmicItem
{
    public BannerItem() {
        super("FACTION_BANNER");
    }
    
    public ItemStack createItem(final Player player, final Object... objects) {
        if (objects.length != 4) {
            return null;
        }
        final Faction faction = (Faction)((objects[0] instanceof Faction) ? objects[0] : Factions.getInstance().getFactionById(objects[0].toString()));
        if (faction == null) {
            return null;
        }
        final Player pla = (Player)((objects[1] instanceof Player) ? objects[1] : player);
        final boolean white = objects[2] instanceof Boolean && (boolean)objects[2];
        final String freeReason = (objects[3] instanceof String) ? ((String)objects[3]) : null;
        final NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder().setString("fId", faction.getId()).setString("fTag", faction.getTag());
        if (freeReason != null) {
            builder.setBoolean("noSoulCost", Boolean.valueOf(true));
        }
        final ItemStack retr = new NBTWrapper(new ItemBuilder(BannerManager.bannerMaterial).name(CC.DarkPurpleB + "Faction Banner").lore(new String[] { "", CC.DarkPurpleB + "COST: " + CC.White + ((freeReason != null) ? (CC.GreenB + "FREE (" + CC.Gray + freeReason + CC.GreenB + ")") : (CosmicFactionBanners.get().getConfig().getInt("soulCost") + " souls")), CC.DarkPurpleB + "DURATION: " + CC.White + CosmicFactionBanners.get().getConfig().getInt("bannerCooldown") + "s", "", CC.Gray + "Place down to create an", CC.Gray + "inter-dimensional rift", CC.Gray + "that allows faction members", CC.Gray + "to travel to your location", CC.Gray + "by using the /f assist command." }).build()).setString("cosmicType", "FACTION_BANNER").setNBTBase("cosmicData", builder.get()).build();
        if (white) {
            final BannerMeta meta = (BannerMeta)retr.getItemMeta();
            meta.setBaseColor(DyeColor.WHITE);
            meta.setPatterns(Lists.newArrayList());
            retr.setItemMeta(meta);
            retr.setDurability((short)meta.getBaseColor().getDyeData());
        }
        return retr;
    }
}
