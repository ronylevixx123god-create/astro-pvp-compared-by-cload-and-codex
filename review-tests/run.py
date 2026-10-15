#!/usr/bin/env python3
"""Focused Java 8 regression tests, independent of unavailable legacy Maven repos."""
import hashlib
import os
from pathlib import Path
import shutil
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
CACHE = Path(os.environ.get('ASTRO_REVIEW_CACHE', '/workspace/.onboarding/review-tests'))
CACHE.mkdir(parents=True, exist_ok=True)
ARTIFACTS = [
    ('com/h2database/h2', '1.4.200'), ('junit/junit', '4.13.2'), ('org/hamcrest/hamcrest-core', '1.3'),
    ('org/mockito/mockito-core', '4.11.0'), ('net/bytebuddy/byte-buddy', '1.12.19'),
    ('net/bytebuddy/byte-buddy-agent', '1.12.19'), ('org/objenesis/objenesis', '3.3'),
]
for artifact, version in ARTIFACTS:
    name = artifact.rsplit('/', 1)[1] + '-' + version + '.jar'
    jar = CACHE / name
    checksum = CACHE / (name + '.sha1')
    url = 'https://repo.maven.apache.org/maven2/' + artifact + '/' + version + '/' + name
    if not jar.exists():
        subprocess.run(['curl', '-fsSL', '--retry', '2', '-o', str(jar), url], check=True)
    if not checksum.exists():
        subprocess.run(['curl', '-fsSL', '--retry', '2', '-o', str(checksum), url + '.sha1'], check=True)
    if hashlib.sha1(jar.read_bytes()).hexdigest() != checksum.read_text().split()[0]:
        raise RuntimeError('Artifact checksum mismatch: ' + name)
java_home = os.environ.get('JAVA_HOME', '/workspace/.onboarding/tools/jdk8u472-b08')
javac = str(Path(java_home) / 'bin/javac')
java = str(Path(java_home) / 'bin/java')
if not Path(javac).exists():
    javac, java = shutil.which('javac'), shutil.which('java')
    if not javac or not java:
        raise RuntimeError('A Java 8 JDK is required; set JAVA_HOME.')
if not (ROOT / 'Enchantments/target/classes/org/arkhamnetwork/enchantments/Enchantments.class').exists():
    raise RuntimeError('Build Enchantments after Utils before running the enchantment regressions.')
if not (ROOT / 'StorageUtil/target/StorageUtil-0.1-shaded.jar').exists():
    raise RuntimeError('Build StorageUtil with the environment install script before running these tests.')
# The checked-in plugin jars provide the legacy APIs, never substitutes for the changed sources.
jar_dirs = sorted({p.parent for p in ROOT.rglob('*.jar') if '.git' not in p.parts and 'target' not in p.parts})
classpath = os.pathsep.join([str(p) for p in sorted(CACHE.glob('*.jar'))] + [str(ROOT / 'StorageUtil/target/StorageUtil-0.1-shaded.jar')] + [str(ROOT / 'Enchantments/target/classes'), str(ROOT / 'Utils/target/classes'), str(ROOT / 'AstroCollect/lib/banana.jar')] + [str(d / '*') for d in jar_dirs])
sources = list((ROOT / 'AstroDiscordSync/src/main/java').rglob('*.java'))
sources += [ROOT / 'HotFix/src/main/java/me/astropvp/hotfixplugin/listeners/AutoOP.java',
            ROOT / 'Core/src/main/java/net/astropvp/core/utils/YouTubeVideoUrl.java',
            ROOT / 'Core/src/main/java/net/astropvp/core/commands/TrailerCommand.java']
sources += [ROOT / 'Enchantments/src/main/java/org/arkhamnetwork/enchantments/util/EnchantmentDescriptions.java', ROOT / 'Enchantments/src/main/java/org/arkhamnetwork/enchantments/enchantments/swords/AgainstTheOdds.java', ROOT / 'Utils/src/main/java/com/cosmicpvp/cosmicutils/utils/RegionFileCloser.java']
sources += list((ROOT / 'review-tests').glob('*.java'))
with tempfile.TemporaryDirectory(prefix='astro-review-') as output:
    subprocess.run([javac, '-encoding', 'UTF-8', '-source', '8', '-target', '8', '-proc:none', '-sourcepath', '',
                    '-cp', classpath, '-d', output] + [str(p) for p in sources], check=True)
    subprocess.run([java, '-cp', output + os.pathsep + classpath, 'org.junit.runner.JUnitCore',
                    'ReviewRegressionTest', 'EnchantmentRegressionTest', 'org.arkhamnetwork.enchantments.enchantments.swords.AgainstTheOddsTest'], check=True)
