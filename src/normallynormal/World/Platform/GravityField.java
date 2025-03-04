package normallynormal.World.Platform;

import normallynormal.Math.Direction;
import normallynormal.Render.Renderers.GravityFieldRenderer;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.Math.AABB;

public class GravityField extends MoveableObject {

    boolean up;

    public GravityField(AABB collisionBox, int id, boolean up) {
        super(collisionBox, id);
        this.up = up;
        this.solid = false;
        this.setRenderer(new GravityFieldRenderer(this::getVisibilityBox, this::getUp));
    }

    @Override
    public void intersectEffect(Entity entity, Level level, Direction direction) {
        if (up && entity.isGravityDownward()) {
            entity.setGravityUpward();
        }
        if (!up && !entity.isGravityDownward()) {
            entity.setGravityDownward();
        }
        super.intersectEffect(entity, level, direction);
    }

    public boolean getUp() {
        return up;
    }
}
