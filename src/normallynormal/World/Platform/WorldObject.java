package normallynormal.World.Platform;
import normallynormal.Game;
import normallynormal.World.*;
import normallynormal.World.Entity.Entity;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;

public abstract class WorldObject implements CollisionObject, RenderObject, Identifiable {
    protected AABB collisionBox;
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
