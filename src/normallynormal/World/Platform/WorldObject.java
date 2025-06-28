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
    AbstractRenderer renderer = new DefaultRenderer(this::getCollisionBox);

    public WorldObject(AABB collisionBox, int id) {
        this.id = id;
        this.collisionBox = collisionBox;
        this.visibilityBox = new AABB(0, 0, 0, 0);
    }

    public AABB getCollisionBox() {
        return collisionBox;
    }

    public AABB getVisibilityBox() {
        return visibilityBox;
    }

    public void process(double timeDeltaSeconds, Level level) {
        updateVisibilityBox();
    }

    private void updateVisibilityBox() {
        this.visibilityBox.x = Math.floor(collisionBox.x);
        this.visibilityBox.y = Math.floor(collisionBox.y);
        this.visibilityBox.w = Math.ceil(collisionBox.w);
        this.visibilityBox.h = Math.ceil(collisionBox.h);
    }

    public boolean isSolid() {
        return solid;
    }

    public boolean isSolid(Entity entity) {
        return solid;
    }

    public void collisionEffect(Entity entity, Level level, Direction direction) {

    }

    public void intersectEffect(Entity entity, Level level, Direction direction) {
        
    }

    public int getId() {
        return id;
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

    public void copyForRender() {
        renderer.copyForRender();
    };
}
