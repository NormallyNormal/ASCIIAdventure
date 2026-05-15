package normallynormal.Util;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;

import java.io.IOException;

/**
 * Prevents the OS from going to sleep while your game is running.
 * Works on Windows (via SetThreadExecutionState) and Linux (via systemd-inhibit).
 * On macOS, falls back to using the "caffeinate" tool.
 */
public class SleepBlocker {
    // -------------------
    // Windows part (JNA)
    // -------------------
    public interface Kernel32 extends Library {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);
        int ES_CONTINUOUS = 0x80000000;
        int ES_SYSTEM_REQUIRED = 0x00000001;
        int ES_DISPLAY_REQUIRED = 0x00000002;

        int SetThreadExecutionState(int esFlags);
    }

    private static Process inhibitorProcess = null;

    /** Call this when your game starts (or when gameplay begins) */
    public static void preventSleep() {
        if (Platform.isWindows()) {
            Kernel32.INSTANCE.SetThreadExecutionState(
                    Kernel32.ES_CONTINUOUS |
                            Kernel32.ES_SYSTEM_REQUIRED |
                            Kernel32.ES_DISPLAY_REQUIRED
            );
        } else if (Platform.isLinux()) {
            try {
                // Launch your process under systemd-inhibit
                // NOTE: This requires your game to be started via this wrapper,
                // but you can also spawn a dummy sleep process to hold inhibition.
                inhibitorProcess = new ProcessBuilder(
                        "systemd-inhibit", "--why=Game running", "sleep", "infinity"
                ).start();
            } catch (IOException e) {
                System.err.println("Could not start systemd-inhibit: " + e.getMessage());
            }
        } else if (Platform.isMac()) {
            try {
                inhibitorProcess = new ProcessBuilder("caffeinate").start();
            } catch (IOException e) {
                System.err.println("Could not start caffeinate: " + e.getMessage());
            }
        }
    }

    /** Call this when your game closes (or when sleep should be allowed again) */
    public static void allowSleep() {
        if (Platform.isWindows()) {
            Kernel32.INSTANCE.SetThreadExecutionState(Kernel32.ES_CONTINUOUS);
        } else {
            if (inhibitorProcess != null) {
                inhibitorProcess.destroy();
                inhibitorProcess = null;
            }
        }
    }
}