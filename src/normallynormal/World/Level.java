package normallynormal.World;

import java.util.*;

import normallynormal.Render.Shader.PostShader;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Entity.NPC;
import normallynormal.World.Entity.Player;
import normallynormal.Input.Input;
import normallynormal.World.Entity.Decoration.Torch;
import normallynormal.World.Platform.*;
import normallynormal.Render.DepthScreen;
import normallynormal.Math.AABB;
import normallynormal.Math.Vector2;
import normallynormal.Math.Direction;
import normallynormal.Constants.ScreenConstants;
import normallynormal.Math.M4th;

public class Level {
    private final List<WorldObject> worldObjects;
    private final List<Entity> entities;
    private final Deque<Entity> entitiesToRemove;
    private final Player player;

    private final List<PostShader> postShaders;

    @SuppressWarnings("UnusedAssignment")
    public Level() {
        int startingLocationX = 3600;
        int startingLocationY = 1200;

        postShaders = new ArrayList<>();
        //addPostShader(new TorchlightPostShader(), 0);

        player = new Player();
        player.getPosition().x = 5 + startingLocationX;
        player.getPosition().y = 34 + startingLocationY;
        player.setSpawnPosition(new Vector2(5 + startingLocationX, 34 + startingLocationY));
        worldObjects = new ArrayList<>();
        entities = new ArrayList<>();
        entitiesToRemove = new ArrayDeque<>();
        int currentId = 0;
//        worldObjects.add(new MovingObject(new AABB(65, 19, 5, 1), currentId++, 0, 10, 0.25));
//        worldObjects.add(new MovingObject(new AABB(75, 19, 5, 1), currentId++, 0, 10, 0.2));
//        worldObjects.add(new StaticObject(new AABB(10, 5, 5, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(55, 20, 50, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(5, 20, 50, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(70, 15, 5, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(70, 10, 5, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(35, 17, 5, 3), currentId++));
//        worldObjects.add(new StaticObject(new AABB(20, 10, 20, 1), currentId++));
//        worldObjects.add(new StaticObject(new AABB(35, 5, 2, 5), currentId++));
//        worldObjects.add(new StaticObject(new AABB(20, 15, 10, 1), currentId++, true));
//        worldObjects.add(new StaticObject(new AABB(5, 30, 30, 1), currentId++, true));
//        worldObjects.add(new StaticObject(new AABB(20, 35, 10, 1), currentId++, true));
//        worldObjects.add(new StaticHazardObject(new AABB(20, 19, 5, 1), currentId++));
//        worldObjects.add(new MovingObject(new AABB(20, 5, 5, 5), currentId++, 10, 0, 0.4));
//        worldObjects.add(new StaticObject(new AABB(28, 2, 5, 3), currentId++));

        worldObjects.add(new StaticObject(new AABB(startingLocationX + -120, startingLocationY + 35, 360, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + -120, startingLocationY + -40, 360, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + -120, startingLocationY + -35, 5, 70), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 235, startingLocationY + -35, 5, 70), currentId++));


        //The semi bois
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + 30, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + 25, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + 20, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + 15, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + 10, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + 5, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + 0, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + -5, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 10, startingLocationY + -10, 5, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 0, startingLocationY + -15, 120, 1), currentId++));

        worldObjects.add(new StaticHazardObject(new AABB(startingLocationX + 125, startingLocationY + 34, 110, 1), currentId++));

        worldObjects.add(new MovingObject(new AABB(startingLocationX + 70, startingLocationY + 33, 10, 2), currentId++, 30, 0, 0.2));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 106, startingLocationY + 30, 2, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 106, startingLocationY + 28, 2, 2), currentId++));
        //worldObjects.add(new MovingObject(new AABB(startingLocationX + 45, startingLocationY + 33, 15, 2), currentId++, 1, 0, 0.001));

        worldObjects.add(new MovingObject(new AABB(startingLocationX + 125, startingLocationY + 32, 10, 2), currentId++, 25, 0, 0.2));
        worldObjects.add(new MovingObject(new AABB(startingLocationX + 125 + 25, startingLocationY + 32, 10, 2), currentId++, 25, 0, 0.2));
        worldObjects.add(new MovingObject(new AABB(startingLocationX + 125 + 50, startingLocationY + 32, 10, 2), currentId++, 25, 0, 0.2));
        worldObjects.add(new MovingObject(new AABB(startingLocationX + 125 + 75, startingLocationY + 32, 10, 2), currentId++, 25, 0, 0.2));
        worldObjects.add(new MovingObject(new AABB(startingLocationX + 125 + 70, startingLocationY + 30, 10, 2), currentId++, 25, 0, 0.2));

        worldObjects.add(new MovingObject(new AABB(startingLocationX + -60, startingLocationY + 23, 10, 2), currentId++, 10, 0, 0.2));
        worldObjects.add(new MovingObject(new AABB(startingLocationX + -40, startingLocationY + 32, 10, 2), currentId++, 0, 9, 0.2));

        entities.add(new Torch(startingLocationX + 30, startingLocationY + 30));

        entities.add(new NPC(startingLocationX + 60, startingLocationY + 34));

        worldObjects.add(new GravityField(new AABB(startingLocationX - 80, startingLocationY + 25, 10, 10), currentId++, true));
        //worldObjects.add(new GravityField(new AABB(startingLocationX - 90, startingLocationY + 25, 10, 10), currentId++, false));

        worldObjects.add(new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 23, 30 , 2), currentId++));

        worldObjects.add(new GravityField(new AABB(startingLocationX - 120, startingLocationY + 15, 120, 3), currentId++, false));
        worldObjects.add(new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 13, 30 , 2), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 6, 30 , 2), currentId++));

        worldObjects.add(new StaticHazardObject(new AABB(startingLocationX + 125, startingLocationY + -10, 110, 1), currentId++));

        worldObjects.add(new BouncyObject(new AABB(startingLocationX + 20, startingLocationY + 33, 5, 2), currentId++));
        worldObjects.add(new BouncyObject(new AABB(startingLocationX - 95, startingLocationY + 17, 5, 2), currentId++));
    }

    public void process(double timeDeltaSeconds, Input input) {
        player.process(timeDeltaSeconds, input);
        checkInsidePlatform(player);
        for (Entity entity : entities) {
            entity.process(timeDeltaSeconds, input);
        }
        while (!entitiesToRemove.isEmpty()) {
            entities.remove(entitiesToRemove.pop());
        }
        for (WorldObject worldObject : worldObjects) {
            worldObject.process(timeDeltaSeconds, this);
        }
        runPlatformCollisions(player);
    }

    private int lastRenderOffsetX = 0;
    private int lastRenderOffsetY = 0;
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        lastRenderOffsetX = xOffset;
        lastRenderOffsetY = yOffset;
        for (WorldObject worldObject : worldObjects) {
            if (worldObject.isOnScreen())
                worldObject.getRenderer().render(screen, xOffset, yOffset);
        }
        player.render(screen, xOffset, yOffset);
        for (Entity entity : entities) {
            if (entity.isOnScreen())
                entity.render(screen, xOffset, yOffset);
        }
    }

    public void applyPostShaders(DepthScreen screen) {
        for (PostShader shader : postShaders) {
            shader.apply(screen, lastRenderOffsetX, lastRenderOffsetY, entities, worldObjects, player);
        }
    }

    private void checkInsidePlatform(Entity entity) {
        for (WorldObject worldObject : worldObjects) {
            if(worldObject.isSolid() && !worldObject.isSemiSolid() && worldObject.getCollisionBox().overlaps(entity.getCollisionBox())) {
                double area = entity.getCollisionBox().overlapArea(worldObject.getCollisionBox());
                if (!entity.hasEnqueuedMovement()) {
                    if (area > entity.getCollisionBox().area() * 0.5) {
                        entity.kill();
                    }
                    else {
                        Direction dir = worldObject.getCollisionBox().getNearestSide(entity.getCollisionBox().center());
                        switch (dir) {
                            case UP:
                                entity.getPosition().y = Math.nextDown(worldObject.getCollisionBox().y - entity.getCollisionBox().h);
                                break;
                            case DOWN:
                                entity.getPosition().y = Math.nextUp(worldObject.getCollisionBox().y + worldObject.getCollisionBox().h);
                                break;
                            case LEFT:
                                entity.getPosition().x = Math.nextDown(worldObject.getCollisionBox().x - entity.getCollisionBox().w);
                                break;
                            case RIGHT:
                                entity.getPosition().x = Math.nextUp(worldObject.getCollisionBox().x + worldObject.getCollisionBox().w);
                                break;
                        }
                    }
                }
            }
        }
    }

    List<WorldObject> collidedThisTick = new ArrayList<>();

    private void runPlatformCollisions(Entity entity) {
        AABB bbb = entity.getCollisionBox().createBBB(entity.getMovementStep());
        Vector2 soonestEntryTime = new Vector2(1, 1);
        boolean mayIngoreYFlag = false;
        Set<Integer> equalCollisionsX = new TreeSet<>();
        Set<Integer> equalCollisionsY = new TreeSet<>();
        for (WorldObject worldObject : worldObjects) {
            if (bbb.overlaps(worldObject.getCollisionBox())) {
                Vector2 entryTime = entity.getEntryTime(worldObject.getCollisionBox());
                Vector2 exitTime = entity.getExitTime(worldObject.getCollisionBox());
                if (entity.collides(entryTime, exitTime)) {
                    boolean solid = worldObject.isSolid() && !worldObject.isSemiSolid() ;
                    boolean passSemisolid = (worldObject.isSemiSolid() && entity.getPosition().y > worldObject.getCollisionBox().y) || !entity.standsOnSemisolid();
                    if(solid || (!passSemisolid && worldObject.isSemiSolid())) {
                        if (soonestEntryTime.x > entryTime.x && !worldObject.isSemiSolid()) {
                            soonestEntryTime.x = entryTime.x;
                            equalCollisionsX.clear();
                            equalCollisionsX.add(worldObject.getId());
                        }
                        else if (soonestEntryTime.x == entryTime.x) {
                            mayIngoreYFlag = true;
                            equalCollisionsX.add(worldObject.getId());
                        }
                        if (soonestEntryTime.y > entryTime.y) {
                            soonestEntryTime.y = entryTime.y;
                            equalCollisionsY.clear();
                            equalCollisionsY.add(worldObject.getId());
                        }
                        else if (soonestEntryTime.y == entryTime.y) {
                            equalCollisionsY.add(worldObject.getId());
                        }
                    }
                    collidedThisTick.add(worldObject);
                    worldObject.intersectEffect(entity, this, entity.getCollisionDirection(entryTime));
                }
                else if (entity.getCollisionBox().overlaps(worldObject.getCollisionBox())) {
                    collidedThisTick.add(worldObject);
                    worldObject.intersectEffect(entity, this);
                }
            }
        }
        if (mayIngoreYFlag && !equalCollisionsY.retainAll(equalCollisionsX))
            soonestEntryTime.y = 1.0;
        entity.applyCollision(soonestEntryTime);
        for (WorldObject worldObject : collidedThisTick) {
            worldObject.collisionEffect(entity, this);
        }
        collidedThisTick.clear();
    }

    public void addWorldObject(WorldObject worldObject) {
        worldObjects.add(worldObject);
    }

    public Direction playerOffScreen(int xOffset, int yOffset) {
        if (player.getPosition().y - yOffset < 0 - 0.5) {
            return Direction.UP;
        }
        else if (player.getPosition().y - yOffset> ScreenConstants.PLAY_SCREEN_HEIGHT - 0.5) {
            return Direction.DOWN;
        }

        if (player.getPosition().x - xOffset < 0 - 0.5) {
            return Direction.LEFT;
        }
        else if (player.getPosition().x - xOffset > ScreenConstants.PLAY_SCREEN_WIDTH - 0.5) {
            return Direction.RIGHT;
        }
        return Direction.NONE;
    }

     public void addEntity(Entity entity) {
        if (!entities.contains(entity))
            entities.add(entity);
     }

    public void removeEntity(Entity entity) {
        entitiesToRemove.add(entity);
    }

    public void addPostShader(PostShader shader, int order) {
        postShaders.add(order, shader);
    }

    public boolean playerNear(Vector2 position, double radius) {
        return M4th.screenDistanceSquared(position, player.getPosition()) < radius * radius;
    }
}
