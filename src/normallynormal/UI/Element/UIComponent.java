package normallynormal.UI.Element;

import com.googlecode.lanterna.TextColor;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;

import java.util.ArrayList;
import java.util.List;

public class UIComponent extends UIBase {
    private UIComponent parent;
    private final List<UIComponent> children = new ArrayList<>();
    protected Vector2 pos = new Vector2(0,0);
    private int zOrder = 1001;

    public boolean highlighted = false;
    protected TextColor textColor = TextColor.ANSI.WHITE;
    protected TextColor highlightColor = TextColor.ANSI.BLUE_BRIGHT;

    public void setParent(UIComponent parent, boolean autoZ) {
        this.parent = parent;
        if (autoZ) zOrder = parent.zOrder + 1;
    }

    public UIComponent getParent() {
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

    public void render(DepthScreen screen) {
        for (UIComponent child : children) {
            child.render(screen);
        }
    }

    protected void addChild(UIComponent child) {
        children.add(child);
        child.setParent(this, true);
    }

    protected int childCount() {
        return children.size();
    }

    protected UIComponent getChild(int idx) {
        return children.get(idx);
    }
}
