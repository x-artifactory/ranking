package io.github.x.artifactory.strategy.ranking.ranks;

import io.github.x.artifactory.strategy.ranking.api.RankWrapper;
import java.util.List;
import java.util.function.Supplier;

public class StringRanks extends RankWrapper<String> {

    @Override
    public List<Supplier<Comparable<?>>> rankFunctionList() {
        return List.of(
                this::stringCodeSum
        );
    }

    public Integer stringCodeSum() {
        return StringFunctionLib.stringCodeSum(get());
    }

}
