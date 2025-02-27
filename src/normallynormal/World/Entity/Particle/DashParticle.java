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

    TextColor[] wokeColors = {TextColor.ANSI.CYAN_BRIGHT, TextColor.ANSI.MAGENTA_BRIGHT, TextColor.ANSI.WHITE};
    TextColor[] prideColors = {TextColor.ANSI.RED, TextColor.ANSI.RED_BRIGHT, TextColor.ANSI.YELLOW_BRIGHT, TextColor.ANSI.GREEN, TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.MAGENTA};
    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        boolean upperHalf = position.y % 1 > 0.75;
        boolean half_y = upperHalf || position.y % 1 < 0.25;
        TextColor renderColor;
        if (!Other.wokeMode) {
            renderColor = fadeTime < 0.09 ? TextColor.ANSI.BLUE : TextColor.ANSI.BLUE_BRIGHT;
        }
        else {
            renderColor = prideColors[(int)(position.x/2) % 6];
        }
        if (half_y) {
            screen.setCharacterWithDepth((int) position.x, upperHalf ? (int) position.y : (int) position.y - 1, xOffset, yOffset, depth, new TextCharacter('ˍ', renderColor, TransparentColor.TRANSPARENT));
        } else {
            screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('-', renderColor, TransparentColor.TRANSPARENT));
        }
    }
}