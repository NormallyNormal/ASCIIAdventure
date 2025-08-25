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

public class Slider extends UIComponent {
    String label;
    int labelWidth = 20;
    float max;
    float min;
    float value;
    int sliderWidth = 22;
    int sliderPos;
    boolean asPercent = false;

    public Slider(int x, int y, String label, float max, float min, float initialValue) {
        pos = new Vector2(x, y);
        this.label = label;
        this.max = max;
        this.min = min;
        if (initialValue < min || initialValue > max) {
            throw new IllegalArgumentException("defaultSelection outside range.");
        }
        this.value = 100;
        this.sliderPos = (int)(M4th.clamp((value - min) / (max - min), 0, 1) * (sliderWidth - 3));
    }

    public Slider(int x, int y, String label, float max, float min, float initialValue, boolean asPercent) {
        this(x, y, label, max, min, initialValue);
        this.asPercent = asPercent;
    }

    @Override
    public void process(double timeDelta, Input input) {
        if (!highlighted) {
            return;
        }
        if (input.wasKeyJustPressed(Keybinds.ui_left)) {
            sliderPos = (int)M4th.clamp(sliderPos - 1, 0, sliderWidth - 3);
        }
        if (input.wasKeyJustPressed(Keybinds.ui_right)) {
            sliderPos = (int)M4th.clamp(sliderPos + 1, 0, sliderWidth - 3);
        }
        this.value = (float) sliderPos/(sliderWidth - 3) * (max - min) + min;
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
        TextCharacter sliderLeft = new TextCharacter('<', textColor, backgroundColor);
        TextCharacter sliderRight = new TextCharacter('>', textColor, backgroundColor);
        TextCharacter sliderBeam = new TextCharacter('-', textColor, backgroundColor);
        TextCharacter sliderKnob = new TextCharacter(' ', textColor, textColor);

        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x, (int)offset.y, getZOrder(), maxLabel, textColor, backgroundColor);
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth, (int)offset.y, getZOrder(), sliderLeft);
        for (int xx = 0; xx < sliderWidth - 2; xx++) {
            screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + 1 + xx, (int)offset.y, getZOrder(), sliderBeam);
        }
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + sliderWidth - 1, (int)offset.y, getZOrder(), sliderRight);

        int knobPos = labelWidth + sliderPos + 1;
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + knobPos, (int)offset.y, getZOrder(), sliderKnob);

        String valueString = getDisplayValue();
        int valuePos = labelWidth + sliderWidth/2 - valueString.length()/2;
        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x + valuePos, (int)offset.y, getZOrder(), valueString, textColor, backgroundColor);
        if (knobPos >= valuePos && knobPos < valuePos + valueString.length()) {
            screen.drawText((int)pos.x, (int)pos.y, (int)offset.x + valuePos + (knobPos - valuePos), (int)offset.y, getZOrder(), "" + valueString.charAt(knobPos - valuePos), TextColor.ANSI.BLACK, textColor);
        }
    }

    private String getDisplayValue() {
        int val = 0;
        if (asPercent) {
            val = (int)(M4th.clamp((value - min) / (max - min), 0, 1) * 100);
            return val + "%";
        }
        else {
            val = (int) value;
            return "" + val;
        }
    }
}
