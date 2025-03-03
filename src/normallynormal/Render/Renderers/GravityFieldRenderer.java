package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Math.Noise;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;

import java.util.function.Supplier;

public class GravityFieldRenderer extends AbstractRenderer {
    private final Supplier<AABB> collisionBoxSupplier;
    private final Supplier<Boolean> upSupplier;

    private static final TextCharacter arrowTextUp = new TextCharacter('▲', TextColor.ANSI.YELLOW , TransparentColor.TRANSPARENT);
    private static final TextCharacter trailText1Up = new TextCharacter('╏', TextColor.ANSI.YELLOW , TransparentColor.TRANSPARENT);
    private static final TextCharacter trailText2Up = new TextCharacter('╎', TextColor.ANSI.YELLOW , TransparentColor.TRANSPARENT);

    private static final TextCharacter arrowTextDown = new TextCharacter('▼', TextColor.ANSI.BLUE , TransparentColor.TRANSPARENT);
    private static final TextCharacter trailText1Down = new TextCharacter('╏', TextColor.ANSI.BLUE , TransparentColor.TRANSPARENT);
    private static final TextCharacter trailText2Down = new TextCharacter('╎', TextColor.ANSI.BLUE , TransparentColor.TRANSPARENT);

    private static final int rate = 70;

    public GravityFieldRenderer(Supplier<AABB> collisionBoxSupplier, Supplier<Boolean> upSupplier) {
        this.collisionBoxSupplier = collisionBoxSupplier;
        this.upSupplier = upSupplier;
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = collisionBoxSupplier.get();
        TextCharacter arrowText;
        TextCharacter trailText1;
        TextCharacter trailText2;
        if (upSupplier.get()) {
            arrowText = arrowTextUp;
            trailText1 = trailText1Up;
            trailText2 = trailText2Up;
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
            arrowText = arrowTextDown;
            trailText1 = trailText1Down;
            trailText2 = trailText2Down;
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
}
