package Math;

public class AABB {
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

    @Override
    public String toString() {
        return "AABB{" +
                "x=" + x +
                ", y=" + y +
                ", w=" + w +
                ", h=" + h +
                '}';
    }
}