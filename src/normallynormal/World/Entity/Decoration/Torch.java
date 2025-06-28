package normallynormal.World.Entity.Decoration;

import normallynormal.Game;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Entity.GlowingEntity;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.Vector2;

public class Torch extends Entity implements GlowingEntity {

    public Torch (int x, int y) {
        this.position = new Vector2(x, y);
        this.depth = 0;
    }

    @Override
    public double glowRadius() {
        return 20 + 0.5 * Math.sin(Game.gameTime() / 50.0);
    }

    Vector2 render_position = position.deepCopy();
    int render_depth = depth;
    @Override
    public void copyForRender() {
        render_position = position.deepCopy(render_position);
        render_depth = depth;
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        boolean leftFlame = Game.gameTime() % 200 < 99;
        screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y, xOffset, yOffset, render_depth, new TextCharacter('▽', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        if (leftFlame) {
            screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y - 1, xOffset, yOffset, render_depth, new TextCharacter('◣', TextColor.ANSI.YELLOW, TransparentColor.TRANSPARENT));
        }
        else {
            screen.setCharacterWithDepth((int) render_position.x, (int) render_position.y - 1, xOffset, yOffset, render_depth, new TextCharacter('◢', TextColor.ANSI.YELLOW, TransparentColor.TRANSPARENT));
        }
    }
}
