package normallynormal.World.Platform.Controller;

import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Math.Vector2;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Level;
import normallynormal.World.Platform.MoveableObject;

public abstract class AbstractPlatformController {
    Direction direction;
    boolean movesNextFrame = false;
    boolean movedThisFrame = false;

    MoveableObject controlledMovableObject;

    public AbstractPlatformController(MoveableObject controlledMovableObject) {
        this.controlledMovableObject = controlledMovableObject;
    }

    public abstract void process(double timeDeltaSeconds, Level level);

    public void collisionEffect(Entity entity, Level level, Direction collisionDirection) {
        AABB collisionBox = controlledMovableObject.getCollisionBox();
        if (!direction.isVertical()) {
            if (collisionDirection.isVertical() && movesNextFrame) {
                if (entity.isGravityDownward()) {
                    if (entity.getCollisionBox().y + entity.getCollisionBox().h <= collisionBox.y) {
                        dragEntityHorizontally(entity);
                    }
                } else {
                    if (entity.getCollisionBox().y >= collisionBox.y + collisionBox.h) {
                        dragEntityHorizontally(entity);
                    }
                }
            } else if (!collisionDirection.isVertical() && movedThisFrame && entity.getPosition().y + entity.getCollisionBox().h > collisionBox.y && entity.getPosition().y < collisionBox.y + collisionBox.h) {
                pushEntityHorizontally(entity);
            }
        }
        else if (movedThisFrame) {
            if (entity.getPosition().x + entity.getCollisionBox().w > collisionBox.x && entity.getPosition().x < collisionBox.x + collisionBox.w) {
                pushEntityVertically(entity);
            }
        }
    }

    private void pushEntityHorizontally(Entity entity) {
        AABB collisionBox = controlledMovableObject.getCollisionBox();
        double newPos = entity.getPosition().x;
        boolean apply = false;
        switch (direction) {
            case LEFT:
//                if (entity.getPosition().x <= collisionBox.x + entity.getMovementStep().x) {
                newPos = Math.nextDown(collisionBox.x - entity.getCollisionBox().w);
                apply = true;
//                }
                break;
            case RIGHT:
//                if (entity.getPosition().x >= collisionBox.x + collisionBox.w - entity.getCollisionBox().w + entity.getMovementStep().x) {
                newPos = Math.nextUp(collisionBox.x + collisionBox.w);
                apply = true;
//                }
                break;
        }
        if (apply) {
            if (entity.getMovementStep().magnitudeSquared() < 0.9) {
                entity.clearMovementStep();
            }
            else {
                entity.clearMovementStep(new Vector2(newPos - entity.getPosition().x, 0));
            }
            entity.getPosition().x = newPos;
        }
    }

    private void dragEntityHorizontally(Entity entity) {
        AABB collisionBox = controlledMovableObject.getCollisionBox();
        entity.setMinInvincibleTicks(2);
        switch (direction) {
            case LEFT:
                entity.enqueueMovement(new Vector2(-1, 0));
                break;
            case RIGHT:
                entity.enqueueMovement(new Vector2(1, 0));
                break;
        }
        if (entity.isGravityDownward()) {
            entity.getPosition().y = Math.nextDown(collisionBox.y - entity.getCollisionBox().h);
            entity.clearMovementStep(Direction.DOWN);
        }
        else {
            entity.getPosition().y = Math.nextUp(collisionBox.y + collisionBox.h);
            entity.clearMovementStep(Direction.UP);
        }
    }

    private void pushEntityVertically(Entity entity) {
        AABB collisionBox = controlledMovableObject.getCollisionBox();
        switch (direction) {
            case UP:
                if (entity.getPosition().y < collisionBox.y + collisionBox.h)
                    entity.getPosition().y = Math.nextDown(collisionBox.y - entity.getCollisionBox().h);
                break;
            case DOWN:
                if (entity.getPosition().y > collisionBox.y)
                    entity.getPosition().y = Math.nextUp(collisionBox.y + collisionBox.h);
                break;
        }
        entity.clearMovementStep();
    }

    protected boolean isAboutToMove() {
        return movesNextFrame;
    }

    protected void allowMoveNextTick() {
        movesNextFrame = true;
    }

    protected boolean moveIfPossible() {
        movedThisFrame = false;
        if (isAboutToMove()) {
            AABB collisionBox = controlledMovableObject.getCollisionBox();
            if (direction.isVertical()) {
                collisionBox.y += direction.toMovement();
            } else {
                collisionBox.x += direction.toMovement();
            }
            movesNextFrame = false;
            movedThisFrame = true;
            return true;
        }
        return false;
    }
}
