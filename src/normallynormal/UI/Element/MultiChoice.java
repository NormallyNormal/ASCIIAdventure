package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Input.Input;
import normallynormal.Math.M4th;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import normallynormal.Settings.Keybinds;
import org.tinylog.Logger;

public class MultiChoice extends UIComponent {
    String label;
    int labelWidth = 20;
    String[] options;
    int optionsWidth = 22;
    int selected = 0;

    public MultiChoice(int x, int y, String label, String[] options, int defaultSelection) {
        pos = new Vector2(x, y);
        this.label = label;
        this.options = options;
        if (defaultSelection < 0 || defaultSelection >= options.length) {
            throw new IllegalArgumentException("defaultSelection outside range.");
        }
        selected = defaultSelection;
    }

    @Override
    public void process(double timeDelta, Input input) {
        if (!highlighted) {
            return;
        }
        if (input.wasKeyJustPressed(Keybinds.ui_left)) {
            selected = (selected - 1 + options.length) % options.length;
        }
        if (input.wasKeyJustPressed(Keybinds.ui_right)) {
            selected = (selected + 1) % options.length;
        }
    }

    @Override
    public void render(DepthScreen screen) {
        Vector2 offset = getOffset();
        String maxLabel = label.substring(0, Math.min(labelWidth, label.length()));
        if (label.length() != maxLabel.length()) {
            Logger.error("Multi choice label is too long for length " + labelWidth + ": " + label);
            label = maxLabel;
        }

        TextColor backgroundColor = highlighted ? highlightColor : TransparentColor.TRANSPARENT;
        TextCharacter arrowLeft = new TextCharacter('<', textColor, backgroundColor);
        TextCharacter arrowRight = new TextCharacter('>', textColor, backgroundColor);

        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x, (int)offset.y, getZOrder(), maxLabel, textColor, backgroundColor);
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth, (int)offset.y, getZOrder(), arrowLeft);
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + optionsWidth - 1, (int)offset.y, getZOrder(), arrowRight);

        String valueString = options[selected];
        int valuePos = labelWidth + optionsWidth/2 - valueString.length()/2;
        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x + valuePos, (int)offset.y, getZOrder(), valueString, textColor, backgroundColor);
    }
}