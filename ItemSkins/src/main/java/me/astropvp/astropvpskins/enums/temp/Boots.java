package me.astropvp.astropvpskins.enums.temp;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.objects.skins.boots.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum Boots {
    JULY_2021_BOOTS(CC.translate("&c&lBarry's Bolts"), ItemSkinType.BOOTS, new BarrysBoltsSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Immune to Slowness Potion Effect", CC.Red + "-15% damage from enemy (Bloody) Deep Wounds", CC.Red + "Flash Ability", CC.translate("&7 (10% chance when damaged to activate. When"), CC.translate("&7 active, all enemies you hit will have their"), CC.translate("&7 pets and Phoenix enchantment silenced, your"), CC.translate("&7 Soul Tether will do +20% damage and incoming"), CC.translate("&7 soul damage to your allies within a 4x4"), CC.translate("&7radius is reduced by -5%")), true),
    CHRISTMAS_BOOTS_SLIPPERS(CC.translate("&c&lSnowflake Slippers"), ItemSkinType.BOOTS, new SnowflakeSlippersSkin(), Arrays.asList(CC.translate("&cGears IV"), CC.translate("&cImmune to Teleblock"), CC.translate("&cImmune to Slowness Potion Effect"), CC.translate("&cSnowflake Storm Ability"), CC.translate("&7(10% chance to spawn 4x4 snow floor for 10s."), CC.translate("&7While stood on the snow floor:"), CC.translate("&7* You will receive -20% Incoming Damage"), CC.translate("&7* Your allies will receive -5% Incoming Damage"), CC.translate("&7* Your enemies will receive Slowness II)")), true),
    COSMIC_CITIZEN_BOOTS(CC.AquaB + "Moon Boots", ItemSkinType.BOOTS, new MoonBootsSkin(), Collections.singletonList(CC.Red + "Increases armor set bonus/crystal damage by 1.2x"), true),
    HALLOWEEN_2021_BOOTS(CC.translate("&8&lBloodstained Galoshes"), ItemSkinType.BOOTS, new BloodstainedGaloshesSkin(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Immune to World Destroyer Cage", CC.Red + "25% chance to reflect cage back at the user", CC.Red + "Immune to Slownesses/Freezes", CC.Red + "Increases Execute HP threshold to proc by 1.3x", CC.translate("&cPsychotic Pursuit Ability"), CC.translate("&7 (+5% chance to proc Dodge +7 Outgoing"), CC.translate("&7 Damage on successful (Ethereal) Dodge procs"), CC.translate("&7 for a short duration")), true),
    SUMMER_2021_BOOTS(CC.translate("&6&lRoller Skates"), ItemSkinType.BOOTS, new RollerSkates(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cAuto Bless"),
            CC.translate("&cImmune to Skrrt Pet"),
            CC.translate("&c+10% Monopoly Chance")
    ), true),
    VALENTINES_2022_BOOTS(CC.translate("&d&lGolden Boots"), ItemSkinType.BOOTS, new ValentinesBoots(), Arrays.asList(
            CC.Red + "Gears IV",
            CC.Red + "1.1x Critical Damage",
            CC.Red + "10% Chance to negate Incoming Knockback",
            CC.Red + "Immune to Witcher Pet"
    ), true),
    CHRISTMAS_2021_BOOTS(CC.translate("&2&lSleigh Riders"), ItemSkinType.BOOTS, new SleighRiders(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Limit enemy Amulet of Corruption to 1 armor set type", CC.Red + "When affected by any Slowness, you will receive -1%", CC.Red + "Incoming Damage (up to a max of 8%) and +1% Outgoing", CC.translate("&cDamage (up to a max of 15%) per 5% HP lost"), CC.translate("&c3% chance to clear your enemy's rage stacks and"), CC.translate("&csilence their ability to gain more for 7s"), CC.translate("&c7% chance to negate enemy Rogue for 8s when their"), CC.translate("&chealth is below 20 HP")), true),
    SPRING_BREAK_2022_BOOTS(CC.translate("&a&lSink or Swim Flippers"), ItemSkinType.BOOTS, new SinkOrSwipFlippers(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Inverts enemy Ragdoll", CC.Red + "Immune to Freezes", CC.Red + "When wielding a sword:", CC.Gray + " * Deal up to +25% damage if hitting your", CC.Gray + " enemy from behind", CC.Gray + " * 6% chance to silence enemy (Extreme)", CC.Gray + " Insanity and (Vampiric) Devour for 4s.", CC.Red + "Sink Effect", CC.Gray + " * Gain a stack when you negate a freeze.", CC.Gray + " * At 7 stacks, of the next 5 hits affecting", CC.Gray + " your enemy, they will receive Sink Effect", CC.Gray + " tick damage for 15s."), true),
    SPRING_BREAK_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &a&lSink or Swim Flippers"), ItemSkinType.BOOTS, new MutatedSinkOrSwipFlippers(), Arrays.asList(CC.Red + "Gears IV", CC.Red + "Inverts enemy Ragdoll", CC.Red + "Immune to Freezes", CC.Red + "When wielding a sword:", CC.Gray + " * Deal up to +25% damage if hitting your", CC.Gray + " enemy from behind", CC.Gray + " * 6% chance to silence enemy (Extreme)", CC.Gray + " Insanity and (Vampiric) Devour for 4s.", CC.Red + "Sink Effect", CC.Gray + " * Gain a stack when you negate a freeze.", CC.Gray + " * At 7 stacks, of the next 5 hits affecting", CC.Gray + " your enemy, they will receive Sink Effect", CC.Gray + " tick damage for 15s.", CC.DarkGreen + "Swim Effect", CC.Gray + " * Receive the following effects when hitting", CC.Gray + " enemies with your Sing Effect tick damage:", CC.Gray + "  * +12.5% Outgoing Damage", CC.Gray + "  * Absolute Bypass of enemy Holy White", CC.Gray + "  Scrolls (for 1 piece only)"), true),
    VALENTINES_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &d&lGolden Boots"), ItemSkinType.BOOTS, new MutatedValentinesBoots(), Arrays.asList(
            CC.Red + "Gears IV",
            CC.Red + "1.1x Critical Damage",
            CC.Red + "10% Chance to negate Incoming Knockback",
            CC.Red + "Immune to Witcher Pet",
            CC.DarkGreen + "1.25x World Destroyer Duration"
    ), true),
    EASTER_2022_BOOTS(CC.translate("&d&lMary Janes"), ItemSkinType.BOOTS, new MaryJanes(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cImmune to Amulet of Corruption"),
            CC.translate("&c1.25x Critical Damage"),
            CC.translate("&cImmune to slowness and freezes")
    ), true),
    BACK_TO_SCHOOL_2021_BOOTS(CC.translate("&8&lBig Timbs"), ItemSkinType.BOOTS, new BigTimbsSkin(), Arrays.asList(CC.Red + "35% chance on incoming damage to", CC.Red + "negate enemy Rage stack multipliers", CC.Red + "Immune to Hero Killer", CC.Red + "Immune to enemy outgoing Heroic Pet abilities", CC.translate("&cGears IV")), true),
    EASTER_2022_MUTATED_BOOTS(CC.translate("&2&l&nMutated&r &d&lMary Janes"), ItemSkinType.BOOTS, new MutatedMaryJanes(), Arrays.asList(
            CC.translate("&cGears IV"),
            CC.translate("&cImmune to Amulet of Corruption"),
            CC.translate("&c1.25x Critical Damage"),
            CC.translate("&cImmune to slowness and freezes"),
            CC.translate("&2-33% Rogue Enchant damage")
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

    Boots(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled) {
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

    public ItemStack getMultiSkin(Boots type) {
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

    public ItemStack apply(ItemStack item, Boots type) {
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