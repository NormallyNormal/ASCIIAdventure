package normallynormal.World;

import java.util.*;
import java.util.concurrent.Semaphore;

import normallynormal.Render.Renderers.AbstractRenderer;
import normallynormal.Render.Renderers.ConnectedTextureRenderer;
import normallynormal.Render.Renderers.RainbowRenderer;
import normallynormal.Render.Renderers.SpikesRenderer;
import normallynormal.Render.Shader.PostShader;
import normallynormal.Render.Shader.TorchlightPostShader;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Entity.NPC;
import normallynormal.World.Entity.Orb;
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
    Semaphore copyPermit = new Semaphore(1); // logic thread releases when done

    protected final List<WorldObject> worldObjects;
    protected final List<Entity> entities;
    private final Deque<Entity> entitiesToRemove;
    protected final Player player;

    protected final List<PostShader> postShaders;

    @SuppressWarnings("UnusedAssignment")
    public Level() {
        postShaders = new ArrayList<>();
        player = new Player();
        worldObjects = new ArrayList<>();
        entities = new ArrayList<>();
        entitiesToRemove = new ArrayDeque<>();
    }

    final ArrayList<Entity> onScreenEntities = new ArrayList<>();

    public synchronized void process(double timeDeltaSeconds, Input input) throws InterruptedException {
        //Wait until rendering values are copied.
        copyPermit.acquire();
        runPlatformCollisions(player);
        player.process(timeDeltaSeconds, input);
        checkInsidePlatform(player);

        onScreenEntities.clear();
        onScreenEntities.add(player);
        for (Entity entity : entities) {
            entity.process(timeDeltaSeconds, input);
            if (entity.isOnScreen()) {
                onScreenEntities.add(entity);
            }
        }
        for (Entity entity1 : onScreenEntities) {
            for (Entity entity2 : onScreenEntities) {
                AABB bbb = entity2.getCollisionBox().createBBB(entity2.getMovementStep());
                if (entity1 != entity2 & entity1.getCollisionBox().overlaps(bbb)) {
                    entity1.intersectEffect(entity2, this);
                }
            }
        }

        while (!entitiesToRemove.isEmpty()) {
            entities.remove(entitiesToRemove.pop());
        }

        for (WorldObject worldObject : worldObjects) {
            worldObject.process(timeDeltaSeconds, this);
        }
        copyPermit.release();
    }

    private int lastRenderOffsetX = 0;
    private int lastRenderOffsetY = 0;
    public synchronized void render(DepthScreen screen, int xOffset, int yOffset) throws InterruptedException {
        //Wait until done processing the current physics tick
        copyPermit.acquire();
        for (Entity entity : entities) {
            if (entity.isOnScreen())
                entity.copyForRender();
        }
        player.copyForRender();
        for (WorldObject worldObject : worldObjects) {
            if (worldObject.isOnScreen())
                worldObject.copyForRender();
        }

        ArrayList<Entity> renderEntities = new ArrayList<Entity>(entities);
        copyPermit.release();

        lastRenderOffsetX = xOffset;
        lastRenderOffsetY = yOffset;
        for (WorldObject worldObject : worldObjects) {
            if (worldObject.isOnScreen())
                worldObject.getRenderer().render(screen, xOffset, yOffset);
        }
        player.render(screen, xOffset, yOffset);
        for (Entity entity : renderEntities) {
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
