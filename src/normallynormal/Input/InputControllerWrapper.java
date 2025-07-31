package normallynormal.Input;

import net.java.games.input.*;
import normallynormal.Settings.Keybinds;
import java.util.*;

public class InputControllerWrapper {
    private final Input input;
    private final ControllerInputMap map = new ControllerInputMap();

    public InputControllerWrapper(Input input) {
        this.input = input;
        java.util.logging.Logger jinputLogger = java.util.logging.Logger.getLogger("net.java.games.input");
        jinputLogger.setLevel(java.util.logging.Level.SEVERE);

        bindControllerInput("A", 1.0f, Keybinds.player_jump);
        bindControllerInput("X", 1.0f, Keybinds.player_dash);
        bindControllerInput("x", 1.0f, Keybinds.player_right);
        bindControllerInput("x", -1.0f, Keybinds.player_left);
        bindControllerInput("y", 1.0f, Keybinds.player_down);
        bindControllerInput("B", 1.0f, Keybinds.dialogue_next);
    }

    public void bindControllerInput(String name, float onValue, int keycode) {
        if (onValue == 0.0f) {
            return;
        }
        map.put(new ControllerInput(name, onValue), keycode);
        map.put(new ControllerInput(name, 0.0f), keycode);
    }

    public void poll() {
        Event event = new Event();

        Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
        for (int i = 0; i < controllers.length; i++) {
            controllers[i].poll();
            EventQueue queue = controllers[i].getEventQueue();
            while (queue.getNextEvent(event)) {
                Component comp = event.getComponent();
                //Logger.info("Controller button pressed: " + comp.getName() + " " + comp.getPollData());
                for (Integer inputVal : map.getInputs(new ControllerInput(comp.getName(), comp.getPollData()))) {
                    input.manualKeyPressed(inputVal, comp.getPollData() != 0.0f);
                }
            }
        }
    }

    record ControllerInput(String button, float value) {
    }

    static class ControllerInputMap {
        private final Map<ControllerInput, Set<Integer>> controllerToInputs = new HashMap<>();
        private final Map<Integer, Set<ControllerInput>> inputToControllers = new HashMap<>();

        // Add a mapping from controller to input
        public void put(ControllerInput controller, Integer input) {
            controllerToInputs.computeIfAbsent(controller, k -> new HashSet<>()).add(input);
            inputToControllers.computeIfAbsent(input, k -> new HashSet<>()).add(controller);
        }

        // Get all inputs mapped to a controller
        public Set<Integer> getInputs(ControllerInput controller) {
            return controllerToInputs.getOrDefault(controller, Collections.emptySet());
        }

        // Get all controllers mapped to an input
        public Set<ControllerInput> getControllers(Integer input) {
            return inputToControllers.getOrDefault(input, Collections.emptySet());
        }

        // Optional: remove a specific mapping
        public void remove(ControllerInput controller, Integer input) {
            Set<Integer> inputs = controllerToInputs.get(controller);
            if (inputs != null) {
                inputs.remove(input);
                if (inputs.isEmpty()) controllerToInputs.remove(controller);
            }

            Set<ControllerInput> controllers = inputToControllers.get(input);
            if (controllers != null) {
                controllers.remove(controller);
                if (controllers.isEmpty()) inputToControllers.remove(input);
            }
        }

        // Optional: clear all mappings for a controller
        public void removeController(ControllerInput controller) {
            Set<Integer> inputs = controllerToInputs.remove(controller);
            if (inputs != null) {
                for (Integer input : inputs) {
                    Set<ControllerInput> controllers = inputToControllers.get(input);
                    if (controllers != null) {
                        controllers.remove(controller);
                        if (controllers.isEmpty()) inputToControllers.remove(input);
                    }
                }
            }
        }
    }

}
