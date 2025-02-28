package normallynormal.World.Platform;
import normallynormal.Game;
import normallynormal.Render.Renderers.AbstractRenderer;
import normallynormal.Render.Renderers.DefaultRenderer;
import normallynormal.World.*;
import normallynormal.World.Entity.Entity;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;

public abstract class WorldObject implements CollisionObject, Identifiable {
    protected AABB collisionBox;
    protected AABB visibilityBox;
    boolean solid = true;
    int id;
    boolean semiSolid = false;
    AbstractRenderer renderer = new DefaultRenderer(this::getCollisionBox);

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
        return visibilityBox.overlaps(Game.screenBoundingBox);
    }

    public AbstractRenderer getRenderer() {
        return renderer;
    }

    public void setRenderer(AbstractRenderer renderer) {
        this.renderer = renderer;
    }
}
