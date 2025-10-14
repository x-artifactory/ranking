package io.github.x.artifactory.strategy.ranking.ranks;

public final class StringFunctionLib {

    /**
     * Don't let anyone instantiate this class.
     */
    private StringFunctionLib() {}

    public static Integer stringCodeSum(String string) {
        return string.codePoints().sum();
    }

}
