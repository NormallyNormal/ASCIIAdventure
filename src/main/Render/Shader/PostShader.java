package main.Render.Shader;

import main.Render.DepthScreen;
import main.World.Entity.Entity;
import main.World.Entity.Player;
import main.World.Platform.WorldObject;

import java.util.List;

public interface PostShader {
    public void apply(DepthScreen depthScreen, int xOffset, int yOffset, List<Entity> entityList, List<WorldObject> worldObjects, Player player);
}
