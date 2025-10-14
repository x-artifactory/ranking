package io.github.x.artifactory.strategy.ranking.rmapping;

import io.github.x.artifactory.strategy.ranking.ulp.Interval;

import java.util.NoSuchElementException;

/**
 * An ordered sequence of disjoint intervals.
 *
 * <p>The type provides a method {@link #intervalByValue(Comparable)} that maps values to an index of
 * an interval it falls into. Disjoint intervals are composed so that the distance between the end
 * of any interval and the beginning of the next one is at least the value comparison tolerance.
 *
 * <p>A value is considered to be inside an interval if it is not less than its minimal border and
 * not greater that its maximal border given equality is estimated with a certain tolerance.
 *
 * <p>The correct use require that mappings for only values that belong to intervals be requested.
 *
 * @param <E> the type of the element to be mapped to an interval it falls into
 *
 * @see Interval
 */
public interface RankMap<E extends Comparable<E>> {

    /**
     * Search for an index of a disjoint interval that contains the {@code value}.
     *
     * @param value value to map
     * @return an index of a disjoint interval that contains the {@code value}
     * @throws NoSuchElementException if the {@code value} doesn't belong to any interval
     */
    Integer intervalByValue(E value);

    /**
     * {@return a string representation of the mapping scheme}
     *
     * i.e. the ordered sequence of disjoint intervals composed from an initial collection of floating-point numbers.
     */
    String getMappingScheme();
}
