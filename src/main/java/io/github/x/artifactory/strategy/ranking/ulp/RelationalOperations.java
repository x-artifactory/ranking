package io.github.x.artifactory.strategy.ranking.ulp;

import static java.lang.Math.abs;
import static java.lang.Math.ulp;

public class RelationalOperations {

    private RelationalOperations() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static <T extends Comparable<T>> int compare(T x, T y, int precision) {
        require(x, precision);

        return switch (x) {
            case Float f -> compare(f.floatValue(), toFloat(y), precision);
            case Double d -> compare(d.doubleValue(), toDouble(y), precision);
            default -> x.compareTo(y);
        };
    }

    private static <T extends Comparable<T>> void require(T t, int precision) {
        if (precision < 0 && (t instanceof Float || t instanceof Double)) {
            throw new IllegalArgumentException("precision multiplier must be non-negative.");
        }
    }

    private static int compare(float x, float y, int precision) {
        return abs(x - y) <= precision * (ulp(x) + ulp(y)) ? 0 : Float.compare(x, y);
    }

    private static int compare(double x, double y, int precision) {
        return abs(x - y) <= precision * (ulp(x) + ulp(y)) ? 0 : Double.compare(x, y);
    }

    private static <T extends Comparable<T>> float toFloat(T value) {
        return (Float) value;
    }

    private static <T extends Comparable<T>>  double toDouble(T value) {
        return (Double) value;
    }



}
