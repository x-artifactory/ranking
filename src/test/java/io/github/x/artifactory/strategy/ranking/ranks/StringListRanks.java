package io.github.x.artifactory.strategy.ranking.ranks;

import io.github.x.artifactory.strategy.ranking.api.RankWrapper;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.IntStream;

public class StringListRanks extends RankWrapper<List<String>> {

    private final BiFunction<List<String>, Integer, Integer> code = (list, index) -> StringFunctionLib.stringCodeSum(
            list.get(index));

    @Override
    public List<Supplier<Comparable<?>>> rankFunctionList() {
        return List.of(
                this::elemCountRank,
                this::sortingRank
        );
    }

    /**
     * Sequence length.
     *
     * <p>The longer string precedes the shorter string.
     * This is the opposite logic to that of the method “compareTo()” in the “String” class,
     * which is done for testing purposes.
     *
     * @return rank value
     */
    public Integer elemCountRank() {
        return -get().size();
    }

    /**
     * Sorting deviation.
     * <p>A larger rating indicates that the sequence of items in the list deviates more from the natural, increasing sequence.
     *
     * @return rank value
     */
    public Float sortingRank() {
        final var ascSorted = get().stream().sorted().toList();
        final var sumDifferenceSquares = IntStream.range(0, get().size() - 1)
                .mapToDouble(index -> Math.pow(Math.abs(code.apply(get(), index) - code.apply(ascSorted, index)), 2))
                .reduce(Double::sum).orElseThrow();

        return (float) Math.sqrt(sumDifferenceSquares);
    }

}
