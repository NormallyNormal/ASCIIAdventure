package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.LanguageManager;

public class PauseMenu extends Box {
    private static final int width = 44;
    private static final int height = 9;

    public PauseMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
    }

    @Override
    public void addElements() {
        addChild(new Button(1, 1, LanguageManager.get("settings.audio_settings"), PauseManager.audioMenu));
        addChild(new Button(1, 2, LanguageManager.get("settings.visual_settings"), PauseManager.visualMenu));
        addChild(new Button(1, 3, LanguageManager.get("settings.input_settings"), PauseManager.inputMenu));
        addChild(new Button(1, 4, LanguageManager.get("settings.other_settings"), PauseManager.otherMenu));
        addChild(new Button(1, 6, LanguageManager.get("settings.resume"), null));
        addChild(new Button(1, 7, LanguageManager.get("settings.quit"), PauseManager.quit));
    }
}