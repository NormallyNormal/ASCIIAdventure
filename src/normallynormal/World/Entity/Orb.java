package normallynormal.World.Entity;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Input.InputHandler;
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
    public void process(double timeDelta, InputHandler input) {
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

    private record State(double posX, double posY, int depth, boolean onScreen,
                         OrbType orbType, double timeUntilRefresh) implements RenderState {
        @Override
        public void render(DepthScreen screen, int xOffset, int yOffset) {
            if (orbType == OrbType.DASH) {
                screen.setCharacterWithDepth((int) posX + 1, (int) posY + 1, xOffset, yOffset, depth, timeUntilRefresh <= 0 ? dashOrb : dashOrbEmpty);
            }
            if (orbType == OrbType.DOUBLE_JUMP) {
                screen.setCharacterWithDepth((int) posX + 1, (int) posY + 1, xOffset, yOffset, depth, timeUntilRefresh <= 0 ? doubleJumpOrb : doubleJumpOrbEmpty);
            }
        }
    }

    @Override
    public void copyForRender() {
        renderState = new State(position.x, position.y, depth, isOnScreen(), orbType, timeUntilRefresh);
    }
}
