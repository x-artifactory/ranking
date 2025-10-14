/**
 * Contains methods for the conversion of a collection of floating-point numbers to an ordered sequence
 * of disjoint intervals (composed from these numbers) with the aim of providing the mapping that ensures
 * the numerical stable sorting together with fuzzy floating-point number comparison.
 *
 * <p>Two main interfaces providing these features are {@link io.github.x.artifactory.strategy.ranking.rmapping.RankMap}
 * for a floating-point number value mapping to an integer index of a disjoint interval it is falling into,
 * and {@link io.github.x.artifactory.strategy.ranking.rmapping.RankMapFactory} to create such a mapping from the initial
 * floating point value set.
 */
package io.github.x.artifactory.strategy.ranking.rmapping;