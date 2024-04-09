package xyz.iamthedefender.cosmetics.util;

import java.util.Random;

public class MathUtil {

    public static int getRandom(int min, int max) {
        return (new Random()).nextInt(max - min + 1) + min;
    }

    public static double getNegative(int min, int max) {
        return (getRandom(min, max) * ((new Random()).nextBoolean() ? -1 : 1));
    }

    public static double getRandom(double min, double max) {
        return (new Random()).nextDouble() * (max - min);
    }

    public static Double getNegative(Double min, Double max) {
        return getNegative(min, max);
    }
}
