package net.astropvp.armorsets;

import com.cosmicpvp.cosmicutils.nbt.NBTTagCompoundBuilder;
import com.cosmicpvp.cosmicutils.nbt.NBTTagListBuilder;
import com.cosmicpvp.cosmicutils.utils.CC;
import com.cosmicpvp.cosmicutils.utils.GeneralUtil;
import com.cosmicpvp.cosmicutils.utils.ItemBuilder;
import com.cosmicpvp.cosmicutils.utils.NBTWrapper;
import com.google.common.collect.Lists;
import de.tr7zw.nbtapi.NBTItem;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicskycustoms.struct.CustomItemBuilder;
import net.astropvp.armorsets.struct.ArmorSetType;
import net.astropvp.armorsets.struct.antiMkit.AntiMkit;
import net.astropvp.armorsets.struct.cache.ArmorSetCache;
import net.astropvp.armorsets.struct.heroicArmor.HeroicArmor;
import net.astropvp.armorsets.utils.Pair;
import net.astropvp.armorsets.utils.modifiers.AttributeModifier;
import net.astropvp.armorsets.utils.modifiers.ModifierType;
import net.astropvp.core.AstroCore;
import net.astropvp.core.utils.cache.CacheManager;
import net.astropvp.core.utils.cache.CachedPlayer;
import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.*;

public class AstroArmorSetsAPI {


    public static ArmorSetType getSetTypeOnItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            NBTWrapper wrapper = new NBTWrapper(item);
            NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("cosmicData");
            return cosmicData.hasKey("armorSetType") ? ArmorSetType.valueOf(cosmicData.getString("armorSetType")) : null;
        } else {
            return null;
        }
    }

    public static boolean isOmniPiece(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            NBTWrapper wrapper = new NBTWrapper(item);
            NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("cosmicData");
            return cosmicData.hasKey("omniArmorSet");
        }
        return false;
    }

    public static boolean hasSetTypeOnItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            NBTWrapper wrapper = new NBTWrapper(item);
            NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("cosmicData");
            return cosmicData.hasKey("armorSetType");
        } else {
            return false;
        }
    }

    public static ItemStack convertItemToHeroic(ItemStack itemStack) {
        if (GeneralUtil.isWeapon(itemStack)) {
            return convertWeaponToHeroic(Objects.requireNonNull(HeroicArmor.getFromMaterial(itemStack.getType())), itemStack);
        } else {
            if (GeneralUtil.isArmor(itemStack)) {
                return convertArmorToHeroic(itemStack, Objects.requireNonNull(HeroicArmor.getFromMaterial(itemStack.getType())));

            }
        }
        return null;
    }

    public static ArmorSetType getCurrentArmorSet(Player player) {
        CachedPlayer cachedPlayer = CacheManager.getInstance().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) cachedPlayer.getCache("armor_set");
        if (cache == null) return null;
        return cache.getCurrentArmorSet();
    }


    public static Map<ArmorSetType, Integer> getCurrentCrystals(Player player) {
        Map<ArmorSetType, Integer> empty = new HashMap<>();
        CachedPlayer cachedPlayer = CacheManager.getInstance().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) cachedPlayer.getCache("armor_set");
        if (cache == null) return empty;
        return cache.getCurrentSetCrystals();
    }

    public static boolean isInOmni(Player player) {
        CachedPlayer cachedPlayer = CacheManager.getInstance().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) cachedPlayer.getCache("armor_set");
        if (cache == null) return false;
        return cache.getOmniPieces() == 3 && cache.getCurrentArmorSet() != null;
    }

    public static void refreshArmorSets(Player player, boolean silent) {

        CachedPlayer armorSetPlayer = AstroCore.getInstance().getCacheManager().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) ((ArmorSetCache) (armorSetPlayer.hasCache("armor_set") ? armorSetPlayer.getCache("armor_set") : armorSetPlayer.registerCache(new ArmorSetCache(player))));
        clearCache(player);

        ItemStack[] armor = player.getInventory().getArmorContents();

        for (ItemStack item : armor) {
            if (item == null) continue;
            if (item.getType() == Material.AIR) continue;

            if (hasCrystalsApplied(item)) {
                List<ArmorSetType> crystals = AstroArmorSetsAPI.getCrystalsFromItem(item);
                for (ArmorSetType type : crystals) {
                    cache.addArmorCrystal(type);
                }
            }
            if (hasSetTypeOnItem(item)) {
                ArmorSetType set = getSetTypeOnItem(item);
                cache.addArmorSet(set);
            }
            if (isOmniPiece(item)) {
                cache.addOmniPiece(item);
            }
            if (isHeroic(item)) {
                cache.addHeroicArmor(item);
            }

            if (AstroArmorSetsAPI.itemHasMkitCrystals(item)) {
                Set<AntiMkit> crystals = AstroArmorSetsAPI.getAntiMkitCrystalsOnItem(item);
                assert crystals != null;
                for (AntiMkit type : crystals) {
                    cache.addMkitCrystal(type);
                }
            }
        }

        if (!silent) {
                if (cache.getCurrentArmorSet() != null) {
                    cache.getCurrentArmorSet().getArmorSet().onEquip(player);

                    for (String s : cache.getCurrentArmorSet().getArmorSet().getArmorEquipMessage()) {
                        player.sendMessage(s);
                    }
                }
        }
    }

    public static ItemStack addWineArmorOverride(ItemStack itemStack, int amount) {
        NBTItem nbtItem = new NBTItem(itemStack);
        nbtItem.setInteger("wine-armor-override", amount);
        return nbtItem.getItem();
    }

    public static void clearCache(Player player) {

        CachedPlayer armorSetPlayer = AstroCore.getInstance().getCacheManager().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) ((ArmorSetCache) (armorSetPlayer.hasCache("armor_set") ? armorSetPlayer.getCache("armor_set") : armorSetPlayer.registerCache(new ArmorSetCache(player))));
        cache.setHeroicPieces(0);
        cache.setOmniPieces(0);
        cache.getCurrentAntiMkitCrystals().clear();
        cache.setCurrentArmorSet(null);
        cache.getCurrentSetCrystals().clear();
        cache.getCurrentArmorSets().clear();
    }

    public static void refreshArmorPiece(ItemStack newArmorPiece, ItemStack oldArmorPiece, Player player) {

        CachedPlayer armorSetPlayer = AstroCore.getInstance().getCacheManager().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) ((ArmorSetCache) (armorSetPlayer.hasCache("armor_set") ? armorSetPlayer.getCache("armor_set") : armorSetPlayer.registerCache(new ArmorSetCache(player))));
        if (isOmniPiece(oldArmorPiece)) {
            cache.removeOmniPiece(oldArmorPiece);
        }
        if (isOmniPiece(newArmorPiece)) {
            cache.addOmniPiece(newArmorPiece);
        }
        if (hasSetTypeOnItem(oldArmorPiece)) {
            ArmorSetType set = getSetTypeOnItem(oldArmorPiece);
            set.getArmorSet().onUnequip(player);
            cache.removeArmorSet(getSetTypeOnItem(oldArmorPiece));
        }
        if (hasSetTypeOnItem(newArmorPiece)) {
            cache.addArmorSet(getSetTypeOnItem(newArmorPiece));
        }
        if (hasCrystalsApplied(oldArmorPiece)) {
            List<ArmorSetType> crystals = AstroArmorSetsAPI.getCrystalsFromItem(oldArmorPiece);
            for (ArmorSetType type : crystals) {
                cache.removeArmorCrystal(type);
            }
        }
        if (hasCrystalsApplied(newArmorPiece)) {
            List<ArmorSetType> crystals = AstroArmorSetsAPI.getCrystalsFromItem(newArmorPiece);
            for (ArmorSetType type : crystals) {
                cache.addArmorCrystal(type);
            }
        }


        if (isHeroic(oldArmorPiece)) {
            cache.removeHeroicArmor(oldArmorPiece);
        }
        if (isHeroic(newArmorPiece)) {
            cache.addHeroicArmor(newArmorPiece);
        }


        if (AstroArmorSetsAPI.itemHasMkitCrystals(oldArmorPiece)) {
            Set<AntiMkit> crystals = AstroArmorSetsAPI.getAntiMkitCrystalsOnItem(oldArmorPiece);
            assert crystals != null;
            for (AntiMkit type : crystals) {
                cache.removeMkitCrystal(type);
            }
        }

        if (AstroArmorSetsAPI.itemHasMkitCrystals(newArmorPiece)) {
            Set<AntiMkit> crystals = AstroArmorSetsAPI.getAntiMkitCrystalsOnItem(newArmorPiece);
            assert crystals != null;
            for (AntiMkit type : crystals) {
                cache.addMkitCrystal(type);
            }
        }


    }

    public static List<ArmorSetType> getArmorCrystalsFromCrystalItem(NBTTagCompoundBuilder wrapper) {
        if (wrapper.hasKey("bonus")) {
            ArmorSetType set = AstroArmorSetsAPI.armorSetTypeFromName(wrapper.getString("bonus"));
            if (set != null) {
                return Collections.singletonList(set);
            }
        } else if (wrapper.hasKey("bonuses")) {
            ArrayList retr2 = Lists.newArrayList();
            NBTTagListBuilder list = new NBTTagListBuilder(wrapper.getNBTBase("bonuses"));
            for (int i = 0; i < list.size(); ++i) {
                ArmorSetType from;
                String bonus = list.getStringAt(i);
                if (bonus == null || bonus.isEmpty() || (from = armorSetTypeFromName(bonus)) == null) continue;
                retr2.add(from);
            }
            return retr2;
        }
        return Collections.emptyList();
    }

    public static List<ArmorSetType> getFromArmorBonusCrystal(NBTWrapper wrapper) {
        NBTTagCompoundBuilder cosmicData = wrapper.getCompound("cosmicData");
        if (cosmicData == null) {
            return null;
        }
        List<ArmorSetType> retr = new ArrayList<>();
        if (cosmicData.hasKey("crystalBonus")) {
            String upgrade = cosmicData.getString("crystalBonus");
            if (upgrade == null) {
                return null;
            }
            ArmorSetType from = AstroArmorSetsAPI.armorSetTypeFromName(upgrade);
            if (from != null) {
                retr.add(from);
            }
        } else if (cosmicData.hasKey("crystalBonuses")) {
            NBTTagListBuilder list = new NBTTagListBuilder(cosmicData.getNBTBase("crystalBonuses"));
            for (int i = 0; i < list.size(); ++i) {
                ArmorSetType from2;
                String bonus = list.getStringAt(i);
                if (bonus == null || bonus.isEmpty() || (from2 = AstroArmorSetsAPI.armorSetTypeFromName(bonus)) == null)
                    continue;
                retr.add(from2);
            }
        }
        return retr;
    }

    public static ItemStack applyCrystalToItem(List<ArmorSetType> set, ItemStack item) {
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("cosmicData");
        NBTTagListBuilder nbtTagListBuilder = new NBTTagListBuilder();
        StringBuilder sb = new StringBuilder();
        set.forEach(s -> {
            nbtTagListBuilder.addString(s.name());
            sb.append(s.getDisplay()).append(CC.Gray).append(", ");
        });
        data.setNBTBase("crystalBonuses", nbtTagListBuilder.build());
        wrapper.setNBTBase("cosmicData", data.get());
        item = wrapper.build();
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore() == null ? Lists.newArrayList() : im.getLore();
        String str = sb.toString();
        if (str.endsWith(", ")) {
            str = str.substring(0, str.length() - 2);
        }
        lore.add(CC.GoldB + "Armor Crystal (" + str + CC.GoldB + ")");
        im.setLore(lore);
        item.setItemMeta(im);
        return item;
    }

    public static ItemStack removeCrystalFromItem(NBTWrapper wrapper) {
        NBTTagCompoundBuilder data = wrapper.getCompoundOrCreate("cosmicData");
        data.remove("crystalBonus");
        data.remove("crystalBonuses");
        wrapper.setNBTBase("cosmicData", data.get());
        ItemStack item = wrapper.build();
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();
        if (lore != null) {
            lore.removeIf(s -> s.startsWith(CC.GoldB + "Armor Crystal ("));
            im.setLore(lore);
        }
        item.setItemMeta(im);
        return item;
    }

    public static ItemStack removeCrystalFromItem(ItemStack item) {
        return removeCrystalFromItem(new NBTWrapper(item));
    }

    public static boolean isHeroic(ItemStack item) {
        if (item == null)
            return false;
        if (!item.getType().name().contains("LEATHER") && !item.getType().name().endsWith("_SWORD") && !item.getType().name().endsWith("_AXE")) {
            return false;
        }

        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.getCompound("cosmicData");
        if (cosmicData == null) return false;
        return cosmicData.hasKey("heroicArmor");

    }

    public static boolean hasCrystalsApplied(ItemStack item) {
        if (item == null) return false;
        getCrystalsFromItem(item);
        return !getCrystalsFromItem(item).isEmpty();
    }

    public static Map<ArmorSetType, Integer> getBonusCrystalsEquipped(Player player) {
        Map<ArmorSetType, Integer> empty = new HashMap<>();
        CachedPlayer cachedPlayer = CacheManager.getInstance().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) cachedPlayer.getCache("armor_set");
        if (cache == null) return empty;
        if (cache.getCurrentSetCrystals() != null && !cache.getCurrentSetCrystals().isEmpty()) {
            return cache.getCurrentSetCrystals();
        }
        return empty;
    }

    public static Map<ArmorSetType, Integer> getBonusCrystalsEquippedIncludingCorruption(Player attacker, Player attacked) {
        Map<ArmorSetType, Integer> map = new HashMap<>();
        CachedPlayer cachedOther = CacheManager.getInstance().getCachedPlayer(attacked);
        ArmorSetCache cacheOther = (ArmorSetCache) cachedOther.getCache("armor_set");
        if (attacker.getInventory().getChestplate() != null && AstroItemSkinsAPI.hasSkinEquipped(attacker, SkinType.CORRUPT_AMULET_CORRUPTION)) {

            if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.RUINS_HELMET)) {
                return cacheOther.getCurrentSetCrystals();
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.EASTER_2022_MUTATED_BOOTS) || AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.EASTER_2022_BOOTS)) {
                return cacheOther.getCurrentSetCrystals();
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.THANKSGIVING_2021_AMULET)) {
                return cacheOther.getCurrentSetCrystals();

            }

            if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.BACK_TO_SCHOOL_2022_HELMET)) {
                return cacheOther.getCurrentSetCrystals();

            }

            if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.CHRISTMAS_2022_AMULET_ACTIVE)) {
                return cacheOther.getCurrentSetCrystals();
            }

            if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.CHRISTMAS_2022_HELMET_ACTIVE)) {
                return cacheOther.getCurrentSetCrystals();

            }
            List<ArmorSetType> chestplateCrystalsPlayer = getCrystalsFromItem(attacker.getInventory().getChestplate());
            for (Map.Entry<ArmorSetType, Integer> entry : cacheOther.getCurrentSetCrystals().entrySet()) {
                ArmorSetType type = entry.getKey();

                Integer amount = entry.getValue();

                if (AstroItemSkinsAPI.hasSkinEquipped(attacked, SkinType.BACK_TO_SCHOOL_2021_HELMET) && isDefensiveCrystal(type)) {
                    map.put(type, amount);

                } else if (!chestplateCrystalsPlayer.contains(type)) {

                    map.put(type, amount);
                }
            }

        } else {
            return cacheOther.getCurrentSetCrystals();

        }
        return map;
    }


    public static boolean hasMutatedCrystalEquipped(Player player) {
        for (ArmorSetType type : getBonusCrystalsEquipped(player).keySet()) {
            if (type.name().contains("MUTATED")) return true;
        }
        return false;
    }

    public static boolean isDefensiveCrystal(ArmorSetType type) {
        switch (type) {
            case ANCIENT:
            case YETI:
            case ASTRO:
            case ENGINEER:
            case MOTHER_OF_YIJKI:
            /*case APOLLO:
                return true;*/
            default:
                return false;
        }
    }

    public static Map<AntiMkit, Integer> getAntiMkitCrystalsEquipped(Player player) {
        Map<AntiMkit, Integer> empty = new HashMap<>();

        CachedPlayer cachedPlayer = CacheManager.getInstance().getCachedPlayer(player);
        ArmorSetCache cache = (ArmorSetCache) cachedPlayer.getCache("armor_set");
        if (cache.getCurrentAntiMkitCrystals() != null && !cache.getCurrentAntiMkitCrystals().isEmpty()) {
            return cache.getCurrentAntiMkitCrystals();
        }
        return empty;
    }

    public static List<ArmorSetType> getCrystalsFromItem(ItemStack item) {
        List<ArmorSetType> crystals = new ArrayList<>();

        if (item == null) {
            return crystals;
        }
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.getCompound("cosmicData");
        if (cosmicData == null) {
            return crystals;
        }
        if (cosmicData.hasKey("crystalBonus")) {
            String upgrade = cosmicData.getString("crystalBonus");
            if (upgrade == null) {
                return crystals;
            }
            ArmorSetType from = armorSetTypeFromName(upgrade);
            if (from != null) {
                crystals.add(from);
            }
        } else if (cosmicData.hasKey("crystalBonuses")) {
            NBTTagListBuilder list = new NBTTagListBuilder(cosmicData.getNBTBase("crystalBonuses"));
            for (int i = 0; i < list.size(); ++i) {
                ArmorSetType from2;
                String bonus = list.getStringAt(i);
                if (bonus == null || bonus.isEmpty() || (from2 = armorSetTypeFromName(bonus)) == null) continue;
                crystals.add(from2);
            }
        }
        return crystals;
    }


    public static ItemStack createCrystal(List<ArmorSetType> armorSet, double applyChance) {
        return (new CustomItemBuilder("armorCrystal", armorSet, applyChance)).build();
    }

    public static ItemStack convertArmorToHeroic(ItemStack item, HeroicArmor type) {
        item.setType(type.getType());
        double modifier = type.getReduction();


        ItemMeta im = item.getItemMeta();
        String strongerLine;
        List<String> lore = im.hasLore() ? im.getLore() : new ArrayList<>();
        if (!lore.contains(strongerLine = CC.DarkRed + "This armor is stronger than diamond.")) {
            lore.add("");
            lore.add(CC.Gray + "+" + modifier + " Armor Value");
            lore.add(strongerLine);
        }
        im.setLore(lore);

        if (im instanceof LeatherArmorMeta) {
            ((LeatherArmorMeta) im).setColor(Color.RED);
        }
        im.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(im);
        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("cosmicData");
        cosmicData.setBoolean("heroicArmor", true);
        wrapper.setNBTBase("cosmicData", cosmicData.get());

        ItemStack tempItem = wrapper.build();
        NBTItem nbtItem = new NBTItem(tempItem);
        nbtItem.setInteger("wine-armor-override", Objects.requireNonNull(HeroicArmor.getFromMaterial(tempItem.getType())).getReduction());
        item = nbtItem.getItem();
        return item;
    }

    public static ItemStack convertWeaponToHeroic(HeroicArmor type, ItemStack item) {
        item.setType(type.getType());
        ItemMeta im = item.getItemMeta();
        List<String> existing = im.hasLore() ? im.getLore() : new ArrayList<>();
        String strongerLine;

        if (!existing.contains(strongerLine = CC.DarkRed + "This weapon is stronger than diamond.")) {
            existing.add("");
            existing.add(CC.Gray + "+" + type.getIncrease() + " Bonus Attack Damage");
            existing.add(strongerLine);
        }
        im.setLore(existing);
        item.setItemMeta(im);

        NBTWrapper wrapper = new NBTWrapper(item);
        NBTTagCompoundBuilder cosmicData = wrapper.getCompoundOrCreate("cosmicData");
        cosmicData.setBoolean("heroicArmor", true);
        wrapper.setNBTBase("cosmicData", cosmicData.get());

        NBTItem nbtItem = new NBTItem(wrapper.build());
        nbtItem.setBoolean("heroic", true);


        ItemStack tempItem = nbtItem.getItem();
        ItemMeta meta = tempItem.getItemMeta();
        meta.spigot().setUnbreakable(true);
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        tempItem.setItemMeta(meta);
        Pair<ModifierType, Double> modifierTypePair = new Pair<>(ModifierType.ATTACK_DAMAGE, 7D);
        return AttributeModifier.applyModifications(tempItem, Collections.singletonList(modifierTypePair));
    }

    public static ItemStack createArmorSetUpgrade(ArmorSetType armorset, double successRate) {

        String armorLore = armorset == null ? CC.Gray + "armor set" : armorset.getDisplay() + CC.Gray + " armor";

        return new NBTWrapper(new ItemBuilder(Material.INK_SACK, CC.GoldB + "Heroic " + (armorset != null ? CC.Gold + "(" + armorset.getDisplay() + CC.Gold + ") " + CC.GoldB : "Armor") + "Upgrade", "", CC.Gray + "Apply to any " + armorLore, CC.Gray + "for a " + CC.Gold + (int) successRate + "%" + CC.Gray + " chance to imbue it", CC.Gray + "with the power of Heroic Armor!", "", CC.Gray + "This will increase the armor's base stats.").data(DyeColor.YELLOW.getDyeData()).build()).setString("cosmicType", "HEROIC_ARMOR_UPGRADE").setNBTBase("cosmicData", new NBTTagCompoundBuilder().setDouble("success", successRate).setString("setType", armorset == null ? "ANY" : armorset.name()).get()).build();
    }


    public static ArmorSetType armorSetTypeFromName(String name) {
        for (ArmorSetType set : ArmorSetType.values()) {
            if (!set.name().equalsIgnoreCase(name)) continue;
            return set;
        }
        return null;
    }


    public static AntiMkit mkitFromName(String name) {
        return Arrays.stream(AntiMkit.values()).filter(ab -> ab.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public static Set<AntiMkit> getAntiMkitCrystal(NBTWrapper wrapper) {
        Set<AntiMkit> retr = new HashSet<>();

        if (wrapper == null)
            return retr;
        NBTTagCompoundBuilder cosmicData = wrapper.getCompound("cosmicData");
        if (cosmicData != null) {
            String abilityArmorSetEquipped = cosmicData.getString("antiMkit");
            if (abilityArmorSetEquipped != null && !abilityArmorSetEquipped.isEmpty()) {
                AntiMkit set = mkitFromName(abilityArmorSetEquipped);
                retr.add(set);
                return retr;
            }
            if (cosmicData.hasKey("antiMkits")) {
                retr = new HashSet<>();
                NBTTagListBuilder list = new NBTTagListBuilder(cosmicData.getNBTBase("antiMkits"));
                for (int i = 0; i < list.size(); i++) {
                    String setName = list.getStringAt(i);
                    AntiMkit set2 = mkitFromName(setName);
                    if (set2 != null)
                        retr.add(set2);
                }
                return retr;
            }
        }
        return retr;
    }

    public static boolean hasMkitEquipped(Player player, AntiMkit armorSet) {
        byte b;
        int i;
        ItemStack[] arrayOfItemStack;
        for (i = (arrayOfItemStack = player.getInventory().getArmorContents()).length, b = 0; b < i; ) {
            ItemStack x = arrayOfItemStack[b];
            if (x != null && x.getType() != Material.AIR) {
                Set<AntiMkit> set = getAntiMkitCrystalsOnItem(x);
                if (set != null)
                    if (set.contains(armorSet))
                        return true;
            }
            b++;
        }
        return false;
    }

    public static Set<AntiMkit> getAntiMkitCrystalsOnItem(ItemStack armorItem) {
        return getAntiMkitCrystal(new NBTWrapper(armorItem));
    }

    public static ItemStack removeAntiMkitCrystalFromItem(NBTWrapper wrapper) {
        NBTTagCompoundBuilder data = wrapper.getCompound("cosmicData");
        if (data != null) {
            data.remove("antiMkit");
            data.remove("antiMkits");
            wrapper.setNBTBase("cosmicData", data.get());
        }
        ItemStack item = wrapper.build();
        ItemMeta im = item.getItemMeta();
        List<String> lore = im.getLore();
        if (lore != null) {
            lore.removeIf(s -> s.startsWith(CC.GoldB + "Anti M-Kit Crystal ("));
            im.setLore(lore);
        }
        item.setItemMeta(im);
        return item;
    }


    public ItemStack removeAntiMkitCrystalFromItem(ItemStack item) {
        return removeAntiMkitCrystalFromItem(new NBTWrapper(item));
    }

    public static Pair<Set<AntiMkit>, Integer> getFromAntiMkitCrystal(ItemStack item) {
        return getFromAntiMkitCrystal(new NBTWrapper(item));
    }

    public static boolean itemHasMkitCrystals(ItemStack itemStack) {
        return getAntiMkitCrystalsOnItem(itemStack) != null;
    }

    public static Pair<Set<AntiMkit>, Integer> getFromAntiMkitCrystal(NBTWrapper wrapper) {
        if (wrapper.getString("cosmicType").equals("anti_mkit")) {
            NBTTagCompoundBuilder builder = wrapper.getCompound("cosmicData");
            if (builder != null) {
                int success = builder.getInt("success");
                Set<AntiMkit> applied = new HashSet<>();
                if (builder.hasKey("mKit")) {
                    AntiMkit set = mkitFromName(builder.getString("mKit"));
                    if (set != null)
                        applied.add(set);
                } else if (builder.hasKey("mKits")) {
                    NBTTagListBuilder list = new NBTTagListBuilder(builder.getNBTBase("mKits"));
                    for (int i = 0; i < list.size(); i++) {
                        String str = list.getStringAt(i);
                        if (str != null) {
                            AntiMkit set2 = mkitFromName(str);
                            if (set2 != null)
                                applied.add(set2);
                        }
                    }
                }
                return new Pair(applied, success);
            }
        }
        return null;
    }
}
