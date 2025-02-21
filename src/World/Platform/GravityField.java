package World.Platform;

import Render.DepthScreen;
import Render.TransparentColor;
import World.Entity.Entity;
import World.Level;
import Math.AABB;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import Math.Noise;

public class GravityField extends WorldObject {

    boolean up;
    char arrow;
    TextCharacter arrowText;
    TextCharacter trailText1;
    TextCharacter trailText2;
    int rate = 70;

    public GravityField(AABB collisionBox, int id, boolean up) {
        this.id = id;
        this.collisionBox = collisionBox;
        this.up = up;
        this.solid = false;
        arrow = up ? '▲' : '▼';
        TextColor color = up ? TextColor.ANSI.YELLOW : TextColor.ANSI.BLUE;
        arrowText = new TextCharacter(arrow, color, TransparentColor.TRANSPARENT);
        trailText1 = new TextCharacter('╏', color, TransparentColor.TRANSPARENT);
        trailText2 = new TextCharacter('╎', color, TransparentColor.TRANSPARENT);
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        if (up) {
            for (int i = 0; i <= collisionBox.w - 1; i++) {
                for (int j = -2; j <= collisionBox.h - 1; j++) {
                    if (Noise.xyNoise(i, j + ((int)System.currentTimeMillis()/rate)) < 0.2) {
                        if (j <= collisionBox.h - 1 && j >= 0)
                            screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, -20, arrowText);
                        if (j - 1 <= collisionBox.h - 1 && j >= -1)
                            screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j + 1, xOffset, yOffset, -20, trailText1);
                        if (j - 2 <= collisionBox.h - 1)
                            screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j + 2, xOffset, yOffset, -20, trailText2);
                    }
                }
            }
        }
        else {
            for (int i = 0; i <= collisionBox.w - 1; i++) {
                for (int j = 0; j <= collisionBox.h - 1 + 2; j++) {
                    if (Noise.xyNoise(i, j - ((int)System.currentTimeMillis()/rate)) < 0.2) {
                        if (j >= 0 && j <= collisionBox.h - 1)
                            screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, -20, arrowText);
                        if (j >= 1 && j <= collisionBox.h)
                            screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j - 1, xOffset, yOffset, -20, trailText1);
                        if (j >= 2)
                            screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j - 2, xOffset, yOffset, -20, trailText2);
                    }
                }
            }
        }
    }

    public void collisionEffect(Entity entity, Level level) {
        if (up && entity.isGravityDownward()) {
            entity.setGravityUpward();
        }
        if (!up && !entity.isGravityDownward()) {
            entity.setGravityDownward();
        }
    }
}
