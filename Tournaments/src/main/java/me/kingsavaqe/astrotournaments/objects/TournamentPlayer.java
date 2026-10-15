package me.kingsavaqe.astrotournaments.objects;

import com.bryside.astroattributes.enums.RuneType;
import com.bryside.astroattributes.runes.KronosRune;
import com.cosmicpvp.cosmicutils.utils.MaskUtils;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;
import lombok.Getter;
import lombok.Setter;
import me.astropvp.astropvpskins.AstroItemSkinsAPI;
import me.astropvp.astropvpskins.enums.SkinType;
import me.ifamasssxd.cosmicmasks.MaskType;
import me.ifamasssxd.cosmicmasks.utils.MaskApplyUtils;
import me.kingsavaqe.astrotournaments.AstroTournamentsPlugin;
import me.kingsavaqe.astrotournaments.managers.TournamentManager;
import me.kingsavaqe.astrotournaments.utils.ItemBuilder;
import me.kingsavaqe.astrotournaments.utils.ItemUtil;
import net.astropvp.armorsets.AstroArmorSetsAPI;
import net.astropvp.armorsets.struct.heroicArmor.HeroicArmor;
import net.astropvp.storage.utils.KeyToSave;
import org.arkhamnetwork.enchantments.EnchantmentBook;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.UUID;

@Getter
@Setter
public class TournamentPlayer implements KeyToSave {

    private UUID uuid;
    private SkinType helmetSkin;
    private SkinType chestplateSkin;
    private SkinType leggingsSkin;
    private SkinType bootsSkin;
    private SkinType swordSkin;
    private SkinType backpackSkin;
    private int kills;
    private int deaths;
    private int tournamenWins;
    private int tournamenLosses;


    private boolean inTournament;

    private transient final ItemStack helmet;
    private transient final ItemStack chestplate;
    private transient final ItemStack leggings;
    private transient final ItemStack boots;
    private transient final ItemStack sword;


    public TournamentPlayer(UUID uuid) {
        this.uuid = uuid;
        this.helmetSkin = null;
        this.chestplateSkin = null;
        this.leggingsSkin = null;
        this.bootsSkin = null;
        this.swordSkin = null;
        this.backpackSkin = null;
        this.inTournament = false;
        this.helmet = getHelmet();
        this.chestplate = getChestplate();
        this.leggings = getLeggings();
        this.boots = getBoots();
        this.sword = getSword();

    }


    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }


    public ItemStack getHelmet() {
        ItemStack helmet = EnchantmentBook.applyTransmogScroll(ItemUtil.applyCustomEnchantments(AstroArmorSetsAPI.convertArmorToHeroic(new ItemBuilder(Material.DIAMOND_BOOTS, "&6&lTournament Helmet")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .colorize()
                .build(), HeroicArmor.HELMET), getHelmetEnchants()));
        if (helmetSkin != null) {
            if (AstroTournamentsPlugin.instance.getTournamentManager().isBannedItemSkin(helmetSkin)) {
                setHelmetSkin(null);
            } else {
                helmetSkin.apply(helmet);
            }
        }

        if (!AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal().isEmpty()) {
            AstroArmorSetsAPI.applyCrystalToItem(AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal(), helmet);
        }

        helmet = MaskApplyUtils.applyMask(helmet, MaskType.THANOS);

        return helmet;

    }

    public ItemStack getChestplate() {
        ItemStack chestplate = EnchantmentBook.applyTransmogScroll(ItemUtil.applyCustomEnchantments(AstroArmorSetsAPI.convertArmorToHeroic(new ItemBuilder(Material.DIAMOND_BOOTS, "&6&lTournament Chestplate")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .colorize()
                .build(), HeroicArmor.CHESTPLATE), getChestplateEnchants()));


        if (!AstroItemSkinsAPI.hasBackpackSocket(chestplate)) {
           chestplate =  AstroItemSkinsAPI.applyBackpackSocket(chestplate);
        }
        if (!AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal().isEmpty()) {
            AstroArmorSetsAPI.applyCrystalToItem(AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal(), chestplate);
        }

        if (chestplateSkin != null) {
            if (AstroTournamentsPlugin.instance.getTournamentManager().isBannedItemSkin(chestplateSkin)) {
                setChestplateSkin(null);
            } else {
                chestplateSkin.apply(chestplate);
            }
        }

        if (backpackSkin != null) {
            if (AstroTournamentsPlugin.instance.getTournamentManager().isBannedItemSkin(backpackSkin)) {
                setBackpackSkin(null);
            } else {
                AstroItemSkinsAPI.applyBackpackSkin(chestplate, backpackSkin);
                backpackSkin.applyBackpack(chestplate);
            }
        }


        return chestplate;
    }

    public ItemStack getLeggings() {
        ItemStack leggings = EnchantmentBook.applyTransmogScroll(ItemUtil.applyCustomEnchantments(AstroArmorSetsAPI.convertArmorToHeroic(new ItemBuilder(Material.DIAMOND_BOOTS, "&6&lTournament Leggings")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .colorize()
                .build(), HeroicArmor.LEGGINGS), getLeggingsEnchants()));

        if (!AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal().isEmpty()) {
            AstroArmorSetsAPI.applyCrystalToItem(AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal(), leggings);
        }
        if (leggingsSkin != null) {
            if (AstroTournamentsPlugin.instance.getTournamentManager().isBannedItemSkin(leggingsSkin)) {
                setLeggingsSkin(null);
            } else {
                leggingsSkin.apply(leggings);
            }
        }


        return leggings;
    }

    public ItemStack getBoots() {
        ItemStack boots = EnchantmentBook.applyTransmogScroll(ItemUtil.applyCustomEnchantments(AstroArmorSetsAPI.convertArmorToHeroic(new ItemBuilder(Material.DIAMOND_BOOTS, "&6&lTournament Boots")
                .addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 5)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .colorize()
                .build(), HeroicArmor.BOOTS), getBootsEnchants()));

        if (!AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal().isEmpty()) {
            AstroArmorSetsAPI.applyCrystalToItem(AstroTournamentsPlugin.instance.getTournamentManager().getCurrentArmorCrystal(), boots);
        }


        if (bootsSkin != null) {
            if (AstroTournamentsPlugin.instance.getTournamentManager().isBannedItemSkin(bootsSkin)) {
                setBootsSkin(null);
            } else {
                bootsSkin.apply(boots);
            }
        }


        return boots;
    }

    public ItemStack getSword() {
        ItemStack sword = EnchantmentBook.applyTransmogScroll(ItemUtil.applyCustomEnchantments(AstroArmorSetsAPI.convertItemToHeroic(new ItemBuilder(Material.DIAMOND_SWORD, "&6&lTournament Sword")
                .addEnchantment(Enchantment.DAMAGE_ALL, 5)
                .addEnchantment(Enchantment.DURABILITY, 3)
                .addEnchantment(Enchantment.FIRE_ASPECT, 2)
                .colorize()
                .build()), getSwordEnchants()));

        if (swordSkin != null) {
            if (AstroTournamentsPlugin.instance.getTournamentManager().isBannedItemSkin(swordSkin)) {
                setSwordSkin(null);
            } else {
                swordSkin.apply(sword);
            }
        }

        RuneType kronosRune = RuneType.KRONOS;
        sword = kronosRune.apply(sword, kronosRune, 10);

        return sword;
    }

    public LinkedList<CustomEnchantment> getHelmetEnchants() {
        return new LinkedList<>(Arrays.asList(EnchantmentAPI.getEnchantment("Paladin Armored"), EnchantmentAPI.getEnchantment("Reinforced Tank"), EnchantmentAPI.getEnchantment("Alien Implants"), EnchantmentAPI.getEnchantment("Infinite Luck"), EnchantmentAPI.getEnchantment("Martyr Valor"), EnchantmentAPI.getEnchantment("Divine Enlighted"), EnchantmentAPI.getEnchantment("Immortal"), EnchantmentAPI.getEnchantment("Drunk"), EnchantmentAPI.getEnchantment("Death God"), EnchantmentAPI.getEnchantment("Protection"), EnchantmentAPI.getEnchantment("Angelic"), EnchantmentAPI.getEnchantment("Poisoned"), EnchantmentAPI.getEnchantment("Frozen"), EnchantmentAPI.getEnchantment("Stormcaller"), EnchantmentAPI.getEnchantment("Smoke Bomb"), EnchantmentAPI.getEnchantment("Ender Shift")));
    }

    public LinkedList<CustomEnchantment> getChestplateEnchants() {
        return new LinkedList<>(Arrays.asList(EnchantmentAPI.getEnchantment("Paladin Armored"), EnchantmentAPI.getEnchantment("Reinforced Tank"), EnchantmentAPI.getEnchantment("Holy Aegis"), EnchantmentAPI.getEnchantment("Infinite Luck"), EnchantmentAPI.getEnchantment("Martyr Valor"), EnchantmentAPI.getEnchantment("Planetary Deathbringer"), EnchantmentAPI.getEnchantment("Heroic Enchant Reflect"), EnchantmentAPI.getEnchantment("Godly Overload"), EnchantmentAPI.getEnchantment("Vengeful Diminish"), EnchantmentAPI.getEnchantment("Divine Enlighted"), EnchantmentAPI.getEnchantment("Blood Lust"), EnchantmentAPI.getEnchantment("Leadership"), EnchantmentAPI.getEnchantment("Angelic"), EnchantmentAPI.getEnchantment("Dodge"), EnchantmentAPI.getEnchantment("Spirit Link")));
    }

    public LinkedList<CustomEnchantment> getLeggingsEnchants() {
        return new LinkedList<>(Arrays.asList(EnchantmentAPI.getEnchantment("Paladin Armored"), EnchantmentAPI.getEnchantment("Reinforced Tank"), EnchantmentAPI.getEnchantment("Mighty Cactus"), EnchantmentAPI.getEnchantment("Holy Aegis"), EnchantmentAPI.getEnchantment("Infinite Luck"), EnchantmentAPI.getEnchantment("Martyr Valor"), EnchantmentAPI.getEnchantment("Divine Enlighted"), EnchantmentAPI.getEnchantment("Nature Wrath"), EnchantmentAPI.getEnchantment("Blood Link"), EnchantmentAPI.getEnchantment("Clarity"), EnchantmentAPI.getEnchantment("Guardians"), EnchantmentAPI.getEnchantment("Obsidianshield"), EnchantmentAPI.getEnchantment("Angelic"), EnchantmentAPI.getEnchantment("Dodge"), EnchantmentAPI.getEnchantment("Wither")));
    }

    public LinkedList<CustomEnchantment> getBootsEnchants() {
        return new LinkedList<>(Arrays.asList(EnchantmentAPI.getEnchantment("Paladin Armored"), EnchantmentAPI.getEnchantment("Reinforced Tank"), EnchantmentAPI.getEnchantment("Infinite Luck"), EnchantmentAPI.getEnchantment("Martyr Valor"), EnchantmentAPI.getEnchantment("Guided Rocket Escape"), EnchantmentAPI.getEnchantment("Polymorphic Metaphysical"), EnchantmentAPI.getEnchantment("Ethereal Dodge"), EnchantmentAPI.getEnchantment("Divine Enlighted"), EnchantmentAPI.getEnchantment("Phoenix"),  EnchantmentAPI.getEnchantment("Ender Walker"), EnchantmentAPI.getEnchantment("Angelic"), EnchantmentAPI.getEnchantment("Undead Ruse"), EnchantmentAPI.getEnchantment("Repair Guard"), EnchantmentAPI.getEnchantment("Frozen"), EnchantmentAPI.getEnchantment("Planetary Deathbringer")));
    }

    public LinkedList<CustomEnchantment> getSwordEnchants() {
        return new LinkedList<>(Arrays.asList(EnchantmentAPI.getEnchantment("Neutralize"), EnchantmentAPI.getEnchantment("Permanent Execute"), EnchantmentAPI.getEnchantment("Perfect Solitude"), EnchantmentAPI.getEnchantment("Reality Inversion"), EnchantmentAPI.getEnchantment("Unrestrained Enrage"), EnchantmentAPI.getEnchantment("Reflective Block"), EnchantmentAPI.getEnchantment("Deep Bleed"), EnchantmentAPI.getEnchantment("Bloody Deep Wounds"), EnchantmentAPI.getEnchantment("Shadow Assassin"), EnchantmentAPI.getEnchantment("Divine Immolation"), EnchantmentAPI.getEnchantment("Rage"), EnchantmentAPI.getEnchantment("Silence"), EnchantmentAPI.getEnchantment("Double Strike"), EnchantmentAPI.getEnchantment("Devour"), EnchantmentAPI.getEnchantment("IceAspect")));
    }

    @Override
    public String getKey() {
        return uuid.toString();
    }

    @Override
    public void setKey(String s) {
        this.uuid = UUID.fromString(s);
    }


    public void leaveTournament() {
        if (inTournament) {
            if (AstroTournamentsPlugin.instance.getTournamentManager().getPlayerQueue().contains(this)) {
                AstroTournamentsPlugin.instance.getTournamentManager().getPlayerQueue().removeFromQueue(this);
            }
            AstroTournamentsPlugin.instance.getTournamentManager().getTournamentParticipants().remove(this);
            this.inTournament = false;
        }
    }

    public void increaseKills() {
        this.kills +=1;
    }

    public void increaseDeaths() {
        this.deaths +=1;
    }

    public void increaseWins() {
        this.tournamenWins +=1;
    }

    public void increaseLosses() {
        this.tournamenLosses +=1;
    }
    public String getKDR() {
        double kdr;
        if (deaths == 0) {
            kdr = kills;
        } else {
            kdr = (double) kills / deaths;
        }
        return String.format("%.1f", kdr);
    }

    public String getWLRatio() {
        double kdr;
        if (tournamenWins == 0) {
            kdr = tournamenWins;
        } else {
            kdr = (double) tournamenWins / tournamenLosses;
        }
        return String.format("%.1f", kdr);
    }




}

