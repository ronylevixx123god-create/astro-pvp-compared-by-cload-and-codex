> Imported upstream recovery notes. Their earlier run IDs, environment paths and live
> results are historical and were not reproduced merely by copying this file. See the
> current [integration report](../INTEGRATION-REPORT.md) for checks executed here.

# AstroBAH: kept exactly as Astro had it (source incomplete)

AstroBAH is left unchanged, exactly as in Astro's repository (`main`, commit 7c2348d3b,
2024-09-27, Kian). It does not compile because most of the plugin was never committed.

## What exists

- `src/main/resources/plugin.yml`: `name: BAH`, `author: quantiom`,
  `description: Remake of Cosmic's BAH`, `main: com.decimatemc.blackmarketauction.BlackMarketAuction`,
  commands `/bah` (alias `/blackmarketauction`) and `/bahloot`.
- `src/main/resources/config.yml` (`minimum-bid-increment: 100000`, `items:`), `history.yml`.
- Seven helper classes in `com/decimatemc/blackmarketauction/eventgame/`
  (`AsyncEvent`, `DelayedEvent`, `EventGameRunnable`, `EventManager`, `GameEvent`,
  `RecurringEvent`, `ViewMaterial`).

## What is missing

The main class `com.decimatemc.blackmarketauction.BlackMarketAuction` and all auction, menu,
command and loot code of quantiom's 2024 plugin. Build error: `EventGameRunnable` and
`EventManager` import `com.decimatemc.blackmarketauction.BlackMarketAuction`, which does not exist.

Checked and NOT the original:
- Astro's git history (all branches): the 2024 plugin's remaining source was never committed.
- `codex/astropvp-restoration` branch and the `AstroPvP-Windows-Test` package
  (`codex/windows-test-download`, `plugins/BAH.jar`, built 2026-10-10/11): a reconstruction
  written by Codex. Its main class, `LootTableBridge`, `AuctionJournal` and
  `AuctionLootPreviewMenu` exist in no Astro developer's commit, and it heavily rewrites
  Astro's older BAH code. Not used.
- `BAH/` (matth, Nov 2021, `net.astropvp.BlackMarketAuction`, `/bah` only): Astro's older,
  different auction plugin, deleted by Astro on 2022-08-12. Complete in history, but not the
  2024 plugin. Not substituted.
- No jar containing `com/decimatemc/` classes exists in this repository.

## What is needed to make it work as on Astro

Either of:
1. The compiled 2024 plugin jar (`BAH.jar`/`AstroBAH.jar` with
   `com/decimatemc/blackmarketauction/BlackMarketAuction.class`), e.g. from Astro's old server
   `plugins/` folder or from Kian/quantiom. It can be installed as-is; no build needed.
2. Its complete source (at least `BlackMarketAuction.java` and the auction/menu/command classes).

## Comparison with Astro's older BAH (commit 3caef671e^, why it is not substituted)

| | BAH 2021 (`BAH/`, complete) | AstroBAH 2024 (this module) |
|---|---|---|
| Plugin name / data folder | `BlackAuctionHouse` | `BAH` |
| Origin | decompiled DecimateMC plugin (Procyon header, `decimatebah.admin`) | quantiom, "Remake of Cosmic's BAH" |
| Commands | `/bah` only | `/bah` (alias `/blackmarketauction`), `/bahloot` |
| Auction start | manual only (`/bah forcestart`) | recurring/delayed event scheduler with countdown warnings (`eventgame`); intervals in missing code |
| Bid increment | hard-coded 100000 + fixed thresholds (500k, 1M, 2.5M, 5M, 10M, 50M, 200M, 1B) | `minimum-bid-increment: 100000` in config |
| Item pool | `possible-items` in config.yml | `items:` in config.yml |
| History | `past-auctions`, `last-auction` in config.yml | separate `history.yml` |
| Winner reward | AuctionHouse unclaimed listing (1 day) | likely `/bahloot` (missing code) |
| Duration | 5 min, +10 s on last-10-s bids | unknown (missing code) |

The 2021 code also withdraws money from the outbid player instead of refunding them
(`EconomyUtils.withdrawBalance` on the previous top bidder); 2024 behavior unknown.
They differ in commands, scheduling, config format, data folder and reward delivery, so the
2021 plugin is not a substitute. The actual 2024 jar or complete source is still required.
