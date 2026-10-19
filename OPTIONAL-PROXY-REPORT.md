# Optional ProxyPatch follow-up — 2026-10-19

Builds on `a4d1c1a0e`; destination `integration` only.

## Fixed paths

`/near` player filtering, armor refresh on join, and the Cosmic Client packet-registration
path still accessed `ProxyPatch.playerNames` without checking that the optional plugin
was available. CCVerify's outer join check only checked plugin presence; it now checks
enabled state, matching its inner packet path.

Each path now consults that list only while ProxyPatch is enabled. No classes, player
lists, command permissions, armor policies or client packet formats were replaced.
When enabled, its listed players remain excluded exactly as before. With the plugin
absent or disabled, ordinary nearby-player reporting, armor-refresh scheduling and
client registration proceed without requiring its classes or stale name list. This
matches the earlier staff random-teleport fix. The upstream optional-plugin work
`bc9126910` was reviewed; an enabled check also handles disabled plugin instances.

## Verification scope

The affected Ark, AstroArmorSets and CCVerify modules passed native `clean verify`.
These modules have no native unit tests; that build is not counted as gameplay testing.
All 61 existing review tests and eight runner regression tests passed again.

The isolated `OptionalProxyProbe` uses server-native EntityPlayer fixtures and explicitly
enables the original opt-in NearLegacy patch. It exercises:

- ProxyPatch absent: the target appears in nearby results, armor refresh is scheduled,
  and CCVerify registers the player and emits the exact REGISTER-channel payload.
- Real ProxyPatch enabled: ordinary players reach those same paths; a fixture name
  added to its real list is excluded from all three paths.
- The same plugin disabled with its list retained: that stale fixture name no longer
  blocks any of those ordinary-player paths.

Only the new armor refresh tasks created by this probe are cancelled, because actual
armor effects require the independently blocked Core integration. Client REGISTER
payloads are captured through the test player's connection, not sent to an official
client. The packet check distinguishes REGISTER from asynchronous handshake packets.

The initial probe failed because the opt-in NearLegacy patch was not initialized in
this disposable server's default configuration. Its fixture was corrected to enable
that original patch explicitly. The initial failure remains in the external run logs;
it is not counted as passed. Earlier successful generic custom-payload probes were
followed by final runs that check the exact REGISTER channel.

See [sanitized results](comparison/optional-proxy-results.json) for the final run IDs,
assertions, omitted-plugin metadata and separate startup error counts.

## Limitations

The full plugin stack still has the previously documented external-IP/licensing,
missing Multiverse/map and integration failures. These focused checks do not certify
clean startup. Full `/near` command authorization, hidden/ghost/faction/radius variants,
actual armor effects, official-client handshake completion, encryption, combat and
reward/economy behavior remain untested in this follow-up. No prior failed reward test
was rerun or reclassified. Source repositories, destination main and the live server
were not modified. Test-only worlds, configuration and logs remain outside the checkout.

Reproduce with the documented Java 8 toolchain and recovered dependencies:

```bash
mvn -B -ntp -s /workspace/.onboarding/central-settings.xml -Dmaven.repo.local=/workspace/.onboarding/m2 -Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts -pl Ark,AstroArmorSets,CCVerify clean verify
python3 review-tests/run.py
python3 -m unittest discover -s testing -p test_probe_results.py
# Minecraft EULA was already accepted for these disposable servers.
python3 testing/isolated_server.py --accept-eula --probe OptionalProxyProbe --omit-plugin ProxyPatch
python3 testing/isolated_server.py --accept-eula --probe OptionalProxyProbe
```
