package main.Math;

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
}
