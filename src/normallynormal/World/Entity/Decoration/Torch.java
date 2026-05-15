package normallynormal.World.Entity.Decoration;

import normallynormal.GameManager;
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
        return 20 + 0.5 * Math.sin(GameManager.gameTime() / 50.0);
    }

    private record State(double posX, double posY, int depth, boolean onScreen) implements RenderState {
        @Override
        public void render(DepthScreen screen, int xOffset, int yOffset) {
            boolean leftFlame = GameManager.gameTime() % 200 < 99;
            screen.setCharacterWithDepth((int) posX, (int) posY, xOffset, yOffset, depth, new TextCharacter('▽', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            if (leftFlame) {
                screen.setCharacterWithDepth((int) posX, (int) posY - 1, xOffset, yOffset, depth, new TextCharacter('◣', TextColor.ANSI.YELLOW, TransparentColor.TRANSPARENT));
            }
            else {
                screen.setCharacterWithDepth((int) posX, (int) posY - 1, xOffset, yOffset, depth, new TextCharacter('◢', TextColor.ANSI.YELLOW, TransparentColor.TRANSPARENT));
            }
        }
    }

    @Override
    public void copyForRender() {
        renderState = new State(position.x, position.y, depth, isOnScreen());
    }
}
