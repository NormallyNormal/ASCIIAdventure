package normallynormal;

import normallynormal.Constants.ScreenConstants;
import normallynormal.Input.Input;
import normallynormal.Math.Direction;
import normallynormal.UI.Settings.PauseManager;
import org.tinylog.Logger;

import java.util.concurrent.locks.LockSupport;

public class PhysicsSystem implements Runnable{
    private static final double PHYSICS_HZ = 240.0;
    private static final long PHYSICS_TICK_NANOS = (long) (1_000_000_000.0 / PHYSICS_HZ);

    @Override
    public void run() {
        long lastTime = System.nanoTime();
        long nextDeadline = lastTime + PHYSICS_TICK_NANOS;
        double tps = 0;
        double timeSinceLastTransitionMovement = 0;
        try {
            while (true) {
                long now = System.nanoTime();
                long remaining = nextDeadline - now;
                if (remaining > 0) {
                    LockSupport.parkNanos(remaining);
                    continue;
                }
                if (-remaining > PHYSICS_TICK_NANOS * 5) nextDeadline = now;
                nextDeadline += PHYSICS_TICK_NANOS;

                if (GameManager.input.wasInputJustPressed(Input.PAUSE)) {
                    boolean nowPaused = !GameManager.paused.get();
                    GameManager.paused.set(nowPaused);
                    if (nowPaused) {
                        PauseManager.showMenu();
                        Logger.info("Paused");
                    } else {
                        Logger.info("Unpaused");
                    }
                }

                double deltaSeconds = (now - lastTime) * 1.0e-9;
                lastTime = now;
                if (deltaSeconds > 1) continue;
                if (deltaSeconds < 0) deltaSeconds = 0;

                Direction outOfBoundsDirection = GameManager.currentLevel.playerOffScreen(GameManager.levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH, GameManager.levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT);
                if (!GameManager.paused.get()) {
                    timeSinceLastTransitionMovement += deltaSeconds;
                    if (outOfBoundsDirection == Direction.NONE) {
                        GameManager.currentLevel.process(deltaSeconds, GameManager.input);
                        timeSinceLastTransitionMovement = 0;
                    } else {
                        double transitionFrequency = 1 / (outOfBoundsDirection.isVertical() ? ScreenConstants.TRANSITION_SPEED_VERTICAL : ScreenConstants.TRANSITION_SPEED_HORIZONTAL);
                        if (timeSinceLastTransitionMovement > transitionFrequency) {
                            switch (outOfBoundsDirection) {
                                case UP, DOWN:
                                    GameManager.renderYOffset.addAndGet(-outOfBoundsDirection.toMovement());
                                    break;
                                case LEFT, RIGHT:
                                    GameManager.renderXOffset.addAndGet(-outOfBoundsDirection.toMovement());
                                    break;
                            }
                            if (GameManager.renderXOffset.get() % ScreenConstants.PLAY_SCREEN_WIDTH == 0 && GameManager.renderYOffset.get() % ScreenConstants.PLAY_SCREEN_HEIGHT == 0) {
                                switch (outOfBoundsDirection) {
                                    case UP:
                                        GameManager.levelFrameY--;
                                        break;
                                    case DOWN:
                                        GameManager.levelFrameY++;
                                        break;
                                    case LEFT:
                                        GameManager.levelFrameX--;
                                        break;
                                    case RIGHT:
                                        GameManager.levelFrameX++;
                                        break;
                                }
                            }
                            timeSinceLastTransitionMovement -= transitionFrequency;
                        }
                    }
                }
                else {
                    PauseManager.process(deltaSeconds, GameManager.input);
                }
                GameManager.input.update();
                if (deltaSeconds > 0) tps = tps * 0.9 + 0.1 * (1 / deltaSeconds);
                if (Double.isInfinite(tps)) tps = 0;
                GameManager.sharedTPS = tps;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
