package normallynormal.UI.Settings;

import normallynormal.Constants.ScreenConstants;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.Slider;

public class SettingsMenu extends Box {
    private static final int width = 44;
    private static final int height = 10;

    public SettingsMenu() {
        super(ScreenConstants.PLAY_SCREEN_WIDTH/2 - width/2, ScreenConstants.PLAY_SCREEN_HEIGHT/2 - height/2, 44, height);
        addChild(new Slider(1, 1, "SliderSliderSlider20", 20, 0, 0));
    }
}
