package net.astropvp.core.objects;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.UUID;

public abstract class EnchantmentCrystal {

    public abstract EnchantmentCrystalType getType();

    public ItemStack getItem() {
        ItemStack item = new ItemStack(Material.INK_SACK, 1, getType().getData());
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(getType().getChatColor() + CC.Bold + "Enchantment Crystal " + CC.Gray + "[" + getType().getName() + "]");
        List<String> lore = Lists.newArrayList();
        lore.add(CC.Gray + "Apply this enchantment crystal to receive");
        lore.add(CC.Gray + "the enchantment " + getType().getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("conaxData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("conaxData")) : new NBTTagCompoundBuilder();
        cosmicData.setString("enchantmentCrystalType", getType().toString());
        wrapper.setNBTBase("conaxData", cosmicData.get());
        wrapper.setString("random", UUID.randomUUID().toString());
        wrapper.setString("cosmicType", "enchantmentCrystal");
        return wrapper.build();
    }

}
