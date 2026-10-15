package net.astropvp.gambling.crash.struct;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor @Getter
public enum CounterSpeed {


    LAST(null, 3.84D, 2L, 5000D),
    TWELTH(CounterSpeed.LAST, 1.77D, 2L, 500D),
    ELEVENTH(CounterSpeed.TWELTH, 0.782D, 2L, 250D),
    TENTH(CounterSpeed.ELEVENTH, 0.45D, 2L, 64D),
    NINTH(CounterSpeed.LAST, 0.219D, 2L, 37D),
    EIGTH(CounterSpeed.NINTH, 0.136D, 2L, 22D),
    SEVENTH(CounterSpeed.EIGTH, 0.051D, 1L, 15D),
    SIXTH(CounterSpeed.SEVENTH, 0.032D, 1L, 9D),
    FIFTH(CounterSpeed.SIXTH, 0.018D, 1L, 5.7D),
    FOURTH(CounterSpeed.FIFTH, 0.017D, 2L, 3.3D),
    THIRD(CounterSpeed.FOURTH, 0.01D, 2L, 2.05D),
    SECOND(CounterSpeed.THIRD, 0.01D, 3L, 1.5D),
    FIRST(CounterSpeed.SECOND, 0.01D, 4L, 1.15D),
    ;

    private final CounterSpeed nextCounterSpeed;
    private final double multiplier;
    private final long ticks;
    private final double threshold;

}
