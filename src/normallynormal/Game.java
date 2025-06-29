package normallynormal;
import java.awt.*;
import java.io.IOException;

import normallynormal.Settings.Keybinds;

public class Game {


    public static void run() throws IOException, FontFormatException, InterruptedException {
        GameManager.prepareScreen();
        GameManager.prepareGame();

        Thread physicsThread = new Thread(new PhysicsSystem());
        Thread renderThread = new Thread(new RenderSystem());

        physicsThread.start();
        renderThread.start();

        while (true) {
            if (GameManager.input.getKeyState(Keybinds.exit)) {
                physicsThread.interrupt();
                renderThread.interrupt();
                break;
            }
            Thread.sleep(100); // Don't spin too hard
            GameManager.terminal.requestFocusInWindow();
        }

        GameManager.terminal.close();
    }
}
