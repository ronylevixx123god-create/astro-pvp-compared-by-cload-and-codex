package me.astropvp.astropvpskins.enums.temp;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.objects.skins.helmets.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum Helmet {

    PEPE_HELMET(CC.BlueB + "Pepe Helmet", ItemSkinType.HELMET, new PepeHelmetSkin(), Collections.emptyList(), true),
    SANTA_HAT(CC.RedB + "Santa Hat", ItemSkinType.HELMET, new SantaHatSkin(), Arrays.asList(CC.Red + "+5% DMG", CC.Red + "-5% DMG"), true),
    CORRUPT_HELMET(CC.DarkPurpleB + "Corrupt Cosmonaut Visor", ItemSkinType.HELMET, new CorruptHelmetSkin(), Arrays.asList(CC.Red + "50% Chance to block Neutralize", CC.Red + "50% Silence Duration"), true),
    HALLOWEEN_HELMET(CC.GoldB + "Candy Collector", ItemSkinType.HELMET, new CandyCollectorSkin(), Arrays.asList(CC.Red + "-20% incoming damage from enemies wielding swords", CC.Red + "1% chance to activate witcher ability"), true),
    RUINS_HELMET(CC.translate("&3&lLeviathan Trophy"), ItemSkinType.HELMET, new LeviathanTrophySkin(), Arrays.asList(CC.translate("&cIncreased chance to drop Ancient Scrolls"), CC.translate("&cwhen killing Guardians in /ruins"), CC.translate("&cImmune to enemy Amulet of Corruption"), CC.translate("&c+1 Max Heart"), CC.translate("&c3% chance to silence enemy Soul Enchants"), CC.translate("  &7(7x7 blocks for 7s)")), true),
    VALENTINES_2021_HELMET(CC.translate("&c&lHeart Halo"), ItemSkinType.HELMET, new HearthHaloSkin(), Arrays.asList(CC.translate("&cPassively removes 1 Bleedstack every 2s"), CC.translate("&cImmune to Soul Harvester"), CC.translate("&c1.25% (Divine) Enlighted Healing")), true),
    BACK_TO_SCHOOL_2021_HELMET(CC.translate("&6&lPylon Head"), ItemSkinType.HELMET, new PylonHeadSkin(), Arrays.asList(CC.Red + "Reduces the duration of active Silence", CC.Red + "or Blackout by 40% on outgoing damage.", CC.Red + "Limits enemy Amulet of Corruption so that it", CC.Red + "affects defensive Armor Crystal attributes only"), true),
    CHRISTMAS_HELMET_STAR(CC.translate("&a&lNorth Star"), ItemSkinType.HELMET, new NorthStarSkin(), Arrays.asList(CC.translate("&c33% Chance to negate Monopoly Mask"), CC.translate("&c-33% enemy Blackout duration"), CC.translate("&cBlocks all enemy (Guided) Rocket Escape"), CC.translate("&cDivine Enlighted IV")), true),
    NYE_HELMET(CC.translate("&d&lNeon Emotions"), ItemSkinType.HELMET, new NeonEmotionsSkin(), Arrays.asList(CC.translate("&c+6% Outgoing Damage"), CC.translate("&c-2% Incoming Damage"), CC.translate("&cImmune to Slowness Potion Effect")), true),
    SUMMER_2021_HELMET(CC.translate("&6&lTiki Mask"), ItemSkinType.HELMET, new TikiMaskSkin(), Arrays.asList(CC.translate("&cLimites enemy amulet of corruption to 1 set type"), CC.translate("&c25% chance to block enemy Silence"), CC.translate("&cLimits enemy Silence to 60% duration")), true),
    HALLOWEEN_2021_HELMET(CC.translate("&6&lMineman Muzzle"), ItemSkinType.HELMET, new MinemanMuzzleSkin(), Arrays.asList(CC.Red + "25% chance to curse enemies with +33% Incoming", CC.Red + "Damage when their Phoenix procs for 10s", CC.Gray + " (Can only proc once every 10m)", CC.Red + "3.5% chance to negate enemy Cosmic Cape for 10s", CC.Red + "Immune to Banshee RKO", CC.Red + "10% chance to reflect damage to enemies in a 3x3 radius"), true),
    THANKSGIVING_HELMET_WITCH(CC.translate("&e&lWitch Hat"), ItemSkinType.HELMET, new WitchHatSkin(), Arrays.asList(CC.translate("&c-25% Silence duration"), CC.translate("&cImmune to offensive Titan Attributes"), CC.translate("&7(Atlas, Ouranos and Prometheus)"), CC.translate("&cImmune to Trap"), CC.translate("&cImmune to Bloodhound & Banshee Pet"), CC.translate("&c10% chance to deflect incoming damage"), CC.translate("&conto nearby enemy"), CC.translate("&c2% chance to enable a LVL 10"), CC.translate("&cBanshee Pet effect when damaged")), true),
    SPRING_BREAK_2022_HELMET(CC.translate("&a&lUndertow Helmet"), ItemSkinType.HELMET, new UndertowHelmet(), Arrays.asList(
            CC.translate("&c1.5% chance when taking damage to summon a"),
            CC.translate("&c5 block Undertow Field for 15s. You receive:"),
            CC.translate(" &7* Immune to Pets"),
            CC.translate(" &7* +3% Outgoing Soul Damage per enemy stacks"),
            CC.translate("&cAll enemies in the field will receive:"),
            CC.translate(" &7* +1 stack per second for 5s (max 3 stacks)"),
            CC.translate(" &7For each active stack:"),
            CC.translate("  &7* -1HP per second"),
            CC.translate("  &7* +3% Incoming Damage"),
            CC.translate(" &7* Phoenix Silence"),
            CC.translate(" &7* Inventory Pet Silence")
    ), true),
    SPRING_BREAK_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &a&lUndertow Helmet"), ItemSkinType.HELMET, new MutatedUndertowHelmet(), Arrays.asList(
            CC.translate("&c1.5% chance when taking damage to summon a"),
            CC.translate("&c5 block Undertow Field for 15s. You receive:"),
            CC.translate(" &7* Immune to Pets"),
            CC.translate(" &7* +3% Outgoing Soul Damage per enemy stacks"),
            CC.translate(" &2* No EXP Pet Costs"),
            CC.translate("&cAll enemies in the field will receive:"),
            CC.translate(" &7* +1 stack per second for 5s (max 3 stacks)"),
            CC.translate(" &7For each active stack:"),
            CC.translate("  &7* -1HP per second"),
            CC.translate("  &7* +3% Incoming Damage"),
            CC.translate(" &7* Phoenix Silence"),
            CC.translate(" &7* Inventory Pet Silence"),
            CC.translate(" &2* Ragdoll Silence"),
            CC.translate(" &2* Custom Block Effect Silence:"),
            CC.translate("  &7(Cauldron, Frosted Snowman, Heart Cookies,"),
            CC.translate("  &7Pumpkin Pie, Roast Turkey, Teddy Bear")
    ), true),
    CHRISTMAS_2022_HELMET_ACTIVE(CC.translate("&9&lFrost Monster Trophy"), ItemSkinType.HELMET, new FrostMonsterSkin(), Arrays.asList(
            CC.translate("&cImmune to Banshee Pet"),
            CC.translate("&c50% chance to block Divine Immolation"),
            CC.translate("&c-15% Incoming Damage from enemies wielding Axes"),
            CC.translate("&c+5% Outgoing Damage to enemies wielding Swords"),
            CC.translate("&c50% chance to dodge offensive Titan Attributes")), true),
    EASTER_2022_HELMET(CC.translate("&d&lMad Hat"), ItemSkinType.HELMET, new MadHat(), Arrays.asList(
            CC.translate("&c1.25x Banshee Duration"),
            CC.translate("&cImmune to opening combo enchant"),
            CC.translate("&cBypass Utility Belt Skin")
    ), true),
    EASTER_2022_MUTATED_HELMET(CC.translate("&2&l&nMutated&r &d&lMad Hat"), ItemSkinType.HELMET, new MutatedMadHat(), Arrays.asList(
            CC.translate("&c1.25x Banshee Duration"),
            CC.translate("&cImmune to opening combo enchant"),
            CC.translate("&cBypass Utility Belt Skin"),
            CC.translate("&2Chance to fully heal your hp when below 14HP")
    ), true),
    JULY4TH_2022_HELMET(CC.translate("&6&lCowboy Hat"), ItemSkinType.HELMET, new CowboyHat(), Arrays.asList(
            CC.translate("&c+15% Outgoing Crystal Damage"),
            CC.translate("&cImmune to Hero Killer"),
            CC.translate("&cImmune to (Heroic) Lifesteal"),
            CC.translate("&c-20% Damage from enemy (Bloody) Deep Wounds"),
            CC.translate("&c15% Pet Leash Negation")
    ), true),


    HALLOWEEN_2022_HELMET(CC.translate("&4&lGrim Hood"), ItemSkinType.HELMET, new GrimHoodSkin(), Arrays.asList(
            CC.translate("&c"),
            CC.translate("&c"),
            CC.translate("&c")
    ), true),
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

    Helmet(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled) {
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

    public ItemStack getMultiSkin(Helmet type) {
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

    public ItemStack apply(ItemStack item, Helmet type) {
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