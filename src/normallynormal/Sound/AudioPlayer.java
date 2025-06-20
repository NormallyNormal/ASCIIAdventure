package normallynormal.Sound;

import normallynormal.Game;

import javax.sound.sampled.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class AudioPlayer {
    static Map<String, Clip> audio = new HashMap<>();

    public static void load() {
        try {
            InputStream indexStream = Game.class.getResourceAsStream("/resources/sounds/sounds.txt");
            if (indexStream == null) {
                System.err.println("Missing sounds.txt");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(indexStream));
            String path;
            while ((path = reader.readLine()) != null) {
                try {
                    String key = path.replace("/", ".").replaceAll("\\.wav$", "");
                    URL audioURL = Game.class.getResource("/resources/sounds/" + path);
                    if (audioURL == null) {
                        System.err.println("Missing: " + path);
                        continue;
                    }

                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioURL);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    audio.put(key, clip);
                    System.out.println("Loaded: " + key);
                } catch (Exception e) {
                    System.err.println("Failed to load " + path + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void play(String audioName) {
        Clip clip = audio.get(audioName);
        if (clip != null) {
            if (clip.isRunning()) clip.stop();
            clip.setFramePosition(0);
            clip.start();
        } else {
            System.err.println("Audio not found: " + audioName);
        }
    }
}

