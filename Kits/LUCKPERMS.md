# CosmicGlobalKits: /gkit unlocks through LuckPerms

## What changed

Unlocking a gkit with a wildcard /gkit item (`WildcardMenu` -> `GlobalKits.unlockOrLevelUp`) now
grants `cosmickits.kit.<kit>` through the LuckPerms API (`permissions/KitPermissionGranter`)
instead of running `pex user <name> add ...` and reloading ArkhamPermissions.

- The grant is scoped to a `server` context (see *Configuration*).
- The result is one of `GRANTED`, `ALREADY_GRANTED` or `FAILED`, delivered on the main thread
  after LuckPerms has saved (or failed to save) the user.
- The wildcard item is consumed only on `GRANTED`. On `FAILED` (LuckPerms missing, no scope,
  storage error) the player is told the item was not used and keeps it. If the menu is closed
  while the save is still running, the result decides (`items/WildcardClaim`).
- Messages and the `GKIT_LEVELUP` event on success are unchanged.

## Old bug (why unlocks never stuck)

The old code ran `pex user <name> add cosmickits.kit.<kit> <CosmicUtils.get().getServerName()>`.
`CosmicUtils.serverName` was never assigned in any build, so the command ended in the literal
world/server argument `null`. CompilexPermissions stored that row with `server_name = 'null'`
and only applied rows whose server matched its own server name or `global`, so the permission
was saved but never applied: players lost the item and did not get the kit. This is fixed by
granting through LuckPerms with a real server context.

## Configuration

`plugins/CosmicGlobalKits/config.yml`:

```yaml
luckperms-server-context: ""
```

- Blank (default): use LuckPerms' own `server:` value from `plugins/LuckPerms/config.yml`, so the
  grant matches the context LuckPerms applies on this server.
- If LuckPerms' `server:` is `global` (LuckPerms' default) or empty and this setting is blank,
  unlocks are **refused** (item kept, error in console) instead of silently granting globally.
  Fix by setting LuckPerms' `server:` to this server's name, or by setting this value.
- `global`: deliberately grant on every server (no server context).

## Web store (Tebex/Buycraft) commands

Store purchases are granted by the store's own console command. Replace the old `pex` command
with, for example:

```
lp user {username} permission set cosmickits.kit.hyper true server=<your LuckPerms server name>
```

`listeners/PermissionListener` only reacted to `pex user ... add cosmickits.kit...` console
commands. It was already inactive in Astro's code (it calls `GlobalKits.valueOf("hyper")` on the
lowercase kit name, which always throws) and only added cosmetic tier-0 menu data. It is left
unchanged and inactive.

## Verification

- Unit tests (`mvn test` in Kits): `KitPermissionGranterTest`, `WildcardClaimTest`,
  `GlobalKitsUnlockTest` (plus the tier tests). Deliberately broken versions of the consume rule
  and of the main-thread delivery are caught.
- Isolated test server (TacoSpigot 1.8.8 + Astro's bundled LuckPerms 5.4.30, YAML storage),
  driving the real `KitPermissionGranter`:
  - LuckPerms `server: global`, blank config -> `FAILED`, storage untouched.
  - `server: devtest`, offline UUID -> `GRANTED`, then `ALREADY_GRANTED`; stored as
    `cosmickits.kit.hyper: true` with `server: devtest`.
  - After a restart: node still stored; access `true` on `devtest`, `false` on another server.
  - Storage write failure (user file path blocked) -> LuckPerms' `modifyUser` future completes
    exceptionally (`FileIOException`) -> `FAILED`.
  - All results delivered on the main thread.
- Not yet tested: the full Kits plugin in-game on WineSpigot (needs its dependencies and the
  WineSpigot license).

## SQL configuration compatibility in the combined branch

The kit database still stores the existing `globalkits_userdata` fields; permissions
are a separate LuckPerms concern. Both database readers now accept either existing
flat `hostname`, `port`, `user`, `password`, `database` values or legacy `sql.*` values,
including `sql.username`. Flat values take priority, and a missing port defaults to
3306. Existing credential values are not printed or replaced. Empty defaults still
need real settings; no database credentials are invented for production.
