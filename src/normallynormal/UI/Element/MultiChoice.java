package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Input.Input;
import normallynormal.Input.InputHandler;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;
import org.tinylog.Logger;

import java.util.function.Consumer;

public class MultiChoice extends UIComponent {
    String label;
    int labelWidth = 20;
    String[] options;
    int optionsWidth = 22;
    int selected = 0;
    private Consumer<Integer> onChange;

    public MultiChoice(int x, int y, String label, String[] options, int defaultSelection) {
        this(x, y, label, options, defaultSelection, null);
    }

    public MultiChoice(int x, int y, String label, String[] options, int defaultSelection, Consumer<Integer> onChange) {
        pos = new Vector2(x, y);
        this.label = label;
        this.options = options;
        if (defaultSelection < 0 || defaultSelection >= options.length) {
            throw new IllegalArgumentException("defaultSelection outside range.");
        }
        selected = defaultSelection;
        this.onChange = onChange;
    }

    @Override
    public void process(double timeDelta, InputHandler input) {
        if (!highlighted) {
            return;
        }
        if (input.wasInputJustPressed(Input.UI_LEFT)) {
            selected = (selected - 1 + options.length) % options.length;
            if (onChange != null) onChange.accept(selected);
        }
        if (input.wasInputJustPressed(Input.UI_RIGHT)) {
            selected = (selected + 1) % options.length;
            if (onChange != null) onChange.accept(selected);
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
