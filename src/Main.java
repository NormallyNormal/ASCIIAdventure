import Constants.ScreenConstants;
import Input.Input;
import World.Game;
import World.Level;
import Render.DepthScreen;
import Math.Direction;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.DecimalFormat;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        // Setup terminal and screen layers
        SwingTerminalFrame terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(ScreenConstants.PLAY_SCREEN_WIDTH,ScreenConstants.PLAY_SCREEN_HEIGHT)).createSwingTerminal();

        terminal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        terminal.setResizable(false);

        Input input = new Input();
        terminal.addKeyListener(input);
        terminal.requestFocusInWindow();
        terminal.setTitle("ASCII Adventure");
        terminal.setVisible(true);

        DepthScreen screen = new DepthScreen(terminal);
        screen.doResizeIfNecessary();
        screen.setCursorPosition(null);
        screen.startScreen();

        Game.currentLevel = new Level();
        long currentTime = System.nanoTime();
        long lastTime = System.nanoTime();
        double timeDeltaSeconds;
        double timeSinceLastFrame = 0;
        double timeSinceLastTransitionMovement = 0;

        double fps = 0;
        double tps = 0;

        DecimalFormat df = new DecimalFormat("#.##");

        boolean running = true;

        int levelFrameX = 30;
        int levelFrameY = 30;

        int renderXOffset = -levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH;
        int renderYOffset = -levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT;

        while(running) {
            input.newFrame();
            timeDeltaSeconds = (currentTime - lastTime) * 1.0e-9;
            if (timeDeltaSeconds > 0.1) {
                lastTime = currentTime;
                continue;
            }
            timeSinceLastFrame += timeDeltaSeconds;
            timeSinceLastTransitionMovement += timeDeltaSeconds;

            Direction outOfBoundsDirection = Game.currentLevel.playerOffScreen(levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH, levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT);
            if(outOfBoundsDirection == Direction.NONE) {
                Game.currentLevel.process(timeDeltaSeconds, input);
                timeSinceLastTransitionMovement = 0;
            }
            else{
                double transitionFrequency = 1/(outOfBoundsDirection.isVertical() ? ScreenConstants.TRANSITION_SPEED_VERTICAL : ScreenConstants.TRANSITION_SPEED_HORIZONTAL);
                if(timeSinceLastTransitionMovement > transitionFrequency) {
                    switch(outOfBoundsDirection) {
                        case UP, DOWN:
                            renderYOffset -= outOfBoundsDirection.toMovement();
                            break;
                        case LEFT, RIGHT:
                            renderXOffset -= outOfBoundsDirection.toMovement();
                            break;
                    }
                    if (renderXOffset % ScreenConstants.PLAY_SCREEN_WIDTH == 0 && renderYOffset % ScreenConstants.PLAY_SCREEN_HEIGHT == 0) {
                        switch(outOfBoundsDirection) {
                            case UP:
                                levelFrameY--;
                                break;
                            case DOWN:
                                levelFrameY++;
                                break;
                            case LEFT:
                                levelFrameX--;
                                break;
                            case RIGHT:
                                levelFrameX++;
                                break;
                        }
                    }
                    timeSinceLastTransitionMovement -= transitionFrequency;
                }
            }
            if(timeSinceLastFrame >= 1/ScreenConstants.TARGET_FPS) {
                screen.clear();
                Game.currentLevel.render(screen, renderXOffset, renderYOffset);

                Game.currentLevel.applyPostShaders(screen);

                screen.drawText(0, 0, 0, 0, Integer.MAX_VALUE, "FPS: " + df.format(fps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                screen.drawText(0, 1, 0, 0, Integer.MAX_VALUE, "TPS: " + df.format(tps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);


                screen.refresh();

                fps = fps * 0.9 + 0.1 * (1/timeSinceLastFrame);
                timeSinceLastFrame -= 1/ScreenConstants.TARGET_FPS;
            }
            terminal.requestFocusInWindow();

            //Complementary filter
            tps = tps * 0.9 + 0.1 * (1/timeDeltaSeconds);
            if (Double.isInfinite(tps)) {
                tps = 0;
            }

            if (input.getKeyState(KeyEvent.VK_ESCAPE)) {
                running = false;
            }

//            Thread.sleep(0, 0);


            lastTime = currentTime;
            currentTime = System.nanoTime();
        }
        terminal.close();
    }
}