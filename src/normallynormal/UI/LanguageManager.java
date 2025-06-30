package normallynormal.UI;

import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.google.gson.*;
import normallynormal.Game;
import org.tinylog.Logger;

public class LanguageManager {
    private static final Map<String, String> translations = new HashMap<>();

    public static void loadLanguage(String code) {
        try {
            InputStreamReader reader = new InputStreamReader(Objects.requireNonNull(Game.class.getResourceAsStream("/resources/lang/" + code + ".json")));
            JsonObject obj = JsonParser.parseReader(reader).getAsJsonObject();
            for (String key : obj.keySet()) {
                translations.put(key, obj.get(key).getAsString());
                Logger.info("Loaded text: " + key);
            }
        } catch (Exception e) {
            Logger.error("Failed to load language file: " + e.getMessage());
        }
    }

    public static String get(String key, Object... args) {
        String text = translations.get(key);
        if (text == null) {
            text = "MISSING_TRANSLATION: " + key;
            Logger.info("Missing translation:" + key);
        }
        return String.format(text, args);
    }
}
