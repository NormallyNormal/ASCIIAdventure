package normallynormal.World.Entity.Particle;

import normallynormal.Input.Input;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.Settings.Other;
import normallynormal.World.Entity.Entity;
import normallynormal.Game;
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
    public void process(double timeDelta, Input input) {
        fadeTime -= timeDelta;
        if (fadeTime <= 0) {
            Game.currentLevel.removeEntity(this);
        }
    }

    private Vector2 render_position = position.deepCopy();
    private double render_fadeTime;
    private int render_depth;
    public void copyForRender() {
        render_position = position.deepCopy(render_position);
        render_fadeTime = fadeTime;
        render_depth = depth;
    }


    TextColor[] wokeColors = {TextColor.ANSI.CYAN_BRIGHT, TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.WHITE};
    TextColor[] prideColors = {TextColor.ANSI.RED, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.GREEN, TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.MAGENTA};
    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        boolean upperHalf = render_position.y % 1 > 0.75;
        boolean half_y = upperHalf || render_position.y % 1 < 0.25;

        TextColor renderColor;
        if (!Other.WOKE_MODE) {
            renderColor = render_fadeTime < 0.09
                    ? TextColor.ANSI.BLUE
                    : TextColor.ANSI.BLUE_BRIGHT;
        } else {
            renderColor = prideColors[(int)(render_position.x / 2) % 6];
        }

        int drawY = upperHalf
                ? (int) render_position.y
                : (int) render_position.y - 1;

        if (half_y) {
            screen.setCharacterWithDepth(
                    (int) render_position.x,
                    drawY,
                    xOffset,
                    yOffset,
                    render_depth,
                    new TextCharacter('ˍ', renderColor, TransparentColor.TRANSPARENT)
            );
        } else {
            screen.setCharacterWithDepth(
                    (int) render_position.x,
                    (int) render_position.y,
                    xOffset,
                    yOffset,
                    render_depth,
                    new TextCharacter('-', renderColor, TransparentColor.TRANSPARENT)
            );
        }
    }
}