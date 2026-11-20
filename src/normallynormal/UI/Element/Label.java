package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Input.Input;
import normallynormal.Input.InputHandler;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.UI.Settings.PauseManager;
import org.tinylog.Logger;

public class Label extends UIComponent{
    String label;
    int labelWidth = 42;

    public Label(int x, int y, String label) {
        this.label = label;
        this.pos = new Vector2(x, y);
    }

    @Override
    public void render(DepthScreen screen) {
        Vector2 offset = getOffset();
        String maxLabel = label.substring(0, Math.min(labelWidth, label.length()));
        if (label.length() != maxLabel.length()) {
            Logger.error("Label label is too long for length " + labelWidth + ": " + label);
            label = maxLabel;
        }
        TextColor backgroundColor = highlighted ? highlightColor : TransparentColor.TRANSPARENT;
        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x + labelWidth/2 - label.length()/2 + 1, (int)offset.y, getZOrder(), maxLabel, textColor, backgroundColor);
    }
}