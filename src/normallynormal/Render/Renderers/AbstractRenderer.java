package normallynormal.Render.Renderers;

import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Util.BufferedSupplier;

import java.util.function.Supplier;

public abstract class AbstractRenderer {
    protected final BufferedSupplier<AABB> visibilityBoxSupplier;

    protected AbstractRenderer(Supplier<AABB> visibilityBoxSupplier) {
        this.visibilityBoxSupplier = new BufferedSupplier<>(visibilityBoxSupplier);
    }

    public abstract void render(DepthScreen screen, int xOffset, int yOffset);

    public void copyForRender() {
        visibilityBoxSupplier.buffer();
    }
}
