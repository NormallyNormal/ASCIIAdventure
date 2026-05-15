package normallynormal;
import java.awt.*;
import java.io.IOException;

import javax.swing.Timer;

import normallynormal.Settings.SettingsManager;
import normallynormal.Util.SleepBlocker;

public class Game {
    public static void run() throws IOException, FontFormatException {
        SleepBlocker.preventSleep();
        SettingsManager.load();
        GameManager.prepareScreen();
        GameManager.prepareGame();

        Thread physicsThread = new Thread(new PhysicsSystem(), "physics");
        Thread renderThread = new Thread(new RenderSystem(), "render");

        physicsThread.start();
        renderThread.start();

        // Focus needs to be re-requested periodically; one-shot requests are not enough on this setup.
        Timer focusPoll = new Timer(100, e -> GameManager.terminal.requestFocusInWindow());
        focusPoll.setRepeats(true);
        focusPoll.start();
    }
}
