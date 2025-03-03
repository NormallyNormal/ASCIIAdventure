package normallynormal.World.Platform;

import normallynormal.Render.DepthScreen;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.World.CollisionObject;

public class StaticObject extends WorldObject {
    public StaticObject(AABB collisionBox, int id) {
        super(collisionBox, id);
    }
}
