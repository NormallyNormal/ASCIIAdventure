package main.World;

import main.Render.DepthScreen;

public interface RenderObject {
    void render(DepthScreen screen, int xOffset, int yOffset);
}
