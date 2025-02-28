package normallynormal.World.Platform;

import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.Render.DepthScreen;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;

public class StaticHazardObject extends StaticObject {

    public StaticHazardObject(AABB collisionBox, int id) {
        super(new AABB(collisionBox.x + 0.2, collisionBox.y + 0.6, collisionBox.w - 0.4, collisionBox.h - 0.6), id);
        this.solid = false;
    }

    @Override
    public void intersectEffect(Entity entity, Level level) {
        entity.kill();
        super.intersectEffect(entity, level);
    }
}
