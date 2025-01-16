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

import java.util.List;

public class TorchlightPostShader implements PostShader {
    private static final double[][] screenLightLevels = new double[ScreenConstants.PLAY_SCREEN_WIDTH][ScreenConstants.PLAY_SCREEN_HEIGHT];
    private static boolean firstCalc = true;
    private static boolean calcOnPlayer = false;

    public void calculate(int xOffset, int yOffset, List<Entity> entityList, List<WorldObject> worldObjects, Player player) {
        for (int x = 0; x < ScreenConstants.PLAY_SCREEN_WIDTH; x++) {
            for (int y = 0; y < ScreenConstants.PLAY_SCREEN_HEIGHT; y++) {
                if (firstCalc) {
                    screenLightLevels[x][y] = 0;
                }
                for (Entity entity : entityList) {
                    entityCircle(x, y, xOffset, yOffset, entity);
                }
                if (!calcOnPlayer) {
                    System.out.println("x");
                    entityCircle(x, y, xOffset, yOffset, player);
                    calcOnPlayer = true;
                }
            }
        }
        firstCalc = false;
    }

    public void apply(DepthScreen depthScreen) {
        firstCalc = true;
        calcOnPlayer = false;
        for (int x = 0; x < ScreenConstants.PLAY_SCREEN_WIDTH; x++) {
            for (int y = 0; y < ScreenConstants.PLAY_SCREEN_HEIGHT; y++) {
                if (screenLightLevels[x][y] >= 1) {
                    //pass
                } else if (screenLightLevels[x][y] > 0.5) {
                    depthScreen.setCharacterWithDepth(x, y, 0, 0, depthScreen.getDepth(x, y) + 1, new TextCharacter(depthScreen.getCharacterInBuffer(x, y, 0, 0).getCharacter(), TextColor.ANSI.BLACK_BRIGHT, TextColor.ANSI.BLACK));
                } else {
                    depthScreen.setCharacterWithDepth(x, y, 0, 0, depthScreen.getDepth(x, y) + 1, new TextCharacter(' ', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                }
            }
        }
    }

    private static void entityCircle(int x, int y, int xOffset, int yOffset, Entity entity) {
        if (entity instanceof GlowingEntity) {
            double radius = Math.sqrt(Math.pow(entity.getPosition().x - x, 2) + Math.pow(2 * (entity.getPosition().y - y), 2));
            double brightRadius = ((GlowingEntity) entity).glowRadius();
            double dimRadius = ((GlowingEntity) entity).glowRadius() * 1.5;
            if(x < 0 || x >= ScreenConstants.PLAY_SCREEN_WIDTH || y < 0 || y >= ScreenConstants.PLAY_SCREEN_HEIGHT) {
                return;
            }
            double lightLevel;
            if (radius <= brightRadius) {
                lightLevel = -0.1 * radius + 2;
            } else if (radius <= dimRadius) {
                lightLevel = -0.2 * radius + 3;
            } else {
                lightLevel = 0;
            }
            screenLightLevels[x][y] += lightLevel;
        }
    }
}
