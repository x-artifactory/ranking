package io.github.x.artifactory.strategy.ranking.collection;

import io.github.x.artifactory.strategy.ranking.api.RankWrapper;
import io.github.x.artifactory.strategy.ranking.api.RankedCollection;
import io.github.x.artifactory.strategy.ranking.exception.NonUniqueRankException;
import io.github.x.artifactory.strategy.ranking.operation.FindOperation;
import io.github.x.artifactory.strategy.ranking.operation.SortOperation;


import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.x.artifactory.strategy.ranking.solver.HierarchicalRankingSolver;
import io.github.x.artifactory.strategy.ranking.solver.RankingSolverImpl;

import static io.github.x.artifactory.strategy.ranking.operation.FindOperation.FIND_MAX;
import static io.github.x.artifactory.strategy.ranking.operation.FindOperation.FIND_MIN;
import static io.github.x.artifactory.strategy.ranking.operation.SortOperation.SORT_ASC;
import static io.github.x.artifactory.strategy.ranking.operation.SortOperation.SORT_DESC;

/**
 * Implementation of the {@link RankedCollection} interface that allows floating-point number ranks.
 *
 * <p>This implementation provides two design features against the non-deterministic behavior issues that may arise from
 * inaccurate decision-making logic about how to order elements with identical ranks or from a typical widespread practice
 * of floating-point number comparison in the context of sorting algorithms.
 *
 * <p>Operations {@link #asc()} and {@link #desc()} sorts the elements of the initial collection hierarchically (multi -
 * level or nested sorting) according to multiple criteria, provided in a specific order of importance.
 *
 * <p>At first, all elements belong to a single root group. The ordering of the group’s elements is undefined if they
 * are more than one. If so, the first ranking function is applied to group elements. If it results to a set having
 * groups containing more than one element the same process is repeated, applying a next ranking function, and so on
 * recursively until all ties are resolved. When all ranking criteria have been exhausted but groups with more than
 * one element remain, an exception {@link NonUniqueRankException} is to be thrown.
 *
 * <p>In this way an instance of this collection type exclusively defines the element ordering, ignoring the initial
 * collection sorting, which potentially can vary from one run to another, even if all parameters remain unchanged.
 *
 * <p>The search operations {@link #min()} and {@link #max()} are distinct from sorting operations in that they retain
 * only a single leading group of elements at each ranking stage, discarding the rest.
 *
 * <p>The challenge with using floating-point numbers in sorting algorithms is that while it is universally accepted
 * that comparisons between them must be made with a certain tolerance, this approach is incompatible with sorting
 * algorithms. Introducing a tolerance level for comparisons allows for the possibility of a = b and b = c, yet a ≠ c,
 * violating the transitivity requirement for sorting algorithms. Consequently, we claim that floating-point numbers
 * are unsuitable for direct use in sorting if numerical stability is required.
 *
 * <p>To overcome this, firstly an ordered sequence of intervals made up of sorted initial floating-point number values
 * is created in such a way that any interval contains its first value followed by all values, each of which is equal
 * to a value immediately preceding it, given equality is estimated with a certain tolerance; secondly the direct raw
 * use of floating-point numbers in sorting is excluded by translating them into integer type indexes of intervals they
 * are falling into.
 *
 * @param <E> the type of elements held in the source collection
 * @param <W> the type of source elements wrapper that provides ranking evaluation functions
 */
public final class RankedCollectionFloatUlp<E, W extends RankWrapper<E>>
        implements RankedCollection<E> {

    /**
     * Collection of source elements wrapped in the object of the type defining rating calculation functions.
     */
    private final Collection<W> wCollection;

    /**
     * Hierarchically ranking solver.
     */
    private final HierarchicalRankingSolver<W> solver = new RankingSolverImpl<>();

    /**
     * Constructs a collection having ranking methods.
     *
     * @param candidates collection of source elements to be ranked
     * @param rankFunctionsInstanceSupplier method creating an instance of a type that defines
     *     the rating calculation function for an element of the source collection to be ranked
     */
    public RankedCollectionFloatUlp(Collection<E> candidates, Supplier<W> rankFunctionsInstanceSupplier) {
        final Function<E, W> wrapElementWithRank = elem -> {
            final var newRankElement = rankFunctionsInstanceSupplier.get();
            newRankElement.set(elem);

            return newRankElement;
        };

        this.wCollection = candidates.stream()
                .map(wrapElementWithRank)
                .toList();
    }

    /**
     * {@inheritDoc}
     * @return the unique lowest-ranked element
     * @throws NoSuchElementException if there is no such unique element
     */
    @Override
    public Optional<E> min() {
        return find(FIND_MIN);
    }

    /**
     * {@inheritDoc}
     * @return the unique highest-ranked element
     * @throws NoSuchElementException if there is no such unique element
     */
    @Override
    public Optional<E> max() {
        return find(FIND_MAX);
    }

    /**
     * {@inheritDoc}
     * @return the collection sorted in ascending order
     * @throws NoSuchElementException if it is not possible to obtain a stable sorting of the collection due to
     *     the presence of elements with the same rating value for all ranking functions
     */
    @Override
    public Optional<List<E>> asc() {
        return sort(SORT_ASC);
    }

    /**
     * {@inheritDoc}
     * @return the collection sorted in descending order
     * @throws NoSuchElementException if it is not possible to obtain a stable sorting of the collection due to
     *     the presence of elements with the same rating value for all ranking functions
     */
    @Override
    public Optional<List<E>> desc() {
        return sort(SORT_DESC);
    }

    // find

    private Optional<E> find(FindOperation operation) {
        return wCollection.isEmpty()
                ? Optional.empty()
                : Optional.of(findByRank(operation, wCollection).get());
    }

    private W findByRank(FindOperation operation, Collection<W> wCollection){
        return solver.findByRank(operation, wCollection);
    }

    // sort

    private Optional<List<E>> sort(SortOperation operation) {
        return wCollection.isEmpty()
                ? Optional.empty()
                : Optional.of(sortByRank(operation, wCollection).stream()
                        .map(RankWrapper::get).toList());
    }

    private List<W> sortByRank(SortOperation operation, Collection<W> wCollection){
        return solver.sortByRank(operation, wCollection);
    }

}