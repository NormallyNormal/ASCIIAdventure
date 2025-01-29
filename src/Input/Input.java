package Input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.HashSet;

public class Input implements KeyListener {
    private final HashMap<Integer, Boolean> keyState;
    private final HashSet<Integer> keyPressed;

    public Input() {
        keyState = new HashMap<>();
        keyPressed = new HashSet<>();
    }

    public void newFrame() {
        keyPressed.clear();
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // Not used in this example
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyState.put(keyCode, true);
        keyPressed.add(keyCode);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        keyState.put(keyCode, false);
    }

    public boolean getKeyState(int keyCode) {
        return keyState.get(keyCode) != null ? keyState.get(keyCode) : false;
    }

    public boolean getKeyPressed(int keycode) {
        return keyPressed.contains(keycode);
    }
}
