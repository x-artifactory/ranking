package io.github.x.artifactory.strategy.ranking.exception;


/**
 * Thrown to indicate that this ranking type cannot be used in ranking algorithms,
 * typically due to numerical instability issues.
 */
public class UnsupportedTypeRankException extends RankingStrategyException {

    /**
     * Constructs a {@code NoUnambiguousResultException}, saving a reference
     * to the error message string {@code s} for later retrieval by the
     * {@code getMessage} method.
     *
     * @param s the detail message.
     */
    public UnsupportedTypeRankException(String s) {
        super(s);
    }

}
