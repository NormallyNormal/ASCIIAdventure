package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.Element.Slider;
import normallynormal.UI.LanguageManager;

public class AudioMenu extends Box {
    private static final int width = 44;
    private static final int height = 6;

    public AudioMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
    }

    @Override
    public void addElements() {
        addChild(new Slider(1, 1, LanguageManager.get("settings.audio_settings.music"), 100, 0, 100, true));
        addChild(new Slider(1, 2, LanguageManager.get("settings.audio_settings.sfx"), 100, 0, 100, true));

        addChild(new Button(1, 4, LanguageManager.get("settings.back"), PauseManager.pauseMenu));
    }
}