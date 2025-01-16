import Constants.ScreenConstants;
import Input.Input;
import Render.Shader.PostShader;
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

        Game.currentLevel = new Level();
        Level transitionLevel = null;
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

            Direction outOfBoundsDirection = Game.currentLevel.playerOutOfBounds();
            if(outOfBoundsDirection == Direction.NONE) {
                Game.currentLevel.process(timeDeltaSeconds, input);
                timeSinceLastTransitionMovement = 0;
            }
            else{
                transitionLevel = Game.currentLevel.connectedLevels.get(outOfBoundsDirection);
                double transitionFrequency = 1/(outOfBoundsDirection.isVertical() ? ScreenConstants.TRANSITION_SPEED_VERTICAL : ScreenConstants.TRANSITION_SPEED_HORIZONTAL);
                if(transitionLevel != null && timeSinceLastTransitionMovement > transitionFrequency) {
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
                        Game.currentLevel = transitionLevel;
                        Game.currentLevel.loopPlayer();
                        renderYOffset = 0;
                        renderXOffset = 0;
                    }
                }
            }
            if(timeSinceLastFrame >= 1/ScreenConstants.TARGET_FPS) {
                Game.currentLevel.render(screen,renderXOffset,renderYOffset);
                if(outOfBoundsDirection != Direction.NONE && transitionLevel != null) {
                    switch (outOfBoundsDirection) {
                        case UP:
                            transitionLevel.render(screen, renderXOffset, renderYOffset - ScreenConstants.PLAY_SCREEN_HEIGHT);
                            break;
                        case DOWN:
                            transitionLevel.render(screen, renderXOffset, renderYOffset + ScreenConstants.PLAY_SCREEN_HEIGHT);
                            break;
                        case LEFT:
                            transitionLevel.render(screen, renderXOffset - ScreenConstants.PLAY_SCREEN_WIDTH, renderYOffset);
                            break;
                        case RIGHT:
                            transitionLevel.render(screen, renderXOffset + ScreenConstants.PLAY_SCREEN_WIDTH, renderYOffset);
                            break;
                    }
                }

                Game.currentLevel.calcPostShaders();
                if (transitionLevel != null) {
                    transitionLevel.calcPostShaders();
                }

                Game.currentLevel.applyPostShaders(screen);
                if (transitionLevel != null) {
                    transitionLevel.applyPostShaders(screen);
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