package normallynormal.World.Platform.Controller;

import normallynormal.Math.Direction;
import normallynormal.World.Level;
import normallynormal.World.Platform.MoveableObject;

public class BasicPlatformController extends AbstractPlatformController {
    int steps;
    int stepsMade;

    double pauseTime;
    double timeSinceLastMovement;

    boolean vertical;

    public BasicPlatformController(MoveableObject controlledMovableObject, Direction initialDirection, int steps, double pauseTime) {
        super(controlledMovableObject);
        this.vertical = initialDirection.isVertical();
        this.direction = initialDirection;
        this.steps = steps;
        this.pauseTime = pauseTime;
        this.stepsMade = direction.isAnyOf(Direction.RIGHT.getValue() | Direction.DOWN.getValue()) ? 0 : steps;
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
            allowMoveNextTick();
            timeSinceLastMovement -= pauseTime;
            boolean overshooting = stepsMade >= steps && direction.isAnyOf(Direction.RIGHT.getValue() | Direction.DOWN.getValue());
            boolean undershooting = stepsMade <= 0 && direction.isAnyOf(Direction.LEFT.getValue() | Direction.UP.getValue());
            if(overshooting || undershooting) {
                direction = direction.opposite();
            }
        }
    }
}
