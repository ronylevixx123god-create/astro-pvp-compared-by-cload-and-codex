package me.astropvp.astropvpskins.enums.temp;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.objects.skins.belts.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum Belt {
    EASTER_BELT(CC.RedB + "Rainbow Belt", ItemSkinType.BELT, new EasterBeltSkin(), Collections.singletonList(CC.Red + "-7.5% Incoming DMG"), true),
    VIRUS_BELT(CC.DarkGreenB + "First Aid Fannypack", ItemSkinType.BELT, new VirusBeltSkin(), Collections.singletonList(CC.Red + "50% chance to negate Blackout proc"), true),
    CORRUPT_BELT_POWER(CC.DarkPurpleB + "Corrupt Belt", ItemSkinType.BELT, new CorruptPowerBeltSkin(), Collections.singletonList(CC.Red + "Godly Overload V"), true),
    CORRUPT_BELT_UTILITY(CC.DarkPurpleB + "Utility Belt", ItemSkinType.BELT, new CorruptUtilityBeltSkin(), Arrays.asList(CC.Red + "Phoenix cannot be blocked", CC.Red + "Auto Feign Death (with Phoenix)"), true),
    CUPID_BELT(CC.RedB + "Divine Sash", ItemSkinType.BELT, new CupidBeltSkin(), Collections.singletonList(CC.Red + "Auto Bless"), true),
    BACK_TO_SCHOOL_2021_BELT(CC.translate("&8&lBandolier Belt"), ItemSkinType.BELT, new BandolierBeltSkin(), Arrays.asList(CC.Red + "Bypass enemy Witch Hat", CC.Red + "Immune to Deep Bleed Slowness", CC.Red + "Limits an enemy stackable enchant level by 50%"), true),
    SPRING_BREAK_2022_BELT(CC.translate("&e&lSplash Zone Belt"), ItemSkinType.BELT, new SplashZoneBelt(), Arrays.asList(
            CC.translate("&cAuto Bless"),
            CC.translate("&c-20% Silence duration"),
            CC.translate("&c+10% Outgoing Damage to Silenced enemies")
    ), true),
    SPRING_BREAK_2022_MUTATED_BELT(CC.translate("&2&l&nMutated&r &e&lSplash Zone Belt"), ItemSkinType.BELT, new MutatedSplashZoneBelt(), Arrays.asList(
            CC.translate("&cAuto Bless"),
            CC.translate("&c-20% Silence duration"),
            CC.translate("&c+10% Outgoing Damage to Silenced enemies"),
            CC.translate("&2Immune to Lifesteal"),
            CC.translate("&2Immune to Opening Combo")

    ), true),
    VALENTINES_2022_BELT(CC.translate("&c&lBling Belt"), ItemSkinType.BELT, new ValentinesBelt(), Arrays.asList(CC.Red + "+8 % Outgoing Damage", CC.Red + "+15% Pet Leash Negation", CC.Red + "10% Chance to block Rogue"), true),
    VALENTINES_2022_MUTATED_BELT(CC.translate("&2&l&nMutated&r &c&lBling Belt"), ItemSkinType.BELT, new MutatedValentinesBelt(), Arrays.asList(CC.Red + "+8 % Outgoing Damage", CC.Red + "+15% Pet Leash Negation", CC.Red + "10% Chance to block Rogue", CC.DarkGreen + "Soul Master VI"), true),
    JULY4TH_2022_BELT(CC.translate("&6&lCowboy Chaps"), ItemSkinType.BELT, new CowboyChaps(), Arrays.asList(
            CC.translate("&cImmune to Stitches Pet Ability"),
            CC.translate("&c+6% Outgoing Damage"),
            CC.translate("&cGodly Overload IV"),
            CC.translate("&cAuto Bless")
    ), true),
    THANKSGIVING_2022_BELT(CC.translate("&e&lMatrix Master Belt"), ItemSkinType.BELT, new MatrixMasterSkin(), Arrays.asList(
            CC.translate("&cLimits enemy stackable enchant level by 50%"),
            CC.translate("&cImmune to World Destroyer Pet"),
            CC.translate("&c10% chance to block Soul Enchant Damage Multipliers"),
            CC.translate("&c25% chance to block Ultra Rage")), true),
    ;


    private final String display;

    private final ItemSkinType itemSkinType;

    private final List<String> lore;

    private final ItemSkin skin;


    @Getter
    private final boolean enabled;



    public List<String> getLore() {
        return lore;
    }

    Belt(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled) {
        this.lore = new ArrayList<>();
        this.display = display;
        this.itemSkinType = itemSkinType;
        this.skin = skin;
        this.lore.addAll(extra);
        this.enabled = enabled;
    }


    public String getDisplay() {
        return this.display;
    }

    public String getDisplayName() {
        return CC.WhiteB + "Item Skin (" + this.display + CC.WhiteB + ")";
    }

    public String getName() {
        return this.display;
    }

    public String getAppliedLore() {
        return CC.WhiteB + "ITEM SKIN: " + this.display;
    }

    public Material getMaterial() {
        return Material.EYE_OF_ENDER;
    }

    public ItemSkinType getItemSkinType() {
        return this.itemSkinType;
    }

    public ItemSkin getItemSkin() {
        return this.skin;
    }

    public ItemStack getItem() {

        String name = getCustomName();
        ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.WhiteB + "Item Skin (" + getName() + CC.WhiteB + ")");
        List<String> lore = new ArrayList<>();
        for (String line : getLore()) {
            lore.add(line);
        }
        lore.add("");
        lore.add(CC.GrayI + "Attach this skin to any " + CC.WhiteU + getItemSkinType().getModifiedName());
        lore.add(CC.Gray + "to override its visual appearance.");
        lore.add("");
        lore.add(CC.RedB + "NOTE: " + CC.Red + "Requires the Cosmic Client");
        lore.add(CC.Red + "to render custom item skins.");
        lore.add(CC.Gray + "* Download at " + CC.GrayU + "discord.astropvp.net");
        lore.add("");
        lore.add(CC.Gray + "Drag n' Drop onto item to attach.");
        lore.add(CC.Gray + "Right-Click item to detach skin.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        cosmicData.setString("itemSkinType", name);
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");

        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        wrapper.setString("random", UUID.randomUUID().toString());
        wrapper.setString("cosmicType", "item_skin");

        return wrapper.build();
    }

    public ItemStack applyBackpack(ItemStack item) {
        String name = getCustomName();
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType")) {
            cosmicData.setString("itemSkinType", name);
        } else {
            String[] current = cosmicData.getString("itemSkinType").split(",");
            List<String> strings = new ArrayList<>(Arrays.asList(current));
            strings.add(name);
            String s = Arrays.toString(strings.toArray());
            s = s.replace("[", "");
            s = s.replace("]", "");
            s = s.replace(" ", "");

            cosmicData.setString("itemSkinType", s);
        }
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");

        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

    public ItemStack apply(ItemStack item) {
        String name = getCustomName();
        ItemMeta meta = item.getItemMeta();
        List<String> lore = (meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
        lore.add(CC.WhiteB + "ITEM SKIN: " + getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType")) {
            cosmicData.setString("itemSkinType", name);
        } else {
            String[] current = cosmicData.getString("itemSkinType").split(",");
            List<String> strings = new ArrayList<>(Arrays.asList(current));
            strings.add(name);
            String s = Arrays.toString(strings.toArray());
            s = s.replace("[", "");
            s = s.replace("]", "");
            s = s.replace(" ", "");
            cosmicData.setString("itemSkinType", s);
        }
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");
        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

    private boolean shouldBeMutated() {
        return name().startsWith("MMM_") || name().contains("MUTATED");
    }

    private String getCustomName() {
        if(name().startsWith("MMM_")) {
            return name().replace("NEXT_MUTATED,", "");
        }
        return name();
    }

    public ItemStack getMultiSkin(Belt type) {
        ItemStack item = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(CC.WhiteB + "Multi Item Skin (" + getName() + ", " + type.getName() + CC.WhiteB + ")");
        List<String> lore = new ArrayList<>();
        lore.add(type.display);
        lore.addAll(type.getLore());
        lore.add("");
        lore.add(display);
        lore.addAll(getLore());
        lore.add("");
        lore.add(CC.GrayI + "Attach this skin to any " + CC.WhiteU + getItemSkinType().getModifiedName());
        lore.add(CC.Gray + "to override its visual appearance.");
        lore.add("");
        lore.add(CC.RedB + "NOTE: " + CC.Red + "Requires the Cosmic Client");
        lore.add(CC.Red + "to render custom item skins.");
        lore.add(CC.Gray + "* Download at " + CC.GrayU + "cosmicclient.com");
        lore.add("");
        lore.add(CC.Gray + "Drag n' Drop onto item to attach.");
        lore.add(CC.Gray + "Right-Click item to detach skin.");
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        cosmicData.setString("itemSkinType", name() + "," + type.name());
        if (shouldBeMutated()) {
            cosmicData.setString("tintColor", "-10237440");

        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        wrapper.setString("random", UUID.randomUUID().toString());
        wrapper.setString("cosmicType", "item_skin");

        return wrapper.build();
    }

    public ItemStack apply(ItemStack item, Belt type) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = (meta.getLore() == null) ? new ArrayList<>() : meta.getLore();
        lore.add(CC.WhiteB + "ITEM SKIN: " + getName());
        lore.add(CC.WhiteB + "ITEM SKIN: " + type.getName());
        meta.setLore(lore);
        item.setItemMeta(meta);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        String[] current;
        if (cosmicData.hasKey("itemSkinType")) {
            current = cosmicData.getString("itemSkinType").split(",");
        } else {
            current = new String[0];
        }
        List<String> strings = new ArrayList<>(Arrays.asList(current));
        strings.add(name());
        strings.add(type.name());
        String s = Arrays.toString(strings.toArray());
        s = s.replace("[", "");
        s = s.replace("]", "");
        s = s.replace(" ", "");
        cosmicData.setString("itemSkinType", s);
        if (this.name().contains("MUTATED")) {
            cosmicData.setString("tintColor", "-10237440");

        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

    public ItemStack applyNoDisplay(ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.hasKey("cosmicData") ? new NBTTagCompoundBuilder(wrapper.getNBTBase("cosmicData")) : new NBTTagCompoundBuilder();
        if (!cosmicData.hasKey("itemSkinType")) {
            cosmicData.setString("itemSkinType", name());
        } else {
            String[] current = cosmicData.getString("itemSkinType").split(",");
            List<String> strings = new ArrayList<>(Arrays.asList(current));
            strings.add(name());
            String s = Arrays.toString(strings.toArray());
            s = s.replace("[", "");
            s = s.replace("]", "");
            s = s.replace(" ", "");

            cosmicData.setString("itemSkinType", s);
        }
        if (this.name().contains("MUTATED")) {
            cosmicData.setString("tintColor", "-10237440");

        }
        wrapper.setNBTBase("cosmicData", cosmicData.get());
        return wrapper.build();
    }

}