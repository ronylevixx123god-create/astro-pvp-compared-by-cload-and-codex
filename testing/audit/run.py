#!/usr/bin/env python3
"""Observe known defects in a separate JVM using mocks and temporary files.

This is an audit reproducer, not a passing application test suite. It starts no
Minecraft server, performs no real economy/SQL/vendor calls, and writes no live data.
Run after the native build and isolated-server preparation documented in TESTING.md.
"""
import argparse, json, os, subprocess, tempfile
from pathlib import Path

root = Path(__file__).resolve().parents[2]
parser = argparse.ArgumentParser(description=__doc__)
parser.add_argument('--state', type=Path, default=Path('/workspace/.integration-server'))
parser.add_argument('--output', type=Path)
args = parser.parse_args()
cache = Path(os.environ.get('ASTRO_BUILD_CACHE', '/workspace/.onboarding'))
java = Path(os.environ.get('JAVA_HOME', str(cache / 'tools/jdk8u472-b08'))) / 'bin'
output = args.output or Path(tempfile.mkdtemp(prefix='astro-deep-audit-'))
output.mkdir(parents=True, exist_ok=True)
runs = [p for p in (args.state / 'runs').iterdir() if (p / 'summary.json').exists() and (p / 'server.jar').exists()]
if not runs:
    parser.error('An existing disposable server run with staged dependency jars is required.')
runtime = max(runs, key=lambda p: p.name)
modules = ['Core', 'Kits', 'AstroCollectors', 'CustomBlocks', 'AuctionHouse']
classes = [root / module / 'target/classes' for module in modules]
if not all(p.is_dir() for p in classes):
    parser.error('Build the audited native modules first; target/classes is missing.')
coordinates = [
    'org/mockito/mockito-inline/4.11.0/mockito-inline-4.11.0.jar',
    'org/mockito/mockito-core/4.11.0/mockito-core-4.11.0.jar',
    'net/bytebuddy/byte-buddy/1.12.19/byte-buddy-1.12.19.jar',
    'net/bytebuddy/byte-buddy-agent/1.12.19/byte-buddy-agent-1.12.19.jar',
    'org/objenesis/objenesis/3.3/objenesis-3.3.jar',
]
libraries = [cache / 'm2' / path for path in coordinates]
if not all(p.is_file() for p in libraries):
    parser.error('The documented test-library cache is missing; run the existing native tests first.')
classpath = os.pathsep.join(map(str, [output, *classes, *libraries, runtime / 'server.jar', *sorted((runtime / 'plugins').glob('*.jar'))]))
with (output / 'compile.log').open('w') as log:
    subprocess.run([str(java / 'javac'), '-encoding', 'UTF-8', '-proc:none', '-cp', classpath,
                    '-d', str(output), str(Path(__file__).with_name('AuditReproduce.java'))], stdout=log, stderr=subprocess.STDOUT, check=True)
with (output / 'observations.log').open('w') as log:
    subprocess.run([str(java / 'java'), '-cp', classpath, 'AuditReproduce'], stdout=log, stderr=subprocess.STDOUT, check=True, timeout=40)
observations = []
for line in (output / 'observations.log').read_text(errors='replace').splitlines():
    if line.startswith('AUDIT|'):
        _, identifier, status, detail = line.split('|', 3)
        observations.append(dict(id=identifier, status=status, detail=detail))
result = dict(scope='Fault observations only; REPRODUCED means an existing defect was observed, not fixed.',
              runtime_dependency_snapshot=runtime.name, observations=observations,
              blocked_or_not_reproduced=sum(o['status'] != 'REPRODUCED' for o in observations))
(output / 'observations.json').write_text(json.dumps(result, indent=2) + '\n')
print(json.dumps(result, indent=2))
if len(observations) != 11:
    raise SystemExit('Incomplete audit observations; inspect the isolated logs.')
