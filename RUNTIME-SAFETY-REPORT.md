# Runtime safety follow-up — 2026-10-19

Builds on `dcbd4faba`, on destination `integration` only.

## Problems fixed

### Missing-map flight tasks

Outpost and tournament flight tasks dereferenced missing worlds on every scheduled
execution. An absent Pluto world also prevented the Mars checks from running.
The reviewed upstream missing-map fixes (`e299e2394`) identify the same paths.
The combined version skips each absent world independently, while keeping the original
rules in installed worlds: ordinary flying players lose flying/flight permission;
operators and players already on the ground are unchanged. No worlds or maps are
fabricated by production code, and no claim/combat policy was reset.

### Staff random teleport

The staff tool accessed `ProxyPatch.playerNames` unconditionally and selected a random
index even if filtering left no targets. The upstream optional-plugin fix
(`bc9126910`) handles only part of this path.

This version consults the ProxyPatch list only while that plugin is enabled, returns
an explanatory message for an empty eligible list, and honors a cancelled/failed
Bukkit teleport. A failed teleport no longer announces success, plays the success
sound, or starts a cooldown. Existing staff-mode/item authorization, target world,
online-player filtering, proxy-player exclusion when enabled, and two-tick successful
teleport cooldown remain. The original ability to select oneself is retained.

### Test-fixture compatibility

The first full reactor verification exposed an ambiguous Bukkit 1.8 static mock in the
previous envoy regression test (`getOnlinePlayers` has legacy bytecode overloads).
Both the static mock and an attempted server mock proved unreliable with these legacy
overloads. The public envoy entry point now reads the real online count once and
delegates to a package-private method accepting that count. The regression test exercises
the same start logic with an explicit count, without mocking the overloaded Bukkit API.
The crate-count formula and gameplay decisions are unchanged; real-server probes test
the public entry point. A separate initial runtime probe failed because
Java dynamic proxies cannot implement the legacy Player health overloads. Its replacement
uses a real server EntityPlayer fixture with no external client or account. An intermediate
probe compilation also caught an ambiguous WorldType import; explicit imports fixed it.
These initial failures are not passing checks and are retained in the external logs.

### Obsolete dependency metadata

HypeBoxes and PvPTop depended on `CrankedUtils`, but inspection found only imports of
`com.cosmicpvp.cosmicutils` from the supplied `CosmicPvPUtils` provider and no runtime
CrankedUtils lookups. HypeBoxes already declared CosmicPvPUtils alongside the obsolete
name; the obsolete entry was removed. PvPTop now declares its actual provider. No
provider stub was installed and no application implementation was swapped. Both plugins
were verified enabled on the isolated server with their real dependencies.

### Honest runner outcomes

The isolated runner previously allowed an unregistered diagnostic with any PROBE log
line to count as a pass, and could return success after a nonzero server exit if a
success marker had already appeared. It now requires registered assertion contracts
and a zero server exit, readiness and no forced stop. Eight regression tests cover
partial output, explicit failures, incomplete phases, unknown diagnostics, nonzero
exit, forced shutdown, and the distinction between a focused probe and full startup.
Imported upstream diagnostic sources remain available for manual review; they cannot
be reported as passing automated probes without a reviewed contract.

## Verification

Final results are recorded in [runtime-safety-results.json](comparison/runtime-safety-results.json).
The native focused build passed Core (11 tests), Tournaments (5), and Outpost (3).
The final clean combined build passed all 77 selected reactor entries, with **85 native
tests**, **61 review tests** and **8 runner tests**, no failures/errors/skips. This includes
the repaired envoy fixture and new flight/teleport tests. After the metadata changes,
HypeBoxes and PvPTop also passed a targeted clean native build.

The final isolated Banana 1.8.8 run staged 89 real plugins (ProxyPatch deliberately
omitted). It verified both repaired plugin enables, missing-map flight tasks, tournament
world unload/reload, and an explanatory no-target chat packet for empty random teleport.
Core remained disabled by its independently failing startup integration: the probe used
Core's own class loader to invoke only this method with an unregistered EntityPlayer.
It did not fake a working Core or exercise staff authorization/client interaction.
The run still recorded **109 unrelated startup/task error lines**. Its successful
assertions therefore do not certify clean startup. The earlier attempt to resolve the
listener through another plugin's loader failed; the final probe resolves the tested
class through the owning loader explicitly.

The final EnvoySafetyProbe also passed again after the entry-point refactor and metadata
fixes, with 90 staged plugins and 109 unrelated error lines: seven real crates spawned,
preexisting chest preserved, event cleanup and repeated stop passed. These servers were
stopped normally; no test Java or database process remained.

## Remaining problems and scope limits

- The full runtime stack is still blocked by FactionsKore's failed external IP/licensing
  route, missing Multiverse-Core and missing original maps/regions. The obsolete
  CrankedUtils metadata blocker for HypeBoxes/PvPTop is resolved as described above.
  No licensing was bypassed and no mandatory feature was replaced by a dummy.
- A read-only attempt to inspect official Multiverse GitHub releases was denied by
  the network proxy (HTTP CONNECT 403). No unverified replacement was installed.
- Native Factions, Trials and PlayerVaultsX remain excluded for the previously
  documented dependency blockers. Their builds are not counted as passed.
- At this checkpoint, unguarded optional-ProxyPatch references remained in Ark, ArmorSets
  and the CCVerify packet path. The subsequent [optional-proxy follow-up](OPTIONAL-PROXY-REPORT.md)
  repairs and tests those paths separately.
- A further audit found the legacy kit tier INSERT uses ON DUPLICATE KEY UPDATE but
  the restored table definition has no unique key. Existing databases may have their
  own indexes or duplicates. This needs a separately tested, data-preserving schema
  migration; no index was imposed on user data in this patch.
- HypeBoxes loot/battle rewards and PvPTop scoring/persistence correctness were not tested.
- Full reward delivery, original combat/maps/economy, official-client interactions,
  production migration and long-running task behavior remain unverified. Prior failed
  reward tests remain failed/blocked; this patch does not reclassify them.

Private server configuration, raw logs, database data and disposable worlds remain
outside the checkout. Source repositories, destination main and the live server are
untouched. The dedicated server runner supports `--omit-plugin ProxyPatch` to exercise
absence explicitly, records the omission, and still reports unrelated startup errors.

## Reproduce

```bash
bash testing/verify.sh
export JAVA_HOME=/workspace/.onboarding/tools/jdk8u472-b08
# Uses only fresh disposable loopback server folders; EULA already accepted.
python3 testing/isolated_server.py --accept-eula --probe MissingMapsProbe --omit-plugin ProxyPatch
python3 testing/isolated_server.py --accept-eula --probe EnvoySafetyProbe
```
