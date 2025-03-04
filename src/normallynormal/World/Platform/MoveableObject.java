package normallynormal.World.Platform;

import normallynormal.Math.Direction;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.Math.AABB;
import normallynormal.World.Platform.Controller.AbstractPlatformController;
import normallynormal.World.Platform.Controller.BasicPlatformController;

public class MoveableObject extends StaticObject {

    private AbstractPlatformController controller;

    public MoveableObject(AABB collisionBox, int id) {
        super(collisionBox, id);
    }

    public void setController(AbstractPlatformController controller) {
        this.controller = controller;
    }

    public void createBasicController(Direction initialDirection, int steps, double pauseTime) {
        this.controller = new BasicPlatformController(this, initialDirection, steps, pauseTime);
    }

    @Override
    public void process(double timeDeltaSeconds, Level level) {
        if (controller != null) {
            controller.process(timeDeltaSeconds, level);
        }
        super.process(timeDeltaSeconds, level);
    }

    @Override
    public void collisionEffect(Entity entity, Level level, Direction direction) {
        if (controller != null && isSolid(entity)) {
            controller.collisionEffect(entity, level, direction);
        }
        super.collisionEffect(entity, level, direction);
    }
}