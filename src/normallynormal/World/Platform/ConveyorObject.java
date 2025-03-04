package normallynormal.World.Platform;

import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Render.Renderers.ConveyorRenderer;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;

public class ConveyorObject extends MoveableObject {

    Direction conveyorDirection;
    double speed;

    public ConveyorObject(AABB collisionBox, int id, Direction direction, double speed) {
        super(collisionBox, id);
        this.conveyorDirection = direction;
        this.speed = speed;
        this.renderer = new ConveyorRenderer(this::getVisibilityBox, this::getConveyorDirection, this::getSpeed);
    }

    @Override
    public void collisionEffect(Entity entity, Level level, Direction direction) {
        if (conveyorDirection.isVertical() && !direction.isVertical()) {
            entity.getInstantVelocity().y += speed * conveyorDirection.toMovement();
        }
        else if (!conveyorDirection.isVertical() && direction.isVertical()) {
            entity.getInstantVelocity().x += speed * conveyorDirection.toMovement();
        }
        super.collisionEffect(entity, level, direction);
    }

    public Direction getConveyorDirection() {
        return conveyorDirection;
    }

    public double getSpeed () {
        return speed;
    }
}
