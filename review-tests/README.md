# Review regression checks

Run from the repository root:

```sh
python3 review-tests/run.py
```

Prerequisites: Python 3, curl, Java 8 JDK (`JAVA_HOME`), the checked-in plugin jars, and the built StorageUtil, Utils and Enchantments outputs. The cloud environment installation script builds all three in order. The runner defaults to the tools and cache under `/workspace/.onboarding`; `ASTRO_REVIEW_CACHE` can select a different dependency cache. It downloads pinned JUnit/Mockito/H2 dependencies from Maven Central, validates their published checksums, compiles all DiscordSync sources plus the changed AutoOP/trailer code, and runs JUnit. It suppresses implicit compilation of Java sources embedded in the legacy server jar, which otherwise interferes with the current test libraries.

The initial 35 tests exercise preservation of existing operators, delayed permission revocation, player disconnect/plugin-disable handling, real YouTube host validation and canonical video IDs and historical claim normalization, trailer command console/missing-key handling, pending-request timeout and cooldown, database result/statement cleanup, real SQL link/unlink and refresh operations, cache invalidation, duplicate link rejection, startup failure, shutdown cleanup, removal of the old owner-role command, malformed Discord codes, main-thread account-code consumption with asynchronous database writes, and whitelist authorization/thread handoff.

SQL round trips also run against an embedded H2 database in MySQL compatibility mode. Discord, YouTube and Minecraft services are mocked; no live MySQL server is used. These checks do not certify live integrations or replace a full module build and server smoke test. No tokens, database passwords or external accounts are required.

The full suite now runs **61 tests**. Added checks cover Against the Odds activation with player damage events (17 tests), preservation of inactive defense hooks, the Alien Implants stub, dormant armor durability effects, reload registry restoration and cross-fork region-file closing (9 tests). The runner uses the original Banana API and freshly built Enchantments/Utils classes before bundled dependency jars. Legacy Bukkit return-type bridges require concrete covariant test fixtures for projectile shooters and double-valued player health. See [ENCHANTMENT-RESTORATION.md](../ENCHANTMENT-RESTORATION.md).
