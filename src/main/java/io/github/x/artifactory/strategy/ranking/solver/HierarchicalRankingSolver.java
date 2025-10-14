package io.github.x.artifactory.strategy.ranking.solver;

import io.github.x.artifactory.strategy.ranking.exception.NonUniqueRankException;
import io.github.x.artifactory.strategy.ranking.exception.UnsupportedTypeRankException;
import io.github.x.artifactory.strategy.ranking.operation.FindOperation;
import io.github.x.artifactory.strategy.ranking.operation.SortOperation;

import java.util.Collection;
import java.util.List;

/**
 * Hierarchical ranking solver.
 *
 * <p>This interface offers operations for hierarchical search {@link #findByRank(FindOperation, Collection)}, ordering
 * {@link #sortByRank(SortOperation, Collection)}.
 *
 * <p>Hierarchical ranking (a.k.a. multi-level, multi-key or nested sorting) is the ranking performed recursively
 * according to multiple criteria, provided in a specific order of importance.
 *
 * <p>At first, all elements belong to a single root group. The ordering of the group’s elements is undefined if they
 * are more than one. If so, the first ranking function is applied to group elements. When it results to a set having
 * groups containing more than one element the same process is repeated, applying a next ranking function, and so on
 * recursively until all ties are resolved. If all ranking criteria are exhausted but groups with more than one element
 * remain, an exception {@link NonUniqueRankException} is to be thrown.
 *
 * <p>The search operations are distinct from sorting operations in that they retain only a single leading group of
 * elements at each ranking stage, discarding the rest.
 *
 * @implNote Due to numerical stability concerns, it is not allowed to use ranks of floating-point types in internal
 *     grouping or sorting operations directly. Attempting to do so should result in {@link UnsupportedTypeRankException}
 *     being thrown.
 *
 * @param <W> the type of elements in the collection; this type must provide indexed access to ranking criteria
 *           evaluation functions
 */
public interface HierarchicalRankingSolver<W> {

    /**
     * Hierarchically search for the lowest (highest) element in the collection by sequentially filtering its elements
     * based on a list of their rank values.
     *
     * @param operation determines whether a minimum or maximum search is required
     * @param collection the collection in which the minimum or maximum element is to be found
     * @return the min (max) element or {@code null} if the input collection is empty
     * @throws NonUniqueRankException when unambiguous result is not achieved
     */
    W findByRank(FindOperation operation, Collection<W> collection);

    /**
     * Hierarchically sorts the collection in ascending or descending order by sequentially grouping its elements
     * based on their rank values.
     *
     * @param operation determines whether ascending or descending order of sorting is required
     * @param collection the collection to be sorted
     * @return the collection sorted in ascending (descending) order or {@code null} if the input collection is empty
     * @throws NonUniqueRankException when unambiguous result is not achieved
     */
    List<W> sortByRank(SortOperation operation, Collection<W> collection);

}