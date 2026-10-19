> Imported upstream recovery notes. Their earlier run IDs, environment paths and live
> results are historical and were not reproduced merely by copying this file. See the
> current [integration report](../INTEGRATION-REPORT.md) for checks executed here.

# AstroBAH recovery

## Provenance

The current checkout contained seven event framework classes, `plugin.yml`,
`config.yml`, and `history.yml`, but no auction implementation or main class.
The project source, all 2,296 bundled JARs, accessible workspace backups, and
available Git history were searched. No copy of the newer
`com.decimatemc.blackmarketauction.BlackMarketAuction` implementation was found.

An older implementation was recovered from the former `BAH` module at commit
`fd70e4c1ee3e5d7f08e0d16ab0e709aa71f7a413`, immediately before its deletion in
`3caef671ee08cef1da0def12a985aeb282f7a137`. Its original main class was
`net.astropvp.BlackMarketAuction`. The auction, command, menu, listener, and
utility sources were restored from that revision. The later history menu was
empty; a deeper search found an earlier `AuctionHistoryInventory` and its
previous/next-page listener at `8570cc966^`. That GUI has now been adapted with
corrected pagination and null handling.

The current main class was reconstructed to initialize these recovered
components and the existing event framework. Historical `net.astropvp.auction`
class names were retained to preserve the serialized `PastAuction` format.

## Restored behavior

- `/bah` auction menu, bidding, collection-bin link, and administrative
  `forcestart`, `setnextauction`, `removenextauction`, `additem`, and `removeitem`.
- `/bah history` and the book in auction-menu slot 7 open paginated history,
  newest first, showing reward, winner/no bids, winning bid, and sale time.
- `/bahloot` is a public read-only reward preview. `/bahloot edit` opens the
  active source's editor and requires operator status or `decimatebah.admin`.
  The external editor is the existing LootTable GUI.
- Five-minute auctions, bid increment thresholds, and ten-second extensions
  for bids made in the final ten seconds.
- Winning items are handed to the existing Auction plugin's unclaimed-listing
  API for collection, retaining the historical one-day claim expiry.
- Auction loot and history persistence. Both historical `possible-items` and
  current `items` keys are accepted. Unrecognized records are retained rather
  than overwritten. The loot editor displays up to 54 items.
- `loot-source` accepts `auto` (default), `loottable`, or `legacy`. Auto uses a
  valid nonempty AstroLoottables BAH table when available, otherwise legacy
  items. Explicit loottable mode requires the plugin and does not fall back.
  The actual LootTable API honors weights and its active economy stage.
  `setnextauction` overrides either source. External `additem` uses weight 1;
  use the LootTable editor for custom weights.
- Automatic auctions every thirty minutes when loot is available, reconstructed
  from the recovered menu's documented behavior. `last-auction` is persisted.
- Startup checks the Vault economy provider and bid increment. Shutdown closes
  plugin menus, refunds an active highest bid, cancels tasks, and clears state.

Winning unclaimed rewards are now also saved synchronously through Auction's
SQL API; the recovered implementation only put them in memory, losing them on
restart before collection. This save call is covered by a unit test but still
requires live verification.

The historical outbid withdrawal bug was corrected: previous bidders are
refunded, including offline players. Vault transaction results are checked;
failed withdrawals do not advance the auction, and failed previous-bidder
refunds trigger a rollback of the new bid. Non-finite, expired, and insufficient
increment bids are rejected. Menu drag events are cancelled to protect items.

## Build and validation

Use Java 8 and run from the repository root:

```sh
mvn -f AstroBAH/pom.xml clean verify
```

In the prepared cloud environment, Maven can be invoked with
`/workspace/tooling/astro-mvn`. The output is `AstroBAH/target/AstroBAH-0.1.jar`.
Compilation uses bundled Spigot, CosmicPvPUtils, Auction and LootTable JARs,
plus StorageUtil's shaded build, required by LootTable's API. Collect, Lombok
and AstroDamageHandler are not used by this module.

All 16 automated tests passed. The tests execute recovered lifecycle, bidding, loot persistence,
timer expiry, history serialization, and reward handoff code with mocked Bukkit,
Vault, and Auction boundaries. They do not start a live Minecraft server or
validate an actual economy/database provider. New tests check history page
bounds and newest-first ordering, and execute the existing weighted LootTable
API across two economy stages.

Ten live integration assertions passed in isolated server run `c3619b23dc`,
including real GUI bids, refunds, reward collection, history and restarts. See
[the integration test plan](INTEGRATION-TESTS.md) for exact commands and status.

## Remaining limits

- The exact newer AstroBAH implementation is unavailable; this is a recovery
  based on the historical implementation, not byte-for-byte restoration.
- The exact newer GUI appearance and undocumented configuration formats remain
  unavailable. History restores the earlier source's intended behavior; the
  LootTable bridge uses the existing BAH enum, stage data and weighted API.
- Server runtime needs Vault, a registered economy provider, CosmicPvPUtils,
  Auction, and those plugins' own dependencies. The isolated test verified live
  initialization and database persistence with the documented dependency set.
- An economy provider that also rejects a rollback or shutdown refund is logged
  with the player and amount for manual reconciliation. Cross-plugin economy
  transactions are not atomic; crash recovery for active auctions was absent
  from the recovered source and has not been invented.

- Active auctions are cancelled/refunded on graceful shutdown, as in the
  recovered implementation; they are not resumed after restart. Completed
  history and unclaimed rewards passed live restart verification; LootTable
  stage changes passed live selection checks. Abrupt-crash recovery remains untested.
