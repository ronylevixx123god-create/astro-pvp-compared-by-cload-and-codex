package org.arkhamnetwork.enchantments;

import com.earth2me.essentials.Essentials;
import com.rit.sucy.CustomEnchantment;
import com.rit.sucy.EnchantPlugin;
import com.rit.sucy.EnchantmentAPI;
import lombok.Getter;
import lombok.Setter;
import me.ifamasssxd.cosmicskycustoms.CosmicSkyCustomItems;
import org.arkhamnetwork.enchantments.cmds.*;
import org.arkhamnetwork.enchantments.enchantments.armor.*;
import org.arkhamnetwork.enchantments.enchantments.axes.*;
import org.arkhamnetwork.enchantments.enchantments.bow.*;
import org.arkhamnetwork.enchantments.enchantments.heroic.*;
import org.arkhamnetwork.enchantments.enchantments.swords.*;
import org.arkhamnetwork.enchantments.enchantments.tools.*;
import org.arkhamnetwork.enchantments.items.EMPPulseItem;
import org.arkhamnetwork.enchantments.items.OmniBlackscroll;
import org.arkhamnetwork.enchantments.listeners.*;
import org.arkhamnetwork.enchantments.mastery.*;
import org.arkhamnetwork.enchantments.tasks.PlayerPotionEffectPersistTask;
import org.arkhamnetwork.enchantments.util.WorldGuardUtils;
import org.arkhamnetwork.enchantments.util.config.ConfigManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Enchantments extends EnchantPlugin {
    public static volatile ConcurrentHashMap<Player, Location> playerLocations;
    private static Enchantments instance;
    private static Essentials ess3;
    @Getter @Setter private static Hex hex;
    public boolean dungeonsEnabled = false;
    public boolean armorsetEnabled = false;
    private MasteryPermafrost permafrostEnchant;
    private IceAspect iceAspectEnchant;
    @Getter
    private ConfigManager configManager;
    private CustomEnchantment[] registeredEnchantments;
    private NatureWrath naturesWrathEnchant;

    public static Essentials getEssentials() {
        return ess3;
    }

    public static Hex getHex() {
        return hex;
    }

    public static Enchantments getInstance() {
        return instance;
    }

    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.configManager = new ConfigManager(this);
        this.configManager.setupFiles();
        this.registeredEnchantments = null;
        ess3 = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
        playerLocations = new ConcurrentHashMap<>();
        WorldGuardUtils.hook();
        registerEnchantments();
        registerListeners();
        registerCommands();
        registerTasks();

        Bukkit.getScheduler().runTaskTimer(this, () -> {
            for (Player p : Bukkit.getServer().getOnlinePlayers())
                Enchantments.playerLocations.put(p, p.getLocation());
        }, 0L, 40L);

        CommandEnchDust.dustEnabled = (new File("plugins/Enchantments/dust_enabled")).exists();
        CommandEnchDust.soulEnabled = (new File("plugins/Enchantments/soul_enabled")).exists();
        this.dungeonsEnabled = Bukkit.getPluginManager().isPluginEnabled("CosmicDungeons");
        this.armorsetEnabled = (new File("plugins/Enchantments.jar")).exists();
        if (Bukkit.getPluginManager().isPluginEnabled("CosmicSkyCustomItems")) {
            EMPPulseItem empPulseItem = new EMPPulseItem();
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(new OmniBlackscroll());
            CosmicSkyCustomItems.get().getItemManager().registerCustomItem(empPulseItem);
            Bukkit.getPluginManager().registerEvents(empPulseItem, this);
        }

    }

    @Override
    public void registerEnchantments() {
        // EnchantmentAPI calls this hook after clearing its registry during reload.
        if (!isEnabled()) {
            return;
        }
        if (registeredEnchantments == null) {
            registeredEnchantments = new CustomEnchantment[] {
                new Lifesteal(), new Glowing(), new Blind(), new Deathbringer(), new Implants(), new Springs(), new Gears(), new Obsidianshield(), new Block(), new DeepWounds(),
                new Enlighted(), new Frozen(), new Hardened(), this.iceAspectEnchant = new IceAspect(), new Lightning(), new Molten(), new Poison(), new Poisoned(), new ThunderingBlow(), new Vampire(),
                new Trap(), new Overload(), new Headless(), new Haste(), new ObsidianDestroyer(), new DoubleStrike(), new EnderShift(), new Healing(), new Piercing(), new Curse(),
                new Infernal(), new Shockwave(), new Aquatic(), new Explosive(), new Venom(), new Wither(), new Tank(), new Featherweight(), new Epicness(),
                new AntiGravity(), new Immortal(), new Armored(), new Nutrition(), new SelfDestruct(), new SpiritLink(), new Berserk(), new Bleed(), new Blessed(), new Confusion(), new Decapitation(),
                new Ravenous(), new Reforged(), new Cleave(), new Commander(), new Valor(), new Inquisitive(), new Cactus(), new RocketEscape(), new Guardians(), new Teleportation(),
                new Lifebloom(), new PlagueCarrier(), new Obliterate(), new EnderWalker(), new Paralyze(), new Drunk(), new Insomnia(), new SkillSwipe(), new Silence(), new Angelic(),
                new Voodoo(), new Tray(), new Stormcaller(), new Execute(), new AgainstTheOdds(), new Demonforged(), new AutoSmelt(), new Experience(), new Detonate(), new ObsidianDestroyer(), new Oxygenate(),
                new Telepathy(), new Lucky(), new Leadership(), new SmokeBomb(), new Rage(), new ArrowLifesteal(), new Clarity(), new Spirits(), new UndeadRuse(), new Trickster(),
                new BloodLust(), new Devour(), new Protection(), new Destruction(), new Hellfire(), new Ragdoll(), new Sniper(), new Snare(), new CombatMedic(), new Overwhelm(), new HeroicAntiGank(), new DivineLight(), this.naturesWrathEnchant = new NatureWrath(), new DivineImmolation(),
                new Rogue(), new HeroicInversion(), new HeroicInsomnia(), new HeroicValor(), new HeroicAegis(), new HeroicEnrage(), new HeroicCorrupt(), new HeroicSolitude(), new HeroicDevour(), new HeroicHex(),
                new Ghost(), new CreeperArmor(), new KillAura(), new Shackle(), new Pummel(), new Barbarian(), new HeroicBarbarian(), new HeroicDeepWounds(),
                new ArrowDeflect(), new Greatsword(), new Virus(), new Unfocus(), new Teleblock(), new Longbow(), new Inversion(), new Farcast(), new Paradox(), new Solitude(),
                new Heavy(), new Hijack(), new Enrage(), new Dodge(), new Insanity(), new Phoenix(), new Disintegrate(), new BloodLink(), new Resilience(),
                new Sticky(), new Metaphysical(), new Pacify(), new TargetTracking(), new DimensionRift(), new EagleEye(), new Marksman(), new Blacksmith(), new ArrowBreak(), new Assassin(),
                new Aegis(), new AntiGank(), new RepairGuard(), new Fuse(), new Dominate(), new DeathGod(), new Diminish(), new HeroicPlagueCarrier(), new HeroicOverload(), new HeroicInquisitive(),
                new HeroicDeathbringer(), new HeroicEnlighted(), new HeroicBlacksmith(), new HeroicSniper(), new HeroicDiminish(), new HeroicAssassin(), new HeroicDetonate(), new HeroicImplants(), new HeroicLifesteal(), new HeroicArmored(),
                new HeroicDodge(), new HeroKiller(), new BossSlayer(), new HeroicLuck(), new HeroicCleave(), new HeroicExecute(), new HeroicBleed(), this.hex = new Hex(), new Corrupt(), new MasteryMarkOfTheBeast(), new MasteryHorrify(),
                new MasteryPoltergeist(), new MasteryFeignDeath(), new HeroicRocketEscape(), new HeroicTeleportation(), new HeroicTrap(), new HeroicBlock(), new HeroicGhost(), new MasterySoulSiphon(), new MasteryMortalCoil(), new MasteryRotAndDecay(),
                new MasteryDemonicGateway(), new Reflect(), new HeroicReflect(), new MasteryLifesteal(),  new MasteryDeathPact(), new MasteryTombstone(), new MasteryDiscombobulate(), new MasteryExplosivesExpert(),
                this.permafrostEnchant = new MasteryPermafrost(), new HeroicMetaphysical(), new MasteryWebWalker(), new HeroicCactus(), new HeroicTank(), new Sabotage(), new MasteryLavaStrider(), new HeroicInsanity(),
                new HeroicOverwhelm(), new KeyGrind(), new Coins(), //new Shiva(),
                new Treasure(),
                new SoulMaster(), new OpeningCombo(), new MasteryBlackout(), new MasteryNeutralize(),
        };
        }
        // Reuse instances: their listeners and repeating tasks must not be registered twice.
        EnchantmentAPI.registerCustomEnchantments(registeredEnchantments);
    }

    public void onDisable() {
        if (DimensionRift.blockRevertTasks.size() > 0)
            for (HashMap<Location, Material> blocks : DimensionRift.blockRevertTasks.values()) {
                for (Map.Entry<Location, Material> data : blocks.entrySet())
                    data.getKey().getBlock().setType(data.getValue());
            }
        if (this.permafrostEnchant != null)
            this.permafrostEnchant.onDisable();
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EnchantmentBookListener(), this);
        getServer().getPluginManager().registerEvents(new EnchanterListener(), this);
        getServer().getPluginManager().registerEvents(new TinkererListener(), this);
        getServer().getPluginManager().registerEvents(new TinkererTradeListener(), this);
        getServer().getPluginManager().registerEvents(new SoulModeListener(), this);
        getServer().getPluginManager().registerEvents(new CosmicAlchemistListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentOrbListener(), this);
        getServer().getPluginManager().registerEvents(new EnchantmentLoginListener(), this);
        getServer().getPluginManager().registerEvents(new VanillaDamageListener(), this);
        getServer().getPluginManager().registerEvents(new ExploitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerPotionEffectListener(), this);
        getServer().getPluginManager().registerEvents(new OmniBlackScrollListener(), this);
        getServer().getPluginManager().registerEvents(new SilenceListener(), this);
        getServer().getPluginManager().registerEvents(new AutoBlessListener(), this);
        getServer().getPluginManager().registerEvents(new EnderWalkerListener(), (Plugin) Enchantments.getInstance());

    }

    private void registerTasks() {
        getServer().getScheduler().runTaskTimer(this, new SoulModeTask(), 5L, 5L);
        getServer().getScheduler().runTaskTimer(this, new PlayerPotionEffectPersistTask(), 20L, 20L);
    }

    private void registerCommands() {
        getCommand("enchbook").setExecutor(new CommandEnchBook());
        getCommand("enchdust").setExecutor(new CommandEnchDust());
        getCommand("enchorb").setExecutor(new CommandEnchOrb());
        getCommand("enchscroll").setExecutor(new CommandEnchScroll());
        getCommand("transmog").setExecutor(new CommandTransmog());
        getCommand("tinkerer").setExecutor(new CommandTinkerer());
        getCommand("enchanter").setExecutor(new CommandEnchanter());
        getCommand("alchemist").setExecutor(new CommandAlchemist());
        getCommand("apply").setExecutor(new CommandApply());
        getCommand("blackscroll").setExecutor(new CommandBlackscroll());
        getCommand("enchreload").setExecutor(new EnchReloadCommand());
        getCommand("phoenix").setExecutor(new CommandPhoenixCooldown());

    }

    public IceAspect getIceAspectEnchant() {
        return this.iceAspectEnchant;
    }

    public NatureWrath getNaturesWrathEnchant() {
        return this.naturesWrathEnchant;
    }
}
