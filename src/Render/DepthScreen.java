package Render;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.Terminal;

import java.io.IOException;

public class DepthScreen extends TerminalScreen {
    int[][] zBuffer;
    boolean[][] written;
    public DepthScreen(Terminal terminal) throws IOException {
        super(terminal);
        TerminalSize terminalSize = terminal.getTerminalSize();
        zBuffer = new int[terminalSize.getColumns()][terminalSize.getRows()];
        written = new boolean[terminalSize.getColumns()][terminalSize.getRows()];
    }

    @Override
    public synchronized TerminalSize doResizeIfNecessary() {
        TerminalSize newSize = super.doResizeIfNecessary();
        if(newSize != null) {
            zBuffer = new int[newSize.getColumns()][newSize.getRows()];
            written = new boolean[newSize.getColumns()][newSize.getRows()];
        }
        return newSize;
    }

    public synchronized void setCharacterWithDepth(int column, int row, int xOffset, int yOffset, int depth, TextCharacter screenCharacter) {
        column += xOffset;
        row += yOffset;
        if(column < 0 || column >= zBuffer.length || row < 0 || row >= zBuffer[0].length) {
            return;
        }
        if(zBuffer[column][row] <= depth) {
            super.setCharacter(column, row, screenCharacter);
            zBuffer[column][row] = depth;
            written[column][row] = true;
        }
    }

    public synchronized TextCharacter getCharacterInBuffer(int column, int row, int xOffset, int yOffset) {
        column += xOffset;
        row += yOffset;
        if(column < 0 || column >= zBuffer.length || row < 0 || row >= zBuffer[0].length) {
            return new TextCharacter(' ', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK);
        }
        return getBackCharacter(column, row);
    }

    public synchronized void drawText(int column, int row, int xOffset, int yOffset, int depth, String string, TextColor foregroundColor, TextColor backgroundColor) {
        for (int i = 0; i < string.length(); i++) {
            setCharacterWithDepth(column + i, row, xOffset, yOffset, depth, new TextCharacter(string.charAt(i), foregroundColor, backgroundColor));
        }
    }

    TextCharacter blank = new TextCharacter(' ', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK);

    @Override
    public synchronized void clear() {
        for (int x = 0; x < written.length; x++) {
            for (int y = 0; y < written[0].length; y++) {
                if (written[x][y]) {
                    super.setCharacter(x, y, blank);
                    zBuffer[x][y] = Integer.MIN_VALUE;
                }
                written[x][y] = false;
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
