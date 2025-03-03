package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class RainbowRenderer extends AbstractRenderer {
    private final Supplier<AABB> collisionBoxSupplier;

    private static final char[] ditherChars = {'█', '▓', '▒', '░'};
    private static final TextColor[] rainbow = {TextColor.ANSI.RED, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.GREEN, TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.MAGENTA};
    private static final List<TextCharacter> rainbowChars = new ArrayList<>();
    private static final TextCharacter white = new TextCharacter('█', TextColor.ANSI.WHITE, TextColor.ANSI.WHITE);

    public RainbowRenderer(Supplier<AABB> collisionBoxSupplier) {
        this.collisionBoxSupplier = collisionBoxSupplier;
        if (rainbowChars.isEmpty()) {
            constructRainbowChars();
        }
    }

    private static void constructRainbowChars() {
        for (int i = 0; i < rainbow.length; i++) {
            for (char ditherChar : ditherChars) {
                rainbowChars.add(new TextCharacter(ditherChar, rainbow[i], rainbow[(i + 1) % rainbow.length]));
            }
        }
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = collisionBoxSupplier.get();
        for (int i = 0; i <= collisionBox.w - 1; i++) {
            for (int j = 0; j <= collisionBox.h - 1; j++) {
                screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, rainbowChars.get(((int) collisionBox.x + i + ((int)System.currentTimeMillis() / 100)) % rainbowChars.size()));
            }
        }
    }
}
