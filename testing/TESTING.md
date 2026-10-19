# Repeatable integration checks

Use the existing isolated checkout; these commands never create a Git worktree or use a
live server. Current results and exceptions are in [INTEGRATION-REPORT.md](../INTEGRATION-REPORT.md).

## Build and regression suites

Install Java 8, Maven 3.9.11, Python 3, curl, and ripgrep. The prepared cloud tools are under
`/workspace/.onboarding/tools`. With `JAVA_HOME` and `MAVEN` set if using other paths:

```sh
bash testing/verify.sh
```

This recovers unavailable legacy dependencies from supplied jars, records original
SHA-256 hashes in `$ASTRO_BUILD_CACHE/recovered-libraries/manifest.json`, restores the
archived ACF references from absent `shaded.com.google` to canonical `com.google`, and
runs native Maven POMs with a **clean** build. No original dependency jar is edited.
A clean build matters: Maven shade can otherwise retain classes from a previously
shaded output after a dependency changes.

The selected reactor has **77 entries**; AstroFactions (aggregator and two children),
Trials, and PlayerVaultsX are excluded because their remaining legacy dependencies
cannot currently be resolved. Their source/policies were not replaced with placeholders.
This command is not an 82-entry full-reactor success claim. Native tests and the separate
61-test review suite must both complete successfully. Logs distinguish test counts from
modules with no tests.

To investigate the full reactor, use the same Maven settings/cache without `-pl`; keep
its dependency failures distinct from the selected workflow. Never disable certificate
or artifact verification to get past a repository failure.

## Isolated server

`isolated_server.py` needs PyYAML 6.0.3 and Java 8. It creates a fresh uniquely named run
under `/workspace/.integration-server/runs`, binds **127.0.0.1:25575**, stages rebuilt
plugins plus explicitly listed dependencies, records jar hashes, and stops its own process.
It neither accepts the EULA by default nor reads/writes live server data. Supply
`--accept-eula` only after accepting https://aka.ms/MinecraftEULA for this test server.

Optional test-only database credentials are read from an external private JSON file:
`host`, `port`, `database`, `user`, `password`. Do not commit that file or copied runtime
configs. The current isolated MariaDB binds 127.0.0.1:33306. Auction, Core, Kits and Contest
configs are generated only in the new test run. Contest defaults to port 3306 when its
new optional `sql.port` setting is absent.

The runner expects an official Vault 1.7.3 jar at its state directory's `Vault.jar`.
The source is https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar.
Other staged dependencies are the existing repository jars. The original Banana server
fork is used; no WineSpigot license check is bypassed.

```sh
export JAVA_HOME=/workspace/.onboarding/tools/jdk8u472-b08
python3 testing/isolated_server.py --accept-eula --db-config /private/test-db.json --probe SlotGiveProbe
python3 testing/isolated_server.py --accept-eula --db-config /private/test-db.json --probe SlotRewardProbe
python3 testing/isolated_server.py --accept-eula --db-config /private/test-db.json --probe DuelMenuProbe
python3 testing/isolated_server.py --accept-eula --db-config /private/test-db.json --probe KitsLPProbe --lp-profile fresh-kit-check --phase grant
python3 testing/isolated_server.py --accept-eula --db-config /private/test-db.json --probe KitsLPProbe --lp-profile fresh-kit-check --phase verify
```

Use a new profile name for each grant/verify pair. The second run intentionally reuses
only that profile's LuckPerms test data to verify persistence across a real server restart.
Downloaded LuckPerms libraries are cached independently. No production permissions are used.

A successful **probe** exit validates only its explicit assertions; `summary.json` still
records other startup errors. A startup-only run exits nonzero when startup errors exist.
Do not describe a probe success as a fully compatible server. Server-side EntityPlayer
fixtures queue packets locally; they are not official Minecraft clients.

`IntegrationWorlds` creates empty test worlds and a tiny arcade region. These fixtures
permit initialization; they do not reproduce original maps, dungeon layouts, loot tables,
economy stages, world protections, or real-player gameplay.

## Scope

- SlotGiveProbe: eight checks for permissions, aliases, OP/admin/console grants, and public menu access.
- SlotRewardProbe: controlled loot-table fixtures exercise actual rolling and reward delivery;
  ticket insertion/consumption and original reward distributions remain untested.
- DuelMenuProbe: actual team menus open at the expected sizes.
- KitsLPProbe: actual LuckPerms grant, duplicate detection, persisted server context and scope isolation.
- Other imported probes and the read-only linker are retained as upstream diagnostic material;
  their presence is not an executed-test claim. The rejected claim-reset/owner-policy probes
  were not imported into this version.

Never work around missing Multiverse-Core, CrankedUtils, FactionsKore licensing, or maps by
installing dummy replacement plugins or weakening gameplay assertions. Record the blocker.

The runner now rejects probes without a registered contract in `probe_results.py` and
rejects nonzero server exits, even after a successful assertion marker. Run the runner
regressions with `python3 -m unittest discover -s testing -p test_probe_results.py`.
For explicit dependency-absence checks, `--omit-plugin ProxyPatch` omits that plugin
from the disposable run and records the requested omission in its summary. This
option does not make dependent-feature startup failures count as passes.
