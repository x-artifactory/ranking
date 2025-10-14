package io.github.x.artifactory.strategy.ranking.ranks;

import java.util.List;
import java.util.function.Supplier;

public class RanksIntervalUlp_2 extends RanksIntervalUlp_1 {

    @Override
    public List<Supplier<Comparable<?>>> rankFunctionList() {
        return List.of(
                this::positiveFour,
                super.rankFunctionList().getFirst()
        );
    }

    private float positiveFour() {
        return 4 * get();
    }

}
