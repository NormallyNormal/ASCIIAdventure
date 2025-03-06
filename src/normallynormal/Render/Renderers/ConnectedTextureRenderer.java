package normallynormal.Render.Renderers;

import com.googlecode.lanterna.TextCharacter;
import normallynormal.Math.AABB;
import normallynormal.Math.Direction;
import normallynormal.Render.DepthScreen;

import java.util.ArrayList;
import java.util.function.Supplier;

import static normallynormal.Render.Renderers.DefaultRenderer.*;

public class ConnectedTextureRenderer extends AbstractRenderer {
    ArrayList<Supplier<AABB>> visibilityBoxSuppliers;

    public ConnectedTextureRenderer() {
        super(null);
        visibilityBoxSuppliers = new ArrayList<>();
    }

    public void addVisibiliyBoxSupplier(Supplier<AABB> visibilityBoxSupplier) {
        visibilityBoxSuppliers.add(visibilityBoxSupplier);
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        AABB firstBox = visibilityBoxSuppliers.get(0).get();
        AABB broadBox = new AABB(firstBox.x, firstBox.y, firstBox.w, firstBox.h);
        for (Supplier<AABB> visibilityBoxSupplier : visibilityBoxSuppliers) {
            broadBox.expandToContain(visibilityBoxSupplier.get());
        }
        for (int i = 0; i <= broadBox.w - 1; i++) {
            for (int j = 0; j <= broadBox.h - 1; j++) {
                double x = (int)broadBox.x + i + 0.5;
                double y = (int)broadBox.y + j + 0.5;
                boolean here = false;
                boolean uphere = false;
                boolean downhere = false;
                boolean lefthere = false;
                boolean righthere = false;
                boolean corners1 = false;
                boolean corners2 = false;
                boolean corners3 = false;
                boolean corners4 = false;
                for (Supplier<AABB> visibilityBoxSupplier : visibilityBoxSuppliers) {
                    here = here | visibilityBoxSupplier.get().contains(x, y);
                    uphere = uphere | visibilityBoxSupplier.get().contains(x, y + Direction.UP.toMovement());
                    downhere = downhere | visibilityBoxSupplier.get().contains(x, y + Direction.DOWN.toMovement());
                    lefthere = lefthere | visibilityBoxSupplier.get().contains(x + Direction.LEFT.toMovement(), y);
                    righthere = righthere | visibilityBoxSupplier.get().contains(x + Direction.RIGHT.toMovement(), y);
                    corners1 = corners1 | visibilityBoxSupplier.get().contains(x + 1, y + 1);
                    corners2 = corners2 | visibilityBoxSupplier.get().contains(x - 1, y + 1);
                    corners3 = corners3 | visibilityBoxSupplier.get().contains(x + 1, y - 1);
                    corners4 = corners4 | visibilityBoxSupplier.get().contains(x - 1, y - 1);
                }
                boolean corners = corners1 & corners2 & corners3 & corners4;
                if (here) {
                    TextCharacter filler;
                    if(uphere && downhere && lefthere && righthere) {
                        if (!corners)
                            filler = CORNER;
                        else
                            filler = EMPTY;
                    }
                    else if (uphere && downhere) {
                        filler = VERTICAL_BORDER;
                    }
                    else if (lefthere && righthere) {
                        filler = HORIZONTAL_BORDER;
                    }
                    else {
                        filler = CORNER;
                    }
                    screen.setCharacterWithDepth((int)broadBox.x + i, (int)broadBox.y + j, xOffset, yOffset, 0, filler);
                }
            }
        }
    }
}
