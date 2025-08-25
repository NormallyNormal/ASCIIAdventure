package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Button;
import normallynormal.UI.Element.MultiChoice;
import normallynormal.UI.LanguageManager;

public class VisualMenu extends Box {
    private static final int width = 44;
    private static final int height = 6;

    private final static String[] refreshRates = {"30","50","60","72","75","85","100","120","144","165","170","180","200","240","280","300","360","390","480"};

    private final static String[] fontSizes = new String[] {"8", "10", "12", "14", "16", "18", "20", "24", "28", "32", "36", "40", "48"};

    public VisualMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
    }

    @Override
    public void addElements() {
        addChild(new MultiChoice(1,1, LanguageManager.get("settings.visual_settings.font_size"), fontSizes, 3));
        addChild(new MultiChoice(1,2, LanguageManager.get("settings.visual_settings.fps"), refreshRates, 2));
        addChild(new Button(1, 4, LanguageManager.get("settings.back"), PauseManager.pauseMenu));
    }
}