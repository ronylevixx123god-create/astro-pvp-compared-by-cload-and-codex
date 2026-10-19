> Current integration: see [INTEGRATION-REPORT.md](INTEGRATION-REPORT.md). The existing
> Against the Odds and inactive-effect policy is retained. Earlier source checkout IDs and
> environment paths below are historical; current validation is reported separately.

# Enchantment restoration

The earlier comparison covered the repositories' `main` branches. The user-created **Against the Odds** enchantment and several gameplay repairs were instead on `Project-AstroPvP` branch `codex/astropvp-restoration`, commit `63127bc6796700f1ac2eefb3d7caba916f56aff5`. Selected compatibility fixes and Against the Odds were recovered from that branch. At the user’s later request, effects that were inactive in the original Astro code are preserved as inactive in this comparison repository. Both source repositories remain unchanged. Other, unrelated restoration-branch changes have not been merged here.

## Current behavior: preserve original inactive effects

Only this comparison repository was changed for this policy update. Project-AstroPvP stays at `68d1e39f6ec7ad9468fd5b6d6f7194dd916a6d4f`; Astro-PVP-COPY-OF- stays at `663c3374c9ce4a67316ef79ecab243fb3d8aa28a`. Their earlier user-authorized fixes are left as they were.

The baseline is the original main-branch enchantment source, before restoration (`3c4a619` in this repository). Rocket Escape, Guided Rocket Escape, Paladin Armored, Alien Implants and ArmorUtil are now byte-for-byte restored to that baseline. Ice Aspect and Disintegrate retain their original commented-out effects. This preserves dormant behavior without removing enchantment names, lore or items. Against the Odds remains the user-approved custom addition.

## Enchantment details

- **Against the Odds:** Legendary sword enchant, levels I–III, +3% / +6% / +10% damage. Requires at least three active opponents within 17 blocks. Same-faction/allied players are excluded as opponents. Equal-sized active sides qualify; a friendly numerical advantage does not. Combat activity expires after six seconds. Projectile hits can establish combat activity, but the sword damage bonus only applies to direct melee hits. Hidden, dead, offline and spectator opponents do not count.
- **Rocket Escape, Guided Rocket Escape and Paladin Armored:** the original unused defense overloads are preserved. The API’s normal defense hook remains inactive for these enchantments; the previous activation fixes have been reverted here.
- **Alien Implants:** the original stub is preserved. No restored regeneration timer or equip effect is enabled.
- **Rage:** restored compatible shared-cache imports.
- **Mark of the Beast:** timed cleanup removes the UUID key instead of trying to remove the player object from a UUID-keyed map.
- **Armor durability effects:** the originally commented-out ArmorUtil durability calls remain commented out. Shared helper methods remain available for compatibility but these effects do not call them. The unused JavaFX import was removed so Pummel compiles; its gameplay logic was not changed.
- Restored the `ParticleLIB` dependency declaration used by the repaired branch.

Additional fixes preserve server `config.yml` on restart, initialize settings before enchantment constructors, restore custom enchantments after API reload, and reuse instances so reload does not duplicate their listeners/timers. `/enchreload` reloads the main config and cached descriptions. Against the Odds has a description fallback for existing configs; configured descriptions still take priority.

## Correct build target

The repaired code expects the original Banana/TacoSpigot fork, including its custom `CraftItemStack.modifyDurability` API. The earlier WineSpigot-only setup compiled Utils but was not sufficient for these restored enchantments. The bundled `AstroCollect/lib/banana.jar` matches the repaired branch's manifest SHA-256:

```
f6cded1600e4b58d978faf45a4b113884540b514afad7c7ebca85b36d0929d5f
```

The reusable environment script now registers this original jar for the legacy server dependency coordinates. Region-file closing supports both the original fork's `c()` and WineSpigot's `closeFile()`, preserving IO errors. No server jar has been modified. The two previously reported missing `Banana.jar` dependencies were actually filename-case mismatches; their POMs now refer to the existing `banana.jar` files.

## Validation and use

- Java 8 Maven `verify` passes for **StorageUtil, Utils and Enchantments**. These modules have no Maven tests; packaging success is distinct from the regression suite below.
- `python3 review-tests/run.py`: **61 tests pass**, including the previous 35 tests, 17 Against the Odds checks and 9 shared lifecycle/inactive-behavior/compatibility checks.
- Against the Odds tests run its production tracking/activation methods using player damage events, deterministic time and injected faction relationships. They cover bonus levels, active vs idle opponents, equal teams, friendly numerical advantage, range/window boundaries, canceled/zero damage, allied/invisible/spectator opponents, projectiles, quit cleanup, and description preservation.
- Targeted checks confirm the original defense hooks remain inactive, Alien Implants equips without starting regeneration, and armor durability is unchanged by the dormant helper calls. Reload registry restoration and both region-file close APIs are also covered.
- **Normal official-client combat and live server integration remain unverified.** These tests do not certify every legacy enchantment or every plugin. Original intentionally inactive enchantments, such as Ice Aspect's commented-out slowness, have not been enabled or rebalanced.

From the repository root, the prepared cloud environment can run:

```sh
bash /workspace/.onboarding/install.sh
python3 review-tests/run.py
```

Build outputs are `Enchantments/target/Enchantments.jar` and `Utils/target/CosmicPvPUtils.jar`. A deployment needs both matching rebuilt plugins, the original server fork and their normal dependencies (including EnchantmentAPI and ParticleLIB). The old jars inside `lib/` are build dependencies, not a newly deployed server package. No running server, original repository, production data or EULA setting was changed.
