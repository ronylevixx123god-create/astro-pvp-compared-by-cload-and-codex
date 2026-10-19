# AstroPvP server setup checklist

This is a dependency inventory adapted from the COPY repository, not a claim of full
server compatibility. Current builds, live probe results and blockers are recorded in
[INTEGRATION-REPORT.md](INTEGRATION-REPORT.md). Use `bash testing/verify.sh` for the
selected Java 8 build. Its five excluded reactor entries remain explicitly unverified.

## 1. Server

- [ ] Server jar: `.lib/WineSpigot-V1.9.15.jar` (Minecraft 1.8.8; needs your WineSpigot license).
- [ ] Java 8. The isolated checks use the supplied `AstroCollect/lib/banana.jar`; WineSpigot and real-client gameplay remain unverified in this integration.
- [ ] `server.properties`: `online-mode=true` (HotFix gives XaitX owner access by UUID, which needs online mode).
- [ ] Accept the EULA yourself (`eula.txt`).

## 2. Plugins built from this repo

Build with Maven from the repo root, then copy each jar below from `<module>/target/` into `plugins/`.

**Do not install** these build outputs:

| Jar | Why |
|---|---|
| `StorageUtil/target/StorageUtil.jar` | Use `StorageUtil-0.1-shaded.jar` instead (the plain one lacks its libraries). |
| `AstroWildPvP/target/AstroWildPvP-0.1.jar`, `WildPvP/target/WildPvP-0.1.jar` | Unshaded leftovers; use the `AstroWildPvP.jar` from the module you choose (see below). |
| `AstroKits/target/AstroKits.jar`, `Vouchers/target/Vouchers.jar`, `AstroSlotBot/target/AstroSlotBot.jar` | No `plugin.yml`: not deployed plugins. The slot bot Astro used is `Slotbot/target/Slotbot-0.1.jar` (plugin AstroSlotBot). |

**Pick one of each** (two modules build a plugin with the same name; which one Astro ran is not confirmed):

| Plugin name | Option A | Option B |
|---|---|---|
| AstroWildPvP | `AstroWildPvP/target/AstroWildPvP.jar` (com.astropvp.wildpvp, v1.0-DEV) | `WildPvP/target/AstroWildPvP.jar` (com.cosmicpvp.cosmicwildpvp, v1.0) |
| FactionLogs | `FactionLogs/target/FactionLogs.jar` (needs AstroKillCounters) | `AstroFactionLogs/target/AstroFactionLogs.jar` (needs CosmicPvPUtils) |

**Install:**

| Plugin | Jar |
|---|---|
| StorageUtil | `StorageUtil/target/StorageUtil-0.1-shaded.jar` |
| CosmicPvPUtils | `Utils/target/CosmicPvPUtils.jar` |
| Factions | `AstroFactions/target/Factions.jar` |
| HotFix | `HotFix/target/HotFix.jar` |
| Arkkit | `Ark/target/Arkkit.jar` |
| AstroCore | `Core/target/AstroCore.jar` |
| EnchantmentAPI | `EnchantmentsAPI/target/EnchantmentsAPI.jar` |
| Enchantments | `Enchantments/target/Enchantments.jar` |
| CosmicSkyCustomItems | `CustomItems/target/CustomItems-0.1.jar` |
| AstroAdminItems | `AdminItems/target/AdminItems.jar` |
| AstroAntiRightClick | `AntiRightClick/target/AntiRightClick.jar` |
| AstroArcade | `Arcade/target/Arcade.jar` |
| AstroArmorSets | `AstroArmorSets/target/AstroArmorSets-0.1.jar` |
| AstroAttributes | `Attributes/target/Attributes-0.1.jar` |
| AstroCagePvP | `AstroCagePvP/target/AstroCagePvP-1.0.jar` |
| AstroChristmas | `AstroChristmas/target/AstroChristmas.jar` |
| AstroCollectors | `AstroCollectors/target/AstroCollectors-1.0-SNAPSHOT.jar` |
| AstroCrateKeys | `AstroCrateKeys/target/AstroCrateKeys.jar` |
| AstroCustomBlocks | `AstroCustomBlocks/target/AstroCustomBlocks.jar` |
| AstroDamageHandler | `AstroDamageHandler/target/AstroDamageHandler-0.1.jar` |
| AstroEnvoys | `AstroEnvoy/target/AstroEnvoy-1.0.jar` |
| AstroEvents | `AstroEvents/target/AstroEvents-1.0.jar` |
| AstroGambling | `AstroGamble/target/AstroGamble-0.1.jar` |
| AstroGiftCardPlugin | `GiftcardShop/target/GiftcardShop-1.0.jar` |
| AstroItemFilter | `AstroItemFilter/target/AstroItemFilter.jar` |
| AstroItemFlip | `ItemFlip/target/AstroItemFlip.jar` |
| AstroItemSkins | `ItemSkins/target/ItemSkins-0.1.jar` |
| AstroLootFilter | `LootFilter/target/LootFilter-0.1.jar` |
| AstroLootboxes | `MysteryChests/target/MysteryChests.jar` |
| AstroLoottables | `LootTable/target/LootTable.jar` |
| AstroMobCoins | `MobCoins/target/MobCoins.jar` |
| AstroRevive | `AstroRevive/target/AstroRevive.jar` |
| AstroRuins | `Ruins/target/Ruins.jar` |
| AstroScoreboard | `AstroScoreboard/target/AstroScoreboard.jar` |
| AstroShowcase | `ShowcaseGUI/target/ShowcaseGUI.jar` |
| AstroSlotBot | `Slotbot/target/Slotbot-0.1.jar` |
| AstroSpaceGrinding | `AstroSpaceGrinding/target/AstroSpaceGrinding-0.1.jar` |
| AstroSpawners | `AstroSpawnersMobcoins/target/AstroSpawners.jar` |
| AstroTournaments | `Tournaments/target/Tournaments-0.1.jar` |
| AstroWarzoneChests | `WarzoneChests/target/WarzoneChests.jar` |
| AstroWeaponBonuses | `WeaponRunes/target/WeaponRunes.jar` |
| Auction | `AuctionHouse/target/AuctionHouse-0.1.jar` |
| BAH | `AstroBAH/target/AstroBAH-0.1.jar` — **Codex's reconstruction, not Astro's original 2024 plugin** |
| CCVerify | `CCVerify/target/CCVerify-1.0.jar` |
| ChatGames | `ChatGames/target/ChatGames.jar` |
| Collect | `AstroCollect/target/Collect.jar` |
| CosmicBosses | `Bosses/target/CosmicBosses.jar` |
| CosmicConquests | `Conquest/target/Conquest-0.1.jar` |
| CosmicContests | `Contest/target/Contest-0.1.jar` |
| CosmicCustomTNT | `Explosive/target/Explosive.jar` |
| CosmicDisguises | `Disguises/target/Disguises-0.1.jar` |
| CosmicDuel | `Duels/target/CosmicDuel.jar` |
| CosmicDungeons | `Dungeon/target/CosmicDungeons.jar` |
| CosmicFactionBanners | `FactionBanners/target/FactionBanners.jar` |
| CosmicGUIShop | `CosmicShop/target/CosmicShop.jar` |
| CosmicGlobalKits | `Kits/target/Kits.jar` |
| CosmicKOTH | `KoTHs/target/KoTHs-0.1.jar` |
| CosmicMasks | `Masks/target/CosmicMasks.jar` |
| CosmicMysteryChest | `Lootboxes/target/Lootboxes-0.1.jar` |
| CosmicSkyInventoryPets | `Pets/target/CosmicSkyInventoryPets.jar` |
| CosmicSpaceChests | `SpaceChests/target/SpaceChests-0.1.jar` |
| CustomSkullBlocks | `CustomBlocks/target/CustomBlocks.jar` |
| LPXPatches | `LPXPatches/target/LPXPatches.jar` |
| NebulaShop | `NebulaShop/target/NebulaShop.jar` |
| OutpostPatches | `Outpost/target/Outpost-0.1.jar` |
| PlayerVaults | `PlayerVaultsX/target/PlayerVaultsX.jar` |
| SpaceRecode | `SpaceRecode/target/SpaceRecode.jar` |

**Known not to load yet** (installing them only logs an error):

| Plugin | Reason |
|---|---|
| AstroHypeBoxes, AstroPvPTop | Need `CrankedUtils`, which no jar in Astro's files provides. |
| ProxyPatch | **Don't install.** Not a patch: it adds fake players (`spoof add <name>`) to inflate the player count and makes them create/join factions. CCVerify works without it. |
| AstroDiscordSync, Trials | Not built (skipped for now). |

## 3. Third-party plugins from Astro's files

Copy these jars from the repo into `plugins/` (paths are relative to the repo root).

| Plugin | Jar in Astro's files | Needed by |
|---|---|---|
| WorldEdit 6.1.7.3 | `Enchantments/lib/worldedit-bukkit-6.1.7.3.jar` | WorldGuard, CosmicDungeons, CosmicKOTH, CosmicSpaceChests, CosmicGlobalKits |
| WorldGuard 6.1 | `AstroCagePvP/lib/WorldGuard.jar` | **required:** Arkkit, AstroCore, CosmicPvPUtils. Not the `cosmicpvp-5.9` copy in most `lib/` folders: CosmicPvPUtils, CosmicDuel, CosmicDungeons, AstroCagePvP and Factions are compiled against 6.x (`ApplicableRegionSet` is an interface there) and fail with `IncompatibleClassChangeError` on 5.9. |
| ProtocolLib 4.5.1 | `HotFix/lib/ProtocolLib.jar` | **required:** AstroRuins, CCVerify, CosmicMasks, CustomSkullBlocks; HotFix's XaitX owner access |
| Essentials | `AstroCollectors/lib/Essentials.jar` | **required:** AstroCollectors, AstroCore, Enchantments |
| LuckPerms 5.4.30 | `Core/lib/LuckPerms-Bukkit-5.4.30.jar` | Factions, CosmicGlobalKits (kit unlocks are LuckPerms permissions) |
| FactionsKore | `.lib/FactionsKore-1.3.62.1.jar` | **required by AstroCore** (chat, MOTD, outposts) although AstroCore doesn't declare it; also PvPTop, AstroSpawners |
| Commons (2021-12-01) | `Ruins/lib/Commons.jar` | **required:** AstroRuins, NebulaShop. Use this copy (newer superset of `NebulaShop/lib/Commons.jar`). |
| BuycraftX 12.0.7 | `CCVerify/libs/BuycraftX.jar` | **required:** CCVerify (and so AstroRuins) |
| AstroKillCounters | `Ruins/lib/AstroKillCounters-1.0 (6).jar` | **required:** AstroTournaments, FactionLogs (option A) |
| HolographicDisplays | `AstroCollect/lib/HolographicDisplays (2).jar` | **required:** AstroCollectors, AstroEvents |
| NBTAPI 2.7.1 | `AstroCustomBlocks/lib/item-nbt-api-plugin-2.7.1.jar` | **required:** AstroArmorSets, AstroHypeBoxes |
| TAB 3.1.2 | `AstroScoreboard/lib/TAB.v3.1.2.jar` | **required:** AstroScoreboard (built against TAB 3's `me.neznamy.tab.api.TabAPI`; the TAB 2.8.10 jar in the same folder doesn't have it) |
| Kotlin 1.3.72 | `Dungeon/lib/Kotlin-1.3.72.jar` | **required:** ChatGames |
| LibsDisguises | `Ark/lib/LibsDisguises.jar` | **required:** CosmicDisguises |
| Citizens | `Ark/lib/Citizens.jar` | optional: Arkkit |
| mcMMO | `Ark/lib/mcMMO.jar` | optional: Arkkit, Enchantments |
| CoreProtect 2.12.0 | `Ark/lib/CoreProtect.jar` | **required by FactionsKore** (its chest-logs feature fails with `NoClassDefFoundError: net/coreprotect/CoreProtectAPI` without it, and then AstroCore can't enable either); optional for Factions |
| FactionUpgrades | `Ark/lib/FactionUpgrades.jar` | optional: Enchantments |
| CosmicArmorSets | `AstroCollect/lib/CosmicArmorSets.jar` | optional: Arkkit |
| CompilexPermissions | `Ark/lib/CompilexPermissions_2_1_1_1.jar` | optional only (CosmicPvPUtils, Kits, Disguises, SpaceChests) |

**Not in Astro's files — download the exact version:**

- [ ] **Vault 1.7.3** — required by AstroCore, Auction, BAH, CosmicGUIShop, CustomSkullBlocks.
- [ ] **Multiverse-Core 2.5.0** — required by AstroCustomBlocks and AstroSpawners.
- [ ] **PlaceholderAPI 2.11.6** — required by FactionsKore (it registers its placeholders on enable and fails
  with `NoClassDefFoundError: me/clip/placeholderapi/...` without it, although it doesn't declare it).

## 4. Worlds

- [ ] `world_ruins` — AstroRuins' mob spawn and no-fly tasks use this world; without it they log NullPointerExceptions every few ticks.
- [ ] Astro's other maps (not in the repo). Without them these plugins log errors or don't enable:
  `world_arcade` with WorldGuard region `arcade_blocks` (AstroArcade), `space` and `mars` (SpaceRecode,
  AstroSpaceGrinding), `tournament_world` (AstroTournaments), `world_outpost_mars`, `world_outpost_pluto`
  (outposts), `event`, and the dungeon worlds named in CosmicDungeons' config (`dungeon_yijki_a`,
  `dungeon_planet_nul`, ...).

## 4b. CosmicDuel arenas (do this before players join)

- [ ] Replace the `Arenas:` section of `plugins/CosmicDuel/config.yml`. Astro's default has a placeholder arena
  `test` in the main world (`world`). CosmicDuel treats the world of the first arena as the duel world, so with
  that placeholder every non-OP player in the main world counts as a duel spectator: all their commands are
  blocked ("You cannot use commands while spectating.") and they are teleported to spawn. Use `Arenas: {}`
  until the real arenas (in a separate duel world, e.g. `world_duels`) are set up.

## 4c. FactionsKore features that take over Astro commands

- [ ] In `plugins/FactionsKore/`, set `enabled: false` in `customenchants.yml`, `mobcoins.yml`, `spectate.yml` and
  `withdraw.yml` (a file with only `enabled: false` is enough; FactionsKore adds the rest). With FactionsKore's
  defaults these features register `/ce`, `/coins`, `/mobcoins`, `/spectate` and `/withdraw` over AstroCore,
  AstroMobCoins, CosmicDuel and CosmicGUIShop. Astro's code only uses FactionsKore's shield, outpost, grace and
  KOTH features.

## 5. Permissions and access

- [ ] Staff who give slot bot tickets or turn the slot bot on/off need `slotbot.admin` (default: OP). Console always works (AstroCore's Last Man Standing reward uses `slotbot give <player> 1` from console).
- [ ] `/nebula shop` needs `astrocore.nebula`.
- [ ] `/f show` needs CosmicKOTH **and** AstroSpawners installed (it reads KOTH wins and faction top from them); without them the command errors.

## 6. Existing data and configuration

This destination retains its Factions source and permission defaults. The COPY branch's
outsider claim restrictions and one-time ally-permission reset were deliberately not
imported. Do not run that branch's configuration-reset instructions on this version.

Back up existing plugin data before any deployment. No live data was migrated here.

- Tournaments now accepts both historical `world,x,y,z` and full
  `world,x,y,z,yaw,pitch` locations. Existing coordinates do not need to be rewritten.
- AstroCollectors' corrected fresh defaults use `spawner-income-per-minute` and
  `tnt-per-minute`, which match the reader. Existing `settings.yml` is not overwritten.
  Review old per-hour keys and intended production rates before migration; no automatic
  60-fold conversion or claim of original economic equivalence is made.
- Global kits retain existing cooldown/tier fields. New unlocks use LuckPerms; read
  [Kits/LUCKPERMS.md](Kits/LUCKPERMS.md) and choose a named server scope, or explicitly
  opt into global grants. Failed grants retain the unlock item.
- CustomSkullBlocks retains the existing `grinder` YAML key. Its bulk and individual
  writers and loader now agree on SOLAR keys; fractional XP is preserved on reload.
- BAH is a documented reconstruction. Preserve legacy auction files and any pending
  journal; ambiguous interrupted economy operations require reconciliation, not replay.

## 7. Known open items

- AstroCore uses FactionsKore without declaring it in `plugin.yml`; load order isn't guaranteed.
- Factions was not rebuilt in the selected 77-module workflow; do not count a zero-test run as validation.
- Astro's own command bugs, left unchanged: `/akoth` without arguments and `/removeskin` (both OP-only) throw an
  internal error; `/lootboxtoggle` and `/lootboxfilter` are rewritten by AstroCore to `/mctoggle`/`/mcfilter`
  (unknown) - use `/lbtoggle` and `/lbfilter`. `/bombs` and `/roulette` are disabled in Astro's code.
- HotFix removes OP from every player on join except the UUIDs in `auto-op-uuids`; give staff permissions
  through LuckPerms instead of `/op`.
- Enchantments, as in Astro's code: Enchanter books and scrolls are free (prices 0 since 2021-01-01; only Mastery
  costs XP); Tinkerer trading has been commented out since 2021-04-12 (the window opens, trades do nothing);
  AstroCore blocks `/alchemist` and `/ce` for players without `commandfix.bypass` - players use `/enchants`.
- WineSpigot, real game clients, and full original-map gameplay remain untested; the database-backed parts (AstroCore, slot bot rewards,
  BAH/Auction, land titles) need the database first.
