package normallynormal.World.Format;

import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Render.Renderers.AbstractRenderer;
import normallynormal.Render.Renderers.ConnectedTextureRenderer;
import normallynormal.Render.Renderers.RainbowRenderer;
import normallynormal.Render.Shader.PostShader;
import normallynormal.Render.Shader.TorchlightPostShader;
import normallynormal.World.Entity.Decoration.Torch;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Entity.NPC;
import normallynormal.World.Entity.Orb;
import normallynormal.World.Platform.*;
import normallynormal.World.Platform.Controller.BasicPlatformController;
import normallynormal.World.Platform.Controller.WatchingPlatformController;

import java.util.Map;
import java.util.TreeMap;

/**
 * Maps the type names used in .alf files to code that builds the real objects. Adding a new
 * kind of platform, entity, controller or renderer to the format means adding one line here.
 *
 * Factories only read the keys specific to their type; position, size and id are handled by
 * {@link LevelBuilder}. Keys a factory never reads are reported as unknown.
 */
public final class LevelRegistry {
    @FunctionalInterface public interface ObjectFactory { WorldObject create(AABB box, int id, AlfArgs args); }
    @FunctionalInterface public interface EntityFactory { Entity create(int x, int y, AlfArgs args); }
    @FunctionalInterface public interface ShaderFactory { PostShader create(AlfArgs args); }
    @FunctionalInterface public interface ControllerFactory { void attach(MoveableObject target, AlfArgs args); }

    /**
     * Builds and installs a renderer on {@code target}. {@code group} is the optional group name; renderers
     * that cannot be shared must reject it. {@code groups} holds shared renderers created so far.
     */
    @FunctionalInterface
    public interface RendererFactory {
        void apply(WorldObject target, String group, Map<String, AbstractRenderer> groups, AlfArgs args);
    }

    private static final Map<String, ObjectFactory> OBJECTS = new TreeMap<>();
    private static final Map<String, EntityFactory> ENTITIES = new TreeMap<>();
    private static final Map<String, ShaderFactory> SHADERS = new TreeMap<>();
    private static final Map<String, ControllerFactory> CONTROLLERS = new TreeMap<>();
    private static final Map<String, RendererFactory> RENDERERS = new TreeMap<>();

    public static void registerObject(String type, ObjectFactory f) { OBJECTS.put(type, f); }
    public static void registerEntity(String type, EntityFactory f) { ENTITIES.put(type, f); }
    public static void registerShader(String type, ShaderFactory f) { SHADERS.put(type, f); }
    public static void registerController(String type, ControllerFactory f) { CONTROLLERS.put(type, f); }
    public static void registerRenderer(String type, RendererFactory f) { RENDERERS.put(type, f); }

    public static ObjectFactory object(String type, AlfArgs at) { return lookup(OBJECTS, type, "object", at); }
    public static EntityFactory entity(String type, AlfArgs at) { return lookup(ENTITIES, type, "entity", at); }
    public static ShaderFactory shader(String type, AlfArgs at) { return lookup(SHADERS, type, "post shader", at); }
    public static RendererFactory renderer(String type, AlfArgs at) { return lookup(RENDERERS, type, "renderer", at); }

    /** Controllers are attached with their own name as the '-' line, e.g. -basic_controller. */
    public static ControllerFactory controller(String type) { return CONTROLLERS.get(type); }

    private static <T> T lookup(Map<String, T> map, String type, String kind, AlfArgs at) {
        T f = map.get(type);
        if (f == null) throw at.error("unknown " + kind + " type '" + type + "' (known: " + String.join(", ", map.keySet()) + ")");
        return f;
    }

    static {
        // World objects
        registerObject("static", (box, id, a) -> new StaticObject(box, id));
        registerObject("semisolid", (box, id, a) -> new SemisolidPlatform(box, id));
        registerObject("hazard", (box, id, a) -> new StaticHazardObject(box, id));
        registerObject("moveable", (box, id, a) -> new MoveableObject(box, id));
        registerObject("bouncy", (box, id, a) -> new BouncyObject(box, id));
        registerObject("gravity", (box, id, a) -> new GravityField(box, id, a.bool("up")));
        registerObject("conveyor", (box, id, a) ->
                new ConveyorObject(box, id, direction(a, "dir"), a.number("speed")));

        // Entities
        registerEntity("torch", (x, y, a) -> new Torch(x, y));
        registerEntity("npc", (x, y, a) -> new NPC(x, y));
        registerEntity("orb", (x, y, a) -> new Orb(x, y, a.enumValue("kind", Orb.OrbType.class)));

        // Post shaders
        registerShader("torchlight", a -> new TorchlightPostShader());

        // Platform controllers
        registerController("basic_controller", (target, a) ->
                target.setController(new BasicPlatformController(target,
                        direction(a, "dir"), a.integer("steps"), a.number("pause"))));
        registerController("watching_controller", (target, a) ->
                target.setController(new WatchingPlatformController(target,
                        direction(a, "dir"), a.integer("steps"), a.number("pause"))));

        // Renderers
        registerRenderer("rainbow", (target, group, groups, a) -> {
            if (group != null) throw a.error("renderer 'rainbow' does not support group");
            target.setRenderer(new RainbowRenderer(target::getVisibilityBox));
        });
        registerRenderer("connected", (target, group, groups, a) -> {
            ConnectedTextureRenderer r;
            if (group == null) {
                r = new ConnectedTextureRenderer();
            } else {
                AbstractRenderer existing = groups.computeIfAbsent(group, g -> new ConnectedTextureRenderer());
                if (!(existing instanceof ConnectedTextureRenderer c)) {
                    throw a.error("group '" + group + "' is already used by a different renderer type");
                }
                r = c;
            }
            r.addVisibiliyBoxSupplier(target::getVisibilityBox);
            target.setRenderer(r);
        });
    }

    private static Direction direction(AlfArgs a, String key) {
        Direction d = a.enumValue(key, Direction.class);
        if (d == Direction.NONE) throw a.error("key '" + key + "' must be up, down, left or right");
        return d;
    }

    private LevelRegistry() {}
}
