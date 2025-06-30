package normallynormal;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.AWTTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFrame;
import normallynormal.Constants.ScreenConstants;
import normallynormal.Input.Input;
import normallynormal.Math.AABB;
import normallynormal.Render.DepthScreen;
import normallynormal.Settings.Other;
import normallynormal.Sound.AudioPlayer;
import normallynormal.UI.LanguageManager;
import normallynormal.World.Level;
import normallynormal.World.Levels.DevLevel;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager {
    public static Level currentLevel;
    public static final AABB screenBoundingBox = new AABB(0, 0, ScreenConstants.PLAY_SCREEN_WIDTH, ScreenConstants.PLAY_SCREEN_HEIGHT);
    private static final long initialTime = System.currentTimeMillis();

    static volatile double sharedTPS = 0;

    static int levelFrameX = 30;
    static int levelFrameY = 30;

    static final AtomicInteger renderXOffset = new AtomicInteger(-levelFrameX * ScreenConstants.PLAY_SCREEN_WIDTH);
    static final AtomicInteger renderYOffset = new AtomicInteger(-levelFrameY * ScreenConstants.PLAY_SCREEN_HEIGHT);

    static final Input input = new Input();

    static DepthScreen screen;

    static SwingTerminalFrame terminal;

    public static void prepareScreen() throws IOException, FontFormatException {
        InputStream fontStream = Game.class.getResourceAsStream("/resources/font/DejaVuSansMono.ttf");
        assert fontStream != null;
        Font ubuntuMono = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(Other.TEXT_SIZE);
        SwingTerminalFontConfiguration fontConfiguration = new SwingTerminalFontConfiguration(true, AWTTerminalFontConfiguration.BoldMode.NOTHING, ubuntuMono);
        terminal = new DefaultTerminalFactory().setInitialTerminalSize(new TerminalSize(ScreenConstants.PLAY_SCREEN_WIDTH, ScreenConstants.PLAY_SCREEN_HEIGHT)).setTerminalEmulatorFontConfiguration(fontConfiguration).createSwingTerminal();
        terminal.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        terminal.setResizable(false);

        terminal.addKeyListener(input);
        terminal.setTitle("Loading...");
        terminal.setSize(ScreenConstants.PLAY_SCREEN_WIDTH, ScreenConstants.PLAY_SCREEN_HEIGHT);
        terminal.setVisible(true);

        screen = new DepthScreen(terminal);
        screen.doResizeIfNecessary();
        screen.setCursorPosition(null);
        screen.startScreen();
    }

    public static void prepareGame() {
        AudioPlayer.load();
        LanguageManager.loadLanguage(Other.LANGUAGE_CODE);
        currentLevel = new DevLevel();
        terminal.setTitle(LanguageManager.get("game.title"));
    }

    public static int gameTime() {
        return (int) ((System.currentTimeMillis() - initialTime) % Integer.MAX_VALUE);
    }
}
