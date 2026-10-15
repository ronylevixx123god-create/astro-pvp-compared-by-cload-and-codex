# Enchantment restoration

The earlier comparison covered the repositories' `main` branches. The user-created **Against the Odds** enchantment and several gameplay repairs were instead on `Project-AstroPvP` branch `codex/astropvp-restoration`, commit `63127bc6796700f1ac2eefb3d7caba916f56aff5`. This update restores that branch's enchantment-specific changes and required shared durability helper into the comparison repository. Both source repositories remain unchanged. Other, unrelated restoration-branch changes have not been merged here.

## Restored behavior

- **Against the Odds:** Legendary sword enchant, levels I–III, +3% / +6% / +10% damage. Requires at least three active opponents within 17 blocks. Same-faction/allied players are excluded as opponents. Equal-sized active sides qualify; a friendly numerical advantage does not. Combat activity expires after six seconds. Projectile hits can establish combat activity, but the sword damage bonus only applies to direct melee hits. Hidden, dead, offline and spectator opponents do not count.
- **Rocket Escape, Guided Rocket Escape and Paladin Armored:** corrected defense method signatures now override the API's actual `EntityDamageEvent` hook instead of declaring unused overloads.
- **Alien Implants:** restored equip/unequip tracking and its regeneration timers.
- **Rage:** restored compatible shared-cache imports.
- **Mark of the Beast:** timed cleanup removes the UUID key instead of trying to remove the player object from a UUID-keyed map.
- **Armor durability effects:** restored the shared add/subtract durability helpers and their call sites. Removed the unused JavaFX import that prevented compiling Pummel on the cloud JDK.
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
- `python3 review-tests/run.py`: **60 tests pass**, including the previous 35 tests, 17 Against the Odds checks and 8 shared lifecycle/defense/compatibility checks.
- Against the Odds tests run its production tracking/activation methods using player damage events, deterministic time and injected faction relationships. They cover bonus levels, active vs idle opponents, equal teams, friendly numerical advantage, range/window boundaries, canceled/zero damage, allied/invisible/spectator opponents, projectiles, quit cleanup, and description preservation.
- The defense-hook overrides, Alien Implants ticking/unequipping, reload registry restoration and both region-file close APIs have targeted checks.
- **Normal official-client combat and live server integration remain unverified.** These tests do not certify every legacy enchantment or every plugin. Original intentionally inactive enchantments, such as Ice Aspect's commented-out slowness, have not been enabled or rebalanced.

From the repository root, the prepared cloud environment can run:

```sh
bash /workspace/.onboarding/install.sh
python3 review-tests/run.py
```

Build outputs are `Enchantments/target/Enchantments.jar` and `Utils/target/CosmicPvPUtils.jar`. A deployment needs both matching rebuilt plugins, the original server fork and their normal dependencies (including EnchantmentAPI and ParticleLIB). The old jars inside `lib/` are build dependencies, not a newly deployed server package. No running server, original repository, production data or EULA setting was changed.
