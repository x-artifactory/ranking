package io.github.x.artifactory.strategy.ranking.rmapping;

import java.util.Collection;
import java.util.HashMap;

public class MappingCash<E extends Comparable<E>>
        implements RankMapFactory<E> {

    private final HashMap<Collection<E>, FP2IntMap<E>> cash = new HashMap<>();

    @Override
    public RankMap<E> findMapper(Collection<E> ranks) {
        return cash.computeIfAbsent(ranks, FP2IntMap::from);
    }

}
