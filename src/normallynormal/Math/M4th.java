package normallynormal.Math;

public class M4th {
    public static int mod(int a, int b) {
        return ((a % b) + b) % b;
    }

    public static double mod(double a, double b) {
        return ((a % b) + b) % b;
    }

    public static double screenDistance(double x, double y) {
        return Math.sqrt(screenDistanceSquared(x, y));
    }

    public static double screenDistanceSquared(double x, double y) {
        return x * x + 4 * y * y;
    }

    public static double screenDistanceSquared(Vector2 a, Vector2 b) {
        return (a.x - b.x) * (a.x - b.x) + 4 * (a.y - b.y) * (a.y - b.y);
    }

    public static double towardsZeroBy(double a, double b) {
        if (a > 0) {
            return Math.max(0, a - b);
        } else if (a < 0) {
            return Math.min(0, a - b);
        }
        return 0;
    }

    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public static double capMagnitude(double a, double b) {
        b = Math.abs(b);
        if (a < 0) {
            return Math.max(a, -b);
        }
        else {
            return Math.min(a, b);
        }
    }
}
