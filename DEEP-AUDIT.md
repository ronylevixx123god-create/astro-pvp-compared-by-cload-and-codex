# Deep Astro audit and repair backlog — 2026-10-19

Application baseline: `ce8b7cbb33ee92131d4a921fcf5bc9e994207692` on destination `integration`. This audit adds a repair list and a manual fault reproducer; it does not apply application fixes.

Coverage: 3,522 Java/Kotlin files across 79 source modules were inventoried and screened by patterns; high-risk storage, economy, permissions, lifecycle and resources received manual review. There are 76 top-level plugin descriptors. Every source line and embedded binary has **not** been manually reviewed, so further defects may remain.

The fault reproducer observed **11 defects**: seven used paths when their plugins are enabled, one latent custom batch path, and three dormant jackpot paths. These observations prove failure behavior under the stated fixtures; they are not passing gameplay tests. All test SQL/economy/vendor boundaries were mocked, files were temporary and no live service/server was touched.

**Immediate priority:** rotate/revoke the embedded Tebex credential through secure store settings (S01), and finish data-preservation repairs before starting restored collector/grinder plugins against real data. No credential values are included here.

Follow-up: [REWARD-DATA-FIXES.md](REWARD-DATA-FIXES.md) records targeted repairs for D06 and D09. The counts and reproducer observations below describe the historical audit baseline, not the remaining unresolved total.

Follow-up: [AUCTION-ESCROW-FIX.md](AUCTION-ESCROW-FIX.md) records the E01 confirmation repair and its scope.

Follow-up: [COMMAND-FIXES.md](COMMAND-FIXES.md) records selected A01 command repairs and the remaining command test limits.

Follow-up: [ARKKIT-ALIAS-FIXES.md](ARKKIT-ALIAS-FIXES.md) records Arkkit command registration and alias repairs; unrelated command collisions remain open.

## Priority totals

| Priority | Count | Meaning |
| --- | ---: | --- |
| P0 | 1 | Immediate credential exposure |
| P1 | 23 | Repair before accepting affected gameplay/data paths |
| P2 | 4 | Hardening, validation or documentation repair |
| BLOCK | 5 | External/source/build prerequisite |
| COND | 4 | Dormant or intentional-policy/feature decision |
| TEST | 8 | Required verification gate; not a proven defect by itself |

## Complete identified backlog

Each item separates the observed evidence from the repair and the test that would close it. “Confirmed by inspection” does not mean a real-server failure was reproduced.

### S01 — Rotate and remove embedded Tebex credentials

**Priority:** P0. **Evidence:** Confirmed by redacted source scan; validity deliberately not tested.

Two literal X-Buycraft-Secret assignments remain in tracked ResetGiftcards source. Treat them as exposed; one source read also surfaced a value in tool output. Public history/binaries may retain copies. No values are included in this report or its JSON.

**Repair:** Have the store owner rotate/revoke the exposed credential through secure vendor settings. Read replacement credentials from private configuration/environment, leave committed defaults empty, and fail billing operations closed when unavailable. Preserve Git history; do not force-push a history rewrite as a substitute for rotation.

**Acceptance check:** Private secret scan of current source, history and shipped binaries; confirm old credentials revoked through owner-controlled settings; sandbox billing tests. Do not use the exposed key to test its validity.

**Source:** [GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java:24](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java#L24).

### D01 — Stop discarding failed kit SQL writes

**Priority:** P1. **Evidence:** Reproduced: KIT-QUEUE.

SQLQueryThread removes queued statements even after executeUpdate throws. A temporary database failure loses pending tier/cooldown writes.

**Repair:** Acknowledge work only after confirmed success, retain failed work with bounded backoff, and add durable/idempotent operation tracking. A blind retry of non-idempotent SQL can itself duplicate effects after an ambiguous commit.

**Acceptance check:** Inject prepare/execute/connection failures, ambiguous commit, process restart and recovery. Verify original tier/cooldown semantics and zero lost or double-applied operations.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryThread.java:58](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryThread.java#L58).

### D02 — Terminate kit SQL workers and serialize shutdown flushing

**Priority:** P1. **Evidence:** Reproduced: KIT-THREADS; concurrent flush risk confirmed by inspection.

Both worker loops swallow interrupts and continue forever. onDisable interrupts them and flushes on the calling thread while background work can still run; reload can retain old workers and duplicate processing.

**Repair:** Use an explicit stop condition, bounded join and a single serialized drain/connection owner. Do not rely on interrupt alone or Thread.stop. Report unfinished work safely.

**Acceptance check:** Start/stop/reload repeatedly with blocked queries; prove workers exit, no overlapping execution occurs, and queued work is retained on timeout.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryThread.java:28](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryThread.java#L28), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryCustomThread.java:19](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryCustomThread.java#L19), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/CosmicGlobalKits.java:74](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/CosmicGlobalKits.java#L74).

### D03 — Migrate kit tier/cooldown tables to deliberate uniqueness

**Priority:** P1. **Evidence:** Confirmed for fresh CREATE TABLE definitions; existing DB indexes not inspected.

Tables are created without unique keys while writers use ON DUPLICATE KEY UPDATE. Tier rows can accumulate, and the loader overwrites tier values in an unordered result loop. Existing installations may have different indexes.

**Repair:** Inspect and back up actual schema/data first. Define the correct player/kit/server identity, resolve duplicates with a documented policy, and migrate transactionally. Do not pick MAX(tier) blindly because legitimate resets/downgrades may exist.

**Acceptance check:** Fresh schema plus duplicate legacy fixture, repeated updates, resets, multiple servers and restart. Verify no data loss and stable final tiers.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/CosmicGlobalKits.java:55](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/CosmicGlobalKits.java#L55), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/utils/GlobalCooldowns.java:45](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/utils/GlobalCooldowns.java#L45), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/struct/GlobalKitUser.java:72](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/struct/GlobalKitUser.java#L72).

### D04 — Give kit SQL operations safe connection ownership

**Priority:** P1. **Evidence:** Confirmed by inspection; connection races not stress-tested.

SQLConnection shares an unsynchronized static connection and closes it after a global query counter threshold while other operations may be using it. Reconnect failures can leave a stale connection returned. Resource lifetimes are inconsistent.

**Repair:** Use owned connections or a correctly configured pool and try-with-resources. Add connection health/timeouts, explicit failure propagation, and retain existing table/field behavior.

**Acceptance check:** Concurrent login reads and writes, reconnect at threshold, outage/recovery and shutdown under load; prove no active statement is closed by unrelated work.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLConnection.java:18](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLConnection.java#L18), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLConnection.java:34](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLConnection.java#L34).

### D05 — Preserve collector records for temporarily missing worlds

**Priority:** P1. **Evidence:** Reproduced: COLLECTOR-WORLD.

CollectorManager skips entries whose world is absent, then saves only loaded entries. The load/save reproducer erased a valid missing-world record from its temporary file. Autosave starts after three seconds.

**Repair:** Retain unresolved records verbatim or postpone saving until world loading is complete; merge loaded/unresolved state and write atomically. Do not create a replacement empty world to make real data load.

**Acceptance check:** Load with a missing world, save, restart, restore that world and prove its collector data remains identical. Complete this before enabling collectors against real saved data.

**Source:** [AstroCollectors/src/main/java/me/venir/astrocollectors/collector/CollectorManager.java:62](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/collector/CollectorManager.java#L62), [AstroCollectors/src/main/java/me/venir/astrocollectors/collector/CollectorManager.java:83](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/collector/CollectorManager.java#L83), [AstroCollectors/src/main/java/me/venir/astrocollectors/AstroCollectors.java:59](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/AstroCollectors.java#L59).

### D06 — Restore collector lootboxes into the correct field

**Priority:** P1. **Evidence:** Reproduced with mocked external hooks: COLLECTOR-LOOTBOX.

The constructor reads the saved lootboxes value into tnt, then overwrites tnt from its own field. A saved lootbox count of 7 reloads as 0 while TNT survives.

**Repair:** Load lootboxes independently with backward-compatible numeric defaults and preserve TNT/worth/spawner fields. Avoid resetting unknown data during migration.

**Acceptance check:** JSON save/load roundtrip with nonzero TNT, lootboxes and worth, legacy missing fields, then a real isolated collector reload/claim test.

**Source:** [AstroCollectors/src/main/java/me/venir/astrocollectors/collector/Collector.java:98](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/collector/Collector.java#L98).

### D07 — Snapshot and atomically save collector state

**Priority:** P1. **Evidence:** Concurrency/crash risk confirmed by inspection; no disk-fault stress run.

An async task walks mutable HashMaps while main-thread gameplay changes collectors/spawners. Saves truncate the destination file directly. A race or interrupted write can produce incomplete data.

**Repair:** Take a consistent snapshot on the owner thread, serialize that snapshot off-thread, and replace a temporary file atomically with recovery/backups. Serialize autosave and shutdown saves.

**Acceptance check:** Concurrent collector creation/removal/income during autosave, forced write failure, malformed/truncated file and graceful shutdown; preserve last good data.

**Source:** [AstroCollectors/src/main/java/me/venir/astrocollectors/AstroCollectors.java:55](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/AstroCollectors.java#L55), [AstroCollectors/src/main/java/me/venir/astrocollectors/collector/CollectorManager.java:73](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/collector/CollectorManager.java#L73), [AstroCollectors/src/main/java/me/venir/astrocollectors/collector/Collector.java:366](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/collector/Collector.java#L366).

### D08 — Harden the shared SaveLoadManager used across plugins

**Priority:** P1. **Evidence:** Confirmed by inspection; concurrency/corruption scenarios not reproduced.

The shared cache is a mutable HashMap, save writes directly to the live JSON file, and load catches IOException but not malformed JSON runtime failures. One bad file can abort loading subsequent records. Many gameplay plugins inherit this code.

**Repair:** Use consistent snapshots, atomic replacement, per-file parse/schema validation and quarantine with backups. Preserve every plugin's keys/format and do not silently replace corrupted data with defaults.

**Acceptance check:** Malformed one-of-many files, truncated write, concurrent cache mutation and original data roundtrips across affected subclasses.

**Source:** [StorageUtil/src/main/java/net/astropvp/storage/utils/SaveLoadManager.java:23](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/StorageUtil/src/main/java/net/astropvp/storage/utils/SaveLoadManager.java#L23), [StorageUtil/src/main/java/net/astropvp/storage/utils/SaveLoadManager.java:96](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/StorageUtil/src/main/java/net/astropvp/storage/utils/SaveLoadManager.java#L96), [StorageUtil/src/main/java/net/astropvp/storage/utils/SaveLoadManager.java:75](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/StorageUtil/src/main/java/net/astropvp/storage/utils/SaveLoadManager.java#L75).

### D09 — Repair the grinder reward counter producer

**Priority:** P1. **Evidence:** Reproduced: GRINDER-KEYS.

The running task reads COSMIC but writes SOLAR. Normal creation/loading initializes SOLAR only, so a reward hit can null-unbox and throw. If COSMIC=0 exists, an accumulated SOLAR total of 41 is overwritten with 1. Earlier save/load repairs did not cover this producer.

**Repair:** Use the same intended counter consistently through production, GUI claim, save and reload. Preserve original reward odds and numeric balances; handle absent legacy counters explicitly.

**Acceptance check:** Deterministic reward-hit tests with missing/present counters and accumulated balances; then production/claim/save/reload together. Current bounded audit simulation observed both failure modes.

**Source:** [CustomBlocks/src/main/java/com/conaxgames/customskulls/tasks/MobGrinderRunnable.java:76](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/tasks/MobGrinderRunnable.java#L76), [CustomBlocks/src/main/java/com/conaxgames/customskulls/tasks/MobGrinderRunnable.java:77](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/tasks/MobGrinderRunnable.java#L77), [CustomBlocks/src/main/java/com/conaxgames/customskulls/data/MakeCustomBlocks.java:51](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/data/MakeCustomBlocks.java#L51).

### D10 — Migrate custom-block identities to include the world

**Priority:** P1. **Evidence:** Reproduced: BLOCK-WORLD-KEY.

AbilityData.getUUID uses only XYZ. Blocks at the same coordinates in different worlds share the global data-map key and file name, allowing cross-world overwrites.

**Repair:** Back up files, introduce a world-aware identity, and migrate map keys, filenames, lookups and references together. Use stored location/world data to migrate recoverable files and retain collisions for review; overwritten records may require backups.

**Acceptance check:** Two or more worlds with identical XYZ, owner/ability/key/XP independence, old-format migration, conflicts, restart and deletion of only the intended block.

**Source:** [CustomBlocks/src/main/java/com/conaxgames/customskulls/ability/AbilityData.java:47](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/ability/AbilityData.java#L47), [CustomBlocks/src/main/java/com/conaxgames/customskulls/data/SaveAllGrinderCustomBlocks.java:27](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/data/SaveAllGrinderCustomBlocks.java#L27).

### D11 — Remove grinder state races and unsafe async access

**Priority:** P1. **Evidence:** Confirmed by inspection; races not stress-tested.

The grinder runnable and bulk save are scheduled asynchronously while GUI actions and gameplay mutate the same ability data, crate-key maps and active-grinder HashMap. A saving flag is not synchronization.

**Repair:** Give gameplay state a defined owner thread and save immutable snapshots; keep Bukkit/world operations on supported threads. Coordinate shutdown and avoid dropping the remainder of a save on concurrent modification.

**Acceptance check:** Concurrent activation, reward hit, redemption and save; exact XP/key conservation and no concurrent modification under load.

**Source:** [CustomBlocks/src/main/java/com/conaxgames/customskulls/CoreSkulls.java:134](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/CoreSkulls.java#L134), [CustomBlocks/src/main/java/com/conaxgames/customskulls/CoreSkulls.java:136](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/CoreSkulls.java#L136), [CustomBlocks/src/main/java/com/conaxgames/customskulls/struct/MobGrinderManager.java:29](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/struct/MobGrinderManager.java#L29).

### D12 — Make custom-block loading tolerant and recoverable

**Priority:** P1. **Evidence:** Confirmed by inspection; corrupt-file/missing-world scenarios not run.

Unknown ability names, invalid UUID/location data or unavailable worlds can throw and stop the load loop. Material mismatch deletes the saved file instead of preserving recoverable evidence.

**Repair:** Validate each record independently, defer unavailable worlds, quarantine invalid/mismatched records with clear diagnostics and retain originals. Confirm the intended block-removal policy before changing it.

**Acceptance check:** One malformed file among valid records, missing world, mismatched block, invalid enum/UUID, restoration from quarantine and startup retry.

**Source:** [CustomBlocks/src/main/java/com/conaxgames/customskulls/data/LoadCustomBlocks.java:35](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/data/LoadCustomBlocks.java#L35), [CustomBlocks/src/main/java/com/conaxgames/customskulls/data/LoadCustomBlocks.java:40](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/data/LoadCustomBlocks.java#L40), [CustomBlocks/src/main/java/com/conaxgames/customskulls/data/LoadCustomBlocks.java:44](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/CustomBlocks/src/main/java/com/conaxgames/customskulls/data/LoadCustomBlocks.java#L44).

### D13 — Initialize and migrate the kit SQL server identity

**Priority:** P1. **Evidence:** Confirmed by inspection; no application assignment to serverName found.

Kits declares serverName but never loads the existing server_name configuration into it. Tier inserts format a null value as the literal string null, while reads bind SQL NULL to server_name = ?, which does not match those rows. The previous LuckPerms context/persistence tests use a different server-context path and do not validate this SQL tier identity.

**Repair:** Initialize/validate the configured identity before workers, reads or writes. Inspect existing null/literal-null rows and migrate only with an explicit server mapping and backups; do not merge multiple servers blindly.

**Acceptance check:** Fresh configuration, existing server names, SQL NULL/literal-null legacy rows, two distinct servers, tier write/reload and permission-context independence.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/CosmicGlobalKits.java:33](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/CosmicGlobalKits.java#L33), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/utils/GlobalCooldowns.java:45](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/utils/GlobalCooldowns.java#L45), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/struct/GlobalKitUser.java:62](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/struct/GlobalKitUser.java#L62).

### D14 — Keep kit profile-load failures distinguishable from successful loading

**Priority:** P1. **Evidence:** Confirmed by inspection; full claim-after-outage scenario not reproduced.

GlobalKitUser catches SQL failures and still clears loadingData and invokes completion. CommandGKit then allows a profile with absent/partial tiers and cooldown data past its loading guard. Whether that enables repeated claims needs a real failure-flow test.

**Repair:** Use explicit loading/success/error states, fail kit claims closed until a successful profile load, retry safely and validate the current player session before callbacks. Preserve loaded data on refresh failure.

**Acceptance check:** Fail each SELECT during login/reload, reconnect and retry; no default-tier overwrite, premature claim or lost cooldown; a successful retry restores the correct profile.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/struct/GlobalKitUser.java:95](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/struct/GlobalKitUser.java#L95), [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/commands/CommandGKit.java:52](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/commands/CommandGKit.java#L52).

### E01 — Return auction escrow items when confirmation cannot pay the fee

**Priority:** P1. **Evidence:** Reproduced: AUCTION-CONFIRM.

The confirm handler removes auctionConfirm/auctionPrice metadata before checking the current fee balance. If funds became insufficient, it closes the menu without selling or returning the escrowed item; the close handler no longer sees the metadata.

**Repair:** Keep escrow ownership until listing completion or item return is confirmed. Centralize cancellation/return so fee changes, close, death, disconnect and failure paths cannot lose or duplicate the item.

**Acceptance check:** Open confirmation, reduce balance, confirm and close; exactly one original item must remain with the player/claimable recovery store and no listing/fee must be created.

**Source:** [AuctionHouse/src/main/java/me/recolance/auction/controller/ConfirmMenu.java:96](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/controller/ConfirmMenu.java#L96), [AuctionHouse/src/main/java/me/recolance/auction/controller/ConfirmMenu.java:100](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/controller/ConfirmMenu.java#L100).

### E02 — Make auction purchases, bids, refunds and listing fees checked transactions

**Priority:** P1. **Evidence:** Confirmed by inspection; provider-failure financial flows not executed.

Purchase/bid/buyout paths ignore Vault responses while transferring items, crediting sellers, refunding previous bidders or changing auction state. A preflight balance check does not guarantee a later withdrawal or payout succeeds.

**Repair:** Use checked escrow/settlement states, durable recovery and idempotency for all money/item transitions. Stop on failed debit; retain owed payouts and refunds safely. Preserve listing, bid, fee and item-ID behavior.

**Acceptance check:** Inject each withdrawal/deposit/refund failure, duplicate clicks, offline parties and crashes between stages; verify conservation of money/items.

**Source:** [AuctionHouse/src/main/java/me/recolance/auction/controller/Controller.java:73](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/controller/Controller.java#L73), [AuctionHouse/src/main/java/me/recolance/auction/data/AuctionBid.java:114](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/data/AuctionBid.java#L114), [AuctionHouse/src/main/java/me/recolance/auction/data/AuctionBid.java:136](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/data/AuctionBid.java#L136).

### E03 — Repair auction SQL completion, statement cleanup and shutdown batches

**Priority:** P1. **Evidence:** Confirmed by inspection; failure/restart not executed.

Async SQL failures only log while memory/item changes may already be committed. Statement close is not reliably in finally, batch statements are left open, and the stopping-server branch of asyncExecuteBatch calls execute instead of executeBatch.

**Repair:** Own connections/statements per operation, acknowledge database results, serialize durable state transitions and execute/drain batches correctly on shutdown. Reconcile pending transactions on restart.

**Acceptance check:** SQL outage, partial batch failure, repeated shutdown, connection/resource tracking, restart reconciliation and no resurrected sold listings.

**Source:** [AuctionHouse/src/main/java/me/recolance/auction/data/DataHandler.java:370](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/data/DataHandler.java#L370), [AuctionHouse/src/main/java/me/recolance/auction/data/DataHandler.java:240](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/data/DataHandler.java#L240), [AuctionHouse/src/main/java/me/recolance/auction/data/Database.java:19](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/data/Database.java#L19).

### E04 — Make coinflip debits, winnings and refunds recoverable

**Priority:** P1. **Evidence:** Confirmed by inspection; failure-injection match not run.

Creation/accept/cancel/quit paths ignore payment responses. givePrize marks gavePrize and updates records before checking payout success; a declined deposit is merely logged as unsuccessful after completion is committed.

**Repair:** Debit only into verified escrow, record a pending payout/refund, and mark completion after confirmed settlement. Recover ambiguous operations without replaying payouts.

**Acceptance check:** Failed creator/joiner debit, failed winner/refund deposit, disconnect, cancel, reload and crash at each boundary; keep original wager/tax/reward rules.

**Source:** [Core/src/main/java/net/astropvp/core/cosmiccoinflip/struct/CoinFlipMatch.java:235](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/cosmiccoinflip/struct/CoinFlipMatch.java#L235), [Core/src/main/java/net/astropvp/core/cosmiccoinflip/struct/CoinFlipMatch.java:251](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/cosmiccoinflip/struct/CoinFlipMatch.java#L251), [Core/src/main/java/net/astropvp/core/cosmiccoinflip/commands/CommandCoinFlip.java:104](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/cosmiccoinflip/commands/CommandCoinFlip.java#L104).

### E05 — Expose checked economy operations without breaking legacy callers

**Priority:** P1. **Evidence:** Confirmed by inspection; affected integrations need functional tests.

EconomyUtils deposit/withdraw helpers discard Vault responses. Callers such as Collector.sellWorth clear the owed amount regardless of whether payment succeeds. Gambling callers similarly cannot determine success through the void API.

**Repair:** Add compatible checked helpers/settlement results while retaining old binary signatures for bundled plugins, then migrate critical callers to preserve pending balances on failure.

**Acceptance check:** Declining/erroring economy providers with collector worth, crash/mines/tower and other native callers; no cleared debt or free reward after failure.

**Source:** [Utils/src/main/java/com/cosmicpvp/cosmicutils/utils/EconomyUtils.java:30](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Utils/src/main/java/com/cosmicpvp/cosmicutils/utils/EconomyUtils.java#L30), [AstroCollectors/src/main/java/me/venir/astrocollectors/collector/Collector.java:170](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/collector/Collector.java#L170).

### E06 — Use authoritative crash-game state for cashout

**Priority:** P1. **Evidence:** Unsafe handler confirmed by inspection; ordinary-client exploitability not demonstrated.

CrashListener trusts the GUI title/display-name text for the payout multiplier, checks it against the future final-crash value, and lacks robust inventory/session/item validation. Money decisions should not depend on formatted display text.

**Repair:** Bind clicks to the owned top inventory and round/player session; use the server's current multiplier and checked settlement. Validate finite values and handle null/foreign items and cancelled events.

**Acceptance check:** Foreign same-title inventory, stale round, null/renamed item, locale formatting, early/late cashout, duplicate click and declined payout, with server-authoritative amounts.

**Source:** [AstroGamble/src/main/java/net/astropvp/gambling/crash/listeners/CrashListener.java:48](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroGamble/src/main/java/net/astropvp/gambling/crash/listeners/CrashListener.java#L48), [AstroGamble/src/main/java/net/astropvp/gambling/crash/listeners/CrashListener.java:49](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroGamble/src/main/java/net/astropvp/gambling/crash/listeners/CrashListener.java#L49).

### L01 — Finish missing-world/region handling across gameplay modules

**Priority:** P1. **Evidence:** Runtime failures recorded; remaining dereferences confirmed by inspection.

Arcade, AstroSpaceGrinding, SpaceRecode, Ruins, Dungeon and parkour cleanup still dereference absent worlds/regions. Earlier guards only covered tournament/outpost flight tasks. SpaceRecode shutdown can throw before its later saves.

**Repair:** Validate required worlds/regions before registering features, disable only unavailable features with diagnostics, and keep cleanup/saving safe. Review upstream missing-map commits e299e2394, fc233f964, 2938abc54 and 9861b6383 per path; do not replace original maps with empty fixtures.

**Acceptance check:** Each world/region absent, present, unloaded and restored; no repeated task errors, skipped unrelated saves, lost entry items or wrong-world teleports.

**Source:** [Arcade/src/main/java/net/crankedpvp/arcade/utils/ChunkUtils.java:51](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Arcade/src/main/java/net/crankedpvp/arcade/utils/ChunkUtils.java#L51), [AstroSpaceGrinding/src/main/java/me/astropvp/astrospacegrinding/AstroSpaceGrindingPlugin.java:43](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroSpaceGrinding/src/main/java/me/astropvp/astrospacegrinding/AstroSpaceGrindingPlugin.java#L43), [SpaceRecode/src/main/kotlin/net/astropvp/space/SpacePlugin.kt:85](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/SpaceRecode/src/main/kotlin/net/astropvp/space/SpacePlugin.kt#L85), [AstroEvents/src/main/java/net/astropvp/astroevents/game/games/ParkourMiniGame.java:243](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroEvents/src/main/java/net/astropvp/astroevents/game/games/ParkourMiniGame.java#L243), [Dungeon/src/main/java/com/compilex/CosmicDungeons/CosmicDungeonsPlugin.java:353](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Dungeon/src/main/java/com/compilex/CosmicDungeons/CosmicDungeonsPlugin.java#L353).

### L02 — Make failed-startup and repeated shutdown safe

**Priority:** P1. **Evidence:** Runtime failures recorded plus source confirmation.

Core/Auction/Dungeons and other cleanup paths dereference partially initialized managers after failed enable. These extra exceptions can obscure the original failure and skip later saves/refunds.

**Repair:** Track initialization explicitly, cancel only owned started tasks, close initialized resources safely and make shutdown idempotent. Keep the original cause visible instead of masking mandatory integrations.

**Acceptance check:** Fail startup at each stage, disable twice, normal stop and plugin reload; prove no secondary cleanup exception and correct flushing of initialized state.

**Source:** [Core/src/main/java/net/astropvp/core/AstroCore.java:281](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/AstroCore.java#L281), [AuctionHouse/src/main/java/me/recolance/auction/data/Database.java:40](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AuctionHouse/src/main/java/me/recolance/auction/data/Database.java#L40).

### N01 — Make Tebex bulk reset bounded, asynchronous and structured

**Priority:** P1. **Evidence:** Confirmed by inspection; no live vendor call made.

ResetGiftcards starts synchronous HTTP work from its command handler with no explicit connect/read timeouts, splits raw JSON strings to select gift cards, and logs response contents. Store-affecting operations need reliable parsing and reconciliation.

**Repair:** After S01, use structured JSON/status validation, timeouts, bounded async work, dry-run selection, redacted logging and explicit per-card outcomes. Keep Bukkit-only work on the server thread and test destructive behavior only in an authorized sandbox.

**Acceptance check:** Recorded/redacted API fixtures for malformed/error/empty/paginated responses, 429/timeouts, exact threshold selection and per-ID failures; no live gift cards changed during testing.

**Source:** [GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java:35](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java#L35), [GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java:107](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java#L107).

### A01 — Validate command sender types and numeric inputs

**Priority:** P2. **Evidence:** Confirmed by inspection; individual command matrix not executed.

Several admin commands parse raw ints/doubles or cast senders to Player without friendly validation. Examples include collector give, giftcard creation and coinflip console handling. Non-finite amounts and out-of-range stack quantities need rejection where reachable.

**Repair:** Validate sender, online target, numeric range/finite value and stack count before mutation; use existing permission gates. Do not change authority or enable disabled commands to make them work.

**Acceptance check:** Console/player, missing/offline target, nonnumeric/overflow/negative/zero/NaN/infinite input and aliases; prove no item/money side effect on invalid input.

**Source:** [AstroCollectors/src/main/java/me/venir/astrocollectors/commands/CollectorGiveCommand.java:19](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroCollectors/src/main/java/me/venir/astrocollectors/commands/CollectorGiveCommand.java#L19), [GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/CreateGiftcardCommand.java:17](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/CreateGiftcardCommand.java#L17), [Core/src/main/java/net/astropvp/core/cosmiccoinflip/commands/CommandCoinFlip.java:138](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/cosmiccoinflip/commands/CommandCoinFlip.java#L138).

### A02 — Use UUID value equality for the gift limit

**Priority:** P2. **Evidence:** Confirmed by inspection and active caller; reload scenario not executed.

AstroPlayer compares gift sender UUID objects with ==. Equal UUID values restored as separate objects can bypass the five-gift-per-sender limit.

**Repair:** Use value equality and preserve existing gifts/limit semantics; audit similar UUID comparisons, including dormant jackpot keys and Arcade hiding.

**Acceptance check:** Five saved gifts, reload into distinct UUID objects, sender reconnect and sixth-gift attempt; no reset or duplicate loss.

**Source:** [Core/src/main/java/net/astropvp/core/struct/AstroPlayer.java:60](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/struct/AstroPlayer.java#L60), [Core/src/main/java/net/astropvp/core/commands/GiftCommand.java:57](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/commands/GiftCommand.java#L57).

### A03 — Redact failure logging and keep error context

**Priority:** P2. **Evidence:** Confirmed by inspection.

Kit SQL workers log full failed queries, and giftcard reset logs raw vendor response fragments. Logs can expose player data or store information beyond the context needed for diagnosis.

**Repair:** Log operation IDs/type, safe status and redacted exceptions; keep private diagnostic detail behind controlled settings. Never include credential values in reports.

**Acceptance check:** Failure fixtures containing synthetic sensitive fields must not appear verbatim in logs while error correlation remains usable.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryThread.java:43](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryThread.java#L43), [GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java:54](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/ResetGiftcards.java#L54).

### DOC01 — Separate historical recovery notes from current status

**Priority:** P2. **Evidence:** Confirmed documentation inconsistency.

Imported AstroBAH notes retain historical statements that source is unchanged/missing or reconstruction not used, while this branch contains a reconstructed implementation. Banners help but headlines can still mislead setup and compatibility decisions.

**Repair:** Archive historical reports with their dates/refs, make one current manifest authoritative, and link the reconstruction/source limitations clearly. Preserve historical evidence and backups.

**Acceptance check:** Fresh reader can identify exactly which plugins are built/staged, which behavior differs, current results and what original BAH material is still absent.

**Source:** [AstroBAH/MISSING-SOURCE.md:5](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/AstroBAH/MISSING-SOURCE.md#L5).

### B01 — Restore an authorized FactionsKore IP/licensing route

**Priority:** BLOCK. **Evidence:** Observed runtime/network blocker.

The bundled plugin fails its external-IP lookup and disables; Core then fails in a mandatory FactionsKore integration. This does not establish that the owner's license is invalid.

**Repair:** Use legitimate vendor authorization and an allowed IP/licensing route for the isolated machine. Required hosts observed include checkip.amazonaws.com, license.elytrondev.co and licensing.mckore.com. Do not bypass licensing or replace mandatory behavior with stubs.

**Acceptance check:** Plugin authorization succeeds legitimately, Core enables, and dependent reward/chat/money flows are tested. Secure values belong in environment settings, not chat.

### B02 — Supply compatible Multiverse-Core and resolve its dependency chain

**Priority:** BLOCK. **Evidence:** Observed UnknownDependency failures.

AstroSpawners and AstroCustomBlocks fail on Multiverse-Core; dependent collectors, CustomSkullBlocks and space-grinding plugins then fail. CrankedUtils metadata was already repaired and is not a remaining blocker.

**Repair:** Acquire a trusted Minecraft 1.8-compatible Multiverse build and required world configuration; keep actual required dependencies. Check load ordering and APIs against bundled forks.

**Acceptance check:** All five previously blocked plugin names load with real providers and original worlds, then run gameplay/data tests. Do D05/D10 migration safeguards before loading real data.

### B03 — Restore original maps, regions, NPCs and runtime configuration

**Priority:** BLOCK. **Evidence:** Missing resources; empty fixtures used only for selected initialization.

Original gameplay requires arcade, ruins, events, space/Mars, outpost and dungeon maps plus region definitions/locations. The isolated runner creates tiny empty worlds/one region; those cannot verify the real game.

**Repair:** Obtain original resources or document deliberate replacements with migration review. Validate referenced worlds, dungeon enum world names, WorldGuard regions and feature locations on startup.

**Acceptance check:** Correct portals/arenas/checkpoints/outposts/claims and rewards in the intended maps with real clients. Literal lookups are inventoried separately; variable/config/enum names also need review.

### B04 — Complete native Factions, Trials and PlayerVaultsX builds

**Priority:** BLOCK. **Evidence:** Prior full reactor failed; selected build explicitly excludes these.

Five excluded reactor entries include Factions aggregator/Main/Legacy plus Trials and PlayerVaultsX. The runtime uses an unchanged bundled Factions jar, so source-Factions compatibility fixes have not been validated by that runtime.

**Repair:** Recover/version-pin legitimate legacy APIs/dependencies and build each module. Compare source behavior against the current bundled provider and avoid unverified replacements.

**Acceptance check:** Clean builds, API linkage, together-server tests, claims/permissions/data and vault/trial workflows. Do not count exclusions as passing/skipped tests.

### B05 — Complete unavailable source/reference material

**Priority:** BLOCK. **Evidence:** Access checked during this audit.

Astro-codex is accessible but advertises zero refs/commits. sss is populated at e437ebe555d66cc869d8ece8e39b60445f46e994. The original 2024 AstroBAH implementation is still incomplete in the original archive; current BAH is reconstructed.

**Repair:** Populate/recover the Codex implementation and original missing BAH source or matching compiled artifact if available; preserve refs/history and review before integration.

**Acceptance check:** Compare actual source/behavior and data contracts. Until then, do not claim comparison with a nonexistent Codex version or exact 2024 BAH equivalence.

### Q01 — Repair the latent custom SQL batch API before it is used

**Priority:** COND. **Evidence:** Reproduced: KIT-BATCH; no application-source addQuery caller.

SQLQueryCustomThread removes entries before executeBatch succeeds. The defect is real but current source has no gameplay enqueue caller for that API, so it is not counted as active batch-data loss in normal gameplay.

**Repair:** If retaining this API, acknowledge batch members only after settlement and handle partial/ambiguous failures idempotently. Otherwise deprecate it with compatibility review.

**Acceptance check:** Partial batch failure/retry/restart plus external-call compatibility; no statement disappears before a confirmed durable outcome.

**Source:** [Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryCustomThread.java:38](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Kits/src/main/java/me/cosmicpvp/CosmicGlobalKits/database/SQLQueryCustomThread.java#L38).

### Q02 — Repair jackpot internals only if this dormant feature is restored

**Priority:** COND. **Evidence:** Three defects reproduced; manager/command/listener/timer disabled in current Core.

Allowed ticket counts can overflow int multiplication before becoming double; offline winner payout dereferences a null online Player after resetting the draw; PlayerInfo equality compares UUID references. Winner selection also allocates one entry per ticket, creating an untested scalability risk.

**Repair:** Keep the original inactive state while auditing. Before an approved restoration, use safe arithmetic and weighted selection, correct UUID equality, offline-aware checked payout and a durable draw/settlement state machine.

**Acceptance check:** Boundary ticket quantities, offline winner, provider failure, restart, equal distinct UUID values and large pool probabilities/memory. These are not listed as currently enabled jackpot failures.

**Source:** [Core/src/main/java/net/astropvp/core/AstroCore.java:199](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/AstroCore.java#L199), [Core/src/main/java/net/astropvp/core/cosmicjackpot/struct/Jackpot.java:44](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/cosmicjackpot/struct/Jackpot.java#L44), [Core/src/main/java/net/astropvp/core/cosmicjackpot/struct/JackpotManager.java:123](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/cosmicjackpot/struct/JackpotManager.java#L123), [Core/src/main/java/net/astropvp/core/cosmicjackpot/struct/PlayerInfo.java:25](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Core/src/main/java/net/astropvp/core/cosmicjackpot/struct/PlayerInfo.java#L25).

### Q03 — Review legacy name-bound admin policy before changing it

**Priority:** COND. **Evidence:** Policy issue; not an unauthenticated privilege grant.

Some privileged giftcard/admin paths require both OP and specific legacy names. Replacing names can expand access, while retaining them may prevent the current owner using a command. Existing safe UUID auto-op defaults are not a newly found backdoor.

**Repair:** Document intended authority per command; use configured UUID/permission policy with safe defaults only after that decision. Preserve original gameplay restrictions until verified.

**Acceptance check:** Normal/admin/OP/console identities, renamed accounts, trusted-proxy UUID behavior and explicit denial after revocation.

**Source:** [GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/CreateGiftcardCommand.java:18](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/GiftcardShop/src/main/java/net/astropvp/giftcardshop/commands/CreateGiftcardCommand.java#L18).

### Q04 — Treat the new upstream mask-menu change as a feature decision

**Priority:** COND. **Evidence:** New upstream commit inspected; no merge performed.

COPY integration advanced to 48dcfd7ad; f1b2fc102 adds permission-based mask item retrieval from a menu that is preview-only in the original/current destination. This changes item/permission behavior rather than merely repairing a crash.

**Repair:** Decide whether that feature is wanted, compare with the original /mask give authority and delivery, then test ordinary/admin/OP/full-inventory cases before adopting it. Do not copy it solely because it is newer.

**Acceptance check:** Original preview remains safe for normal players; authorized retrieval creates exactly one intended mask without changing unrelated permissions.

**Source:** [Masks/src/main/java/me/ifamasssxd/cosmicmasks/menu/MaskHelpMenu.java:35](https://github.com/ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/blob/ce8b7cbb33ee92131d4a921fcf5bc9e994207692/Masks/src/main/java/me/ifamasssxd/cosmicmasks/menu/MaskHelpMenu.java#L35).

### T01 — Run a clean full stack with real isolated database configuration

**Priority:** TEST. **Evidence:** Open release-validation gate.

Latest focused server runs still have 109 startup/task error lines. Some intentionally omit DB configuration, so Auction/Contest/Kits failures there are setup failures, not proof those databases cannot work; earlier real-MariaDB checks enabled relevant services.

**Repair:** Configure every database-dependent plugin against a dedicated isolated database, resolve mandatory dependencies and map prerequisites, and classify each remaining error/root cause.

**Acceptance check:** Every required plugin enabled, representative real requests, clean shutdown/restart, no unexpected recurring errors; external telemetry/update-check failures assessed separately from gameplay failures.

### T02 — Retest end-to-end rewards and consumption

**Priority:** TEST. **Evidence:** Prior slot reward probe failed; not rerun by this audit.

The controlled reward probe delivered zero prizes when LootFilter depended on unavailable Core. Slot ticket insertion/consumption and full original reward distributions have not been verified.

**Repair:** After Core is operational, run controlled loot fixtures through the real slot/reward path, then verify original tables/weights, permissions, item consumption, full-inventory delivery and persistence.

**Acceptance check:** Exactly one required consumption/award per action, no duplication on retry/reconnect/reload, and original reward semantics supported by measured tests.

### T03 — Exercise full financial failure and restart scenarios

**Priority:** TEST. **Evidence:** Mostly untested beyond selected unit/probe checks.

AuctionHouse, coinflip, reconstructed BAH, collectors and gambling need real economy/SQL failure, rollback and restart tests. BAH has 19 earlier native tests and journaling, but full real bidding/refund/claim/crash roundtrips were not run here.

**Repair:** Use dedicated accounts/data and deterministic failure providers. Verify original rules while measuring money/item conservation and idempotent recovery.

**Acceptance check:** Failed/ambiguous debit, payout and refund; offline players; duplicate clicks; graceful and abrupt restart; recovery of every pending claim without double payment.

### T04 — Complete permissions/commands/inventory-action coverage

**Priority:** TEST. **Evidence:** Selected slot/trailer/Discord/UUID checks exist; full matrix untested.

Build success and selected probes do not cover every original command, alias, operator/admin boundary or inventory manipulation mode.

**Repair:** Create a command/permission matrix from original files; test normal/admin/OP/console plus aliases, shift/double/number-key clicks, drag, disconnect and cancelled events.

**Acceptance check:** No unauthorized item/money/admin action, and all intended original commands remain available under the correct identity/permission.

### T05 — Verify real map gameplay and official-client compatibility

**Priority:** TEST. **Evidence:** Untested release gate.

Empty fixtures and synthetic EntityPlayers do not validate combat, packet handshake completion, armor effects, dungeon/claim/arena rules or real-client behavior.

**Repair:** Test installed maps and full plugin stack with appropriate clients; include logout/death/teleport, faction roles/allies/enemies, enchantments and restart.

**Acceptance check:** Original commands, gameplay, rewards and permissions observed end-to-end. WineSpigot remains untested; Banana 1.8.8 is the tested isolated server.

### T06 — Validate data migration, backups and ownership limits

**Priority:** TEST. **Evidence:** Untested production migration/crash recovery gate.

Original datasets may contain cross-world key collisions, duplicate SQL tiers, legacy numeric fields and missing-world records. Current envoy ownership is in-memory and cannot identify old orphaned entities or a replacement chest of the same material.

**Repair:** Use copied original datasets, preserve backups and quarantine ambiguous records. Plan world-aware key migration, SQL duplicate resolution and reload/crash behavior before production use.

**Acceptance check:** No original record disappears, no global resets, no foreign entity/block removal, and reversible documented recovery for ambiguous cases.

### T07 — Test external integrations through authorized sandbox fixtures

**Priority:** TEST. **Evidence:** Live Discord/YouTube/Tebex services untested.

Network/credential failures and vendor-side semantics cannot be certified from source alone. Secret rotation must precede any billing integration test.

**Repair:** Use mocked/redacted responses first and authorized sandbox credentials only through secure settings; separate read and write permissions, timeouts and rate limits.

**Acceptance check:** Rewards/roles/giftcards granted only after validated service results, retries do not duplicate effects, and no live user/store action occurs during isolated validation.

### T08 — Publish reproducible dependency and deployment provenance

**Priority:** TEST. **Evidence:** Partial existing recovery scripts/manifests; complete supply-chain/API review untested.

The build relies on version-specific bundled legacy jars and recovered APIs. Duplicate source modules/plugin names require deliberate artifact selection; native source builds and actual staged providers can differ.

**Repair:** Record artifact hashes/origins/licenses/API versions, preserve TLS/signature checks, pin supported Java/server/plugin combinations and produce a one-plugin-name/one-jar deployment manifest. Audit binary/history secrets privately.

**Acceptance check:** Fresh environment reproduces the selected build and server staging without replacing protected original behavior; excluded modules and untested binaries remain explicit.

## Reproduced observations

| Observation | Status | Result |
| --- | --- | --- |
| KIT-QUEUE | REPRODUCED | Failed update was removed from the queue; no retry entry remains |
| KIT-BATCH | REPRODUCED | Batch failure left an empty queue; addQuery has no application-source caller, so this is a latent path |
| COLLECTOR-WORLD | REPRODUCED | Valid saved entry for an unavailable world vanished after load and save of a temporary file |
| COLLECTOR-LOOTBOX | REPRODUCED | Saved lootboxes=7 restored as 0; TNT=5 survived; external hooks were mocked |
| GRINDER-KEYS | REPRODUCED | Actual task hit a null COSMIC counter after 28 iterations; with COSMIC=0 present, SOLAR dropped from 41 to 1 |
| BLOCK-WORLD-KEY | REPRODUCED | Same XYZ in different worlds generated the same data-map/file key |
| AUCTION-CONFIRM | REPRODUCED | Insufficient-fee confirmation removed escrow metadata; close handler returned no item to inventory or world |
| DORMANT-JACKPOT-OVERFLOW | REPRODUCED | Actual purchase method passed a negative cost to the mocked economy: -2.147477296E9 |
| DORMANT-JACKPOT-OFFLINE | REPRODUCED | Offline winner caused NullPointerException after replacement of the current draw |
| DORMANT-JACKPOT-KEY | REPRODUCED | PlayerInfo with equal-valued separate UUID objects compares unequal |
| KIT-THREADS | REPRODUCED | Both worker implementations remained alive after interruption; test daemon threads end with this JVM |

Reproduce with [testing/audit/run.py](testing/audit/run.py) after the documented Java 8/native build and isolated dependency staging. It starts a separate JVM, uses mocks and temporary files, and starts no Minecraft server. Its daemon-thread fixtures end with that JVM. `REPRODUCED` means a defect was observed. After a fix, an observation may become `NOT_REPRODUCED_OR_BLOCKED`; this manual diagnostic is intentionally not part of the passing regression suite.

```bash
python3 testing/audit/run.py
```

The grinder check runs a bounded simulation of its existing random reward branch; the recorded attempt count is an observation, not a fixed timing guarantee. Collector constructor hooks are mocked. Financial/SQL failures use synthetic data only. An initial constructor fixture needed explicit mocked CustomBlocks initialization; the final persisted reproducer completed all 11 observations.

## Runtime, build and source limits

- Latest available isolated server evidence at audit start: EnvoySafetyProbe run `20261019T043610Z-f61bf272`; focused assertions pass, with 109 separate startup/task error lines. No new full Minecraft startup or live vendor call was performed for this audit.
- The earlier selected clean build passed 77 reactor entries and 85 native tests, 61 review tests and eight runner tests at its checkpoint. Later focused envoy changes passed eight envoy tests. This audit did not rerun a clean full build, and it does not claim a new total covering every current plugin.
- Database-dependent plugins intentionally lacked database configuration in many focused runs; earlier isolated MariaDB checks validated selected startup/LuckPerms grant persistence. Distinguish setup omissions from newly reproduced queue/data-loss bugs.
- Original `sss/main` is available at `e437ebe555d66cc869d8ece8e39b60445f46e994`; `Astro-codex` still has zero advertised refs. COPY integration was refreshed to `48dcfd7adff37bf5a4ac86b9ba8c0d5c29631872`; its new mask-menu feature was inspected, not copied.
- [Literal world lookup inventory](comparison/audit-world-lookups.tsv) has 44 references to 10 literal names. Constants/configuration/enum-based names (including arcade/events and additional dungeon worlds) are outside that literal scan; it is not a complete map manifest.
- The secret scan identifies two literal Tebex credential occurrences in tracked text. It does not certify history, archives, binaries or external settings free of secrets. Values are omitted and credentials were not used to call the vendor.

## Important findings deliberately not classified as active bugs

- Jackpot manager, command binding, listener and timer are disabled in original/current Core. Keep it dormant unless restoration is intended; its reproduced defects are conditional Q02.
- HotFix's old event-world flight task is commented out. It is not counted as an active repeating missing-world failure.
- Collector give/reload permissions exist in plugin.yml (`astro.admin`), even though executors lack their own hasPermission calls. This is not evidence of an ordinary-player admin bypass.
- Giftcard creation requires OP and legacy-name checks; those are a policy/usability concern, not proof of unauthenticated access.
- Auction command numeric validation uses a digit regex, so a raw `NaN` price through that command was not claimed as a demonstrated exploit.
- Existing fixes for slot authorization, UUID auto-op, optional ProxyPatch, tournament/outpost flight tasks, grinder fractional-XP persistence and envoy cleanup remain in place. They are not re-listed as unresolved. Their narrower tests do not certify all related gameplay.

## Suggested execution order

1. Rotate credentials and remove literal secrets (S01), then prevent data-loss before re-enabling blocked data plugins (D01–D12).
2. Repair escrow/settlement and crash-game authoritative state (E01–E06); retain original fees, odds, rewards and permissions.
3. Finish lifecycle/map guards (L01–L02), resolve legitimate environment prerequisites (B01–B05) and run a real configured full-stack test (T01).
4. Execute reward/finance/permission/client/migration/service gates (T02–T08), then decide the conditional features/policies (Q01–Q04).

No application source, source repository, destination main, production data or live server was changed by this audit. Existing history and backups remain intact.
