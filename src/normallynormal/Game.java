package normallynormal;
import java.awt.*;
import java.io.IOException;

import normallynormal.Util.SleepBlocker;

public class Game {
    public static void run() throws IOException, FontFormatException, InterruptedException {
        SleepBlocker.preventSleep();
        GameManager.prepareScreen();
        GameManager.prepareGame();

        Thread physicsThread = new Thread(new PhysicsSystem());
        Thread renderThread = new Thread(new RenderSystem());

        physicsThread.start();
        renderThread.start();

        while (true) {
            if (false) {
                physicsThread.interrupt();
                renderThread.interrupt();
                break;
            }
            Thread.sleep(100); // Don't spin too hard
            GameManager.terminal.requestFocusInWindow();
        }

        GameManager.terminal.close();
        SleepBlocker.allowSleep();
    }
}
