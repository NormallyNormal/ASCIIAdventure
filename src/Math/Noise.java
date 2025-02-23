package Math;

public class Noise {
    public static float xyNoise(int x, int y) {
        int hash = xyHash(x, y);
        return (hash & 0x7FFFFFFF) / (float) Integer.MAX_VALUE;
    }

    private static int xyHash(int x, int y) {
        int h = x * 0x85ebca6b + y * 0xc2b2ae35;
        h ^= h >>> 16;
        h *= 0x85ebca6b;
        h ^= h >>> 13;
        h *= 0xc2b2ae35;
        h ^= h >>> 16;
        return h;
    }
}
