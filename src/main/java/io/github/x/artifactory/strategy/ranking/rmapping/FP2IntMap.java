package io.github.x.artifactory.strategy.ranking.rmapping;

import io.github.x.artifactory.strategy.ranking.ulp.Interval;
import java.util.Collection;
import java.util.List;

public record FP2IntMap<E extends Comparable<E>> (
        List<Interval<E>> intervalBoundariesAsc) implements RankMap<E> {

    public static <E extends Comparable<E>> FP2IntMap<E> from(Collection<E> values) {
        return from(values, 1);
    }

    public static <E extends Comparable<E>> FP2IntMap<E> from(Collection<E> values, int precisionUlp) {
        if (precisionUlp < 1) {
            throw new IllegalArgumentException("This parameter must be not less than one.");
        }

        return new FP2IntMap<>(MappingUtils.searchIntervalBoundaries(values.stream()
                .distinct().sorted().toList(), precisionUlp));
    }

    @Override
    public Integer intervalByValue(E value) {
        return intervalBoundariesAsc().indexOf(intervalBoundariesAsc().stream()
                .filter(interval -> interval.contains(value))
                .reduce((a, b) -> {
                    throw new IllegalStateException("only one result is allowed");
                })
                .orElseThrow());
    }

    @Override
    public String getMappingScheme() {
        return this.intervalBoundariesAsc.stream().map(interval ->
                "[" + interval.min().toString() + ";" + interval.max().toString() + "]").toList().toString();
    }

}
