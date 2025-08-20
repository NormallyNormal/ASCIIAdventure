package normallynormal.UI.Element;

import com.googlecode.lanterna.TextColor;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import org.tinylog.Logger;

public class MultiChoice extends UIComponent {
    String label;
    int labelWidth = 20;
    String[] options;
    int optionsWidth = 22;
    int selected = 0;

    @Override
    public void render(DepthScreen screen) {
        Vector2 offset = getOffset();
        String maxLabel = label.substring(0, labelWidth);
        if (label.length() != maxLabel.length()) {
            Logger.error("Label is too long for length " + labelWidth + ": " + label);
            label = maxLabel;
        }
        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x, (int)offset.y, getZOrder(), maxLabel, TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    }
}