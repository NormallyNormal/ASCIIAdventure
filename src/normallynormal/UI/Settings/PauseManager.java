package normallynormal.UI.Settings;

import normallynormal.GameManager;
import normallynormal.Input.Input;
import normallynormal.Render.DepthScreen;
import normallynormal.UI.Element.Box;
import org.tinylog.Logger;

public class PauseManager {
    static Box currentMenu = null;

    static Box pauseMenu = new PauseMenu();
    static Box audioMenu = new AudioMenu();
    static Box visualMenu = new VisualMenu();
    static Box inputMenu = new InputMenu();
    static Box otherMenu = new OtherMenu();

    public static void initialize() {
        currentMenu = pauseMenu;
        pauseMenu.addElements();
        audioMenu.addElements();
        visualMenu.addElements();
        inputMenu.addElements();
        otherMenu.addElements();
    }

    public static void render(DepthScreen screen) {
        if (currentMenu != null)
            currentMenu.render(screen);
    }

    public static void process(double timeDelta, Input input) {
        if (currentMenu != null)
            currentMenu.process(timeDelta, input);
    }

    public static void showMenu(Box show) {
        currentMenu.reset();
        currentMenu = show;
        if (currentMenu == null) {
            GameManager.paused.set(false);
            Logger.info("Unpaused");
            currentMenu = pauseMenu;
        }
    }

    public static void showMenu() {
        pauseMenu.reset();
        currentMenu.reset();
        currentMenu = pauseMenu;
    }
}