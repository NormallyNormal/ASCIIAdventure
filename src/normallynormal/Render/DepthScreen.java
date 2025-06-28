package normallynormal.Render;

import normallynormal.Constants.ScreenConstants;
import normallynormal.Math.Line;
import normallynormal.UI.ColorfulText;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class DepthScreen extends TerminalScreen {
    int[][] zBuffer;
    boolean[][] written;
    boolean[][] backgroundAlpha;
    public DepthScreen(Terminal terminal) throws IOException {
        super(terminal);
        zBuffer = new int[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];
        written = new boolean[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];
        backgroundAlpha = new boolean[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];
        fullClear();
    }

    @Override
    public TerminalSize doResizeIfNecessary() {
        TerminalSize newSize = super.doResizeIfNecessary();
        if(newSize != null) {
            zBuffer = new int[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];
            written = new boolean[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];
            backgroundAlpha = new boolean[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];
            fullClear();
        }
        return newSize;
    }

    public void setCharacterWithDepth(int column, int row, int xOffset, int yOffset, int depth, TextCharacter screenCharacter) {
        column += xOffset;
        row += yOffset;
        boolean transparentBackgroundFlag = screenCharacter.getBackgroundColor() instanceof TransparentColor;
        if (transparentBackgroundFlag) {
            screenCharacter = new TextCharacter(screenCharacter.getCharacter(), screenCharacter.getForegroundColor(), this.getCharacterInBuffer(column, row, 0, 0).getBackgroundColor());
        }
        if(column < 0 || column >= zBuffer.length || row < 0 || row >= zBuffer[0].length) {
            return;
        }
        if(zBuffer[column][row] <= depth) {
            super.setCharacter(column, row, screenCharacter);
            zBuffer[column][row] = depth;
            written[column][row] = true;
            if (!transparentBackgroundFlag) {
                backgroundAlpha[column][row] = false;
            }
        }
        else if(backgroundAlpha[column][row] && !transparentBackgroundFlag) {
            written[column][row] = true;
            super.setCharacter(column, row, this.getCharacterInBuffer(column, row, 0, 0).withBackgroundColor(screenCharacter.getBackgroundColor()));
        }
    }

    public void drawLine(int column1, int row1, int column2, int row2, int xOffset, int yOffset, int depth, TextColor color) {
        TextCharacter[] screenCharacters = {
                new TextCharacter('○', color, TransparentColor.TRANSPARENT)
        };
        drawLine(column1, row1, column2, row2, xOffset, yOffset, depth, screenCharacters);
    }

    public void drawLine(int column1, int row1, int column2, int row2, int xOffset, int yOffset, int depth, TextCharacter[] screenCharacters) {
        List<int[]> points = Line.drawLine(column1, row1, column2, row2);
        for (int i = 0; i < points.size(); i++) {
            int [] cur = points.get(i);
            setCharacterWithDepth(cur[0], cur[1], xOffset, yOffset, depth, screenCharacters[i % screenCharacters.length]);
        }
    }

    public TextCharacter getCharacterInBuffer(int column, int row, int xOffset, int yOffset) {
        column += xOffset;
        row += yOffset;
        if(column < 0 || column >= zBuffer.length || row < 0 || row >= zBuffer[0].length) {
            return new TextCharacter(' ', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK);
        }
        TextCharacter character = getBackCharacter(column, row);
        return Objects.requireNonNullElseGet(character, () -> new TextCharacter(' ', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK));
    }

    public void drawText(int column, int row, int xOffset, int yOffset, int depth, String string, TextColor foregroundColor, TextColor backgroundColor) {
        for (int i = 0; i < string.length(); i++) {
            setCharacterWithDepth(column + i, row, xOffset, yOffset, depth, new TextCharacter(string.charAt(i), foregroundColor, backgroundColor));
        }
    }

    public void drawTextAdvanced(int column, int row, int xOffset, int yOffset, int depth, ColorfulText text, TextColor foregroundColor, TextColor backgroundColor, int rowLength, int maxChars) {
        for (int i = 0; i < text.length() && i < maxChars; i++) {
            setCharacterWithDepth((column + i % (rowLength)), row + (i / rowLength), xOffset, yOffset, depth, new TextCharacter(text.charAt(i), text.colorAt(i), backgroundColor));
        }
    }

    TextCharacter blank = new TextCharacter(' ', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);

    @Override
    public void clear() {
        for (int x = 0; x < written.length; x++) {
            for (int y = 0; y < written[0].length; y++) {
                super.setCharacter(x, y, blank);
                written[x][y] = false;
                zBuffer[x][y] = Integer.MIN_VALUE;
                backgroundAlpha[x][y] = true;
            }
        }
    }

    public void fullClear() {
        for (int x = 0; x < written.length; x++) {
            for (int y = 0; y < written[0].length; y++) {
                super.setCharacter(x, y, blank);
                zBuffer[x][y] = Integer.MIN_VALUE;
                written[x][y] = false;
                backgroundAlpha[x][y] = true;
            }
        }
    }

    public int getDepth(int x, int y) {
        return zBuffer[x][y];
    }

    public boolean isWritten(int x, int y) {
        return written[x][y];
    }
}
