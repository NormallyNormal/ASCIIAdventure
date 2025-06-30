package normallynormal.World;

import normallynormal.Render.DepthScreen;

public interface RenderObject {
    public void copyForRender();

    void render(DepthScreen screen, int xOffset, int yOffset);

    boolean isOnScreen();
}
