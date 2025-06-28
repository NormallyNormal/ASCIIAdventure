package normallynormal.World.Entity.Particle;

import normallynormal.Input.Input;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Entity.Entity;
import normallynormal.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

public class SlamParticle extends Entity {
    double fadeTime = 0.15;
    Direction direction;
    boolean ground;
    boolean downward;

    public SlamParticle(Vector2 position, Direction direction, boolean ground, boolean downward) {
        this.position = position;
        this.collisionBox = new AABB(position.x, position.y, 1, 1);
        this.depth = -1;
        this.direction = direction;
        this.ground = ground;
        this.downward = downward;
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

    private Vector2 render_position = position.deepCopy();
    private boolean render_ground;
    private double render_fadeTime;
    private Direction render_direction;
    private boolean render_downward;
    private int render_depth;

    public void copyForRender() {
        render_position = position.deepCopy(render_position);
        render_ground = ground;
        render_fadeTime = fadeTime;
        render_direction = direction;
        render_downward = downward;
        render_depth = depth;
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        TextColor renderColor;
        if (render_ground) {
            renderColor = render_fadeTime < 0.10 ? TextColor.ANSI.BLACK_BRIGHT : TextColor.ANSI.WHITE;
        } else {
            renderColor = render_fadeTime < 0.07
                    ? TextColor.ANSI.RED
                    : render_fadeTime < 0.12
                    ? TextColor.ANSI.RED_BRIGHT
                    : TextColor.ANSI.WHITE;
        }

        char character;
        if (render_downward) {
            character = (render_direction == Direction.LEFT)
                    ? (render_ground ? '◣' : '\\')
                    : (render_ground ? '◢' : '/');
        } else {
            character = (render_direction == Direction.LEFT)
                    ? (render_ground ? '◤' : '/')
                    : (render_ground ? '◥' : '\\');
        }

        screen.setCharacterWithDepth(
                (int) render_position.x,
                (int) render_position.y,
                xOffset,
                yOffset,
                render_depth,
                new TextCharacter(character, renderColor, TransparentColor.TRANSPARENT)
        );
    }

}
