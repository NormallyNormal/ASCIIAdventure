package UI.Element;

import Constants.ScreenConstants;
import Input.Input;
import Render.DepthScreen;
import Settings.Keybinds;
import com.googlecode.lanterna.TextColor;

public class DialogueBox extends UIElement {
    private String[] text;
    private int stage = 0;
    private long stageUpdateTime = 0;
    private double timePerChar = 0.05;

    public void reset() {
        stageUpdateTime = System.currentTimeMillis();
        stage = 0;
    }

    @Override
    public void process (double timeDelta, Input input) {
        if (input.getKeyPressed(Keybinds.dialogue_next)) {
            stage++;
            stageUpdateTime = System.currentTimeMillis();
        };
    }

    @Override
    public void render (DepthScreen screen) {
        int charsToDrawNum = (int)(System.currentTimeMillis() - stageUpdateTime) / (int)(timePerChar * 1000);
        screen.drawTextAdvanced(2, 2, 0, 0, 99, text[stage], TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, ScreenConstants.PLAY_SCREEN_WIDTH - 4 ,charsToDrawNum);
    }
}
