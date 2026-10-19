# Current integration comparison

The current branch comparison, decisions, reproducible checks, and limitations are in
[INTEGRATION-REPORT.md](INTEGRATION-REPORT.md). The original files are now available in
`ronylevixx123god-create/sss` at `e437ebe555d66cc869d8ece8e39b60445f46e994`, copied unchanged
from `x0/Project-AstroPvP`. The notes below describe the earlier destination work, retained
as provenance rather than evidence for the current integration run.

# AstroPvP comparison and selected version

Compared Project-AstroPvP `e437ebe555d66cc869d8ece8e39b60445f46e994` against Astro-PVP-COPY-OF- `9f1d10c324a7e621ffac4233b4cb2e99afc61efa` on 2026-10-15. Both source checkouts were clean before inspection and remain unchanged. This repository was initially empty and was populated from the COPY repository. At the user’s request, the follow-up review below now adds fixes and regression checks here; neither original repository has been changed.

## Enchantment follow-up

Current user preference: preserve original inactive enchantment behavior only in this comparison repository. Rocket Escape, Guided Rocket Escape, Paladin Armored, Alien Implants and the inactive armor durability calls have been returned to their original code. Ice Aspect’s slowness remains disabled. Against the Odds and non-activation reliability fixes remain. The two original repositories were not changed by this policy update.

The repaired source was on a separate branch that the initial main-branch comparison did not include. Against the Odds and missing enchantment repairs are now restored from `codex/astropvp-restoration`. See [ENCHANTMENT-RESTORATION.md](ENCHANTMENT-RESTORATION.md) for provenance, behavior, 61 passing checks and deployment requirements.

## Initial comparison

Keep the COPY version as the base, with the follow-up fixes below. Across the tracked trees there are 83 differing paths: 72 modified, 2 added in COPY, and 9 IDE files present only in Project. No tracked jar differs. All 16 changed Java files were reviewed; the remaining codebase was identical at the initial comparison.

| Area | Selected improvement | Qualification |
| --- | --- | --- |
| HotFix AutoOP | Empty-by-default UUID allowlist replaces embedded staff usernames; delayed task checks the player is still online. | The follow-up removes the automatic de-op behavior and rechecks delayed grants. UUIDs still require a correctly authenticated server/proxy setup. |
| DiscordSync | Bot token and MySQL connection settings moved to config; missing token disables the plugin; shutdown checks missing initialization. | Bot/database integration was not exercised. The follow-up fixes startup sequencing and cleanup; live integration testing remains outstanding. |
| Core, HotFix, CustomItems, Lootboxes, Revive | Discord webhooks move into config with empty defaults; absent webhook skips notification. CustomItems saves its defaults and Revive creates webhooks.yml. | Notification destinations must be configured privately. |
| Core trailer command | YouTube API key moves from Java into config. | Empty key means trailer API verification is unavailable until configured. |
| Ark and ShowcaseGUI | Embedded integration/database credentials cleared. | Existing credentials in prior source history should be rotated by their owner. This comparison is not a complete credential audit. |
| Utils WorldUtil | RegionFile.closeFile() replaces obfuscated c(). | Correct for the supplied WineSpigot 1.9.15 jar; changing server implementation requires compatibility testing. |
| Maven | Updated repository URLs, HTTPS replacements and removal of puha repository entries. 150 system-scope dependency group IDs receive a local prefix. StorageUtil, LootTable and LootFilter move earlier in the module list. | Local group prefixes distinguish prebuilt jars from reactor coordinates; they do not change jar contents. Full build remains unverified. |
| IDE metadata | COPY omits nine Project-only IDE files. | No gameplay functionality is removed. |

## Follow-up fixes requested by the user

All application changes are made only in this comparison repository.

- **AutoOP:** preserve existing operators instead of stripping OP on every join. Match configured UUIDs case-insensitively and recheck the allowlist, plugin status and player connection before the delayed grant. Removing a UUID before the task runs now cancels that grant.
- **Discord startup/shutdown:** initialize the database first, wait for JDA readiness before registering features, stop initialization on failure, and shut down JDA and the database when disabled. Publish the pending JDA client before waiting so shutdown can cancel login. Avoid logging token-bearing exception messages.
- **Discord authorization:** remove the unrestricted `!ownerupdate` path that granted roles to historical staff accounts. Ignore bot messages and handle malformed verification codes without crashing.
- **Account linking:** consume codes on the Minecraft thread, serialize duplicate-account checks and database insertion, and send success only after persistence succeeds. Run database writes asynchronously and player effects on the Minecraft thread. Missing guild/member/role data no longer causes a null dereference during verification. Duplicate checks cover this manager’s concurrent requests; this is not a database migration for multi-server deployments.
- **Whitelist commands:** ignore unrelated messages rather than sending permission errors to every chat message; check the existing permission role and schedule whitelist changes on the Minecraft thread.
- **Database:** close statements/result sets with try-with-resources, update and invalidate the cache after writes/refreshes, expose a read-only cache snapshot, keep healthy connections instead of periodically closing them, validate connection configuration, bound JDBC connect/read waits and prevent reconnect after shutdown.
- **Trailer verification:** reject lookalike hosts, normalize watch/short/embed/shorts links and historical stored claims to one validated 11-character video ID, handle console and missing API key safely, bound HTTP timeouts, close connections reliably, publish completion across threads, poll for completion, and recheck claimed videos before awarding a pending request. Fix the cooldown to store an expiration timestamp.

The changes retain the COPY version’s configurable credentials/webhooks, WineSpigot closeFile compatibility and build configuration improvements. They are focused fixes to the reviewed areas, not a claim that the entire legacy plugin suite is defect-free.

## Validation

- `python3 review-tests/run.py`: **PASS — 35 tests**, with zero failures. The runner compiles all DiscordSync source files and the changed AutoOP/trailer code against the bundled APIs using Java 8. See [review-tests/README.md](review-tests/README.md) for prerequisites and coverage.
- Tests exercise failure paths, delayed callbacks, unauthorized commands, duplicate links, resource cleanup, malformed URLs, missing credentials, startup/shutdown, and trailer timeout/cooldown behavior. Real SQL link/unlink and cache-refresh operations also run against an embedded H2 database in MySQL compatibility mode. No live accounts are contacted.
- Initial onboarding: root Maven `validate` and StorageUtil `verify` passed, and AutoOP/WorldUtil compiled. StorageUtil had no tests; that build alone was not gameplay validation.
- Full reactor packaging, Factions tests, Minecraft gameplay and live Discord/MySQL/YouTube integrations remain unverified. Utils Maven `verify` now passes after restoring repository access and registering the repository-supplied WineSpigot jar as the missing legacy `com.conaxgames:server:1.8.8-R0.2-SNAPSHOT` artifact. The environment draft’s install_script and start_skill were updated to reproduce this local build setup. No tracked jar or dependency declaration was changed.
- Both source repositories remain clean and unchanged.

## Remaining prerequisites

The environment draft contains installation/startup instructions and Maven domain additions. Draft saving does not establish publication or a fresh-task restore. Use environment settings to review/save and publish the environment when desired.

The previously reported missing `Banana.jar` dependencies were filename-case mismatches: the lowercase `banana.jar` files already exist. Their POM paths are now corrected. Other initially missing target jars are generated by earlier module builds. The enchantment follow-up uses the original Banana/TacoSpigot API, superseding the initial WineSpigot-only build setup.

Discord guild/role IDs and deployment-specific integration settings still need to match the operator’s server. Prior committed credentials should be rotated by their owner. No Minecraft server was started and no EULA was accepted.

## Reproduce the representative build

Use the existing isolated checkouts; do not create Git worktrees unless explicitly requested. In the prepared cloud machine, `bash /workspace/.onboarding/install.sh` refreshes tool/proxy setup, validates Maven project models and builds StorageUtil and Utils. Then run `python3 review-tests/run.py` from this repository. The original repositories must remain unchanged.
