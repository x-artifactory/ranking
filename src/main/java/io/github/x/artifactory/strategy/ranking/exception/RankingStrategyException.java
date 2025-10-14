package io.github.x.artifactory.strategy.ranking.exception;

/**
 * Supertype for all exceptions thrown in Ranking Strategy Framework.
 */
public class RankingStrategyException extends RuntimeException {

    /**
     * Constructs a {@code NoUnambiguousResultException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param s the detail message.
     */
    public RankingStrategyException(String s) {
        super(s);
    }
}
