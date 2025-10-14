package io.github.x.artifactory.strategy.ranking.rmapping;

import io.github.x.artifactory.strategy.ranking.ulp.Interval;
import io.github.x.artifactory.strategy.ranking.ulp.RelationalOperations;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.IntFunction;
import java.util.stream.Stream;

class MappingUtils {

    private MappingUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    static <E extends Comparable<E>> List<Interval<E>> searchIntervalBoundaries(
            List<E> values, int precisionUlp) {
        final AtomicReference<Interval<E>> currentInterval = new AtomicReference<>(
                new Interval<>(values.getFirst(), values.getFirst()));
        final IntFunction<E> nextElement = nextIndex -> nextIndex > values.size() - 1
                ? values.getLast()
                : values.get(nextIndex);

        return Stream.iterate(0, i -> i < values.size(), i -> ++i).map(i -> {
                    if (!overlap(List.of(values.get(i), nextElement.apply(i + 1)), precisionUlp)) {
                        final var disjointInterval = currentInterval.get();
                        currentInterval.set(new Interval<>(nextElement.apply(i + 1), nextElement.apply(i + 1)));

                        return disjointInterval;
                    }

                    currentInterval.set(new Interval<>(currentInterval.get().min(), nextElement.apply(i + 1)));

                    return i == values.size() - 1
                            ? currentInterval.get()
                            : null;
                })
                .filter(Objects::nonNull)
                .distinct()
                .toList();
    }

    static <E extends Comparable<E>> boolean overlap(List<E> neighborValues, int precisionUlp) {
        return RelationalOperations.compare(neighborValues.getFirst(), neighborValues.getLast(), precisionUlp) == 0;
    }

}