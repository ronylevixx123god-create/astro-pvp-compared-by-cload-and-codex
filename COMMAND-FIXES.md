# Command input repairs — 2026-10-19

This closes selected input-validation paths from audit A01; it does not certify every command or close the entire command/permission test backlog (T04).

## Repairs and behavior

- `/c` / `/collector <player> <amount>`: retain the declared `astro.admin` authority and also check it inside the executor. Show feedback for offline targets, malformed/overflowing integers, zero and negative counts. Split bulk grants into normal stacks of at most 64 before item/NBT creation. Drop only the returned inventory remainder at the recipient instead of discarding it. **New safety bound:** 1–2,304 items per request (36 stacks, one empty player inventory), rather than accepting unbounded or invalid stack counts. This preserves tested normal bulk use such as 128; larger administrative batches require multiple calls. Console grants remain supported with permission.
- `/creategiftcard` / `/creategc`: reject console use before casting, handle malformed numbers, reject non-finite/zero/negative amounts, and resolve the online recipient once. Preserve both original OP and legacy-name restrictions. This patch does not grant the current owner or arbitrary OP accounts new billing authority. External vendor calls are mocked in tests; no cards were created and no embedded credentials were used.
- `/coinflip` and existing aliases: catch parsing failures for Unicode digit strings accepted by StringUtils.isNumeric; reject numeric overflow to infinity before any economy access; give feedback for malformed wager inputs previously silently ignored. Preserve the existing k/m/b expansion rules, minimum wager, permission policy, menus and economy flow. Valid `10k` still opens selection for $10,000 in the controlled test. Console was already guarded; its safe behavior is regression-tested, not claimed as a newly fixed cast bug.

## Why these versions

Targeted inspection of original source-original/main and Claude source-copy/main and source-copy/integration found the same raw parsing paths and no finite-amount guards in these three handlers. Apply narrow fixes in the destination rather than copying full files. Astro-codex still provides no source refs, so its command implementation cannot be compared. Original error/crash behavior and invalid stack acceptance are deliberately not preserved.

## Passed checks

- Tests first reproduced collector offline/numeric failures, discarded inventory remainder and unsafe direct executor access; giftcard console/numeric exceptions; coinflip Unicode parse failure and infinity reaching the mocked economy boundary. One initial coinflip test fixture omitted its economy getter stub; after fixing that fixture, the two application failures remained reproduced before correction.
- Five modules (AuctionHouse, Core, CustomBlocks, AstroCollectors and GiftcardShop) passed Maven clean verify together with 40 native tests. After refining collector bulk grants, AstroCollectors passed clean verify again; current reports total **42 passing native tests**, including **19 new command tests**, zero failures/errors/skips. These reports represent the combined run plus that targeted follow-up, not another full reactor run.
- Command tests cover console/player identity, retained OP/name/permission restrictions, missing arguments, offline targets, malformed/overflow/negative/zero/NaN/infinite input, aliases supplied to executors, valid amounts, bulk splitting and inventory leftovers. Aliases supplied directly to an executor are not by themselves proof of Bukkit registration.
- Review suite: 61 passed. Server-runner contracts: nine passed, including a new contract requiring individual command checks as well as the completion marker.
- A limited registration screen checked 159 literal getCommand calls across 43 modules against their plugin.yml command/alias declarations. Three apparent mismatches in Arkkit's BalanceTop intentionally address Essentials' balancetop command. No missing local literal declarations were identified by this screen. It excludes dynamic frameworks, command collisions, branch-wide command inventory and runtime command ownership. See comparison/command-registration-screen.json.

## Isolated command probe

Final run `20261019T053009Z-fa4e2643` staged 90 plugin jars, including all five current artifacts. The server reached ready state and shut down normally (exit zero, no forced stop). The enabled giftcard plugin's `creategc` alias resolved to the registered `creategiftcard` command, and actual console dispatch rejected it without a cast exception. The coinflip console handler returned safely using Core's own class loader; **Core was disabled**, so this does not verify live registration or player coinflip gameplay. No vendor or economy calls are made by this probe.

The focused command probe passed. **Full startup still failed its prerequisite checks:** 109 startup error lines, including missing Multiverse-Core/AstroSpawners dependencies, unavailable FactionsKore external-IP/license initialization, and existing database/other plugin failures. These are separate from the passing probe. See comparison/command-runtime-results.json.

## Untested and blocked

Real collector grants and player coinflip gameplay remain blocked by missing runtime prerequisites; the mock tests do not prove FactionsKore, world, reward, permission-provider or economy compatibility. Successful vendor operations, giftcard HTTP error handling, broader command authority policy and all other commands remain unverified. Core and collector dependency startup issues are not repaired by input guards. Auction economy/persistence concerns and the remaining deep-audit backlog remain open. No live server, source repository or main branch was changed.
