import unittest
from probe_results import probe_passed, run_succeeded


class ProbeResultTest(unittest.TestCase):
    def test_starting_message_is_not_a_completed_probe(self):
        self.assertFalse(probe_passed('SlotGiveProbe', 'grant', ['starting']))

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
