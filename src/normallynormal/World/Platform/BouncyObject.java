package normallynormal.World.Platform;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Render.DepthScreen;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;

public class BouncyObject extends StaticObject {
    private long bounceTime = System.currentTimeMillis() - 1000;

    public BouncyObject(AABB collisionBox, int id) {
        super(collisionBox, id);
    }

    public void intersectEffect(Entity entity, Level level, Direction direction) {
        if (direction.isVertical()) {
            entity.bounce();
            bounceTime = System.currentTimeMillis();
        }
        super.intersectEffect(entity, level);
    }

    public long lastBounceTime() {
        return bounceTime;
    }
}
