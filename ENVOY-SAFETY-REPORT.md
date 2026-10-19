# Envoy safety follow-up — 2026-10-19

Follow-up to commit `4eedf2b9d`, on destination `integration` only.

## Changes and rationale

- Fixed random selection's exclusive upper bound: the original implementation excluded
  the final saved location and crashed when exactly enough locations existed. Select
  distinct, loaded, empty locations without replacement, including the final location.
- Validate capacity before starting/replacing an event. Missing worlds, duplicate
  locations and occupied blocks do not count as usable locations. An unsuccessful
  replacement leaves the existing event running and does not reset its timer.
- Consume an envoy summoner only after a successful start. A failed start reports the
  setup problem and keeps the item. Existing active-event/cooldown eligibility remains.
- Only tracked event chests can award loot. Previously any chest at a saved setup
  location could award the two original loot rolls, even outside an event.
- Cleanup only removes tracked event chests, using a snapshot so removal cannot modify
  the list being traversed. Preexisting chests at configured locations are preserved.
  Stopping an event clears its crate counter and tolerates an absent firework task.

The upstream `9ca794899` guard was reviewed but is insufficient by itself: it requires
*more* locations than crates to work around the random-bound bug, counts duplicates and
occupied locations, and leaves the unrelated-chest cleanup/reward problem. These changes
fix the underlying paths instead. Crate-count formula, command names, permissions,
reward table, two reward rolls per valid crate, and persisted location format remain.
Uniform selection now includes every eligible location; this intentionally corrects the
old bias. Underconfigured events skip safely instead of crashing or consuming a ticket.

## Current verification

- **Passed:** native `AstroEnvoy clean verify`, six regression tests, zero failures,
  errors or skips; all 61 existing review tests.
- **Passed:** isolated Banana 1.8.8 server with 90 staged plugins: empty setup stays
  inactive; exactly seven available locations create seven real chests; an occupied
  configured location is excluded; cleanup removes those seven chests and preserves
  the preexisting chest; crate state clears; repeated stop is safe.
- **Failed/blocked:** the full stack still reports startup errors from the previously
  documented missing plugins, external licensing/IP lookup and unavailable integrations.
  The focused envoy probe's pass does not turn those errors into a passing stack.
- **Untested:** envoy loot/economy delivery, official-client item interaction, long-running
  event scheduling, restart recovery, and full original gameplay. The prior failed
  slot-reward check has not been rerun or reclassified.

See [sanitized evidence](comparison/envoy-safety-results.json). Private runtime configs,
raw logs and disposable worlds remain outside the checkout. The server was stopped;
no live server or source repository was modified. The existing integration report and
its results describe the preceding checkpoint and remain available for comparison.

Reproduce after the documented toolchain/dependency preparation:

```bash
export JAVA_HOME=/workspace/.onboarding/tools/jdk8u472-b08
export PATH="$JAVA_HOME/bin:/workspace/.onboarding/tools/apache-maven-3.9.11/bin:$PATH"
export MAVEN_OPTS="-Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts"
mvn -B -ntp -s /workspace/.onboarding/central-settings.xml -Dmaven.repo.local=/workspace/.onboarding/m2 -pl AstroEnvoy clean verify
python3 review-tests/run.py
# Requires the Minecraft EULA acceptance already given for this isolated test server.
python3 testing/isolated_server.py --accept-eula --probe EnvoySafetyProbe
```
