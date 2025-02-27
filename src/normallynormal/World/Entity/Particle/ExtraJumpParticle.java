package normallynormal.World.Entity.Particle;

import normallynormal.Input.Input;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Entity.Entity;
import normallynormal.Math.Vector2;
import normallynormal.Math.Direction;
import normallynormal.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public class ExtraJumpParticle extends Entity {
    double fadeTime = 0.2;
    Direction direction;
    public ExtraJumpParticle(Vector2 position, Direction direction) {
        this.position = position;
        this.collisionBox = new AABB(position.x, position.y, 1, 1);
        this.depth = -1;
        this.direction = direction;
    }

    @Override
    public void process(double timeDelta, Input input) {
        fadeTime -= timeDelta;
        if (fadeTime <= 0) {
            Game.currentLevel.removeEntity(this);
        }
        if (direction == Direction.LEFT) {
            position.x -= 20 * timeDelta;
        }
        else {
            position.x += 20 * timeDelta;
        }
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        TextColor renderColor = fadeTime < 0.12 ? TextColor.ANSI.GREEN : TextColor.ANSI.GREEN_BRIGHT;
        if (direction == Direction.LEFT) {
            screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('[', renderColor, TransparentColor.TRANSPARENT));
        }
        else {
            screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter(']', renderColor, TransparentColor.TRANSPARENT));
        }
    }
}
