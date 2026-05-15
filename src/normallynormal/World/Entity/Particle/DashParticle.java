package normallynormal.World.Entity.Particle;

import normallynormal.GameManager;
import normallynormal.Input.InputHandler;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.Settings.Other;
import normallynormal.World.Entity.Entity;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.Vector2;

public class DashParticle extends Entity {
    double fadeTime;

    public DashParticle(Vector2 position) {
        this.position = position;
        this.collisionBox = new AABB(position.x, position.y, 1, 1);
        fadeTime = 0.2;
        this.depth = -1;
    }

    @Override
    public void process(double timeDelta, InputHandler input) {
        fadeTime -= timeDelta;
        if (fadeTime <= 0) {
            GameManager.currentLevel.removeEntity(this);
        }
    }

    private static final TextColor[] WOKE_COLORS = {TextColor.ANSI.CYAN_BRIGHT, TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.WHITE};
    private static final TextColor[] PRIDE_COLORS = {TextColor.ANSI.RED, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.GREEN, TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.MAGENTA};

    private record State(double posX, double posY, int depth, boolean onScreen, double fadeTime) implements RenderState {
        @Override
        public void render(DepthScreen screen, int xOffset, int yOffset) {
            boolean upperHalf = posY % 1 > 0.75;
            boolean half_y = upperHalf || posY % 1 < 0.25;

            TextColor renderColor;
            if (Other.WOKE_MODE == 0) {
                renderColor = fadeTime < 0.09
                        ? TextColor.ANSI.BLUE
                        : TextColor.ANSI.BLUE_BRIGHT;
            } else if (Other.WOKE_MODE == 2) {
                renderColor = WOKE_COLORS[(int) posX % 3];
            } else {
                renderColor = PRIDE_COLORS[(int) (posX / 2) % 6];
            }

            int drawY = upperHalf ? (int) posY : (int) posY - 1;

            char ch = half_y ? 'ˍ' : '-';
            int drawTargetY = half_y ? drawY : (int) posY;
            screen.setCharacterWithDepth(
                    (int) posX,
                    drawTargetY,
                    xOffset,
                    yOffset,
                    depth,
                    new TextCharacter(ch, renderColor, TransparentColor.TRANSPARENT)
            );
        }
    }

    @Override
    public void copyForRender() {
        renderState = new State(position.x, position.y, depth, isOnScreen(), fadeTime);
    }
}
