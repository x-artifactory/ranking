package io.github.x.artifactory.strategy.ranking.ulp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.x.artifactory.strategy.ranking.util.UnitTestUtils.nextUpTimes;


class IntervalTest {

    @Test
    void intervalInstCase1() {
        Assertions.assertDoesNotThrow(() -> new Interval<>(0.5784f, 0.57840014f));
    }

    @Test
    void intervalInstCase2() {
        final var min = nextUpTimes(0.57840014f, 1);
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Interval<>(min, 0.5784f));
    }

    @Test
    void intervalInstCase3() {
        Assertions.assertDoesNotThrow(() -> new Interval<>(0.57840014f, Math.nextDown(0.57840014f)));
    }

    @Test
    void intervalInstCase4() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> new Interval<>(0.5784f, 0.57840014f, -1));
    }

    @Test
    void intervalCase1() {
        final var interval = new Interval<>(0.5784f, 0.57840014f);

        Assertions.assertFalse(interval.ltMin(0.57840014f));
        Assertions.assertFalse(interval.gtMax(0.57840014f));
        Assertions.assertTrue(interval.contains(0.57840014f));
    }

    @Test
    void intervalCase2() {
        final var interval = new Interval<>(0.5784f, 0.57840014f);

        Assertions.assertFalse(interval.ltMin(0.5784f));
        Assertions.assertFalse(interval.gtMax(0.5784f));
        Assertions.assertTrue(interval.contains(0.5784f));
    }

    @Test
    void intervalCase3() {
        final var interval = new Interval<>(0.5784f, 0.57840014f);

        final var valueToTest = nextUpTimes(0.57840014f, 1);

        Assertions.assertFalse(interval.ltMin(valueToTest));
        Assertions.assertFalse(interval.gtMax(valueToTest));
        Assertions.assertTrue(interval.contains(valueToTest));
    }

    @Test
    void intervalCase4() {
        final var interval = new Interval<>(0.5784f, 0.57840014f);

        final var valueToTest = nextUpTimes(0.57840014f, 3);

        Assertions.assertFalse(interval.ltMin(valueToTest));
        Assertions.assertTrue(interval.gtMax(valueToTest));
        Assertions.assertFalse(interval.contains(valueToTest));
    }

}