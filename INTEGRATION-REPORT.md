# Combined Astro integration — 2026-10-19

The current prioritized repair and verification backlog is in [DEEP-AUDIT.md](DEEP-AUDIT.md), with source evidence and isolated fault observations.

Further protection for envoy decorations and changed crate blocks is recorded in [ENVOY-OWNERSHIP-REPORT.md](ENVOY-OWNERSHIP-REPORT.md).

The remaining optional ProxyPatch paths are covered by [OPTIONAL-PROXY-REPORT.md](OPTIONAL-PROXY-REPORT.md).

Further runtime/dependency/runner fixes are recorded in [RUNTIME-SAFETY-REPORT.md](RUNTIME-SAFETY-REPORT.md).

Later tested envoy improvements are recorded in [ENVOY-SAFETY-REPORT.md](ENVOY-SAFETY-REPORT.md).
The counts and test runs below describe the initial integration checkpoint.

This branch combines reviewed fixes with the destination's existing work. It is **not a
production-ready server package or an exact-gameplay-compatibility certification**.
Builds and targeted server checks pass as listed below; the complete stack and reward
handoff still have concrete blockers.

## Repositories and history

| Role | Repository/ref inspected | Exact commit |
| --- | --- | --- |
| Original reference | `x0/Project-AstroPvP/main`, now copied unchanged to `ronylevixx123god-create/sss/main` at the user's request | `e437ebe555d66cc869d8ece8e39b60445f46e994` |
| COPY main | `ronylevixx123god-create/Astro-PVP-COPY-OF-/main` | `463da680560f817649a70f17044818658e28baa7` |
| COPY integration | `ronylevixx123god-create/Astro-PVP-COPY-OF-/integration` | `dbd9ab813be5224459f45dd1bb85a5606de83e79` (earlier integration baseline: `2a27bba19`) |
| Destination before work | `ronylevixx123god-create/astro-pvp-compared-by-cload-and-codex/main` | `1c19e45a22d0ada436a9ab23f7f5790a8a47e14d` |
| Additional source | `ronylevixx123god-create/Astro-codex` | Accessible but no advertised refs/commits; no files to compare |

The exact originally typed destination containing `and=codex` returned Repository not
found. The user confirmed `and-codex` before destination changes. All advertised COPY
branches were fetched into destination-only comparison refs; the source checkout and
remote branches were not changed. See [branches.tsv](comparison/branches.tsv) for heads
and whether their tips are included in COPY integration. The original repository's
`factions/code-cleanup` tip is already an ancestor of its main; it adds no missing tip
changes to the original reference.

The destination's four existing commits remain ancestors of this branch. Its review,
authorization/database fixes, Against the Odds addition and deliberately dormant
enchantments were preserved. A local `backup/destination-before-integration-2026-10-19`
ref retains the starting commit. Existing source backup branches and all isolated test
runs were retained. Destination main and the live server were never modified. The only
source remote write was the separately requested original-file copy into empty `sss`.

## Comparison and selected changes

[paths.tsv](comparison/paths.tsv) records changed/added/omitted paths and Git blob IDs
against the original, COPY integration and destination baseline, covering code, resources,
binaries, POMs and configuration. No original runtime source or resource was deleted;
the nine original-only omitted paths are IDE project metadata already absent from the
destination. A blob match is provenance, not proof of gameplay behavior.

| Area | Decision and reason |
| --- | --- |
| Original commands, loot and data | Retain existing command names/aliases and reward amounts except explicitly listed fixes. No database, permission, world or player-data migration was applied to a live server. |
| Build compatibility | Import reviewed UTF-8 pins, bundled TitleManager, ProtocolLib/WorldEdit ordering, optional Explosive and faction-point hooks, missing AstroSlotBot helper, scoped dependency/shading fixes, and MiniMessage-independent build repairs. Keep the destination's lowercase Banana paths and durability/region-file helpers. |
| Command libraries | Core, LootFilter and Ruins bundle relocated ACF; Duel bundles relocated MF-GUI. The archived ACF bytecode referenced absent `shaded.com.google`; reproducible dependency recovery restores canonical Guava references without changing original jars. ProxyPatch declares its actual CosmicPvPUtils command-library dependency. Clean builds prevent stale shaded classes. |
| Optional integrations | Preserve Discord/YouTube/MySQL review fixes. Import the lazy DiscordSync badge hook, optional Explosive bonuses, optional faction-point rewards and ProxyPatch join guard. Missing integrations do not grant substitutes or fake rewards. |
| Global kits | Restore tier/cooldown access and the finished LuckPerms migration from `56b0e55`, including its original 34 tests plus four SQL-configuration tests. Grants are scoped, saved before an unlock is consumed, and failed grants retain the item. The older `f686366` branch is explicitly unfinished and was not selected. |
| Slot administration | Restrict `give`, `giveheroic`, `enable`, `disable` to `slotbot.admin` (default OP). Preserve public `/slot`, aliases and console reward commands. Arbitrary public ticket creation is an original bug, not behavior to restore. |
| Loot tables | Restore ENVOY because AstroEnvoy references it. Existing table names and stored weights remain intact; original production loot contents were not supplied. |
| CustomSkullBlocks | Import unmerged `f782a6f` compatibility with current crate-key and loot-filter APIs. Keep stored `grinder` keys; correct the upstream bulk-save COSMIC/SOLAR mismatch, continue past missing files, always release the saving flag, and preserve fractional XP on reload. Three tests exercise actual save/reload behavior and failure cleanup. |
| Configurations | Correct collector default key names without changing numeric values. Preserve old four-field tournament locations while accepting yaw/pitch; three tests cover old format, round trip and malformed input. Contest gains an optional SQL port defaulting to 3306. Both kit SQL readers now accept the same flat/nested settings, including the shipped username alias and optional port; failed initialization no longer dereferences an unstarted worker. Existing data/table names and credentials are preserved. |
| BAH | Restore the source branch's recovered/reconstructed implementation and its 19 recovery tests. It is **not the missing 2024 Astro implementation**. Historical serialized names/keys and conservative escrow handling are retained. Exact original timing, menus, economy and recovery behavior remain unverified. |
| Ownership and claims | Reject source-wide reassignment of special cases to XaitX, forced de-op on every join, and the one-time ally/outsider claim-policy rewrite. Preserve destination UUID allowlisting/delayed-grant checks and original Factions data/defaults; do not reset existing permissions. |
| Enchantments | Keep Against the Odds and verified lifecycle/cache fixes already in the destination. Rocket Escape, Guided Rocket Escape, Paladin Armored, Alien Implants, dormant armor durability effects and Ice Aspect remain in their deliberately inactive reference state. No new regeneration or damage rebalance was imported. |
| Deferred changes | Keep destination Factions dependency declarations and PlayerVaultsX implementation rather than replacing them with an unverified dependency/API upgrade. Their full builds remain blocked below. |

The `testing-probes` branch (`f8a17a1`) supplied diagnostic fixtures, not production
logic. Claim-reset and named-owner probes that assert rejected behavior were omitted.
Historical upstream BAH documents and earlier destination comparison notes are retained
with explicit provenance banners; their old run IDs are not evidence for this run.

## Necessary differences and migration limits

- Hard-coded staff auto-OP is replaced by the existing empty-by-default UUID allowlist;
  existing operators are retained and pending grants are rechecked. Existing webhook
  and external-service configuration stays configurable rather than restoring embedded
  credentials from the original files.
- Slot mutation commands now need an admin permission. Console LMS ticket grants still work.
- New kit unlocks use LuckPerms instead of invoking a missing PEX command. Configure the
  intended named server scope; explicit `global` is the only way to request global grants.
- Against the Odds is an existing destination-specific addition, not original Astro behavior.
- BAH is reconstructed. Ambiguous interrupted economy operations stop for reconciliation;
  they are not guessed/replayed. Live bidding/refunds/claims/crash recovery were not retested here.
- Collector resource labels originally said per-hour while the code reads per-minute.
  Fresh defaults now match the reader. Existing files are not overwritten or silently
  converted; original production rates need review before migration.
- Crate enum/API mappings and persistence fixes preserve usable keys/XP rather than
  retaining known loss/crash bugs. No original reward distribution was invented.

Read [SERVER-SETUP.md](SERVER-SETUP.md) before deployment. Empty fixtures do not replace
Astro's original maps, protected regions, loot configuration or production database.

## Checks executed here

### Passed

- Clean native Maven build of **77 selected reactor entries**, using Java 8 and actual
  repository POMs, followed by the review suite: `bash testing/verify.sh`. The final kit SQL compatibility
  change was then rebuilt cleanly with its 38 tests; other module source was unchanged.
- **68 native tests**: Core badges 5; kits 38; grinder persistence 3; tournament location
  compatibility 3; BAH recovery/journal 19. Zero failures/errors/skips in these suites.
- **61 separate review tests**: authorization, linking, SQL round trips with H2, trailer
  URL handling, Against the Odds and retained inactive effects. These are not live service tests.
- The original copy's remote main commit and tree match `x0/Project-AstroPvP/main`.
- Real isolated Banana 1.8.8 servers reached readiness with **90 staged plugins**,
  real Essentials/Vault and a separate real MariaDB at loopback port 33306.
- Slot administration: eight assertions pass for normal/admin/OP/console senders,
  aliases, unchanged config after denied mutations, and public menu access.
- Duel team menus open at 27 and 54 slots with the relocated MF-GUI implementation.
- Real LuckPerms grants return GRANTED then ALREADY_GRANTED on the main thread. After
  a real server restart, the stored permission is true in `astro-integration` and false
  in `othersrv`. The kit plugin uses the real isolated MariaDB, not mocked SQL.

### Failed / externally blocked

- The full 90-plugin stack is **not clean**. FactionsKore reports invalid license/IP
  and disables itself after its external-IP lookup fails. Core then fails in its required FactionsKore chat integration. This does not establish that an existing license is invalid.
  No license validation or gameplay requirement was bypassed.
- A real controlled slot-reward probe **failed**: zero prizes arrived. Its stack trace
  is `LootFilterAPI.giveItem -> AstroCore` after Core's licensed dependency failed.
  This is a diagnosed blocked handoff, not a passing reward or full slot-gameplay test.
- Multiverse-Core is missing, which blocks AstroSpawners, collectors, CustomSkullBlocks
  and related plugins. CrankedUtils is missing for HypeBoxes/PvPTop. No dummy provider
  was installed just to satisfy dependency metadata.
- Original dungeon/outpost maps/regions/configuration are missing. Empty fixtures permit
  some initialization but do not cover hard-coded additional worlds or real gameplay.
- Full native dependency resolution failed for Factions, Trials and PlayerVaultsX.
  Missing prerequisites include archived Spigot/WorldGuard/WorldEdit APIs, legacy JDA/
  PlaceholderAPI/ProtocolLib/Essentials artifacts and CardboardBox/PasteGG dependencies.
  They are explicitly excluded from the selected build, not counted as passed/skipped tests.

### Untested

WineSpigot runtime, official-client combat, original map/dungeon/claim gameplay, all
original reward weights/economy stages, collector production rates, full BAH financial
round trips/crash recovery, live Discord/YouTube services, every plugin/command and
production data migration. Astro-codex still contributes no files/history to compare.
The now available original code permits file-level comparison, not automatic proof that
all original behavior is reproduced.

## Reproduce and inspect evidence

See [testing/TESTING.md](testing/TESTING.md). Raw logs, jar manifests, generated private
configs, isolated database and run folders are outside this checkout under
`/workspace/.onboarding` and `/workspace/.integration-server`. Test configurations and
credentials are not committed. [results.json](comparison/results.json) contains sanitized
build/test summaries and current probe outcomes, including failures. A probe's successful
exit proves only its assertions; unrelated startup errors remain recorded separately.

To unblock the remaining server checks, restore an authorized FactionsKore licensing/IP-lookup route for this test machine
(the bundled jar references `checkip.amazonaws.com`, `license.elytrondev.co`, and
`licensing.mckore.com`; HTTPS IP lookup is currently denied by the network proxy), and provide the missing plugin binaries/dependencies and required
original maps/configuration. Then rerun the failed reward and full-stack checks; do not
deploy this branch to the live server on the basis of compilation alone.

## Final upstream refresh

COPY integration advanced during testing to `dbd9ab813`. Its new commits were inspected.
The missing `globalkit_player_tiers` CREATE TABLE from `4eb7f6d1a` is included: existing
read/write code already uses that table, and IF NOT EXISTS preserves existing data.
The newer optional-ProxyPatch and missing-map guards are deferred, not counted as
verified fixes in this branch. They require targeted unavailable-map/optional-plugin
tests; the map-dependent failures above remain explicitly unresolved.

## Collector and grinder follow-up

See [REWARD-DATA-FIXES.md](REWARD-DATA-FIXES.md) for D06/D09 repairs, regression results and remaining runtime limitations.

## Auction escrow follow-up

See [AUCTION-ESCROW-FIX.md](AUCTION-ESCROW-FIX.md) for the E01 item-return correction, regression results and remaining settlement risks.

## Command validation follow-up

See [COMMAND-FIXES.md](COMMAND-FIXES.md) for collector, giftcard and coinflip input fixes, bounded bulk grants, registration screening and command-specific isolated-server evidence.

## Command inventory

[COMMAND-CATALOG.md](COMMAND-CATALOG.md) lists 553 source entries (including aliases, alternative implementations, inactive definitions and subcommands), with descriptions and source links. The searchable HTML and CSV/JSON versions are in comparison/. Java/Kotlin annotations, 194 descriptor entries and faction constructor routing were checked for catalog coverage. This is documentation, not a claim that all commands load or work in gameplay.

## Arkkit and alias follow-up

See [ARKKIT-ALIAS-FIXES.md](ARKKIT-ALIAS-FIXES.md) for showcase command registration, bounded command-map failure handling and precise alias rewriting, with native and isolated-server evidence.
