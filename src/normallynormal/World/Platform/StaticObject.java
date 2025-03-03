package normallynormal.World.Platform;

import normallynormal.Render.DepthScreen;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;

public class StaticObject extends WorldObject {
    public StaticObject(AABB collisionBox, int id) {
        this.id = id;
        this.collisionBox = collisionBox;
        this.visibilityBox = collisionBox;
    }
}
