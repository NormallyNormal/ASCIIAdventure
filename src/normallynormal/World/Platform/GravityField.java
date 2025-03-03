package normallynormal.World.Platform;

import normallynormal.Render.Renderers.GravityFieldRenderer;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.Math.AABB;

public class GravityField extends WorldObject {

    boolean up;

    public GravityField(AABB collisionBox, int id, boolean up) {
        this.id = id;
        this.collisionBox = collisionBox;
        this.visibilityBox = collisionBox;
        this.up = up;
        this.solid = false;
        this.setRenderer(new GravityFieldRenderer(this::getVisibilityBox, this::getUp));
    }

    public void intersectEffect(Entity entity, Level level) {
        if (up && entity.isGravityDownward()) {
            entity.setGravityUpward();
        }
        if (!up && !entity.isGravityDownward()) {
            entity.setGravityDownward();
        }
    }

    public boolean getUp() {
        return up;
    }
}
