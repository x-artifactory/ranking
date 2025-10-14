package io.github.x.artifactory.strategy.ranking.ranks;

import io.github.x.artifactory.strategy.ranking.api.RankWrapper;
import java.util.List;
import java.util.function.Supplier;

public class RanksIntervalUlp_1 extends RankWrapper<Float>  {

    @Override
    public List<Supplier<Comparable<?>>> rankFunctionList() {
        return List.of(
                this::positiveOne,
                this::negativeOne
        );
    }

    private float positiveOne() {
        return get();
    }

    private float negativeOne() {
        return -get();
    }
}
