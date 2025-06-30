package normallynormal;

import com.googlecode.lanterna.TextColor;
import normallynormal.Constants.ScreenConstants;
import normallynormal.Settings.Other;

import java.text.DecimalFormat;

public class RenderSystem implements Runnable {
    DecimalFormat df = new DecimalFormat("#.##");

    @Override
    public void run() {
        float fps = ScreenConstants.TARGET_FPS;
        float overshootFPS = ScreenConstants.TARGET_FPS;
        long lastFrameTime = System.nanoTime();
        try {
            while (true) {
                long now = System.nanoTime();
                double deltaSeconds = (now - lastFrameTime) * 1.0e-9;
                if (deltaSeconds < 1.0 / overshootFPS) continue;

                GameManager.screenBoundingBox.x = -GameManager.renderXOffset.get();
                GameManager.screenBoundingBox.y = -GameManager.renderYOffset.get();

                GameManager.screen.clear();
                GameManager.currentLevel.render(GameManager.screen, GameManager.renderXOffset.get(), GameManager.renderYOffset.get());
                GameManager.currentLevel.applyPostShaders(GameManager.screen);
                GameManager.screen.drawText(0, 0, 0, 0, Integer.MAX_VALUE, Other.VERSION_STRING, TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                GameManager.screen.drawText(0, 1, 0, 0, Integer.MAX_VALUE, "FPS: " + df.format(fps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                GameManager.screen.drawText(0, 2, 0, 0, Integer.MAX_VALUE, "TPS: " + df.format(GameManager.sharedTPS), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                GameManager.screen.refresh();

                fps = (float) (fps * 0.9f + 0.1f * (1f / deltaSeconds));
                overshootFPS = Math.max(ScreenConstants.TARGET_FPS, (ScreenConstants.TARGET_FPS - fps) + ScreenConstants.TARGET_FPS);

                lastFrameTime = now;
            }
        } catch (InterruptedException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
