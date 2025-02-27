package normallynormal.Render.Shader;

import normallynormal.Render.DepthScreen;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Entity.Player;
import normallynormal.World.Platform.WorldObject;

import java.util.List;

public interface PostShader {
    public void apply(DepthScreen depthScreen, int xOffset, int yOffset, List<Entity> entityList, List<WorldObject> worldObjects, Player player);
}
