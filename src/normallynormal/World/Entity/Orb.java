package normallynormal.World.Entity;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Game;
import normallynormal.Input.Input;
import normallynormal.Math.M4th;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.World.Level;

public class Orb extends Entity {
    private static final TextCharacter dashOrb = new TextCharacter('◉', TextColor.ANSI.BLUE_BRIGHT, TransparentColor.TRANSPARENT);
    private static final TextCharacter doubleJumpOrb = new TextCharacter('◉', TextColor.ANSI.GREEN, TransparentColor.TRANSPARENT);
    private static final TextCharacter dashOrbEmpty = new TextCharacter('○', TextColor.ANSI.BLUE_BRIGHT, TransparentColor.TRANSPARENT);
    private static final TextCharacter doubleJumpOrbEmpty = new TextCharacter('○', TextColor.ANSI.GREEN, TransparentColor.TRANSPARENT);

    private static final double REFRESH_TIME = 5;

    public enum OrbType {
        DOUBLE_JUMP,
        DASH
    }

    private final OrbType orbType;
    private double timeUntilRefresh;

    public Orb(int x, int y, OrbType orbType) {
        super();
        this.position.x = x;
        this.position.y = y;
        this.collisionBox.w = 3;
        this.collisionBox.h = 3;
        this.orbType = orbType;
        depth = 9;
    }

    @Override
    public void process(double timeDelta, Input input) {
        super.process(timeDelta, input);
        timeUntilRefresh -= timeDelta;
    }

    @Override
    public void intersectEffect(Entity entity, Level level) {
        if (timeUntilRefresh <= 0 & entity instanceof Player) {
            timeUntilRefresh = REFRESH_TIME;
            if (orbType == OrbType.DASH) {
                ((Player) entity).refreshDash();
            }
            else if (orbType == OrbType.DOUBLE_JUMP) {
                ((Player) entity).refreshDoubleJump();
            }
        }
    }

    Vector2 render_position = position.deepCopy();
    int render_depth = depth;
    double render_timeUntilRefresh = timeUntilRefresh;
    @Override
    public void copyForRender() {
        render_position = position.deepCopy(render_position);
        render_depth = depth;
        render_timeUntilRefresh = timeUntilRefresh;
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        if (orbType == OrbType.DASH) {
            screen.setCharacterWithDepth((int) render_position.x + 1, (int) render_position.y + 1, xOffset, yOffset, render_depth, render_timeUntilRefresh <= 0 ? dashOrb : dashOrbEmpty);
        }
        if (orbType == OrbType.DOUBLE_JUMP) {
            screen.setCharacterWithDepth((int) render_position.x + 1, (int) render_position.y + 1, xOffset, yOffset, render_depth, render_timeUntilRefresh <= 0 ? doubleJumpOrb : doubleJumpOrbEmpty);
        }
    }
}
