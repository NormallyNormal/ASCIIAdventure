package World.Entity;

import Input.Input;
import Render.DepthScreen;
import World.Game;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import Math.Vector2;

public class DashParticle extends Entity {
    double fadeTime;

    public DashParticle(Vector2 position) {
        this.position = position;
        fadeTime = 0.2;
        this.depth = -1;
    }

    @Override
    public void process(double timeDelta, Input input) {
        fadeTime -= timeDelta;
        if (fadeTime <= 0) {
            Game.currentLevel.removeEntity(this);
        }
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        boolean upperHalf = position.y % 1 > 0.75;
        boolean half_y = upperHalf || position.y % 1 < 0.25;
//        System.out.println("half:" + half_y);
//        System.out.println("upper:" + upperHalf);
        if (half_y) {
            screen.setCharacterWithDepth((int) position.x, upperHalf ? (int) position.y: (int) position.y - 1, xOffset, yOffset, depth, new TextCharacter('ˍ', TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.BLACK));
        }
        else {
            screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('-', TextColor.ANSI.BLUE_BRIGHT, TextColor.ANSI.BLACK));
        }
    }
}