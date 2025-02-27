package normallynormal.World.Platform;

import normallynormal.Render.DepthScreen;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;

public class StaticObject extends WorldObject {
    public StaticObject(AABB collisionBox, int id) {
        this.id = id;
        this.collisionBox = collisionBox;
    }

    public StaticObject(AABB collisionBox, int id, boolean semiSolid) {
        this(collisionBox, id);
        this.semiSolid = semiSolid;
    }

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
                screen.setCharacterWithDepth((int)collisionBox.x + i, (int)collisionBox.y + j, xOffset, yOffset, 0, new TextCharacter(filler, TextColor.ANSI.WHITE, TextColor.ANSI.BLACK_BRIGHT));
            }
        }
    }
}
