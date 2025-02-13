package UI.Element;

import Constants.ScreenConstants;
import Input.Input;
import Render.DepthScreen;
import Settings.Keybinds;
import UI.ColorfulText;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.List;

public class DialogueBox extends UIElement {
    private String[] text;
    private List<ColorfulText> colorfulText = new ArrayList<>();
    private int stage = 0;
    private long stageUpdateTime = 0;

    public DialogueBox(String[] text) {
        this.text = text;
        for(String s : text) {
            colorfulText.add(new ColorfulText(s));
        }
    }

    public void reset() {
        stageUpdateTime = System.currentTimeMillis();
        stage = 0;
        charsToDrawNum = 0;
    }

    boolean keyHeld;
    double timePerChar = 0.03;
    int charsToDrawNum = 0;
    @Override
    public void process (double timeDelta, Input input) {
        boolean hasCharsLeft = charsToDrawNum < colorfulText.get(stage).length();
        if (hasCharsLeft) {
            charsToDrawNum = (int) (System.currentTimeMillis() - stageUpdateTime) / (int) (timePerChar * 1000);
        }
        boolean nextKeyPressed = input.getKeyState(Keybinds.dialogue_next);
        if (nextKeyPressed && !keyHeld) {
            if (hasCharsLeft) {
                charsToDrawNum = colorfulText.get(stage).length();
            }
            else {
                if (stage < text.length - 1) stage++;
                stageUpdateTime = System.currentTimeMillis();
                charsToDrawNum = 0;
            }
            keyHeld = true;
        }
        else if (!nextKeyPressed) {
            keyHeld = false;
        }
    }

    @Override
    public void render (DepthScreen screen) {
        for (int i = 0; i <= ScreenConstants.PLAY_SCREEN_WIDTH - 16 - 1; i++) {
            for (int j = 0; j <= 8 - 1; j++) {
                char filler = ' ';
                if (i == 0 || i == ScreenConstants.PLAY_SCREEN_WIDTH - 16 - 1) {
                    filler = '|';
                    if (j == 0 || j == 8 - 1) {
                        filler = '+';
                    }
                }
                else if (j == 0 || j == 8 - 1) {
                    filler = '-';
                }
                screen.setCharacterWithDepth(7 + i, 3 + j, 0, 0, 0, new TextCharacter(filler, TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK));
            }
        }
        screen.drawTextAdvanced(8, 4, 0, 0, 99, colorfulText.get(stage), TextColor.ANSI.WHITE, TextColor.ANSI.BLACK, ScreenConstants.PLAY_SCREEN_WIDTH - 16 - 2, charsToDrawNum);
    }
}
