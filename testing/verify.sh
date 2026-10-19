#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")/.."
export ASTRO_BUILD_CACHE="${ASTRO_BUILD_CACHE:-/workspace/.onboarding}"
export JAVA_HOME="${JAVA_HOME:-$ASTRO_BUILD_CACHE/tools/jdk8u472-b08}"
export PATH="$JAVA_HOME/bin:$ASTRO_BUILD_CACHE/tools/apache-maven-3.9.11/bin:$PATH"
export MAVEN="${MAVEN:-$(command -v mvn)}"
export MAVEN_OPTS="${MAVEN_OPTS:-} -Djavax.net.ssl.trustStore=/etc/ssl/certs/java/cacerts -Xmx2g"
java -version 2>&1 | head -1 | rg '"1\.8\.' >/dev/null || { echo 'Use a Java 8 JDK.' >&2; exit 1; }
python3 testing/prepare_maven.py
# These five reactor entries remain explicitly unverified. Do not turn this into a
# claim that the full reactor passed. See INTEGRATION-REPORT.md for prerequisites.
# Clean is required: shade can otherwise reuse an already shaded jar and retain
# obsolete library classes after a dependency changes.
"$MAVEN" -B -ntp -s "$ASTRO_BUILD_CACHE/central-settings.xml" \
  "-Dmaven.repo.local=$ASTRO_BUILD_CACHE/m2" -fae \
  -pl '!AstroFactions,!AstroFactions/modules/Main,!AstroFactions/modules/Legacy,!Trials,!PlayerVaultsX' \
  clean verify
python3 review-tests/run.py

python3 -m unittest discover -s testing -p test_probe_results.py
