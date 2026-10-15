/*
 * Decompiled with CFR 0.150.
 * 
 * Could not load the following classes:
 *  com.rit.sucy.CustomEnchantment
 *  com.rit.sucy.EnchantmentAPI
 */
package com.cosmicpvp.CosmicSpaceChests.utils;

import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantmentAPI;

public class CustomEnchants {

    public static enum BowCustomEnchants {
        Lightning(EnchantmentAPI.getEnchantment((String)"Lightning")),
        Healing(EnchantmentAPI.getEnchantment((String)"Healing")),
        Piercing(EnchantmentAPI.getEnchantment((String)"Piercing")),
        IceFreeze(EnchantmentAPI.getEnchantment((String)"Ice Freeze")),
        Explosive(EnchantmentAPI.getEnchantment((String)"Explosive")),
        Venom(EnchantmentAPI.getEnchantment((String)"Venom")),
        Teleporation(EnchantmentAPI.getEnchantment((String)"Teleporation")),
        Sniper(EnchantmentAPI.getEnchantment((String)"Sniper")),
        Hellfire(EnchantmentAPI.getEnchantment((String)"Hellfire")),
        ArrowLifesteal(EnchantmentAPI.getEnchantment((String)"Arrow Lifesteal")),
        Virus(EnchantmentAPI.getEnchantment((String)"Virus")),
        Longbow(EnchantmentAPI.getEnchantment((String)"Longbow")),
        Farcast(EnchantmentAPI.getEnchantment((String)"Farcast")),
        Hijack(EnchantmentAPI.getEnchantment((String)"Hijack")),
        Unfocus(EnchantmentAPI.getEnchantment((String)"Unfocus"));

        public CustomEnchantment enchant;

        private BowCustomEnchants(CustomEnchantment ce) {
            this.enchant = ce;
        }
    }

    public static enum ToolCustomEnchants {
        AutoSmelt(EnchantmentAPI.getEnchantment((String)"Auto Smelt")),
        Experience(EnchantmentAPI.getEnchantment((String)"Experience")),
        Detonate(EnchantmentAPI.getEnchantment((String)"Detonate")),
        ObsidianDestroyer(EnchantmentAPI.getEnchantment((String)"Obsidian Destroyer")),
        Telepathy(EnchantmentAPI.getEnchantment((String)"Telepathy")),
        Haste(EnchantmentAPI.getEnchantment((String)"Haste")),
        Skilling(EnchantmentAPI.getEnchantment((String)"Skilling"));

        public CustomEnchantment enchant;

        private ToolCustomEnchants(CustomEnchantment ce) {
            this.enchant = ce;
        }
    }

    public static enum AxeCustomEnchants {
        Berserk(EnchantmentAPI.getEnchantment((String)"Berserk")),
        Bleed(EnchantmentAPI.getEnchantment((String)"Bleed")),
        Blessed(EnchantmentAPI.getEnchantment((String)"Blessed")),
        Decapitation(EnchantmentAPI.getEnchantment((String)"Decapitation")),
        Reforged(EnchantmentAPI.getEnchantment((String)"Reforged")),
        Devour(EnchantmentAPI.getEnchantment((String)"Devour")),
        Pummel(EnchantmentAPI.getEnchantment((String)"Pummel")),
        Insanity(EnchantmentAPI.getEnchantment((String)"Insanity")),
        Barbarian(EnchantmentAPI.getEnchantment((String)"Barbarian"));

        public CustomEnchantment enchant;

        private AxeCustomEnchants(CustomEnchantment ce) {
            this.enchant = ce;
        }
    }

    public static enum SwordCustomEnchants {
        IceAspect(EnchantmentAPI.getEnchantment((String)"IceAspect")),
        Lifesteal(EnchantmentAPI.getEnchantment((String)"Lifesteal")),
        Poison(EnchantmentAPI.getEnchantment((String)"Poison")),
        ThunderingBlow(EnchantmentAPI.getEnchantment((String)"Thundering Blow")),
        Vampire(EnchantmentAPI.getEnchantment((String)"Vampire")),
        Blind(EnchantmentAPI.getEnchantment((String)"Blind")),
        Block(EnchantmentAPI.getEnchantment((String)"Block")),
        DeepWounds(EnchantmentAPI.getEnchantment((String)"Deep Wounds")),
        DoubleStrike(EnchantmentAPI.getEnchantment((String)"Double Strike")),
        Headless(EnchantmentAPI.getEnchantment((String)"Headless")),
        Trap(EnchantmentAPI.getEnchantment((String)"Trap")),
        Epicness(EnchantmentAPI.getEnchantment((String)"Epicness")),
        Paralyze(EnchantmentAPI.getEnchantment((String)"Paralyze")),
        SkillSwipe(EnchantmentAPI.getEnchantment((String)"Skill Swipe")),
        Silence(EnchantmentAPI.getEnchantment((String)"Silence")),
        Voodoo(EnchantmentAPI.getEnchantment((String)"Voodoo")),
        Execute(EnchantmentAPI.getEnchantment((String)"Execute")),
        Disarmor(EnchantmentAPI.getEnchantment((String)"Disarmor")),
        Demonforged(EnchantmentAPI.getEnchantment((String)"Demonforged")),
        Rage(EnchantmentAPI.getEnchantment((String)"Rage")),
        Featherweight(EnchantmentAPI.getEnchantment((String)"Featherweight")),
        Inquisitive(EnchantmentAPI.getEnchantment((String)"Inquisitive")),
        BloodLust(EnchantmentAPI.getEnchantment((String)"Blood Lust")),
        Obliterate(EnchantmentAPI.getEnchantment((String)"Obliterate")),
        KillAura(EnchantmentAPI.getEnchantment((String)"Kill Aura")),
        Training(EnchantmentAPI.getEnchantment((String)"Training")),
        Shackle(EnchantmentAPI.getEnchantment((String)"Shackle")),
        Greatsword(EnchantmentAPI.getEnchantment((String)"Greatsword")),
        Enrage(EnchantmentAPI.getEnchantment((String)"Enrage")),
        Disintegrate(EnchantmentAPI.getEnchantment((String)"Disintegrate")),
        Solitude(EnchantmentAPI.getEnchantment((String)"Solitude"));

        public CustomEnchantment enchant;

        private SwordCustomEnchants(CustomEnchantment ce) {
            this.enchant = ce;
        }
    }

    public static enum HelmetCustomEnchants {
        Glowing(EnchantmentAPI.getEnchantment((String)"Glowing")),
        Implants(EnchantmentAPI.getEnchantment((String)"Implants")),
        Ender_Shift(EnchantmentAPI.getEnchantment((String)"Ender Shift")),
        Aquatic(EnchantmentAPI.getEnchantment((String)"Aquatic")),
        RocketEscape(EnchantmentAPI.getEnchantment((String)"Rocket Escape"));

        public CustomEnchantment enchant;

        private HelmetCustomEnchants(CustomEnchantment ce) {
            this.enchant = ce;
        }
    }

    public static enum BootCustomEnchants {
        Gears(EnchantmentAPI.getEnchantment((String)"Gears")),
        Springs(EnchantmentAPI.getEnchantment((String)"Springs")),
        Anti_Gravity(EnchantmentAPI.getEnchantment((String)"Anti Gravity")),
        Ender_Walker(EnchantmentAPI.getEnchantment((String)"Ender Walker")),
        Nimble(EnchantmentAPI.getEnchantment((String)"Nimble"));

        public CustomEnchantment enchant;

        private BootCustomEnchants(CustomEnchantment ce) {
            this.enchant = ce;
        }
    }

    public static enum ArmorCustomEnchants {
        Hardened(EnchantmentAPI.getEnchantment((String)"Hardened")),
        Poisoned(EnchantmentAPI.getEnchantment((String)"Poisoned")),
        Deathbringer(EnchantmentAPI.getEnchantment((String)"Deathbringer")),
        Enlighted(EnchantmentAPI.getEnchantment((String)"Enlighted")),
        Overload(EnchantmentAPI.getEnchantment((String)"Overload")),
        Tank(EnchantmentAPI.getEnchantment((String)"Tank")),
        Wither(EnchantmentAPI.getEnchantment((String)"Wither")),
        Armored(EnchantmentAPI.getEnchantment((String)"Armored")),
        Self_Destruct(EnchantmentAPI.getEnchantment((String)"Self Destruct")),
        Cactus(EnchantmentAPI.getEnchantment((String)"Cactus")),
        Guardians(EnchantmentAPI.getEnchantment((String)"Guardians")),
        Stormcaller(EnchantmentAPI.getEnchantment((String)"Stormcaller")),
        Angelic(EnchantmentAPI.getEnchantment((String)"Angelic")),
        Drunk(EnchantmentAPI.getEnchantment((String)"Drunk")),
        Insomnia(EnchantmentAPI.getEnchantment((String)"Insomnia")),
        Leadership(EnchantmentAPI.getEnchantment((String)"Leadership")),
        SmokeBomb(EnchantmentAPI.getEnchantment((String)"Smoke Bomb")),
        Lucky(EnchantmentAPI.getEnchantment((String)"Lucky")),
        Curse(EnchantmentAPI.getEnchantment((String)"Curse")),
        SpiritLink(EnchantmentAPI.getEnchantment((String)"Spirit Link")),
        Valor(EnchantmentAPI.getEnchantment((String)"Valor")),
        PlagueCarrier(EnchantmentAPI.getEnchantment((String)"Plague Carrier")),
        Spirits(EnchantmentAPI.getEnchantment((String)"Spirits")),
        UndeadRuse(EnchantmentAPI.getEnchantment((String)"Undead Ruse")),
        Protection(EnchantmentAPI.getEnchantment((String)"Protection")),
        Destruction(EnchantmentAPI.getEnchantment((String)"Destruction")),
        Ragdoll(EnchantmentAPI.getEnchantment((String)"Ragdoll")),
        Ghost(EnchantmentAPI.getEnchantment((String)"Ghost")),
        Creeper_Armor(EnchantmentAPI.getEnchantment((String)"Creeper Armor")),
        Arrow_Deflect(EnchantmentAPI.getEnchantment((String)"Arrow Deflect")),
        Heavy(EnchantmentAPI.getEnchantment((String)"Heavy")),
        Dodge(EnchantmentAPI.getEnchantment((String)"Dodge")),
        Clarity(EnchantmentAPI.getEnchantment((String)"Clarity"));

        public CustomEnchantment enchant;

        private ArmorCustomEnchants(CustomEnchantment ce) {
            this.enchant = ce;
        }
    }
}

