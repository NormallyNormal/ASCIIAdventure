package normallynormal.World.Platform;

import normallynormal.Game;
import normallynormal.GameManager;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Render.Renderers.BouncePadRenderer;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;

public class BouncyObject extends MoveableObject {
    private long bounceTime = GameManager.gameTime() - 1000;

    public BouncyObject(AABB collisionBox, int id) {
        super(collisionBox, id);
        this.setRenderer(new BouncePadRenderer(this::getVisibilityBox, this::lastBounceTime));
    }

    @Override
    public void intersectEffect(Entity entity, Level level, Direction direction) {
        if (direction.isVertical()) {
            entity.bounce();
            bounceTime = GameManager.gameTime();
        }
        super.intersectEffect(entity, level, direction);
    }

    public long lastBounceTime() {
        return bounceTime;
    }
}
