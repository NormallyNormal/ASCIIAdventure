package normallynormal.World.Format;

/** Thrown for any problem reading or building an .alf level. The message is "source:line: problem". */
public class AlfException extends RuntimeException {
    public AlfException(String source, int line, String message) {
        super(source + (line > 0 ? ":" + line : "") + ": " + message);
    }

    public AlfException(String source, String message, Throwable cause) {
        super(source + ": " + message, cause);
    }
}
