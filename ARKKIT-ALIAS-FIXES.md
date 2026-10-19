# Arkkit command registration and alias fixes — 2026-10-19

## Repairs

- **Showcase expander command:** Arkkit's patch passed `/showcaseexpander` (with an internal leading slash) to Bukkit registration, so the normal command name did not resolve. Register `showcaseexpander`; keep `sce`, the Arkkit namespace, the OP check and grant behavior. The patch remains disabled by default in committed configuration. It is temporarily enabled only in the disposable probe and its registrations are cleaned up.
- **Command-map lookup failure:** AbstractCommand.getCommandMap printed reflection failures and recursively retried without any state change. Replace that unbounded path with a descriptive IllegalStateException retaining the reflection cause. Preserve cached-map behavior and successful access to the actual CraftServer map. This does not add support for unknown server internals; unsupported environments fail clearly instead of overflowing the stack.
- **Exact lootbox alias:** Rewrite only the `/lootbox` command token to `/mc`, preserving the rest of the input exactly. Previously it lowercased every argument and replaced matching text throughout the payload, and rewrote longer names such as `/lootboxfilter` to `/mcfilter`.
- **Preserve alias arguments:** `/tpyes`, `/tpno`, `/mask`, `/craft`, the lightning aliases and `/eat` retain target names, case-sensitive IDs and spacing when replacing the command root. Previously most discarded arguments; tpyes lowercased them.
- **Respect cancellation:** Both alias handlers leave cancelled events unchanged; the lootbox event annotation now also ignores cancelled events.

These narrow corrections preserve existing handler authorization and commands. No class feature, armor bonus, cage, FactionsKore configuration, vendor operation or live server was changed. Other known overlaps—including customblocks forced-view routing, root ownership between lootbox/slot implementations, and creload—remain separate problems.

## Evidence

Before correction, three showcase registration/dispatch tests failed. After correcting an initial alias test fixture that omitted the legacy event's player server, three alias tests reproduced changed argument case, modified payload text and corrupted longer command names. Command-map recursion was confirmed by inspection; deliberately flooding logs/stack-overflowing it was avoided.

After correction, Arkkit, Core, AuctionHouse, CustomBlocks, AstroCollectors and GiftcardShop passed Maven clean verify together: **54 native tests passed**, including **12 new tests**, zero failures/errors/skips. New coverage includes normal/alias/namespaced showcase lookup, OP usage and ordinary-sender denial; cached-map lookup, absent server and missing reflection field; exact alias matching, argument/payload preservation, cancellation and untouched unrelated commands. The command-map failure tests have bounded timeouts and retain the NoSuchFieldException cause.

The independent review suite passed 61 tests. Server-runner contracts passed ten tests, including explicit command dispatch and cleanup requirements for the new probe. These are targeted checks, not a full 79-module reactor or gameplay compatibility claim.

## Isolated-server checks

The first server run passed actual Arkkit enablement, uncached command-map lookup, normal/alias/namespaced registration and usage dispatch. It then failed in a **probe fixture**, because Java dynamic proxies cannot implement the legacy Player API's incompatible getHealth method return types. The probe cleaned up registrations and exited normally; this was not counted as a passing probe. The replacement message-only fixture invokes alias handlers directly and cannot access a player or economy service.

Final run `20261019T101807Z-d5936cc9` co-staged 90 plugin jars including all six current native builds, reached ready state, passed every probe marker and cleaned up the patch's command/listener registrations. Actual Arkkit was enabled; uncached reflection and console usage dispatch worked. The server exited zero without forced termination. **Full startup remains unsuccessful:** 109 startup error lines, including missing Multiverse-Core/AstroSpawners dependencies, FactionsKore external-IP/license initialization, and existing database/plugin failures. Core remained disabled, so alias checks were direct message-only handler calls. Both attempts are recorded in comparison/arkkit-alias-runtime.json; native suite results are in comparison/arkkit-alias-native.json.

## Remaining limits

Core startup remains blocked independently; direct alias-handler checks do not prove live player event dispatch or every interacting plugin's behavior. Real showcase item grants, inventory-full handling, expander redemption and slot persistence remain untested by this repair. FactionsKore licensing/runtime checks remain deferred by the user. External/plugin startup failures are reported separately from a focused probe result. The command catalog will be refreshed to distinguish repaired behavior from its earlier source snapshot.

Only destination integration is changed. Source repositories, destination main, existing backups and live server remain untouched.
