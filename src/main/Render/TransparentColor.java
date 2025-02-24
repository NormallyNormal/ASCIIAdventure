package main.Render;

import com.googlecode.lanterna.TextColor;

import java.awt.*;

public class TransparentColor implements TextColor {
    @Override
    public byte[] getForegroundSGRSequence() {
        return new byte[0];
    }

    @Override
    public byte[] getBackgroundSGRSequence() {
        return new byte[0];
    }

    @Override
    public int getRed() {
        return 0;
    }

    @Override
    public int getGreen() {
        return 0;
    }

    @Override
    public int getBlue() {
        return 0;
    }

    @Override
    public Color toColor() {
        return null;
    }

    public static TransparentColor TRANSPARENT = new TransparentColor();
}
