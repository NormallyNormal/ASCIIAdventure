package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.Element.MultiChoice;
import normallynormal.UI.LanguageManager;

public class VisualMenu extends Box {
    private static final int width = 44;
    private static final int height = 5;

    public VisualMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
    }

    @Override
    public void addElements() {
        addChild(new MultiChoice(1,1, LanguageManager.get("settings.visual_settings.font_size"), new String[] {"8", "10", "12", "14", "16", "18", "20", "24", "28", "32", "36", "40", "48"}, 3));
        addChild(new Button(1, 3, LanguageManager.get("settings.back"), PauseManager.pauseMenu));
    }
}