# Auction confirmation escrow repair — 2026-10-19

## Change and selection

Fix audit E01 in the destination implementation: if a player's funds become insufficient while the confirmation menu is open, retain auctionConfirm and auctionPrice metadata until the close handler returns the escrowed item. Previously, clicking confirm removed both entries before the fee check; closing the menu could no longer find or return the item.

Move metadata removal past fee validation and item-ID preparation, immediately before the existing listing call. Keep commands, permissions, fee calculation, normal listing implementation and menu timing unchanged. The existing close handler still returns to inventory or drops into the world when inventory is full. No whole source file was substituted and Git history remains intact.

## Evidence

- Red before correction: all three new tests failed: missing escrow after denied confirmation, and no item returned to inventory/world.
- Green after correction: AuctionHouse, CustomBlocks and AstroCollectors passed Maven clean verify together; 12 native tests passed with no failures or skips.
- Three new tests exercise actual click/close listeners with mocked Bukkit, player metadata, economy and listing boundaries: insufficient balance retains escrow, full inventory receives exactly one world drop, repeated denied clicks/close events return exactly one item.
- Review suite: 61 passed. Server-runner contracts: eight passed.
- Historical fault observer no longer reproduces AUCTION-CONFIRM because the close handler now calls inventory.addItem. Its NOT_REPRODUCED_OR_BLOCKED status is not a regression-test result; native assertions above provide deterministic repair evidence. Eight other baseline fault observations still reproduce, including three dormant jackpot paths. Collector/grinder repairs from the previous commit remain passing.

## Isolated server

Run `20261019T051534Z-a6da4e7d` co-staged 90 plugin jars including the three freshly built modules. The server reached ready state, the envoy-safety regression probe passed and shutdown completed normally with exit code zero (no forced stop). This is a co-staging/regression check, not an auction transaction test. Startup still has 109 error lines; missing Multiverse-Core blocks AstroSpawners and dependent collector/grinder plugins, and existing FactionsKore external-IP/license initialization remains unavailable. See comparison/auction-escrow-runtime.json for the bounded runner's result. Auction itself enabled and then disabled: SafetyToggle could not create its file on the fresh fixture, followed by ERROR CONNECTING TO DATABASE. Consequently no active auction GUI/transaction was tested on this server. These startup failures are not counted as passed checks.

## Remaining limits

This does not close audit E02/E03: listing creation still ignores the Vault withdrawal response and persistent transaction failures require broader settlement work. These tests do not prove successful auction settlement, crash recovery, player disconnect/death behavior, or exact original gameplay parity. No real economy, database, vendor or live server was used by these unit tests. FactionsKore runtime verification remains deferred by the user; Astro-codex still has no source refs available for comparison.

Only destination integration is changed. Source repositories, destination main, existing backups and the live server remain untouched.
