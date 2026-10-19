> Imported upstream recovery notes. Their earlier run IDs, environment paths and live
> results are historical and were not reproduced merely by copying this file. See the
> current [integration report](../INTEGRATION-REPORT.md) for checks executed here.

# Integration test environment and current results

## Current status

| Capability | Evidence | Status |
| --- | --- | --- |
| History GUI and pagination | Earlier source recovered at `8570cc966^`; new page/order test passed | Restored; live GUI clicks pending |
| BAH LootTable weights and economy stages | Existing BAH enum/API and bundled JAR; actual random API exercised by unit test | Integrated; live server pending |
| Bidding, refunds and shutdown cleanup | Existing regression tests passed with mocked Bukkit/Vault boundaries | Automated checks pass; live pending |
| Durable winning reward | SQL save call added and verified by unit test | Implemented; real Auction SQL handoff pending |
| Isolated MariaDB | Real server started, queried, stopped and restarted; row remained | Service verified |
| Minecraft runtime | WineSpigot stopped at its authentication/IP check; bundled standard Spigot 1.8.8 reached its EULA check | Blocked before plugin startup |
| Live player scenarios | Runner explicitly refused to proceed without accepted EULA | Not executed |
| Active auction resumption | Historical shutdown cancels/refunds current auction | Not implemented; restart behavior needs live verification |

All 16 module tests passed; no tests were skipped. This does **not** establish
that Minecraft plugins start, a live economy accepts payments, players can
claim rewards, or game data survives server restarts.

## Prepared files

The isolated environment is `/workspace/astro-test-server`, outside the checkout.
The server binds to `127.0.0.1:25565` in offline mode, exclusively for local test
clients. MariaDB binds to `127.0.0.1:3306`. Test users are `BAHTestA` and
`BAHTestB`; they are operators only on this disposable server.

Staged dependencies are the repository's Spigot 1.8.8, CosmicPvPUtils, Auction,
WorldGuard, WorldEdit, Essentials, LootTable, and built StorageUtil/AstroBAH
JARs, plus Vault 1.7.3 from its official GitHub release. All staged archives
passed entry CRC checks and have recorded SHA-256 hashes. Debian MariaDB
packages were fetched using signed repository metadata and package hash checks,
then extracted under this directory without changing system packages.

Generated local database credentials are held in private test plugin configs;
their values must not be printed or committed. Test configuration must not be
copied into a production server. Staging binaries alone is not validation; see the live results below.

## Start and execute

The user explicitly accepted Minecraft’s EULA on 2026-10-10. Only the isolated
server’s `eula.txt` was set to true. Run:

```sh
bash /workspace/astro-test-server/start-db.sh
python3 /workspace/astro-test-server/run-live-tests.py
```

The runner starts a real server process and two real Minecraft protocol clients.
It uses the separate test-only `BAHProbe` plugin for deterministic fixtures,
timestamps and snapshots, not as a replacement economy or Auction plugin.
Actual live runs use real Essentials/Vault, Auction, LootTable and MariaDB;
no mocked economy or database is used.

Required scenarios are: command/menu startup, SOTW weighted reward selection,
actual GUI bids, an outbid refund, auction completion with a SQL reward,
restart before collection, actual collection GUI claim, history pagination,
economy-stage change, and restart with an active bid. The last scenario expects
the historical cancellation/refund behavior, not resumed bidding. Fixtures
use shortened expiry through the probe to avoid waiting five minutes.

Each run uses a unique ID and writes fresh logs, actual plugin snapshots, and
asserted outcomes under `/workspace/astro-test-server/results`. A failure exits
nonzero; independent scenarios continue after a collection assertion failure.
Earlier passed scenarios and remaining failed/unrun scenarios must be
reported separately. Unexecuted scenarios must never be marked passed.

Stop the game server with its `stop` console command. Stop the owned database
through its local socket with `SHUTDOWN`; only these test processes should be
stopped. The launcher scripts restart services from retained files and data.

## Executed live results (2026-10-10)

Final run `c3619b23dc` completed all ten assertions successfully, with real
Spigot 1.8.8, two Minecraft 1.8.8 protocol clients, Essentials/Vault and MariaDB.
Evidence is in `/workspace/astro-test-server/results/c3619b23dc-summary.json`,
with matching server/client logs and per-scenario JSON snapshots.

Passed: plugin/economy startup; weighted SOTW loot; first GUI bid and withdrawal;
outbid refund and second withdrawal; completion with history and SQL reward;
completed history/balance/unclaimed reward across restart; actual collection
GUI granting one diamond and removing one SQL claim; history GUI pagination;
FIRST_FRIDAY stage selecting emerald; active-auction graceful restart refund.

Banana.jar is referenced by AstroCrateKeys and NebulaShop, neither needed here.
No Banana runtime dependency was found in AstroBAH’s required chain. The
Banana references in Vault/Essentials/WorldEdit are optional bPermissions hooks.

The successful test dependency set additionally uses:
- `EnchantmentsAPI/lib/EssentialsX.jar` (2.18.2.0) instead of the customized
  Essentials binary requiring a nonstandard Guava method.
- `ProxyPatch/lib/Factions (1).jar`, supplying CosmicPvPUtils’s undeclared
  Factions class references.
- `HotFix/lib/Arkkit-0.1.jar` and `Core/lib/ProtocolLib (4).jar`, satisfying
  Auction’s undeclared Arkkit dependency and Arkkit’s ProtocolLib references.

Earlier real runs failed: newer Arkkit required absent LilyPad/NetworkManager,
and oldest Arkkit lacked AntiCrash. Failed claim attempts with uninitialized
Arkkit removed the SQL claim without granting the item. Those failed logs are
retained; the compatible dependency set passed collection afterward. Auction’s
existing non-atomic claim implementation remains a risk if dependencies fail.

Limits: active auctions cancel/refund on graceful shutdown rather than resume.
Abrupt-crash escrow recovery, full production-plugin compatibility, and all
Arkkit/Factions features are untested. Factions logs that its spawner-boost
listener requires TacoSpigot; that unrelated feature is unavailable on this
standard Spigot test server. WineSpigot could not authenticate in this cloud.
No production server configuration was changed. The Minecraft test process
was stopped cleanly after the run; local MariaDB remains available.
