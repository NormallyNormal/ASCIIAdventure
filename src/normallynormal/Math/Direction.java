package normallynormal.Math;

public enum Direction {
    NONE(0),
    UP(1),
    DOWN(1 << 1),
    LEFT(1 << 2),
    RIGHT(1 << 3);

    private final int value;

    Direction(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public int toMovement() {
        return switch (this) {
            case UP, LEFT -> -1;
            case DOWN, RIGHT -> 1;
            default -> 0;
        };
    }

    public Vector2 toMovementVector() {
        return switch (this) {
            case UP -> new Vector2(0,-1);
            case DOWN -> new Vector2(0,1);
            case LEFT -> new Vector2(-1,0);
            case RIGHT -> new Vector2(1,0);
            default -> new Vector2(0,0);
        };
    }

    public Direction opposite() {
        return switch (this) {
            case UP -> DOWN;
            case DOWN -> UP;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            default -> NONE;
        };
    }

    public boolean isVertical() {
        return switch (this) {
            case UP, DOWN -> true;
            case LEFT, RIGHT -> false;
            default -> false;
        };
    }

    public boolean isAnyOf(int directions) {
        return (this.getValue() & directions) != 0;
    }
}
