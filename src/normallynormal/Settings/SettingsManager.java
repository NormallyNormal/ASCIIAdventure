package normallynormal.Settings;

import com.google.gson.*;
import normallynormal.Input.BoundInput;
import normallynormal.Input.ControllerInput;
import normallynormal.Input.Input;
import normallynormal.Input.InputHandler;
import org.tinylog.Logger;

import java.io.*;
import java.nio.file.*;

public class SettingsManager {
    private static final Path SETTINGS_FILE = Paths.get("settings.json");

    public static void load() {
        if (!Files.exists(SETTINGS_FILE)) return;
        try (Reader reader = Files.newBufferedReader(SETTINGS_FILE)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            if (root.has("sound")) {
                JsonObject sound = root.getAsJsonObject("sound");
                if (sound.has("musicVolume")) Sound.MUSIC_VOLUME = sound.get("musicVolume").getAsDouble();
                if (sound.has("sfxVolume")) Sound.SFX_VOLUME = sound.get("sfxVolume").getAsDouble();
            }

            if (root.has("visual")) {
                JsonObject visual = root.getAsJsonObject("visual");
                if (visual.has("textSize")) Other.TEXT_SIZE = visual.get("textSize").getAsFloat();
                if (visual.has("targetFPS")) Other.TARGET_FPS = visual.get("targetFPS").getAsFloat();
            }

            if (root.has("other")) {
                JsonObject other = root.getAsJsonObject("other");
                if (other.has("languageCode")) Other.LANGUAGE_CODE = other.get("languageCode").getAsString();
                if (other.has("debug")) Other.DEBUG = other.get("debug").getAsBoolean();
                if (other.has("wokeMode")) Other.WOKE_MODE = other.get("wokeMode").getAsInt();
            }

            Logger.info("Settings loaded from " + SETTINGS_FILE);
        } catch (Exception e) {
            Logger.error("Failed to load settings: " + e.getMessage());
        }
    }

    public static void loadKeybinds(InputHandler inputHandler) {
        if (!Files.exists(SETTINGS_FILE)) return;
        try (Reader reader = Files.newBufferedReader(SETTINGS_FILE)) {
            JsonObject root = JsonParser.parseReader(reader).getAsJsonObject();

            if (root.has("keybinds")) {
                JsonObject keybinds = root.getAsJsonObject("keybinds");
                for (Input input : Input.values()) {
                    if (!keybinds.has(input.name())) continue;
                    BoundInput bound = inputHandler.getBoundInput(input);
                    if (bound == null) continue;
                    bound.clearKeybinds();
                    for (JsonElement key : keybinds.getAsJsonArray(input.name())) {
                        bound.addKeybind(key.getAsInt());
                    }
                }
            }

            if (root.has("controllerBinds")) {
                JsonObject controllerBinds = root.getAsJsonObject("controllerBinds");
                for (Input input : Input.values()) {
                    if (!controllerBinds.has(input.name())) continue;
                    BoundInput bound = inputHandler.getBoundInput(input);
                    if (bound == null) continue;
                    bound.clearControllerBinds();
                    for (JsonElement el : controllerBinds.getAsJsonArray(input.name())) {
                        JsonObject obj = el.getAsJsonObject();
                        bound.addControllerBind(new ControllerInput(
                                obj.get("button").getAsString(),
                                obj.get("value").getAsFloat()
                        ));
                    }
                }
            }
        } catch (Exception e) {
            Logger.error("Failed to load keybinds: " + e.getMessage());
        }
    }

    public static void save(InputHandler inputHandler) {
        JsonObject root = new JsonObject();

        JsonObject sound = new JsonObject();
        sound.addProperty("musicVolume", Sound.MUSIC_VOLUME);
        sound.addProperty("sfxVolume", Sound.SFX_VOLUME);
        root.add("sound", sound);

        JsonObject visual = new JsonObject();
        visual.addProperty("textSize", Other.TEXT_SIZE);
        visual.addProperty("targetFPS", Other.TARGET_FPS);
        root.add("visual", visual);

        JsonObject other = new JsonObject();
        other.addProperty("languageCode", Other.LANGUAGE_CODE);
        other.addProperty("debug", Other.DEBUG);
        other.addProperty("wokeMode", Other.WOKE_MODE);
        root.add("other", other);

        JsonObject keybinds = new JsonObject();
        JsonObject controllerBinds = new JsonObject();
        for (Input input : Input.values()) {
            BoundInput bound = inputHandler.getBoundInput(input);
            if (bound == null) continue;

            JsonArray keys = new JsonArray();
            for (int key : bound.getKeybinds()) keys.add(key);
            keybinds.add(input.name(), keys);

            JsonArray binds = new JsonArray();
            for (ControllerInput ci : bound.getControllerBinds()) {
                JsonObject obj = new JsonObject();
                obj.addProperty("button", ci.button());
                obj.addProperty("value", ci.value());
                binds.add(obj);
            }
            controllerBinds.add(input.name(), binds);
        }
        root.add("keybinds", keybinds);
        root.add("controllerBinds", controllerBinds);

        try (Writer writer = Files.newBufferedWriter(SETTINGS_FILE)) {
            new GsonBuilder().setPrettyPrinting().create().toJson(root, writer);
            Logger.info("Settings saved to " + SETTINGS_FILE);
        } catch (Exception e) {
            Logger.error("Failed to save settings: " + e.getMessage());
        }
    }
}
