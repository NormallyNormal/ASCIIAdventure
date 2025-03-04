package normallynormal.World.Entity;

import normallynormal.Input.Input;
import normallynormal.World.CollisionObject;
import normallynormal.Game;
import normallynormal.World.PhysicsObject;
import normallynormal.World.RenderObject;
import normallynormal.Render.DepthScreen;
import normallynormal.Math.Vector2;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;

public abstract class Entity implements CollisionObject, PhysicsObject, RenderObject {
    protected Vector2 position = new Vector2(0, 0);
    protected int depth;
    protected Vector2 velocity = new Vector2(0, 0);
    protected Vector2 instantVelocity = new Vector2(0, 0);
    protected Vector2 movementStep = new Vector2(0,0);
    protected double gravityMagnitude = 0;
    private Direction gravityDirection = Direction.DOWN;
    private Vector2 gravity = new Vector2(0, 0);
    protected boolean noGravity = true;
    protected AABB collisionBox = new AABB(0,0, 0 ,0);
    protected boolean standsOnSemisolid = true;
    protected boolean dead;
    int invincibleTicks = 0;
    protected double timeSinceOnGround = Double.POSITIVE_INFINITY;

    protected Vector2 enqueuedMovement = new Vector2(0,0);

    protected double timeSinceWallHit = Double.POSITIVE_INFINITY;
    protected Direction lastCollisionDirection = Direction.NONE;

    protected boolean bounce;

    public void process(double timeDelta, Input input) {
        position.add(movementStep);
        if (!noGravity) {
            velocity.add(Vector2.multiply(gravity, timeDelta));
        }
        movementStep.zero();
        if (hasEnqueuedMovement()) {
            movementStep.add(enqueuedMovement);
        }
        else{
            movementStep = Vector2.multiply(Vector2.add(velocity, instantVelocity), timeDelta);
        }
        enqueuedMovement.zero();
        instantVelocity.zero();
        collisionBox.x = position.x;
        collisionBox.y = position.y;
        timeSinceOnGround += timeDelta;
        timeSinceWallHit += timeDelta;
        invincibleTicks--;
    }

    public void render(DepthScreen screen) {

    }

    public void kill() {
        if(invincibleTicks > 0) {
            return;
        }
        this.dead = true;
        this.velocity = new Vector2(0,0);
        this.gravity = new Vector2(0,0);
        this.movementStep = new Vector2(0,0);
    }

    public void setMinInvincibleTicks(int invincibleTicks) {
        this.invincibleTicks = Math.max(invincibleTicks, this.invincibleTicks);
    }

    public Vector2 getEntryDistance(AABB platform) {
        Vector2 entryDistance = new Vector2(0,0);

        if (movementStep.x > 0) {
            entryDistance.x = platform.x - (collisionBox.x + collisionBox.w);
        }
        else {
            entryDistance.x = collisionBox.x - (platform.x + platform.w);
        }

        if (movementStep.y > 0) {
            entryDistance.y = platform.y - (collisionBox.y + collisionBox.h);
        }
        else {
            entryDistance.y = collisionBox.y - (platform.y + platform.h);
        }

        return entryDistance;
    }

    public Vector2 getExitDistance(AABB platform) {
        Vector2 exitDistance = new Vector2(0,0);
        if (movementStep.x > 0) {
            exitDistance.x = (platform.x + platform.w) - collisionBox.x;
        }
        else {
            exitDistance.x = (collisionBox.x + collisionBox.w) - platform.x;
        }

        if (movementStep.y > 0) {
            exitDistance.y = (platform.y + platform.h) - collisionBox.y;
        }
        else {
            exitDistance.y = (collisionBox.y + collisionBox.h) - platform.y;
        }

        return exitDistance;
    }

    public Vector2 getEntryTime(AABB platform) {
        Vector2 entryDistance = getEntryDistance(platform);
        Vector2 entryTime = new Vector2(0,0);
        entryTime.x = Math.nextDown(Math.abs(entryDistance.x/ movementStep.x));
        entryTime.y = Math.nextDown(Math.abs(entryDistance.y/ movementStep.y));
        return entryTime;
    }

    public Vector2 getExitTime(AABB platform) {
        Vector2 exitDistance = getExitDistance(platform);
        Vector2 exitTime = new Vector2(0,0);
        exitTime.x = Math.nextUp(Math.abs(exitDistance.x/ movementStep.x));
        exitTime.y = Math.nextUp(Math.abs(exitDistance.y/ movementStep.y));
        return exitTime;
    }

    public Direction getCollisionDirection(Vector2 entryTime) {
        if (entryTime.x < 1) {
            if (movementStep.x < 0)
                return Direction.LEFT;
            return Direction.RIGHT;
        }
        if (entryTime.y < 1) {
            if (movementStep.y < 0)
                return Direction.UP;
            return Direction.DOWN;
        }
        return Direction.NONE;
    }

    public boolean collides(Vector2 entryTime, Vector2 exitTime) {
        return entryTime.x < exitTime.x || entryTime.y < exitTime.y;
    }

    public void applyCollision(Vector2 entryTime) {
        if (entryTime.x < 1) {
            velocity.x = 0;
            hitWall();
            if (movementStep.x > 0) {
                lastCollisionDirection = Direction.RIGHT;
            }
            else {
                lastCollisionDirection = Direction.LEFT;
            }
        }
        if (entryTime.y < 1) {
            velocity.y = bounce ? -velocity.y : 0;
            if (movementStep.y > 0) {
                if (isGravityDownward()) {
                    hitGround();
                }
                lastCollisionDirection = Direction.DOWN;
            }
            else {
                if (!isGravityDownward()) {
                    hitGround();
                }
                lastCollisionDirection = Direction.UP;
            }
            bounce = false;
        }
        if(entryTime.x < 1)
            movementStep.x *= Math.max(0, entryTime.x - 0.01);
        if(entryTime.y < 1)
            movementStep.y *= Math.max(0, entryTime.y - 0.01);
    }

    protected void hitGround() {
        timeSinceOnGround = 0;
    }

    protected void hitWall() {
        timeSinceWallHit = 0;
    }

    public AABB getCollisionBox() {
        return collisionBox;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public Vector2 getInstantVelocity() {
        return instantVelocity;
    }

    public Vector2 getMovementStep() {
        return movementStep;
    }

    public Vector2 getAcceleration() {
        return gravity;
    }

    public boolean standsOnSemisolid() {
        return standsOnSemisolid;
    }

    public void enqueueMovement(Vector2 movement) {
        enqueuedMovement.add(movement);
    }

    public boolean hasEnqueuedMovement() {
        return !enqueuedMovement.isZero();
    }

    public void clearMovementStep() {
        movementStep.zero();
    }

    public void clearMovementStep(Vector2 vector) {
        movementStep.moveTowardsZeroBy(vector);
    }

    public void clearMovementStep(Direction direction) {
        switch (direction) {
            case LEFT:
                movementStep.x = Math.min(movementStep.x, 0);
                break;
            case RIGHT:
                movementStep.x = Math.max(movementStep.x, 0);
                break;
            case UP:
                movementStep.y = Math.min(movementStep.y, 0);
                break;
            case DOWN:
                movementStep.y = Math.max(movementStep.y, 0);
                break;
        }
    }

    public void setGravityUpward() {
        gravity = new Vector2(0, -gravityMagnitude);
        gravityDirection = Direction.UP;
    }

    public void setGravityDownward() {
        gravity = new Vector2(0, gravityMagnitude);
        gravityDirection = Direction.DOWN;
    }

    public boolean isGravityDownward() {
        return gravityDirection == Direction.DOWN;
    }

    protected void setGravityMagnitude(double gravityMagnitude) {
        this.gravityMagnitude = gravityMagnitude;
        this.gravity = new Vector2(0, gravityMagnitude);
    }

    public boolean isOnScreen() {
        return collisionBox.overlaps(Game.screenBoundingBox);
    }

    public void bounce() {
        bounce = true;
    }
}