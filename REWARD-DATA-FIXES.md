# Collector and grinder follow-up — 2026-10-19

This follows the baseline audit in DEEP-AUDIT.md. FactionsKore licensing/runtime checks are deferred at the user's request; no license bypass or replacement provider was introduced.

## Selected repairs

- **D06: collector lootbox restoration.** The loader assigned saved lootboxes to TNT and immediately overwrote that field with saved TNT, leaving lootboxes at zero. Restore the lootbox field independently, accepting JSON numeric representations and defaulting missing legacy balances to zero. Preserve TNT, worth, location, spawners and timestamps. Tests exercise construction and repeated serialization with external hologram, custom-block and lootbox hooks mocked.
- **D09: grinder reward counter.** The producer read COSMIC but wrote SOLAR, although loading, saving and claiming use SOLAR. Increment SOLAR with a zero default. Tests exercise the actual scheduled task with deterministic reward/no-reward rolls. Production still uses Math.random; XP, activation, reward odds and timing are unchanged. Existing commented blaze-consumption and additional reward paths remain inactive.

These are narrow corrections to broken destination implementations, rather than whole-file replacements. The original archive also has a mismatched grinder counter; reproducing that crash/overwrite would not preserve useful reward behavior. No compensation or reconstruction of previously lost balances is attempted.

## Validation

- Before correction: grinder tests reproduced a null-counter exception and an overwrite (expected 42, observed 901); collector tests reproduced seven saved lootboxes returning as zero. Initial collector test setup lacked ProtocolLib and CosmicSkyCustomItems API classes; those bundled jars were added only to the test classpath before reproducing the application defect.
- After correction: both affected modules passed Maven clean verify together; nine native tests passed (six new, three existing persistence tests), with no failures or skips.
- Review suite: 61 passed. Server-runner contracts: eight passed.
- Isolated server run `20261019T050902Z-b11cb644`: 90 jars staged including both rebuilt artifacts; ready state reached, envoy-safety probe passed, normal exit code zero. This is a co-staging and unrelated regression check, not a collector/grinder gameplay pass.
- Failed runtime prerequisites: 109 startup error lines; missing Multiverse-Core prevents AstroSpawners and therefore AstroCollectors/CustomSkullBlocks loading. Existing FactionsKore initialization also reports external-IP/license failure. These failures remain unresolved; the user has deferred the FactionsKore prerequisite.
- Follow-up fault observations: nine baseline defects still reproduced, including three dormant jackpot defects. COLLECTOR-LOOTBOX and GRINDER-KEYS no longer reproduced; the historical observer labels these NOT_REPRODUCED_OR_BLOCKED. Deterministic native tests, rather than that ambiguous label or its random observation window, provide the repair evidence.

Real player claims, collector sales, reward delivery, restart behavior with real services and exact gameplay parity remain untested. Mocked data tests do not prove these paths. Astro-codex has no advertised source refs, so its implementation cannot be compared. Other audit items, including missing-world data retention, world-aware grinder identity, database/economy settlement and exposed vendor credentials, remain open.

Only destination integration is changed; source repositories, main, existing backups and live server are outside this repair.
