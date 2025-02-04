package Math;

public class Mth {
    public static int mod(int a, int b) {
        return ((a % b) + b) % b;
    }

    public static double mod(double a, double b) {
        return ((a % b) + b) % b;
    }

    public static double screenDistance(double x, double y) {
        return Math.sqrt(x * x + 4 * y * y);
    }

    public static double towardsZeroBy(double a, double b) {
        if (a > 0) {
            return Math.max(0, a - b);
        } else if (a < 0) {
            return Math.min(0, a - b);
        }
        return 0;
    }
}
