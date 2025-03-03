package normallynormal.Render.Renderers;

import normallynormal.Render.DepthScreen;

public abstract class AbstractRenderer {
    public abstract void render(DepthScreen screen, int xOffset, int yOffset);
}
