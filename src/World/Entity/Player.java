package World.Entity;

import Input.Input;
import Render.DepthScreen;
import com.googlecode.lanterna.TextCharacter;
import com.googlecode.lanterna.TextColor;
import Math.Vector2;
import Math.AABB;

import java.awt.event.KeyEvent;

public class Player extends Entity{
    double timeDead;
    double jumpBuffer = 0;
    public Player() {
        timeDead = 0;
        velocity = new Vector2(0,0);
        position = new Vector2(5.5, 5.5);
        depth = 10;
        gravity = new Vector2(0, 60);
        collisionBox = new AABB(position.x - 0.49, position.y - 0.49, 0.98, 0.98);
    }

    @Override
    public void process(double timeDelta, Input input) {
        if (!dead) {
            if (jumpBuffer > 0) {
                jumpBuffer -= timeDelta;
            }
            if (input.getKeyState(KeyEvent.VK_UP)) {
                jumpBuffer = 0.2;
            }
            else if (velocity.y < 0) {
                velocity.y = 0;
            }
            if (jumpBuffer > 0 && timeSinceOnGround < 0.1) {
                velocity.y = -30;
                timeSinceOnGround = Double.POSITIVE_INFINITY;
            }
            velocity.x = 0;
            if (input.getKeyState(KeyEvent.VK_LEFT)) {
                velocity.x -= 20;
            }
            if (input.getKeyState(KeyEvent.VK_RIGHT)) {
                velocity.x += 20;
            }
        }
        else {
            timeDead += timeDelta;
            if(timeDead > 1) {
                dead = false;
                position = new Vector2(5.5,5.5);
                gravity = new Vector2(0, 60);
                timeDead = 0;
            }
        }
        super.process(timeDelta, input);
    }

    @Override
    public void render(DepthScreen screen, int xOffset, int yOffset) {
        Vector2 position = Vector2.add(this.position, new Vector2(0.5, 0.5));
        boolean half_x = position.x % 1 > 0.75 || position.x % 1 < 0.25;
        boolean half_y = position.y % 1 > 0.75 || position.y % 1 < 0.25;

        double round_x = Math.round(position.x);
        double round_y = Math.round(position.y);

        if (dead) {
            if (timeDead < 0.15)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            else if (timeDead < 0.3)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▓', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            else if (timeDead < 0.45)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▒', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            else if (timeDead < 0.6)
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('░', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
        }
        else {
            if (!half_x && !half_y) {
                screen.setCharacterWithDepth((int) position.x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('█', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            } else if (half_x && !half_y) {
                screen.setCharacterWithDepth((int) round_x - 1, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▐', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x, (int) position.y, xOffset, yOffset, depth, new TextCharacter('▌', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            } else if (!half_x) {
                screen.setCharacterWithDepth((int) position.x, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▄', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) position.x, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▀', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            } else {
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▗', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x, (int) round_y - 1, xOffset, yOffset, depth, new TextCharacter('▖', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x - 1, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▝', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
                screen.setCharacterWithDepth((int) round_x, (int) round_y, xOffset, yOffset, depth, new TextCharacter('▘', TextColor.ANSI.WHITE, TextColor.ANSI.BLACK));
            }
        }
    }

    @Override
    public void kill() {
        super.kill();
    }
}
