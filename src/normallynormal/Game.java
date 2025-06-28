package normallynormal;

import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import normallynormal.Constants.ScreenConstants;
import normallynormal.Input.Input;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

import normallynormal.Math.Direction;
import normallynormal.Settings.Keybinds;
import normallynormal.Settings.Other;
import normallynormal.Sound.AudioPlayer;
import normallynormal.World.Level;
import normallynormal.World.Levels.DevLevel;
import normallynormal.World.Levels.TutorialLevel;

public class Game {
    public static Level currentLevel;
    public static final AABB screenBoundingBox = new AABB(0, 0, ScreenConstants.PLAY_SCREEN_WIDTH, ScreenConstants.PLAY_SCREEN_HEIGHT);
    private static final long initialTime = System.currentTimeMillis();

    private static volatile double sharedTPS = 0;

    private static int levelFrameX = 30;
    private static int levelFrameY = 30;

    private static final AtomicInteger renderXOffset = new AtomicInteger(-levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH);
    private static final AtomicInteger renderYOffset = new AtomicInteger(-levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT);

    public static void run() throws IOException, FontFormatException, InterruptedException {
        InputStream fontStream = Game.class.getResourceAsStream("/resources/font/DejaVuSansMono.ttf");
        assert fontStream != null;
        Font ubuntuMono = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(14f);
        SwingTerminalFontConfiguration fontConfiguration = new SwingTerminalFontConfiguration(true, AWTTerminalFontConfiguration.BoldMode.NOTHING, ubuntuMono);
        SwingTerminalFrame terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(ScreenConstants.PLAY_SCREEN_WIDTH, ScreenConstants.PLAY_SCREEN_HEIGHT)).setTerminalEmulatorFontConfiguration(fontConfiguration).createSwingTerminal();
        terminal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        terminal.setResizable(false);

        Input input = new Input();
        terminal.addKeyListener(input);
        terminal.setTitle("ASCII Adventure");
        terminal.setSize(ScreenConstants.PLAY_SCREEN_WIDTH, ScreenConstants.PLAY_SCREEN_HEIGHT);
        terminal.setVisible(true);

        AudioPlayer.load();

        DepthScreen screen = new DepthScreen(terminal);
        screen.doResizeIfNecessary();
        screen.setCursorPosition(null);
        screen.startScreen();

        currentLevel = new DevLevel();

        DecimalFormat df = new DecimalFormat("#.##");

        Thread physicsThread = new Thread(() -> {
            long lastTime = System.nanoTime();
            double tps = 0;
            double timeSinceLastTransitionMovement = 0;
            try {
                while (true) {
                    long now = System.nanoTime();
                    double deltaSeconds = (now - lastTime) * 1.0e-9;
                    if (deltaSeconds > 1) {
                        lastTime = now;
                        continue;
                    }
                    if (deltaSeconds < 0) deltaSeconds = 0;
                    timeSinceLastTransitionMovement += deltaSeconds;

                    Direction outOfBoundsDirection = currentLevel.playerOffScreen(levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH, levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT);
                    if(outOfBoundsDirection == Direction.NONE) {
                        currentLevel.process(deltaSeconds, input);
                        timeSinceLastTransitionMovement = 0;
                    }
                    else{
                        double transitionFrequency = 1/(outOfBoundsDirection.isVertical() ? ScreenConstants.TRANSITION_SPEED_VERTICAL : ScreenConstants.TRANSITION_SPEED_HORIZONTAL);
                        if(timeSinceLastTransitionMovement > transitionFrequency) {
                            switch(outOfBoundsDirection) {
                                case UP, DOWN:
                                    renderYOffset.addAndGet(-outOfBoundsDirection.toMovement());
                                    break;
                                case LEFT, RIGHT:
                                    renderXOffset .addAndGet(-outOfBoundsDirection.toMovement());
                                    break;
                            }
                            if (renderXOffset.get() % ScreenConstants.PLAY_SCREEN_WIDTH == 0 && renderYOffset.get() % ScreenConstants.PLAY_SCREEN_HEIGHT == 0) {
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
                    lastTime = now;
                    tps = tps * 0.9 + 0.1 * (1 / deltaSeconds);
                    sharedTPS = tps;
                    if (Double.isInfinite(tps)) tps = 0;
                    if (Other.REDUCE_CPU_USAGE) Thread.sleep(5);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread renderThread = new Thread(() -> {
            float fps = ScreenConstants.TARGET_FPS;
            float overshootFPS = ScreenConstants.TARGET_FPS;
            long lastFrameTime = System.nanoTime();
            try {
                while (true) {
                    long now = System.nanoTime();
                    double deltaSeconds = (now - lastFrameTime) * 1.0e-9;
                    if (deltaSeconds < 1.0 / overshootFPS) continue;

                    screenBoundingBox.x = -renderXOffset.get();
                    screenBoundingBox.y = -renderYOffset.get();

                    screen.clear();
                    currentLevel.render(screen, renderXOffset.get(), renderYOffset.get());
                    currentLevel.applyPostShaders(screen);
                    screen.drawText(0, 0, 0, 0, Integer.MAX_VALUE, Other.VERSION_STRING, TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                    screen.drawText(0, 1, 0, 0, Integer.MAX_VALUE, "FPS: " + df.format(fps), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                    screen.drawText(0, 2, 0, 0, Integer.MAX_VALUE, "TPS: " + df.format(sharedTPS), TextColor.ANSI.WHITE, TextColor.ANSI.BLUE);
                    screen.refresh();

                    fps = (float) (fps * 0.9f + 0.1f * (1f / deltaSeconds));
                    overshootFPS = Math.max(ScreenConstants.TARGET_FPS, (ScreenConstants.TARGET_FPS - fps) + ScreenConstants.TARGET_FPS);

                    lastFrameTime = now;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        physicsThread.start();
        renderThread.start();

        while (true) {
            if (input.getKeyState(Keybinds.exit)) {
                physicsThread.interrupt();
                renderThread.interrupt();
                break;
            }
            Thread.sleep(100); // Don't spin too hard
            terminal.requestFocusInWindow();
        }

        terminal.close();
    }

    public static int gameTime() {
        return (int) ((System.currentTimeMillis() - initialTime) % Integer.MAX_VALUE);
    }
}
