package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.Input.Input;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.Element.KeyChoice;
import normallynormal.UI.LanguageManager;

public class KeyboardMenu extends Box {
    private static final int width = 44;
    private static final int height = 9;

    public KeyboardMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, width, height);
    }

    @Override
    public void addElements() {
        addChild(new KeyChoice(1, 1, LanguageManager.get("settings.input_settings.keyboard.jump"), Input.PLAYER_JUMP));
        addChild(new KeyChoice(1, 2, LanguageManager.get("settings.input_settings.keyboard.left"), Input.PLAYER_LEFT));
        addChild(new KeyChoice(1, 3, LanguageManager.get("settings.input_settings.keyboard.right"), Input.PLAYER_RIGHT));
        addChild(new KeyChoice(1, 4, LanguageManager.get("settings.input_settings.keyboard.down"), Input.PLAYER_DOWN));
        addChild(new KeyChoice(1, 5, LanguageManager.get("settings.input_settings.keyboard.dash"), Input.PLAYER_DASH));
        addChild(new Button(1, 7, LanguageManager.get("settings.back"), PauseManager.inputMenu));
    }
}
