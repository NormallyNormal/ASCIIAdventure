package normallynormal.World.Platform;

import normallynormal.Math.AABB;
import normallynormal.Render.Renderers.SemisolidRenderer;
import normallynormal.World.Entity.Entity;

public class SemisolidPlatform extends MoveableObject {
    public SemisolidPlatform(AABB collisionBox, int id) {
        super(collisionBox, id);
        this.renderer = new SemisolidRenderer(this::getVisibilityBox);
    }

    @Override
    public boolean isSolid(Entity entity) {
        return solid && entity.getCollisionBox().y + entity.getCollisionBox().h <= this.getCollisionBox().y && entity.standsOnSemisolid();
    }
}
