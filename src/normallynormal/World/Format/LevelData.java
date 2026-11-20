package normallynormal.World.Format;

import java.util.List;

/**
 * A parsed .alf file: plain data with no game objects in it. {@link LevelBuilder} turns this into
 * real objects, so the file format and the game classes stay independent of each other.
 */
public record LevelData(
        String source,
        int formatVersion,
        double originX, double originY,
        double spawnX, double spawnY,
        List<ItemDef> postShaders,
        List<ItemDef> worldObjects,
        List<ItemDef> entities
) {
    /** One item line plus the '-' child lines under it. */
    public record ItemDef(int line, String type, AlfArgs args, List<ChildDef> children, RepeatDef repeat) {}

    /** A '-' line such as a controller or renderer (not -repeat, which is stored on the item). */
    public record ChildDef(int line, String type, AlfArgs args) {}

    /** `-repeat count:n dx:d dy:d`: n copies of the item, each offset by (dx, dy) from the previous. */
    public record RepeatDef(int line, int count, double dx, double dy) {}
}
