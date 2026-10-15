package net.astropvp.armorsets.struct;

import com.cosmicpvp.cosmicutils.itembuilder.ItemBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import net.astropvp.armorsets.struct.ArmorSet;
import net.astropvp.armorsets.struct.sets.*;
import net.minecraft.server.v1_8_R3.NBTBase;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter
public enum ArmorSetType {
    PHANTOM(CC.translate("&c&lPhantom"), new PhantomArmorSet(), CC.Red, 14294340, Material.COAL),
    DRAGON(CC.translate("&e&lDragon"), new DragonArmorSet(), CC.Yellow, 0xFFEE00, Material.BLAZE_POWDER),
    ENGINEER(CC.translate("&6&lEngineer"), new EngineerArmorSet(), CC.Gold, 15176967, Material.LEVER),
    GRINDER(CC.translate("&8&lGrinder"), new GrinderArmorSet(), CC.DarkGray, 0x404040, Material.EXP_BOTTLE),
    SUPREME(CC.translate("&4&lSupreme"), new SupremeArmorSet(), CC.DarkRed, 8264475, Material.DIAMOND_CHESTPLATE),
    DIMENSIONAL_TRAVELER(CC.translate("&5&lDimensional Traveler"), new TravelerArmorSet(), CC.DarkPurple,12001978, Material.COMPASS),
    YETI(CC.translate("&b&lYeti"), new YetiArmorSet(), CC.Aqua, 518113, Material.SNOW_BALL),
    MOTHER_OF_YIJKI(CC.translate("&f&lMother of Yijki"), new YijkiArmorSet(), CC.White,0xFFFFFF, Material.NETHER_STALK),
    KOTH(CC.translate("&f&l&k;&r &c&lK&6&l.&e&lO&a&l.&b&lT&5&l.&d&lH &f&l&k;&r"), new KothArmorSet(), CC.Aqua,31999, Material.STICK),
    ASTRO(CC.translate("&a&lAstro"), new AstroArmorSet(), CC.Green, 5765888, Material.FIREBALL),
    ANCIENT(CC.translate("&3&lAncient"), new AncientArmorSet(), CC.DarkAqua, 3650995, Material.QUARTZ),
    CHAMPION(CC.translate("&6&lChampion"), new ChampionArmorSet(), CC.Gold, 15176967, Material.GOLD_NUGGET),
    //FROSTY(CC.translate("&b&lF&3&lr&b&lo&3&ls&b&lt&3&ly"), new FrostyArmorSet(), CC.Aqua, 0x404040, Material.ICE),
    ULTRA(CC.translate("&7&lUltra"), new UltraArmorSet(), CC.Gray, 0xA0A0A0, Material.TORCH),
    APOLLO(CC.translate("&f&lApollo"), new ApolloArmorSet(), CC.White, 0xFFFFFF, Material.NETHER_STAR),
    RANGER(CC.translate("&a&lRanger"), new RangerArmorSet(), CC.Green, 0xAAFF00, Material.ARROW),


    ;

    //OMNI Rbg -> 0x0027FF


    private final String display;
    private final ArmorSet armorSet;
    private final String color;
    private final int heroicRGB;
    private final Material displayItem;

    private ArmorSetType(String display, ArmorSet armorSet, String color, int heroicRGB, Material displayItem) {
        this.display = display;
        this.armorSet = armorSet;
        this.color = color;
        this.heroicRGB = heroicRGB;
        this.displayItem = displayItem;
    }

    public String getFirstDisplayNameLetter() {
        if (name().contains("MUTATED")) {
            return  this.color + CC.Bold + CC.Underline + name().split("MUTATED_")[1].charAt(0);
        } else {
            return this.color + CC.Bold + name().charAt(0);

        }
    }

    public List<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        ArmorSet set = this.getArmorSet();
        items.add(set.getHelmet());
        items.add(set.getChestplate());
        items.add(set.getLeggings());
        items.add(set.getBoots());
        List<ItemStack> newItems = new ArrayList<>();

        for (ItemStack item : items) {

            ItemMeta meta = item.getItemMeta();
            List<String> lore = meta.getLore();
            lore.add("");
            lore.addAll(set.getArmorLore());
            meta.setLore(lore);
            item.setItemMeta(meta);
            NBTWrapper wrapper = new NBTWrapper(item);


            NBTTagCompoundBuilder builder = wrapper.getCompoundOrCreate("cosmicData");
            builder.setString("armorSetType", this.name());
            wrapper.setNBTBase("cosmicData", (NBTBase)builder.get());
            newItems.add(wrapper.build());
        }

        if (set instanceof ArmorSetWeapon) {
            newItems.addAll((((ArmorSetWeapon) set).getWeapons()));
        }

        return newItems;
    }


}
