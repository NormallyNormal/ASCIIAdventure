package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Game;
import normallynormal.GameManager;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Util.BufferedPrimitiveSupplier;

import java.util.function.Supplier;

public class BouncePadRenderer extends AbstractRenderer {
    private final BufferedPrimitiveSupplier<Long> glowTimeSupplier;

    private static final TextCharacter[] fades = {new TextCharacter('█', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter('▓', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter('▒', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter('░', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA), new TextCharacter(' ', TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.MAGENTA)};

    public BouncePadRenderer(Supplier<AABB> visibilityBoxSupplier, Supplier<Long> glowTimeSupplier) {
        super(visibilityBoxSupplier);
        this.glowTimeSupplier = new BufferedPrimitiveSupplier<>(glowTimeSupplier);
    }

    public void copyForRender() {
        glowTimeSupplier.buffer();
        super.copyForRender();
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = visibilityBoxSupplier.get();
        long bounceTime = glowTimeSupplier.get();
        for (int i = 0; i <= collisionBox.w - 1; i++) {
            for (int j = 0; j <= collisionBox.h - 1; j++) {
                long delta = GameManager.gameTime() - bounceTime;
                int index = Math.min((int)(delta/100), fades.length - 1);
                screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, fades[index]);
            }
        }
    }
}
