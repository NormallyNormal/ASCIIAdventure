package main.World.Platform;
import main.World.*;
import main.World.Entity.Entity;
import main.Math.AABB;
import main.Math.Direction;

public abstract class WorldObject implements CollisionObject, RenderObject, Identifiable {
    AABB collisionBox;
    boolean solid = true;
    int id;
    boolean semiSolid = false;
    public AABB getCollisionBox() {
        return collisionBox;
    }

    public void process(double timeDeltaSeconds, Level level) {

    }

    public boolean isSolid() {
        return solid;
    }

    public void collisionEffect(Entity entity, Level level) {

    }

    public void intersectEffect(Entity entity, Level level) {

    }

    public void intersectEffect(Entity entity, Level level, Direction direction) {
        intersectEffect(entity, level);
    }

    public int getId() {
        return id;
    }

    public boolean isSemiSolid() {
        return semiSolid;
    }

    public boolean isOnScreen() {
        return collisionBox.overlaps(Game.screenBoundingBox);
    }
}
