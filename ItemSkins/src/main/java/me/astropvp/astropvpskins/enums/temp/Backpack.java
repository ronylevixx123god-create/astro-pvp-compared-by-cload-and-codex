package me.astropvp.astropvpskins.enums.temp;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.objects.skins.backpacks.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public enum Backpack {
    VALENTINES_2021_BACKPACK(CC.RedB + "Teddy Backpack", ItemSkinType.BACKPACK, new TeddyBackpackSkin(), Arrays.asList(CC.Red + "Immune to (Lethal) Sniper", CC.Red + "Immune to Hero Killer", CC.Red + "+2% Outgoing Damage"), true),
    JULY_2021_BACKPACK(CC.translate("&f&lCosmic Cape"), ItemSkinType.BACKPACK, new CosmicCapeSkin(), Arrays.asList(CC.Red + "Immune to Hero Killer", CC.Red + "Decreases incoming damage from Soul Enchants by 15%", CC.Red + "2.5% chance to negate enemy Shockwave and (Guided) Rocket", CC.Red + "Escape on outgoing damage for 10s", CC.Red + "Increases outgoing damage by up to 15% when under 50% HP"), true),
    VALENTINES_2022_BACKPACK(CC.translate("&d&lBank Roll Backpack"), ItemSkinType.BACKPACK, new ValentinesBackpack(), Arrays.asList(
            CC.translate("&c+50% Opening Combo Damage"),
            CC.translate("&c-25% Incoming Damage from Rogue"),
            CC.translate("&c+10% (Divine) Enlighted Chance")

    ), true),
    HALLOWEEN_BACKPACK(CC.GoldB + "Cursed Wings", ItemSkinType.BACKPACK, new CursedWingsSkin(), Arrays.asList(CC.translate("&c-75% Silence Duration"), CC.translate("&cBlocks all enemy Rocket Escape"), CC.translate("&c-3% Incoming Damage")), true),
    VALENTINES_2022_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &d&lBank Roll Backpack"), ItemSkinType.BACKPACK, new MutatedValentinesBackpack(), Arrays.asList(
            CC.translate("&c+50% Opening Combo Damage"),
            CC.translate("&c-25% Incoming Damage from Rogue"),
            CC.translate("&c+10% (Divine) Enlighted Chance"),
            CC.translate("&2Immune to Divine Ticks"),
            CC.translate("&2Immune to Lifesteal")


    ), true),
    SPRING_BREAK_2022_BACKPACK(CC.translate("&a&lSpray and Pray Backpack"), ItemSkinType.BACKPACK, new SprayPrayBackpack(), Arrays.asList(
            CC.translate("&c2% chance when dealing damage to inflict the"),
            CC.translate("&cfollowing Spray and Pray Ability for 10s:"),
            CC.translate(" &7* -6% Outgoing Damage"),
            CC.translate(" &7* +6% Incoming Damage"),
            CC.translate(" &7* -2HP tick damage per second"),
            CC.translate(" &7* When receiving this tick damage, your enemy"),
            CC.translate(" &7 will infect a nearby enemy, within 7 blocks, with"),
            CC.translate(" &7* the same effects and remaining time.")
    ), true),
    SPRING_BREAK_2022_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &a&lSpray and Pray Backpack"), ItemSkinType.BACKPACK, new MutatedSprayPrayBackpack(), Arrays.asList(
            CC.translate("&c2% chance when dealing damage to inflict the"),
            CC.translate("&cfollowing Spray and Pray Ability for 10s:"),
            CC.translate(" &7* -6% Outgoing Damage"),
            CC.translate(" &7* +6% Incoming Damage"),
            CC.translate(" &7* -2HP tick damage per second"),
            CC.translate(" &7 When receiving this tick damage, your enemy"),
            CC.translate(" &7 will infect a nearby enemy, within 7 blocks, with"),
            CC.translate(" &7 the same effects and remaining time."),
            CC.translate(" &2* A nearby ally, within 7 blocks, will also"),
            CC.translate(" &2receive Regeneration I for the remaining time.")
    ), true),
    THANKSGIVING_2021_BACKPACK(CC.translate("&6&lTommy Tail"), ItemSkinType.BACKPACK, new TommyTail(), Arrays.asList(CC.Red + "+5% Outgoing PvP Damage when using a sword", CC.Red + "Immune to Hero Killer", CC.Red + "7.5% chance to block Soul Enchant Damage", CC.Red + "2% chance to activate a LVL 10", CC.Red + "Banshee Pet effect when damaged", CC.Red + "2x Outgoing Damage on any hit that makes", CC.Red + "your enemy's (Guided) Rocket Escape", CC.Red + "Immune to (Lethal) Sniper"), true),
    THANKSGIVING_2021_MUTATED_BACKPACK(CC.translate("&2&l&nMutated&r &6&lTommy Tail"), ItemSkinType.BACKPACK, new MutatedTommyTail(), Arrays.asList(CC.Red + "+5% Outgoing PvP Damage when using a sword", CC.Red + "Immune to Hero Killer", CC.Red + "7.5% chance to block Soul Enchant Damage", CC.Red + "2% chance to activate a LVL 10", CC.Red + "Banshee Pet effect when damaged", CC.Red + "2x Outgoing Damage on any hit that makes", CC.Red + "your enemy's (Guided) Rocket Escape", CC.Red + "Immune to (Lethal) Sniper", CC.DarkGreen + "Increases outgoing damage by up to 10%", CC.DarkGreen + "when under 60% HP"), true),
    BACK_TO_SCHOOL_2022_BACKPACK(CC.translate("&8&lKeg"), ItemSkinType.BACKPACK, new KegSkin(), Arrays.asList(
            CC.translate("&c-75% Silence Duration"),
            CC.translate("&cBlocks all enemy Ragdoll and Rocket Escape"),
            CC.translate("&c-15% Incoming Soul Enchant Damage")
    ), true),
    HALLOWEEN_2022_BACKPACK(CC.translate("&4&lGrim Cloak"), ItemSkinType.BACKPACK, new GrimCloakSkin(), Arrays.asList(
            CC.translate("&c-50% Incoming Rogue Damage"),
            CC.translate("&c1.25x Rage Damage Multiplier"),
            CC.translate("&c+25% Extra Damage from behind")
    ), true),

    VALENTINES_2023_BACKPACK(CC.translate("&5&lLucifer Wings"), ItemSkinType.BACKPACK, new LuciferBackpack(), Arrays.asList(
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

    Backpack(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled) {
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

    public ItemStack getMultiSkin(Backpack type) {
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

    public ItemStack apply(ItemStack item, Backpack type) {
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