package net.astropvp.core.items;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.cosmicpvp.cosmicutils.utils.NumberUtils;
import com.massivecraft.factions.*;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class MagicalLavaBucket implements Listener {
    public static ItemStack createMagicalLavaBucket(int lavaBucketUses) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.LAVA_BUCKET, 1, (short) 0
        );
        itemBuilder.setName(CC.translate("&c&lMagical Lava Bucket&8 (&7" + NumberUtils.formatMoney(lavaBucketUses) + " uses&8)"));

        itemBuilder.addLoreLines(
                "",
                CC.translate("&7This lava bucket is capable of"),
                CC.translate("&7refilling its self when used"),
                "",
                CC.translate("&c* &7Usage: &fRight-Click on a block"));
        ItemStack itemStack = itemBuilder.build();
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.DURABILITY, 3, true);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);


        NBTWrapper wrapper = new NBTWrapper(itemStack);
        NBTTagCompoundBuilder builder = new NBTTagCompoundBuilder();
        builder.setString("randomUUID", String.valueOf(UUID.randomUUID()));
        builder.setString("extinctCustomItem", "magicalLavaBucket");
        builder.setInt("lavaBucketUses", lavaBucketUses);
        wrapper.setNBTBase("bucketData", builder.get());

        return wrapper.build();
    }

    @EventHandler
    public void onLavaBucketPlace(PlayerBucketEmptyEvent event) {
        Player player = event.getPlayer();
        NBTWrapper nbtItem = new NBTWrapper(player.getItemInHand());

        BlockFace blockFace = event.getBlockFace();
        Location location = event.getBlockClicked().getLocation().add(blockFace.getModX(), blockFace.getModY(), blockFace.getModZ());

        FLocation fLocation = new FLocation(location);
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        Faction faction = fPlayer.getFaction();

        if (nbtItem.hasKey("bucketData")) {
            NBTTagCompoundBuilder compound = new NBTTagCompoundBuilder(nbtItem.getNBTBase("bucketData"));

            if (compound.getString("extinctCustomItem").equalsIgnoreCase("magicalLavaBucket")) {
                event.setCancelled(true);

                if (!Board.getInstance().getFactionAt(fLocation).isWilderness() &&
                        Board.getInstance().getFactionAt(fLocation) != faction) {
                    event.getPlayer().setItemInHand(nbtItem.build());
                    return;
                }
                event.getBlockClicked().getRelative(event.getBlockFace()).setType(Material.LAVA);
                int uses = compound.getInt("lavaBucketUses");
                if (uses <= 1) {
                    player.getInventory().setItemInHand(new ItemStack(Material.BUCKET));
                    player.updateInventory();
                } else {
                    player.getInventory().setItem(player.getInventory().getHeldItemSlot(), createMagicalLavaBucket(uses - 1));
                }
            }
        }
    }
}
