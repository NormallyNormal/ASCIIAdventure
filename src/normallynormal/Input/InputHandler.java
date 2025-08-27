package normallynormal.Input;

import net.java.games.input.*;
import org.tinylog.Logger;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InputHandler implements KeyListener {
    private final HashMap<Input, Boolean> inputState;
    private final HashMap<Input, Boolean> prevInputState;

    private final List<BoundInput> boundInputs = new ArrayList<>();

    public InputHandler() {
        this.prevInputState = new HashMap<>();
        this.inputState = new HashMap<>();
        java.util.logging.Logger jinputLogger = java.util.logging.Logger.getLogger("net.java.games.input");
        jinputLogger.setLevel(java.util.logging.Level.SEVERE);
    }

    public void loadInputBinds() {
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_ESCAPE}, new ControllerInput[]{new ControllerInput("Select", 1.0f)}, Input.PAUSE));

        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_UP}, new ControllerInput[]{new ControllerInput("y", -1.0f)}, Input.UI_UP));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_DOWN}, new ControllerInput[]{new ControllerInput("y", 1.0f)}, Input.UI_DOWN));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_LEFT}, new ControllerInput[]{new ControllerInput("x", -1.0f)}, Input.UI_LEFT));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_RIGHT}, new ControllerInput[]{new ControllerInput("x", 1.0f)}, Input.UI_RIGHT));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_ENTER}, new ControllerInput[]{new ControllerInput("B", 1.0f), new ControllerInput("A", 1.0f)}, Input.UI_SELECT));

        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_ENTER}, new ControllerInput[]{new ControllerInput("Start", 1.0f)}, Input.DIALOGUE_NEXT));

        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_SPACE, KeyEvent.VK_UP}, new ControllerInput[]{new ControllerInput("A", 1.0f), new ControllerInput("B", 1.0f), new ControllerInput("y", -1.0f)}, Input.PLAYER_JUMP));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_LEFT}, new ControllerInput[]{new ControllerInput("x", -1.0f)}, Input.PLAYER_LEFT));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_RIGHT}, new ControllerInput[]{new ControllerInput("x", 1.0f)}, Input.PLAYER_RIGHT));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_DOWN}, new ControllerInput[]{new ControllerInput("y", 1.0f)}, Input.PLAYER_DOWN));
        boundInputs.add(new BoundInput(new Integer[]{KeyEvent.VK_SHIFT}, new ControllerInput[]{new ControllerInput("X", 1.0f), new ControllerInput("Y", 1.0f)}, Input.PLAYER_DASH));
    }

    @Override
    public synchronized void keyTyped(KeyEvent e) {
        // Unused!
    }

    @Override
    public synchronized void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (BoundInput boundInput : boundInputs) {
            if (boundInput.hasKeybind(keyCode)) {
                inputState.put(boundInput.getDestination(), true);
            }
        }
    }

    @Override
    public synchronized void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        for (BoundInput boundInput : boundInputs) {
            if (boundInput.hasKeybind(keyCode)) {
                inputState.put(boundInput.getDestination(), false);
            }
        }
    }

    public synchronized boolean getInputState(Input inputDestination) {
        return inputState.getOrDefault(inputDestination, false);
    }

    public synchronized void setInputState(Input inputDestination, boolean state) {
        inputState.put(inputDestination, state);
    }

    public synchronized boolean wasInputJustPressed(Input inputDestination) {
        return getInputState(inputDestination) && !prevInputState.getOrDefault(inputDestination, false);
    }

    public synchronized void update() {
        prevInputState.clear();
        prevInputState.putAll(inputState);

        Event event = new Event();

        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();

        for (Controller controller : controllers) {
            controller.poll();
            EventQueue queue = controller.getEventQueue();
            while (queue.getNextEvent(event)) {
                Component comp = event.getComponent();
                ControllerInput thisInput = new ControllerInput(comp.getName(), comp.getPollData());
                for (BoundInput boundInput : boundInputs) {
                    if (boundInput.hasControllerButton(thisInput)) {
                        setInputState(boundInput.getDestination(), boundInput.hasControllerInput(thisInput));
                    }
                }
            }
        }
    }
}
