package normallynormal.World.Platform;

import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.Math.AABB;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.Noise;

public class GravityField extends WorldObject {

    boolean up;

    public GravityField(AABB collisionBox, int id, boolean up) {
        this.id = id;
        this.collisionBox = collisionBox;
        this.visibilityBox = collisionBox;
        this.up = up;
        this.solid = false;
    }

    public void intersectEffect(Entity entity, Level level) {
        if (up && entity.isGravityDownward()) {
            entity.setGravityUpward();
        }
        if (!up && !entity.isGravityDownward()) {
            entity.setGravityDownward();
        }
    }
}
