package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.LanguageManager;

public class InputMenu extends Box {
    private static final int width = 44;
    private static final int height = 6;

    public InputMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
    }

    @Override
    public void addElements() {
        addChild(new Button(1, 1, LanguageManager.get("settings.input_settings.keyboard"), PauseManager.pauseMenu));
        addChild(new Button(1, 2, LanguageManager.get("settings.input_settings.controller"), PauseManager.pauseMenu));
        addChild(new Button(1, 4, LanguageManager.get("settings.back"), PauseManager.pauseMenu));
    }
}