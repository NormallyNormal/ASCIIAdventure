package normallynormal;

import normallynormal.Constants.ScreenConstants;
import normallynormal.Math.Direction;
import normallynormal.Settings.Keybinds;
import normallynormal.Settings.Other;
import org.tinylog.Logger;

public class PhysicsSystem implements Runnable{
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double tps = 0;
        double timeSinceLastTransitionMovement = 0;
        try {
            while (true) {
                GameManager.controllerInput.poll();
                if (GameManager.input.wasKeyJustPressed(Keybinds.pause)) {
                    if (GameManager.paused.get()) {
                        Logger.info("Unpaused");
                    } else {
                        Logger.info("Paused");
                    }
                    GameManager.paused.set(!GameManager.paused.get());

                }
                long now = System.nanoTime();
                double deltaSeconds = (now - lastTime) * 1.0e-9;
                if (deltaSeconds > 1) {
                    lastTime = now;
                    continue;
                }
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
                    GameManager.pause.process(deltaSeconds, GameManager.input);
                }
                GameManager.input.update();
                lastTime = now;
                tps = tps * 0.9 + 0.1 * (1 / deltaSeconds);
                GameManager.sharedTPS = tps;
                if (Double.isInfinite(tps)) tps = 0;
                if (Other.REDUCE_CPU_USAGE) Thread.sleep(5);
            }
        } catch (InterruptedException e) {
            return;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
