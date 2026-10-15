package me.astropvp.astropvpskins.enums.temp;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.objects.skins.amulets.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum Amulet {
    HALLOWEEN_AMULET(CC.GoldB + "Silent Death", ItemSkinType.AMULET, new SilentDeathSkin(), Arrays.asList(CC.Red + "5% chance to afflict enemy", CC.Red + "with Silent Death ability", CC.Gray + " (Enemy will receive 20% Incoming DMG", CC.Gray + " until caster has been hit 5 times", CC.Gray + " or 15 seconds has elapsed.)", CC.Red + "Immune to Silent Death ability", CC.Red + "+3% Outgoing Damage"), true),
    CORRUPT_AMULET_CORRUPTION(CC.DarkPurpleB + "Amulet of Corruption", ItemSkinType.AMULET, new CorruptAmuletCorruptionSkin(), Collections.singletonList(CC.Red + "Immune to enemy armor crystals of same type on attached item."), true),
    CORRUPT_AMULET_DESTRUCTION(CC.DarkPurpleB + "Amulet of Destruction", ItemSkinType.AMULET, new CorruptAmuletDestructionSkin(), Collections.singletonList(CC.Red + "+7.5% Outgoing Damage"), true),
    THANKSGIVING_2021_AMULET(CC.translate("&e&lThe Crumble"), ItemSkinType.AMULET, new TheCrumbleSkin(), Arrays.asList(CC.Red + "7.5% chance to receive -2.5% Incoming", CC.Red + "Damage (up to a max of -15%) for 8s", CC.Red + "when receiving Divine Immolation damage", CC.Red + "Immune to (Obsidian) Guardians", CC.Red + "5% chance to silence your enemy's", CC.Red + "Phoenix for 6s when they're under 15 HP", CC.Red + "Immune to enemy Amulet of Corruption"), true),
    SUMMER_2021_AMULET(CC.translate("&c&lLuau Lei"), ItemSkinType.AMULET, new LuauLei(), Arrays.asList(CC.translate("&c+25% Outgoing Soul Enchant damage"), CC.translate("&cChance to block Soul Enchant Damage Modifiers")), true),
    RUINS_AMULET(CC.translate("&3&lLost Amulet"), ItemSkinType.AMULET, new LostAmulet(), Arrays.asList(CC.translate("&c50% Neutralize Negation"), CC.translate("&c-5% Incoming Damage"), CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.1x")), true),
    CHRISTMAS_AMULET_SNOWFLAKE(CC.RedB + "Preserved Snowflake", ItemSkinType.AMULET, new PreservedSnowflakeSkin(), Arrays.asList(CC.Red + "Immune to Soul Tether, Soul Trap", CC.Red + "Immune to Nature's Wrath", CC.Red + "Able to see invisible player", CC.Gray + " (Feign Death, Undead Ruse)"), true),
    EASTER_2022_AMULET(CC.translate("&e&lCheshire Grin"), ItemSkinType.AMULET, new Grin(), Arrays.asList(
            CC.translate("&c-30% Incoming Rage Damage"),
            CC.translate("&cImmune to Divine Light Enchant"),
            CC.translate("&c-5% Incoming damage")
    ), true),
    EASTER_2022_MUTATED_AMULET(CC.translate("&2&l&nMutated&r &e&lCheshire Grin"), ItemSkinType.AMULET, new MutatedGrin(), Arrays.asList(
            CC.translate("&c-30% Incoming Rage Damage"),
            CC.translate("&cImmune to Divine Light Enchant"),
            CC.translate("&c-5% Incoming damage"),
            CC.translate("&2Immune to Stitches & Skrrt Pet")
    ), true),
    VALENTINES_2021_AMULET(CC.RedB + "Rose Amulet", ItemSkinType.AMULET, new RoseAmuletSkin(), Arrays.asList(CC.Red + "Immune to Snowflake Axe skin abilities", CC.Red + "-50% damage from enemy Rage", CC.Red + "Automatic Phoenix Feather (30m cooldown)"), true),
    HALLOWEEN_2022_AMULET(CC.translate("&4&lMomento Mori"), ItemSkinType.AMULET, new MomentoMoriSkin(), Arrays.asList(
            CC.translate("&cImmune to Rogue Enchant"),
            CC.translate("&c30 Chance to block Champion Ability"),
            CC.translate("&c+5% Outgoing Damage to players wielding Diamond Weapons")
    ), true),
    CHRISTMAS_2022_AMULET_ACTIVE(CC.translate("&b&lIcicle Amulet"), ItemSkinType.AMULET, new IcicleSkin(), Arrays.asList(
            CC.translate("&cImmune to enemy Amulet of Corruption"),
            CC.translate("&cImmune to Divine Immolation"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&c+15% Outgoing Soul Enchant Damage"),
            CC.translate("&c-25% damage from enemy Rage")), true),

    VALENTINES_2023_AMULET(CC.translate("&5&lLucifer Amulet"), ItemSkinType.AMULET, new LuciferAmulet(), Arrays.asList(
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c")), true),
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

    Amulet(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled) {
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

    public ItemStack getMultiSkin(Amulet type) {
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

    public ItemStack apply(ItemStack item, Amulet type) {
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