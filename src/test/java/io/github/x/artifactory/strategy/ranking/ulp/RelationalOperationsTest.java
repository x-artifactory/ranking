package io.github.x.artifactory.strategy.ranking.ulp;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static io.github.x.artifactory.strategy.ranking.util.UnitTestUtils.nextUpTimes;
import static org.junit.jupiter.api.Assertions.*;

class RelationalOperationsTest {

    // float

    @Test
    void compareCase1f() {
        Assertions.assertTrue(RelationalOperations.compare(-1f, 1f, 1) < 0);
        assertNotEquals(0, RelationalOperations.compare(-1f, 1f, 1));
        Assertions.assertFalse(RelationalOperations.compare(-1f, 1f, 1) > 0);
    }

    @Test
    void compareCaseSumFNegative() {
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 1), 1) < 0);
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 2), 1) < 0);
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 3), 1) < 0);

        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 1), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 2), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 3), 1) > 0);
    }

    @Test
    void compareCaseSumFPositive() {
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 1), 1) < 0);
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 2), 1) < 0);
        Assertions.assertTrue(RelationalOperations.compare(-1f, nextUpTimes(-1f, 4), 1) < 0);

        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 1), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 2), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1f, nextUpTimes(-1f, 3), 1) > 0);
    }

    // double

    @Test
    void compareCase1d() {
        Assertions.assertTrue(RelationalOperations.compare(-1d, 1d, 1) < 0);
        assertNotEquals(0, RelationalOperations.compare(-1d, 1d, 1));
        Assertions.assertFalse(RelationalOperations.compare(-1d, 1d, 1) > 0);
    }

    @Test
    void compareCase2dNegative() {
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 1), 1) < 0);
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 2), 1) < 0);
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 3), 1) < 0);

        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 1), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 2), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 3), 1) > 0);
    }

    @Test
    void compareCaseSumDPositive() {
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 1), 1) < 0);
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 2), 1) < 0);
        Assertions.assertTrue(RelationalOperations.compare(-1d, nextUpTimes(-1d, 4), 1) < 0);

        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 1), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 2), 1) > 0);
        Assertions.assertFalse(RelationalOperations.compare(-1d, nextUpTimes(-1d, 3), 1) > 0);
    }

    // string

    @Test
    void compareCase1s() {
        Assertions.assertTrue(RelationalOperations.compare("a", "b", 1) < 0);
        assertNotEquals(0, RelationalOperations.compare("a", "b", 1));
        Assertions.assertFalse(RelationalOperations.compare("a", "b", 1) > 0);
    }

}