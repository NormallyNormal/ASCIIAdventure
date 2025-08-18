package normallynormal.UI.Element;

import normallynormal.Math.AABB;
import normallynormal.Math.Vector2;

public class Box extends UIElement {
    AABB aabb;

    public Box(int x, int y, int w, int h) {
        this.aabb = new AABB(x, y, w, h);
        this.setPos(new Vector2(x, y));
    }
}
