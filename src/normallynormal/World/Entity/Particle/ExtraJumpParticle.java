package normallynormal.World.Entity.Particle;

import normallynormal.GameManager;
import normallynormal.Input.InputHandler;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Entity.Entity;
import normallynormal.Math.Vector2;
import normallynormal.Math.Direction;
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
    public void process(double timeDelta, InputHandler input) {
        fadeTime -= timeDelta;
        if (fadeTime <= 0) {
            GameManager.currentLevel.removeEntity(this);
        }
        if (direction == Direction.LEFT) {
            position.x -= 20 * timeDelta;
        }
        else {
            position.x += 20 * timeDelta;
        }
    }

    private record State(double posX, double posY, int depth, boolean onScreen, double fadeTime, Direction direction) implements RenderState {
        @Override
        public void render(DepthScreen screen, int xOffset, int yOffset) {
            TextColor renderColor = fadeTime < 0.12 ? TextColor.ANSI.GREEN : TextColor.ANSI.GREEN_BRIGHT;
            char character = (direction == Direction.LEFT) ? '[' : ']';
            screen.setCharacterWithDepth(
                    (int) posX,
                    (int) posY,
                    xOffset,
                    yOffset,
                    depth,
                    new TextCharacter(character, renderColor, TransparentColor.TRANSPARENT)
            );
        }
    }

    @Override
    public void copyForRender() {
        renderState = new State(position.x, position.y, depth, isOnScreen(), fadeTime, direction);
    }
}
