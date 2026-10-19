# Envoy entity ownership follow-up — 2026-10-19

Builds on `582eb7922`; destination `integration` only.

## Problems and fixes

The original nearby-hologram cleanup removed every armor stand within its search box,
including preexisting decorations. Each spawned envoy hologram is now tracked by its
actual ArmorStand reference at the crate location. Cleanup removes only that reference,
not unrelated entities found nearby. Public method names/signatures remain available.

Cleanup also skipped holograms when a crate had been replaced with a non-chest block.
It now cleans tracked locations regardless of their current block type, removes each
owned hologram, and sets a block to air only if the tracked location still contains a
chest. Calls to remove a crate outside the current event are ignored. This preserves a
replacement stone block and prevents the public removal method from deleting an
untracked chest. Crate counters are still cleared when the event ends.

Command names, permissions, reward rolls/tables, crate-count formula, timer and saved
location format are unchanged. Hologram ownership is in memory for the current event;
no storage migration was added. Untracked entities, including old orphaned holograms,
are left untouched. Cleanup identifies blocks by tracked location and chest material;
it cannot distinguish a replacement chest of the same material at that same location.

## Verification

- **Before fix:** the two new native regression tests failed against the prior code,
  with no test errors: deletion of a nearby decoration, and skipped hologram cleanup
  for a changed crate block.
- **After fix:** `AstroEnvoy clean verify` passed all eight native tests, zero failures,
  errors or skips. The 61 existing review tests and eight runner tests passed again.
- **Isolated server:** the enhanced EnvoySafetyProbe starts seven real crates and
  holograms beside a real decorative armor stand, replaces a tracked crate with stone,
  and ends the event. It checks preservation of that stone, the nearby decoration and
  an untracked chest; removal of the six remaining crate blocks; removal of all seven
  owned holograms; cleared event state; and safe repeated stop/unowned cleanup.
- The first server run checked world-index removal too early and failed: Bukkit marks
  removed entities dead immediately, then purges the world index during a tick. The
  final probe verifies both steps, including absence from the world after two ticks
  and survival of the decoration after that tick. No assertion was discarded to count
  the initial failure as passed.

See [sanitized current results](comparison/envoy-ownership-results.json) for final run,
assertions and separate startup-error counts. Raw before/after build logs and both
server-run directories remain outside the checkout.

## Remaining limits

The full plugin stack still has the documented licensing/IP-route, missing Multiverse,
missing original maps and integration failures. A focused cleanup probe does not
certify a clean stack or reward delivery. Official-client interactions, combat,
production data migration, restart/crash recovery and complete original gameplay remain
untested. Previous reward failures have not been rerun or reclassified.

No source repository, destination main, live server or existing backups were changed.
The disposable test server is stopped normally after the probe. Reproduce using the
existing Java 8 setup:

```bash
mvn -B -ntp -s /workspace/.onboarding/central-settings.xml -Dmaven.repo.local=/workspace/.onboarding/m2 -Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts -pl AstroEnvoy clean verify
python3 review-tests/run.py
python3 -m unittest discover -s testing -p test_probe_results.py
# EULA already accepted for this isolated server.
python3 testing/isolated_server.py --accept-eula --probe EnvoySafetyProbe
```
