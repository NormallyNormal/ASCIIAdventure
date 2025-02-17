package World.Entity.Particle;

import Input.Input;
import Math.Direction;
import Math.Vector2;
import Render.DepthScreen;
import Render.TransparentColor;
import World.Entity.Entity;
import World.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public class SlamParticle extends Entity {
    double fadeTime = 0.15;
    Direction direction;
    boolean ground;
    public SlamParticle(Vector2 position, Direction direction, boolean ground) {
        this.position = position;
        this.depth = -1;
        this.direction = direction;
        this.ground = ground;
        if (ground) {
            fadeTime = 0.25;
        }
    }

    @Override
    public void process(double timeDelta, Input input) {
        fadeTime -= timeDelta;
        if (fadeTime <= 0) {
            Game.currentLevel.removeEntity(this);
        }
        if (ground) {
            if (direction == Direction.LEFT) {
                position.x -= 20 * timeDelta;
            } else {
                position.x += 20 * timeDelta;
            }
        }
        else {
            if (direction == Direction.LEFT) {
                position.x -= 35 * timeDelta;
            } else {
                position.x += 35 * timeDelta;
            }
        }
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        TextColor renderColor;
        if (ground) {
            renderColor = fadeTime < 0.10 ? TextColor.ANSI.BLACK_BRIGHT : TextColor.ANSI.WHITE;

        }
        else {
            renderColor = fadeTime < 0.07 ? TextColor.ANSI.RED : fadeTime < 0.12 ? TextColor.ANSI.RED_BRIGHT : TextColor.ANSI.WHITE;
        }
        char character;
        if (direction == Direction.LEFT) {
            character = ground ? '◣' : '\\';
        }
        else {
            character = ground ? '◢' : '/';
        }
        screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter(character, renderColor, TransparentColor.TRANSPARENT));
    }
}
