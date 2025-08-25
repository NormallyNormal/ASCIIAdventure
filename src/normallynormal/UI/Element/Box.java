package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Input.Input;
import normallynormal.Math.AABB;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Settings.Keybinds;

public abstract class Box extends UIComponent {
    protected AABB aabb;
    int selectedChild = 0;

    public Box(int x, int y, int w, int h) {
        this.aabb = new AABB(x, y, w, h);
        this.setPos(new Vector2(x, y));
    }

    public void reset() {
        if (childCount() > selectedChild) getChild(selectedChild).highlighted = false;
        selectedChild = 0;
    }

    protected static final TextCharacter VERTICAL_BORDER =
            new TextCharacter('│', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT);
    protected static final TextCharacter HORIZONTAL_BORDER =
            new TextCharacter('─', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT);

    protected static final TextCharacter TOP_LEFT_CORNER =
            new TextCharacter('┌', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT);
    protected static final TextCharacter TOP_RIGHT_CORNER =
            new TextCharacter('┐', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT);
    protected static final TextCharacter BOTTOM_LEFT_CORNER =
            new TextCharacter('└', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT);
    protected static final TextCharacter BOTTOM_RIGHT_CORNER =
            new TextCharacter('┘', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT);

    protected static final TextCharacter EMPTY =
            new TextCharacter(' ', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK_BRIGHT);

    @Override
    public void render(DepthScreen screen) {
        for (int i = 0; i < aabb.w; i++) {
            for (int j = 0; j < aabb.h; j++) {
                TextCharacter filler = EMPTY;

                if (i == 0 && j == 0) {
                    filler = TOP_LEFT_CORNER;
                } else if (i == aabb.w - 1 && j == 0) {
                    filler = TOP_RIGHT_CORNER;
                } else if (i == 0 && j == aabb.h - 1) {
                    filler = BOTTOM_LEFT_CORNER;
                } else if (i == aabb.w - 1 && j == aabb.h - 1) {
                    filler = BOTTOM_RIGHT_CORNER;
                } else if (i == 0 || i == aabb.w - 1) {
                    filler = VERTICAL_BORDER;
                } else if (j == 0 || j == aabb.h - 1) {
                    filler = HORIZONTAL_BORDER;
                }

                screen.setCharacterWithDepth(
                        (int) aabb.x + i,
                        (int) aabb.y + j,
                        0, 0, getZOrder(),
                        filler
                );
            }
        }
        super.render(screen);
    }

    @Override
    public void process(double timeDelta, Input input) {
        int old = selectedChild;
        if (input.wasKeyJustPressed(Keybinds.ui_up) && selectedChild > 0) {
            selectedChild--;
        }
        else if (input.wasKeyJustPressed(Keybinds.ui_down) && selectedChild < childCount() - 1) {
            selectedChild++;
        }
        if (selectedChild < childCount()) {
            getChild(selectedChild).process(timeDelta, input);
        }
        if (childCount() > old && old != selectedChild) getChild(old).highlighted = false;
        if (childCount() > selectedChild) getChild(selectedChild).highlighted = true;
    }

    public abstract void addElements();
}
