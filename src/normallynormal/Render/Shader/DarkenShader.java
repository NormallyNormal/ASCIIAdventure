package normallynormal.Render.Shader;

import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import normallynormal.Constants.ScreenConstants;
import normallynormal.Render.DepthScreen;
import normallynormal.World.Entity.Entity;
import normallynormal.World.Entity.Player;
import normallynormal.World.Platform.WorldObject;

import java.util.List;

public class DarkenShader implements PostShader {
    private static final TextColor dark = new TextColor.RGB(35, 35, 35);

    public void apply(DepthScreen depthScreen, int xOffset, int yOffset, List<Entity> entityList, List<WorldObject> worldObjects, Player player) {
        for (int x = 0; x < ScreenConstants.PLAY_SCREEN_WIDTH; x++) {
            for (int y = 0; y < ScreenConstants.PLAY_SCREEN_HEIGHT; y++) {
                depthScreen.setCharacterWithDepth(x, y, 0, 0, depthScreen.getDepth(x, y) + 1, new TextCharacter(depthScreen.getCharacterInBuffer(x, y, 0, 0).getCharacter(), dark, TextColor.ANSI.BLACK));
            }
        }
    }
}
