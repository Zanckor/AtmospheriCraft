package dev.zanckor.atmosphericraft.common.util;

import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import org.apache.commons.lang3.Range;

public class Mathematic {

    public static boolean numberBetween(float number, float min, float max) {
        Range<Float> range = Range.between(min, max);

        if (range.contains(number)) return true;

        return false;
    }

    public static float randomFromInterval(float min, float max) {
        return (float) (Math.random() * (max - min + 1) + min);
    }

    public static boolean randomBoolean(float successPercent) {
        float random = Mth.randomBetween(RandomSource.create(), 0, 100);

        return random < successPercent;
    }


    public static float randomFloat(float min, float max) {
        float random = Mth.randomBetween(RandomSource.create(), min, max);

        return random;
    }

    public static boolean intBetween(int min, int max, int number) {
        return number > min && number < max;
    }

    public static boolean randomBoolean() {
        float randomNumber = (int) randomFromInterval(0, 1);

        if (randomNumber == 0) return true;

        return false;
    }


    public static double calculateDistanceBetweenPoints(double x1, double y1, double z1, double x2, double y2, double z2) {

        return Math.sqrt((z2 - z1) * (z2 - z1) + (y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
    }


    public static double xDistance(double x1, double x2) {

        return Math.sqrt((x2 - x1) * (x2 - x1));
    }

    public static double yDistance(double y1, double y2) {

        return Math.sqrt((y2 - y1) * (y2 - y1));
    }

    public static double zDistance(double z1, double z2) {

        return Math.sqrt((z2 - z1) * (z2 - z1));
    }
}
