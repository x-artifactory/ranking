package io.github.x.artifactory.strategy.ranking.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * The supertype for classes that define ranking functions with their application orders.
 *
 * <p>Extension example:
 * <pre>{@code
 *
 * public class RankMyType extends RankWrapper<MyType> {
 *
 * @Override
 * public List<Supplier<Comparable<?>>> rankFunctionList() {
 *      return List.of(
 *          this::positiveRank,
 *          this::negativeRank,
 *          this::anotherBigRank,
 *          this::alphabeticalRank);
 *      }
 *
 *     Float positiveRank() {
 *         ...
 *     }
 *
 *     double negativeRank() {
 *         ...
 *     }
 *
 *     String alphabeticalRank() {
 *         ...
 *     }
 *
 *     BigDecimal anotherBigRank() {
 *         ...
 *     }
 *
 * }
 * }</pre>
 *
 * <p>Example of how to use it for a ranked collection instantiation:
 * <pre>{@code
 *
 * final Collection<MyType> collection = getAsyncGenResults();
 * final var rc = RankedCollection.from(collection, RankMyType::new);
 * final var collectionMinElem = rc.min();
 * final var collectionSortedDesc = rc.desc();
 *  ...
 * }</pre>
 *
 * @param <E> type of object for which the rank is calculated
 *
 * @author <a href="mailto:abbbrs@icloud.com">Aleksei Baranov</a>
 */
public abstract class RankWrapper<E> {

    /**
     * Object for which the rank is calculated.
     */
    private E element;

    /**
     * Rank value cache.
     */
    private final Map<Integer, Comparable<?>> rankValueMap = new HashMap<>();

    /**
     * Returns the rank value by function index.
     *
     * @param functionIndex rank calculation function index
     * @return returns the rank value
     */
    @SuppressWarnings("java:S1452")
    public final Comparable<?> rank(int functionIndex) {
        return rankValueMap.computeIfAbsent(functionIndex, index -> rankFunctionList().get(index).get());
    }

    /**
     * Returns a list of functions for calculating object ranks.
     *
     * @return a list of functions for calculating object ranks
     */
    @SuppressWarnings("java:S1452")
    public abstract List<Supplier<Comparable<?>>> rankFunctionList();

    /**
     * Initializes with an object for which the rank is calculated.
     *
     * @param element object for which the rating is calculated
     */
    public final void set(E element) {
        this.element = element;
    }

    /**
     * Returns an object for which the rank is calculated.
     *
     * @return object for which the rank is calculated
     */
    public final E get() {
        return element;
    }

}
