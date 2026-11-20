package normallynormal.World.Format;

import normallynormal.World.Format.LevelData.ChildDef;
import normallynormal.World.Format.LevelData.ItemDef;
import normallynormal.World.Format.LevelData.RepeatDef;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Parses the ASCII Level Format. See src/resources/levels/dev_level.alf for the syntax rules.
 * Purely syntactic: it checks structure, key:value shape, and -repeat, but not which item types
 * or keys exist (that is {@link LevelRegistry} / {@link LevelBuilder}).
 */
public final class AlfParser {
    private static final int MAX_REPEAT = 100_000;
    private static final Pattern SECTION = Pattern.compile("[A-Z][A-Z_]*");
    private static final Pattern IDENT = Pattern.compile("[a-z][a-z0-9_]*");
    private static final Pattern NAME = Pattern.compile("[A-Za-z_][A-Za-z0-9_]*");

    private enum Section { META, POST_SHADERS, WORLD_OBJECTS, ENTITIES }

    private record Token(String name, String value) {}

    private AlfParser() {}

    public static LevelData parse(InputStream in, String source) throws IOException {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        in.transferTo(buffer);
        return parse(buffer.toString(StandardCharsets.UTF_8), source);
    }

    public static LevelData parse(String text, String source) {
        return new Run(source).parse(text);
    }

    /** An item whose child lines are still being collected. */
    private static final class Pending {
        final int line;
        final String type;
        final AlfArgs args;
        final List<ChildDef> children = new ArrayList<>();
        RepeatDef repeat;

        Pending(int line, String type, AlfArgs args) {
            this.line = line;
            this.type = type;
            this.args = args;
        }
    }

    private static final class Run {
        final String source;
        final List<ItemDef> postShaders = new ArrayList<>();
        final List<ItemDef> worldObjects = new ArrayList<>();
        final List<ItemDef> entities = new ArrayList<>();

        Integer formatVersion;
        double originX, originY;
        boolean hasOrigin;
        Double spawnX, spawnY;

        Section section;
        Pending pending;

        Run(String source) {
            this.source = source;
        }

        AlfException error(int line, String message) {
            return new AlfException(source, line, message);
        }

        LevelData parse(String text) {
            if (text.startsWith("﻿")) text = text.substring(1);
            String[] lines = text.split("\\R", -1);
            for (int i = 0; i < lines.length; i++) {
                parseLine(lines[i].strip(), i + 1);
            }
            finishItem();

            if (formatVersion == null) throw error(0, "missing 'format v:<n>' in META");
            if (spawnX == null) throw error(0, "missing 'spawn x:<n> y:<n>' in META");
            return new LevelData(source, formatVersion, originX, originY, spawnX, spawnY,
                    postShaders, worldObjects, entities);
        }

        void parseLine(String line, int n) {
            if (line.isEmpty() || line.startsWith("#")) return;

            if (SECTION.matcher(line).matches()) {
                finishItem();
                try {
                    section = Section.valueOf(line);
                } catch (IllegalArgumentException e) {
                    throw error(n, "unknown section '" + line + "' (expected META, POST_SHADERS, WORLD_OBJECTS or ENTITIES)");
                }
                return;
            }
            if (section == null) throw error(n, "item outside of any section: '" + line + "'");

            boolean child = line.startsWith("-");
            List<Token> tokens = tokenize(child ? line.substring(1) : line, n);
            if (tokens.isEmpty()) throw error(n, "empty '-' line");

            Token head = tokens.get(0);
            if (head.value() != null || !IDENT.matcher(head.name()).matches()) {
                throw error(n, "expected a lowercase type name, got '" + head.name()
                        + (head.value() != null ? ":" + head.value() : "") + "'");
            }
            AlfArgs args = new AlfArgs(source, n);
            for (Token t : tokens.subList(1, tokens.size())) {
                if (t.value() == null) throw error(n, "expected key:value, got '" + t.name() + "'");
                if (!IDENT.matcher(t.name()).matches()) throw error(n, "invalid key name '" + t.name() + "'");
                args.put(t.name(), t.value());
            }

            if (child) {
                addChild(head.name(), args, n);
            } else if (section == Section.META) {
                parseMeta(head.name(), args, n);
            } else {
                finishItem();
                pending = new Pending(n, head.name(), args);
            }
        }

        void addChild(String type, AlfArgs args, int n) {
            if (section == Section.META) throw error(n, "META does not take '-' lines");
            if (pending == null) throw error(n, "'-" + type + "' has no item above it to attach to");
            if (type.equals("repeat")) {
                if (pending.repeat != null) throw error(n, "item already has a -repeat");
                if (section == Section.POST_SHADERS) throw error(n, "-repeat is not allowed in POST_SHADERS");
                int count = args.integer("count");
                if (count < 1 || count > MAX_REPEAT) throw error(n, "-repeat count must be between 1 and " + MAX_REPEAT);
                double dx = args.number("dx", 0);
                double dy = args.number("dy", 0);
                args.requireAllUsed("repeat");
                pending.repeat = new RepeatDef(n, count, dx, dy);
            } else {
                pending.children.add(new ChildDef(n, type, args));
            }
        }

        void parseMeta(String type, AlfArgs args, int n) {
            switch (type) {
                case "format" -> {
                    if (formatVersion != null) throw error(n, "duplicate 'format'");
                    formatVersion = args.integer("v");
                    args.requireAllUsed("format");
                    if (formatVersion != 1) throw error(n, "unsupported format version " + formatVersion + " (this build reads 1)");
                }
                case "origin" -> {
                    if (hasOrigin) throw error(n, "duplicate 'origin'");
                    originX = args.number("x");
                    originY = args.number("y");
                    args.requireAllUsed("origin");
                    hasOrigin = true;
                }
                case "spawn" -> {
                    if (spawnX != null) throw error(n, "duplicate 'spawn'");
                    spawnX = args.number("x");
                    spawnY = args.number("y");
                    args.requireAllUsed("spawn");
                }
                default -> throw error(n, "unknown META entry '" + type + "' (expected format, origin or spawn)");
            }
        }

        void finishItem() {
            Pending p = pending;
            pending = null;
            if (p == null) return;
            if (p.repeat != null && p.args.has("id")) {
                throw error(p.repeat.line(), "an item with an id cannot be repeated (names must be unique)");
            }
            if (p.args.has("id") && !NAME.matcher(p.args.string("id")).matches()) {
                throw p.args.error("invalid id '" + p.args.string("id") + "' (use letters, digits and underscores)");
            }
            ItemDef def = new ItemDef(p.line, p.type, p.args, List.copyOf(p.children), p.repeat);
            switch (section) {
                case POST_SHADERS -> postShaders.add(def);
                case WORLD_OBJECTS -> worldObjects.add(def);
                case ENTITIES -> entities.add(def);
                default -> throw new IllegalStateException();
            }
        }

        /** Splits a line into whitespace separated `name` or `name:value` tokens; values may be "quoted". */
        List<Token> tokenize(String s, int n) {
            List<Token> out = new ArrayList<>();
            int i = 0;
            int len = s.length();
            while (i < len) {
                if (Character.isWhitespace(s.charAt(i))) { i++; continue; }

                int start = i;
                while (i < len && !Character.isWhitespace(s.charAt(i)) && s.charAt(i) != ':') i++;
                String name = s.substring(start, i);
                if (name.isEmpty()) throw error(n, "missing name before ':'");
                if (i >= len || Character.isWhitespace(s.charAt(i))) {
                    out.add(new Token(name, null));
                    continue;
                }

                i++; // ':'
                if (i >= len || Character.isWhitespace(s.charAt(i))) throw error(n, "missing value after '" + name + ":'");
                StringBuilder value = new StringBuilder();
                if (s.charAt(i) == '"') {
                    i++;
                    boolean closed = false;
                    while (i < len) {
                        char c = s.charAt(i++);
                        if (c == '\\' && i < len) {
                            value.append(s.charAt(i++));
                        } else if (c == '"') {
                            closed = true;
                            break;
                        } else {
                            value.append(c);
                        }
                    }
                    if (!closed) throw error(n, "unterminated quote in value of '" + name + "'");
                    if (i < len && !Character.isWhitespace(s.charAt(i))) {
                        throw error(n, "unexpected text after closing quote in value of '" + name + "'");
                    }
                } else {
                    int vs = i;
                    while (i < len && !Character.isWhitespace(s.charAt(i))) i++;
                    value.append(s, vs, i);
                    if (value.indexOf("\"") >= 0) throw error(n, "unexpected quote in value of '" + name + "' (quote the whole value)");
                }
                out.add(new Token(name, value.toString()));
            }
            return out;
        }
    }
}
