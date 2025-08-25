package normallynormal.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Input implements KeyListener {
    private final HashMap<Integer, Boolean> keyState;
    private final HashMap<Integer, Boolean> prevKeyState;

    public Input() {
        this.prevKeyState = new HashMap<>();
        this.keyState = new HashMap<>();
    }

    @Override
    public synchronized void keyTyped(KeyEvent e) {
        // Unused!
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyState.put(keyCode, true);
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyState.put(keyCode, false);
    }

    public synchronized void manualKeyPressed(int keyCode, boolean state) {
        keyState.put(keyCode, state);
    }

    public synchronized boolean getKeyState(int keyCode) {
        return keyState.getOrDefault(keyCode, false);
    }

    public synchronized boolean wasKeyJustPressed(int keyCode) {
        return getKeyState(keyCode) && !prevKeyState.getOrDefault(keyCode, false);
    }

    public synchronized void update() {
        prevKeyState.clear();
        prevKeyState.putAll(keyState);
    }
}
