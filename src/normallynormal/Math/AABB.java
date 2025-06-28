package normallynormal.Math;

import normallynormal.Util.DeepCopyable;

public class AABB implements DeepCopyable<AABB> {
    public double x,y,w,h;
    public AABB(double x,double y, double w, double h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    private AABB() {
        this.x = 0;
        this.y = 0;
        this.w = 0;
        this.h = 0;
    }

    private AABB(AABB aabb) {
        this.x = aabb.x;
        this.y = aabb.y;
        this.w = aabb.w;
        this.h = aabb.h;
    }

    public boolean overlaps(AABB aabb){
        if (this.x > aabb.x + aabb.w)
            return false;
        if (this.x + this.w < aabb.x)
            return false;
        if (this.y > aabb.y + aabb.h)
            return false;
        if (this.y + this.h < aabb.y)
            return false;
        return true;
    }

    public boolean contains(double px, double py) {
        return px >= x && px <= x + w && py >= y && py <= y + h;
    }

    public boolean contains(Vector2 vector) {
        return contains(vector.x, vector.y);
    }

    public double overlapArea(AABB other) {
        double xOverlap = Math.max(0, Math.min(this.x + this.w, other.x + other.w) - Math.max(this.x, other.x));
        double yOverlap = Math.max(0, Math.min(this.y + this.h, other.y + other.h) - Math.max(this.y, other.y));
        return xOverlap * yOverlap;
    }

    public double area() {
        return this.w * this.h;
    }

    public AABB createBBB(Vector2 velocity) {
        AABB bbb = new AABB();
        if (velocity.x > 0)
            bbb.x = this.x;
        else
            bbb.x = this.x + velocity.x;
        if (velocity.y > 0)
            bbb.y = this.y;
        else
            bbb.y = this.y + velocity.y;
        bbb.w = this.w + Math.abs(velocity.x);
        bbb.h = this.h + Math.abs(velocity.y);
        return bbb;
    }

    public void expandToContain(AABB other) {
        double minX = Math.min(this.x, other.x);
        double minY = Math.min(this.y, other.y);
        double maxX = Math.max(this.x + this.w, other.x + other.w);
        double maxY = Math.max(this.y + this.h, other.y + other.h);

        this.x = minX;
        this.y = minY;
        this.w = maxX - minX;
        this.h = maxY - minY;
    }

    public Direction getNearestSide(double px, double py) {
        double distLeft = Math.abs(px - x);
        double distRight = Math.abs(px - (x + w));
        double distUp = Math.abs(py - y);       // UP is negative Y
        double distDown = Math.abs(py - (y + h)); // DOWN is positive Y

        // Find the minimum distance
        double minDist = Math.min(Math.min(distLeft, distRight), Math.min(distUp, distDown));

        // Return the corresponding direction
        if (minDist == distLeft) {
            return Direction.LEFT;
        } else if (minDist == distRight) {
            return Direction.RIGHT;
        } else if (minDist == distUp) {
            return Direction.UP;
        } else {
            return Direction.DOWN;
        }
    }

    public Direction getNearestSide(Vector2 vector) {
        return getNearestSide(vector.x, vector.y);
    }

    public Vector2 center() {
        return new Vector2(x + w/2, y + h/2);
    }

    @Override
    public String toString() {
        return "AABB{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }

    @Override
    public AABB deepCopy() {
        return new AABB(x, y, w, h);
    }

    @Override
    public AABB deepCopy(AABB aabb) {
        aabb.x = x;
        aabb.y = y;
        aabb.w = w;
        aabb.h = h;
        return aabb;
    }
}