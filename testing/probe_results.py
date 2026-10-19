"""Assertion contracts for isolated probes; startup errors retain their own scope."""
CONTRACTS = {
    'ArkkitAliasProbe': ['PASS uncached command map reflection succeeds',
                         'PASS showcase normal alias and namespace resolve together',
                         'PASS showcase normal and alias dispatch usage without grants',
                         'PASS lootbox arguments preserved', 'PASS longer lootbox command left intact',
                         'PASS teleport alias target preserved', 'PASS cancelled alias left intact',
                         'PASS fixture patch registrations cleaned up', 'RESULT arkkit-alias passed'],
    'CommandInputProbe': ['PASS giftcard alias shares registered command',
                          'PASS giftcard console command rejected without exception',
                          'PASS coinflip console handler safe', 'RESULT command-input passed'],
    'OptionalProxyProbe': ['RESULT optional-proxy passed'],
    'MissingMapsProbe': ['RESULT missing-maps passed'],
    'EnvoySafetyProbe': ['RESULT envoy-safety passed'],
    'SlotGiveProbe': ['RESULT fails=0'],
    'SlotRewardProbe': ['PASS exactly one fixture reward for one rolling column: 1'],
    'DuelMenuProbe': ["Team Menu: title='Team Menu' size=27", "Team List: title='Team List' size=54", 'RESULT done'],
    'KitsLPProbe': {
        'grant': ['grant1=GRANTED mainThread=true', 'grant2=ALREADY_GRANTED mainThread=true'],
        'verify': ['access server=astro-integration -> true', 'access server=othersrv -> false'],
    },
}


def probe_passed(name, phase, lines):
    expected = CONTRACTS[name]
    if isinstance(expected, dict):
        expected = expected[phase]
    return bool(lines) and not any('FAIL' in line for line in lines) and all(
        any(want in line for line in lines) for want in expected)


def run_succeeded(ready, forced, exit_code, probe, passed, errors):
    if not ready or forced or exit_code != 0:
        return False
    return bool(passed) if probe else not errors
