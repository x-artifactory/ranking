package io.github.x.artifactory.strategy.ranking.operation;

import static io.github.x.artifactory.strategy.ranking.operation.ComparatorType.NATURAL;
import static io.github.x.artifactory.strategy.ranking.operation.ComparatorType.REVERSED;

/**
 * Specifies the search operations for lowest and highest elements of a collection,
 * as well as the associated comparator type.
 */
public enum FindOperation implements AccessComparatorType {

    FIND_MIN(NATURAL),
    FIND_MAX(REVERSED);

    private final ComparatorType comparatorType;

    FindOperation(ComparatorType comparatorType) {
        this.comparatorType = comparatorType;
    }

    @Override
    public ComparatorType comparatorType() {
        return comparatorType;
    }
}
