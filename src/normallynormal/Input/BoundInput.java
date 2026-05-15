package normallynormal.Input;

import java.awt.event.KeyEvent;
import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

    public void setSingleKeybind(int keyCode) {
        keybinds.clear();
        keybinds.add(keyCode);
    }

    public void setSingleControllerBind(ControllerInput ci) {
        controllerBinds.clear();
        controllerBinds.add(ci);
    }

    public String getKeybindText() {
        if (keybinds.isEmpty()) return "None";
        return keybinds.stream().map(KeyEvent::getKeyText).collect(Collectors.joining(", "));
    }

    public String getControllerBindText() {
        if (controllerBinds.isEmpty()) return "None";
        return controllerBinds.stream().map(ControllerInput::getDisplayText).collect(Collectors.joining(", "));
    }

    public Set<Integer> getKeybinds() {
        return keybinds;
    }

    public Set<ControllerInput> getControllerBinds() {
        return controllerBinds;
    }
}
