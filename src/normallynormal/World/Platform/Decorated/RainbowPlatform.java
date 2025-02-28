package normallynormal.World.Platform.Decorated;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Platform.StaticObject;

import java.util.ArrayList;
import java.util.List;

public class RainbowPlatform extends StaticObject {

    private static final char[] ditherChars = {'█', '▓', '▒', '░'};
    private static final TextColor[] rainbow = {TextColor.ANSI.RED, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.GREEN, TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.MAGENTA};
    private static final List<TextCharacter> rainbowChars = new ArrayList<>();
    private static final TextCharacter white = new TextCharacter('█', TextColor.ANSI.WHITE, TextColor.ANSI.WHITE);

    public RainbowPlatform(AABB collisionBox, int id) {
        super(collisionBox, id);
        if (rainbowChars.isEmpty()) {
            constructRainbowChars();
        }
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        for (int i = 0; i <= collisionBox.w - 1; i++) {
            for (int j = 0; j <= collisionBox.h - 1; j++) {
                screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, rainbowChars.get(((int) collisionBox.x + i + ((int)System.currentTimeMillis() / 100)) % rainbowChars.size()));
            }
        }
    }

    private static void constructRainbowChars() {
        for (int i = 0; i < rainbow.length; i++) {
            for (int j = 0; j < ditherChars.length; j++) {
                rainbowChars.add(new TextCharacter(ditherChars[j], rainbow[i], rainbow[(i + 1) % rainbow.length]));
            }
        }
    }
}
