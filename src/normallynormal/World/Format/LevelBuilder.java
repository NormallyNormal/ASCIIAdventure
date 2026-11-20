package normallynormal.World.Format;

import normallynormal.Math.AABB;
import normallynormal.Math.Vector2;
import normallynormal.Render.Renderers.AbstractRenderer;
import normallynormal.Render.Shader.PostShader;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Format.LevelData.ChildDef;
import normallynormal.World.Format.LevelData.ItemDef;
import normallynormal.World.Format.LevelData.RepeatDef;
import normallynormal.World.Platform.MoveableObject;
import normallynormal.World.Platform.WorldObject;

import java.util.*;

/** Turns a parsed {@link LevelData} into real game objects using {@link LevelRegistry}. */
public final class LevelBuilder {
    public record Result(
            Vector2 spawn,
            List<WorldObject> worldObjects,
            List<Entity> entities,
            List<PostShader> postShaders,
            Map<String, WorldObject> namedObjects,
            Map<String, Entity> namedEntities
    ) {}

    private final LevelData data;
    private final List<WorldObject> worldObjects = new ArrayList<>();
    private final List<Entity> entities = new ArrayList<>();
    private final List<PostShader> postShaders = new ArrayList<>();
    private final Map<String, WorldObject> namedObjects = new HashMap<>();
    private final Map<String, Entity> namedEntities = new HashMap<>();
    private final Map<String, AbstractRenderer> rendererGroups = new HashMap<>();
    private int nextId = 0;

    private LevelBuilder(LevelData data) {
        this.data = data;
    }

    public static Result build(LevelData data) {
        return new LevelBuilder(data).build();
    }

    private Result build() {
        for (ItemDef item : data.postShaders()) {
            buildShader(item);
        }
        for (ItemDef item : data.worldObjects()) {
            int copies = item.repeat() == null ? 1 : item.repeat().count();
            for (int i = 0; i < copies; i++) buildObject(item, i);
        }
        for (ItemDef item : data.entities()) {
            int copies = item.repeat() == null ? 1 : item.repeat().count();
            for (int i = 0; i < copies; i++) buildEntity(item, i);
        }
        return new Result(
                new Vector2(data.originX() + data.spawnX(), data.originY() + data.spawnY()),
                worldObjects, entities, postShaders, namedObjects, namedEntities);
    }

    private void buildShader(ItemDef item) {
        requireNoChildren(item);
        postShaders.add(LevelRegistry.shader(item.type(), item.args()).create(item.args()));
        item.args().requireAllUsed(item.type());
    }

    private void buildObject(ItemDef item, int copy) {
        AlfArgs a = item.args();
        double[] pos = position(item, copy);
        AABB box = new AABB(pos[0], pos[1], a.number("w"), a.number("h"));

        int id = nextId++;
        WorldObject object = LevelRegistry.object(item.type(), a).create(box, id, a);

        for (ChildDef child : item.children()) {
            attachChild(object, child);
        }
        registerName(item, namedObjects, object);
        a.requireAllUsed(item.type());
        worldObjects.add(object);
    }

    private void buildEntity(ItemDef item, int copy) {
        requireNoChildren(item);
        AlfArgs a = item.args();
        double[] pos = position(item, copy);
        Entity entity = LevelRegistry.entity(item.type(), a).create(a.toInt("x", pos[0]), a.toInt("y", pos[1]), a);
        registerName(item, namedEntities, entity);
        a.requireAllUsed(item.type());
        entities.add(entity);
    }

    /** Absolute position of copy number {@code copy}: origin + (x, y) + copy * (dx, dy). */
    private double[] position(ItemDef item, int copy) {
        RepeatDef r = item.repeat();
        double dx = r == null ? 0 : r.dx() * copy;
        double dy = r == null ? 0 : r.dy() * copy;
        return new double[]{
                data.originX() + item.args().number("x") + dx,
                data.originY() + item.args().number("y") + dy
        };
    }

    private void attachChild(WorldObject object, ChildDef child) {
        AlfArgs a = child.args();
        if (child.type().equals("renderer")) {
            String type = a.string("type");
            String group = a.string("group", null);
            LevelRegistry.renderer(type, a).apply(object, group, rendererGroups, a);
            a.requireAllUsed("renderer " + type);
            return;
        }
        LevelRegistry.ControllerFactory controller = LevelRegistry.controller(child.type());
        if (controller == null) {
            throw a.error("unknown child '-" + child.type() + "' (expected -renderer, -repeat or a controller)");
        }
        if (!(object instanceof MoveableObject moveable)) {
            throw a.error("'-" + child.type() + "' only works on moveable objects");
        }
        controller.attach(moveable, a);
        a.requireAllUsed(child.type());
    }

    private <T> void registerName(ItemDef item, Map<String, T> into, T value) {
        String name = item.args().string("id", null);
        if (name == null) return;
        if (namedObjects.containsKey(name) || namedEntities.containsKey(name)) {
            throw item.args().error("duplicate id '" + name + "'");
        }
        into.put(name, value);
    }

    private void requireNoChildren(ItemDef item) {
        if (!item.children().isEmpty()) {
            ChildDef c = item.children().get(0);
            throw c.args().error("'" + item.type() + "' does not take '-" + c.type() + "' lines");
        }
    }
}
