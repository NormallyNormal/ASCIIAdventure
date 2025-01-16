package World.Entity;

import Render.DepthScreen;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import Math.Vector2;

public class Torch extends Entity implements GlowingEntity {

    public Torch (int x, int y) {
        this.position = new Vector2(x, y);
    }

    @Override
    public int glowRadius() {
        return 20;
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        boolean leftFlame = System.currentTimeMillis() % 200 < 99;
        screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▽', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        if (leftFlame) {
            screen.setCharacterWithDepth((int) position.x, (int) position.y - 1, xOffset, yOffset, depth, new TextCharacter('◣', TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK));
        }
        else {
            screen.setCharacterWithDepth((int) position.x, (int) position.y - 1, xOffset, yOffset, depth, new TextCharacter('◢', TextColor.ANSI.YELLOW, TextColor.ANSI.BLACK));
        }
    }
}
