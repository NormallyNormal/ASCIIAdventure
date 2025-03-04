package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;

import java.util.function.Supplier;

public class SemisolidRenderer extends AbstractRenderer {

    private static final TextCharacter dash = new TextCharacter('¯', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);

    public SemisolidRenderer(Supplier<AABB> visibilityBoxSupplier) {
        super(visibilityBoxSupplier);
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = visibilityBoxSupplier.get();
        for (int i = 0; i <= collisionBox.w - 1; i++) {
            screen.setCharacterWithDepth((int)collisionBox.x + i, (int)collisionBox.y, xOffset, yOffset, 0, dash);
        }
    }
}
