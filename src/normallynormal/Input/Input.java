package normallynormal.Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;

public class Input implements KeyListener {
    private final HashMap<Integer, Boolean> keyState;

    public Input() {
        keyState = new HashMap<>();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused!
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyState.put(keyCode, true);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyState.put(keyCode, false);
    }

    public void manualKeyPressed(int keyCode, boolean state) {
        keyState.put(keyCode, state);
    }

    public boolean getKeyState(int keyCode) {
        return keyState.get(keyCode) != null ? keyState.get(keyCode) : false;
    }
}
