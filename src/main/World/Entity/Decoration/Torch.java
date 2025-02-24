package main.World.Entity.Decoration;

import main.Render.DepthScreen;
import main.Render.TransparentColor;
import main.World.Entity.Entity;
import main.World.Entity.GlowingEntity;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import main.Math.Vector2;

public class Torch extends Entity implements GlowingEntity {

    public Torch (int x, int y) {
        this.position = new Vector2(x, y);
        this.depth = 0;
    }

    @Override
    public double glowRadius() {
        return 20 + 0.5 * Math.sin(System.currentTimeMillis()/50.0);
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        boolean leftFlame = System.currentTimeMillis() % 200 < 99;
        screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▽', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        if (leftFlame) {
            screen.setCharacterWithDepth((int) position.x, (int) position.y - 1, xOffset, yOffset, depth, new TextCharacter('◣', TextColor.ANSI.YELLOW, TransparentColor.TRANSPARENT));
        }
        else {
            screen.setCharacterWithDepth((int) position.x, (int) position.y - 1, xOffset, yOffset, depth, new TextCharacter('◢', TextColor.ANSI.YELLOW, TransparentColor.TRANSPARENT));
        }
    }
}
