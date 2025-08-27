package normallynormal.Input;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class BoundInput {
    private final Set<Integer> keybinds = new TreeSet<>();
    private final Set<Integer> defaultKeybinds = new TreeSet<>();;
    private final Set<ControllerInput> controllerBinds = new TreeSet<>();
    private final Set<ControllerInput> defaultControllerBinds = new TreeSet<>();
    private final Input destination;

    public BoundInput(Integer[] keybinds, ControllerInput[] controllerBinds, Input destination) {
        this.defaultKeybinds.addAll(Arrays.stream(keybinds).toList());
        this.defaultControllerBinds.addAll(Arrays.stream(controllerBinds).toList());
        this.destination = destination;
        resetKeybinds();
    }

    public void addKeybind(Integer keycode) {
        keybinds.add(keycode);
    }

    public void addControllerBind(ControllerInput input) {
        controllerBinds.add(input);
    }

    public boolean hasKeybind(Integer keycode) {
        return keybinds.contains(keycode);
    }

    public boolean hasControllerInput(ControllerInput input) {
        return controllerBinds.contains(input);
    }

    public boolean hasControllerButton(ControllerInput input) {
        boolean has = false;
        for (ControllerInput controllerInput : controllerBinds) {
            has |= controllerInput.matchesButton(input);
        }
        return has;
    }

    public Input getDestination() {
        return destination;
    }

    public void clearKeybinds() {
        keybinds.clear();
    }

    public void clearControllerBinds() {
        controllerBinds.clear();
    }

    public void clearAll() {
        clearKeybinds();
        clearControllerBinds();
    }

    public void resetKeybinds() {
        keybinds.clear();
        keybinds.addAll(defaultKeybinds);
        controllerBinds.clear();
        controllerBinds.addAll(defaultControllerBinds);
    }
}
