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

import normallynormal.Settings.Sound;
import org.tinylog.Logger;

public class AudioPlayer {
    static Map<String, Clip> audio = new HashMap<>();

    public static void load() {
        try {
            InputStream indexStream = Game.class.getResourceAsStream("/resources/sounds/sounds.txt");
            if (indexStream == null) {
                Logger.error("Missing sounds.txt");
                return;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(indexStream));
            String path;
            while ((path = reader.readLine()) != null) {
                try {
                    String key = path.replace("/", ".").replaceAll("\\.wav$", "");
                    URL audioURL = Game.class.getResource("/resources/sounds/" + path);
                    if (audioURL == null) {
                        Logger.error("Missing sound: " + path);
                        System.err.println();
                        continue;
                    }

                    AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioURL);
                    Clip clip = AudioSystem.getClip();
                    clip.open(audioStream);
                    audio.put(key, clip);
                    Logger.info("Loaded sound: " + key);
                } catch (Exception e) {
                    Logger.error("Failed to load sound: " + path + ", caused by: " + e.getMessage());
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
            playWithVolume(clip, getVolumeForCategory(audioName));
        } else {
            System.err.println("Audio not found: " + audioName);
        }
    }

    private static double getVolumeForCategory(String audioName) {
        int dotIndex = audioName.indexOf('.');
        String audioType = (dotIndex != -1) ? audioName.substring(0, dotIndex) : audioName;
        if (audioType.equals("player")) {
            return Sound.PLAYER_VOLUME;
        }
        return 1;
    }

    private static void playWithVolume(Clip clip, double volume) {
        clip.setFramePosition(0);
        if (volume < 0f || volume > 1f)
            throw new IllegalArgumentException("Volume not valid: " + volume);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(20f * (float) Math.log10(volume));
        clip.start();
    }
}

