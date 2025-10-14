package io.github.x.artifactory.strategy.ranking.mapper;

import java.util.List;
import java.util.NoSuchElementException;

import io.github.x.artifactory.strategy.ranking.rmapping.FP2IntMap;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FP2IntMapperTest {

    @Test
    @DisplayName("1: 1 ulp")
    void Case_1() {
        final var values = List.of(10f, 100f, 20f, 746.5784f, 746.57837f, 504.0f, 200f);
        final var instance = FP2IntMap.from(values);

        Assertions.assertEquals("[" +
                        "[10.0;10.0], " +
                        "[20.0;20.0], " +
                        "[100.0;100.0], " +
                        "[200.0;200.0], " +
                        "[504.0;504.0], " +
                        "[746.57837;746.5784]" +
                        "]",
                instance.getMappingScheme());

        Assertions.assertEquals(5, instance.intervalByValue(746.57837f));
        Assertions.assertEquals(5, instance.intervalByValue(746.5784f));
    }

    @Test
    @DisplayName("2: 1 ulp")
    void Case_2() {
        final var values = List.of(0.1f, 1f, 0.2f, 0.5784f, 0.5784001f, 504.0f, 200f);
        final var instance = FP2IntMap.from(values);

        Assertions.assertEquals("[" +
                        "[0.1;0.1], " +
                        "[0.2;0.2], " +
                        "[0.5784;0.5784001], " +
                        "[1.0;1.0], " +
                        "[200.0;200.0], " +
                        "[504.0;504.0]" +
                        "]",
                instance.getMappingScheme());

        Assertions.assertEquals(2, instance.intervalByValue(0.5784f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784001f));
    }

    @Test
    @DisplayName("3: 1 ulp")
    void Case_3() {
        final var values = List.of(0.1f, 1f, 0.2f, 0.57840014f, 0.5784f, 0.5784001f, 504.0f, 200f);
        final var instance = FP2IntMap.from(values);

        Assertions.assertEquals("[" +
                        "[0.1;0.1], " +
                        "[0.2;0.2], " +
                        "[0.5784;0.57840014], " +
                        "[1.0;1.0], " +
                        "[200.0;200.0], " +
                        "[504.0;504.0]" +
                        "]",
                instance.getMappingScheme());

        Assertions.assertEquals(2, instance.intervalByValue(0.5784f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784001f));
        Assertions.assertEquals(2, instance.intervalByValue(0.57840014f));
    }

    @Test
    @DisplayName("4: 1 ulp")
    void Case_4() {
        final var values = List.of(0.1f, 1f, 0.2f, 0.57840014f, 0.57840025f, 0.5784f, 0.5784001f, 504.0f, 200f);
        final var instance = FP2IntMap.from(values);

        Assertions.assertEquals("[" +
                        "[0.1;0.1], " +
                        "[0.2;0.2], " +
                        "[0.5784;0.57840025], " +
                        "[1.0;1.0], " +
                        "[200.0;200.0], " +
                        "[504.0;504.0]" +
                        "]",
                instance.getMappingScheme());

        Assertions.assertEquals(2, instance.intervalByValue(0.57840014f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784002f));
        Assertions.assertThrows(NoSuchElementException.class, () -> instance.intervalByValue(0.5784433f));
        Assertions.assertEquals(2, instance.intervalByValue(0.57840025f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784001f));
    }

    @Test
    @DisplayName("5: 4 ulp")
    void Case_5() {
        final var values = List.of(0.1f, 1f, 0.2f, 0.57840014f, 0.57840025f, 0.5784f, 0.5784001f, 504.0f, 200f);
        final var instance = FP2IntMap.from(values, 4);

        Assertions.assertEquals(
                "[" +
                        "[0.1;0.1], " +
                        "[0.2;0.2], " +
                        "[0.5784;0.57840025], " +
                        "[1.0;1.0], " +
                        "[200.0;200.0], " +
                        "[504.0;504.0]" +
                        "]",
                instance.getMappingScheme());

        Assertions.assertEquals(2, instance.intervalByValue(0.57840014f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784002f));
        Assertions.assertEquals(2, instance.intervalByValue(0.57840025f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784f));
        Assertions.assertEquals(2, instance.intervalByValue(0.5784001f));
    }

    @Test
    @DisplayName("6: 0 ulp")
    void Case_6() {
        final var values = List.of(0.1f, 1f, 0.2f, 0.57840014f, 0.57840025f, 0.5784f, 0.5784001f, 504.0f, 200f);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> FP2IntMap.from(values, 0));
    }

}