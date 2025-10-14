package io.github.x.artifactory.strategy.ranking.util;

public class UnitTestUtils {

    private UnitTestUtils() {
    }

    public static float nextUpTimes(float value, int times) {
        for (int i = 0; i < times; i++) {
            value = Math.nextUp(value);
        }

        return value;
    }

    public static double nextUpTimes(double value, int times) {
        for (int i = 0; i < times; i++) {
            value = Math.nextUp(value);
        }

        return value;
    }
}