package Render.Shader;

import Constants.ScreenConstants;
import Render.DepthScreen;
import World.Entity.Entity;
import World.Entity.GlowingEntity;
import World.Entity.Player;
import World.Level;
import World.Platform.WorldObject;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import Math.Mth;

import java.util.List;

public class TorchlightPostShader implements PostShader {

    private static final double[][] screenLightLevels = new double[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];

    public void apply(DepthScreen depthScreen, int xOffset, int yOffset, List<Entity> entityList, List<WorldObject> worldObjects, Player player) {
        calculate(xOffset, yOffset, entityList, player);
        for (int x = 0; x < ScreenConstants.PLAY_SCREEN_WIDTH; x++) {
            for (int y = 0; y < ScreenConstants.PLAY_SCREEN_HEIGHT; y++) {
                if (screenLightLevels[x][y] < 1) {
                    if (screenLightLevels[x][y] > 0.5) {
                        depthScreen.setCharacterWithDepth(x, y, 0, 0, depthScreen.getDepth(x, y) + 1, new TextCharacter(depthScreen.getCharacterInBuffer(x, y, 0, 0).getCharacter(), TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK));
                    } else {
                        depthScreen.setCharacterWithDepth(x, y, 0, 0, depthScreen.getDepth(x, y) + 1, new TextCharacter(' ', TextColor.ANSI.BLACK, TextColor.ANSI.BLACK));
                    }
                }
            }
        }
    }

    private void calculate(int xOffset, int yOffset, List<Entity> entityList, Player player) {
        for (int x = 0; x < ScreenConstants.PLAY_SCREEN_WIDTH; x++) {
            for (int y = 0; y < ScreenConstants.PLAY_SCREEN_HEIGHT; y++) {
                screenLightLevels[x][y] = 0;
                entityCircle(x, y, xOffset, yOffset, player);
                for (Entity entity : entityList) {
                    entityCircle(x, y, xOffset, yOffset, entity);
                }
            }
        }
    }

    private static void entityCircle(int x, int y, int xOffset, int yOffset, Entity entity) {
        if (entity instanceof GlowingEntity) {
            double radius = Mth.screenDistance(entity.getPosition().x + xOffset - x, entity.getPosition().y + yOffset - y);
            double brightRadius = ((GlowingEntity) entity).glowRadius();
            if(x < 0 || x >= ScreenConstants.PLAY_SCREEN_WIDTH || y < 0 || y >= ScreenConstants.PLAY_SCREEN_HEIGHT) {
                return;
            }
            double lightLevel = -(1/brightRadius) * radius + 2;
            lightLevel = Math.max(lightLevel, 0);
            screenLightLevels[x][y] += lightLevel;
        }
    }
}
