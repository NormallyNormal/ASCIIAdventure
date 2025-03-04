package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;

import java.util.function.Supplier;

public class SpikesRenderer extends AbstractRenderer {

    private static final TextCharacter spike = new TextCharacter('◮', TextColor.ANSI.RED, TransparentColor.TRANSPARENT);

    public SpikesRenderer(Supplier<AABB> visibilityBoxSupplier) {
        super(visibilityBoxSupplier);
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = visibilityBoxSupplier.get();
        for (int i = 0; i < collisionBox.w; i++) {
            for (int j = 0; j < collisionBox.h; j++) {
                screen.setCharacterWithDepth((int)collisionBox.x + i, (int)collisionBox.y + j, xOffset, yOffset, 0, spike);
            }
        }
    }
}
