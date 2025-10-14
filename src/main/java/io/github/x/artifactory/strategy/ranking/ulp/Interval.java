package io.github.x.artifactory.strategy.ranking.ulp;


/**
 * Interval.
 *
 * <p>Contains methods for identification a given value's position with respect to interval boundaries:
 * <ul>
 *   <li>{@link #ltMin(Comparable)}</li>
 *   <li>{@link #gtMax(Comparable)}</li>
 *   <li>{@link #contains(Comparable)}</li>
 * </ul>
 *
 * <p>Floating-point numbers are compared using a fuzzy equality relation. Two values {@code x} and {@code y}
 * are considered equal if their absolute difference doesn't exceed the sum of two units of the least precision
 * evaluated for each one separately and taken {@code ulpMultiplier} times; i.e. when the following expression
 * is true:
 *
 * <pre>{@code
 *
 * abs(x - y) <= ulpMultiplier * (ulp(x) + ulp(y));
 *
 * }</pre>
 *
 * @param min minimal interval boundary
 * @param max maximal interval boundary
 * @param ulpMultiplier multiplier
 * @param <T> type of interval boundaries
 */
public record Interval<T extends Comparable<T>>(T min, T max, int ulpMultiplier) {

    public Interval {
        if (RelationalOperations.compare(min, max, ulpMultiplier) > 0) {
            throw new IllegalArgumentException("min is greater than max.");
        }
    }

    /**
     * Constructs an interval with {@code ulpMultiplier} set to one.
     *
     * @param min minimal interval boundary
     * @param max maximal interval boundary
     */
    public Interval(T min, T max) {
        this(min, max, 1);
    }

    /**
     * Determines whether a parameter {@code value} falls within this interval.
     *
     * @param value parameter value
     * @return {@code true} if the parameter {@code value} lies within the boundaries of the interval
     */
    public boolean contains(T value) {
        return !ltMin(value) && !gtMax(value);
    }

    /**
     * Determines if a parameter {@code value} falls below the minimum boundary of this interval.
     *
     * @param value parameter value
     * @return {@code true} if the parameter {@code value} is less than the minimum boundary of this interval
     */
    public boolean ltMin(T value) {
        return RelationalOperations.compare(value, min(), ulpMultiplier) < 0;
    }

    /**
     * Determines if a parameter {@code value} falls above the maximum boundary of this interval.
     *
     * @param value parameter value
     * @return {@code true} if the parameter {@code value} is greater than the maximum boundary of this interval
     */
    public boolean gtMax(T value) {
        return RelationalOperations.compare(value, max(), ulpMultiplier) > 0;
    }

}

