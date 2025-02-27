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

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        for (int i = 0; i < collisionBox.w; i++) {
            for (int j = 0; j < collisionBox.h; j++) {
                char filler = '◮';
                screen.setCharacterWithDepth((int)collisionBox.x + i, (int)collisionBox.y + j, xOffset, yOffset, 0, new TextCharacter(filler, TextColor.ANSI.RED, TextColor.ANSI.BLACK));
            }
        }
    }

    @Override
    public void intersectEffect(Entity entity, Level level) {
        entity.kill();
        super.collisionEffect(entity, level);
    }
}
