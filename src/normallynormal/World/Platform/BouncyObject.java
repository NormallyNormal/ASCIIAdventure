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
    private static final TextCharacter[] fades = {new TextCharacter('█', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter('▓', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter('▒', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter('░', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter(' ', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA)};

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

    public void collisionEffect(Entity entity, Level level) {
        super.collisionEffect(entity, level);
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        for (int i = 0; i <= collisionBox.w - 1; i++) {
            for (int j = 0; j <= collisionBox.h - 1; j++) {
                long delta = System.currentTimeMillis() - bounceTime;
                int index = Math.min((int)(delta/100), fades.length - 1);
                screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, fades[index]);
            }
        }
    }
}
