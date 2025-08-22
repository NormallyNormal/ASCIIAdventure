package normallynormal.UI.Settings;

import normallynormal.GameManager;
import normallynormal.Input.Input;
import normallynormal.Render.DepthScreen;
import normallynormal.UI.Element.Box;
import normallynormal.UI.Element.UIComponent;

public class PauseParent {
    Box currentMenu = null;

    Box pauseMenu = new PauseMenu();

    public PauseParent() {
        this.currentMenu = this.pauseMenu;
    }

    public void render(DepthScreen screen) {
        if (currentMenu != null)
            currentMenu.render(screen);
    }

    public void process(double timeDelta, Input input) {
        if (currentMenu != null)
            currentMenu.process(timeDelta, input);
    }

    public void showMenu(Box show) {
        currentMenu.reset();
        currentMenu = show;
        if (currentMenu == null) {
            GameManager.paused.set(false);
            currentMenu = this.pauseMenu;
        }
    }
}