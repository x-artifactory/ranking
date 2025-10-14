package io.github.x.artifactory.strategy.ranking.collection;

import io.github.x.artifactory.strategy.ranking.api.RankedCollection;
import io.github.x.artifactory.strategy.ranking.ranks.RanksIntervalUlp_1;
import io.github.x.artifactory.strategy.ranking.ranks.RanksIntervalUlp_2;
import io.github.x.artifactory.strategy.ranking.ranks.StringListRanks;
import io.github.x.artifactory.strategy.ranking.ranks.StringRanks;
import io.github.x.artifactory.strategy.ranking.exception.NonUniqueRankException;
import java.util.List;
import java.util.function.Supplier;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@SuppressWarnings("java:S5778")
class RankedCollectionTest {

    // Cases with one single "string" element

    @Test
    @DisplayName("exact_1: Throwing exceptions when searching for extreme values and when sorting.")
    void rankedCollectionExactTestCase1() {
        final Supplier<RankedCollection<String>> instantiate = () -> RankedCollection.from(
                List.of("a", "a"), StringRanks::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().min());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().max());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().desc());
    }

    @Test
    @DisplayName("exact_2: Successful search for maximal value, but throwing exceptions when sorting and searching for the minimum.")
    void rankedCollectionExactTestCase2() {
        final Supplier<RankedCollection<String>> instantiate = () -> RankedCollection.from(
                List.of("a", "a", "b"), StringRanks::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().min());
        Assertions.assertEquals("b", instantiate.get().max().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().desc());
    }

    @Test
    @DisplayName("exact_3: Successful search for minimal value, but throwing exceptions when sorting and searching for the maximum.")
    void rankedCollectionExactTestCase3() {
        final Supplier<RankedCollection<String>> instantiate = () -> RankedCollection.from(
                List.of("a", "b", "b"), StringRanks::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals("a", instantiate.get().min().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().max());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().desc());
    }

    @Test
    @DisplayName("exact_4: Successful search for extreme values, but throwing exceptions when sorting.")
    void rankedCollectionExactTestCase4() {
        final Supplier<RankedCollection<String>> instantiate = () -> RankedCollection.from(
                List.of("a", "b", "b", "c"), StringRanks::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals("a", instantiate.get().min().orElseThrow());
        Assertions.assertEquals("c", instantiate.get().max().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().desc());
    }

    @Test
    @DisplayName("exact_5: Successful search for extreme values and both sorts.")
    void rankedCollectionExactTestCase5() {
        final Supplier<RankedCollection<String>> instantiate = () -> RankedCollection.from(
                List.of("a", "b", "c", "d"), StringRanks::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals("a", instantiate.get().min().orElseThrow());
        Assertions.assertEquals("d", instantiate.get().max().orElseThrow());
        Assertions.assertEquals("[a, b, c, d]", instantiate.get().asc().orElseThrow().toString());
        Assertions.assertEquals("[d, c, b, a]", instantiate.get().desc().orElseThrow().toString());
    }

    // Cases with lists of "string" elements

    @Test
    @DisplayName("exact_6: Successful search for extreme values and both sorts.")
    void rankedCollectionExactTestCase6() {
        final var collection = List.of(
                List.of("a", "b"),
                List.of("b", "a"));
        final Supplier<RankedCollection<List<String>>> instantiate = () -> RankedCollection.from(
                collection, StringListRanks::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals("[a, b]", instantiate.get().min().orElseThrow().toString());
        Assertions.assertEquals("[b, a]", instantiate.get().max().orElseThrow().toString());
        Assertions.assertEquals("[[a, b], [b, a]]", instantiate.get().asc().orElseThrow().toString());
        Assertions.assertEquals("[[b, a], [a, b]]", instantiate.get().desc().orElseThrow().toString());
    }

    @Test
    @DisplayName("exact_7: Successful search for extreme values and both sorts.")
    void rankedCollectionExactTestCase7() {
        final var collection = List.of(
                List.of("a", "b"),
                List.of("b", "a"),
                List.of("b", "b", "b")
        );

        final var rc = RankedCollection.from(collection, StringListRanks::new);
        Assertions.assertEquals("[b, b, b]", rc.min().orElseThrow().toString());
        Assertions.assertEquals("[b, a]", rc.max().orElseThrow().toString());
        Assertions.assertEquals("[[b, b, b], [a, b], [b, a]]", rc.asc().orElseThrow().toString());
        Assertions.assertEquals("[[b, a], [a, b], [b, b, b]]", rc.desc().orElseThrow().toString());
    }

    @Test
    @DisplayName("exact_8: Successful search for the maximum, but an exception is thrown when searching for the minimum and when sorting.")
    void rankedCollectionExactTestCase8() {
        final var collection = List.of(
                List.of("a", "b"),
                List.of("b", "a"),
                List.of("a", "a", "a"), //  As per logic defined in "StringListRanks" ("a", "a", "a") and
                List.of("b", "b", "b")  //  ("b", "b", "b") have the same rank value, i.e. are equals.
        );
        final Supplier<RankedCollection<List<String>>> instantiate = () -> RankedCollection.from(
                collection, StringListRanks::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().min());
        Assertions.assertEquals("[b, a]", instantiate.get().max().orElseThrow().toString());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().desc());
    }

    @Test
    @DisplayName("exact_8: Successful search for the maximum, but an exception is thrown when searching for the minimum and when sorting." +
            "Testing all operations with one collection instance.")
    void rankedCollectionExactTestCase88() {
        final var collection = List.of(
                List.of("a", "b"),
                List.of("b", "a"),
                List.of("a", "a", "a"), //  As per logic defined in "StringListRanks" ("a", "a", "a") and
                List.of("b", "b", "b")  //  ("b", "b", "b") have the same rank value, i.e. are equals.
        );
        final Supplier<RankedCollection<List<String>>> instantiate = () -> RankedCollection.from(
                collection, StringListRanks::new);
        final var coll = instantiate.get();
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertThrows(NonUniqueRankException.class, coll::min);
        Assertions.assertEquals("[b, a]", coll.max().orElseThrow().toString());
        Assertions.assertThrows(NonUniqueRankException.class, coll::asc);
        Assertions.assertThrows(NonUniqueRankException.class, coll::desc);
    }

    // with rank values classification Ulp

    @Test
    @DisplayName("intervalUlp_1: throw the exception because of 1 ulp distance between 746.5784f and 746.57837f")
    void rankedCollectionIntervalUlpTestCase1() {
        final var initCollection = List.of(10f, 100f, 20f, 746.5784f, 746.57837f, 504.0f, 200f);
        final Supplier<RankedCollection<Float>> instantiate = () -> RankedCollection.from(
                initCollection, RanksIntervalUlp_1::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals(10f, instantiate.get().min().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().max().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().desc().orElseThrow());
    }

    @Test
    @DisplayName("intervalUlp_1: throw the exception because of 2 ulp distance between 746.5784f and 746.5783f")
    void rankedCollectionIntervalUlpTestCase2() {
        final var initCollection = List.of(10f, 100f, 20f, 746.5784f, 746.5783f, 504.0f, 200f);
        final Supplier<RankedCollection<Float>> instantiate = () -> RankedCollection.from(
                initCollection, RanksIntervalUlp_1::new);
        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals(10f, instantiate.get().min().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().max().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().desc().orElseThrow());
    }

    @Test
    @DisplayName("intervalUlp_2: doesn't throw exception")
    void rankedCollectionIntervalUlpTestCase3() {
        final var initCollection = List.of(10f, 100f, 20f, 746.5784f, 746.57837f, 504.0f, 200f);
        final Supplier<RankedCollection<Float>> instantiate = () -> RankedCollection.from(
                initCollection, RanksIntervalUlp_2::new);

        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals(10f, instantiate.get().min().orElseThrow());

        // for the original values multiplied by 4: Math.nextUp(2986.3135f) = 2986.3137 -> throw exception
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().max().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () ->  instantiate.get().desc().orElseThrow());
    }

    @Test
    @DisplayName("intervalUlp_2: doesn't throw exception")
    void rankedCollectionIntervalUlpTestCase4() {
        final var initCollection = List.of(0.1f, 1f, 0.2f, 0.5784f, 0.5784001f, 504.0f, 200f);
        final Supplier<RankedCollection<Float>> instantiate = () -> RankedCollection.from(
                initCollection, RanksIntervalUlp_2::new);

        Assertions.assertDoesNotThrow(instantiate::get);
        Assertions.assertEquals(0.1f, instantiate.get().min().orElseThrow());
        Assertions.assertEquals(504f, instantiate.get().max().orElseThrow());

        // for the original values multiplied by 4: Math.nextUp(2986.3135f) = 2986.3137 -> throw exception
        Assertions.assertThrows(NonUniqueRankException.class, () -> instantiate.get().asc().orElseThrow());
        Assertions.assertThrows(NonUniqueRankException.class, () ->  instantiate.get().desc().orElseThrow());
    }

}