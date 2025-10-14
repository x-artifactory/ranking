package io.github.x.artifactory.strategy.ranking.operation;


import static io.github.x.artifactory.strategy.ranking.operation.ComparatorType.NATURAL;
import static io.github.x.artifactory.strategy.ranking.operation.ComparatorType.REVERSED;

/**
 * Specifies the sorting operations in both ascending and descending orders,
 * as well as the associated comparator type.
 */
public enum SortOperation implements AccessComparatorType {

    SORT_ASC(NATURAL),
    SORT_DESC(REVERSED);

    private final ComparatorType type;

    SortOperation(ComparatorType type) {
        this.type = type;
    }

    @Override
    public ComparatorType comparatorType() {
        return type;
    }
}
