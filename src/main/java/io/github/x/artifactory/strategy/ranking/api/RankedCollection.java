package io.github.x.artifactory.strategy.ranking.api;

import io.github.x.artifactory.strategy.ranking.collection.RankedCollectionFloatUlp;
import io.github.x.artifactory.strategy.ranking.exception.NonUniqueRankException;
import io.github.x.artifactory.strategy.ranking.exception.UnsupportedTypeRankException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * A collection that provides the hierarchical ranking of its elements.
 *
 * <p>The interface provides the static factory-method {@link #from(Collection, Supplier) } and defines methods for
 * hierarchical ranking {@link #asc asc()}, {@link #desc desc()} of a source collection and to search for its
 * extreme elements {@link #min min()}, {@link #max max()}.
 *
 * <p>The collection must not contain any duplicate elements. Elements and ranking rules are to be defined when
 * the collection is instantiated and remain unchanged afterward.
 *
 * <p>Implementations must provide two design features contributing to the isolation of non-deterministic behavior
 * sources.
 *
 * <p>The <u>first</u> one consists of stating that it is impossible to order two elements having all identical ranks,
 * i.e. the hierarchical ranking process is to be performed until the unambiguous unique result is achieved or a
 * {@link NonUniqueRankException} is thrown.  (To overcome this exceptional situation, the user must either
 * incorporate additional ranking functions or eliminate duplicate items from the initial collection.)
 *
 * <p>The <u>second</u> one supposes that an approach is to be provided to allow floating-point number comparisons with
 * a precision tolerance but without violating the comparison transitivity property required by sorting algorithms.
 * The precision tolerance value should be as small as possible, but not smaller than one unit in the last place, one
 * ulp. Alternatively, the use of floating point number ranks can be forbidden, and an {@link UnsupportedTypeRankException}
 * has to be thrown if they are encountered.
 *
 * <p>Example of how to use see: {@link RankWrapper}.
 *
 * @param <E> the type of elements held in the source collection
 *
 * @author <a href="mailto:abbbrs@icloud.com">Aleksei Baranov</a>
 */
public sealed interface RankedCollection<E> permits RankedCollectionFloatUlp {

    /**
     * Creates an instance of a hierarchically ranked collection from a source elements collection and
     * a supplier of an object that defines a list of ranking functions.
     *
     * @param sourceCollection the source collection of elements to be sorted or in which
     *     the minimum or maximum element to be searched
     * @param rankFunctionsSupplier a function reference returning an object that provides
     *     methods to calculate a collection's element rank values and their application order;
     *     practically, this refers to the constructor of a user-defined class that must extend
     *     {@link RankWrapper}.
     * @return an instance of hierarchically ranked collection
     * @param <E> the type of elements held in the source collection
     * @param <W> the type of rank functions evaluator
     */
    static <E, W extends RankWrapper<E>> RankedCollection<E> from(
            Collection<E> sourceCollection, Supplier<W> rankFunctionsSupplier) {

        return new RankedCollectionFloatUlp<>(sourceCollection, rankFunctionsSupplier);
    }

    /**
     * Searches for the unique lowest-ranked element.
     *
     * @return the unique lowest-ranked element
     * @throws NonUniqueRankException if there is no such unique element
     */
    Optional<E> min();

    /**
     * Searches for the unique highest-ranked element.
     *
     * @return the unique highest-ranked element
     * @throws NonUniqueRankException if there is no such unique element
     */
    Optional<E> max();

    /**
     * Sorts a collection in ascending order, i.e. the first element has the lowest rating values,
     * the last element has the highest rating values, and each subsequent element in the sorted
     * list has at least one rating value higher than its preceding element has.
     *
     * @return the collection sorted in ascending order
     * @throws NonUniqueRankException if it is not possible to obtain an unambiguous sorting
     *     of the collection due to the presence of at least two elements with the same rating value
     *     for all ranking functions
     */
    Optional<List<E>> asc();

    /**
     * Sorts a collection in descending order, i.e. the first element has the highest rating values,
     * the last element has the lowest rating values, and each subsequent element in the sorted list
     * has at least one rating value lower than its preceding element has.
     *
     * @return the collection sorted in descending order
     * @throws NonUniqueRankException if it is not possible to obtain an unambiguous sorting of the collection
     *     due to the presence of at least two elements with the same rating value for all ranking functions
     */
    Optional<List<E>> desc();

}