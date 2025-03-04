package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;

import java.util.function.Supplier;

public class DefaultRenderer extends AbstractRenderer {

    public DefaultRenderer(Supplier<AABB> visibilityBoxSupplier) {
        super(visibilityBoxSupplier);
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = visibilityBoxSupplier.get();
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
