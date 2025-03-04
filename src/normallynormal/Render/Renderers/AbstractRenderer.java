package normallynormal.Render.Renderers;

import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;

import java.util.function.Supplier;

public abstract class AbstractRenderer {
    protected final Supplier<AABB> visibilityBoxSupplier;

    protected AbstractRenderer(Supplier<AABB> visibilityBoxSupplier) {
        this.visibilityBoxSupplier = visibilityBoxSupplier;
    }

    public abstract void render(DepthScreen screen, int xOffset, int yOffset);
}
