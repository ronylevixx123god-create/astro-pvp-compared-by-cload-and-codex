package me.astropvp.astropvpskins.enums.temp;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import lombok.Getter;
import me.astropvp.astropvpskins.enums.ItemSkinType;
import me.astropvp.astropvpskins.objects.ItemSkin;
import me.astropvp.astropvpskins.objects.skins.swords.*;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public enum Sword {
    PEPE_WEAPON(CC.BlueB + "Pepe Clapper", ItemSkinType.SWORD, new PepeWeaponSkin(), Collections.singletonList(CC.Red + "1.25x Damage"), true),
    DOOMSDAY_SWORD(CC.DarkGreenB + "Doomsday Machete", ItemSkinType.SWORD, new DoomsdayMacheteSkin(), Collections.singletonList(CC.Red + "Increases outgoing damage from soul enchantments by 1.25x"), true),
    SUMMER_2021_SWORD(CC.translate("&c&lMaui's Hook"), ItemSkinType.SWORD, new MauisHookSkin(), Arrays.asList(CC.translate("&c+15% Outgoing Soul Enchant Damage"), CC.translate("&cBlackout V"), CC.translate("&cNeutralize VI"), CC.translate("&cTranscendence ability"), CC.translate("&7(For each soul enchant that is"), CC.translate("&7blocked (not including immunity)"), CC.translate("&7gain +1 stack. At 10 stacks crouch"), CC.translate("&7to activate Transcendence massively"), CC.translate("&7damaging and silencing nearby"), CC.translate("&7players and pets.)")), true),
    CHRISTMAS_SWORD_ORNAMENTAL(CC.translate("&a&lOrnamental Carnage"), ItemSkinType.SWORD, new OrnamentalCarnageSkin(), Arrays.asList(CC.translate("&c-5% Incoming Damage"), CC.translate("&c-15% Incoming Damage from enemies with Soul Mode active"), CC.translate("&cTitan Trap V"), CC.translate("&c+10% Outgoing Damage to trapped players"), CC.translate("&c+2 blocks Divine Immolation radius"), CC.translate("&c2% chance for Dimensional Shift Ability")), true),
    VALENTINES_2021_SWORD(CC.translate("&c&lHeartbreaker"), ItemSkinType.SWORD, new HeartBreakerSkin(), Arrays.asList(CC.translate("&cBypass enemy (Vengeful) Diminish"), CC.translate("&c+1% Damage for each enemy level of (Planetary) Deathbringer"), CC.translate("&cMartyr Valor V"), CC.translate("&cReality Inversion V")), true),
    CHRISTMAS_2021_SWORD(CC.translate("&4&lSeason's Beatings"), ItemSkinType.SWORD, new SeasonsBeatings(), Arrays.asList(CC.Red + "+7% Outgoing Damage to players wielding weapons with Hero Killer", CC.Red + "+10% Outgoing Damage to enemies with Soul Mode Activated", CC.Red + "3.5% chance to silence enemy (Reflective) Block for 8s", CC.Red + "Outgoing Damage while Frozen or Slowed has a 12.5% chance", CC.Red + "to remove the Freeze or Slow effect", CC.Red + "+10% Rage Damage"), true),
    THANKSGIVING_2021_SWORD(CC.translate("&e&lCarver"), ItemSkinType.SWORD, new Carver(), Arrays.asList(CC.Red + "15% chance to deal 2x Divine Immolation tick damage", CC.Gray + "  (costs 2x extra souls on proc)", CC.Red + "33% chance to bypass Heroic Armor", CC.Red + "Damage you deal has a 3% chance to deal", CC.Red + "+50% Outgoing Soul Enchant Damage for 5s", CC.Red + "4% chance to silence your enemy's Rogue for 8s"), true),
    THANKSGIVING_SWORD_HARVESTER(CC.translate("&e&lSoul Harvester"), ItemSkinType.SWORD, new SoulHarvesterSkin(), Arrays.asList(CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.3x"), CC.translate("&c+10% Outgoing Damage to enemies with Soul Mode active"), CC.translate("&c50% chance to block Divine Immolation"), CC.translate("&c-25% Soul Enchant Costs")), true),
    EASTER_2022_SWORD(CC.translate("&e&lQueen's Verdict"), ItemSkinType.SWORD, new QueensVerdict(), Arrays.asList(
            CC.translate("&cBypass enemy (Paladin) Armored"),
            CC.translate("&cIncrease outgoing soul enchant damage by 1.3x"),
            CC.translate("&c15% chance to bypass enemy Ancient Crystals"),
            CC.translate("&cDevour VI"),
            CC.translate("&c20% chance to block enemy Divine Immolation")
    ), true),
    CHRISTMAS_2021_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &4&lSeason's Beatings"), ItemSkinType.SWORD, new MutatedSeasonsBeatings(), Arrays.asList(CC.Red + "+7% Outgoing Damage to players wielding weapons with Hero Killer", CC.Red + "+10% Outgoing Damage to enemies with Soul Mode Activated", CC.Red + "3.5% chance to silence enemy (Reflective) Block for 8s", CC.Red + "Outgoing Damage while Frozen or Slowed has a 12.5% chance", CC.Red + "to remove the Freeze or Slow effect", CC.Red + "+10% Rage Damage", CC.DarkGreen + "Soul Master VI"), true),
    JULY4TH_2022_SWORD(CC.translate("&a&lPrickle Pear"), ItemSkinType.SWORD, new PricklePear(), Arrays.asList(
            CC.translate("&c15% Chance to break enemy Rage Stacks"),
            CC.translate("&c+4% Outgoing Damage"),
            CC.translate("&cSilence V"),
            CC.translate("&cPrickle Chaos"),
            CC.translate("&7 Chance to struck enemy with a"),
            CC.translate("&7 prickle and make them bleed for 5s.")
    ), true),

    EASTER_2022_MUTATED_SWORD(CC.translate("&2&l&nMutated&r &e&lQueen's Verdict"), ItemSkinType.SWORD, new MutatedQueensVerdict(), Arrays.asList(
            CC.translate("&cBypass enemy (Paladin) Armored"),
            CC.translate("&cIncrease outgoing soul enchant damage by 1.3x"),
            CC.translate("&c15% chance to bypass enemy Ancient Crystals"),
            CC.translate("&cDevour VI"),
            CC.translate("&c20% chance to block enemy Divine Immolation"),
            CC.translate("&2Double Strike IV")
    ), true),
    CHRISTMAS_2022_SWORD_ACTIVE(CC.translate("&b&lFrostbound Sword"), ItemSkinType.SWORD, new FrostBoundSkin(), Arrays.asList(
            CC.translate("&cOpening Combo VI"),
            CC.translate("&c2.5% chance to silence enemy (Demonic) Lifesteal for 3s"),
            CC.translate("&c1% chance to silence enemy Titan Attributes for 3s"),
            CC.translate("&c+5% Outgoing Damage"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.25x")), true),

    THANKSGIVING_2022_SWORD_GREEN(CC.translate("&e&lRay Sabre"), ItemSkinType.SWORD, new RaySabreSkin(), Arrays.asList(
            CC.translate("&cSoul Master VI"),
            CC.translate("&cIncreases outgoing damage from Soul Enchants by 1.25x"),
            CC.translate("&c33% chance to block Divine Immolation"),
            CC.translate("&c3% chance to block enemy Defensive Crystals for 7s")), true),
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

    Sword(String display, ItemSkinType itemSkinType, ItemSkin skin, List<String> extra, boolean enabled) {
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

    public ItemStack getMultiSkin(Sword type) {
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

    public ItemStack apply(ItemStack item, Sword type) {
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