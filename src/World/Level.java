package World;

import java.util.*;

import World.Entity.Entity;
import World.Entity.Player;
import Input.Input;
import World.Platform.*;
import Render.DepthScreen;
import Math.AABB;
import Math.Vector2;
import Math.Direction;
import Constants.ScreenConstants;

public class Level {
    private final List<WorldObject> worldObjects;
    private final List<Entity> entities;
    private final Player player;

    public final Map<Direction, Level> connectedLevels = new EnumMap<>(Direction.class);

    @SuppressWarnings("UnusedAssignment")
    public Level() {
        player = new Player();
        worldObjects = new ArrayList<>();
        entities = new ArrayList<>();
        int currentId = 0;
        worldObjects.add(new MovingObject(new AABB(65, 19, 5, 1), currentId++, 0, 10, 0.25));
        worldObjects.add(new MovingObject(new AABB(75, 19, 5, 1), currentId++, 0, 10, 0.2));
        worldObjects.add(new StaticObject(new AABB(10, 5, 5, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(55, 20, 50, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(5, 20, 50, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(70, 15, 5, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(70, 10, 5, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(35, 17, 5, 3), currentId++));
        worldObjects.add(new StaticObject(new AABB(20, 10, 20, 1), currentId++));
        worldObjects.add(new StaticObject(new AABB(35, 5, 2, 5), currentId++));
        worldObjects.add(new StaticObject(new AABB(20, 15, 10, 1), currentId++));
        worldObjects.add(new StaticHazardObject(new AABB(20, 19, 5, 1), currentId++));
        worldObjects.add(new MovingObject(new AABB(20, 5, 5, 5), currentId++, 10, 0, 0.4));
        worldObjects.add(new StaticObject(new AABB(28, 2, 5, 3), currentId++));
        connectedLevels.put(Direction.DOWN, this);
        connectedLevels.put(Direction.LEFT, this);
        connectedLevels.put(Direction.RIGHT, this);
        connectedLevels.put(Direction.UP, this);
    }

    public void process(double timeDeltaSeconds, Input input) {
        player.process(timeDeltaSeconds, input);
        checkInsidePlatform(player);
        for (WorldObject worldObject : worldObjects) {
            worldObject.process(timeDeltaSeconds, this);
        }
        runPlatformCollisions(player);
    }

    public void render(DepthScreen screen, int xOffset, int yOffset) {
        for (WorldObject worldObject : worldObjects) {
            worldObject.render(screen, xOffset, yOffset);
        }
        player.render(screen, xOffset, yOffset);
    }

    private void checkInsidePlatform(Entity entity) {
        for (WorldObject worldObject : worldObjects) {
            if(worldObject.isSolid() && worldObject.getCollisionBox().overlaps(entity.getCollisionBox())) {
                double area = entity.getCollisionBox().overlapArea(worldObject.getCollisionBox());
                if (area > entity.getCollisionBox().area() * 0.7) {
                    entity.kill();
                }
            }
        }
    }

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
                    if(worldObject.isSolid()) {
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
                            equalCollisionsY.add(worldObject.getId());
                        }
                    }
                    worldObject.collisionEffect(entity, this);
                }
                else if (entity.getCollisionBox().overlaps(worldObject.getCollisionBox())) {
                    worldObject.collisionEffect(entity, this);
                }
            }
        }
        if (mayIngoreYFlag && !equalCollisionsY.retainAll(equalCollisionsX))
            soonestEntryTime.y = 1.0;
        entity.applyCollision(soonestEntryTime);
    }

    public void addWorldObject(WorldObject worldObject) {
        worldObjects.add(worldObject);
    }

    public Direction playerOutOfBounds() {
        if (player.getPosition().y < 0 - 0.5) {
            return Direction.UP;
        }
        else if (player.getPosition().y > ScreenConstants.PLAY_SCREEN_HEIGHT - 0.5) {
            return Direction.DOWN;
        }

        if (player.getPosition().x < 0 - 0.5) {
            return Direction.LEFT;
        }
        else if (player.getPosition().x > ScreenConstants.PLAY_SCREEN_WIDTH - 0.5) {
            return Direction.RIGHT;
        }
        return Direction.NONE;
    }

    public void loopPlayer() {
        Direction playerOutOfBoundsDirection = playerOutOfBounds();
        switch (playerOutOfBoundsDirection) {
            case UP -> player.getPosition().y += ScreenConstants.PLAY_SCREEN_HEIGHT;
            case DOWN -> player.getPosition().y -= ScreenConstants.PLAY_SCREEN_HEIGHT;
            case LEFT -> player.getPosition().x += ScreenConstants.PLAY_SCREEN_WIDTH;
            case RIGHT -> player.getPosition().x -= ScreenConstants.PLAY_SCREEN_WIDTH;
        }
    }
}
