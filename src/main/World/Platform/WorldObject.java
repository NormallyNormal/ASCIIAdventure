package main.World.Platform;
import main.World.CollisionObject;
import main.World.Entity.Entity;
import main.World.Level;
import main.World.RenderObject;
import main.World.Identifiable;
import main.Math.AABB;

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
