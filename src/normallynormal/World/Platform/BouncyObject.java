package normallynormal.World.Platform;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Render.DepthScreen;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;

public class BouncyObject extends StaticObject {
    public BouncyObject(AABB collisionBox, int id) {
        super(collisionBox, id);
    }

    public void intersectEffect(Entity entity, Level level, Direction direction) {
        if (direction.isVertical()) {
            entity.bounce();
        }
        super.intersectEffect(entity, level);
    }

    public void collisionEffect(Entity entity, Level level) {
        super.collisionEffect(entity, level);
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        if(semiSolid) {
            for (int i = 0; i <= collisionBox.w - 1; i++) {
                screen.setCharacterWithDepth((int)collisionBox.x + i, (int)collisionBox.y, xOffset, yOffset, 0, new TextCharacter('¯', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            }
            return;
        }
        for (int i = 0; i <= collisionBox.w - 1; i++) {
            for (int j = 0; j <= collisionBox.h - 1; j++) {
                char filler = ' ';
                if (i == 0 || i == (int)collisionBox.w - 1) {
                    filler = '║';
                    if (j == 0 || j == (int)collisionBox.h - 1) {
                        filler = '◇';
                    }
                }
                else if (j == 0 || j == (int)collisionBox.h - 1) {
                    filler = '═';
                }
                screen.setCharacterWithDepth((int)collisionBox.x + i, (int)collisionBox.y + j, xOffset, yOffset, 0, new TextCharacter(filler, TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA));
            }
        }
    }
}
