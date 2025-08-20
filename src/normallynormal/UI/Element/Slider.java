package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.M4th;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;

import org.tinylog.Logger;

public class Slider extends UIComponent {
    String label;
    int labelWidth = 20;
    float max;
    float min;
    float value;
    int sliderWidth = 22;
    int sliderPos = 0;
    boolean asPercent;

    public Slider(int x, int y, String label, float max, float min, int initalValue) {
        pos = new Vector2(x, y);
        this.label = label;
        this.max = max;
        this.min = min;
        this.value = initalValue;
        this.sliderPos = (int)M4th.clamp((value - min) / (max - min), min, max);
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
        TextCharacter sliderKnob = new TextCharacter('|', textColor, backgroundColor);

        screen.drawText((int)pos.x, (int)pos.y, (int)offset.x, (int)offset.y, getZOrder(), maxLabel, textColor, backgroundColor);
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth, (int)offset.y, getZOrder(), sliderLeft);
        for (int xx = 0; xx < sliderWidth - 2; xx++) {
            screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + 1 + xx, (int)offset.y, getZOrder(), sliderBeam);
        }
        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + sliderWidth - 1, (int)offset.y, getZOrder(), sliderRight);

        screen.setCharacterWithDepth((int)pos.x, (int)pos.y, (int)offset.x + labelWidth + sliderPos + 1, (int)offset.y, getZOrder(), sliderKnob);
    }
}
