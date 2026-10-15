package net.astropvp.core.items.coder;

import com.google.common.collect.Lists;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import net.astropvp.core.AstroCore;
import net.astropvp.storage.StoragePlugin;
import net.astropvp.storage.utils.SaveLoadManager;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class EnchantmentCoderManager extends SaveLoadManager<EnchantmentCoderData> {


    public EnchantmentCoderManager() {
        super(AstroCore.getInstance(), StoragePlugin.getInstance().getGson(), "enchantmentcoderdata/", EnchantmentCoderData.class);
    }


    public List<CustomEnchantment> getEnchantmentsForUser(UUID uuid, Material type) {
        if (!this.contains(uuid.toString()))
            return Lists.newArrayList();
        EnchantmentCoderData data = this.getFromCache(uuid.toString());
        if (!data.getTypeEnchants().containsKey(type))
            return Lists.newArrayList();
        List<CustomEnchantment> enchantments = Lists.newArrayList();
        List<String> ce = data.getTypeEnchants().get(type);
        for (String s : ce)
            enchantments.add(EnchantmentAPI.getEnchantment(s));
        return enchantments;
    }


    public void add(UUID uuid, Material type, List<CustomEnchantment> enchants) {
        List<String> list = Lists.newArrayList();
        enchants.forEach(ce -> list.add(ce.name()));

        EnchantmentCoderData data = contains(uuid.toString()) ? getFromCache(uuid.toString()) : new EnchantmentCoderData(uuid,new HashMap<>());
        data.getTypeEnchants().put(type, list);
        addToCache(data);
    }



}