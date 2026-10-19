import unittest
from probe_results import probe_passed, run_succeeded


class ProbeResultTest(unittest.TestCase):
    def test_starting_message_is_not_a_completed_probe(self):
        self.assertFalse(probe_passed('SlotGiveProbe', 'grant', ['starting']))

    def test_command_probe_requires_checks_beyond_completion(self):
        self.assertFalse(probe_passed('CommandInputProbe', 'grant', ['RESULT command-input passed']))
        lines = ['PASS giftcard alias shares registered command',
                 'PASS giftcard console command rejected without exception',
                 'PASS coinflip console handler safe', 'RESULT command-input passed']
        self.assertTrue(probe_passed('CommandInputProbe', 'grant', lines))
        self.assertFalse(probe_passed('CommandInputProbe', 'grant', lines + ['FAIL command dispatch']))

    def test_arkkit_probe_requires_cleanup_and_each_repair_check(self):
        from probe_results import CONTRACTS
        expected = list(CONTRACTS['ArkkitAliasProbe'])
        self.assertTrue(probe_passed('ArkkitAliasProbe', 'grant', expected))
        self.assertFalse(probe_passed('ArkkitAliasProbe', 'grant', ['RESULT arkkit-alias passed']))
        self.assertFalse(probe_passed('ArkkitAliasProbe', 'grant', [line for line in expected if 'cleaned up' not in line]))
        self.assertFalse(probe_passed('ArkkitAliasProbe', 'grant', expected + ['FAIL dispatch']))

    def test_failure_overrides_completion_marker(self):
        self.assertFalse(probe_passed('SlotGiveProbe', 'grant', ['FAIL permission check', 'RESULT fails=0']))

    def test_every_phase_assertion_is_required(self):
        self.assertFalse(probe_passed('KitsLPProbe', 'verify', ['access server=astro-integration -> true']))
        self.assertTrue(probe_passed('KitsLPProbe', 'verify', [
            'access server=astro-integration -> true', 'access server=othersrv -> false']))

    def test_unreviewed_diagnostic_cannot_be_counted_as_passed(self):
        with self.assertRaises(KeyError):
            probe_passed('BahProbe', 'grant', ['PROBE started'])

    def test_crash_after_success_marker_is_failed(self):
        self.assertFalse(run_succeeded(True, False, 1, 'SlotGiveProbe', True, []))

    def test_forced_stop_or_no_readiness_is_failed(self):
        self.assertFalse(run_succeeded(True, True, 0, 'SlotGiveProbe', True, []))
        self.assertFalse(run_succeeded(False, False, 0, 'SlotGiveProbe', True, []))

    def test_focused_success_does_not_mean_full_startup_success(self):
        errors = ['missing required dependency']
        self.assertTrue(run_succeeded(True, False, 0, 'SlotGiveProbe', True, errors))
        self.assertFalse(run_succeeded(True, False, 0, '', None, errors))

    def test_failed_assertions_do_not_pass_with_clean_startup(self):
        self.assertFalse(run_succeeded(True, False, 0, 'SlotGiveProbe', False, []))


if __name__ == '__main__':
    unittest.main()
