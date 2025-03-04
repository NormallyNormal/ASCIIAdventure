package normallynormal.World.Platform;

import normallynormal.Math.Direction;
import normallynormal.Render.Renderers.SpikesRenderer;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.Math.AABB;

public class StaticHazardObject extends StaticObject {

    public StaticHazardObject(AABB collisionBox, int id) {
        super(new AABB(collisionBox.x + 0.2, collisionBox.y + 0.6, collisionBox.w - 0.4, collisionBox.h - 0.6), id);
        this.solid = false;
        this.setRenderer(new SpikesRenderer(this::getVisibilityBox));
    }

    @Override
    public void intersectEffect(Entity entity, Level level, Direction direction) {
        entity.kill();
        super.intersectEffect(entity, level, direction);
    }
}
