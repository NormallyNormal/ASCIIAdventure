package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.Element.MultiChoice;
import normallynormal.UI.LanguageManager;

public class OtherMenu extends Box {
    private static final int width = 44;
    private static final int height = 7;

    public OtherMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
    }

    @Override
    public void addElements() {
        addChild(new MultiChoice(1,1, LanguageManager.get("settings.other_settings.language"), new String[] {"en"}, 0));
        addChild(new MultiChoice(1,2, LanguageManager.get("settings.other_settings.debug"), new String[] {LanguageManager.get("settings.disabled"), LanguageManager.get("settings.enabled")}, 0));

        addChild(new MultiChoice(1,3, LanguageManager.get("settings.other_settings.woke_mode"), new String[] {LanguageManager.get("settings.other_settings.woke_mode.none"), LanguageManager.get("settings.other_settings.woke_mode.gay"), LanguageManager.get("settings.other_settings.woke_mode.trans")}, 0));
        addChild(new Button(1, 5, LanguageManager.get("settings.back"), PauseManager.pauseMenu));
    }
}