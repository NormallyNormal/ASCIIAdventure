package normallynormal.World.Levels;

import normallynormal.Game;
import normallynormal.Math.Vector2;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Format.AlfException;
import normallynormal.World.Format.AlfParser;
import normallynormal.World.Format.LevelBuilder;
import normallynormal.World.Format.LevelData;
import normallynormal.World.Level;
import normallynormal.World.Platform.WorldObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/** A level read from an .alf file, e.g. {@code LoadedLevel.load("/resources/levels/dev_level.alf")}. */
public class LoadedLevel extends Level {
    private final Map<String, WorldObject> namedObjects;
    private final Map<String, Entity> namedEntities;

    public static LoadedLevel load(String resourcePath) {
        try (InputStream in = Game.class.getResourceAsStream(resourcePath)) {
            if (in == null) throw new AlfException(resourcePath, 0, "level file not found on the classpath");
            return new LoadedLevel(AlfParser.parse(in, resourcePath));
        } catch (IOException e) {
            throw new AlfException(resourcePath, "could not read level file", e);
        }
    }

    public LoadedLevel(LevelData data) {
        super();
        LevelBuilder.Result built = LevelBuilder.build(data);
        Vector2 spawn = built.spawn();
        player.getPosition().x = spawn.x;
        player.getPosition().y = spawn.y;
        player.setSpawnPosition(new Vector2(spawn.x, spawn.y));
        worldObjects.addAll(built.worldObjects());
        entities.addAll(built.entities());
        postShaders.addAll(built.postShaders());
        namedObjects = built.namedObjects();
        namedEntities = built.namedEntities();
    }

    /** The world object declared with {@code id:<name>}, or null. */
    public WorldObject getNamedObject(String name) {
        return namedObjects.get(name);
    }

    /** The entity declared with {@code id:<name>}, or null. */
    public Entity getNamedEntity(String name) {
        return namedEntities.get(name);
    }
}
