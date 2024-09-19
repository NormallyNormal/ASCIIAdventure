package World.Platform;
import World.CollisionObject;
import World.Entity.Entity;
import World.Level;
import World.RenderObject;
import World.Identifiable;
import Math.AABB;

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

    public int getId() {
        return id;
    }

    public boolean isSemiSolid() {
        return semiSolid;
    }
}
