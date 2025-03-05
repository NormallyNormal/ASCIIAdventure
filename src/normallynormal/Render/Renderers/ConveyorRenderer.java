package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Render.DepthScreen;

import java.util.function.Supplier;

public class ConveyorRenderer extends AbstractRenderer {
    private final Supplier<Direction> directionSupplier;
    private final Supplier<Double> speedSupplier;

    private static final TextColor DARK_GREY = new TextColor.RGB(20, 20, 20);
    private static final TextCharacter VERTICAL_LINE = new TextCharacter('|', TextColor.ANSI.BLACK_BRIGHT, DARK_GREY);
    private static final TextCharacter HORIZONTAL_LINE = new TextCharacter('─', TextColor.ANSI.BLACK_BRIGHT, DARK_GREY);
    private static final TextCharacter UP_ARROW = new TextCharacter('▲', TextColor.ANSI.YELLOW, DARK_GREY);
    private static final TextCharacter DOWN_ARROW = new TextCharacter('▼', TextColor.ANSI.YELLOW, DARK_GREY);
    private static final TextCharacter LEFT_ARROW = new TextCharacter('◀', TextColor.ANSI.YELLOW, DARK_GREY);
    private static final TextCharacter RIGHT_ARROW = new TextCharacter('▶', TextColor.ANSI.YELLOW, DARK_GREY);

    private static final TextCharacter[] leftCharacters = {VERTICAL_LINE, VERTICAL_LINE, LEFT_ARROW};
    private static final TextCharacter[] rightCharacters = {VERTICAL_LINE, VERTICAL_LINE, RIGHT_ARROW};
    private static final TextCharacter[] upCharacters = {HORIZONTAL_LINE, HORIZONTAL_LINE, UP_ARROW};
    private static final TextCharacter[] downCharacters = {HORIZONTAL_LINE, HORIZONTAL_LINE, DOWN_ARROW};

    public ConveyorRenderer(Supplier<AABB> visibilityBoxSupplier, Supplier<Direction> directionSupplier, Supplier<Double> speedSupplier) {
        super(visibilityBoxSupplier);

        this.directionSupplier = directionSupplier;
        this.speedSupplier = speedSupplier;
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB collisionBox = visibilityBoxSupplier.get();
        Direction direction = directionSupplier.get();
        double speed = speedSupplier.get();
        int offset = (int)(((System.currentTimeMillis() * (int)speed) / 1000));
        switch (direction) {
            case LEFT:
                for (int i = 0; i <= collisionBox.w - 1; i++) {
                    for (int j = 0; j <= collisionBox.h - 1; j++) {
                        screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, leftCharacters[(i + offset) % leftCharacters.length]);
                    }
                }
                break;
            case RIGHT:
                for (int i = 0; i <= collisionBox.w - 1; i++) {
                    for (int j = 0; j <= collisionBox.h - 1; j++) {
                        screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, rightCharacters[Math.abs(i - offset) % rightCharacters.length]);
                    }
                }
                break;
            case UP:
                for (int i = 0; i <= collisionBox.w - 1; i++) {
                    for (int j = 0; j <= collisionBox.h - 1; j++) {
                        screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, upCharacters[(j + offset) % upCharacters.length]);
                    }
                }
                break;
            case DOWN:
                for (int i = 0; i <= collisionBox.w - 1; i++) {
                    for (int j = 0; j <= collisionBox.h - 1; j++) {
                        screen.setCharacterWithDepth((int) collisionBox.x + i, (int) collisionBox.y + j, xOffset, yOffset, 0, downCharacters[Math.abs(j + offset) % downCharacters.length]);
                    }
                }
                break;
        }

    }
}
