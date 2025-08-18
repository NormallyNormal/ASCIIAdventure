package normallynormal.UI.Element;

import normallynormal.Input.Input;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;

import java.util.List;

public abstract class UIElement {
    private UIElement parent;
    private List<UIElement> children;
    protected Vector2 pos = new Vector2(0,0);
    protected final Vector2 renderPos = new Vector2(0,0);
    private int zOrder = 0;

    public void process(double timeDelta, Input input) {

    }

    public void render(DepthScreen screen) {

    }

    public void copyForRender() {
        pos.deepCopy(renderPos);
    }

    public void setParent(UIElement parent, boolean autoZ) {
        this.parent = parent;
        if (autoZ) zOrder = parent.zOrder + 1;
    }

    public UIElement getParent() {
        return parent;
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    public int getZOrder() {
        return zOrder;
    }

    public void setZOrder(int zOrder) {
        this.zOrder = zOrder;
    }

    public Vector2 getOffset() {
        Vector2 offset = new Vector2();
        if (parent != null) {
            offset.add(parent.getPos());
            offset.add(parent.getOffset());
        }
        return offset;
    }
}
