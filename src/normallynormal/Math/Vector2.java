package normallynormal.Math;

import normallynormal.Util.DeepCopyable;

public class Vector2 implements DeepCopyable<Vector2> {
    public double x, y;
    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static Vector2 add(Vector2 vec1, Vector2 vec2) {
        return new Vector2(vec1.x + vec2.x, vec1.y + vec2.y);
    }

    public void add(Vector2 vec) {
        x += vec.x;
        y += vec.y;
    }

    public static Vector2 subtract(Vector2 vec1, Vector2 vec2) {
        return new Vector2(vec1.x - vec2.x, vec1.y - vec2.y);
    }

    public void subtract(Vector2 vec) {
        x -= vec.x;
        y -= vec.y;
    }

    public static Vector2 multiply(Vector2 vec, double scalar) {
        return new Vector2(vec.x * scalar, vec.y * scalar);
    }

    public void multiply(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
    }

    public static double dot(Vector2 vec1, Vector2 vec2) {
        return vec1.x * vec2.x + vec1.y * vec2.y;
    }

    public static Vector2 hadamard(Vector2 vec1, Vector2 vec2) {
        return new Vector2(vec1.x * vec2.x, vec1.y * vec2.y);
    }

    public void hadamard(Vector2 vec) {
        this.x *= vec.x;
        this.y *= vec.y;
    }

    public static Vector2 divide(Vector2 vec, double scalar) {
        return new Vector2(vec.x / scalar, vec.y / scalar);
    }

    public void divide(double scalar) {
        this.x /= scalar;
        this.y /= scalar;
    }

    public void moveTowardsZeroBy(Vector2 vector2) {
        this.x = M4th.towardsZeroBy(this.x, vector2.x);
        this.y = M4th.towardsZeroBy(this.y, vector2.y);
    }

    public void zero() {
        this.x = 0;
        this.y = 0;
    }

    public double magnitude() {
        return Math.sqrt(x * x + y * y);
    }

    public double magnitudeSquared() {
        return x * x + y * y;
    }

    public static Vector2 normalize(Vector2 vec) {
        double magnitude = vec.magnitude();
        return divide(vec, magnitude);
    }

    public void normalize() {
        double magnitude = magnitude();
        divide(magnitude);
    }

    public boolean isZero() {
        return x == 0 && y == 0;
    }

    @Override
    public String toString() {
        return "Vector2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public Vector2 deepCopy() {
        return new Vector2(x, y);
    }

    @Override
    public Vector2 deepCopy(Vector2 vector2) {
        vector2.x = x;
        vector2.y = y;
        return vector2;
    }
}
