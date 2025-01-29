package Math;

public class Mth {
    public static int mod(int a, int b) {
        return ((a % b) + b) % b;
    }

    public static double mod(double a, double b) {
        return ((a % b) + b) % b;
    }

    public static double screenDistance(double x, double y) {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(2 * y, 2));
    }
}
