package Render.Shader;

import Render.DepthScreen;
import World.Entity.Entity;
import World.Entity.Player;
import World.Platform.WorldObject;

import java.util.List;

public interface PostShader {
    public void apply(DepthScreen depthScreen, int xOffset, int yOffset, List<Entity> entityList, List<WorldObject> worldObjects, Player player);
}
