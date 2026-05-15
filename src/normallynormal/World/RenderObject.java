package normallynormal.World;

import normallynormal.Render.DepthScreen;

public interface RenderObject {
    /**
     * Snapshots render-relevant state into a volatile {@code renderState} field. Called by the
     * physics thread at the end of each tick. The render thread reads {@code renderState} when
     * drawing — the volatile publish gives it a consistent immutable view of the entity at the
     * moment of the snapshot.
     */
    void copyForRender();

    void render(DepthScreen screen, int xOffset, int yOffset);

    /** Live (physics-time) on-screen check, used by physics for collision filtering. */
    boolean isOnScreen();

    /** On-screen flag captured at last {@link #copyForRender()}; used by the render thread. */
    boolean isRenderOnScreen();
}
