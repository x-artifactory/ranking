package io.github.x.artifactory.strategy.ranking.exception;;

/**
 * Thrown by various ranking methods to indicate that it is not possible to find an unambiguous, unique result.
 */
public class NonUniqueRankException extends RankingStrategyException {

    /**
     * Constructs a {@code NoUnambiguousResultException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param s the detail message.
     */
    public NonUniqueRankException(String s) {
        super(s);
    }
}
