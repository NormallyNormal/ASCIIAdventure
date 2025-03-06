package normallynormal.World;

import java.util.*;

import normallynormal.Render.Renderers.RainbowRenderer;
import normallynormal.Render.Renderers.SpikesRenderer;
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
import normallynormal.World.Platform.Controller.WatchingPlatformController;

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
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 30, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 25, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 20, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 15, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 10, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + 5, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + -5, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX + 10, startingLocationY + -10, 5, 1), currentId++));
        worldObjects.add(new SemisolidPlatform(new AABB(startingLocationX, startingLocationY + -15, 120, 1), currentId++));

        worldObjects.add(new StaticHazardObject(new AABB(startingLocationX + 125, startingLocationY + 34, 110, 1), currentId++));

        MoveableObject mo1 = new MoveableObject(new AABB(startingLocationX + 70, startingLocationY + 33, 10, 2), currentId++);
        mo1.createBasicController(Direction.RIGHT, 30, 0.1);
        worldObjects.add(mo1);

        MoveableObject wo1 = new MoveableObject(new AABB(startingLocationX + 70, startingLocationY + 30, 10, 2), currentId++);
        wo1.setController(new WatchingPlatformController(wo1, Direction.UP, 10, 0.1));
        worldObjects.add(wo1);

        worldObjects.add(new StaticObject(new AABB(startingLocationX + 106, startingLocationY + 30, 2, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(startingLocationX + 106, startingLocationY + 28, 2, 2), currentId++));

        MoveableObject mo2 = new MoveableObject(new AABB(startingLocationX + 125, startingLocationY + 32, 10, 2), currentId++);
        mo2.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo2);
        MoveableObject mo3 = new MoveableObject(new AABB(startingLocationX + 125 + 25, startingLocationY + 32, 10, 2), currentId++);
        mo3.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo3);
        MoveableObject mo4 = new MoveableObject(new AABB(startingLocationX + 125 + 50, startingLocationY + 32, 10, 2), currentId++);
        mo4.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo4);
        MoveableObject mo5 = new MoveableObject(new AABB(startingLocationX + 125 + 75, startingLocationY + 32, 10, 2), currentId++);
        mo5.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo5);
        MoveableObject mo6 = new MoveableObject(new AABB(startingLocationX + 125 + 70, startingLocationY + 30, 10, 2), currentId++);
        mo6.createBasicController(Direction.RIGHT, 25, 0.2);
        worldObjects.add(mo6);

        MoveableObject mo7 = new MoveableObject(new AABB(startingLocationX - 60, startingLocationY + 23, 10, 2), currentId++);
        mo7.createBasicController(Direction.RIGHT, 10, 0.1);
        mo7.setRenderer(new RainbowRenderer(mo7::getVisibilityBox));
        worldObjects.add(mo7);

        MoveableObject mo8 = new MoveableObject(new AABB(startingLocationX - 40, startingLocationY + 32, 10, 2), currentId++);
        mo8.createBasicController(Direction.UP, 9, 0.2);
        mo8.setRenderer(new RainbowRenderer(mo8::getVisibilityBox));
        worldObjects.add(mo8);

        entities.add(new Torch(startingLocationX + 30, startingLocationY + 30));

        entities.add(new NPC(startingLocationX + 60, startingLocationY + 34));

        GravityField gf1 = new GravityField(new AABB(startingLocationX - 70, startingLocationY + 25, 10, 10), currentId++, true);
        gf1.createBasicController(Direction.LEFT, 20, 0.1);
        //demo of custom renderers
        //gf1.setRenderer(new SpikesRenderer(gf1::getVisibilityBox));
        worldObjects.add(gf1);
        worldObjects.add(new GravityField(new AABB(startingLocationX - 120, startingLocationY + 15, 120, 3), currentId++, false));

        StaticObject s1 = new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 23, 30 , 2), currentId++);
        s1.setRenderer(new RainbowRenderer(s1::getVisibilityBox));
        worldObjects.add(s1);
        StaticObject s2 = new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 13, 30 , 2), currentId++);
        s2.setRenderer(new RainbowRenderer(s2::getVisibilityBox));
        worldObjects.add(s2);

        StaticObject s3 = new StaticObject(new AABB(startingLocationX - 90, startingLocationY + 6, 30 , 2), currentId++);
        s3.setRenderer(new RainbowRenderer(s3::getVisibilityBox));
        worldObjects.add(s3);

        worldObjects.add(new StaticHazardObject(new AABB(startingLocationX + 125, startingLocationY + -10, 110, 1), currentId++));

        worldObjects.add(new BouncyObject(new AABB(startingLocationX + 20, startingLocationY + 33, 5, 2), currentId++));
        worldObjects.add(new BouncyObject(new AABB(startingLocationX - 95, startingLocationY + 17, 5, 2), currentId++));

        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 20, startingLocationY + -16, 20, 1), currentId++, Direction.RIGHT, 10));
        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 40, startingLocationY + -25, 1, 10), currentId++, Direction.UP, 10));
        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 41, startingLocationY + -25, 20, 1), currentId++, Direction.RIGHT, 10));
        worldObjects.add(new ConveyorObject(new AABB(startingLocationX + 45, startingLocationY + -16, 20, 1), currentId++, Direction.LEFT, 10));
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
            if(worldObject.isSolid(entity) && worldObject.getCollisionBox().overlaps(entity.getCollisionBox())) {
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

    private record ObjectEntryPair(WorldObject worldObject, Direction entryDirection) { }

    List<ObjectEntryPair> collidedThisTick = new ArrayList<>();

    private void runPlatformCollisions(Entity entity) {
        AABB bbb = entity.getCollisionBox().createBBB(entity.getMovementStep());
        Vector2 soonestEntryTime = new Vector2(1, 1);
        boolean mayIngoreYFlag = false;
        boolean mayIngoreXFlag = false;
        Set<Integer> equalCollisionsX = new TreeSet<>();
        Set<Integer> equalCollisionsY = new TreeSet<>();
        for (WorldObject worldObject : worldObjects) {
            if (bbb.overlaps(worldObject.getCollisionBox())) {
                Vector2 entryTime = entity.getEntryTime(worldObject.getCollisionBox());
                Vector2 exitTime = entity.getExitTime(worldObject.getCollisionBox());
                if (entity.collides(entryTime, exitTime)) {
                    if(worldObject.isSolid(entity)) {
                        if (soonestEntryTime.x > entryTime.x) {
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
                            mayIngoreXFlag = true;
                            equalCollisionsY.add(worldObject.getId());
                        }
                    }
                    collidedThisTick.add(new ObjectEntryPair(worldObject, entity.getCollisionDirection(entryTime)));
                    worldObject.intersectEffect(entity, this, entity.getCollisionDirection(entryTime));
                }
                else if (entity.getCollisionBox().overlaps(worldObject.getCollisionBox())) {
                    collidedThisTick.add(new ObjectEntryPair(worldObject, entity.getCollisionDirection(entryTime)));
                    worldObject.intersectEffect(entity, this, entity.getCollisionDirection(entryTime));
                }
            }
        }
        if (mayIngoreYFlag && !equalCollisionsY.retainAll(equalCollisionsX))
            soonestEntryTime.y = 1.0;
        if (mayIngoreXFlag && !equalCollisionsX.retainAll(equalCollisionsY))
            soonestEntryTime.x = 1.0;
        entity.applyCollision(soonestEntryTime);
        for (ObjectEntryPair objectEntryPair : collidedThisTick) {
            objectEntryPair.worldObject.collisionEffect(entity, this, objectEntryPair.entryDirection);
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
