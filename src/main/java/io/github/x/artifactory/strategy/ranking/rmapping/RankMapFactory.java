package io.github.x.artifactory.strategy.ranking.rmapping;

import java.util.Collection;


public interface RankMapFactory<E extends Comparable<E>> {

    RankMap<E> findMapper(Collection<E> ranks);
}
