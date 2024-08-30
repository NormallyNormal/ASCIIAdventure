package Math;

public enum Direction {
    NONE,
    UP,
    DOWN,
    LEFT,
    RIGHT;

    public int toMovement() {
        return switch (this) {
            case UP, LEFT -> -1;
            case DOWN, RIGHT -> 1;
            default -> 0;
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
}
