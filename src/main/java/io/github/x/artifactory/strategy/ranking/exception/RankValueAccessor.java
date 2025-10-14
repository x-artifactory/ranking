package io.github.x.artifactory.strategy.ranking.exception;

import io.github.x.artifactory.strategy.ranking.api.RankWrapper;


/**
 * Ranking value assessor - validator.
 */
public final class RankValueAccessor {
    private RankValueAccessor() {}

    /**
     * Accesses the result of the ranking function at index {@code functionIndex}.
     *
     * @param element the element on which the function is to be applied
     * @param functionIndex the index of the function to be applied
     * @return the result of the function at index {@code functionIndex} applied to this element
     * @param <W> the type of the element on which the function is to be applied
     * @param <R> the type of the function result
     * @throws NonUniqueRankException if a non-existent ranking function is requested
     * @throws ClassCastException when the ranking function returns type that does not implement {@link Comparable}
     */
    @SuppressWarnings("unchecked")
    public static <W extends RankWrapper<?>, R extends Comparable<R>> R getRankValue(W element, int functionIndex) {
        try {
            return (R) element.rank(functionIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NonUniqueRankException("No unique result found after applying all ranking functions.");
        }
    }

    /**
     * Checks if this ranking object type use is allowed within sorting and grouping operations.
     *
     * @param r ranking object
     * @return ranking object
     * @param <R> ranking object type
     * @throws UnsupportedTypeRankException if this ranking object has floating-point number type
     */
    public static <R extends Comparable<R>> R checkRankType(R r) {
        if (r instanceof Double || r instanceof Float) {
            throw new UnsupportedTypeRankException("Due to the numerical stability issue floating type numbers" +
                    "aren't allowed within sorting and grouping operations.");
        }

        return r;
    }

}