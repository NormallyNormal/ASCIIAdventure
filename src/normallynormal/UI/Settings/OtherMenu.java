package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.Settings.Other;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.Element.MultiChoice;
import normallynormal.UI.LanguageManager;

public class OtherMenu extends Box {
    private static final int width = 44;
    private static final int height = 7;

    private static final String[] languages = {"en"};

    public OtherMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
    }

    @Override
    public void addElements() {
        int langIndex = findIndex(languages, Other.LANGUAGE_CODE, 0);

        addChild(new MultiChoice(1, 1, LanguageManager.get("settings.other_settings.language"), languages, langIndex,
                idx -> Other.LANGUAGE_CODE = languages[idx]));
        addChild(new MultiChoice(1, 2, LanguageManager.get("settings.other_settings.debug"),
                new String[]{LanguageManager.get("settings.disabled"), LanguageManager.get("settings.enabled")},
                Other.DEBUG ? 1 : 0,
                idx -> Other.DEBUG = (idx == 1)));
        addChild(new MultiChoice(1, 3, LanguageManager.get("settings.other_settings.woke_mode"),
                new String[]{LanguageManager.get("settings.other_settings.woke_mode.none"), LanguageManager.get("settings.other_settings.woke_mode.gay"), LanguageManager.get("settings.other_settings.woke_mode.trans")},
                Math.max(0, Math.min(2, Other.WOKE_MODE)),
                idx -> Other.WOKE_MODE = idx));
        addChild(new Button(1, 5, LanguageManager.get("settings.back"), PauseManager.pauseMenu));
    }

    private static int findIndex(String[] options, String value, int fallback) {
        for (int i = 0; i < options.length; i++) {
            if (options[i].equals(value)) return i;
        }
        return fallback;
    }
}
