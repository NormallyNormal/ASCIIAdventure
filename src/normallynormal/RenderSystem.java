package normallynormal;

import com.googlecode.lanterna.TextColor;
import normallynormal.Render.Shader.DarkenShader;
import normallynormal.Settings.Other;
import normallynormal.UI.Settings.PauseManager;

import java.text.DecimalFormat;
import java.util.concurrent.locks.LockSupport;

public class RenderSystem implements Runnable {
    DecimalFormat df = new DecimalFormat("#.##");
    DarkenShader darkenShader = new DarkenShader();

    @Override
    public void run() {
        float fps = Other.TARGET_FPS;
        long lastFrameTime = System.nanoTime();
        long nextDeadline = lastFrameTime;
        try {
            while (true) {
                long frameNanos = (long) (1_000_000_000.0 / Other.TARGET_FPS);
                long now = System.nanoTime();
                long remaining = nextDeadline - now;
                if (remaining > 0) {
                    LockSupport.parkNanos(remaining);
                    continue;
                }
                // If we fell more than a few frames behind, drop catch-up to avoid death spirals.
                if (-remaining > frameNanos * 5) nextDeadline = now;
                nextDeadline += frameNanos;

                double deltaSeconds = (now - lastFrameTime) * 1.0e-9;
                lastFrameTime = now;

                GameManager.screenBoundingBox.x = -GameManager.renderXOffset.get();
                GameManager.screenBoundingBox.y = -GameManager.renderYOffset.get();

                GameManager.screen.clear();
                GameManager.currentLevel.render(GameManager.screen, GameManager.renderXOffset.get(), GameManager.renderYOffset.get());
                GameManager.currentLevel.applyPostShaders(GameManager.screen);

                if(GameManager.paused.get()) {
                    darkenShader.apply(GameManager.screen, 0, 0, null, null, null);
                    PauseManager.render(GameManager.screen);
                }

                if(Other.DEBUG) {
                    GameManager.screen.drawText(0, 0, 0, 0, Integer.MAX_VALUE, Other.VERSION_STRING, TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                    GameManager.screen.drawText(0, 1, 0, 0, Integer.MAX_VALUE, "FPS: " + df.format(fps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                    GameManager.screen.drawText(0, 2, 0, 0, Integer.MAX_VALUE, "TPS: " + df.format(GameManager.sharedTPS), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                }

                GameManager.screen.refresh();

                if (deltaSeconds > 0) fps = (float) (fps * 0.9f + 0.1f * (1f / deltaSeconds));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
