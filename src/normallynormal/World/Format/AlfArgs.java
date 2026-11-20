package normallynormal.World.Format;

import java.util.*;

/**
 * The key:value arguments of one .alf line. Remembers which keys were read so that anything
 * left over (usually a typo) can be reported with {@link #requireAllUsed(String)}.
 */
public final class AlfArgs {
    private final String source;
    private final int line;
    private final Map<String, String> values = new LinkedHashMap<>();
    private final Set<String> used = new TreeSet<>();

    public AlfArgs(String source, int line) {
        this.source = source;
        this.line = line;
    }

    void put(String key, String value) {
        if (values.putIfAbsent(key, value) != null) {
            throw error("duplicate key '" + key + "'");
        }
    }

    public AlfException error(String message) {
        return new AlfException(source, line, message);
    }

    public boolean has(String key) {
        return values.containsKey(key);
    }

    private String raw(String key) {
        used.add(key);
        return values.get(key);
    }

    private String requiredRaw(String key) {
        String v = raw(key);
        if (v == null) throw error("missing required key '" + key + "'");
        return v;
    }

    public String string(String key) {
        return requiredRaw(key);
    }

    public String string(String key, String fallback) {
        String v = raw(key);
        return v == null ? fallback : v;
    }

    public double number(String key) {
        return parseNumber(key, requiredRaw(key));
    }

    public double number(String key, double fallback) {
        String v = raw(key);
        return v == null ? fallback : parseNumber(key, v);
    }

    public int integer(String key) {
        return toInt(key, number(key));
    }

    public int integer(String key, int fallback) {
        String v = raw(key);
        return v == null ? fallback : toInt(key, parseNumber(key, v));
    }

    public boolean bool(String key) {
        return parseBool(key, requiredRaw(key));
    }

    public boolean bool(String key, boolean fallback) {
        String v = raw(key);
        return v == null ? fallback : parseBool(key, v);
    }

    public <E extends Enum<E>> E enumValue(String key, Class<E> type) {
        return parseEnum(key, requiredRaw(key), type);
    }

    public <E extends Enum<E>> E enumValue(String key, Class<E> type, E fallback) {
        String v = raw(key);
        return v == null ? fallback : parseEnum(key, v, type);
    }

    /** Converts an already computed value (e.g. origin + x) to an int, rejecting fractions. */
    public int toInt(String what, double value) {
        if (value != Math.rint(value) || Math.abs(value) > Integer.MAX_VALUE) {
            throw error("'" + what + "' must be a whole number here, got " + value);
        }
        return (int) value;
    }

    /** Throws if any key on this line was never read. {@code context} names the item for the message. */
    public void requireAllUsed(String context) {
        for (String key : values.keySet()) {
            if (!used.contains(key)) {
                throw error("unknown key '" + key + "' for '" + context + "'"
                        + (used.isEmpty() ? "" : " (known keys: " + String.join(", ", used) + ")"));
            }
        }
    }

    private double parseNumber(String key, String text) {
        try {
            double d = Double.parseDouble(text);
            if (Double.isNaN(d) || Double.isInfinite(d)) throw new NumberFormatException();
            return d;
        } catch (NumberFormatException e) {
            throw error("key '" + key + "' expects a number, got '" + text + "'");
        }
    }

    private boolean parseBool(String key, String text) {
        if (text.equals("true")) return true;
        if (text.equals("false")) return false;
        throw error("key '" + key + "' expects true or false, got '" + text + "'");
    }

    private <E extends Enum<E>> E parseEnum(String key, String text, Class<E> type) {
        for (E constant : type.getEnumConstants()) {
            if (constant.name().equalsIgnoreCase(text)) return constant;
        }
        List<String> names = new ArrayList<>();
        for (E constant : type.getEnumConstants()) names.add(constant.name().toLowerCase());
        throw error("key '" + key + "' expects one of " + names + ", got '" + text + "'");
    }
}
