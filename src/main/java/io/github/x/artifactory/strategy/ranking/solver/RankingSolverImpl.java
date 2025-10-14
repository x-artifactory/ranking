package io.github.x.artifactory.strategy.ranking.solver;

import io.github.x.artifactory.strategy.ranking.exception.NonUniqueRankException;
import io.github.x.artifactory.strategy.ranking.exception.RankValueAccessor;
import io.github.x.artifactory.strategy.ranking.rmapping.MappingCash;
import io.github.x.artifactory.strategy.ranking.rmapping.RankMapFactory;
import io.github.x.artifactory.strategy.ranking.operation.AccessComparatorType;
import io.github.x.artifactory.strategy.ranking.operation.FindOperation;
import io.github.x.artifactory.strategy.ranking.operation.SortOperation;
import io.github.x.artifactory.strategy.ranking.api.RankWrapper;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.x.artifactory.strategy.ranking.exception.RankValueAccessor.getRankValue;
import static java.util.stream.Collectors.mapping;
import static java.util.stream.Collectors.toList;

/**
 * Implementation of the {@link HierarchicalRankingSolver} interface.
 *
 * <p>This implementation relies on the collection element type providing a list of ranking functions.
 *
 * @param <W> the type of elements held in a collection; must provide its ranking criteria values in a predefined order
 * @param <R> the type of the rank value; must implement the {@link Comparable} interface
 */
public final class RankingSolverImpl<W extends RankWrapper<?>, R extends Comparable<R>>
        implements HierarchicalRankingSolver<W> {

    private final RankMapFactory<R> mappingFactory = new MappingCash<>();

    /**
     * {@inheritDoc}
     *
     * @param operation  determines whether a minimum or maximum search is required
     * @param collection the collection in which the minimum or maximum element is to be found
     * @return the min (max) element or {@code null} if the input collection is empty
     * @throws NonUniqueRankException when unambiguous result is not achieved
     */
    @Override
    public W findByRank(FindOperation operation, Collection<W> collection) {
        if (collection.isEmpty()) {
            return null;
        }

        var candidateList = collection;
        int rankFunctionIndex = 0;

        while (true) {
            final Function<W, R> getRank = getElementRankMapping(collection, rankFunctionIndex);
            candidateList = candidateList.stream()
                    .collect(Collectors.groupingBy(getRank))
                    .entrySet().stream()
                    .min(getComparator(operation))
                    .orElseThrow()
                    .getValue();

            if (candidateList.size() == 1) {
                return candidateList.iterator().next();
            }

            rankFunctionIndex++;
        }
    }

    /**
     * {@inheritDoc}
     *
     * @param operation determines whether ascending or descending order of sorting is required
     * @param collection the collection to be sorted
     * @return the collection sorted in ascending (descending) order or {@code null} if the input collection is empty
     * @throws NonUniqueRankException when unambiguous result is not achieved
     */
    @Override
    public List<W> sortByRank(SortOperation operation, Collection<W> collection) {
        return collection.isEmpty()
                ? null
                : sortByRank(getComparator(operation), collection, 0);
    }

    private List<W> sortByRank(Comparator<? super Entry<R, List<W>>> comparator, Collection<W> wCollection,
                              int rankFunctionIndex) {
        if (wCollection.size() == 1) {
            return List.copyOf(wCollection);
        }

        final Function<W, R> getRank = getElementRankMapping(wCollection, rankFunctionIndex);

        return wCollection.stream()
                .collect(Collectors.groupingBy(getRank, mapping(Function.identity(), toList()))).entrySet()
                .stream().sorted(comparator)
                .map(entry -> sortByRank(comparator, entry.getValue(), rankFunctionIndex + 1))
                .flatMap(Collection::stream)
                .toList();
    }

    private Comparator<? super Entry<R, List<W>>> getComparator(AccessComparatorType typeAccessor) {
        return switch (typeAccessor.comparatorType()) {
            case NATURAL -> Entry.comparingByKey();
            case REVERSED -> (c1, c2) -> c2.getKey().compareTo(c1.getKey());
        };
    }

    private Function<W, R> getElementRankMapping(Collection<W> wCollection, int functionIndex) {
        final Function<W, R> getRank = e -> evalRankFunction(e, functionIndex);

        return getRank.andThen(getRankMapping(wCollection, functionIndex))
                .andThen(RankValueAccessor::checkRankType);
    }

    private Function<R, R> getRankMapping(Collection<W> wCollection, int functionIndex) {
        final List<R> rankList = wCollection.stream()
                .map(e -> evalRankFunction(e, functionIndex))
                .toList();

        // trivial mapping
        if (rankList.stream().noneMatch(r -> r instanceof Float || r instanceof Double)) {
            return r -> r;
        }

        // floating-point to integer mapping
        final var mapping = mappingFactory.findMapper(rankList);

        return r -> upcast2Comparable(mapping.intervalByValue(r));
    }

    /**
     * Safely converts an {@link Integer} object into its supertype {@link R} that extends {@link Comparable} only.
     *
     * @param value an integer-type object that has to be converted into its higher-level type {@link Comparable}
     * @return the upcasted object
     */
    @SuppressWarnings("unchecked")
    private R upcast2Comparable(Integer value) {
        return (R) value;
    }

    private R evalRankFunction(W element, int functionIndex) {
        return getRankValue(element, functionIndex);
    }

}
