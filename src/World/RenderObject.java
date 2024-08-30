package World;

import Render.DepthScreen;

public interface RenderObject {
    void render(DepthScreen screen, int xOffset, int yOffset);
}
