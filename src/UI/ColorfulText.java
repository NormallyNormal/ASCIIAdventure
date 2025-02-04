package UI;

import com.googlecode.lanterna.TextColor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ColorfulText {
    String text;
    List<TextColor> textColors = new ArrayList<>();

    Pattern rgbPattern = Pattern.compile("<(\\d{1,3},\\d{1,3},\\d{1,3})>");
    Pattern namePattern = Pattern.compile("<([A-Z]+)>");
    Pattern pattern = Pattern.compile("<(\\d{1,3},\\d{1,3},\\d{1,3}|[A-Z]+)>");

    public ColorfulText(String string) {
        StringBuilder buildText = new StringBuilder();
        Matcher matcher = pattern.matcher(string);

        String[] spilt = pattern.split(string);
        TextColor currentColor = TextColor.ANSI.WHITE;
        Stream<MatchResult> stream = matcher.results();
        Iterator<MatchResult> iterator = stream.iterator();
        for (String s : spilt) {
            buildText.append(s);
            for (int i = 0; i < s.length(); i++) {
                textColors.add(currentColor);
            }
            if(iterator.hasNext()) {
                currentColor = stringToTextColor(iterator.next().group());
            }
        }

        text = buildText.toString();
    }

    public char charAt(int index){
        return text.charAt(index);
    }

    public TextColor colorAt(int index) {
        return textColors.get(index);
    }

    public int length() {
        return text.length();
    }

    private TextColor stringToTextColor(String string) {
        Matcher matcher = rgbPattern.matcher(string);
        if (matcher.matches()) {
            int[] rgb = extractRGB(string);
            if (rgb == null) {
                return TextColor.ANSI.DEFAULT;
            }
            return new TextColor.RGB(rgb[0], rgb[1], rgb[2]);
        }
        matcher = namePattern.matcher(string);
        if (matcher.matches()) {
            String trimmed = string.substring(1, string.length() - 1);
            return switch (trimmed) {
                case "BLACK" -> TextColor.ANSI.BLACK;
                case "RED" -> TextColor.ANSI.RED;
                case "GREEN" -> TextColor.ANSI.GREEN;
                case "YELLOW" -> TextColor.ANSI.YELLOW;
                case "BLUE" -> TextColor.ANSI.BLUE;
                case "MAGENTA" -> TextColor.ANSI.MAGENTA;
                case "CYAN" -> TextColor.ANSI.CYAN;
                case "WHITE" -> TextColor.ANSI.WHITE;
                case "BLACK_BRIGHT" -> TextColor.ANSI.BLACK_BRIGHT;
                case "RED_BRIGHT" -> TextColor.ANSI.RED_BRIGHT;
                case "GREEN_BRIGHT" -> TextColor.ANSI.GREEN_BRIGHT;
                case "YELLOW_BRIGHT" -> TextColor.ANSI.YELLOW_BRIGHT;
                case "BLUE_BRIGHT" -> TextColor.ANSI.BLUE_BRIGHT;
                case "MAGENTA_BRIGHT" -> TextColor.ANSI.MAGENTA_BRIGHT;
                case "CYAN_BRIGHT" -> TextColor.ANSI.CYAN_BRIGHT;
                case "WHITE_BRIGHT" -> TextColor.ANSI.WHITE_BRIGHT;
                default -> TextColor.ANSI.DEFAULT;
            };
        }
        return TextColor.ANSI.DEFAULT;
    }

    public static int[] extractRGB(String input) {
        Pattern rgbPattern = Pattern.compile("<(\\d{1,3}),\\s*(\\d{1,3}),\\s*(\\d{1,3})>");
        Matcher matcher = rgbPattern.matcher(input);

        if (matcher.find()) {
            int r = Integer.parseInt(matcher.group(1));
            int g = Integer.parseInt(matcher.group(2));
            int b = Integer.parseInt(matcher.group(3));
            return new int[]{r, g, b};
        }
        return null; // Return null if no match found
    }
}
