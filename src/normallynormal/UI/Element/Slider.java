package normallynormal.UI.Element;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Math.Vector2;
import normallynormal.Render.DepthScreen;
import normallynormal.Render.TransparentColor;

import org.tinylog.Logger;

public class Slider extends UIElement {
    String label;
    int labelWidth = 20;
    float max;
    float min;
    float value;
    int sliderWidth = 22;
    int sliderPos = 0;
    boolean asPercent;

    @Override
    public void copyForRender() {

    }

    TextCharacter sliderLeft = new TextCharacter('<', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    TextCharacter sliderRight = new TextCharacter('>', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    TextCharacter sliderBeam = new TextCharacter('-', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
    TextCharacter sliderKnob = new TextCharacter('|', TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);

    @Override
    public void render(DepthScreen screen) {
        Vector2 offset = getOffset();
        String maxLabel = label.substring(0, labelWidth);
        if (label.length() != maxLabel.length()) {
            Logger.error("Label is too long for length " + labelWidth + ": " + label);
            label = maxLabel;
        }
        screen.drawText((int)renderPos.x, (int)renderPos.y, (int)offset.x, (int)offset.y, getZOrder(), maxLabel, TextColor.ANSI.WHITE, TransparentColor.TRANSPARENT);
        screen.setCharacterWithDepth((int)renderPos.x, (int)renderPos.y, (int)offset.x, (int)offset.y + labelWidth + 1, getZOrder(), sliderLeft);
        for (int xx = 0; xx < sliderWidth - 2; xx++) {
            screen.setCharacterWithDepth((int)renderPos.x, (int)renderPos.y, (int)offset.x, (int)offset.y + labelWidth + 1 + xx, getZOrder(), sliderBeam);
        }
        screen.setCharacterWithDepth((int)renderPos.x, (int)renderPos.y, (int)offset.x, (int)offset.y + labelWidth + sliderWidth, getZOrder(), sliderRight);

        screen.setCharacterWithDepth((int)renderPos.x, (int)renderPos.y, (int)offset.x, (int)offset.y + labelWidth + sliderPos + 1, getZOrder(), sliderKnob);
    }
}
