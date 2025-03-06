package normallynormal.World.Platform.Controller;

import normallynormal.Math.Direction;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.World.Platform.MoveableObject;

public class WatchingPlatformController extends BasicPlatformController {
    boolean forward = false;
    Direction initialDirection;

    public WatchingPlatformController(MoveableObject controlledMovableObject, Direction initialDirection, int steps, double pauseTime) {
        super(controlledMovableObject, initialDirection, steps, pauseTime);
        this.initialDirection = initialDirection;
    }

    @Override
    public void process(double timeDeltaSeconds, Level level) {
        timeSinceLastMovement += timeDeltaSeconds;
        if(steps > 0) {
            boolean moved = moveIfPossible();
            if (moved)
                stepsMade += direction.toMovement();
        }
        if(timeSinceLastMovement >= pauseTime) {
            direction = forward ? initialDirection : initialDirection.opposite();
            timeSinceLastMovement -= pauseTime;
            boolean overshooting = stepsMade >= steps && direction.isAnyOf(Direction.RIGHT.getValue() | Direction.DOWN.getValue());
            boolean undershooting = stepsMade <= 0 && direction.isAnyOf(Direction.LEFT.getValue() | Direction.UP.getValue());
            if (!(overshooting || undershooting))
                allowMoveNextTick();
        }
        forward = false;
    }

    @Override
    public void collisionEffect(Entity entity, Level level, Direction collisionDirection) {
        if (collisionDirection.opposite() == initialDirection)
            forward = true;
        super.collisionEffect(entity, level, collisionDirection);
    }
}
