package World.Platform;

import World.Entity.Entity;
import World.Level;
import Math.Direction;
import Math.Vector2;
import Math.AABB;

public class MovingObject extends StaticObject {
    int xSteps;
    int xStepsMade;
    Direction xDirection;
    int ySteps;
    int yStepsMade;
    Direction yDirection;
    double speed;
    double timeSinceLastMovement;
    public MovingObject(AABB collisionBox, int id, int xSteps, int ySteps, double speed) {
        super(collisionBox, id);
        this.xSteps = xSteps;
        this.ySteps = ySteps;
        xStepsMade = 0;
        yStepsMade = 0;
        xDirection = Direction.RIGHT;
        yDirection = Direction.UP;
        this.speed = speed;
    }

    public MovingObject(AABB collisionBox, int id, int xSteps, int ySteps, double speed, boolean semiSolid) {
        this(collisionBox, id, xSteps, ySteps, speed);
        this.semiSolid = semiSolid;
    }

    boolean movesNextFrame = false;
    boolean movedThisFrame = false;
    @Override
    public void process(double timeDeltaSeconds, Level level) {
        timeSinceLastMovement += timeDeltaSeconds;
        movedThisFrame = false;
        if(movesNextFrame) {
            movesNextFrame = false;
            movedThisFrame = true;
            if(xSteps > 0) {
                collisionBox.x += xDirection.toMovement();
                xStepsMade += xDirection.toMovement();
            }
            if(ySteps > 0) {
                collisionBox.y += yDirection.toMovement();
                yStepsMade -= yDirection.toMovement();
            }
        }
        if(timeSinceLastMovement >= speed) {
            movesNextFrame = true;
            timeSinceLastMovement -= speed;
            if((xStepsMade >= xSteps && xDirection == Direction.RIGHT) || (xStepsMade <= 0 && xDirection == Direction.LEFT)) {
                xDirection = xDirection.opposite();
            }
            if((yStepsMade >= ySteps && yDirection == Direction.UP) || (yStepsMade <= 0 && yDirection == Direction.DOWN)) {
                yDirection = yDirection.opposite();
            }
        }
        super.process(timeDeltaSeconds, level);
    }

    @Override
    public void collisionEffect(Entity entity, Level level) {
        if(entity.getCollisionBox().y + entity.getCollisionBox().h <= collisionBox.y + 0.1) {
            if (xSteps > 0 && movesNextFrame) {
                entity.setMinInvincibleTicks(2);
                switch (xDirection) {
                    case LEFT:
                        entity.enqueueMovement(new Vector2(-1, 0));
                        entity.getPosition().y = Math.nextDown(collisionBox.y - entity.getCollisionBox().h);
                        break;
                    case RIGHT:
                        entity.enqueueMovement(new Vector2(1, 0));
                        entity.getPosition().y = Math.nextDown(collisionBox.y - entity.getCollisionBox().h);
                        break;
                }
            }
        }
        else if (xSteps > 0 && movedThisFrame && entity.getPosition().y + entity.getCollisionBox().h > collisionBox.y && entity.getPosition().y < collisionBox.y + collisionBox.h) {
            switch (xDirection) {
                case LEFT:
                    if (entity.getPosition().x < collisionBox.x + collisionBox.w) {
                        entity.getPosition().x = Math.nextDown(collisionBox.x - entity.getCollisionBox().w);
                        entity.clearMovementStep();
                    }
                    break;
                case RIGHT:
                    if (entity.getPosition().x > collisionBox.x) {
                        entity.getPosition().x = Math.nextUp(collisionBox.x + collisionBox.w);
                        entity.clearMovementStep();
                    }
                    break;
            }

        }
        if(ySteps > 0 && movedThisFrame && entity.getPosition().x + entity.getCollisionBox().w > collisionBox.x && entity.getPosition().x < collisionBox.x + collisionBox.w) {
            switch (yDirection) {
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
        super.collisionEffect(entity, level);
    }
}
