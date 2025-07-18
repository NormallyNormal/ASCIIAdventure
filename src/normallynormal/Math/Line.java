package normallynormal.Math;

import java.util.ArrayList;
import java.util.List;

public class Line {
    public static List<int[]> drawLine(int x0, int y0, int x1, int y1) {
        List<int[]> points = new ArrayList<>();

        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);

        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;

        int err = dx - dy;

        while (true) {
            points.add(new int[]{x0, y0});

            if (x0 == x1 && y0 == y1) break;

            int e2 = 2 * err;

            if (e2 > -dy) {
                err -= dy;
                x0 += sx;
            }

            if (e2 < dx) {
                err += dx;
                y0 += sy;
            }
        }

        return points;
    }
}
