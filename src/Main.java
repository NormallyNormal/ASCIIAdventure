import Constants.ScreenConstants;
import Input.Input;
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
    public static void main(String[] args) throws IOException {
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

        Level level1 = new Level();
        Level level2 = null;
        long currentTime = System.nanoTime();
        long lastTime = System.nanoTime();
        double timeDeltaSeconds;
        double timeSinceLastFrame = 0;
        double timeSinceLastTransitionMovement = 0;

        double fps = 0;
        double tps = 0;

        DecimalFormat df = new DecimalFormat("#.##");

        boolean running = true;

        int renderXOffset = 0;
        int renderYOffset = 0;

        while(running) {
            timeDeltaSeconds = (currentTime - lastTime) * 1.0e-9;
            timeSinceLastFrame += timeDeltaSeconds;
            timeSinceLastTransitionMovement += timeDeltaSeconds;

            Direction outOfBoundsDirection = level1.playerOutOfBounds();
            if(outOfBoundsDirection == Direction.NONE) {
                level1.process(timeDeltaSeconds, input);
                timeSinceLastTransitionMovement = 0;
            }
            else{
                level2 = level1.connectedLevels.get(outOfBoundsDirection);
                double transitionFrequency = 1/(outOfBoundsDirection.isVertical() ? ScreenConstants.TRANSITION_SPEED_VERTICAL : ScreenConstants.TRANSITION_SPEED_HORIZONTAL);
                if(level2 != null && timeSinceLastTransitionMovement > transitionFrequency) {
                    switch(outOfBoundsDirection) {
                        case UP, DOWN:
                            renderYOffset -= outOfBoundsDirection.toMovement();
                            break;
                        case LEFT, RIGHT:
                            renderXOffset -= outOfBoundsDirection.toMovement();
                            break;
                    }
                    timeSinceLastTransitionMovement -= transitionFrequency;
                    if(Math.abs(renderYOffset) >= ScreenConstants.PLAY_SCREEN_HEIGHT || Math.abs(renderXOffset) >= ScreenConstants.PLAY_SCREEN_WIDTH) {
                        level1 = level2;
                        level1.loopPlayer();
                        renderYOffset = 0;
                        renderXOffset = 0;
                    }
                }
            }
            if(timeSinceLastFrame >= 1/ScreenConstants.TARGET_FPS) {
                level1.render(screen,renderXOffset,renderYOffset);
                if(outOfBoundsDirection != Direction.NONE && level2 != null) {
                    switch (outOfBoundsDirection) {
                        case UP:
                            level2.render(screen, renderXOffset, renderYOffset - ScreenConstants.PLAY_SCREEN_HEIGHT);
                            break;
                        case DOWN:
                            level2.render(screen, renderXOffset, renderYOffset + ScreenConstants.PLAY_SCREEN_HEIGHT);
                            break;
                        case LEFT:
                            level2.render(screen, renderXOffset - ScreenConstants.PLAY_SCREEN_WIDTH, renderYOffset);
                            break;
                        case RIGHT:
                            level2.render(screen, renderXOffset + ScreenConstants.PLAY_SCREEN_WIDTH, renderYOffset);
                            break;
                    }
                }
                screen.drawText(0, 0, 0, 0, 50, "FPS: " + df.format(fps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                screen.drawText(0, 1, 0, 0, 50, "TPS: " + df.format(tps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);

                screen.refresh();
                screen.clear();

                fps = fps * 0.9 + 0.1 * (1/timeSinceLastFrame);
                timeSinceLastFrame -= 1/ScreenConstants.TARGET_FPS;
            }
            terminal.requestFocusInWindow();
            lastTime = currentTime;
            currentTime = System.nanoTime();

            //Complementary filter
            tps = tps * 0.9 + 0.1 * (1/timeDeltaSeconds);

            if (input.getKeyState(KeyEvent.VK_ESCAPE)) {
                running = false;
            }
        }
        terminal.close();
    }
}