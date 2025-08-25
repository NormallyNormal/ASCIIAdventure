package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.GameManager;
import normallynormal.Input.Input;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.Settings.Keybinds;
import normallynormal.UI.Settings.PauseManager;
import org.tinylog.Logger;

public class Button extends UIComponent{
    String label;
    int labelWidth = 40;
    Box show;

    public Button(int x, int y, String label, Box show) {
        this.show = show;
        this.label = label;
        this.pos = new Vector2(x, y);
    }

    @Override
    public void process(double timeDelta, Input input) {
        if (input.wasKeyJustPressed(Keybinds.ui_select)) {
            PauseManager.showMenu(show);
        }
    }

    @Override
    public void render(DepthScreen screen) {
        Vector2 offset = getOffset();
        String maxLabel = label.substring(0, Math.min(labelWidth, label.length()));
        if (label.length() != maxLabel.length()) {
            Logger.error("Slider label is too long for length " + labelWidth + ": " + label);
            label = maxLabel;
        }

        TextColor backgroundColor = highlighted ? highlightColor : TransparentColor.TRANSPARENT;
        TextCharacter left = new TextCharacter('[', textColor, backgroundColor);
        TextCharacter right = new TextCharacter(']', textColor, backgroundColor);

        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth/2 - label.length()/2, (int)offset.y, getZOrder(), left);
        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x + labelWidth/2 - label.length()/2 + 1, (int)offset.y, getZOrder(), maxLabel, textColor, backgroundColor);
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth/2 + (label.length()+1)/2 + 1, (int)offset.y, getZOrder(), right);
    }
}