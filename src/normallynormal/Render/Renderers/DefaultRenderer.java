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

    protected static final TextCharacter VERTICAL_BORDER = new TextCharacter('║', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK_BRIGHT);
    protected static final TextCharacter HORIZONTAL_BORDER = new TextCharacter('═', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK_BRIGHT);
    protected static final TextCharacter CORNER = new TextCharacter('◇', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK_BRIGHT);
    protected static final TextCharacter EMPTY = new TextCharacter(' ', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK_BRIGHT);

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = visibilityBoxSupplier.get();
        for (int i = 0; i <= collisionBox.w - 1; i++) {
            for (int j = 0; j <= collisionBox.h - 1; j++) {
                TextCharacter filler = EMPTY;
                if (i == 0 || i == (int)collisionBox.w - 1) {
                    filler = VERTICAL_BORDER;
                    if (j == 0 || j == (int)collisionBox.h - 1) {
                        filler = CORNER;
                    }
                }
                else if (j == 0 || j == (int)collisionBox.h - 1) {
                    filler = HORIZONTAL_BORDER;
                }
                screen.setCharacterWithDepth((int)collisionBox.x + i, (int)collisionBox.y + j, xOffset, yOffset, 0, filler);
            }
        }
    }
}
