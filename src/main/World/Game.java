package main.World;

import main.Constants.ScreenConstants;
import main.Input.Input;
import main.Math.AABB;
import main.Render.DepthScreen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import main.Math.Direction;
import main.Settings.Keybinds;

public class Game {
    public static Level currentLevel;
    public static final AABB screenBoundingBox = new AABB(0, 0, ScreenConstants.PLAY_SCREEN_WIDTH, ScreenConstants.PLAY_SCREEN_HEIGHT);

    public static void run() throws IOException, FontFormatException, InterruptedException {
        SwingTerminalFrame terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(ScreenConstants.PLAY_SCREEN_WIDTH,ScreenConstants.PLAY_SCREEN_HEIGHT)).createSwingTerminal();

        terminal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);Font ubuntuMono = Font.createFont(Font.TRUETYPE_FONT, new File("src/resources/font/UbuntuMono-Regular.ttf"))
                .deriveFont(16f);
        terminal.setFont(ubuntuMono);
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

        currentLevel = new Level();
        long currentTime = System.nanoTime();
        long lastTime = System.nanoTime();
        double timeDeltaSeconds;
        double timeSinceLastFrame = 0;
        double timeSinceLastTransitionMovement = 0;

        float fps = ScreenConstants.TARGET_FPS;
        double tps = 0;

        DecimalFormat df = new DecimalFormat("#.##");

        boolean running = true;

        int levelFrameX = 30;
        int levelFrameY = 30;

        int renderXOffset = -levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH;
        int renderYOffset = -levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT;

        float overshootFPS = ScreenConstants.TARGET_FPS;
        while(running) {
            input.newFrame();
            timeDeltaSeconds = (currentTime - lastTime) * 1.0e-9;
            if (timeDeltaSeconds > 1) {
                lastTime = currentTime;
                continue;
            }
            if (timeDeltaSeconds < 0) {
                timeDeltaSeconds = 0;
                lastTime = currentTime;
            }
            timeSinceLastFrame += timeDeltaSeconds;
            timeSinceLastTransitionMovement += timeDeltaSeconds;

            Direction outOfBoundsDirection = currentLevel.playerOffScreen(levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH, levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT);
            if(outOfBoundsDirection == Direction.NONE) {
                currentLevel.process(timeDeltaSeconds, input);
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
            if(timeSinceLastFrame >= 1/overshootFPS) {
                screenBoundingBox.x = -renderXOffset;
                screenBoundingBox.y = -renderYOffset;

                screen.clear();
                currentLevel.render(screen, renderXOffset, renderYOffset);

                currentLevel.applyPostShaders(screen);

                screen.drawText(0, 0, 0, 0, Integer.MAX_VALUE, "FPS: " + df.format(fps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                screen.drawText(0, 1, 0, 0, Integer.MAX_VALUE, "TPS: " + df.format(tps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);


                screen.refresh();

                fps = (float) (fps * 0.9f + 0.1f * (1f/timeSinceLastFrame));
                timeSinceLastFrame -= 1/ScreenConstants.TARGET_FPS;
            }

            //Complementary filter
            tps = tps * 0.9 + 0.1 * (1/timeDeltaSeconds);
            if (Double.isInfinite(tps)) {
                tps = 0;
            }

            if (input.getKeyState(Keybinds.exit)) {
                running = false;
            }

            lastTime = currentTime;
            currentTime = System.nanoTime();

            terminal.requestFocusInWindow();

            Thread.sleep(1);

            overshootFPS = (ScreenConstants.TARGET_FPS - fps) + ScreenConstants.TARGET_FPS;
        }
        terminal.close();
    }
}
